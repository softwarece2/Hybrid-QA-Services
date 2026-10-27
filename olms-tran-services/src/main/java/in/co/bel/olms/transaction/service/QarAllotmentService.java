package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.QarAllotmentDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.QarAllotment;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.QarAllotmentRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QarAllotmentService {

	private static final Logger logger = LoggerFactory.getLogger(QarAllotmentService.class);

	private final OfferLetterService offerLetterService;

	private final EmployeeDao employeeDao;

	private final QarAllotmentRepository qarAllotmentRepository;

	private final NotificationService notificationService;

	private final WSService wsService;

	private final EmployeeRepository employeeRepository;

	private final OfferLetterRepository offerLetterRepository;

	public QarAllotment allotSubordinate(@Valid QarAllotmentDto olAllotmentDto) {
		QarAllotment returnOlAllotment;
		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		Employee allotedTo = employeeDao.getEmployee(olAllotmentDto.getAllotedTo());
		String sbuName = null;

		QarAllotment allotment = new QarAllotment();
		allotment.setAllotedBy(allotedBy);
		allotment.setAllotedTo(allotedTo);
		allotment.setSection(olAllotmentDto.getSection());
		OfferLetter offerletter = offerLetterService.getOfferLetterById(olAllotmentDto.getOfferLetterId());
		offerletter.setIsNotified(true);
		offerLetterRepository.save(offerletter);
		allotment.setOfferletter(offerletter);
		allotment.setCreatedDate(new Date());
		sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
		returnOlAllotment = qarAllotmentRepository.save(allotment);

		// notify all alloted to and by employees of allotment
		try {
			List<Integer> receivers = new ArrayList<>();
			List<Object[]> response = employeeRepository
					.getAllParentIncCurrentEmpOfEmpByRoleId(olAllotmentDto.getAllotedTo(), -1);
			for (Object[] item : response) {
				receivers.add(Integer.parseInt(item[0].toString()));
			}

			String msg = "QACL " + olAllotmentDto.getOfferLetterName() + " QAR "  + olAllotmentDto.getSection() + " forwarded to " + allotedTo.getEname()
					+ " by " + allotedBy.getEname();

			NotificationDto notificationDto = new NotificationDto();
			notificationDto.setSenderId(olAllotmentDto.getAllotedBy());
			notificationDto.setReceiverId(olAllotmentDto.getAllotedTo());
			notificationDto.setNotificationName(msg);
			notificationDto.setNotificationCategory(olAllotmentDto.getSection());
			notificationDto.setCategoryId(olAllotmentDto.getOfferLetterId());
			notificationDto.setSbuName(sbuName);
			notificationService.saveNotification(notificationDto);

			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.OL);
			message.setMessage(msg);
			message.setItemNo(olAllotmentDto.getOfferLetterName());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);

		} catch (Exception e) {
			logger.error("Notification error in QAR forward {} ", e.getMessage());
			logger.error("Error during QAR notification ", e);
		}
		return returnOlAllotment;
	}
	
	public @Valid QarAllotmentDto convertBotoDto(QarAllotment olAllotment) {
		QarAllotmentDto olAllotmentDto =  new QarAllotmentDto();
		olAllotmentDto.setId(olAllotment.getId());
		return olAllotmentDto;
	}
}
