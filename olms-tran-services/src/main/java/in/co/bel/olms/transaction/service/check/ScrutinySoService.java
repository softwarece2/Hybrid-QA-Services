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

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.entity.checklist.ChecklistScrutinySO;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.checklist.ChecklistScrutinySORepository;
import in.co.bel.olms.transaction.dto.PoObservationDetDto;
import in.co.bel.olms.transaction.dto.PoObservationDto;
import in.co.bel.olms.transaction.dto.check.ScrutinySoDetDto;
import in.co.bel.olms.transaction.dto.check.ScrutinySoDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.ScrutinySo;
import in.co.bel.olms.transaction.entity.check.ScrutinySoDet;
import in.co.bel.olms.transaction.repository.check.ScrutinySoDetRepository;
import in.co.bel.olms.transaction.repository.check.ScrutinySoRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.so.PoObservationService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 06-03-2023
 *
 */

@Service
@RequiredArgsConstructor
public class ScrutinySoService {

	private final ChecklistScrutinySORepository checklistScrutinySORepository;

	private final ScrutinySoRepository scrutinySoRepository;

	private final ScrutinySoDetRepository scrutinySoDetRepository;

	private final SupplyOrderService supplyOrderService;

	private final PoObservationService poObservationService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;
	
	private final EmployeeRepository employeeRepository;

