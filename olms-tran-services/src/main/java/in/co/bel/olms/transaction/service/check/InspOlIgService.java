package in.co.bel.olms.transaction.service.check;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.entity.checklist.ChecklistOlIG;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.checklist.ChecklistOlIGRepository;
import in.co.bel.olms.transaction.dto.check.InspOlIgDetDto;
import in.co.bel.olms.transaction.dto.check.InspOlIgDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.InspOlIg;
import in.co.bel.olms.transaction.entity.check.InspOlIgDet;
import in.co.bel.olms.transaction.repository.check.InspOlIgDetRepository;
import in.co.bel.olms.transaction.repository.check.InspOlIgRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import in.co.bel.olms.validator.InputValidation;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspOlIgService {

	private final InspOlIgRepository inspOlIgRepository;

	private final InspOlIgDetRepository inspOlIgDetRepo;

	private final Mapper mapper;

	private final ChecklistOlIGRepository checklistOlIGRepository;

	private final OfferLetterService offerLetterService;

	private final EntityValidator entityValidator;

	private final NotifyUtil notifyUtil;

	private final EmployeeRepository employeeRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(InspOlIgService.class);	
	
	DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	/**
	 * 
	 * @param inspOlIgDto
	 * @return
	 */
	@Transactional
	public InspOlIg saveInspOlIg(InspOlIgDto inspOlIgDto) {
		isValidDto(inspOlIgDto);

		InspOlIg inspOlIg = mapper.map(inspOlIgDto, InspOlIg.class);

		OfferLetter offerLetter = offerLetterService.getOfferLetterById(inspOlIgDto.getOfferLetterId());
		inspOlIg.setOfferLetter(offerLetter);
		inspOlIg.setCreatedDatetime(new Date());
		boolean isValid = entityValidator.isValidEntity(inspOlIg);
		if (isValid) {
			inspOlIg = inspOlIgRepository.save(inspOlIg);
		} else {
			throw new DataNotValidException("InspOlIg save: " + inspOlIg.toString());
		}

		List<InspOlIgDet> inspOlIgDets = prepareOlIgDets(inspOlIgDto.getInspOlIgDetDtos(), inspOlIg);
		inspOlIgDets = inspOlIgDetRepo.saveAll(inspOlIgDets);
		inspOlIg.setInspOlIgDets(inspOlIgDets);
		isValid = entityValidator.isValidEntity(inspOlIg);
		if (isValid) {
			inspOlIg = inspOlIgRepository.save(inspOlIg);
		} else {
			throw new DataNotFoundException("InspOlIg save: " + inspOlIg.toString());
		}
		return inspOlIg;
	}

	private boolean isValidDto(InspOlIgDto inspOlIgDto) {
		for (InspOlIgDetDto detDto : inspOlIgDto.getInspOlIgDetDtos()) {
			if (StringUtils.isNotBlank(detDto.getRemarks())) {
				if (!InputValidation.checkRemark(detDto.getRemarks())) {
					throw new DataNotValidException("InspCables det remarks: " + detDto.getRemarks());
				}
			}
		}
		return true;
	}

	private List<InspOlIgDet> prepareOlIgDets(List<InspOlIgDetDto> inspOlIgDetDtos, InspOlIg inspOlIg) {
		List<InspOlIgDet> inspOlIgDets = new ArrayList<>();
		for (InspOlIgDetDto detDto : inspOlIgDetDtos) {
			InspOlIgDet olIgDet;

			if (detDto.getId() == 0) {
				olIgDet = new InspOlIgDet();
			} else {
				Optional<InspOlIgDet> olIgOptional = inspOlIgDetRepo.findById(detDto.getId());
				if (olIgOptional.isPresent()) {
					olIgDet = olIgOptional.get();
				} else {
					olIgDet = new InspOlIgDet();
				}
			}

			Optional<ChecklistOlIG> checkOlIG = checklistOlIGRepository.findById(detDto.getChecklistId());
			if (checkOlIG.isPresent()) {
				olIgDet.setChecklistOlIG(checkOlIG.get());
			} else {
				throw new DataNotFoundException("InspOlIG save: checklist not found by Id: " + detDto.getChecklistId());
			}
			olIgDet.setRemarks(detDto.getRemarks());

			olIgDet.setInspOlIg(inspOlIg);

			boolean isValid = entityValidator.isValidEntity(olIgDet);
			if (isValid) {
				inspOlIgDets.add(olIgDet);
			} else {
				throw new DataNotValidException("InspOlIg save: det " + olIgDet.toString());
			}
		}

		return inspOlIgDets;
	}

	/**
	 * 
	 * @param inspOlIgDto
	 * @return
	 */
	@Transactional
	public InspOlIg submitInspOlIg(InspOlIgDto inspOlIgDto) {
		InspOlIg inspOlIg = saveInspOlIg(inspOlIgDto);
		boolean isValid = entityValidator.isValidEntity(inspOlIg);
		if (isValid) {
			inspOlIg = inspOlIgRepository.save(inspOlIg);
		} else {
			throw new DataNotValidException("InspOlIg submit: " + inspOlIg.toString());
		}
		
		try { 
			Integer empId =	employeeRepository.getEmpIdByolId(inspOlIg.getOfferLetter().getId());
			notifyUtil.NotifySBUnCqae("Scrutiny OL IG inspection is completed for the OL no " + inspOlIg.getOfferLetter().getOlNumber() ,
					 1, inspOlIg.getOfferLetter().getOlNumber(), 1, NotificationConstants.OL,    inspOlIg.getOfferLetter().getSaleOrder().getSupplyOrder().getSalesGroup().getId() ,
					 empId);
		} catch (Exception e) {
			logger.error("Notification error in Scrutiny OL IG inspection submitted for the order no {} ", e.getMessage());
			logger.error("Error in Scrutiny OL IG inspection submitted for the order no  ", e);
		}
		
		return inspOlIg;
	}

	/**
	 * 
	 * @param olId
	 * @return
	 */
	public InspOlIgDto getByOlId(int olId) {
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
		InspOlIg inspOlIg = inspOlIgRepository.getByInspOlIg(offerLetter);

		InspOlIgDto dto = new InspOlIgDto();
		List<InspOlIgDetDto> inspOlIgDetDtos = new ArrayList<>();
		if (inspOlIg == null) {
			dto.setId(0);
			List<ChecklistOlIG> chklists = checklistOlIGRepository.findAll();
			for (ChecklistOlIG bo : chklists) {
				InspOlIgDetDto detDto = new InspOlIgDetDto();
				detDto.setId(0);
				detDto.setChecklistId(bo.getId());
				detDto.setSlNo(bo.getSlno());
				detDto.setReference(bo.getReference());
				detDto.setSplit(bo.getSplit());
				detDto.setObsType(bo.getObsType());
				detDto.setObsSts("");
				inspOlIgDetDtos.add(detDto);
			}
		}

		else {
			dto = mapper.map(inspOlIg, InspOlIgDto.class);
			for (InspOlIgDet olIgDet : inspOlIg.getInspOlIgDets()) {
				InspOlIgDetDto detDto = new InspOlIgDetDto();
				detDto.setId(olIgDet.getId());
				ChecklistOlIG checklistOlIG = olIgDet.getChecklistOlIG();
				detDto.setChecklistId(checklistOlIG.getId());
				detDto.setSlNo(checklistOlIG.getSlno());
				detDto.setReference(checklistOlIG.getReference());

				if (!StringUtils.isNotBlank(olIgDet.getRemarks())) {

					detDto.setRemarks("");
				} else {
					detDto.setRemarks(olIgDet.getRemarks());

				}
				detDto.setObsType(checklistOlIG.getObsType());
				detDto.setObsSts(olIgDet.getObsSts());
				detDto.setSplit(checklistOlIG.getSplit());
				inspOlIgDetDtos.add(detDto);

			}

		}
		Collections.sort(inspOlIgDetDtos, new Comparator<InspOlIgDetDto>() {

			@Override
			public int compare(InspOlIgDetDto o1, InspOlIgDetDto o2) {
				return o2.getChecklistId().compareTo(o1.getChecklistId());
			}
		});

		dto.setInspOlIgDetDtos(inspOlIgDetDtos);
		return dto;

	}
	
	/**
	 * 
	 * @param inspOlIgDets
	 * @return
	 */
	public List<InspOlIgDetDto> prepareDetBos(List<InspOlIgDet> inspOlIgDets) {
		List<InspOlIgDetDto> inspOlIgDetDtos = new ArrayList<>();
		for (InspOlIgDet inspOlIgDet : inspOlIgDets) {
			InspOlIgDetDto olIgDetDto = new InspOlIgDetDto();
			olIgDetDto.setId(inspOlIgDet.getId());
			ChecklistOlIG checkOlIg = inspOlIgDet.getChecklistOlIG();
			olIgDetDto.setChecklistId(checkOlIg.getId());
			olIgDetDto.setSlNo(checkOlIg.getSlno());
			olIgDetDto.setReference(checkOlIg.getReference());
			if (StringUtils.isNotBlank(inspOlIgDet.getRemarks())) {
				olIgDetDto.setRemarks(inspOlIgDet.getRemarks());
			} else {
				olIgDetDto.setRemarks("");
			}
			olIgDetDto.setObsType(checkOlIg.getObsType());
			olIgDetDto.setObsSts(inspOlIgDet.getObsSts());
			olIgDetDto.setSplit(checkOlIg.getSplit());
			inspOlIgDetDtos.add(olIgDetDto);
		}
		Collections.sort(inspOlIgDetDtos, new Comparator<InspOlIgDetDto>() {
			public int compare(InspOlIgDetDto o1, InspOlIgDetDto o2) {
				return o1.getChecklistId().compareTo(o2.getChecklistId());
			}
		});
		return inspOlIgDetDtos;
	}

}
