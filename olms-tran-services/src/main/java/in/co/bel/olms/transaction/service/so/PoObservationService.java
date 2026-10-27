package in.co.bel.olms.transaction.service.so;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.PoObservationDetDto;
import in.co.bel.olms.transaction.dto.PoObservationDto;
import in.co.bel.olms.transaction.entity.PoObservation;
import in.co.bel.olms.transaction.entity.PoObservationDet;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.poobs.PoObservationHelper;
import in.co.bel.olms.transaction.repository.PoObservationDetRepository;
import in.co.bel.olms.transaction.repository.PoObservationRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 *
 */
@Service
@RequiredArgsConstructor
public class PoObservationService {

	private final PoObservationRepository poObservationRepository;

	private final Mapper mapper;

	private final SupplyOrderService supplyOrderService;

	private final EntityValidator entityValidator;

	private final PoObservationHelper poObservationHelper;

	private final PoObservationDetRepository poObservationDetRepo;

	private final EmployeeRepository employeeRepository;

	private static final Logger logger =LoggerFactory.getLogger(PoObservationService.class);

	private final NotifyUtil notifyUtil;
	
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
	
	SimpleDateFormat outputDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:MM:SS"); 
	/**
	 * @since 13-02-2023
	 * @param poObservation
	 * @return
	 */
	public PoObservationDto convertBoToDto(PoObservation poObservation ) {
		
		PoObservationDto poObservationDto = mapper.map(poObservation, PoObservationDto.class);
		SupplyOrder so = poObservation.getSupplyOrder();
		poObservationDto.setSupplyOrderId(so.getId());
		poObservationDto.setSoOrderNo(so.getOrderNo());
		poObservationDto.setSoDate(so.getOrderDate().toString());
		poObservationDto.setCreatedDatetime(poObservation.getCreatedDatetime().toString());
		if(poObservation.getSecOfficer()!=null)
		{
			poObservationDto.setSecOfficer(poObservation.getSecOfficer().getEname());
		}
		
		if(poObservation.getSubmittedDate()!=null)
		{
			poObservationDto.setSubmittedDate(poObservation.getSubmittedDate().toString());
		}
		else
		{
			poObservationDto.setSubmittedDate(null);
		}		
		if(poObservation.getOpaSubmittedDate()!=null)
		{
			poObservationDto.setOpaSubmittedDate(poObservation.getOpaSubmittedDate().toString());
		}
		
		else
		{
			poObservationDto.setOpaSubmittedDate("");
		}
		if(poObservation.getClosureDate()!=null )
		{
			poObservationDto.setClosureDate(poObservation.getClosureDate().toString());
		}
		else
		{
			poObservationDto.setClosureDate("");
		}	
		
		List<PoObservationDetDto> obsList = new ArrayList<>();
		for(PoObservationDet detBo: poObservation.getPoObservationDets() )
		{
			PoObservationDetDto detDto= mapper.map(detBo, PoObservationDetDto.class);
			detDto.setIsClosed(detBo.getIsClosed());
			if(detBo.getScrutinyTenderDet() != null) {
				detDto.setActionReference(detBo.getScrutinyTenderDet().getActionAndRef());				
			}
			obsList.add(detDto);			
		}
		
		poObservationDto.setObsList(obsList);		
		
		if( null != poObservation.getStatus())
		{
			poObservationDto.setStatusId(poObservation.getStatus());
		}
		else
		{
			poObservationDto.setStatusId(0);
		}
		
		return poObservationDto;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public PoObservation getPoObservationById(int id) {
		Optional<PoObservation> poObservationOptional = poObservationRepository.findById(id);
		if (poObservationOptional.isPresent()) {
			return poObservationOptional.get();
		} else {
			throw new DataNotFoundException("PoObservation not found by Id: " + id);
		}
	}
	
	/**
	 * 
	 * @param poObservationDto
	 * @return
	 */
	@Transactional
	public PoObservation createPoObservation(PoObservationDto poObservationDto) {

		PoObservation poObservation;

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(poObservationDto.getSupplyOrderId());
		if (poObservationDto.getId() != 0) {
			Optional<PoObservation> obsOptional = poObservationRepository.findById(poObservationDto.getId());
			if (obsOptional.isPresent()) {
				poObservation = obsOptional.get();
			} else {
				throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
			}
		} else {
			poObservation = mapper.map(poObservationDto, PoObservation.class);
			poObservation.setCreatedDatetime(new Date());
		}
		poObservation = preparePoObsObj(poObservationDto, poObservation);

		poObservation.setSupplyOrder(supplyOrder);

		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}

		// Add code for iterate for child class and save
		List<PoObservationDet> poObservationDets = poObservationHelper.prepareCreateDetBos(poObservationDto.getObsList(), poObservation);
		poObservationDets = poObservationDetRepo.saveAll(poObservationDets);
		poObservation.setPoObservationDets(poObservationDets);
		// Then save the base class
		poObservation.setStatus(MasterConstants.PoObsStatus.ID_CREATED);
		
		isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}
		return poObservation;
	}

	
	/**
	 * 
	 * @param poObservationDto
	 * @return
	 */
	@Transactional
	public PoObservation saveRemActPoObservation(PoObservationDto poObservationDto) {

		PoObservation poObservation;

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(poObservationDto.getSupplyOrderId());
		if (poObservationDto.getId() != 0) {
			Optional<PoObservation> obsOptional = poObservationRepository.findById(poObservationDto.getId());
			if (obsOptional.isPresent()) {
				poObservation = obsOptional.get();
			} else {
				throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
			}
		} else {
			poObservation = mapper.map(poObservationDto, PoObservation.class);
			poObservation.setCreatedDatetime(new Date());
		}
		poObservation = preparePoObsObj(poObservationDto, poObservation);

		poObservation.setSupplyOrder(supplyOrder);

		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}

		// Add code for iterate for child class and save
		List<PoObservationDet> poObservationDets = poObservationHelper.prepareRemedialDetBos(poObservationDto.getObsList(), poObservation);
		poObservationDets = poObservationDetRepo.saveAll(poObservationDets);
		poObservation.setPoObservationDets(poObservationDets);
		// Then save the base class
		isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}
		return poObservation;
	}
	
	@Transactional
	public PoObservation submitRemActPoObservation(PoObservationDto poObservationDto) {		
		
		PoObservation poObservation = saveRemActPoObservation(poObservationDto);
		poObservation.setOpaSubmittedDate(new Date());
		poObservation.setStatus(MasterConstants.PoObsStatus.ID_OPA_SUBMITTED);
		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation submit remedial action: " + poObservation.toString());
		}


		try {
			SupplyOrder supplyOrder = poObservation.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdBysoId(supplyOrder.getId());
			notifyUtil.NotifySBUnCqae("SO/PO remedial action submitted for the order no " + poObservation.getSupplyOrder().getOrderNo() ,
					supplyOrder.getId(), supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(),
					 empId);
		} catch (Exception e) {
			logger.error("Notification error in SO/PO remedial action submitted for the order no {} ", e.getMessage());
			logger.error("Error occured while SO/PO remedial action submitted for the order no ", e);
		}
		
		return	poObservation;
		
	}
	
	/**
	 * 
	 * @param poObservationDto
	 * @return
	 */
	@Transactional
	public PoObservation saveRemarkPoObservation(PoObservationDto poObservationDto) {

		PoObservation poObservation;

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(poObservationDto.getSupplyOrderId());
		if (poObservationDto.getId() != 0) {
			Optional<PoObservation> obsOptional = poObservationRepository.findById(poObservationDto.getId());
			if (obsOptional.isPresent()) {
				poObservation = obsOptional.get();
			} else {
				throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
			}
		} else {
			throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
		}
		poObservation = preparePoObsObj(poObservationDto, poObservation);

		poObservation.setSupplyOrder(supplyOrder);

		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}

		// Add code for iterate for child class and save
		List<PoObservationDet> poObservationDets = poObservationHelper.prepareClosureDetBos(poObservationDto.getObsList(), poObservation);
		poObservationDets = poObservationDetRepo.saveAll(poObservationDets);
		poObservation.setPoObservationDets(poObservationDets);
		// Then save the base class
		isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}
		
		
		return poObservation;
	}
	
	@Transactional
	public PoObservation submitRemarkPoObs(PoObservationDto poObservationDto) {

		PoObservation poObservation;

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(poObservationDto.getSupplyOrderId());
		if (poObservationDto.getId() != 0) {
			Optional<PoObservation> obsOptional = poObservationRepository.findById(poObservationDto.getId());
			if (obsOptional.isPresent()) {
				poObservation = obsOptional.get();
			} else {
				throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
			}
		} else {
			throw new DataNotFoundException("Save : PoObservation not found By Id: " + poObservationDto.getId());
		}
		poObservation = preparePoObsObj(poObservationDto, poObservation);

		poObservation.setSupplyOrder(supplyOrder);
	
		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}

		// Add code for iterate for child class and save
		List<PoObservationDet> poObservationDets = poObservationHelper.prepareClosureSubmitDetBos(poObservationDto.getObsList(), poObservation);
		
		poObservationDets = poObservationDetRepo.saveAll(poObservationDets);
		poObservation.setPoObservationDets(poObservationDets);
		// Then save the base class
		isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation save: " + poObservation.toString());
		}
		List<PoObservationDet> detbosList =  new ArrayList<>();
		
		for(PoObservationDet detBo: poObservationDets)
		{			
			if(detBo.getIsClosed().equals(Boolean.FALSE) )
			{
				PoObservationDet newDetBo = new PoObservationDet();
				newDetBo.setObservation(detBo.getRemarks());
				newDetBo.setScrutinySoDet(detBo.getScrutinySoDet());
				detbosList.add(newDetBo);				
			}
		}
		if(!detbosList.isEmpty() ) {
			PoObservation po = new PoObservation();
			po.setSupplyOrder(supplyOrder);
			po.setCreatedDatetime(new Date());
			po.setStatus(MasterConstants.PoObsStatus.ID_CREATED);
			if (isValid) {
				po = poObservationRepository.save(po);
			} else {
				throw new DataNotValidException("PoObservation save: " + po.toString());
			}
			for(PoObservationDet detBo: detbosList)
			{	
				detBo.setPoObservation(po);
			}
			poObservationDetRepo.saveAll(detbosList);
		}
		else
		{
			//Todo : to be updated after new service class creation
			//SupplyOrder so= poObservation.getSupplyOrder();
			
			
			//so.setSoStatus(SupplyOrderConstants.Status.ID_SCRUTINY_APPROVED);
			//supplyOrderRepository.save(so);
		}
		
		

		
		
		
		return poObservation;
	}
	
	
	
	@Transactional
	public PoObservation submitRemarkPoObservation(PoObservationDto poObservationDto) {
		PoObservation poObservation = submitRemarkPoObs(poObservationDto);
		poObservation.setClosureDate(new Date());
		poObservation.setStatus(MasterConstants.PoObsStatus.ID_CLOSED);
		
		//poObservation.set
		boolean isValid = entityValidator.isValidEntity(poObservation);
		if (isValid) {
			poObservation = poObservationRepository.save(poObservation);
		} else {
			throw new DataNotValidException("PoObservation submit remark: " + poObservation.toString());
		}

		try {
			SupplyOrder supplyOrder = poObservation.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdByolId(supplyOrder.getId());
			notifyUtil.NotifySBUnCqae("SO/PO remarks submitted for the order no " + poObservation.getSupplyOrder().getOrderNo(),
					supplyOrder.getId(), supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(),
					 empId);
		} catch (Exception e) {
			logger.error("Notification error in SO/PO remarks submitted for the order no {} ", e.getMessage());
			logger.error("Error occured while SO/PO remarks submitted for the order no  {", e);
		}
		
		return	poObservation;
	}
	
	private PoObservation preparePoObsObj(PoObservationDto poObservationDto, PoObservation poObservation) {
		//poObservation.setSecOfficer(poObservationDto.getSecOfficerId());
		return poObservation;
	}

	
	
	
	

	/**
	 * 
	 * @param soId
	 * @return
	 */
	@Transactional
	public PoObservationDto getPoObservationBySoId(int soId) {
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		PoObservation poObservation = poObservationRepository.getPoObservationBySupplyOrder(supplyOrder);
		if (poObservation == null) {
			throw new DataNotFoundException("PoObservation not found by So Id: " + soId);
		}

		PoObservationDto poObservationDto =convertBoToDto(poObservation) ;		
		return poObservationDto;
	}

	

	/**
	 * Get the list of PO observation forms by the OPA org id
	 * @param orgId
	 * @return
	 */
	@Transactional
	public List<PoObservationDto> getPoObservationByOpaId(int orgId) {
		List<PoObservation> poObservationList = poObservationRepository.getPoObservationByOpaId(orgId) ;
		if (poObservationList.isEmpty()) {
			throw new DataNotFoundException("PoObservation not found by OPA Id: " + orgId);
		}
		List<PoObservationDto>  dtoList = new ArrayList<>();
		for(PoObservation poObservation :  poObservationList ) {
			PoObservationDto poObservationDto = convertBoToDtoForTable(poObservation);
			dtoList.add(poObservationDto);
		}
				
		return dtoList;
	}

	
	/**
	 * Get the list of PO observation forms by the Sales grp id
	 * @param orgId
	 * @return
	 */
	@Transactional
	public List<PoObservationDto> getPoObservationBySalesGrpId(int orgId) {
		List<PoObservation> poObservationList = poObservationRepository.getPoObservationBySalesGrpId(orgId) ;
		if (poObservationList.isEmpty()) {
			throw new DataNotFoundException("PoObservation not found by Sales group Id: " + orgId);
		}
		List<PoObservationDto>  dtoList = new ArrayList<>();
		for(PoObservation poObservation :  poObservationList ) {
			PoObservationDto dto = convertBoToDtoForTable(poObservation);
			dtoList.add(dto);
		}
				
		return dtoList;
	}

	
