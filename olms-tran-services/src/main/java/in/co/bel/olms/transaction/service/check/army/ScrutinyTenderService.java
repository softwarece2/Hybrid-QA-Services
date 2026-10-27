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

import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyTender;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyTenderRepository;
import in.co.bel.olms.transaction.dto.PoObservationDetDto;
import in.co.bel.olms.transaction.dto.PoObservationDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyTenderDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyTenderDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTender;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTenderDet;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyTenderDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyTenderRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.transaction.service.so.PoObservationService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyTenderService {

	private final SupplyOrderService supplyOrderService;

	private final Mapper mapper;

	private final ScrutinyTenderRepository scrutinyTenderRepo;

	private final ScrutinyTenderDetRepository scrutinyTenderDetRepo;

	private final ChecklistScrutinyTenderRepository checklistScrutinyTenderRepos;

	private final PoObservationService poObservationService;

	private final EmployeeRepository employeeRepository;

	private final NotifyUtil notifyUtil;

	private final EntityValidator entityValidator;

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyTenderService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public ScrutinyTender saveScrutinyTender(@Valid ScrutinyTenderDto scrutinyTenderDto) {
		List<ScrutinyTenderDet> scrutinyTenderDetlist = new ArrayList<>();
		ScrutinyTender scrutinyTender;
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(scrutinyTenderDto.getSupplyOrderId());
		if (scrutinyTenderDto.getId() != 0) {

			Optional<ScrutinyTender> scrutinyTenderOptional = scrutinyTenderRepo.findById(scrutinyTenderDto.getId());
			if (scrutinyTenderOptional.isPresent()) {
				scrutinyTender = scrutinyTenderOptional.get();
			} else {
				throw new DataNotFoundException(
						"Save : Scrutiny Tender not found by  Id: " + scrutinyTenderDto.getId());
			}
		} else {
			scrutinyTender = mapper.map(scrutinyTenderDto, ScrutinyTender.class);
			scrutinyTender.setCreatedDatetime(new Date());
		}
		scrutinyTender.setSupplyOrder(supplyOrder);
		boolean isValid = entityValidator.isValidEntity(scrutinyTender);
		if (isValid) {
			scrutinyTender = scrutinyTenderRepo.save(scrutinyTender);
		} else {
			throw new DataNotValidException("ScrutinySo save:" + scrutinyTender.toString());
		}

		scrutinyTenderDetlist = prepareScrutinyTenderObj(scrutinyTenderDto, scrutinyTender);
		scrutinyTender.setScrutinyTenderDets(scrutinyTenderDetlist);
		return scrutinyTender;
	}

	private List<ScrutinyTenderDet> prepareScrutinyTenderObj(ScrutinyTenderDto scrutinyTenderDto,
			ScrutinyTender scrutinyTender) {
		List<ScrutinyTenderDet> scrutinyTenderDets = new ArrayList<>();

		for (ScrutinyTenderDetDto scrutinyTenderDetDto : scrutinyTenderDto.getScrutinyTenderDetDtos()) {
			ScrutinyTenderDet scrutinyTenderDet;
			if (scrutinyTenderDetDto.getId() == 0) {
				scrutinyTenderDet = mapper.map(scrutinyTenderDetDto, ScrutinyTenderDet.class);
			} else {
				Optional<ScrutinyTenderDet> scrutinyTenderDetopt = scrutinyTenderDetRepo
						.findById(scrutinyTenderDetDto.getId());
				if (scrutinyTenderDetopt.isPresent()) {
					scrutinyTenderDet = scrutinyTenderDetopt.get();
				} else {
					scrutinyTenderDet = mapper.map(scrutinyTenderDetDto, ScrutinyTenderDet.class);
				}
			}

			Optional<ChecklistScrutinyTender> checklistScrutinyTenderOptional = checklistScrutinyTenderRepos
					.findById(scrutinyTenderDetDto.getChecklistScrutinyTenderId());
			if (checklistScrutinyTenderOptional.isPresent()) {
				scrutinyTenderDet.setChecklistScrutinyTender(checklistScrutinyTenderOptional.get());
			} else {
				throw new DataNotFoundException("ScrutinySo save: checklist not found by Id: "
						+ scrutinyTenderDetDto.getChecklistScrutinyTenderId());
			}
		//	ChecklistScrutinyTender checklistScrutinyTender = checklistScrutinyTenderOptional.get();
			scrutinyTenderDet.setObsSts(scrutinyTenderDetDto.getObsSts());
			scrutinyTenderDet.setRemarks(scrutinyTenderDetDto.getRemarks());
			scrutinyTenderDet.setActionAndRef(scrutinyTenderDetDto.getActionAndRef());
			scrutinyTenderDet.setScrutinyTender(scrutinyTender);
			
		//	scrutinyTenderDet.setObsSts(checklistScrutinyTender.getObsType());
		//	if (StringUtils.isNotBlank(scrutinyTenderDet.getObsSts())) 
		//	{
		//		scrutinyTenderDet.setObsSts(scrutinyTenderDet.getObsSts());
		//	}		
		//	else {
		//		scrutinyTenderDet.setObsSts("");
		//	}

			boolean isvalid = entityValidator.isValidEntity(scrutinyTenderDet);
			if (isvalid) {
				scrutinyTenderDets.add(scrutinyTenderDet);
			} else {
				throw new DataNotValidException("ScrutinyTender save: det " + scrutinyTenderDet.toString());
			}
		}

		scrutinyTenderDets = scrutinyTenderDetRepo.saveAll(scrutinyTenderDets);
		return scrutinyTenderDets;
	}

	public List<ScrutinyTenderDetDto> prepareTenderDets(List<ScrutinyTenderDet> scrutinyTenderDets) {
		List<ScrutinyTenderDetDto> scrutinyTenderDetDtos = new ArrayList<>();
		for (ScrutinyTenderDet scrutinyTenderDet : scrutinyTenderDets) {
			ScrutinyTenderDetDto scrutinyDetDto = new ScrutinyTenderDetDto();
			scrutinyDetDto.setId(scrutinyTenderDet.getId());
			ChecklistScrutinyTender checklist = scrutinyTenderDet.getChecklistScrutinyTender();
			scrutinyDetDto.setChecklistScrutinyTenderId(checklist.getId());
			ScrutinyTender scrutinyTender = scrutinyTenderDet.getScrutinyTender();
			scrutinyDetDto.setScrutinyTenderId(scrutinyTender.getId());
			scrutinyDetDto.setSlNo(checklist.getSlNo());
			scrutinyDetDto.setReference(checklist.getReference());
			if (StringUtils.isNotBlank(scrutinyTenderDet.getRemarks())) {
				scrutinyDetDto.setRemarks(scrutinyTenderDet.getRemarks());
			} else {
				scrutinyDetDto.setRemarks("");
			}
			if (StringUtils.isNotBlank(scrutinyTenderDet.getActionAndRef())) {
				scrutinyDetDto.setActionAndRef(scrutinyTenderDet.getActionAndRef());
			} else {
				scrutinyDetDto.setActionAndRef("");
			}
			scrutinyDetDto.setObsType(checklist.getObsType());
			if (StringUtils.isNotBlank(scrutinyTenderDet.getObsSts())) {
				scrutinyDetDto.setObsSts(scrutinyTenderDet.getObsSts());
			} else {
				scrutinyDetDto.setObsSts("");
			}

			scrutinyDetDto.setSplit(checklist.getSplit());
			scrutinyTenderDetDtos.add(scrutinyDetDto);
		}
		Collections.sort(scrutinyTenderDetDtos, new Comparator<ScrutinyTenderDetDto>() {
			public int compare(ScrutinyTenderDetDto o1, ScrutinyTenderDetDto o2) {
				return o1.getChecklistScrutinyTenderId().compareTo(o2.getChecklistScrutinyTenderId());
			}
		});
		return scrutinyTenderDetDtos;
	}

	public ScrutinyTenderDto getScrutinyTenderBySoId(int soId) {
		ScrutinyTenderDto scrutinyTenderDto = new ScrutinyTenderDto();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		ScrutinyTender scrutinyTender = scrutinyTenderRepo.getScrutinyTendeBySupplyOrder(supplyOrder);

		List<ScrutinyTenderDetDto> inspTenderDetDto = new ArrayList<>();
		if (scrutinyTender == null) {
			scrutinyTenderDto.setId(0);
			scrutinyTenderDto.setSubmittedDatetime(null);
			List<ChecklistScrutinyTender> chkSTender = checklistScrutinyTenderRepos.findAll();
			for (ChecklistScrutinyTender checklistScrutinyTender : chkSTender) {
				ScrutinyTenderDetDto detDto = new ScrutinyTenderDetDto();
				detDto.setId(0);
				detDto.setChecklistScrutinyTenderId(checklistScrutinyTender.getId());
				detDto.setSlNo(checklistScrutinyTender.getSlNo());
				detDto.setReference(checklistScrutinyTender.getReference());
				detDto.setSplit(checklistScrutinyTender.getSplit());
				detDto.setObsType(checklistScrutinyTender.getObsType());
				detDto.setObsSts("");
				detDto.setRemarks("");
				detDto.setActionAndRef("");
				inspTenderDetDto.add(detDto);
			}

		} else {
			scrutinyTenderDto = mapper.map(scrutinyTender, ScrutinyTenderDto.class);
			SupplyOrder so = scrutinyTender.getSupplyOrder();
			scrutinyTenderDto.setSupplyOrderId(so.getId());
			if (null != scrutinyTender.getSubmittedDatetime()) {
				scrutinyTenderDto.setSubmittedDatetime(dateFormat.format(scrutinyTender.getSubmittedDatetime()));
			}
			for (ScrutinyTenderDet detBo : scrutinyTender.getScrutinyTenderDets()) {
				ScrutinyTenderDetDto detDto = new ScrutinyTenderDetDto();
				ChecklistScrutinyTender checklistScrutinyTender = detBo.getChecklistScrutinyTender();
				detDto.setId(detBo.getId());
				detDto.setChecklistScrutinyTenderId(checklistScrutinyTender.getId());
				detDto.setSlNo(checklistScrutinyTender.getSlNo());
				detDto.setReference(checklistScrutinyTender.getReference());
				detDto.setSplit(checklistScrutinyTender.getSplit());
				detDto.setObsType(checklistScrutinyTender.getObsType());
				detDto.setObsSts(detBo.getObsSts());
				detDto.setRemarks(detBo.getRemarks());
				detDto.setActionAndRef(detBo.getActionAndRef());
				inspTenderDetDto.add(detDto);
			}
		}

		Collections.sort(inspTenderDetDto, new Comparator<ScrutinyTenderDetDto>() {
			@Override
			public int compare(ScrutinyTenderDetDto o1, ScrutinyTenderDetDto o2) {

				return o1.getChecklistScrutinyTenderId().compareTo(o2.getChecklistScrutinyTenderId());
			}

		});

		scrutinyTenderDto.setScrutinyTenderDetDtos(inspTenderDetDto);
		return scrutinyTenderDto;
	}
	
	public ScrutinyTenderDto getSubmittedScrutinyTenderBySoId(int soId) {
		ScrutinyTenderDto scrutinyTenderDto = new ScrutinyTenderDto();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		ScrutinyTender scrutinyTender = scrutinyTenderRepo.getScrutinyTendeBySupplyOrder(supplyOrder);

		List<ScrutinyTenderDetDto> inspTenderDetDto = new ArrayList<>();
		if (scrutinyTender != null) {
			scrutinyTenderDto = mapper.map(scrutinyTender, ScrutinyTenderDto.class);
			SupplyOrder so = scrutinyTender.getSupplyOrder();
			scrutinyTenderDto.setSupplyOrderId(so.getId());
			if (null != scrutinyTender.getSubmittedDatetime()) {
				scrutinyTenderDto.setSubmittedDatetime(dateFormat.format(scrutinyTender.getSubmittedDatetime()));
			}
			for (ScrutinyTenderDet detBo : scrutinyTender.getScrutinyTenderDets()) {
				ScrutinyTenderDetDto detDto = new ScrutinyTenderDetDto();
				ChecklistScrutinyTender checklistScrutinyTender = detBo.getChecklistScrutinyTender();
				detDto.setId(detBo.getId());
				detDto.setChecklistScrutinyTenderId(checklistScrutinyTender.getId());
				detDto.setSlNo(checklistScrutinyTender.getSlNo());
				detDto.setReference(checklistScrutinyTender.getReference());
				detDto.setSplit(checklistScrutinyTender.getSplit());
				detDto.setObsType(checklistScrutinyTender.getObsType());
				detDto.setObsSts(detBo.getObsSts());
				detDto.setRemarks(detBo.getRemarks());
				detDto.setActionAndRef(detBo.getActionAndRef());
				inspTenderDetDto.add(detDto);
		}
		}
		Collections.sort(inspTenderDetDto, new Comparator<ScrutinyTenderDetDto>() {
			@Override
			public int compare(ScrutinyTenderDetDto o1, ScrutinyTenderDetDto o2) {

				return o1.getChecklistScrutinyTenderId().compareTo(o2.getChecklistScrutinyTenderId());
			}

		});

		scrutinyTenderDto.setScrutinyTenderDetDtos(inspTenderDetDto);
		return scrutinyTenderDto;
	}

	public ScrutinyTender submitScrutinyTender(@Valid ScrutinyTenderDto scrutinyTenderDto) {
		ScrutinyTender scrutinyTender = saveScrutinyTender(scrutinyTenderDto);
		scrutinyTender.setCreatedDatetime(new Date());
		scrutinyTender.setSubmittedDatetime(new Date());
		
		
		boolean isValid = entityValidator.isValidEntity(scrutinyTender);
		if (isValid) {
			scrutinyTender = scrutinyTenderRepo.save(scrutinyTender);
		} else {
			throw new DataNotFoundException("ScrutinyTender Submit:" + scrutinyTender.toString());
		}

		
		List<ScrutinyTenderDet> scrTenderDets = scrutinyTender.getScrutinyTenderDets();

		List<PoObservationDetDto> obsList = new ArrayList<>();

		for (ScrutinyTenderDet detBo : scrTenderDets) {
			boolean isObs = isObservation(detBo);
			if (isObs) {
				PoObservationDetDto detDto = new PoObservationDetDto();
				detDto.setObservation(detBo.getRemarks());
				detDto.setScrutinyTenderDetId(detBo.getId());
				obsList.add(detDto);
			}
		}

		if(!obsList.isEmpty())
		{
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(0);
			poObservationDto.setSupplyOrderId(scrutinyTender.getSupplyOrder().getId());
			poObservationDto.setObsList(obsList);
			poObservationService.createPoObservation(poObservationDto);
		}
		
		try {
			SupplyOrder supplyOrder = scrutinyTender.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdBysoId(supplyOrder.getId());
			notifyUtil.NotifySBUnCqae("Scrutiny Tender  is completed for the SO/PO no " + supplyOrder.getOrderNo(),
					supplyOrder.getId(), supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(),
					 empId);
		} catch (Exception e) {
			logger.error("Notification error in Scrutiny SO/PO submitted for the order no {} ", e.getMessage());
			logger.error("Error in Scrutiny SO/PO submitted for the order no ", e);
		}
		
		return scrutinyTender;

	}

	public boolean isObservation(ScrutinyTenderDet tenderDet) {
		
		return StringUtils.isNotBlank(tenderDet.getRemarks());

	}
}
