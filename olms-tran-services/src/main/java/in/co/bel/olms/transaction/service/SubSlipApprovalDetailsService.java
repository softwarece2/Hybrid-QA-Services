package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.Status;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.dto.SubSlipApprovalDto;
import in.co.bel.olms.transaction.dto.SubSlipSerialNoDto;
import in.co.bel.olms.transaction.entity.InspectionFileNote;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlApprovalDetails;
import in.co.bel.olms.transaction.entity.OlApprovalUploads;
import in.co.bel.olms.transaction.entity.SubSlipAllotment;
import in.co.bel.olms.transaction.entity.SubSlipApprovalDetails;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.SubSlipAllotmentRepository;
import in.co.bel.olms.transaction.repository.SubSlipApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.transaction.repository.insp.InspectionFileNoteRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubSlipApprovalDetailsService {

    @Value("${file.domain-url}")
    private String domainUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private final OlApprovalDetailsRepository olApprovalRepository;

    private final FileStorageService fileStorageService;

    private final OfferLetterRepository olRepository;

    private final EmployeeRepository empRepository;

    private final OlStatusRepository olStatusRepository;

    private final StatusRepository statusRepository;

    private final MergeSubSlipService mergeSSlipService;

    private final NotificationService notificationService;

    private final WSService wsService;

    private final Mapper mapper;

    private final SubmissionSlipRepository subSlipRepository;

    private final SubSlipApprovalDetailsRepository subSlipApprovalDetailsRepository;

    private final SubSlipAllotmentRepository subSlipAllotmentRepository;

    private final InspectionFileNoteRepository inspectionFileNoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubSlipApprovalDetailsService.class);

    private static final String NOTIFICATION_INSPECTION_FILE_NOTE_RECEIVED = "Inspection File Note received for approval";
    
    public OlApprovalDto updateOlApproval(OlApprovalDto olApprovalDto, MultipartFile[] uploads) {

        try {
            if (olApprovalDto != null) {
                Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
                if (empOptional.isPresent()) {
                    OlApprovalDetails approvalDetails = mapper.map(olApprovalDto, OlApprovalDetails.class);
                    List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                    String sbuName = null;
                    if (olApprovalDto.getNextApproverId() == 0) {
                        approvalDetails.setNextApprover(null);
                        // Updating the OL status to 1 if CQAO approves Inspection File Note
                        Optional<OfferLetter> olOptional = olRepository.findById(olApprovalDto.getOfferLetterId());
                        if (olOptional.isPresent()) {
                            OfferLetter offerLetter = olOptional.get();
                            OlStatus olApprovedStatus = olStatusRepository.findById(ApplicationConstants.OL_APPROVED)
                                    .get();
                            offerLetter.setOlStatus(olApprovedStatus);
                            olRepository.save(offerLetter);
                            sbuName = offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
                        }
                        List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
                        receivers = employees.stream().map(Employee::getId).toList();
                    }
                    approvalDetails.setCreatedDate(new Date());
                    // creating a notification for the CQAE user
                    for (Integer receiverId : receivers) {
                        NotificationDto notificationDto = new NotificationDto();
                        notificationDto.setNotificationName(NOTIFICATION_INSPECTION_FILE_NOTE_RECEIVED);
                        notificationDto.setNotificationCategory("OL");
                        notificationDto.setCategoryId(olApprovalDto.getOfferLetterId());
                        notificationDto.setSenderId(olApprovalDto.getCreatedBy());
                        notificationDto.setReceiverId(receiverId);
                        notificationDto.setSbuName(sbuName);
                        notificationService.saveNotification(notificationDto);
                    }

                    OlApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
                    MessageBean message = new MessageBean();
                    message.setReceivers(receivers);
                    message.setSender(olApprovalDto.getCreatedBy());
                    message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
                    message.setMessage(NOTIFICATION_INSPECTION_FILE_NOTE_RECEIVED);
                    wsService.notifyCqae(message);
                    List<String> uploadedFiles = new ArrayList<>();
                    if (uploads != null && uploads.length > 0) {
                        List<String> uploadedFilesUrl = new ArrayList<>();
                        for (int i = 0; i < uploads.length; i++) {
                            String fileName = fileStorageService.storeFile(uploads[i], savedOlApproval.getId(), i);
                            if (StringUtils.isNotBlank(fileName)
                                    && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
                                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR
                                        + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
                                uploadedFilesUrl.add(fileDownloadUri);
                                uploadedFiles.add(fileName);
                            }
                        }
                        olApprovalDto.setUploadedFiles(uploadedFilesUrl);
                        updateOlApprovalWithUploadInfo(uploadedFiles, savedOlApproval);
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("Error occured while updating offer letter approval ", e);
            logger.error("Error in updating offer letter approval {}", e.getMessage());
        }
        return olApprovalDto;

    }

    // updating the OL approval uploads table for the uploaded files
    private void updateOlApprovalWithUploadInfo(List<String> uploadedFiles, OlApprovalDetails olApproval) {

        List<OlApprovalUploads> approvalUploadsList = new ArrayList<>();
        for (String fileName : uploadedFiles) {
            OlApprovalUploads approvalUploads = new OlApprovalUploads();
            approvalUploads.setFileName(fileName);
            approvalUploadsList.add(approvalUploads);
            approvalUploads.setOlApprovalDetails(olApproval);
        }
        olApproval.setOlApprovalUploads(approvalUploadsList);
        olApprovalRepository.save(olApproval);

    }

    public SoApprovalDto getOlApprovalDetailsByOlId(Integer subSlipId, Integer empId) {

        SoApprovalDto soApprovalDto = new SoApprovalDto();
        SubmissionSlip subSlip = subSlipRepository.findById(subSlipId)
        		.orElseThrow(() -> new IllegalArgumentException("Submission slip not found for id: " + subSlipId));
        Integer eligibleApproverId = subSlipApprovalDetailsRepository.getEligibleApprover(subSlipId);
        Integer empRoleId = empRepository.getEmpRoleByEmpId(empId);
        boolean cqaoApprovalStatus = subSlipApprovalDetailsRepository.getLatestApprovalStatusByOlId(subSlipId);
        
        if (cqaoApprovalStatus) {
            soApprovalDto.setIsEditAllowed(false);
        } else {
            if ((eligibleApproverId == null && empRoleId == ApplicationConstants.INSPECTOR)
                    || eligibleApproverId != null && eligibleApproverId.intValue() == empId) {
                soApprovalDto.setIsEditAllowed(true);
            } else {
                soApprovalDto.setIsEditAllowed(false);
            }
        }

        List<SubSlipApprovalDto> approvalDtos = new ArrayList<>();
        Set<SubSlipApprovalDetails> approvalDetails = subSlip.getSubSlipApprovalDetails();
        for (SubSlipApprovalDetails olApprovalDetails : approvalDetails) {
            SubSlipApprovalDto approvalDto = mapper.map(olApprovalDetails, SubSlipApprovalDto.class);
            getInspectionFiles(approvalDto, olApprovalDetails);
            approvalDtos.add(approvalDto);
        }
        
        Collections.sort(approvalDtos, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        soApprovalDto.setSSlipApprovalDtos(approvalDtos);
        return soApprovalDto;
    
    }

    private SubSlipApprovalDto getInspectionFiles(SubSlipApprovalDto olApprovalDto, SubSlipApprovalDetails approvalDetails) {

        List<String> uploadedFilesUrl = new ArrayList<>();
        try {
            List<OlApprovalUploads> approvalUploads = approvalDetails.getOlApprovalUploads();
            for (OlApprovalUploads olApprovalUpload : approvalUploads) {

                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
                        + olApprovalUpload.getFileName();
                uploadedFilesUrl.add(fileDownloadUri);

            }
            olApprovalDto.setUploadedFiles(uploadedFilesUrl);
        } catch (Exception e) {
            logger.error("Could not set uploaded files in ol approval fetch{}", e.getMessage());
        }
        return olApprovalDto;

    }

    public SubSlipApprovalDto updateFileNoteApproval(SubSlipApprovalDto sSlipApprovalDto, MultipartFile[] uploads, String role) {
        try {
            if (sSlipApprovalDto != null) {
                Optional<Employee> empOptional = empRepository.findById(sSlipApprovalDto.getCreatedBy());
                if (empOptional.isPresent()) {
                    OlApprovalDetails approvalDetails = mapper.map(sSlipApprovalDto, OlApprovalDetails.class);
//                    int fileNoteId = sSlipApprovalDto.getInspectionFileNoteId();
//                    List<Integer> offerLetterIds = offerLetterRepository.getOLIds(fileNoteId);
//                    List<OfferLetter> offerLetters = offerLetterRepository.findAllById(offerLetterIds);
                    SubmissionSlip sSlip = subSlipRepository.findById(sSlipApprovalDto.getSubSlipId())
                    		.orElseThrow(() -> new IllegalArgumentException("Submission slip not found for given id: " + sSlipApprovalDto.getSubSlipId()));
                    List<Integer> receivers = Arrays.asList(sSlipApprovalDto.getNextApproverId());
                    String sbuName = null;
                    if (sSlipApprovalDto.getNextApproverId() == 0) {
                        approvalDetails.setNextApprover(null);
                        // Updating the OL status to 1 if CQAO approves Inspection File Note
                        Status olApprovedStatus = statusRepository.findById(ApplicationConstants.COMMON_STATUS_CONSTANTS.APPROVED).get();
                        sSlip.setOlStatus(olApprovedStatus);
                        subSlipRepository.save(sSlip);
                        sbuName = sSlip.getWarrant().getSoLineItem().getSupplyOrder().getSalesGroup().getName();
                        
                        List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
                        receivers = employees.stream().map(Employee::getId).toList();
                    }
                    approvalDetails.setCreatedDate(new Date());
                    approvalDetails.setDesignation(sSlipApprovalDto.getDesignation());
                    // creating a notification for the CQAE user
                    for (Integer receiverId : receivers) {
                        NotificationDto notificationDto = new NotificationDto();
                        notificationDto.setNotificationName(NOTIFICATION_INSPECTION_FILE_NOTE_RECEIVED);
                        notificationDto.setNotificationCategory("OL");
//                        notificationDto.setCategoryId(fileNoteId);
                        notificationDto.setSenderId(sSlipApprovalDto.getCreatedBy());
                        notificationDto.setReceiverId(receiverId);
                        notificationDto.setSbuName(sbuName);
                        notificationService.saveNotification(notificationDto);
                    }
                    // I-Note number generation when JC approves file note
                    // Generating different INote no in case OLs in file note have different
                    // consignees
                    // OLs with same consignee shall have same INote No.
//                    OfferLetter offerLetter = offerLetters.get(0);
                    if (sSlipApprovalDto.getOlStatus() == ApplicationConstants.APPROVED) {
//                        if (ApplicationConstants.JC_ROLE.equals(role)) {
//                            generateINoteNo(offerLetter, fileNoteId);
//                        }
//                        boolean isINoteNoExists = inspectionFileNoteRepository
//                                .checkINoteNoStatusByFileNoteId(fileNoteId);
//                        if (!isINoteNoExists && ApplicationConstants.CQAO_ROLE.equals(role)) {
//                            generateINoteNo(offerLetter, fileNoteId);
//                        }
                    }

                    OlApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
                    MessageBean message = new MessageBean();
                    message.setReceivers(receivers);
                    message.setSender(sSlipApprovalDto.getCreatedBy());
                    message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
                    message.setMessage(NOTIFICATION_INSPECTION_FILE_NOTE_RECEIVED);
                    wsService.notifyCqae(message);
                    List<String> uploadedFiles = new ArrayList<>();
                    if (uploads != null && uploads.length > 0) {
                        List<String> uploadedFilesUrl = new ArrayList<>();
                        for (int i = 0; i < uploads.length; i++) {
                            String fileName = fileStorageService.storeFile(uploads[i], savedOlApproval.getId(), i);
                            if (StringUtils.isNotBlank(fileName)
                                    && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
                                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR
                                        + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
                                uploadedFilesUrl.add(fileDownloadUri);
                                uploadedFiles.add(fileName);
                            }
                        }
                        sSlipApprovalDto.setUploadedFiles(uploadedFilesUrl);
                        updateOlApprovalWithUploadInfo(uploadedFiles, savedOlApproval);
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("Error occured while updating offer letter approval ", e);
            logger.error("Error in updating offer letter approval {}", e.getMessage());
        }
        return sSlipApprovalDto;

    }

    public SubSlipApprovalDto updateInternalOlApproval(SubSlipApprovalDto olApprovalDto, MultipartFile[] uploads, String role) {

        if (olApprovalDto != null) {
            Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
            if (empOptional.isPresent()) {
            	SubSlipApprovalDetails approvalDetails = mapper.map(olApprovalDto, SubSlipApprovalDetails.class);
            		
            	SubmissionSlip subSlip = subSlipRepository.findById(olApprovalDto.getSubSlipId())
            			.orElseThrow(() -> new IllegalArgumentException("Submission Slip not found for id: " + olApprovalDto.getSubSlipId()));
            	
                List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                String sbuName = null;
                if (olApprovalDto.getNextApproverId() == null || olApprovalDto.getNextApproverId() == 0) {
                    approvalDetails.setNextApprover(null);
                    // Updating the OL status to 1 if CQAO approves Inspection File Note
                    Status olApprovedStatus = statusRepository.findById(ApplicationConstants.COMMON_STATUS_CONSTANTS.APPROVED)
                            .get();
                    subSlip.setOlStatus(olApprovedStatus);
                    subSlipRepository.save(subSlip);
//                        sbuName = ol.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
                    sbuName = "";
                    List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
                    receivers = employees.stream().map(Employee::getId).toList();
                }
                approvalDetails.setCreatedDate(new Date());
                approvalDetails.setDesignation(olApprovalDto.getDesignation());

                //checking for allotment
				if (olApprovalDto.getNextApproverId() != null) {
					SubSlipAllotment existingALlotment = subSlipAllotmentRepository.getAllotedSubSlipToEmp(olApprovalDto.getSubSlipId(), olApprovalDto.getNextApproverId(), olApprovalDto.getCreatedBy());
					if (existingALlotment == null) {
						SubSlipAllotment olAllotment = new SubSlipAllotment();
						olAllotment.setAllotedBy(approvalDetails.getCreatedBy());
						olAllotment.setAllotedTo(approvalDetails.getNextApprover());
						olAllotment.setSubmissionSlip(approvalDetails.getSubSlip());
						olAllotment.setCreatedDate(new Date());
						subSlipAllotmentRepository.save(olAllotment);
					}
				}

                // creating a notification for the CQAE user
                String msg = "Submission Slip " + subSlip.getOlNumber() + " received for approval";
                for (Integer receiverId : receivers) {
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setNotificationName(msg);
                    notificationDto.setNotificationCategory("OL");
//						notificationDto.setCategoryId(fileNoteId);
                    notificationDto.setCategoryId(olApprovalDto.getSubSlipId());
                    notificationDto.setSenderId(olApprovalDto.getCreatedBy());
                    notificationDto.setReceiverId(receiverId);
                    notificationDto.setSbuName(sbuName);
                    notificationService.saveNotification(notificationDto);
                }

                //checking for RFR
                if(olApprovalDto.getIsRfr() == null ){
                    Optional<SubSlipApprovalDetails> lastRecord = subSlipApprovalDetailsRepository.getLastRecordBySubSlipId(olApprovalDto.getSubSlipId());
                    approvalDetails.setIsRfr(lastRecord.isPresent() ? lastRecord.get().getIsRfr() : false);
                } else if(olApprovalDto.getIsRfr()) {
                	approvalDetails.setIsRfr(olApprovalDto.getIsRfr());
                	mergeSSlipService.addToRfr(subSlip);
                }

                SubSlipApprovalDetails savedOlApproval = subSlipApprovalDetailsRepository.save(approvalDetails);
                MessageBean message = new MessageBean();
                message.setReceivers(receivers);
                message.setSender(olApprovalDto.getCreatedBy());
                message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
                message.setMessage(msg);
                wsService.notifyCqae(message);
                List<String> uploadedFiles = new ArrayList<>();
                if (uploads != null && uploads.length > 0) {
                    List<String> uploadedFilesUrl = new ArrayList<>();
                    for (int i = 0; i < uploads.length; i++) {
                        String fileName = fileStorageService.storeFile(uploads[i], savedOlApproval.getId(), i);
                        if (StringUtils.isNotBlank(fileName)
                                && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
                            String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR
                                    + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
                            uploadedFilesUrl.add(fileDownloadUri);
                            uploadedFiles.add(fileName);
                        }
                    }
                    olApprovalDto.setUploadedFiles(uploadedFilesUrl);
//                    this function is yet to be developed for sub slip flow
//                    updateOlApprovalWithUploadInfo(uploadedFiles, savedOlApproval);
                }
                
            }
        }

        return olApprovalDto;

    }

    
    public SoApprovalDto getOlApprovalDetailsByFileNoteId(int filNoteId, int empId) {

        SoApprovalDto soApprovalDto = new SoApprovalDto();
        try {
            Optional<InspectionFileNote> fnOptional = inspectionFileNoteRepository.findById(filNoteId);
            if (fnOptional.isPresent()) {
                InspectionFileNote inspectionFileNote = fnOptional.get();
                Integer eligibleApproverId = olApprovalRepository.getNextApprover(filNoteId);
                Integer empRoleId = empRepository.getEmpRoleByEmpId(empId);

                boolean cqaoApprovalStatus = olApprovalRepository.getLatestApprovalStatusByFileNoteId(filNoteId);
                if (cqaoApprovalStatus) {
                    soApprovalDto.setIsEditAllowed(false);
                } else {
                    if ((eligibleApproverId == null && empRoleId == ApplicationConstants.INSPECTOR)
                            || eligibleApproverId != null && eligibleApproverId.intValue() == empId) {
                        soApprovalDto.setIsEditAllowed(true);

                    } else {
                        soApprovalDto.setIsEditAllowed(false);

                    }
                }
                soApprovalDto.setCqaoApprovalDate(inspectionFileNote.getCqaoApprovalDate());
                List<OlApprovalDto> approvalDtos = new ArrayList<>();
                Set<OlApprovalDetails> approvalDetails = inspectionFileNote.getOlApprovalDetails();
                for (OlApprovalDetails olApprovalDetails : approvalDetails) {
                    OlApprovalDto approvalDto = mapper.map(olApprovalDetails, OlApprovalDto.class);
                    approvalDto.setApprovedByStr(olApprovalDetails.getApprovedBy().getEname());
                    approvalDto.setApproverDesignation(
                            olApprovalDetails.getDesignation() != null ? olApprovalDetails.getDesignation()
                                    : olApprovalDetails.getApprovedBy().getEmpRole().getEmpRole());
//                    getInspectionFiles(approvalDto, olApprovalDetails);
                    approvalDtos.add(approvalDto);
                }
                Collections.sort(approvalDtos, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
                soApprovalDto.setSoApprovalDtos(approvalDtos);
                return soApprovalDto;
            }
        } catch (Exception e) {
        	logger.error("Error occured while updating offer letter approval ", e);
            logger.error("Error in updating offer letter approval {}", e.getMessage());
            // return null;
        }

        return soApprovalDto;

    }

    private void generateINoteNo(OfferLetter ol, int fileNoteId) {

        SupplyOrder supplyOrder = ol.getSaleOrder().getSupplyOrder();
        Organization organization = supplyOrder.getOrganization();
        String stdCode = organization.getStdCode();
        String inoteCode = organization.getInoteCode();
        List<Object[]> consigneeDetails = inspectionFileNoteRepository.getOlNConsigneeByFileNoteId(fileNoteId);
        Map<Integer, String> consigneeIdMap = new HashMap<>();
        Integer maxValue = inspectionFileNoteRepository.checkMaxValue();
        if (maxValue == null || inoteCode == null)
            maxValue = 0;
        for (int i = 0; i < consigneeDetails.size(); i++) {

            if (consigneeDetails.get(i)[1] != null) {
                int consigneeId = (int) consigneeDetails.get(i)[1];
                String inoteNo = null;
                if (consigneeId != 0 && !consigneeIdMap.keySet().contains(consigneeId)) {
                    maxValue++;
                    String newNumericPart = String.format("%04d", maxValue);
                    String newINoteNo = stdCode + newNumericPart + inoteCode;
                    inoteNo = newINoteNo;
					logger.info("New I-Note Generated");
                    consigneeIdMap.put(consigneeId, newINoteNo);
                } else {
                    inoteNo = consigneeIdMap.get(consigneeId);
                }
                if (consigneeDetails.get(i)[0] != null) {
                    inspectionFileNoteRepository.updateINoteNo(inoteNo, fileNoteId, (int) consigneeDetails.get(i)[0]);
                }
            }
        }
    }

	public List<SubSlipSerialNoDto> getPassedRegNoBySubSlipId(Integer subSlipId) {
		List<Object[]> data = subSlipApprovalDetailsRepository.getPassedRegNoBySubSlipId(subSlipId);
	    if (data == null || data.isEmpty()) {
	        return new ArrayList<>();
	    }
	    List<SubSlipSerialNoDto> dtos = new ArrayList<>();
	    try {
	        for (Object[] obj : data) {	 
	        	int index = 0;
	            SubSlipSerialNoDto dto = new SubSlipSerialNoDto();
	            dto.setId(obj[index]!=null ? (Integer)obj[index]: null);
	            index ++;
	            dto.setSerialNo(obj[index] != null ? obj[index].toString() : null);
	            index++;
	            dto.setFinalStatus(obj[index] != null ? obj[index].toString() : null);     
	            dtos.add(dto);
	        }
	    } catch (Exception e) {	      
	        throw new RuntimeException("Error processing approval data mapping", e);
	    }
	    return dtos;
		
		
	}

	public List<SubSlipSerialNoDto> getRegNoBySubSlipId(Integer subSlipId) {
		List<Object[]> data = subSlipApprovalDetailsRepository.getRegNoBySubSlipId(subSlipId);
	    if (data == null || data.isEmpty()) {
	        return new ArrayList<>();
	    }
	    List<SubSlipSerialNoDto> dtos = new ArrayList<>();
	    try {
	        for (Object[] obj : data) {	 
	        	int index = 0;
	            SubSlipSerialNoDto dto = new SubSlipSerialNoDto();
	            dto.setId(obj[index]!=null ? (Integer)obj[index]: null);
	            index ++;
	            dto.setSerialNo(obj[index] != null ? obj[index].toString() : null);
	            index++;
	            dto.setFinalStatus(obj[index] != null ? obj[index].toString() : null);     
	            index++ ;
	            dto.setRegistrationNo(obj[index]!= null ? (Integer)obj[index]: null);
	            dtos.add(dto);
	        }
	    } catch (Exception e) {	      
	        throw new RuntimeException("Error processing approval data mapping", e);
	    }
	    return dtos;
	}

}
