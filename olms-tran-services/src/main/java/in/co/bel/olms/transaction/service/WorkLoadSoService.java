package in.co.bel.olms.transaction.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.olms.master.entity.InspPhaseActivities;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.InspPhaseActivitiesRepository;
import in.co.bel.olms.transaction.dto.WorkLoadDto;
import in.co.bel.olms.transaction.dto.WorkloadInspActDetDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.WorkLoad;
import in.co.bel.olms.transaction.entity.WorkloadInspActDet;
import in.co.bel.olms.transaction.repository.WorkLoadRepository;
import in.co.bel.olms.transaction.repository.WorkloadInspActDetRepository;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 26-04-2023
 *
 */
@Service
@RequiredArgsConstructor
public class WorkLoadSoService {

	private final WorkLoadRepository workLoadRepository;

	private final Mapper mapper;

	private final SupplyOrderService supplyOrderService;

	private final EntityValidator entityValidator;

	private final WorkloadInspActDetRepository workloadInspActDetRepo;


	private final InspPhaseActivitiesRepository inspPhaseActRepo;



	Logger logger = LoggerFactory.getLogger(WorkLoadSoService.class);

	DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private WorkLoadDto updateSodetails(SupplyOrder so, WorkLoadDto workLoadDto) {
		workLoadDto.setSupplyOrderId(so.getId());
		workLoadDto.setSoOrderNo(so.getOrderNo());
		workLoadDto.setValue(so.getValue());
		workLoadDto.setTotalQty(so.getTotalQty());
		workLoadDto.setNoOfItems(so.getNoOfItems());
		if (so.getIndenter() != null) {
			workLoadDto.setOrderIssuedBy(so.getIndenter().getName());
		}
		if (so.getSoType() != null) {
			workLoadDto.setMainEqptItem(so.getSoType().getSoType());
		}
		workLoadDto.setSoDate(outputDateFormat.format(so.getOrderDate()));
		if (null != so.getDd()) {
			workLoadDto.setOriginalDd(outputDateFormat.format(so.getDd()));
		} else {
			workLoadDto.setOriginalDd("");
		}

		if (null != so.getSupplier()) {
			workLoadDto.setSupplierName(so.getSupplier().getName());
		} else {
			workLoadDto.setSupplierName("");
		}
		
		
		if(StringUtils.isNoneBlank(so.getFileNo()))
		{
			workLoadDto.setFileNo(so.getFileNo());
		}
		return workLoadDto;
	}

