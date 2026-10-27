package in.co.bel.olms.iol.ate.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.iol.ate.dto.InspectionRoundDto;
import in.co.bel.olms.iol.ate.dto.ProductTestMapDto;
import in.co.bel.olms.iol.ate.dto.SubmittedTestsDto;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionRoundService {

	private final EmployeeRepository employeeRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final SubSlipLineItemRepository subSlipLineItemRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final OLLineItemSerialNoRepository olLineItemSerialNoRepository;

	private final ProductsRepository productsRepository;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final OfferLetterRepository offerLetterRepository;

	private final ProductMappingRepository productMappingRepository;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionRoundService.class);
	
	DateFormat dateFormat = new SimpleDateFormat(StringLiteralConstants.DATE_TIME_FORMAT);
	
	private static final String NULL_DATA_ERROR_MESSAGE = "Error: Data is not present or is null.";

	public List<InspectionRoundDto> getRoundId(Integer devId, Integer prodId, Integer olLineItem, Integer empId) {

		Boolean isQa = null;
		Optional<Employee> empOptional = employeeRepository.findById(empId);
		List<InspectionRound> inspectionRounds = new ArrayList<>();
		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();

			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();

				if (orgTypeId != null && orgTypeId == 1) {
					isQa = true;
					inspectionRounds = inspectionRoundRepository.findRoundIdIsQa(devId, prodId, olLineItem);
				} else {
					inspectionRounds = inspectionRoundRepository.findRoundId(devId, prodId, olLineItem);
				}
			}
		}

		List<InspectionRoundDto> dtos = new ArrayList<>();

		if (inspectionRounds.isEmpty()) {
			// round save the
			InspectionRound inspectionRound = new InspectionRound();
			Optional<OLLineItemSerialNo> olLineItemSerialNoOptional = olLineItemSerialNoRepository
          .findFirstBySerialNoIdOrderByCreatedAtDesc(devId);
			if (olLineItemSerialNoOptional.isPresent()) {
				inspectionRound.setLineItemSerialNo(olLineItemSerialNoOptional.get().getSerialNo());
			} else {
				throw new DataNotFoundException("Save : Dev Id not found: " + devId);
			}

			Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(olLineItem);
			if (olLineItemOptional.isPresent()) {
				inspectionRound.setOlLineItem(olLineItemOptional.get());
			} else {
				throw new DataNotFoundException("Save : OlLineIten Id not found: " + olLineItem);
			}

			Optional<Products> productsOptional = productsRepository.findById(prodId);
			if (productsOptional.isPresent()) {
				inspectionRound.setProduct(productsOptional.get());
			} else {
				throw new DataNotFoundException("Save : Product Id not found: " + prodId);
			}
			inspectionRound.setRoundId(1);
			inspectionRound.setIsQa(isQa);
			inspectionRoundRepository.save(inspectionRound);

			InspectionRoundDto dto = new InspectionRoundDto();
			if (inspectionRound != null) {
				dto.setId(inspectionRound.getId());
				dto.setProductId(inspectionRound.getProduct().getId());
				dto.setIsQa(isQa);
				dto.setRoundId(inspectionRound.getRoundId());
			}
			dtos.add(dto);
		} else {
			for (InspectionRound inspectionRound : inspectionRounds) {
				InspectionRoundDto dto = new InspectionRoundDto();
				if (inspectionRound != null) {
					dto.setId(inspectionRound.getId());
					dto.setProductId(inspectionRound.getProduct().getId());
					dto.setIsQa(isQa);
					dto.setRoundId(inspectionRound.getRoundId());
				} else {
					dto.setRoundId(1);
				}
				dtos.add(dto);
			}
		}

		return dtos;

	}

	public boolean isQaEmployee(Integer empId) {
		Optional<Employee> employeeOptional = employeeRepository.findById(empId);
		return employeeOptional.map(employee -> employee.getOrganization() != null
				&& employee.getOrganization().getOrganizationType() != null
				&& employee.getOrganization().getOrganizationType().getId() == 1).orElse(false);

	}

	public List<ProductTestMapDto> getAllTestListByPrdId(Integer devId, Integer prodId, Integer olLineItem,
			Integer roundId, Boolean isQa) {

		List<Object[]> productList;

		if (isQa != null && isQa) {
			productList = inspectionRoundRepository.getAllTestListByPrdIdIsQa(devId, prodId, olLineItem, roundId);
		} else {
			productList = inspectionRoundRepository.getAllTestListByPrdId(devId, prodId, olLineItem, roundId);
		}

		List<ProductTestMapDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductTestMapDto dto = new ProductTestMapDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setProductId(Integer.parseInt(obj[1].toString()));
			dto.setTestId(Integer.parseInt(obj[2].toString()));
			dto.setTestName(obj[3].toString());
			dto.setRoundId(Integer.parseInt(obj[4].toString()));
			dto.setSubmittedById(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
			dto.setSubmittedBy(obj[7] != null ? obj[6].toString() : null);
			dto.setSequence(obj[8] != null ? Integer.parseInt(obj[8].toString()) : null);
			productMappingDtos.add(dto);
		}

		return productMappingDtos;
	}
	
	public List<ProductTestMapDto> getAllTestListInQa(Integer devId, Integer prodId, Integer olLineItem,
			Integer roundId, Boolean isQa) {

		List<Object[]> productList;
        Boolean isFaiSample = olLineItemSerialNoRepository.getIsFaiSample(olLineItem, devId).orElse(null);
        
		if (isQa != null && isQa && isFaiSample!=null && isFaiSample) {
			productList = inspectionRoundRepository.getAllTestListByPrdIdIsFai(devId, prodId, olLineItem, roundId);
		} else {
			productList = inspectionRoundRepository.getAllTestListByPrdIdIsQa(devId, prodId, olLineItem, roundId);
		}

		List<ProductTestMapDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductTestMapDto dto = new ProductTestMapDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setProductId(Integer.parseInt(obj[1].toString()));
			dto.setTestId(Integer.parseInt(obj[2].toString()));
			dto.setTestName(obj[3].toString());
			dto.setRoundId(Integer.parseInt(obj[4].toString()));
			dto.setSubmittedById(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
			dto.setSubmittedBy(obj[7] != null ? obj[6].toString() : null);
			dto.setSequence(obj[8] != null ? Integer.parseInt(obj[8].toString()) : null);
			productMappingDtos.add(dto);
		}

		return productMappingDtos;
	}
	
	public List<ProductTestMapDto> getAllTestListByPrdIdAndSsLiid(Integer devId, Integer prodId, Integer ssLiId,
			Integer roundId, Boolean isQa) {

		List<Object[]> productList;
		if (!productMappingRepository.existsByProductId(prodId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Product with Id " + prodId + " not mapped with any Tests.");
		}

		if (isQa != null && isQa) {
			productList = inspectionRoundRepository.getAllTestListByPrdIdIsQaSsLiId(devId, prodId, ssLiId, roundId);
		} else {
			productList = inspectionRoundRepository.getAllTestListByPrdIdSsLiId(devId, prodId, ssLiId, roundId);
		}
		List<ProductTestMapDto> productMappingDtos = new ArrayList<>();

		if (productList == null || productList.isEmpty()) {
			return productMappingDtos;
		}


		for (Object[] obj : productList) {
			ProductTestMapDto dto = new ProductTestMapDto();
		    dto.setId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : null);
		    dto.setProductId(obj[1] != null ? Integer.parseInt(obj[1].toString()) : null);
		    dto.setTestId(obj[2] != null ? Integer.parseInt(obj[2].toString()) : null);
		    dto.setTestName(obj[3] != null ? obj[3].toString() : ""); 	    
		    dto.setRoundId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : null);
		    dto.setSubmittedById(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
		    dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
		    dto.setSubmittedBy(obj[7] != null ? obj[7].toString() : null);
		    dto.setSequence(obj[8] != null ? Integer.parseInt(obj[8].toString()) : null);
			productMappingDtos.add(dto);
		}

		return productMappingDtos;
	}

	public List<ProductTestMapDto> getAllTestListByPrdIdQa(Integer devId, Integer prodId, Integer olLineItem,
			Integer roundId) {

		List<Object[]> productList = null;

		try {
			productList = inspectionRoundRepository.getAllTestListByPrdId(devId, prodId, olLineItem, roundId);
		} catch (ClassCastException | NullPointerException e) {
			logger.error(NULL_DATA_ERROR_MESSAGE);
		}
		List<ProductTestMapDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductTestMapDto dto = new ProductTestMapDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setProductId(Integer.parseInt(obj[1].toString()));
			dto.setTestId(Integer.parseInt(obj[2].toString()));
			dto.setTestName(obj[3].toString());
			dto.setRoundId(Integer.parseInt(obj[4].toString()));
			dto.setSubmittedById(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
			dto.setSubmittedBy(obj[7] != null ? obj[6].toString() : null);
			productMappingDtos.add(dto);
		}
		return productMappingDtos;

	}
	public List<ProductTestMapDto> getAllTestListByPrdIdIsFai(Integer devId, Integer prodId, Integer olLineItem,
			Integer roundId) {

		List<Object[]> productList;
	    productList = inspectionRoundRepository.getAllTestListByPrdIdIsFai(devId, prodId, olLineItem, roundId);
		List<ProductTestMapDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductTestMapDto dto = new ProductTestMapDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setProductId(Integer.parseInt(obj[1].toString()));
			dto.setTestId(Integer.parseInt(obj[2].toString()));
			dto.setTestName(obj[3].toString());
			dto.setRoundId(Integer.parseInt(obj[4].toString()));
			dto.setSubmittedById(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
			dto.setSubmittedBy(obj[7] != null ? obj[7].toString() : null);
			dto.setSequence(obj[8] != null ? Integer.parseInt(obj[8].toString()) : null);
//			dto.setIsFai(obj[9] !=null ? obj[9].toString() : null);
			productMappingDtos.add(dto);
		}

		return productMappingDtos;
	}
	public List<InspectionRoundDto> getNextRoundId(Integer devId, Integer prodId, Integer olLineItem, Integer empId) {
		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
		Boolean isQa = null;

		Optional<Employee> empOptional = employeeRepository.findById(empId);
		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();
			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();
				if (orgTypeId != null && orgTypeId == 1) {
					isQa = true;
				}
			}
		}

		// Fetch all inspection rounds *before* potentially saving a new one.
		List<InspectionRound> allInspectionRounds;
		if (isQa != null && isQa) {
			allInspectionRounds = inspectionRoundRepository.findRoundIdIsQa(devId, prodId, olLineItem);
		} else {
			allInspectionRounds = inspectionRoundRepository.findRoundId(devId, prodId, olLineItem);
		}

		// Determine the next round ID based on existing rounds
		Integer nextRoundId = 1;
		if (!allInspectionRounds.isEmpty()) {
			nextRoundId = allInspectionRounds.stream().mapToInt(InspectionRound::getRoundId).max().getAsInt() + 1;
		}

		// Only save if a new round needs to be created
		InspectionRound savedinspectionRound = null;

		if (nextRoundId > (allInspectionRounds.stream().mapToInt(InspectionRound::getRoundId).max().orElse(0))) {
			savedinspectionRound = new InspectionRound();
			Optional<OLLineItemSerialNo> olLineItemSerialNoOptional = olLineItemSerialNoRepository
          .findFirstBySerialNoIdOrderByCreatedAtDesc(devId);
			if (olLineItemSerialNoOptional.isPresent()) {
				savedinspectionRound.setLineItemSerialNo(olLineItemSerialNoOptional.get().getSerialNo());
			} else {
				throw new DataNotFoundException("Save : Dev Id not found: " + devId);
			}

			Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(olLineItem);
			if (olLineItemOptional.isPresent()) {
				savedinspectionRound.setOlLineItem(olLineItemOptional.get());
			} else {
				throw new DataNotFoundException("Save : OlLineIten Id not found: " + olLineItem);
			}

			Optional<Products> productsOptional = productsRepository.findById(prodId);
			if (productsOptional.isPresent()) {
				savedinspectionRound.setProduct(productsOptional.get());
			} else {
				throw new DataNotFoundException("Save : Product Id not found: " + prodId);
			}
			savedinspectionRound.setRoundId(nextRoundId);
			savedinspectionRound.setIsQa(isQa);
			savedinspectionRound = inspectionRoundRepository.save(savedinspectionRound);
		}

		for (InspectionRound round : allInspectionRounds) {
			inspectionRoundDtoList.add(mapper.map(round, InspectionRoundDto.class));
		}

		if (savedinspectionRound != null) {
			inspectionRoundDtoList.add(mapper.map(savedinspectionRound, InspectionRoundDto.class));
		}

		return inspectionRoundDtoList;
	}

	public List<InspectionRoundDto> getNextRoundIdBySsLiId(Integer devId, Integer prodId, Integer ssLiId, Integer empId) {
		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
		Boolean isQa = null;

		Optional<Employee> empOptional = employeeRepository.findById(empId);
		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();
			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();
				if (orgTypeId != null && orgTypeId == 1) {
					isQa = true;
				}
			}
		}

		// Fetch all inspection rounds *before* potentially saving a new one.
		List<InspectionRound> allInspectionRounds;
		if (isQa != null && isQa) {
			allInspectionRounds = inspectionRoundRepository.findRoundIdIsQaSsLiId(devId, prodId, ssLiId);
		} else {
			allInspectionRounds = inspectionRoundRepository.findRoundIdSsLiId(devId, prodId, ssLiId);
		}

		// Determine the next round ID based on existing rounds
    int nextRoundId = 1;
		if (!allInspectionRounds.isEmpty()) {
			nextRoundId = allInspectionRounds.stream().mapToInt(InspectionRound::getRoundId).max().getAsInt() + 1;
		}

		// Only save if a new round needs to be created
		InspectionRound savedinspectionRound = null;

		if (nextRoundId > (allInspectionRounds.stream().mapToInt(InspectionRound::getRoundId).max().orElse(0))) {
			savedinspectionRound = new InspectionRound();

      SubSlipLineItem subSlipLineItem = subSlipLineItemRepository.findById(ssLiId)
          .orElseThrow(() -> new IllegalArgumentException("Save : subSlipLineItem Id not found: " + ssLiId));
      savedinspectionRound.setSubSlipLineItem(subSlipLineItem);

            int sSlipId = subSlipLineItem.getSubmissionSlip().getId();
//      SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findByWarrantDetailsLineItemSerialNoIdAndWarrantDetailsCategoryId(devId, categoryId)
            SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findBySslipIdAndLisnId(sSlipId, devId)
          .orElseThrow(() -> new IllegalArgumentException("Save : Dev Id not found: " + devId));
			savedinspectionRound.setSubSlipSerialNo(subSlipSerialNo);

      Products product = productsRepository.findById(prodId)
          .orElseThrow(() -> new IllegalArgumentException("Save : Product Id not found: " + prodId));
      savedinspectionRound.setProduct(product);

			savedinspectionRound.setRoundId(nextRoundId);
			savedinspectionRound.setIsQa(isQa);
			savedinspectionRound = inspectionRoundRepository.save(savedinspectionRound);
		}

		for (InspectionRound round : allInspectionRounds) {
			inspectionRoundDtoList.add(mapper.map(round, InspectionRoundDto.class));
		}

		if (savedinspectionRound != null) {
			inspectionRoundDtoList.add(mapper.map(savedinspectionRound, InspectionRoundDto.class));
		}

		return inspectionRoundDtoList;
	}
	
	public List<InspectionRoundDto> getListofRoundIds(Integer devId, Integer prodId, Integer olLineItem,
			Integer empId,Boolean isQa ) {
		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
//		Boolean isQa = null;
		Optional<Employee> empOptional = employeeRepository.findById(empId);
		List<InspectionRound> inspectionRounds = new ArrayList<>(); // Changed to Integer for round IDs

		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();
			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();
				if ((orgTypeId != null && orgTypeId == 1) || (isQa != null && isQa)) {
//					isQa = true;
					inspectionRounds = inspectionRoundRepository.findRoundIdIsQa(devId, prodId, olLineItem);
				} else {
					inspectionRounds = inspectionRoundRepository.findRoundId(devId, prodId, olLineItem);
				}
			}
		}

		for (InspectionRound inspectionRound : inspectionRounds) {
			InspectionRoundDto dto = mapper.map(inspectionRound, InspectionRoundDto.class);
			inspectionRoundDtoList.add(dto);
		}

		return inspectionRoundDtoList;
	}
	
