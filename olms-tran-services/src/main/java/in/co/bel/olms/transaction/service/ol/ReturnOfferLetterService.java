package in.co.bel.olms.transaction.service.ol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import in.co.bel.olms.transaction.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.ReturnOfferLetterDetDto;
import in.co.bel.olms.transaction.dto.ReturnOfferLetterDto;
import in.co.bel.olms.transaction.repository.ReturnOfferLetterDetRepository;
import in.co.bel.olms.transaction.repository.ReturnOfferLetterRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 11-05-2023
 *
 */
@Service
@RequiredArgsConstructor
public class ReturnOfferLetterService {

Logger logger = LoggerFactory.getLogger(ReturnOfferLetterService.class);


	private final ReturnOfferLetterRepository returnOfferLetterRepo;

	private final ReturnOfferLetterDetRepository returnOfferLetterDetRepository;

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;

	private final EmployeeDao employeeDao;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	/**
	 * 
	 * @param returnOfferLetter
	 * @return
	 */
	public ReturnOfferLetterDto convertBoToDtoForTable(ReturnOfferLetter returnOfferLetter) {
		ReturnOfferLetterDto dto = mapper.map(returnOfferLetter, ReturnOfferLetterDto.class);
		OfferLetter offerLetter = returnOfferLetter.getOfferLetter();
		ReturnOfferLetterDet det = new ReturnOfferLetterDet();
		List<ReturnOfferLetterDetDto> lDetDto = new ArrayList<>();
		Optional<ReturnOfferLetterDet> optRetDto = returnOfferLetterDetRepository.findById(returnOfferLetter.getId());
		if (optRetDto.isPresent()) {
			det = optRetDto.get();
		}
		ReturnOfferLetterDetDto detDto = mapper.map(det, ReturnOfferLetterDetDto.class);
		lDetDto.add(detDto);
		dto.setOfferLetterNo(offerLetter.getOlNumber());
		dto.setOfferLetterId(offerLetter.getId());
		dto.setCreatedDateStr(dateFormat.format(returnOfferLetter.getCreatedDate()));
		if (null != offerLetter.getSaleOrder().getSupplyOrder()) {
			SaleOrder saleOrder = offerLetter.getSaleOrder();
			SupplyOrder so = offerLetter.getSaleOrder().getSupplyOrder();
			dto.setSoNumber(so.getOrderNo());
			dto.setDetDtos(lDetDto);
			if (null != saleOrder.getCategory() && null != saleOrder.getCategory().getShip()
					&& null != saleOrder.getCategory().getShip().getProject()) {
				dto.setProjectName(saleOrder.getCategory().getShip().getProject().getProjectName());
			} else {
				dto.setProjectName("");
			}

			if (null != so.getSalesGroup()) {
				dto.setSalesGrp(so.getSalesGroup().getName());
			} else {
				dto.setSalesGrp("");
			}

		} else {
			dto.setSoNumber("");
			dto.setSalesGrp("");
			dto.setProjectName("");
		}

		try {
			dto.setOrgTypeId(offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getOrganization()
					.getOrganizationType().getId());
		} catch (Exception e) {
			logger.error("Error occured  {}", e.getMessage());
		}
		return dto;
	}

