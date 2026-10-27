package in.co.bel.olms.transaction.service.so;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import in.co.bel.olms.master.entity.SalesGroup;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.FclDto;
import in.co.bel.olms.transaction.dto.FclVigilanceDto;
import in.co.bel.olms.transaction.entity.Fcl;
import in.co.bel.olms.transaction.entity.FclVigilance;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.fcl.FclHelper;
import in.co.bel.olms.transaction.helper.so.SupplyOrderHelper;
import in.co.bel.olms.transaction.repository.FclRepository;
import in.co.bel.olms.transaction.repository.FclVigilanceRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.validator.EntityValidator;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 19-04-2023
 *
 */
@Service
@RequiredArgsConstructor
public class FclService {

	private final  SupplyOrderService supplyOrderService;

	private final  Mapper mapper;

	private final  FclRepository fclRepository;

	private final  FclVigilanceRepository fclVigilanceRepository;

	private final  EntityValidator entityValidator;

	private final  FclHelper fclHelper;

	private final  SupplyOrderHelper supplyOrderHelper;

	private final  EmployeeRepository employeeRepository;

	private final  EmployeeDao employeeDao;

	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private final  NotifyUtil notifyUtil;
	
	private static final Logger logger =LoggerFactory.getLogger(FclService.class);
	
	public FclDto convertBotoDto(Fcl fcl) {
		return convertEntitytoDto(fcl, fcl.getSupplyOrder());
	}

	/**
	 * 
	 * @param fcl
	 * @return
	 */

	public FclDto convertEntitytoDto(Fcl fcl, SupplyOrder supplyOrder) {

		FclDto fclDto;	
		
		if (fcl == null) {
			fclDto = new FclDto();
			fclDto.setId(0);
			fclDto=fclHelper.getSoDetails(fclDto, supplyOrder);
		} else {
			fclDto = mapper.map(fcl, FclDto.class);
			supplyOrder = fcl.getSupplyOrder();
			if(fcl.getSubmittedDate() !=null)
			{
				fclDto.setSubmittedDate(outputDateFormat.format(fcl.getSubmittedDate()));				
				fclDto.setSupplyOrderId(supplyOrder.getId());
				fclDto.setProjectName(fcl.getProjectName());
				fclDto.setOpa(fcl.getOpa());
				fclDto.setFileNo(supplyOrder.getFileNo());
				SalesGroup salesGroup = supplyOrder.getSalesGroup();
				if (salesGroup != null) {
					fclDto.setSalesGrpName(salesGroup.getName());
				}
				Employee employee=	employeeDao.getEmployee(fcl.getCreatedBy());
				fclDto.setSignedBy(employee.getEname());
			}
			else
			{
				fclDto.setSubmittedDate(null);
				fclDto=fclHelper.getSoDetails(fclDto, supplyOrder);
			}			
		}		
		SimpleDateFormat soDateFormat = new SimpleDateFormat("dd MMM yyyy");
		String soDate = soDateFormat.format(supplyOrder.getOrderDate());
		fclDto.setSoDate(soDate);
		fclDto.setSoOrderNo(supplyOrder.getOrderNo());
		
		// Add details of the so and order

		List<FclVigilanceDto> detDtos = new ArrayList<>();
		if (fcl != null) {
			for (FclVigilance detBo : fcl.getFclVigilances()) {
				FclVigilanceDto vigDto = mapper.map(detBo, FclVigilanceDto.class);
				detDtos.add(vigDto);
			}
		}
		fclDto.setFclVigDtoList(detDtos);
		return fclDto;
	}

	/**
	 * save service for fcl
	 * 
	 * @param fclDto
	 * @return
	 */
	@Transactional
	public Fcl saveFcl(FclDto fclDto) {
		Fcl fcl;
		if (fclDto.getId() != 0) {
			Optional<Fcl> fclOptional = fclRepository.findById(fclDto.getId());
			if (fclOptional.isPresent()) {
				fcl = fclOptional.get();
			} else {
				throw new DataNotFoundException("FCL save : FCL not found By Id: " + fclDto.getId());
			}
		} else {
			fcl = mapper.map(fclDto, Fcl.class);
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fclDto.getSupplyOrderId());
			fcl.setSupplyOrder(supplyOrder);
			fcl.setCreatedDate(new Date());
		}
		boolean isvalid = entityValidator.isValidEntity(fcl);
		if (isvalid) {
			fcl = fclRepository.save(fcl);
		} else {
			throw new DataNotValidException("FCL save: det " + fcl.toString());
		}
		
		deleteVigilanceDetBos(fclDto.getDeletedVigList());
		List<FclVigilance> fclVigilancelist = prepareVigilanceDetBos(fclDto.getFclVigDtoList(), fcl);
		
		fclVigilancelist=fclVigilanceRepository.saveAll(fclVigilancelist);
		
		
		fcl.setFclVigilances(fclVigilancelist);