//	/**
//	 * Get the list of PO observation forms by the Inspector id
//	 * @param soId
//	 * @return
//	 */
//	@Transactional
//	public List<PoObservationDto> getPoObservationByInspectorId(int inspectorId) {
//		List<PoObservation> poObservationList = poObservationRepository.getByInsp(inspectorId) ;
//		if (poObservationList.isEmpty()) {
//			throw new DataNotFoundException("PoObservation not found by Inspector Id: " + inspectorId);
//		}
//		List<PoObservationDto>  dtoList = new ArrayList<>();
//		for(PoObservation poObservation :  poObservationList ) {
//			PoObservationDto poObservationDto = new PoObservationDto();						
//			SupplyOrder so = poObservation.getSupplyOrder();
//			poObservationDto.setSupplyOrderId(so.getId());
//			poObservationDto.setSoOrderNo(so.getOrderNo());
//			poObservationDto.setSoDate(so.getOrderDate().toString());
//			poObservationDto.setCreatedDatetime(poObservation.getCreatedDatetime().toString());
//			if(poObservation.getSubmittedDate()!=null)
//			{
//				poObservationDto.setSubmittedDate(poObservation.getSubmittedDate().toString());
//			}
//			else
//			{
//				poObservationDto.setSubmittedDate("");
//			}
//			
//			if(poObservation.getClosureDate()!=null )
//			{
//				poObservationDto.setClosureDate(poObservation.getClosureDate().toString());
//			}
//			else
//			{
//				poObservationDto.setClosureDate("");
//			}
//			dtoList.add(poObservationDto);
//		}			
//		return dtoList;
//	}

	/**
	 * Get the list of PO observation forms by the CQAE org id
	 * @param orgId
	 * @return
	 */
	@Transactional
	public List<PoObservationDto> getByCqae(int orgId) {
		List<PoObservation> poObservationList = poObservationRepository.getPoObservationByCqaeId(orgId) ;
		if (poObservationList.isEmpty()) {
			throw new DataNotFoundException("PoObservation not found by CQAE org Id: " + orgId);
		}
		List<PoObservationDto>  dtoList = new ArrayList<>();
		for(PoObservation poObservation :  poObservationList ) {
			PoObservationDto poObservationDto = new PoObservationDto();						
			SupplyOrder so = poObservation.getSupplyOrder();
			poObservationDto.setSupplyOrderId(so.getId());
			poObservationDto.setSoOrderNo(so.getOrderNo());
			poObservationDto.setSoDate(so.getOrderDate().toString());
			try {
				poObservationDto.setCreatedDatetime(poObservation.getCreatedDatetime().toString());
			} catch (Exception e) {
				logger.error("Error occured in setting created dateTime {}", e.getMessage());
			}
			if(poObservation.getSubmittedDate()!=null)
			{
				poObservationDto.setSubmittedDate(poObservation.getSubmittedDate().toString());
			}
			else
			{
				poObservationDto.setSubmittedDate("");
			}
			
			if(poObservation.getClosureDate()!=null )
			{
				poObservationDto.setClosureDate(poObservation.getClosureDate().toString());
			}
			else
			{
				poObservationDto.setClosureDate("");
			}
			dtoList.add(poObservationDto);
		}			
		return dtoList;
	}

	
	public List<PoObservationDto> getByEmpnSub(int empId) {
		List<PoObservationDto> dtos = new ArrayList<>();
		List<PoObservation> bos= poObservationRepository.getByEmpnSub(empId);
		for (PoObservation bo : bos) {
			PoObservationDto dto = convertBoToDtoForTable(bo);
			dtos.add(dto);
		}
		return dtos;
	}
	
