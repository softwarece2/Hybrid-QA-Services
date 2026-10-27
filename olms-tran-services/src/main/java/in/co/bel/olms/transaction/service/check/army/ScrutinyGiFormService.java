package in.co.bel.olms.transaction.service.check.army;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyGiForm;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyGiFormRepository;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyGiFormDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyGiFormDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyGiForm;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyGiFormDet;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyGiFormDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyGiFormRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyGiFormService {

	private final ChecklistScrutinyGiFormRepository checklistScrutinyGiFormRepository;

	private final ScrutinyGiFormRepository scrutinyGiFormRepository;

	private final ScrutinyGiFormDetRepository scrutinyGiFormDetRepository;

	private final SupplyOrderService supplyOrderService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyGiFormService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	public ScrutinyGiForm saveScrutinyGiForm(ScrutinyGiFormDto scrutinyGiFormDto) {

		List<ScrutinyGiFormDet> scrutinyGiFormDetList;
		ScrutinyGiForm scrutinyGiForm;
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(scrutinyGiFormDto.getSupplyOrderId());
		if (scrutinyGiFormDto.getId() != 0) {

			Optional<ScrutinyGiForm> scrutinyGiFormOpt = scrutinyGiFormRepository.findById(scrutinyGiFormDto.getId());
			if (scrutinyGiFormOpt.isPresent()) {
				scrutinyGiForm = scrutinyGiFormOpt.get();
				scrutinyGiForm.setModifiedDatetime(new Date());
			} else {
				throw new DataNotFoundException(
						"Save : Scrutiny Gi Form not found by  Id: " + scrutinyGiFormDto.getId());
			}

		} else {
			scrutinyGiForm = mapper.map(scrutinyGiFormDto, ScrutinyGiForm.class);
			scrutinyGiForm.setCreatedDatetime(new Date());
		}
		scrutinyGiForm.setSupplyOrder(supplyOrder);
		boolean isValid = entityValidator.isValidEntity(scrutinyGiForm);
		if (isValid) {
			scrutinyGiForm = scrutinyGiFormRepository.save(scrutinyGiForm);
		} else {
			throw new DataNotValidException("ScrutinyGiForm save:" + scrutinyGiForm.toString());
		}

		scrutinyGiFormDetList = prepareScrutinyGiFormObj(scrutinyGiFormDto, scrutinyGiForm);
		scrutinyGiForm.setScrutinyGiFormDets(scrutinyGiFormDetList);
		return scrutinyGiForm;
	}

	private List<ScrutinyGiFormDet> prepareScrutinyGiFormObj(ScrutinyGiFormDto scrutinyGiFormDto,
			ScrutinyGiForm scrutinyGiForm) {
		List<ScrutinyGiFormDet> scrutinyGiFormDets = new ArrayList<>();
		for (ScrutinyGiFormDetDto detDto : scrutinyGiFormDto.getScrutinyGiFormDetDtos()) {
			ScrutinyGiFormDet detGiForm;
			if (detDto.getId() == 0) {
				detGiForm = mapper.map(detDto, ScrutinyGiFormDet.class);
				detGiForm.setCreatedDatetime(new Date());
			} else {
				Optional<ScrutinyGiFormDet> detBoopt = scrutinyGiFormDetRepository.findById(detDto.getId());
				if (detBoopt.isPresent()) {
					detGiForm = detBoopt.get();
				} else {
					detGiForm = mapper.map(detDto, ScrutinyGiFormDet.class);
					detGiForm.setCreatedDatetime(new Date());
				}
			}

			Optional<ChecklistScrutinyGiForm> checkbo = checklistScrutinyGiFormRepository
					.findById(detDto.getChecklistScrutinyGiFormId());
			if (checkbo.isPresent()) {
				detGiForm.setChecklistScrutinyGiForm(checkbo.get());
			} else {
				throw new DataNotFoundException(
						"ScrutinyGiForm save: checklist not found by Id: " + detDto.getChecklistScrutinyGiFormId());
			}
			detGiForm.setObsSts(detDto.getObsSts());
			detGiForm.setRemarks(detDto.getRemarks());
			detGiForm.setScrutinyGiForm(scrutinyGiForm);

			// Add code for the observation status

			boolean isvalid = entityValidator.isValidEntity(detGiForm);
			if (isvalid) {
				scrutinyGiFormDets.add(detGiForm);
			} else {
				throw new DataNotValidException("ScrutinySo save: det " + detGiForm.toString());
			}

		}

		scrutinyGiFormDets = scrutinyGiFormDetRepository.saveAll(scrutinyGiFormDets);
		return scrutinyGiFormDets;
	}

	/**
	 * 
	 * @param scrutinySoDets
	 * @return
	 */
	public List<ScrutinyGiFormDetDto> prepareGiFormDets(List<ScrutinyGiFormDet> scrutinyGiFormDets) {
		List<ScrutinyGiFormDetDto> scrutinyGiFormDetDtos = new ArrayList<>();
		for (ScrutinyGiFormDet scrutinyGiFormDet : scrutinyGiFormDets) {
			ScrutinyGiFormDetDto scrutinyGiFormDetDto = new ScrutinyGiFormDetDto();
			scrutinyGiFormDetDto.setId(scrutinyGiFormDet.getId());
			ChecklistScrutinyGiForm checkSo = scrutinyGiFormDet.getChecklistScrutinyGiForm();
			scrutinyGiFormDetDto.setChecklistScrutinyGiFormId(checkSo.getId());
			scrutinyGiFormDetDto.setSlNo(checkSo.getSlNo());
			scrutinyGiFormDetDto.setReference(checkSo.getReference());
			scrutinyGiFormDetDto.setScrutinyGiFormId(scrutinyGiFormDet.getScrutinyGiForm().getId());
			scrutinyGiFormDetDto.setLastUpdatedBy(scrutinyGiFormDet.getLastUpdatedBy());
			if (StringUtils.isNotBlank(scrutinyGiFormDet.getRemarks())) {
				scrutinyGiFormDetDto.setRemarks(scrutinyGiFormDet.getRemarks());
			} else {
				scrutinyGiFormDetDto.setRemarks("");
			}
			scrutinyGiFormDetDto.setObsType(checkSo.getObsType());
			if (StringUtils.isNotBlank(scrutinyGiFormDet.getObsSts())) {
				scrutinyGiFormDetDto.setObsSts(scrutinyGiFormDet.getObsSts());
			} else {
				scrutinyGiFormDetDto.setObsSts("");
			}

			scrutinyGiFormDetDto.setSplit(checkSo.getSplit());
			scrutinyGiFormDetDtos.add(scrutinyGiFormDetDto);
		}
		Collections.sort(scrutinyGiFormDetDtos, new Comparator<ScrutinyGiFormDetDto>() {
			public int compare(ScrutinyGiFormDetDto o1, ScrutinyGiFormDetDto o2) {
				return o1.getChecklistScrutinyGiFormId().compareTo(o2.getChecklistScrutinyGiFormId());
			}
		});
		return scrutinyGiFormDetDtos;
	}

	/**
	 * 
	 * @author Manasa B
	 * @since 24-02-2023
	 * @param soId
	 * @return
	 */
	public ScrutinyGiFormDto getScrutinyGiFormBySoId(int soId) {
		ScrutinyGiFormDto scrutinyGiFormDto = new ScrutinyGiFormDto();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		ScrutinyGiForm scrutinyGiForm = scrutinyGiFormRepository.getScrutinyGiFormBySupplyOrder(supplyOrder);

		List<ScrutinyGiFormDetDto> inspGiFormDetDto = new ArrayList<>();
		if (scrutinyGiForm == null) {
			scrutinyGiFormDto.setId(0);
			scrutinyGiFormDto.setSubmittedDate(null);
			List<ChecklistScrutinyGiForm> chkSGiForm = checklistScrutinyGiFormRepository.findAll();
			for (ChecklistScrutinyGiForm checklistScrutinyGiForm : chkSGiForm) {
				ScrutinyGiFormDetDto detDto = new ScrutinyGiFormDetDto();
				detDto.setId(0);
				detDto.setChecklistScrutinyGiFormId(checklistScrutinyGiForm.getId());
				detDto.setSlNo(checklistScrutinyGiForm.getSlNo());
				detDto.setReference(checklistScrutinyGiForm.getReference());
				detDto.setSplit(checklistScrutinyGiForm.getSplit());
				detDto.setObsType(checklistScrutinyGiForm.getObsType());
				detDto.setObsSts("");
				detDto.setRemarks("");
				inspGiFormDetDto.add(detDto);
			}

		} else {
			scrutinyGiFormDto = mapper.map(scrutinyGiForm, ScrutinyGiFormDto.class);
			SupplyOrder so = scrutinyGiForm.getSupplyOrder();
			scrutinyGiFormDto.setSupplyOrderId(so.getId());
			if (null != scrutinyGiForm.getSubmittedDate()) {
				scrutinyGiFormDto.setSubmittedDate(dateFormat.format(scrutinyGiForm.getSubmittedDate()));
			}
			for (ScrutinyGiFormDet detBo : scrutinyGiForm.getScrutinyGiFormDets()) {
				ScrutinyGiFormDetDto detDto = new ScrutinyGiFormDetDto();
				ChecklistScrutinyGiForm checklistScrutinyGiForm = detBo.getChecklistScrutinyGiForm();
				detDto.setId(detBo.getId());
				detDto.setChecklistScrutinyGiFormId(checklistScrutinyGiForm.getId());
				detDto.setSlNo(checklistScrutinyGiForm.getSlNo());
				detDto.setReference(checklistScrutinyGiForm.getReference());
				detDto.setSplit(checklistScrutinyGiForm.getSplit());
				detDto.setObsType(checklistScrutinyGiForm.getObsType());
				detDto.setObsSts(detBo.getObsSts());
				detDto.setRemarks(detBo.getRemarks());
				inspGiFormDetDto.add(detDto);
			}
		}

		Collections.sort(inspGiFormDetDto, new Comparator<ScrutinyGiFormDetDto>() {
			@Override
			public int compare(ScrutinyGiFormDetDto o1, ScrutinyGiFormDetDto o2) {

				return o1.getChecklistScrutinyGiFormId().compareTo(o2.getChecklistScrutinyGiFormId());
			}

		});

		scrutinyGiFormDto.setScrutinyGiFormDetDtos(inspGiFormDetDto);
		return scrutinyGiFormDto;
	}

	public ScrutinyGiForm submitScrutinyGiForm(@Valid ScrutinyGiFormDto scrutinyGiFormDto) {
		ScrutinyGiForm scrutinyGiForm = saveScrutinyGiForm(scrutinyGiFormDto);
		scrutinyGiForm.setSubmittedDate(new Date());
		boolean isValid = entityValidator.isValidEntity(scrutinyGiForm);
		if (isValid) {
			scrutinyGiForm = scrutinyGiFormRepository.save(scrutinyGiForm);
		} else {
			throw new DataNotFoundException("ScrutinyGiForm Submit:" + scrutinyGiForm.toString());
		}
		return scrutinyGiForm;
	}
}