//	public List<InspectionRoundDto> getListofRoundIdsOlFirm(Integer devId, Integer prodId, Integer olLineItem,
//			Integer empId) {
//		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
//		Boolean isQa = null;
//		Optional<Employee> empOptional = employeeRepository.findById(empId);
//		List<InspectionRound> inspectionRounds = new ArrayList<>(); // Changed to Integer for round IDs
//        Integer ssLiId = subSlipLineItemRepository.findSslipLineItemByOlLineItemId(olLineItem);
//		if (empOptional.isPresent()) {
//			Employee employee = empOptional.get();
//			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
//				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();
//				if (orgTypeId != null && orgTypeId == 1) {
//					isQa = true;
//					inspectionRounds = inspectionRoundRepository.findRoundIdIsQaSsLiId(devId, prodId, ssLiId);
//				} else {
//					inspectionRounds = inspectionRoundRepository.findRoundIdSsLiId(devId, prodId, ssLiId);
//				}
//			}
//		}
//
//		for (InspectionRound inspectionRound : inspectionRounds) {
//			InspectionRoundDto dto = mapper.map(inspectionRound, InspectionRoundDto.class);
//			inspectionRoundDtoList.add(dto);
//		}
//
//		return inspectionRoundDtoList;
//	}

	public List<InspectionRoundDto> getListofRoundIdsBySSlipLineItem(Integer devId, Integer prodId, Integer ssLiId,
			Integer empId) {
		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
		Optional<Employee> empOptional = employeeRepository.findById(empId);
		List<InspectionRound> inspectionRounds = new ArrayList<>(); // Changed to Integer for round IDs

		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();
			if (employee.getOrganization() != null && employee.getOrganization().getOrganizationType() != null) {
				Integer orgTypeId = employee.getOrganization().getOrganizationType().getId();
				if (orgTypeId != null && orgTypeId == 1) {
					inspectionRounds = inspectionRoundRepository.findRoundIdIsQaSsLiId(devId, prodId, ssLiId);
				} else {
					inspectionRounds = inspectionRoundRepository.findRoundIdSsLiId(devId, prodId, ssLiId);
				}
			}
		}

		for (InspectionRound inspectionRound : inspectionRounds) {
			InspectionRoundDto dto = mapper.map(inspectionRound, InspectionRoundDto.class);
			inspectionRoundDtoList.add(dto);
		}

		return inspectionRoundDtoList;
	}
	
	public <T> InspectionRound populateInspectionRound(InspectionRoundDto dto, boolean isSave,
			InspectionRound inspectionRound) {

		if (inspectionRound == null) {
			inspectionRound = new InspectionRound();
		}
		if (!isSave) {
			inspectionRound = mapper.map(dto, InspectionRound.class);
		}
//
//		Optional<LineItemLotsRegister> lineItemLotsRegisterOptional = lineItemLotsRegisterRepository
//				.findById(dto.getDevId());
//		if (lineItemLotsRegisterOptional.isPresent()) {
//			inspectionRound.setLineItemLotsRegister(lineItemLotsRegisterOptional.get());
//		} else {
//			throw new DataNotFoundException("Save : Dev Id not found: " + dto.getDevId());
//		}

		
		if(null != dto.getIsQa() && dto.getIsQa())
		{
      // add for offer letter
      if (dto.getOlId() != null) {
        OfferLetter offerletter = offerLetterRepository.findById(dto.getOlId())
            .orElseThrow(() -> new IllegalArgumentException("offerLetter Id not found: " + dto.getOlId()));
        inspectionRound.setOfferletter(offerletter);
      } else {
        throw new IllegalArgumentException("offerLetter id is required.");
      }
			// and ol line item id
      if (dto.getOlLineItemId() != null) {
        OlLineItem olLineItem = olLineItemRepository.findById(dto.getOlLineItemId())
            .orElseThrow(() -> new IllegalArgumentException("Save : OlLineItem Id not found: " + dto.getOlLineItemId()));
        inspectionRound.setOlLineItem(olLineItem);
      } else {
        throw new IllegalArgumentException("OlLineItem id is required.");
      }
		}
		else {
      if (dto.getDevId() != null) {
        SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findById(dto.getDevId())
            .orElseThrow(() -> new IllegalArgumentException("Save : Dev Id not found: " + dto.getDevId()));
        inspectionRound.setSubSlipSerialNo(subSlipSerialNo);
      } else {
        throw new IllegalArgumentException("Dev id is required.");
      }

			// add for submission slip line item id code..
      if (dto.getSubSlipLineItemId() != null) {
        SubSlipLineItem subSlipLineItem = subSlipLineItemRepository.findById(dto.getSubSlipLineItemId())
            .orElseThrow(() -> new IllegalArgumentException("Save : subSlipLineItem Id not found: " + dto.getSubSlipLineItemId()));
        inspectionRound.setSubSlipLineItem(subSlipLineItem);
      } else {
        throw new IllegalArgumentException("subSlipLineItem id is required.");
      }
		}
		
		
//		Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(dto.getOlLineItemId());
//		if (olLineItemOptional.isPresent()) {
//			inspectionRound.setOlLineItem(olLineItemOptional.get());
//		} else {
//			throw new DataNotFoundException("Save : OlLineItem Id not found: " + dto.getOlLineItemId());
//		}

		if (dto.getApprovedById() != null) {
      Employee approvedBy = employeeRepository.findById(dto.getApprovedById())
          .orElseThrow(() -> new IllegalArgumentException("Save : ApprovedBy Id not found: " + dto.getApprovedById()));
      inspectionRound.setApprovedBy(approvedBy);
		}

    if (dto.getProductId() != null) {
      Products product = productsRepository.findById(dto.getProductId())
          .orElseThrow(() -> new IllegalArgumentException("Save : Product Id not found: " + dto.getProductId()));
      inspectionRound.setProduct(product);
    } else {
      throw new IllegalArgumentException("Product id is required.");
    }

		if (dto.getSubmittedById() != null) {
      Employee submittedBy = employeeRepository.findById(dto.getSubmittedById())
          .orElseThrow(() -> new IllegalArgumentException("Save : SubmittedBy Id not found: " + dto.getApprovedById()));
      inspectionRound.setSubmittedBy(submittedBy);
		}

    inspectionRound.setCreatedDatetime(new Date());
		inspectionRound.setRoundId(dto.getRoundId());
		inspectionRound.setIsQa(dto.getIsQa());
		return inspectionRound;
	}

	public List<SubmittedTestsDto> getAllSubmittedTestStatusByProdIdDevId(Integer devId, Integer prodId,
			Integer olLineItem, Integer roundId, Boolean isQa, Boolean showQa, Boolean qa) {
		List<SubmittedTestsDto> submittedTestDtos = new ArrayList<>();
		List<Object[]> submittedTest = null;
		try {
			
			if( qa!= null && !qa) {
				submittedTest = inspectionRoundRepository.getAllSubmittedTestStatusByProdIdDevId(devId, prodId,
						olLineItem, roundId);					
			}		
			if ((isQa != null && isQa)|| (showQa!=null && showQa)) {
				submittedTest = inspectionRoundRepository.getAllSubmittedTestStatusByProdIdDevIdIsQa(devId, prodId,
						olLineItem, roundId);
			} else {
				submittedTest = inspectionRoundRepository.getAllSubmittedTestStatusByProdIdDevId(devId, prodId,
						olLineItem, roundId);
			}	
			
//     submittedTest = inspectionRoundRepository.getAllSubmittedTestStatusByProdIdDevId(devId, prodId, olLineItem, roundId);     
		} catch (ClassCastException | NullPointerException e) {
			logger.error(NULL_DATA_ERROR_MESSAGE);
		}
		for (Object[] testDto : submittedTest) {
			SubmittedTestsDto dto = new SubmittedTestsDto();
			dto.setProdId(testDto[0] != null ? (int) testDto[0] : null);
			dto.setTestId(testDto[1] != null ? (int) testDto[1] : null);
			dto.setTestStatus(testDto[2] != null ? testDto[2].toString() : null);
			dto.setSubmittedBy(testDto[3] != null ? testDto[3].toString() : null);
//			dto.setSubmittedDate(testDto[4] != null ? dateFormat.format((Date) testDto[4]) : null);
//			System.out.println("--------" + testDto[4] );
//			System.out.println("*****" + dateFormat.format((Date)testDto[4]));
			dto.setSubmitDate(testDto[4] != null ? dateFormat.format((Date)testDto[4])  : null);
			dto.setSequence(testDto[5]!=null ? (Integer)testDto[5]: null);
			submittedTestDtos.add(dto);
		}
		return submittedTestDtos;
	}

	public List<SubmittedTestsDto> getAllSubmittedTestStatusByProdIdDevIdQa(Integer devId, Integer prodId,
			Integer olLineItem, Integer roundId) {
		List<SubmittedTestsDto> submittedTestDtos = new ArrayList<>();
		List<Object[]> submittedTest = null;
		try {
			submittedTest = inspectionRoundRepository.getAllSubmittedTestStatusByProdIdDevId(devId, prodId, olLineItem,
					roundId);

		} catch (ClassCastException | NullPointerException e) {
			logger.error(NULL_DATA_ERROR_MESSAGE);
		}
		for (Object[] testDto : submittedTest) {
			SubmittedTestsDto dto = new SubmittedTestsDto();
			dto.setProdId(testDto[0] != null ? (int) testDto[0] : null);
			dto.setTestId(testDto[1] != null ? (int) testDto[1] : null);
			dto.setTestStatus(testDto[2] != null ? testDto[2].toString() : null);
			dto.setSubmittedBy(testDto[3] != null ? testDto[3].toString() : null);
//			dto.setSubmittedDate(testDto[4] != null ? (Date) testDto[4] : null);
			dto.setSubmitDate(testDto[4] != null ? dateFormat.format((Date)testDto[4])  : null);
			submittedTestDtos.add(dto);
		}
		return submittedTestDtos;
	}

	public List<InspectionRoundDto> getListofRoundIdsQa(Integer devId, Integer prodId, Integer olLineItem) {
		List<InspectionRoundDto> inspectionRoundDtoList = new ArrayList<>();
		
		List<InspectionRound> inspectionRounds = new ArrayList<>(); // Changed to Integer for round IDs
		try {
			inspectionRounds = inspectionRoundRepository.findRoundId(devId, prodId, olLineItem);

		} catch (ClassCastException | NullPointerException e) {
			logger.error(NULL_DATA_ERROR_MESSAGE);
		}
		for (InspectionRound inspectionRound : inspectionRounds) {
			InspectionRoundDto dto = mapper.map(inspectionRound, InspectionRoundDto.class);
			inspectionRoundDtoList.add(dto);
		}

		return inspectionRoundDtoList;
	}

}