//	/**
//	 * @author Manasa B
//	 * @param roleId
//	 * @param empId
//	 * @return
//	 */
//	public List<PoObservationDto> getByRolenEmp(int roleId, int empId) {
//		  
//		List<PoObservationDto> dtos = new ArrayList<>();
//		List<PoObservation> bos;
//		switch (roleId) {
//		case ApplicationConstants.Role.INSPECTOR:
//			bos = poObservationRepository.getByInsp(empId);
//		break;
//		case ApplicationConstants.Role.ACQAO:
//			bos = poObservationRepository.getByAcqao(empId);
//			break;
//		case ApplicationConstants.Role.DCQAO:
//			bos = poObservationRepository.getByDcqao(empId);
//			break;
// 
//		case ApplicationConstants.Role.JCQAO:
//			bos = poObservationRepository.getByJcqao(empId);
//			break;
//		default:
//			bos= new ArrayList<>();
//			break;
//		}
//		
//		for (PoObservation bo : bos) {
//			PoObservationDto dto = convertBoToDtoForTable(bo);
//			dtos.add(dto);
//		}
//		return dtos;
//	}
	
	public PoObservationDto convertBoToDtoForTable(PoObservation poObservation) {
		PoObservationDto poObservationDto = mapper.map(poObservation, PoObservationDto.class);
		SupplyOrder so = poObservation.getSupplyOrder();
		poObservationDto.setSupplyOrderId(so.getId());
		poObservationDto.setSoOrderNo(so.getOrderNo());
		poObservationDto.setSoDate( outputDateFormat.format( so.getOrderDate() ) );

		
		if(null!=so.getSalesGroup())
		{
			poObservationDto.setSalesGroupName(so.getSalesGroup().getName());
		}
		else
		{
			poObservationDto.setSalesGroupName("");
		}

		if (null != so.getProjects())
		{
			List<Project> projects = so.getProjects();
			// Changed after multiple projects for one PO/SO
			String allProjectNames = "";
			if (!projects.isEmpty()) {

				allProjectNames = projects.stream()
						.map(Project::getProjectName)
						.collect(Collectors.joining(","));
			}
			poObservationDto.setProjectName(allProjectNames);
		}
		else
		{
			poObservationDto.setProjectName("");
		}
		poObservationDto.setCreatedDatetime(  outputDateTimeFormat.format(poObservation.getCreatedDatetime()));
		if(poObservation.getSecOfficer()!=null)
		{
			poObservationDto.setSecOfficer(poObservation.getSecOfficer().getEname());
		}
		
		if(poObservation.getSubmittedDate()!=null)
		{
			poObservationDto.setSubmittedDate(outputDateFormat.format( poObservation.getSubmittedDate()));
		}
		else
		{
			poObservationDto.setSubmittedDate("");
		}		
		if(poObservation.getOpaSubmittedDate()!=null)
		{
			poObservationDto.setOpaSubmittedDate(outputDateTimeFormat.format(poObservation.getOpaSubmittedDate()));
		}
		
		else
		{
			poObservationDto.setOpaSubmittedDate("");
		}
		if(poObservation.getClosureDate()!=null )
		{
			poObservationDto.setClosureDate(outputDateTimeFormat.format(poObservation.getClosureDate()));
		}
		else
		{
			poObservationDto.setClosureDate("");
		}
		if( null != poObservation.getStatus())
		{
			poObservationDto.setStatusId(poObservation.getStatus());
			if (poObservation.getStatus() == 1)
			{
				poObservationDto.setStatus("Created");				
			}
			else if (poObservation.getStatus() == 2) {
				poObservationDto.setStatus("Submitted");				
			}
			else {
				poObservationDto.setStatus("Closed");				
			}
		}
		else
		{
			poObservationDto.setStatusId(0);
			poObservationDto.setStatus("NA");
		}
		return poObservationDto;
	}
}