	/**
	 * Covert the entity to Dto
	 * 
	 * @since 18-04-2023
	 * @param workLoad
	 * @return
	 */
	public WorkLoadDto convertBoToDto(WorkLoad workLoad) {
		String regex = "[0-9]+"; // regex to check if string contains only digits
		Pattern pattern = Pattern.compile(regex); // compiles the regex
		WorkLoadDto workLoadDto = mapper.map(workLoad, WorkLoadDto.class);
		SupplyOrder so = workLoad.getSupplyOrder();

		workLoadDto = updateSodetails(so, workLoadDto);

		if (workLoad.getPlanSubmittedDate() != null) {
			workLoadDto.setPlanSubmittedDate(outputDateFormat.format(workLoad.getPlanSubmittedDate()));
		} else {
			workLoadDto.setPlanSubmittedDate("");
		}
		if (workLoad.getSubmittedDate() != null) {
			workLoadDto.setSubmittedDate(outputDateFormat.format(workLoad.getSubmittedDate()));
		} else {
			workLoadDto.setSubmittedDate("");
		}

		workLoadDto.setTotalEstDays(workLoad.getTotalEstDays());
		workLoadDto.setTotalActDays(workLoad.getTotalActDays());
		List<WorkloadInspActDetDto> wrkLdDetDtos = new ArrayList<>();
		if (workLoad.getWorkLoadDets() != null) {
			for (WorkloadInspActDet detBo : workLoad.getWorkLoadDets()) {
				WorkloadInspActDetDto detDto = mapper.map(detBo, WorkloadInspActDetDto.class);
				detDto.setInspPhaseActId(detBo.getInspPhaseActivities().getId());
				detDto.setInspPhaseActivity(detBo.getInspPhaseActivities().getActivity());
				detDto.setSlNo(detBo.getInspPhaseActivities().getSlNo());
				Matcher matcherText = pattern.matcher(detBo.getInspPhaseActivities().getSlNo());
				detDto.setBold(matcherText.matches());
				wrkLdDetDtos.add(detDto);
			}
			wrkLdDetDtos.sort(Comparator.comparing(WorkloadInspActDetDto::getSlNo));
		}

		workLoadDto.setWrkLdPreDetDtos(wrkLdDetDtos);
		return workLoadDto;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public WorkLoad getWorkLoadById(int id) {

		Optional<WorkLoad> workLoadOptional = workLoadRepository.findById(id);
		if (workLoadOptional.isPresent()) {
			return workLoadOptional.get();
		} else {
			throw new DataNotFoundException("WorkLoad not found with Id: " + id);
		}
	}

	/**
	 * @since 14-02-2023
	 * @param soId
	 * @return
	 */
	public WorkLoadDto getWorkLoadBySoId(int soId) {
		String regex = "[0-9]+"; // regex to check if string contains only digits
		Pattern pattern = Pattern.compile(regex); // compiles the regex
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		WorkLoad workLoad = workLoadRepository.getWorkLoadBySupplyOrder(supplyOrder);
		WorkLoadDto dto = new WorkLoadDto();
		if (workLoad == null) {
			dto = updateSodetails(supplyOrder, dto);

			List<InspPhaseActivities> activities = inspPhaseActRepo
					.getAllInspPhaseActivitiesByInspPhaseId(MasterConstants.PRE_INSP_PHASE);
			List<WorkloadInspActDetDto> acitivitiesDtos = new ArrayList<>();
			for (InspPhaseActivities act : activities) {
				WorkloadInspActDetDto actdto = new WorkloadInspActDetDto();
				actdto.setInspPhaseActId(act.getId());
				actdto.setInspPhaseActivity(act.getActivity());
				actdto.setSlNo(act.getSlNo());
				Matcher matcherText = pattern.matcher(actdto.getSlNo());
				actdto.setBold(matcherText.matches());
				acitivitiesDtos.add(actdto);
			}
			acitivitiesDtos.sort(Comparator.comparing(WorkloadInspActDetDto::getSlNo));
			dto.setWrkLdPreDetDtos(acitivitiesDtos);

			return dto;
		}
		return convertBoToDto(workLoad);
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@Transactional
	public WorkLoad savePlan(WorkLoadDto workLoadDto) {
		WorkLoad workLoad;
		SupplyOrder supplyOrder;
		if (workLoadDto.getId() != 0) {
			Optional<WorkLoad> workLoadOptional = workLoadRepository.findById(workLoadDto.getId());
			if (workLoadOptional.isPresent()) {
				workLoad = workLoadOptional.get();
				workLoad.setTotalEstDays(workLoadDto.getTotalEstDays());
			} else {
				throw new DataNotFoundException("Save Plan: WorkLoad Not Found By ID : " + workLoadDto.getId());
			}
		} else {
			workLoad = mapper.map(workLoadDto, WorkLoad.class);
			supplyOrder = supplyOrderService.getSupplyOrder(workLoadDto.getSupplyOrderId());
			workLoad.setSupplyOrder(supplyOrder);
			workLoad.setCreatedDatetime(new Date());
			workLoad.setCreatedBy(workLoadDto.getCreatedBy());
			workLoad.setTotalEstDays(workLoadDto.getTotalEstDays());
			
		}
		boolean isValid = entityValidator.isValidEntity(workLoad);
		if (isValid) {
			workLoad = workLoadRepository.save(workLoad);
		} else {
			throw new DataNotValidException(" workLoad save plan: not a valid entity");
		}

		List<WorkloadInspActDet> workLoadInspActDets = preparePlanWorkSoDetBos(workLoadDto.getWrkLdDetDtos(), workLoad);
		workLoadInspActDets = workloadInspActDetRepo.saveAll(workLoadInspActDets);
		workLoad.setWorkLoadDets(workLoadInspActDets);
		return workLoad;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@Transactional
	public WorkLoad saveAct(WorkLoadDto workLoadDto) {
		WorkLoad workLoad;
		if (workLoadDto.getId() != 0) {
			Optional<WorkLoad> workLoadOptional = workLoadRepository.findById(workLoadDto.getId());
			if (workLoadOptional.isPresent()) {
				workLoad = workLoadOptional.get();
				workLoad.setTotalActDays(workLoadDto.getTotalActDays());
			} else {
				throw new DataNotFoundException("Save Actual :  WorkLoad Not Found By ID : " + workLoadDto.getId());
			}
		} else {
			throw new DataNotFoundException("Save Actual: WorkLoad Not Found By ID : " + workLoadDto.getId());
		}

		List<WorkloadInspActDet> workLoadInspActDets = prepareActWorkSoDetBos(workLoadDto.getWrkLdDetDtos(), workLoad);
		workLoadInspActDets = workloadInspActDetRepo.saveAll(workLoadInspActDets);
		workLoad.setWorkLoadDets(workLoadInspActDets);
		workLoad.setTotalActDays(workLoadDto.getTotalActDays());
		return workLoad;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@Transactional
	public WorkLoad submitPlan(WorkLoadDto workLoadDto) {
		WorkLoad workLoad = savePlan(workLoadDto);
		workLoad.setPlanSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(workLoad);
		if (isvalid) {
			workLoad = workLoadRepository.save(workLoad);
		} else {
			throw new DataNotValidException("workload submit plan :  " + workLoad.toString());
		}
		return workLoad;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@Transactional
	public WorkLoad submitAct(WorkLoadDto workLoadDto) {
		WorkLoad workLoad = saveAct(workLoadDto);
		workLoad.setSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(workLoad);
		if (isvalid) {
			workLoad = workLoadRepository.save(workLoad);
		} else {
			throw new DataNotValidException("workload submit actual:  " + workLoad.toString());
		}
		return workLoad;
	}

	/**
	 * 
	 * @param workloadActDetDtos
	 * @param workLoad
	 * @return
	 */

	private List<WorkloadInspActDet> preparePlanWorkSoDetBos(List<WorkloadInspActDetDto> workloadActDetDtos,
			WorkLoad workLoad) {
		List<WorkloadInspActDet> workloadInspActDets = new ArrayList<>();
		for (WorkloadInspActDetDto workloadActDetDto : workloadActDetDtos) {
			WorkloadInspActDet workloadInspActDet;
			if (workloadActDetDto.getId() == 0) {
				workloadInspActDet = mapper.map(workloadActDetDto, WorkloadInspActDet.class);
			} else {
				Optional<WorkloadInspActDet> workInspActDet = workloadInspActDetRepo
						.findById(workloadActDetDto.getId());
				if (workInspActDet.isPresent()) {
					workloadInspActDet = workInspActDet.get();
				} else {
					throw new DataNotFoundException("Workload not found by Id: " + workInspActDet);
				}
			}

			Optional<InspPhaseActivities> inspPhaseBo = inspPhaseActRepo
					.findById(workloadActDetDto.getInspPhaseActId());
			if (inspPhaseBo.isPresent()) {
				workloadInspActDet.setInspPhaseActivities(inspPhaseBo.get());
			} else {
				throw new DataNotFoundException(" Workload Plan save: InspPhaseActivities not found by Id: "
						+ workloadActDetDto.getInspPhaseActId());
			}
			workloadInspActDet.setEstManDays(workloadActDetDto.getEstManDays());

			try {
				if (StringUtils.isNoneBlank(workloadActDetDto.getPlanCompletion())) {
					Date planDate = inputDateFormat.parse(workloadActDetDto.getPlanCompletion());
					workloadInspActDet.setPlanCompletion(planDate);
				}
			} catch (ParseException e) {
				logger.error("Error occured while parsing {}", e.getMessage());
			}
			workloadInspActDet.setWorkLoad(workLoad);
			boolean isValid = entityValidator.isValidEntity(workloadInspActDet);
			if (isValid) {
				workloadInspActDets.add(workloadInspActDet);
			} else {
				throw new DataNotValidException("WorkLoadInspActDet Plan save: det" + workloadInspActDet.toString());
			}
		}
		return workloadInspActDets;
	}

	/**
	 * 
	 * @param workloadActDetDtos
	 * @param workLoad
	 * @return
	 */

	private List<WorkloadInspActDet> prepareActWorkSoDetBos(List<WorkloadInspActDetDto> workloadActDetDtos,
			WorkLoad workLoad) {
		List<WorkloadInspActDet> workloadInspActDets = new ArrayList<>();
		for (WorkloadInspActDetDto workloadActDetDto : workloadActDetDtos) {
			WorkloadInspActDet workloadInspActDet;

			Optional<WorkloadInspActDet> workInspActDet = workloadInspActDetRepo.findById(workloadActDetDto.getId());
			if (workInspActDet.isPresent()) {
				workloadInspActDet = workInspActDet.get();
			} else {
				throw new DataNotFoundException("Workload not found by Id: " + workInspActDet);
			}

			Optional<InspPhaseActivities> inspPhaseBo = inspPhaseActRepo
					.findById(workloadActDetDto.getInspPhaseActId());
			if (inspPhaseBo.isPresent()) {
				workloadInspActDet.setInspPhaseActivities(inspPhaseBo.get());
			} else {
				throw new DataNotFoundException(" Workload Act save: InspPhaseActivities not found by Id: "
						+ workloadActDetDto.getInspPhaseActId());
			}
			workloadInspActDet.setActualManDays(workloadActDetDto.getActualManDays());

			try {
				if (StringUtils.isNoneBlank(workloadActDetDto.getActualCompletion())) {
					Date actDate = inputDateFormat.parse(workloadActDetDto.getActualCompletion());
					workloadInspActDet.setActualCompletion(actDate);
				}
			} catch (ParseException e) {
				logger.error("Error occured while parsing {}", e.getMessage());
			}

			workloadInspActDet.setRemarks(workloadActDetDto.getRemarks());
			workloadInspActDet.setWorkLoad(workLoad);
			boolean isValid = entityValidator.isValidEntity(workloadInspActDet);
			if (isValid) {
				workloadInspActDets.add(workloadInspActDet);
			} else {
				throw new DataNotValidException("WorkLoadInspActDet Act save: det" + workloadInspActDet.toString());
			}
		}
		return workloadInspActDets;
	}
}
