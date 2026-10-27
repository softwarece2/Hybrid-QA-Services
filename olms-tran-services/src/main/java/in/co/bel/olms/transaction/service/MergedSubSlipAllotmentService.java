package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.MergeSubSlipAllotmentDto;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.entity.MergeSubSlip;
import in.co.bel.olms.transaction.entity.SubSlipAllotment;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.SubSlipAllotmentRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.transaction.service.ol.OlAllotmentService;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MergedSubSlipAllotmentService {

	private final EmployeeDao employeeDao;

	private final SubSlipService subSlipService;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final MergeSubSlipRepository mergeSubSlipRepository;

	private final EmployeeRepository employeeRepository;

	private final SubSlipAllotmentRepository subSlipAllotmentRepository;

	private final NotificationService notificationService;

	private final WSService wsService;
	
	private static final Logger logger = LoggerFactory.getLogger(MergedSubSlipAllotmentService.class);
	
	public List<SubSlipAllotment> allotSubordinate(@Valid MergeSubSlipAllotmentDto olAllotmentDto) {

		MergeSubSlip mergeSubSlip = mergeSubSlipRepository.findById(olAllotmentDto.getMergedOlId()).orElseThrow(() -> new IllegalArgumentException("Merge sub slip not found"));
		
		SubmissionSlip offerletter = submissionSlipRepository.findOneByMergedSslipId(olAllotmentDto.getMergedOlId());
		offerletter.setIsNotified(true);
		submissionSlipRepository.save(offerletter);
		
		
		List<SubSlipAllotment> allotmentList = new ArrayList<>();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		List<Integer> receivers = new ArrayList<>();
		List<String> allotedToNameList = new ArrayList<>();		

		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		
		for (Integer allotedToId : olAllotmentDto.getAllotedTo()) {
			SubSlipAllotment returnOlAllotment;
			Employee allotedTo = employeeDao.getEmployee(allotedToId);
			String sbuName = null;

			SubSlipAllotment allotment = new SubSlipAllotment();
			allotment.setAllotedBy(allotedBy);
			allotment.setAllotedTo(allotedTo);
			allotment.setSubmissionSlip(offerletter);
			allotment.setCreatedDate(new Date());
			allotment.setIsDeligation(false);
			// Manasa sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
			allotmentList.add(allotment);
			allotedToNameList.add(allotedTo.getEname());
			
			
//			if (olAllotmentDto.getIsDeligation())
//				olAllotmentRepository.updateDeligationStatus(allotedBy.getId(), olAllotmentDto.getOfferLetterId(),
//						returnOlAllotment.getCreatedDate());

			// Updating the OL Status on successful allotment of OL to inspector
			if (allotedTo.getEmpRole().getId() == ApplicationConstants.INSPECTOR) {
				logger.info("Updating the OL Status");
				subSlipService.updateOlStatus(olAllotmentDto.getMergedOlId(), ApplicationConstants.OL_PENDING);
			}

			// notify all alloted to and by employees of allotment
			try {
				List<Object[]> response = employeeRepository
						.getAllParentIncCurrentEmpOfEmpByRoleId(allotedToId, -1);
				for (Object[] item : response) {
					receivers.add(Integer.parseInt(item[0].toString()));
				}

				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setSenderId(olAllotmentDto.getAllotedBy());
				notificationDto.setReceiverId(allotedToId);
				notificationDto.setNotificationCategory(NotificationConstants.OL_ALLOTMENT);
				notificationDto.setCategoryId(olAllotmentDto.getMergedOlId());
				notificationDto.setSbuName(sbuName);
//				notificationService.saveNotification(notificationDto);
				notificationDtoList.add(notificationDto);

			} catch (Exception e) {
				logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
				logger.error("Error occured whin Allot subordinate for SO ", e);
			}
		}
		List<SubSlipAllotment> savedAllotments = subSlipAllotmentRepository.saveAll(allotmentList);
		
		// notify all alloted to and by employees of allotment
		try {
			String allotedTName = allotedToNameList.stream().collect(Collectors.joining(", "));
			String msg = "Offer Letter " + offerletter.getOlNumber() + " alloted to "
					+ allotedTName + " by " + allotedBy.getEname();
			
			for(NotificationDto dto : notificationDtoList) {
				dto.setNotificationName(msg);
			}
			
			notificationService.saveNotifications(notificationDtoList);
			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.OL);
			message.setMessage(msg);
			message.setItemNo(offerletter.getOlNumber());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);
		} catch (Exception e) {
			logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
			logger.error("Error occured whin Allot subordinate for SO ", e);
		}

		return savedAllotments;

	}
	
}
