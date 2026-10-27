package in.co.bel.olms.transaction.service.so;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.entity.SoApprovalDetAttachment;
import in.co.bel.olms.transaction.entity.SoApprovalDetails;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.SoApprovalDetAttachRepo;
import in.co.bel.olms.transaction.repository.SoApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.validator.InputValidation;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SoApprovalDetailsService {

	private final SoApprovalDetailsRepository soApprovalRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final EmployeeRepository empRepository;

	private final NotificationService notificationService;

	private final WSService wsService;

	private final Mapper mapper;

	private final FileStorageService fileStorageService;

	private final SoApprovalDetAttachRepo soApprovalDetAttachRepo;
	
	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;
	
	private static final Logger logger = LoggerFactory.getLogger(SoApprovalDetailsService.class);

	public boolean updateSoApproval(OlApprovalDto soApprovalDto , MultipartFile[] attachments) {
		
		boolean returnStatus = false;
		try {
			if (soApprovalDto != null) {
				Optional<Employee> empOptional = empRepository.findById(soApprovalDto.getCreatedBy());
			if (empOptional.isPresent()) {
					SoApprovalDetails approvalDetails = mapper.map(soApprovalDto, SoApprovalDetails.class);
					// creating a notification
					int supplyOrderId = soApprovalDto.getSupplyOrderId();
					List<NotificationDto> notificationDtos = new ArrayList<>();
					SupplyOrder supplyOrder = supplyOrderRepository.findById(supplyOrderId).get();
					if (soApprovalDto.getNextApproverId() == null) {
						soApprovalDto.setNextApproverId(0);
					}
					if(soApprovalDto.getNextApproverId() == 0) {
						List<Integer> receiverIds = supplyOrderRepository.getSbuAdminIdBySoId(supplyOrderId);
						for (int id:receiverIds) {
						NotificationDto notificationDto=new NotificationDto();
						approvalDetails.setNextApprover(null);
						notificationDto.setNotificationName("Supply Order "+supplyOrder.getOrderNo()+"  has been approved.");
					
						notificationDto.setReceiverId(id);
						notificationDto.setSbuName(supplyOrder.getSalesGroup().getName());
						notificationDto.setNotificationCategory("SO");
						notificationDto.setCategoryId(supplyOrderId);
						notificationDto.setSenderId(soApprovalDto.getCreatedBy());
						
						}
						notificationService.saveNotifications(notificationDtos);
					}
					else {
						NotificationDto notificationDto=new NotificationDto();
						notificationDto.setNotificationName("Supply Order "+supplyOrder.getOrderNo()+" received for approval.");
						notificationDto.setReceiverId(soApprovalDto.getNextApproverId());
						notificationDto.setSbuName(supplyOrder.getSalesGroup().getName());
						notificationDto.setNotificationCategory("SO");
						notificationDto.setCategoryId(supplyOrderId);
						notificationDto.setSenderId(soApprovalDto.getCreatedBy());
						notificationService.saveNotification(notificationDto);
					}
					


					approvalDetails.setCreatedDate(new Date());
					
					
					List<Integer> receivers = Arrays.asList(soApprovalDto.getNextApproverId());
						if (!InputValidation.checkRemark(soApprovalDto.getRemarks())) {
							throw new DataNotValidException("SO Approval remarks:  " + soApprovalDto.getRemarks());


						}
						else {
					SoApprovalDetails savedSoApprovalDetails = soApprovalRepository.save(approvalDetails);
					List<SoApprovalDetAttachment> soApprovalDetAttachments = new  ArrayList<>();
					if (attachments != null && attachments.length > 0) {
						saveSoApprovalAttachment(attachments,  savedSoApprovalDetails, soApprovalDetAttachments);
					}
					soApprovalDetAttachRepo.saveAll(soApprovalDetAttachments);
					}
					MessageBean message = new MessageBean();
					message.setReceivers(receivers);
					message.setSender(soApprovalDto.getCreatedBy());
					message.setMessageType(ApplicationConstants.SUPPLY_ORDER_APPROVAL);
					message.setMessage("Supply Order received for approval");
					message.setSbuName(supplyOrder.getSalesGroup().getName());
					wsService.notifyCqae(message);
					returnStatus = true;
				}
			}
		} catch (Exception e) {
			logger.error("Error occured while updating supply order approval ", e);
			logger.error("Error in updating supply order approval {}", e.getMessage());
		}
		return returnStatus;
		
	}

	private void saveSoApprovalAttachment(MultipartFile[] files, SoApprovalDetails savedSoApprovalDetails,List<SoApprovalDetAttachment> soApprovalDetAttachments) {
		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(files[i], savedSoApprovalDetails.getId(), i);
			if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				SoApprovalDetAttachment attachment = new SoApprovalDetAttachment();
				attachment.setCreatedDate(date);
				attachment.setFileName(fileName);
				attachment.setSoApprovalDetails(savedSoApprovalDetails);
				soApprovalDetAttachments.add(attachment);
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ fileName;
				uploadedFilesUrl.add(fileDownloadUri);
			}
		}
		
	}


	public SoApprovalDto getSoApprovalDetailsBySoId(int soId, int empId) {
		SoApprovalDto soApprovalDto = new SoApprovalDto();
		Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(soId);
		if (supplyOrderOptional.isPresent()) {
			SupplyOrder supplyOrder = supplyOrderOptional.get();
//			Integer empRoleId = empRepository.getEmpRoleByEmpId(empId);
//			Integer eligibleApproverId = soApprovalRepository.getEligibleApprover(soId);
//			if ((eligibleApproverId == null && empRoleId == ApplicationConstants.INSPECTOR )
//					|| eligibleApproverId != null && eligibleApproverId.intValue() == empId) {
//				soApprovalDto.setIsEditAllowed(true);
//			} else {
//				soApprovalDto.setIsEditAllowed(false);
//			}
	
			List<OlApprovalDto> approvalDtos = new ArrayList<>();
			List<SoApprovalDetails> approvalDetails = supplyOrder.getSoApprovalDetails();
			for (SoApprovalDetails soApprovalDetails : approvalDetails) {
				OlApprovalDto approvalDto = mapper.map(soApprovalDetails, OlApprovalDto.class);
				
				List<Object[]> response = soApprovalDetAttachRepo.getBySoApprovalDetailsId(soApprovalDetails.getId());
				if (response != null) {
					List<String> olAttachments = new ArrayList<>();
					for (Object[] obj : response) {
						String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + obj[2].toString();

						olAttachments.add(fileDownloadUri);
						approvalDto.setUploadedFiles(olAttachments);
					}

					approvalDtos.add(approvalDto);
				}
			}
			
			SoApprovalDetails eligibleApproverId = soApprovalRepository.getEligibleApprover(soId);
			if (eligibleApproverId == null) {
			    soApprovalDto.setIsEditAllowed(true);
			} else {
			    soApprovalDto.setIsEditAllowed(false);
			    
			}
			
			soApprovalDto.setSoApprovalDtos(approvalDtos);
			return soApprovalDto;
		}
		return null;
	}

	public SoApprovalDetails feedBackUpdateSo(OlApprovalDto soApprovalDto) {
		SoApprovalDetails soApprovalDetails = new SoApprovalDetails();
		Optional<SoApprovalDetails> soApprovalDetailsOptional = soApprovalRepository.findById(soApprovalDto.getId());
		if(soApprovalDetailsOptional.isPresent()) {
			soApprovalDetails = soApprovalDetailsOptional.get();
			if(soApprovalDetails.getSoStatus().getId() == 2) {
			soApprovalDetails.setQaoRemarks(soApprovalDto.getQaoRemarks());
			soApprovalRepository.save(soApprovalDetails);
			}
			else {
				throw new DataNotFoundException("QAO remarks can be added for the rejected records only");
			}
		}
		
		return soApprovalDetails;
	}

	

}