	/**
	 * 
	 * @param returnOfferLetter
	 * @return
	 */
	public ReturnOfferLetterDto convertBoToDto(ReturnOfferLetter returnOfferLetter) {
		ReturnOfferLetterDto dto = mapper.map(returnOfferLetter, ReturnOfferLetterDto.class);
		dto.setOfferLetterNo(returnOfferLetter.getOfferLetter().getOlNumber());
		dto.setOfferDate(returnOfferLetter.getOfferLetter().getOfferDate());
		SaleOrder saleOrder = returnOfferLetter.getOfferLetter().getSaleOrder();
		if (null != saleOrder.getSupplyOrder()) {
			SupplyOrder so = returnOfferLetter.getOfferLetter().getSaleOrder().getSupplyOrder();
			dto.setSoNumber(so.getOrderNo());
			if (null != saleOrder.getCategory() && null != saleOrder.getCategory().getShip()
					&& null != saleOrder.getCategory().getShip().getProject()) {
				dto.setProjectName(saleOrder.getCategory().getShip().getProject().getProjectName());
			} else {
				dto.setProjectName("");
			}
		} else {
			dto.setSoNumber("");
			dto.setSalesGrp("");
			dto.setProjectName("");
		}
		List<ReturnOfferLetterDetDto> detDtos = new ArrayList<>();
		List<ReturnOfferLetterDet> returnOfferLetterDets = returnOfferLetter.getReturnOfferLetterDets();
		for (ReturnOfferLetterDet detBo : returnOfferLetterDets) {
			ReturnOfferLetterDetDto detDto = mapper.map(detBo, ReturnOfferLetterDetDto.class);

			detDtos.add(detDto);

		}
		dto.setDetDtos(detDtos);
		return dto;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ReturnOfferLetterDto getById(int id) {
		Optional<ReturnOfferLetter> returnOfferLetteropt = returnOfferLetterRepo.findById(id);
		if (returnOfferLetteropt.isPresent()) {
			ReturnOfferLetter returnOfferLetter = returnOfferLetteropt.get();
			ReturnOfferLetterDto dto = convertBoToDto(returnOfferLetter);
			return dto;
		} else {
			throw new DataNotValidException("Return OL no data: " + id);
		}
	}

	/**
	 * 
	 * @param returnOfferLetterDto
	 * @return
	 */
	// Todo : to be completed with full details
	public ReturnOfferLetter saveReturnOfferLetter(ReturnOfferLetterDto returnOfferLetterDto) {
		ReturnOfferLetter returnOfferLetter = mapper.map(returnOfferLetterDto, ReturnOfferLetter.class);
		returnOfferLetter.setCreatedDate(new Date());
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(returnOfferLetterDto.getOfferLetterId());
		returnOfferLetter.setOfferLetter(offerLetter);

		boolean isValid = entityValidator.isValidEntity(returnOfferLetter);
		if (isValid) {
			returnOfferLetter = returnOfferLetterRepo.save(returnOfferLetter);
		} else {
			throw new DataNotValidException("Return OL save: " + returnOfferLetter.toString());
		}

		List<ReturnOfferLetterDet> detBos = prepareDetBos(returnOfferLetterDto.getDetDtos(), returnOfferLetter);
		detBos = returnOfferLetterDetRepository.saveAll(detBos);

		returnOfferLetter.setReturnOfferLetterDets(detBos);
		return returnOfferLetter;
	}

	public ReturnOfferLetterDto getByOlId(int offerLetterId) {

		ReturnOfferLetter returnOfferLetter = returnOfferLetterRepo.getByOlId(offerLetterId);
		if (null != returnOfferLetter) {
			ReturnOfferLetterDto dto = convertBoToDto(returnOfferLetter);
			return dto;
		} else {
			throw new DataNotValidException("Return OL no data: " + offerLetterId);
		}
	}

	/**
	 * 
	 * @param detDtos
	 * @param returnOfferLetter
	 * @return
	 */
	private List<ReturnOfferLetterDet> prepareDetBos(List<ReturnOfferLetterDetDto> detDtos,
			ReturnOfferLetter returnOfferLetter) {
		List<ReturnOfferLetterDet> detBos = new ArrayList<>();

		for (ReturnOfferLetterDetDto detDto : detDtos) {
			if (StringUtils.isNoneBlank(detDto.getObservation())) {
				ReturnOfferLetterDet detBo = mapper.map(detDto, ReturnOfferLetterDet.class);
				detBo.setReturnOfferLetter(returnOfferLetter);
				detBos.add(detBo);
			} else {
				throw new DataNotValidException(
						"Return OL Observation is empty : " + returnOfferLetter.getOfferLetter().getId());
			}
		}

		return detBos;
	}

	/**
	 * 
	 * @param salesGrpId
	 * @return
	 */
	public List<ReturnOfferLetterDto> getBySalesGrpId(int salesGrpId) {

		List<ReturnOfferLetterDto> dtos = new ArrayList<>();

		List<ReturnOfferLetter> bos = returnOfferLetterRepo.getBySalesGrpId(salesGrpId);
		for (ReturnOfferLetter bo : bos) {
			ReturnOfferLetterDto dto = convertBoToDtoForTable(bo);
			dtos.add(dto);
		}
		return dtos;
	}

	public List<ReturnOfferLetterDto> getByEmpnSub(int empId) {

		List<ReturnOfferLetterDto> dtos = new ArrayList<>();
		List<ReturnOfferLetter> bos = returnOfferLetterRepo.getByEmpnSub(empId);
		for (ReturnOfferLetter bo : bos) {
			ReturnOfferLetterDto dto = convertBoToDtoForTable(bo);
			dtos.add(dto);
		}
		return dtos;
	}

	/**
	 * @author Manasa B
	 * @param roleId
	 * @param empId
	 * @return
	 */
	public List<ReturnOfferLetterDto> getByRolenEmp(int roleId, int empId) {

		List<ReturnOfferLetterDto> dtos = new ArrayList<>();
		List<ReturnOfferLetter> bos;

		bos = returnOfferLetterRepo.getByEmpnSub(empId);

		for (ReturnOfferLetter bo : bos) {
			ReturnOfferLetterDto dto = convertBoToDtoForTable(bo);
			dtos.add(dto);
		}
		return dtos;
	}

	/**
	 * 
	 * @param empId
	 * @return
	 */
	public List<ReturnOfferLetterDto> getByEmp(int empId) {
		Employee employee = employeeDao.getEmployee(empId);

		if (employee.getOrganization().getOrganizationType().getId() == ApplicationConstants.OrgType.ID_CQAE) {
			return getByRolenEmp(employee.getEmpRole().getId(), empId);
		} else {

			return getBySalesGrpId(employee.getSalesGroup().getId());
		}

	}
}