		return fclRepository.save(fcl);
	}

	private void  deleteVigilanceDetBos(List<Integer> deletedVigList)
	{
		List<FclVigilance> poObservationDets = new ArrayList<>();		
		if(null !=deletedVigList &&  !deletedVigList.isEmpty() )
		{
			for(Integer vigId:  deletedVigList ) {
				FclVigilance detBo;
				Optional<FclVigilance> detBoopt = fclVigilanceRepository.findById(vigId);
				if (detBoopt.isPresent()) {
					 detBo = detBoopt.get();
					 poObservationDets.add(detBo);
				}
			}
		}
		if(!poObservationDets.isEmpty()) {
			fclVigilanceRepository.deleteAll(poObservationDets);
		}
		
	}
	
	/**
	 * 
	 * @param fclVigilanceDtos
	 * @param fcl
	 * @return
	 */
	public List<FclVigilance> prepareVigilanceDetBos(List<FclVigilanceDto> fclVigilanceDtos, Fcl fcl) {
		List<FclVigilance> poObservationDets = new ArrayList<>();
		for (FclVigilanceDto detDto : fclVigilanceDtos) {
			FclVigilance detBo;
			Optional<FclVigilance> detBoopt = fclVigilanceRepository.findById(detDto.getId());
			if (detBoopt.isPresent()) {
				detBo = detBoopt.get();
				if (!InputNameValidation.checkVigilanceRemarks(detDto.getVigilance())) {
					throw new DataNotValidException("Vigilance remarks not valid");
				}
				detBo.setVigilance(detDto.getVigilance());
				boolean isvalid = entityValidator.isValidEntity(detBo);
				if (!isvalid) {
					throw new DataNotValidException("FCL save: det " + detBo.toString());
				}
			} else {
				if (!InputNameValidation.checkVigilanceRemarks(detDto.getVigilance())) {
					throw new DataNotValidException("Vigilance remarks not valid");
				}
				detBo = mapper.map(detDto, FclVigilance.class);
			}
			detBo.setFcl(fcl);
			poObservationDets.add(detBo);
		}
		return poObservationDets;
	}

	/**
	 * submit fcl service
	 * 
	 * @param fclDto
	 * @return
	 */
	public Fcl submitFcl(FclDto fclDto) {
		Fcl fcl = prepareSubmitFcl(fclDto);
		fcl.setSubmittedDate(new Date());
		return fclRepository.save(fcl);

	}

	
	@Transactional(propagation = Propagation.NESTED)
	private Fcl prepareSubmitFcl(FclDto fclDto)
	{
		Fcl fcl;
		if (fclDto.getId() != 0) {
			Optional<Fcl> fclOptional = fclRepository.findById(fclDto.getId());
			if (fclOptional.isPresent()) {
				fcl = fclOptional.get();
			} else {
				throw new DataNotFoundException("Prepare submit : FCL not found By Id: " + fclDto.getId());
			}
		} else {
			fcl = mapper.map(fclDto, Fcl.class);
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fclDto.getSupplyOrderId());
			fcl.setSupplyOrder(supplyOrder);
			fcl.setCreatedDate(new Date());
		}
		SupplyOrder so = fcl.getSupplyOrder();
		fcl.setOpa(supplyOrderHelper.getOpa(so));
		fcl.setSoOrderNo(so.getOrderNo());
		fcl.setSoDate(fclDto.getSoDate());
		fcl.setProjectName(supplyOrderHelper.getProjectName(so));
		boolean isvalid = entityValidator.isValidEntity(fcl);

		List<FclVigilance> fclVigilancelist = prepareVigilanceDetBos(fclDto.getFclVigDtoList(), fcl);

		fcl.setFclVigilances(fclVigilancelist);
		if (isvalid) {
			fcl = fclRepository.save(fcl);
		} else {
			throw new DataNotValidException("Prepare fcl submit: " + fcl.toString());
		}

		try {
			SupplyOrder supplyOrder = fcl.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdBysoId(supplyOrder.getId());
			if(empId != null) {
				notifyUtil.NotifySBUnCqae("FCL is submitted for the SO no " + supplyOrder.getOrderNo(), supplyOrder.getId(),
						supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(), empId);
			}
			
		} catch (Exception e) {
			logger.error("Notification error in FCL submitted for the order no {} ", e.getMessage());
			logger.error("Error occured while FCL submitted for the order no ", e);
		}

		return fcl;
	}
	
	
	
	/**
	 * 
	 * Fetch the FCL details by the supply order
	 * @param soId
	 * @return
	 */
	public FclDto getFclBySoId(int soId) {
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		if (supplyOrder == null) {
			throw new DataNotFoundException("Get FCL by Supply order : Supply order not found By Id: " + soId);
		}
		Fcl fcl = fclRepository.getFclBySupplyOrder(supplyOrder);
		return convertEntitytoDto(fcl, supplyOrder);
	}
}
