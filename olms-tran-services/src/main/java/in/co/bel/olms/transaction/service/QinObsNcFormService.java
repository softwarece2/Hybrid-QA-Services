package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.QinObsNcFormDetDto;
import in.co.bel.olms.transaction.dto.QinObsNcFormDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.QinObsNcForm;
import in.co.bel.olms.transaction.entity.QinObsNcFormDet;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.QinObsNcFormDetRepository;
import in.co.bel.olms.transaction.repository.QinObsNcFormRepository;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QinObsNcFormService {

	private final QinObsNcFormRepository qinObsNcFormRepo;

	private final OfferLetterRepository offerLetterRepo;

	private final QinObsNcFormDetRepository qinObsNcFormDetRepo;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(QinObsNcFormService.class);

	private final NotifyUtil notifyUtil;

	/**
	 * @author Awadhesh Verma
	 * @apiNote Method to save QIN details
	 * @param qinObsNcFormDto
	 * @return
	 */
	public Map<Boolean, String> saveQinObsNcForm(QinObsNcFormDto qinObsNcFormDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		String observation = null;
		String subPartNo = null;
		List<QinObsNcFormDetDto> qinObsNcFormDetDtos = qinObsNcFormDto.getQinObsNcFormDets();
		try {
			for (QinObsNcFormDetDto qinObsNcDto : qinObsNcFormDetDtos) {
				observation = qinObsNcDto.getObservation();
				subPartNo = qinObsNcDto.getSubPartNo();

				if (StringUtils.isNotBlank(observation)) {
					if (!InputNameValidation.checkString(observation)) {
						throw new DataNotValidException("Invalid observation");
					}
				}

				if (subPartNo == null || subPartNo.trim().isEmpty()) {
					throw new DataNotValidException("Sub Part No. cannot be null");
				}

				if (StringUtils.isNotBlank(subPartNo)) {
					if (!InputNameValidation.checkString(subPartNo)) {
						throw new DataNotValidException("Invalid sub part no");
					}
				}

			}
			List<QinObsNcFormDet> qinObsNcFormDets = new ArrayList<>();
			QinObsNcForm qinObsNcForm = mapper.map(qinObsNcFormDto, QinObsNcForm.class);
			List<QinObsNcFormDetDto> ncFormDetDtos = qinObsNcFormDto.getQinObsNcFormDets();
			for (QinObsNcFormDetDto qinObsNcFormDetDto : ncFormDetDtos) {
				QinObsNcFormDet qinObsNcFormDet = mapper.map(qinObsNcFormDetDto, QinObsNcFormDet.class);
				qinObsNcFormDet.setClosureStatus(qinObsNcFormDetDto.isClosureStatus());
				qinObsNcFormDet.setQinObsNcForm(qinObsNcForm);
				if (qinObsNcFormDet.getReworkAction() != null) {
					qinObsNcForm.setIsReworkSubmitted(true);
				}

				qinObsNcFormDets.add(qinObsNcFormDet);
			}
			qinObsNcForm.setModifiedDate(todayDate);
			qinObsNcForm.setQinObsNcFormDets(qinObsNcFormDets);
			qinObsNcForm.setCreatedDate(todayDate);
			QinObsNcForm savedObsNcForm = qinObsNcFormRepo.save(qinObsNcForm);

			// Sending notification to SBU user on QIN creation
			Optional<OfferLetter> olOptional = offerLetterRepo.findById(qinObsNcFormDto.getOfferLetterId());
			if (olOptional.isPresent()) {
				OfferLetter offerLetter = olOptional.get();
				String notificationMsg = "QIN received for OL: " + offerLetter.getOlNumber();
				List<Integer> receivers = Arrays.asList(offerLetter.getCreatedBy());
				notifyUtil.notifyCqae(notificationMsg, savedObsNcForm.getId(), null, qinObsNcFormDto.getCreatedBy(),
						NotificationConstants.QIN, receivers,
						offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
			}

			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (DataNotValidException de) {
			logger.error("Validation failed: {}", de.getMessage());
			throw de;
		} catch (Exception e) {
			logger.error("Error in saving QIN Obs Nc Form {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}

	}

	/**
	 * @author Awadhesh Verma
	 * @apiNote Method to updated the Rework action on received QIN
	 * @param qinObsNcFormDto
	 * @return
	 */
	public Map<Boolean, String> updateQinObsNcFormAction(QinObsNcFormDto qinObsNcFormDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			Optional<QinObsNcForm> qinObsNcFormOptional = qinObsNcFormRepo.findById(qinObsNcFormDto.getId());
			if (qinObsNcFormOptional.isPresent()) {
				QinObsNcForm qinObsNcForm = qinObsNcFormOptional.get();
				List<QinObsNcFormDet> qinObsNcFormDets = new ArrayList<>();
				List<QinObsNcFormDetDto> ncFormDetDtos = qinObsNcFormDto.getQinObsNcFormDets();
				for (QinObsNcFormDetDto qinObsNcFormDetDto : ncFormDetDtos) {
					QinObsNcFormDet qinObsNcFormDet = qinObsNcFormDetRepo.findById(qinObsNcFormDetDto.getId()).get();
					qinObsNcFormDet.setReworkAction(qinObsNcFormDetDto.getReworkAction());
					qinObsNcFormDet.setRemarks(qinObsNcFormDetDto.getRemarks());
					qinObsNcFormDet.setClosureStatus(qinObsNcFormDetDto.isClosureStatus());
					qinObsNcFormDet.setQinObsNcForm(qinObsNcForm);
					qinObsNcFormDets.add(qinObsNcFormDet);
				}
				qinObsNcForm.setQinObsNcFormDets(qinObsNcFormDets);
				qinObsNcForm.setIsReworkSubmitted(true);
				qinObsNcForm.setModifiedDate(todayDate);
				QinObsNcForm updatedObsNcForm = qinObsNcFormRepo.save(qinObsNcForm);

				// Sending notification to SBU user on QIN creation
				OfferLetter offerLetter = qinObsNcForm.getOfferLetter();
				String notificationMsg = "Rework Action updated on QIN for OL: " + offerLetter.getOlNumber();
				List<Integer> receivers = Arrays.asList(updatedObsNcForm.getCreatedBy().getId());
				notifyUtil.notifyCqae(notificationMsg, updatedObsNcForm.getId(), null, offerLetter.getCreatedBy(),
						NotificationConstants.QIN, receivers,
						offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
				returnMap.put(true, "Data saved successfully.");
			} else {
				returnMap.put(false, "QIN form not found for the given ID");
			}
		} catch (Exception e) {
			logger.error("Error in saving QIN Obs Nc Form {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
		}

		return returnMap;
	}

	public QinObsNcFormDto getQinNcFormDetails(int offerLetterId) {

		QinObsNcFormDto qinObsNcFormDto = null;
		QinObsNcForm qinObsNcForm = qinObsNcFormRepo.getQinDetailsByOlId(offerLetterId);
		if (qinObsNcForm != null) {
			qinObsNcFormDto = mapper.map(qinObsNcForm, QinObsNcFormDto.class);
			qinObsNcFormDto.setIsReworkSubmitted(qinObsNcForm.getIsReworkSubmitted());
			boolean qinCloseStatus = true;
			List<QinObsNcFormDet> qinObsNcFormDets = qinObsNcForm.getQinObsNcFormDets();
			for (QinObsNcFormDet qinObsNcFormDet : qinObsNcFormDets) {
				Boolean closureStatus = qinObsNcFormDet.getClosureStatus();
				if (closureStatus == null || !closureStatus) {
					qinCloseStatus = false;
					break;
				}
			}
			qinObsNcFormDto.setIsQinClosed(qinCloseStatus);
			return qinObsNcFormDto;
		} else {

			return qinObsNcFormDto;
		}

	}

}
