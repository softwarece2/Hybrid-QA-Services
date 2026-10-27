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

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.olms.master.entity.InspPhaseActivities;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.InspPhaseActivitiesRepository;
import in.co.bel.olms.transaction.dto.WorkLoadOlDto;
import in.co.bel.olms.transaction.dto.WorkloadInspActDetDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.WorkLoadOl;
import in.co.bel.olms.transaction.entity.WorkloadOlDet;
import in.co.bel.olms.transaction.repository.WorkLoadOlRepository;
import in.co.bel.olms.transaction.repository.WorkloadOlDetRepository;
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
public class WorkLoadServiceOl {

	private final WorkLoadOlRepository workLoadOlRepository;

	private final Mapper mapper;

	private final OfferLetterService offerLetterService;

	private final EntityValidator entityValidator;

	private final WorkloadOlDetRepository workloadOlDetRepo;

	private final InspPhaseActivitiesRepository inspPhaseActRepo;

	Logger logger = LoggerFactory.getLogger(WorkLoadServiceOl.class);
	DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * Covert the entity to Dto
	 * 
	 * @param workLoadOl
	 * @return
	 */
	public WorkLoadOlDto convertBoToDto(WorkLoadOl workLoadOl) {
		String regex = "[0-9]+"; // regex to check if string contains only digits
		Pattern pattern = Pattern.compile(regex); // compiles the regex
		
		WorkLoadOlDto workLoadDto = mapper.map(workLoadOl, WorkLoadOlDto.class);
		OfferLetter offerLetter = workLoadOl.getOfferLetter();
		SupplyOrder supplyOrder = offerLetter.getSaleOrder().getSupplyOrder();

		workLoadDto.setSupplyOrderId(supplyOrder.getId());
		if(offerLetter.getOfferDate()!=null)
		workLoadDto.setOfferDate(outputDateFormat.format(offerLetter.getOfferDate()));

		workLoadDto.setSoOrderNo(supplyOrder.getOrderNo());
		workLoadDto.setValue(supplyOrder.getValue());
		workLoadDto.setTotalQty(supplyOrder.getTotalQty());
		workLoadDto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
		if(StringUtils.isNoneBlank(supplyOrder.getFileNo()))
		{
			workLoadDto.setFileNo(supplyOrder.getFileNo());
		}
		if (null != supplyOrder.getDd()) {
			workLoadDto.setOriginalDd(outputDateFormat.format(supplyOrder.getDd()));
		} else {
			workLoadDto.setOriginalDd("");
		}
		if (null != supplyOrder.getSupplier()) {
			workLoadDto.setSupplierName(supplyOrder.getSupplier().getName());
		} else {
			workLoadDto.setSupplierName("");
		}

		if (workLoadOl.getPlanSubmittedDate() != null) {
			workLoadDto.setPlanSubmittedDate(outputDateFormat.format(workLoadOl.getPlanSubmittedDate()));
		} else {
			workLoadDto.setPlanSubmittedDate("");
		}
		if (workLoadOl.getSubmittedDate() != null) {
			workLoadDto.setSubmittedDate(outputDateFormat.format(workLoadOl.getSubmittedDate()));
		} else {
			workLoadDto.setSubmittedDate("");
		}
		
		workLoadDto.setTotalEstDays(workLoadOl.getTotalEstDays());
		workLoadDto.setTotalActDays(workLoadOl.getTotalActDays());

		List<WorkloadInspActDetDto> inprogacitivitiesDtos = new ArrayList<>();
		List<WorkloadInspActDetDto> postacitivitiesDtos = new ArrayList<>();

		workLoadDto.setTotalEstDays(workLoadOl.getTotalEstDays());
		workLoadDto.setTotalActDays(workLoadOl.getTotalActDays());
		if (workLoadOl.getWorkLoadDets() != null) {
			for (WorkloadOlDet detBo : workLoadOl.getWorkLoadDets()) {
				WorkloadInspActDetDto detodto = mapper.map(detBo, WorkloadInspActDetDto.class);
				InspPhaseActivities act = detBo.getInspPhaseActivities();
				detodto.setInspPhaseActId(act.getId());
				detodto.setInspPhaseActivity(act.getActivity());
				detodto.setSlNo(act.getSlNo());
				Matcher matcherText = pattern.matcher(detodto.getSlNo());
				detodto.setBold(matcherText.matches());
				if (detBo.getInspPhaseActivities().getInspPhase().getId() == MasterConstants.IN_PROCESS_INSP_PHASE) {
					inprogacitivitiesDtos.add(detodto);
				} else if (detBo.getInspPhaseActivities().getInspPhase().getId() == MasterConstants.POST_INSP_PHASE) {
					postacitivitiesDtos.add(detodto);
				}
			}
		}

		workLoadDto.setWrkLdInprogDetDtos(inprogacitivitiesDtos);
		;
		workLoadDto.setWrkLdPostDetDtos(postacitivitiesDtos);

		return workLoadDto;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public WorkLoadOl getWorkLoadById(int id) {

		Optional<WorkLoadOl> workLoadOptional = workLoadOlRepository.findById(id);
		if (workLoadOptional.isPresent()) {
			return workLoadOptional.get();
		} else {
			throw new DataNotFoundException("WorkLoad not found with Id: " + id);
		}
	}

	/**
	 * 
	 * @param so
	 * @param workLoadDto
	 * @return
	 */
	private WorkLoadOlDto updateSodetails(SupplyOrder so, WorkLoadOlDto workLoadDto) {
		workLoadDto.setSupplyOrderId(so.getId());
		workLoadDto.setSoOrderNo(so.getOrderNo());
		workLoadDto.setValue(so.getValue());
		workLoadDto.setTotalQty(so.getTotalQty());
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
		return workLoadDto;
	}

	/**
	 * 
	 * @param olId
	 * @return
	 */
	public WorkLoadOlDto getWorkLoadByOlId(int olId) {
		String regex = "[0-9]+"; // regex to check if string contains only digits
		Pattern pattern = Pattern.compile(regex); // compiles the regex

		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);

		WorkLoadOl workLoadOl = workLoadOlRepository.getWorkLoadByOfferLetter(offerLetter);
		WorkLoadOlDto workLoadDto = null;
		if (workLoadOl == null) {
			workLoadDto = new WorkLoadOlDto();
			workLoadDto = updateSodetails(offerLetter.getSaleOrder().getSupplyOrder(), workLoadDto);
			workLoadDto.setOfferLetterId(offerLetter.getId());
			if (null != offerLetter.getOfferDate()) {
				workLoadDto.setOfferDate(outputDateFormat.format(offerLetter.getOfferDate()));
			}
			List<InspPhaseActivities> activities = inspPhaseActRepo
					.getAllInspPhaseActivitiesByInspPhaseId(MasterConstants.IN_PROCESS_INSP_PHASE);
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
			workLoadDto.setWrkLdInprogDetDtos(acitivitiesDtos);

			List<InspPhaseActivities> postactivities = inspPhaseActRepo
					.getAllInspPhaseActivitiesByInspPhaseId(MasterConstants.POST_INSP_PHASE);
			List<WorkloadInspActDetDto> postacitivitiesDtos = new ArrayList<>();
			for (InspPhaseActivities act : postactivities) {
				WorkloadInspActDetDto actdto = new WorkloadInspActDetDto();
				actdto.setInspPhaseActId(act.getId());
				actdto.setInspPhaseActivity(act.getActivity());
				actdto.setSlNo(act.getSlNo());
				postacitivitiesDtos.add(actdto);
			}
			workLoadDto.setWrkLdPostDetDtos(postacitivitiesDtos);

		} else {
			workLoadDto = convertBoToDto(workLoadOl);
		}

		return workLoadDto;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	public WorkLoadOl savePlan(WorkLoadOlDto workLoadDto) {
		WorkLoadOl workLoadOl;

		if (workLoadDto.getId() != 0) {
			Optional<WorkLoadOl> workLoadOptional = workLoadOlRepository.findById(workLoadDto.getId());
			if (workLoadOptional.isPresent()) {
				workLoadOl = workLoadOptional.get();
				workLoadOl.setTotalEstDays(workLoadDto.getTotalEstDays());
			} else {

				throw new DataNotFoundException("Save: WorkLoadOl Not Found By ID : " + workLoadDto.getId());
			}

		} else {
			workLoadOl = mapper.map(workLoadDto, WorkLoadOl.class);
			OfferLetter offerLetter = offerLetterService.getOfferLetterById(workLoadDto.getOfferLetterId());
			workLoadOl.setOfferLetter(offerLetter);
			workLoadOl.setSupplyOrder(offerLetter.getSaleOrder().getSupplyOrder());
			workLoadOl.setCreatedDatetime(new Date());
			workLoadOl.setCreatedBy(workLoadDto.getCreatedBy());
			workLoadOl.setTotalEstDays(workLoadDto.getTotalEstDays());
		}

		boolean isValid = entityValidator.isValidEntity(workLoadOl);

		if (isValid) {
			workLoadOl = workLoadOlRepository.save(workLoadOl);
		} else {
			throw new DataNotValidException("WorkLoadOl save plan: not a valid entity");
		}

		List<WorkloadOlDet> workLoadInspActDets = preparePlanWorkOlDetBos(workLoadDto.getWrkLdDetDtos(), workLoadOl);
		workLoadInspActDets = workloadOlDetRepo.saveAll(workLoadInspActDets);
		workLoadOl.setWorkLoadDets(workLoadInspActDets);
		return workLoadOl;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	public WorkLoadOl submitPlan(WorkLoadOlDto workLoadDto) {
		WorkLoadOl workLoadOl = savePlan(workLoadDto);
		workLoadOl.setPlanSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(workLoadOl);
		if (isvalid) {
			workLoadOl = workLoadOlRepository.save(workLoadOl);
		} else {
			throw new DataNotValidException("WorkLoadOl submit plan:  " + workLoadOl.toString());
		}
		return workLoadOl;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	public WorkLoadOl saveAct(WorkLoadOlDto workLoadDto) {
		WorkLoadOl workLoadOl;

		if (workLoadDto.getId() != 0) {
			Optional<WorkLoadOl> workLoadOptional = workLoadOlRepository.findById(workLoadDto.getId());
			if (workLoadOptional.isPresent()) {
				workLoadOl = workLoadOptional.get();
				workLoadOl.setTotalActDays(workLoadDto.getTotalActDays());
			} else {
				throw new DataNotFoundException("Save act: WorkLoadOl Not Found By ID : " + workLoadDto.getId());
			}
		} else {
			workLoadOl = mapper.map(workLoadDto, WorkLoadOl.class);
			OfferLetter offerLetter = offerLetterService.getOfferLetterById(workLoadDto.getOfferLetterId());
			workLoadOl.setOfferLetter(offerLetter);
			workLoadOl.setSupplyOrder(offerLetter.getSaleOrder().getSupplyOrder());
			workLoadOl.setCreatedDatetime(new Date());
			workLoadOl.setCreatedBy(workLoadDto.getCreatedBy());
			workLoadOl.setTotalActDays(workLoadDto.getTotalActDays());
		}

		boolean isValid = entityValidator.isValidEntity(workLoadOl);

		if (isValid) {
			workLoadOl = workLoadOlRepository.save(workLoadOl);
		} else {
			throw new DataNotValidException("WorkLoadOl save act: not a valid entity");
		}

		List<WorkloadOlDet> workLoadInspActDets = prepareActWorkOlDetBos(workLoadDto.getWrkLdDetDtos(), workLoadOl);
		workLoadInspActDets = workloadOlDetRepo.saveAll(workLoadInspActDets);
		workLoadOl.setWorkLoadDets(workLoadInspActDets);
		return workLoadOl;
	}

	/**
	 * 
	 * @param workLoadDto
	 * @return
	 */
	public WorkLoadOl submitAct(WorkLoadOlDto workLoadDto) {
		WorkLoadOl workLoad = saveAct(workLoadDto);
		workLoad.setSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(workLoad);
		if (isvalid) {
			workLoad = workLoadOlRepository.save(workLoad);
		} else {
			throw new DataNotValidException("WorkLoadOl submit plan:  " + workLoad.toString());
		}
		return workLoad;
	}

	/**
	 * 
	 * @return
	 */
	private List<WorkloadOlDet> preparePlanWorkOlDetBos(List<WorkloadInspActDetDto> workloadActDetDtos,
			WorkLoadOl workLoadOl) {
		List<WorkloadOlDet> workloadInspActDets = new ArrayList<>();
		for (WorkloadInspActDetDto workloadActDetDto : workloadActDetDtos) {
			WorkloadOlDet workloadInspActDet;
			if (workloadActDetDto.getId() == 0) {
				workloadInspActDet = mapper.map(workloadActDetDto, WorkloadOlDet.class);
			} else {
				Optional<WorkloadOlDet> workInspActDet = workloadOlDetRepo.findById(workloadActDetDto.getId());
				if (workInspActDet.isPresent()) {
					workloadInspActDet = workInspActDet.get();
				} else {
					throw new DataNotFoundException("WorkLoadOl not found by Id: " + workInspActDet);
				}
			}

			Optional<InspPhaseActivities> inspPhaseBo = inspPhaseActRepo.findById(workloadActDetDto.getInspPhaseActId());
			if (inspPhaseBo.isPresent()) {
				workloadInspActDet.setInspPhaseActivities(inspPhaseBo.get());

			} else {
				throw new DataNotFoundException(
						" Workload ol prep plan: InspPhaseact not found by Id: " + workloadActDetDto.getInspPhaseActId());
			}
			try {
				if (StringUtils.isNoneBlank(workloadActDetDto.getPlanCompletion())) {
					Date planDate = inputDateFormat.parse(workloadActDetDto.getPlanCompletion());
					workloadInspActDet.setPlanCompletion(planDate);
				}
			} catch (ParseException e) {
				logger.error("Error occured while parsing {}", e.getMessage());
			}

			workloadInspActDet.setEstManDays(workloadActDetDto.getEstManDays());
			workloadInspActDet.setWorkLoadOl(workLoadOl);

			boolean isValid = entityValidator.isValidEntity(workloadInspActDet);
			if (isValid) {
				workloadInspActDets.add(workloadInspActDet);
			} else {
				throw new DataNotValidException("WorkLoadInspActDet ol prep plan: det" + workloadInspActDet.toString());
			}
		}
		return workloadInspActDets;
	}

	/**
	 * 
	 * @return
	 */
	private List<WorkloadOlDet> prepareActWorkOlDetBos(List<WorkloadInspActDetDto> workloadActDetDtos,
			WorkLoadOl workLoadOl) {
		List<WorkloadOlDet> workloadInspActDets = new ArrayList<>();
		for (WorkloadInspActDetDto workloadActDetDto : workloadActDetDtos) {
			WorkloadOlDet workloadInspActDet;
			if (workloadActDetDto.getId() == 0) {
				workloadInspActDet = mapper.map(workloadActDetDto, WorkloadOlDet.class);
			} else {
				Optional<WorkloadOlDet> workInspActDet = workloadOlDetRepo.findById(workloadActDetDto.getId());
				if (workInspActDet.isPresent()) {
					workloadInspActDet = workInspActDet.get();
				} else {
					throw new DataNotFoundException("Workload ol det prep not found by Id: " + workInspActDet);
				}
			}

			Optional<InspPhaseActivities> inspPhaseBo = inspPhaseActRepo.findById(workloadActDetDto.getInspPhaseActId());
			if (inspPhaseBo.isPresent()) {
				workloadInspActDet.setInspPhaseActivities(inspPhaseBo.get());

			} else {
				throw new DataNotFoundException(
						" Workload ol prep det: InspPhaseact not found by Id: " + workloadActDetDto.getInspPhaseActId());
			}
			try {
				if (StringUtils.isNoneBlank(workloadActDetDto.getActualCompletion())) {
					Date planDate = inputDateFormat.parse(workloadActDetDto.getActualCompletion());
					workloadInspActDet.setActualCompletion(planDate);
				}
			} catch (ParseException e) {
				logger.error("Error occured while parsing {}", e.getMessage());
			}
			workloadInspActDet.setRemarks(workloadActDetDto.getRemarks());
			workloadInspActDet.setActualManDays(workloadActDetDto.getActualManDays());
			workloadInspActDet.setWorkLoadOl(workLoadOl);

			boolean isValid = entityValidator.isValidEntity(workLoadOl);
			if (isValid) {
				workloadInspActDets.add(workloadInspActDet);
			} else {
				throw new DataNotValidException("WorkLoad ol InspActDet prep det: det" + workloadInspActDet.toString());
			}
		}
		return workloadInspActDets;
	}

}
