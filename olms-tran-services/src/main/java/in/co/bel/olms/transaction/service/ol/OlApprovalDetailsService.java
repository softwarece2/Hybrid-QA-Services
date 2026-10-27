package in.co.bel.olms.transaction.service.ol;

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
import in.co.bel.olms.constants.ApplicationConstants.REGISTRATION_CONSTANTS;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.entity.InspectionFileNote;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlAllotment;
import in.co.bel.olms.transaction.entity.OlApprovalDetails;
import in.co.bel.olms.transaction.entity.OlApprovalUploads;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlAllotmentRepository;
import in.co.bel.olms.transaction.repository.OlApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.insp.InspectionFileNoteRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlApprovalDetailsService {

    @Value("${file.domain-url}")
    private String domainUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private final OlApprovalDetailsRepository olApprovalRepository;

    private final FileStorageService fileStorageService;

    private final OfferLetterRepository olRepository;

    private final EmployeeRepository empRepository;

    private final OlStatusRepository olStatusRepository;

    private final OlAllotmentRepository olAllotmentRepository;

    private final NotificationService notificationService;

    private final WSService wsService;

    private final Mapper mapper;

    private final OfferLetterRepository offerLetterRepository;

    private final OLLineItemSerialNoRepository olLineItemSerialNoRepo;
    
    private final LineItemSerialNoRepository lineItemSerialNoRepo;

    private final InspectionFileNoteRepository inspectionFileNoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(OlApprovalDetailsService.class);
    
    private static final String NOTIFICATION_PREFIX = "Offer Letter ";
    
    private static final String NOTIFICATION_SUFFIX = " received for approval";

//	private SupplyOrderRepository supplyOrderRepository;

    public OlApprovalDto updateOlApproval(OlApprovalDto olApprovalDto, MultipartFile[] uploads) {

        try {
            if (olApprovalDto != null) {
                Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
                if (empOptional.isPresent()) {
                    OlApprovalDetails approvalDetails = mapper.map(olApprovalDto, OlApprovalDetails.class);
                    List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                    String sbuName = null;
                    OfferLetter offerLetter = new OfferLetter();
                    if (olApprovalDto.getNextApproverId() == 0) {
                        approvalDetails.setNextApprover(null);
                        // Updating the OL status to 1 if CQAO approves Inspection File Note
                        Optional<OfferLetter> olOptional = olRepository.findById(olApprovalDto.getOfferLetterId());
                        if (olOptional.isPresent()) {
                             offerLetter = olOptional.get();
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
//                        notificationDto.setNotificationName("Inspection File Note received for approval");
                        notificationDto.setNotificationName(NOTIFICATION_PREFIX+ offerLetter.getOlNumber()+ NOTIFICATION_SUFFIX);
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
                    message.setMessage(NOTIFICATION_PREFIX+ offerLetter.getOlNumber()+ NOTIFICATION_SUFFIX);
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

    public SoApprovalDto getOlApprovalDetailsByOlId(int olId, int empId) {

        SoApprovalDto soApprovalDto = new SoApprovalDto();
        Optional<OfferLetter> olOptional = olRepository.findById(olId);
        if (olOptional.isPresent()) {
            OfferLetter offerLetter = olOptional.get();
            Integer eligibleApproverId = olApprovalRepository.getEligibleApprover(olId);
            Integer empRoleId = empRepository.getEmpRoleByEmpId(empId);

            boolean cqaoApprovalStatus = olApprovalRepository.getLatestApprovalStatusByOlId(olId);
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

            List<OlApprovalDto> approvalDtos = new ArrayList<>();
            Set<OlApprovalDetails> approvalDetails = offerLetter.getOlApprovalDetails();
            for (OlApprovalDetails olApprovalDetails : approvalDetails) {
                OlApprovalDto approvalDto = mapper.map(olApprovalDetails, OlApprovalDto.class);
                getInspectionFiles(approvalDto, olApprovalDetails);
                approvalDtos.add(approvalDto);
            }
            Collections.sort(approvalDtos, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
            soApprovalDto.setSoApprovalDtos(approvalDtos);
            return soApprovalDto;
        }
        return null;
    }

    private OlApprovalDto getInspectionFiles(OlApprovalDto olApprovalDto, OlApprovalDetails approvalDetails) {

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

    public OlApprovalDto updateFileNoteApproval(OlApprovalDto olApprovalDto, MultipartFile[] uploads, String role) {
        try {
            if (olApprovalDto != null) {
                Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
                if (empOptional.isPresent()) {
                    OlApprovalDetails approvalDetails = mapper.map(olApprovalDto, OlApprovalDetails.class);
                    int fileNoteId = olApprovalDto.getInspectionFileNoteId();
                    List<Integer> offerLetterIds = offerLetterRepository.getOLIds(fileNoteId);
                    List<OfferLetter> offerLetters = offerLetterRepository.findAllById(offerLetterIds);
                    List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                    String sbuName = null;
                    if (olApprovalDto.getNextApproverId() == 0) {
                        approvalDetails.setNextApprover(null);
                        // Updating the OL status to 1 if CQAO approves Inspection File Note
                        for (OfferLetter ol : offerLetters) {
                            OlStatus olApprovedStatus = olStatusRepository.findById(ApplicationConstants.OL_APPROVED)
                                    .get();
                            ol.setOlStatus(olApprovedStatus);
                            olRepository.save(ol);
                            sbuName = ol.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
                        }
                        List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
                        receivers = employees.stream().map(Employee::getId).toList();
                    }
                    approvalDetails.setCreatedDate(new Date());
                    approvalDetails.setDesignation(olApprovalDto.getDesignation());
                    // creating a notification for the CQAE user
                    for (Integer receiverId : receivers) {
                        NotificationDto notificationDto = new NotificationDto();
                        notificationDto.setNotificationName("Inspection File Note received for approval");
                        notificationDto.setNotificationCategory("OL");
                        notificationDto.setCategoryId(fileNoteId);
                        notificationDto.setSenderId(olApprovalDto.getCreatedBy());
                        notificationDto.setReceiverId(receiverId);
                        notificationDto.setSbuName(sbuName);
                        notificationService.saveNotification(notificationDto);
                    }
                    // I-Note number generation when JC approves file note
                    // Generating different INote no in case OLs in file note have different
                    // consignees
                    // OLs with same consignee shall have same INote No.
                    OfferLetter offerLetter = offerLetters.get(0);
                    if (olApprovalDto.getOlStatus() == ApplicationConstants.APPROVED) {
                        if (ApplicationConstants.JC_ROLE.equals(role)) {
                            generateINoteNo(offerLetter, fileNoteId);
                        }
                        boolean isINoteNoExists = inspectionFileNoteRepository
                                .checkINoteNoStatusByFileNoteId(fileNoteId);
                        if (!isINoteNoExists && ApplicationConstants.CQAO_ROLE.equals(role)) {
                            generateINoteNo(offerLetter, fileNoteId);
                        }
                    }

                    OlApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
                    MessageBean message = new MessageBean();
                    message.setReceivers(receivers);
                    message.setSender(olApprovalDto.getCreatedBy());
                    message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
                    message.setMessage("Inspection File Note received for approval");
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

    public OlApprovalDto updateInternalOlApproval(OlApprovalDto olApprovalDto, MultipartFile[] uploads, String role) {

        if (olApprovalDto != null) {
            Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
            if (empOptional.isPresent()) {
                OlApprovalDetails approvalDetails = mapper.map(olApprovalDto, OlApprovalDetails.class);
//					int fileNoteId = olApprovalDto.getInspectionFileNoteId();
//					List<Integer> offerLetterIds = offerLetterRepository.getOLIds(fileNoteId);
                Optional<OfferLetter> offerLetterOptional = offerLetterRepository.findById(olApprovalDto.getOfferLetterId());
                List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                String sbuName = null;
                if (olApprovalDto.getNextApproverId() == null || olApprovalDto.getNextApproverId() == 0) {
                    approvalDetails.setNextApprover(null);
                    // Updating the OL status to 1 if CQAO approves Inspection File Note
                    if (offerLetterOptional.isPresent()) {
                        OfferLetter ol = offerLetterOptional.get();
                        OlStatus olApprovedStatus = olStatusRepository.findById(ApplicationConstants.OL_APPROVED)
                                .get();
                        ol.setOlStatus(olApprovedStatus);
                        olRepository.save(ol);
                        sbuName = ol.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
                    }
                    List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
                    receivers = employees.stream().map(Employee::getId).toList();
                }
                approvalDetails.setCreatedDate(new Date());
                approvalDetails.setDesignation(olApprovalDto.getDesignation());

                //checking for allotment
				if (olApprovalDto.getNextApproverId() != null) {
					OlAllotment existingALlotment = olAllotmentRepository.getAllotedOlToEmp(olApprovalDto.getOfferLetterId(), olApprovalDto.getNextApproverId(), olApprovalDto.getCreatedBy());
					if (existingALlotment == null) {
						OlAllotment olAllotment = new OlAllotment();
						olAllotment.setAllotedBy(approvalDetails.getCreatedBy());
						olAllotment.setAllotedTo(approvalDetails.getNextApprover());
						olAllotment.setOfferletter(approvalDetails.getOfferLetter());
						olAllotment.setCreatedDate(new Date());
						olAllotmentRepository.save(olAllotment);
					}
				}


                // creating a notification for the CQAE user
                String msg = "Submission Slip received for approval";
                for (Integer receiverId : receivers) {
                    NotificationDto notificationDto = new NotificationDto();
                    notificationDto.setNotificationName(msg);
                    notificationDto.setNotificationCategory("OL");
//						notificationDto.setCategoryId(fileNoteId);
                    notificationDto.setCategoryId(olApprovalDto.getOfferLetterId());
                    notificationDto.setSenderId(olApprovalDto.getCreatedBy());
                    notificationDto.setReceiverId(receiverId);
                    notificationDto.setSbuName(sbuName);
                    notificationService.saveNotification(notificationDto);
                }

                //checking for RFR
                if(olApprovalDto.getIsRfr() == null){
                    Optional<OlApprovalDetails> lastRecord = olApprovalRepository.getLastRecordByOlId(olApprovalDto.getOfferLetterId());
                    approvalDetails.setIsRfr(lastRecord.isPresent() ? lastRecord.get().getIsRfr() : false);
                }

                OlApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
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
                    updateOlApprovalWithUploadInfo(uploadedFiles, savedOlApproval);
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
                    getInspectionFiles(approvalDto, olApprovalDetails);
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
        Organization organization = supplyOrder.getSoLineItems().stream().map(SOLineItem::getInspectionAuthorityId).toList().get(0);
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
                    logger.info("Generated new inoteNo: {}", newINoteNo);
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

    public OlApprovalDto updateFileNoteApprovalNew(OlApprovalDto olApprovalDto, MultipartFile[] uploads) {
      try {
        if (olApprovalDto != null) {
          Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
          if (empOptional.isPresent()) {
            OlApprovalDetails approvalDetails = mapper.map(olApprovalDto, OlApprovalDetails.class);
            Integer olId = olApprovalDto.getInternalInoteId();
            OfferLetter ol = offerLetterRepository.findById(olId).orElseThrow(() -> new IllegalArgumentException("OL not found"));
            List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
            String sbuName = null;
            if (olApprovalDto.getNextApproverId() == 0) {
              approvalDetails.setNextApprover(null);
              // Updating the OL status to 1 if CQAO approves Inspection File Note
              Optional<OlStatus> olApprovedStatus = olStatusRepository.findById(ApplicationConstants.OL_APPROVED);
              olApprovedStatus.ifPresent(ol::setOlStatus);

              // Update LineItemSerialNo statuses...
              List<OLLineItemSerialNo> regNos = olLineItemSerialNoRepo.findRegNosByOlId(olId);
              if (!regNos.isEmpty()) {
                SerialNoStatus rfrStatus = new SerialNoStatus();
                rfrStatus.setId(ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS.RFR);

                boolean hasFailedFaiSample = regNos.stream().anyMatch(regNo ->
                    Boolean.TRUE.equals(regNo.getIsFaiSample()) &&
                        REGISTRATION_CONSTANTS.FAIL.equalsIgnoreCase(regNo.getStatus()));

                boolean hasFailedSample = regNos.stream().anyMatch(regNo ->
                    Boolean.TRUE.equals(regNo.getIsSample()) &&
                        REGISTRATION_CONSTANTS.FAIL.equalsIgnoreCase(regNo.getStatus()));

                List<LineItemSerialNo> lisnList = new ArrayList<>();

                if ( hasFailedSample ) {  // If a Sample failed, mark the lot as RFR                  
                  regNos.stream()
                  .map(OLLineItemSerialNo::getSerialNo)
                  .forEach(lisn -> {
                    lisn.setStatus(rfrStatus);
                    lisnList.add(lisn);
                  });
                  
                } else if (hasFailedFaiSample) { // If FAI failed, only mark the FAI failed ones                 
                  
                	regNos.stream()
                    .filter(regNo -> Boolean.TRUE.equals(regNo.getIsFaiSample()) &&
                        REGISTRATION_CONSTANTS.FAIL.equalsIgnoreCase(regNo.getStatus()))
                    .map(OLLineItemSerialNo::getSerialNo)
                    .forEach(lisn -> {
                      lisn.setStatus(rfrStatus);
                      lisnList.add(lisn);
                    });
                  
                  
                }
                
                if (!lisnList.isEmpty()) lineItemSerialNoRepo.saveAll(lisnList);

                if (!hasFailedSample && !hasFailedFaiSample) {
                  regNos.forEach(regNo -> regNo.setStatus(REGISTRATION_CONSTANTS.PASS));
                  olLineItemSerialNoRepo.saveAll(regNos);
                }
              }

              olRepository.save(ol);
              sbuName = ol.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
              List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAE_ADMIN);
              receivers = employees.stream().map(Employee::getId).toList();
            }

            approvalDetails.setCreatedDate(new Date());
            approvalDetails.setDesignation(olApprovalDto.getDesignation());
            approvalDetails.setOfferLetter(ol);

            if (olApprovalDto.getNextApproverId() != null && olApprovalDto.getNextApproverId() != 0) {
              OlAllotment existingALlotment = olAllotmentRepository.getAllotedOlToEmp(olId, olApprovalDto.getNextApproverId(), olApprovalDto.getCreatedBy());
              if (existingALlotment == null) {
                OlAllotment olAllotment = new OlAllotment();
                olAllotment.setAllotedBy(approvalDetails.getCreatedBy());
                olAllotment.setAllotedTo(approvalDetails.getNextApprover());
                olAllotment.setOfferletter(approvalDetails.getOfferLetter());
                olAllotment.setCreatedDate(new Date());
                olAllotmentRepository.save(olAllotment);
              }
            }

            // creating a notification for the CQAE user
            for (Integer receiverId : receivers) {
              NotificationDto notificationDto = new NotificationDto();
              notificationDto.setNotificationName(NOTIFICATION_PREFIX + ol.getOlNumber() + NOTIFICATION_SUFFIX);
              notificationDto.setNotificationCategory("OL");
              notificationDto.setCategoryId(olId);
              notificationDto.setSenderId(olApprovalDto.getCreatedBy());
              notificationDto.setReceiverId(receiverId);
              notificationDto.setSbuName(sbuName);
              notificationService.saveNotification(notificationDto);
            }
            // I-Note number generation when JC approves file note
            // Generating different INote no in case OLs in file note have different
            // consignees
            // OLs with same consignee shall have same INote No.

            Integer role = empOptional.get().getEmpRole().getId();
            if (olApprovalDto.getOlStatus() != null && olApprovalDto.getOlStatus() == ApplicationConstants.APPROVED) {
              if (ApplicationConstants.CQAE_ADMIN == role) {
                generateINoteNo(ol, olId);
                approvalDetails.setApprovedBy(empOptional.get());
              }
//                        boolean isINoteNoExists = inspectionFileNoteRepository
//                                .checkINoteNoStatusByFileNoteId(olId);
//                        if (!isINoteNoExists && ApplicationConstants.CQAO_ROLE.equals(role)) {
//                            generateINoteNo(ol, olId);
//                        }
            }

            OlApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
            MessageBean message = new MessageBean();
            message.setReceivers(receivers);
            message.setSender(olApprovalDto.getCreatedBy());
            message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
            message.setMessage("Offer Letter \"+ ol.getOlNumber()+ \" received for approval");
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
    
}