	private final NotifyUtil notifyUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinySoService.class);	

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	public ScrutinySo saveScrutinySo(ScrutinySoDto scrutinySoDto) {

		ScrutinySo scrutinySo;
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(scrutinySoDto.getSupplyOrderId());
		if (scrutinySoDto.getId() != 0) {

			Optional<ScrutinySo> scrutinySoOpt = scrutinySoRepository.findById(scrutinySoDto.getId());
			if (scrutinySoOpt.isPresent()) {
				scrutinySo = scrutinySoOpt.get();
			} else {
				throw new DataNotFoundException("Save : Scrutiny So not found by  Id: " + scrutinySoDto.getId());
			}

		} else {
			scrutinySo = mapper.map(scrutinySoDto, ScrutinySo.class);
			scrutinySo.setCreatedDatetime(new Date());
		}
		scrutinySo.setSupplyOrder(supplyOrder);
		boolean isValid = entityValidator.isValidEntity(scrutinySo);
		if (isValid) {
			scrutinySo = scrutinySoRepository.save(scrutinySo);
		} else {
			throw new DataNotValidException("ScrutinySo save:" + scrutinySo.toString());
		}

		scrutinySo = prepareScrutinySoObj(scrutinySoDto, scrutinySo);
		return scrutinySo;
	}

	/**
	 * submit the scrutiny of the Supply order and generate the PO observation form
	 * @param scrutinySoDto
	 * @return
	 */
	@Transactional
	public ScrutinySo submitScrutinySo(ScrutinySoDto scrutinySoDto) {
		ScrutinySo scrutinySo = saveScrutinySo(scrutinySoDto);
		scrutinySo.setCreatedDatetime(new Date());
		scrutinySo.setSubmittedDatetime(new Date());
		boolean isValid = entityValidator.isValidEntity(scrutinySo);
		if (isValid) {
			scrutinySo = scrutinySoRepository.save(scrutinySo);
		}
		else {
			throw new DataNotFoundException("ScrutinySo Submit:" + scrutinySo.toString());
		}

		List<ScrutinySoDet> soDets = scrutinySo.getScrutinySoDets();

		List<PoObservationDetDto> obsList = new ArrayList<>();

		for (ScrutinySoDet detBo : soDets) {
			boolean isObs = isObservation(detBo.getObsSts());
			if (isObs) {
				PoObservationDetDto detDto = new PoObservationDetDto();
				detDto.setObservation(detBo.getRemarks());
				detDto.setSoScrutinyDetId(detBo.getId());
				obsList.add(detDto);
			}
		}

		if(!obsList.isEmpty())
		{
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(0);
			poObservationDto.setSupplyOrderId(scrutinySo.getSupplyOrder().getId());
			poObservationDto.setObsList(obsList);
			poObservationService.createPoObservation(poObservationDto);
		}

		try {
			SupplyOrder supplyOrder = scrutinySo.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdBysoId(supplyOrder.getId());
			notifyUtil.NotifySBUnCqae("Scrutiny SO/PO is completed for the SO/PO no " + supplyOrder.getOrderNo(),
					supplyOrder.getId(), supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(),
					 empId);
		} catch (Exception e) {
			logger.error("Notification error in Scrutiny SO/PO submitted for the order no {} ", e.getMessage());
			logger.error("Error in Scrutiny SO/PO submitted for the order no ", e);
		}
		return scrutinySo;
	}

	public boolean isObservation(String obsSts) {
		switch (obsSts) {
			case "no":
			case "nonsat" : 
				return true;
	
			default:
				return false;
		}

	}

	/**
	 * 
	 * @param scrutinySoDto
	 * @param scrutinySo
	 * @return
	 */
	private ScrutinySo prepareScrutinySoObj(ScrutinySoDto scrutinySoDto, ScrutinySo scrutinySo) {

		List<ScrutinySoDet> scrutinySoDets = new ArrayList<>();

		for (ScrutinySoDetDto detDto : scrutinySoDto.getScrutinySoDetDtos()) {
			ScrutinySoDet detBo;
			if (detDto.getId() == 0) {
				detBo = mapper.map(detDto, ScrutinySoDet.class);
			} else {
				Optional<ScrutinySoDet> detBoopt = scrutinySoDetRepository.findById(detDto.getId());
				if (detBoopt.isPresent()) {
					detBo = detBoopt.get();
				} else {
					detBo = mapper.map(detDto, ScrutinySoDet.class);
				}
			}

			Optional<ChecklistScrutinySO> checkbo = checklistScrutinySORepository.findById(detDto.getChecklistId());
			if (checkbo.isPresent()) {
				detBo.setChecklistScrutinySO(checkbo.get());
			} else {
				throw new DataNotFoundException(
						"ScrutinySo save: checklist not found by Id: " + detDto.getChecklistId());
			}
			detBo.setObsSts(detDto.getObsSts());
			detBo.setRemarks(detDto.getRemarks());
			detBo.setScrutinySo(scrutinySo);

			// Add code for the observation status

			boolean isvalid = entityValidator.isValidEntity(detBo);
			if (isvalid) {
				scrutinySoDets.add(detBo);
			} else {
				throw new DataNotValidException("ScrutinySo save: det " + detBo.toString());
			}

		}
		scrutinySoDets = scrutinySoDetRepository.saveAll(scrutinySoDets);
		scrutinySo.setScrutinySoDets(scrutinySoDets);
		return scrutinySo;
	}

	/**
	 * 
	 * @author Manasa B
	 * @since 24-02-2023
	 * @param soId
	 * @return
	 */
	public ScrutinySoDto getScrutinySoBySoId(int soId) {
		ScrutinySoDto scrutinySoDto = new ScrutinySoDto();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		ScrutinySo scrutinySo = scrutinySoRepository.getScrutinySoBySupplyOrder(supplyOrder);

		List<ScrutinySoDetDto> inspsoDetDtos = new ArrayList<>();
		if (scrutinySo == null) {
			scrutinySoDto.setId(0);
			scrutinySoDto.setSubmittedDatetime(null);
			List<ChecklistScrutinySO> chkSos = checklistScrutinySORepository.findAll();
			for (ChecklistScrutinySO bo : chkSos) {
				ScrutinySoDetDto detDto = new ScrutinySoDetDto();
				detDto.setId(0);
				detDto.setChecklistId(bo.getId());
				detDto.setSlNo(bo.getSlNo());
				detDto.setReference(bo.getReference());
				detDto.setSplit(bo.getSplit());
				detDto.setObsType(bo.getObsType());
				detDto.setObsSts("");
				detDto.setRemarks("");
				inspsoDetDtos.add(detDto);
			}

		} else {
			scrutinySoDto = mapper.map(scrutinySo, ScrutinySoDto.class);
			SupplyOrder so = scrutinySo.getSupplyOrder();
			scrutinySoDto.setSupplyOrderId(so.getId());
			if(null!=scrutinySo.getSubmittedDatetime())
			{
				scrutinySoDto.setSubmittedDatetime(dateFormat.format(scrutinySo.getSubmittedDatetime()));
			}
			
			
			for (ScrutinySoDet detBo : scrutinySo.getScrutinySoDets()) {
				ScrutinySoDetDto detDto = new ScrutinySoDetDto();
				ChecklistScrutinySO checklistScrutinySO = detBo.getChecklistScrutinySO();
				detDto.setId(detBo.getId());
				detDto.setChecklistId(checklistScrutinySO.getId());
				detDto.setSlNo(checklistScrutinySO.getSlNo());
				detDto.setReference(checklistScrutinySO.getReference());
				detDto.setSplit(checklistScrutinySO.getSplit());
				detDto.setObsType(checklistScrutinySO.getObsType());
				detDto.setObsSts(detBo.getObsSts());
				detDto.setRemarks(detBo.getRemarks());
				inspsoDetDtos.add(detDto);
			}

			// scrutinySoDto.setSoDate(so.getOrderDate().toString());
			// Add details of the so and order
		}

		Collections.sort(inspsoDetDtos, new Comparator<ScrutinySoDetDto>() {
			@Override
			public int compare(ScrutinySoDetDto o1, ScrutinySoDetDto o2) {

				return o1.getChecklistId().compareTo(o2.getChecklistId());
			}

		});

		scrutinySoDto.setScrutinySoDetDtos(inspsoDetDtos);
		return scrutinySoDto;
	}

	/**
	 * 
	 * @param scrutinySoDets
	 * @return
	 */
	public List<ScrutinySoDetDto> prepareSoDets(List<ScrutinySoDet> scrutinySoDets) {
		List<ScrutinySoDetDto> scrutinySoDetDtos = new ArrayList<>();
		for (ScrutinySoDet scrutinySoDet : scrutinySoDets) {
			ScrutinySoDetDto scrutinyDetDto = new ScrutinySoDetDto();
			scrutinyDetDto.setId(scrutinySoDet.getId());
			ChecklistScrutinySO checkSo = scrutinySoDet.getChecklistScrutinySO();
			scrutinyDetDto.setChecklistId(checkSo.getId());
			scrutinyDetDto.setSlNo(checkSo.getSlNo());
			scrutinyDetDto.setReference(checkSo.getReference());
			if (StringUtils.isNotBlank(scrutinySoDet.getRemarks())) {
				scrutinyDetDto.setRemarks(scrutinySoDet.getRemarks());
			} else {
				scrutinyDetDto.setRemarks("");
			}
			scrutinyDetDto.setObsType(checkSo.getObsType());
			if (StringUtils.isNotBlank(scrutinySoDet.getObsSts())) 
			{
				scrutinyDetDto.setObsSts(scrutinySoDet.getObsSts());
			}		
			else {
				scrutinyDetDto.setObsSts("");
			}
			
			scrutinyDetDto.setSplit(checkSo.getSplit());
			scrutinySoDetDtos.add(scrutinyDetDto);
		}
		Collections.sort(scrutinySoDetDtos, new Comparator<ScrutinySoDetDto>() {
			public int compare(ScrutinySoDetDto o1, ScrutinySoDetDto o2) {
				return o1.getChecklistId().compareTo(o2.getChecklistId());
			}
		});
		return scrutinySoDetDtos;
	}

}
