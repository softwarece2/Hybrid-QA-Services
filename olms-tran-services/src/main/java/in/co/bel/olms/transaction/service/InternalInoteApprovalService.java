package in.co.bel.olms.transaction.service;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmpRoleRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.InternalInoteApprovalDetails;
import in.co.bel.olms.transaction.entity.InternalOlAprovlDetAttachment;
import in.co.bel.olms.transaction.repository.InternalINoteRepository;
import in.co.bel.olms.transaction.repository.InternalInoteApprovalDetailsRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternalInoteApprovalService {

    @Value("${file.domain-url}")
    private String domainUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private final InternalInoteApprovalDetailsRepository olApprovalRepository;

    private final EmployeeRepository empRepository;

    private final EmpRoleRepository empRoleRepository;

    private final NotificationService notificationService;

    private final WSService wsService;

    private final Mapper mapper;

    private final InternalINoteRepository internalINoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(InternalInoteApprovalService.class);

    public OlApprovalDto updateOlApproval(OlApprovalDto olApprovalDto, MultipartFile[] uploads) {

        try {
            if (olApprovalDto != null) {
                Optional<Employee> empOptional = empRepository.findById(olApprovalDto.getCreatedBy());
                if (empOptional.isPresent()) {

                InternalInoteApprovalDetails approvalDetails = mapper.map(olApprovalDto, InternalInoteApprovalDetails.class);
                    List<Integer> receivers = Arrays.asList(olApprovalDto.getNextApproverId());
                    String sbuName = null;
                    approvalDetails.setApprovedBy(empOptional.get());
                    if (olApprovalDto.getNextApproverId()==null) {
                        approvalDetails.setNextApprover(null);         
                        List<Employee> employees = empRepository.getEmpByRole(ApplicationConstants.CQAO);
                        receivers = employees.stream().map(Employee::getId).toList();
                        
                    }
                    approvalDetails.setCreatedDate(new Date());
                   
                    
                    if(olApprovalDto.getInternalInoteId() != null) { 
                    	Optional<InternalINote>op=internalINoteRepository.findById(olApprovalDto.getInternalInoteId());
                    	
                    	op.ifPresentOrElse(internalInote -> approvalDetails.setInternalInote(internalInote),
                    			() -> new IllegalArgumentException("InternalINote not found for ID: " + olApprovalDto.getInternalInoteId()));
                    	
                    }
                    
                   
                   	approvalDetails.setApprovalStatus(olApprovalDto.getApprovalStatus());
                  
                    String msg = olApprovalDto.getApprovalStatus() != null && olApprovalDto.getApprovalStatus() == ApplicationConstants.OlStatus.RETURN_QACL ? "Challan " + approvalDetails.getInternalInote().getINoteNo() + " returned" :
                            "Offer letter received for approval";
                    // creating a notification for the CQAE user
                    for (Integer receiverId : receivers) {
                        NotificationDto notificationDto = new NotificationDto();
                        notificationDto.setNotificationName(msg);
                        notificationDto.setNotificationCategory("OL");
                        notificationDto.setCategoryId(olApprovalDto.getInternalInoteId());
                        notificationDto.setSenderId(olApprovalDto.getCreatedBy());
                        notificationDto.setReceiverId(receiverId);
                        notificationDto.setSbuName(sbuName);
                        notificationService.saveNotification(notificationDto);
                    }

                    InternalInoteApprovalDetails savedOlApproval = olApprovalRepository.save(approvalDetails);
                    MessageBean message = new MessageBean();
                    message.setReceivers(receivers);
                    message.setSender(olApprovalDto.getCreatedBy());
                    message.setMessageType(ApplicationConstants.INSP_FILE_NOTE);
                    message.setMessage(msg);
                    wsService.notifyCqae(message);
                }
            }
        } catch (Exception e) {
        	logger.error("Error occured while updating offer letter approval  ", e);
            logger.error("Error in updating offer letter approval {}", e.getMessage());
        }
        return olApprovalDto;
    }
    
    // updating the OL approval uploads table for the uploaded files
    private void updateOlApprovalWithUploadInfo(List<String> uploadedFiles, InternalInoteApprovalDetails olApproval) {

        List<InternalOlAprovlDetAttachment> approvalUploadsList = new ArrayList<>();
        for (String fileName : uploadedFiles) {
        	InternalOlAprovlDetAttachment  approvalUploads = new InternalOlAprovlDetAttachment ();
            approvalUploads.setFileName(fileName);
            approvalUploadsList.add(approvalUploads);
            approvalUploads.setInternalInoteApprovalDetails(olApproval);
        }
        olApproval.setInternalOlAprovlDetAttachments(approvalUploadsList);
        olApprovalRepository.save(olApproval);
    }


    public SoApprovalDto getInternalInoteApprovalDetailsByOlId(int internalInoteId, int empId) {

        SoApprovalDto soApprovalDto = new SoApprovalDto();
        Optional<InternalINote> olOptional = internalINoteRepository.findById(internalInoteId);
        if (olOptional.isPresent()) {
        	InternalINote internalINote = olOptional.get();
            Integer eligibleApproverId = olApprovalRepository.getEligibleApprover(internalInoteId);
            Integer empRoleId = empRepository.getEmpRoleByEmpId(empId);
         
                if ((eligibleApproverId == null && empRoleId == ApplicationConstants.INSPECTOR)
                        || eligibleApproverId != null && eligibleApproverId.intValue() == empId) {
                    soApprovalDto.setIsEditAllowed(true);
                } else {
                    soApprovalDto.setIsEditAllowed(false);
                }

            List<OlApprovalDto> approvalDtos = new ArrayList<>();
            Set<InternalInoteApprovalDetails> approvalDetails = internalINote.getOlApprovalDetails();
            for (InternalInoteApprovalDetails olApprovalDetails : approvalDetails) {
                OlApprovalDto approvalDto = mapper.map(olApprovalDetails, OlApprovalDto.class);
                Integer createdRoleId = empRepository.getEmpRoleByEmpId(approvalDto.getCreatedBy());
                EmpRole empRole = empRoleRepository.getById(createdRoleId);
                approvalDto.setEmpRole(empRole.getEmpRole());
               getInspectionFiles(approvalDto, olApprovalDetails);
                approvalDtos.add(approvalDto);
            }
            Collections.sort(approvalDtos, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
            soApprovalDto.setSoApprovalDtos(approvalDtos);
            return soApprovalDto;
        }
        return null;
    }
    
    private OlApprovalDto getInspectionFiles(OlApprovalDto olApprovalDto, InternalInoteApprovalDetails approvalDetails) {

        List<String> uploadedFilesUrl = new ArrayList<>();
        try {
            List<InternalOlAprovlDetAttachment> approvalUploads = approvalDetails.getInternalOlAprovlDetAttachments();
            for (InternalOlAprovlDetAttachment olApprovalUpload : approvalUploads) {

                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
                        + olApprovalUpload.getFileName();
                uploadedFilesUrl.add(fileDownloadUri);

            }
            olApprovalDto.setUploadedFiles(uploadedFilesUrl);
        } catch (Exception e) {
            logger.error("Could not set uploaded files in ol approval fetch{}", e.getMessage());
        }
        return olApprovalDto;
    }
    
    public List<SoApprovalDto> getAllInternalInotesByEmpId( int empId) {

        List<SoApprovalDto> soApprovalDtos = new ArrayList<>();
        try {
        	List<Object[]> dtos = olApprovalRepository.getAllRecordsByEmpId(empId);
           for(Object[] dto:dtos) {
        	   SoApprovalDto soApprovalDto =new SoApprovalDto();
        	   soApprovalDto.setId(Integer.parseInt(dto[0].toString()));
        	   soApprovalDto.setInternalNo(dto[1].toString());  
        	   soApprovalDto.setOfferLetterId(Integer.parseInt(dto[2].toString()));
        	   soApprovalDtos.add(soApprovalDto);      	   
           }   
        } catch (Exception e) {
        	logger.error("Error occured in getting all internalInotes  ", e);
            logger.error("Error in fetching details{}", e.getMessage());
            // return null;
        }
        List <SoApprovalDto>result= soApprovalDtos;
		  result.sort(Comparator.comparing(SoApprovalDto::getId).reversed());
        return result;
    }
}
