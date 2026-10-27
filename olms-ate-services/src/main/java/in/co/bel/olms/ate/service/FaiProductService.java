package in.co.bel.olms.ate.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import in.co.bel.olms.ate.constants.ApplicationConstants;
import in.co.bel.olms.ate.constants.StringLiteralConstants;
import in.co.bel.olms.ate.dto.FaiProductDto;
import in.co.bel.olms.ate.dto.ObsRemDto;
import in.co.bel.olms.ate.dto.ObservationDto;
import in.co.bel.olms.ate.dto.ProductSpecMappingDto;
import in.co.bel.olms.ate.dto.RdsRecDto;
import in.co.bel.olms.ate.entity.FAI;
import in.co.bel.olms.ate.entity.Observation;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.repository.FaiRepository;
import in.co.bel.olms.ate.repository.ObservationRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaiProductService {
	
	private static final Logger logger = LoggerFactory.getLogger(FaiProductService.class);

	private final ObservationRepository observationRepository;

	private final SpecRepository specRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final ProductsRepository productsRepository;

	private final LineItemLotsRegisterRepository  olLineItemSerialNoRepository ;

	private final FaiRepository  faiRepository ;

	private final EmployeeRepository employeeRepository;

	private final Mapper mappers;
	
	private static final String OL_LINE_ITEM_ID = StringLiteralConstants.OL_LINEITEM_ID_ERROR_STRING;
	private static final String OL_LINE_ITEM_SL_NO_ID = StringLiteralConstants.OL_LINEITEM_ID_SERIAL_NO_ERROR_STRING;
	private static final String CREATED_BY_ID = StringLiteralConstants.CREATED_BY_ID_ERROR_STRING;
	private static final String NOT_FOUND = " not found.";

	Gson gson = new Gson();

	SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);

	public ResponseEntity<String> saveAteObs(@RequestBody FaiProductDto faiDto, Boolean isSubmit)
			throws JsonMappingException, ParseException {
		boolean isValidSpec = false;
		Set<String> devProdIdsInRequest = new HashSet<>();
		List<Observation> existingRdsForDevId = new ArrayList<>();
		List<Observation> existingRds = new ArrayList<>();
		boolean hasInvalidId = false;

		Optional<FAI> faiIdOptional = faiRepository.findById(faiDto.getFaiId());
		if (faiIdOptional.isEmpty()) {
			return ResponseEntity.badRequest().body("Error: Fai Id not found");
		}
		
		List<ObservationDto> dtoList = faiDto.getDtoList();
		Integer prodsId = dtoList.get(0).getProdId();
		Integer olLineItemId = dtoList.get(0).getOlLineItemId();
		Integer olLineItemSlNoId = dtoList.get(0).getDevId();

		if (faiIdOptional.isPresent()) {
			FAI fai = faiIdOptional.get();
			fai.setSubQty(faiDto.getSubQty());
			fai.setActQty(faiDto.getActQty());
			fai.setRejQty(faiDto.getRejQty());
			fai.setRemarks(faiDto.getRemarks());
			faiRepository.save(fai);
		} else {
			return ResponseEntity.badRequest().body("Error: Fai Id not found");
		}

		List<ObservationDto> dtoListDtos = faiDto.getDtoList();

		
		String subString1 = StringLiteralConstants.OL_LINEITEM_ID_NOT_PRESENT_STRING;
		String subString2 = StringLiteralConstants.OL_LINEITEM_ID_SERIAL_NO_NOT_PRESENT_STRING;
		String subString3 = StringLiteralConstants.EMPLOYEE_NOT_PRESENT_STRING;
		
		for (ObservationDto dto : dtoListDtos) {
			Optional<OlLineItem> olLineItem = olLineItemRepository.findById(dto.getOlLineItemId());

			Optional<LineItemLotsRegister> oLLineItemSerialNo = olLineItemSerialNoRepository.findById(dto.getDevId());
			
			Optional<Employee> createdBy = employeeRepository.findById(dto.getCreatedById());	
			
			if (dto.getId() == 0 && isSubmit.equals(true)) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID  + dto.getOlLineItemId() + subString1);
				}

				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId() + subString2);
				}

				if (createdBy.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(CREATED_BY_ID + dto.getCreatedById() + subString3);
				}
				
			}

			if (dto.getId() != 0 && isSubmit.equals(true)) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}

				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId()	+ subString2);
				}
				
				if (createdBy.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(CREATED_BY_ID + dto.getCreatedById() + subString3);					
				}

			}

			if (dto.getId() == 0 && isSubmit.equals(false)) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}
				
				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId()	+ subString2);
				}
				
				if (createdBy.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(CREATED_BY_ID + dto.getCreatedById() + subString3);					
				}
				
			} else if (dto.getId() != 0) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}

				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId()	+ subString2);
				}
				
				if (createdBy.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(CREATED_BY_ID + dto.getCreatedById() + subString3);
					
				}

			}
			ProductSpecMappingDto specMappingDto = new ProductSpecMappingDto();
			specMappingDto.setProductId(dto.getProdId());
			specMappingDto.setSpecId(dto.getObsRemDtos().get(0).getSpecId());
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
			List<ObservationDto> savedRdsDtos = new ArrayList<>();
			List<Observation> savedObservations = new ArrayList<>();
			if (hasInvalidId = dtoList.stream().anyMatch(dto -> dto.getId() == 0)) {
				for (ObservationDto dto : dtoList) {
					Observation observation = new Observation();
					observation.setCreatedDatetime(new Date());
					observation.setOlLineItem(olLineItemRepository.findById(dto.getOlLineItemId()).orElseThrow());
					observation.setProduct(productsRepository.findById(dto.getProdId()).orElseThrow());
					observation.setLineItemLotsRegister(
							olLineItemSerialNoRepository.findById(dto.getDevId()).orElseThrow());
					observation.setFai(faiRepository.findById(faiDto.getFaiId()).orElseThrow());
					observation.setPageNo(dto.getPageNo());
					observation.setCreatedBy(employeeRepository.findById(dto.getCreatedById()).orElseThrow());
					
					if (dto.getSubmittedDate() != null) {
						observation.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
					} else {
						observation.setSubmittedDate(null);
					}
					Optional<Spec> specOptional = specRepository.findById(dto.getObsRemDtos().get(0).getSpecId());
					if (specOptional.isEmpty()) {
						return ResponseEntity.badRequest()
								.body("Spec ID " + dto.getObsRemDtos().get(0).getSpecId() + NOT_FOUND);
					}
					observation.setSpec(specOptional.get());
					observation.setObs(dto.getObsRemDtos().get(0).getObs());
					observation.setObsValue(dto.getObsRemDtos().get(0).getObsValue());

					if (isValidRemarks(dto.getObsRemDtos().get(0).getRem())) {
						observation.setRemarks(dto.getObsRemDtos().get(0).getRem());
					} else {
						return ResponseEntity.badRequest().body("Please enter remarks without special characters");
					}
					if (dto.getObsRemDtos().get(0).getResultId() != null
							&& dto.getObsRemDtos().get(0).getResultId() == 0) {
						observation.setResult("Pass");
					} else {
						observation.setResult("Fail");
					}
					Observation newEntity = observationRepository.save(observation);
					savedObservations.add(newEntity);
					ObservationDto rdsDtos = mappers.map(newEntity, ObservationDto.class);					
					List<ObsRemDto> lObsRemDtos = new ArrayList<>();
					ObsRemDto obsRemDto = new ObsRemDto();
					obsRemDto.setSpecId(newEntity.getSpec().getId());
					obsRemDto.setObs(newEntity.getObs());
					obsRemDto.setObsValue(newEntity.getObsValue());
					obsRemDto.setRem(newEntity.getRemarks());
					obsRemDto.setResult(newEntity.getResult());
					if (newEntity.getResult() == "Pass") {
						obsRemDto.setResultId(0);
					} else {
						obsRemDto.setResultId(1);
					}
					lObsRemDtos.add(obsRemDto);
					rdsDtos.setObsRemDtos(lObsRemDtos);
					rdsDtos.setId(newEntity.getId());
					savedRdsDtos.add(rdsDtos);
					faiDto.setDtoList(savedRdsDtos);
				}
			} else {
				for (ObservationDto dto : dtoList) {
					List<ObsRemDto> updatedObsRemDtos = new ArrayList<>();
					if (dto.getId() != 0) {						
						Optional<Observation> existingEntityOptional = observationRepository.findById(dto.getId());
						if (existingEntityOptional.isPresent()) {						
							Observation existingEntity = existingEntityOptional.get();						
							existingEntity.setOlLineItem(olLineItemRepository.findById(dto.getOlLineItemId()).orElseThrow());
							existingEntity.setProduct(productsRepository.findById(dto.getProdId()).orElseThrow());
							existingEntity.setLineItemLotsRegister(
									olLineItemSerialNoRepository.findById(dto.getDevId()).orElseThrow());
							existingEntity.setFai(faiRepository.findById(faiDto.getFaiId()).orElseThrow());
							existingEntity.setPageNo(dto.getPageNo());
							existingEntity.setCreatedBy(employeeRepository.findById(dto.getCreatedById()).orElseThrow());
							

							existingEntity.setCreatedDatetime(existingEntity.getCreatedDatetime());
							if (dto.getSubmittedDate() != null) {
								existingEntity.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
							}
							if (existingEntityOptional.get().getCreatedDatetime() != null) {
								dto.setCreatedDatetime(existingEntityOptional.get().getCreatedDatetime().toString());
							}
							
							for(ObsRemDto obsRemDto : dto.getObsRemDtos()) {
								Optional<Spec> specOptional = specRepository.findById(obsRemDto.getSpecId());
								 if (specOptional.isPresent()) {
									Spec spec = specOptional.get();
									existingEntity.setSpec(spec);
									existingEntity.setObs(obsRemDto.getObs());
									existingEntity.setObsValue(obsRemDto.getObsValue());
									if (dto.getObsRemDtos().get(0).getResultId() != null && dto.getObsRemDtos().get(0).getResultId() == 0) {
										existingEntity.setResult("Pass");
									} else {
										existingEntity.setResult("Fail");
									}

									if (isValidRemarks(dto.getObsRemDtos().get(0).getRem())) {
										existingEntity.setRemarks(dto.getObsRemDtos().get(0).getRem());
									} else {
										return ResponseEntity.badRequest()
												.body("Please enter remarks without special characters");
									}

									if (dto.getSubmittedDate() != null) {
										existingEntity.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
									}
									Observation savedEnity = observationRepository.save(existingEntity);
									savedObservations.add(savedEnity);
									ObsRemDto savedObsRemDto = new ObsRemDto();
									savedObsRemDto.setSpecId(savedEnity.getSpec().getId());
									savedObsRemDto.setObs(savedEnity.getObs());
									savedObsRemDto.setRem(savedEnity.getRemarks());
									savedObsRemDto.setObsValue(savedEnity.getObsValue());
									savedObsRemDto.setResult(savedEnity.getResult());
									if (savedEnity.getResult() == "Pass") {
										savedObsRemDto.setResultId(0);
									}
									if (savedEnity.getResult() == "Fail") {
										savedObsRemDto.setResultId(1);
									}
									updatedObsRemDtos.add(savedObsRemDto);
						      	}
								 else {
										return ResponseEntity.badRequest().body("Spec ID " + obsRemDto.getSpecId() + NOT_FOUND);
									}							
							}
							dto.setObsRemDtos(updatedObsRemDtos);
							savedRdsDtos.add(dto);
							faiDto.setDtoList(savedRdsDtos);							
						}
					}
					else {
						return ResponseEntity.badRequest().body("Observation ID " + dto.getId() + NOT_FOUND);
					}
				}
			}
			
			//update device/reg-no status			
			if (isSubmit.equals(true)) {
				Map<Integer, List<Observation>> map = savedObservations.stream()
						.collect(Collectors.groupingBy(obs -> obs.getLineItemLotsRegister().getId()));
				for (Integer i : map.keySet()) {
					List<Observation> observations = map.get(i);
					LineItemLotsRegister lineItemLotsRegister = observations.isEmpty() ? null
							: observations.get(0).getLineItemLotsRegister();
					if (lineItemLotsRegister != null) {
						boolean hasFail = observations.stream()
								.anyMatch(obs -> obs.getResult().equalsIgnoreCase("Fail"));
						if (hasFail) {
							lineItemLotsRegister.setStatus(ApplicationConstants.FAIL);
						} else {
							lineItemLotsRegister.setStatus(ApplicationConstants.PASS);
						}
						olLineItemSerialNoRepository.save(lineItemLotsRegister);
					} else {
						return ResponseEntity.badRequest().body("No observations found for register ID: " + i);
					}
				}
			}
			return ResponseEntity.ok(gson.toJson(faiDto));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving RDS records." + e.getMessage());

		}
	}

	public ResponseEntity<String> submitAteObs(FaiProductDto dto, Boolean isSubmit)
			throws JsonMappingException, ParseException {
		ResponseEntity<String> res = saveAteObs(dto, isSubmit);
		return res;
	}

	public List<RdsRecDto> getRdsRecDashDet(int prodId) {
		List<Object[]> resultList = observationRepository.getRdsRecDashDet(prodId);

		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}

		List<RdsRecDto> rdsRecDtoList = new ArrayList<>();
		for (Object[] record : resultList) {
			RdsRecDto rdsRecDto = new RdsRecDto();
			rdsRecDto.setId((Integer) record[0]);
			rdsRecDto.setDevId((Integer) record[1]);
			rdsRecDto.setOlLineItemId((Integer) record[2]);
			rdsRecDto.setApplicableTestId((Integer) record[3]);
			rdsRecDto.setSubmittedDate(record[4] != null ? sdf.format((Date) record[4]) : null);
			rdsRecDtoList.add(rdsRecDto);
		}

		rdsRecDtoList.sort(
				Comparator.comparing(RdsRecDto::getSubmittedDate, Comparator.nullsLast(Comparator.naturalOrder())));

		return rdsRecDtoList;
	}

	public List<ObservationDto> getObsByTestAndOlLineItemId(int testId, int olLineItemId, int prodId) {
		List<Observation> rdsList = observationRepository.findByApplicableTestAndOlLineItemId(testId, olLineItemId,
				prodId);
		if (rdsList.isEmpty()) {
			return Collections.emptyList();
		}
		List<ObservationDto> observationDtoList = new ArrayList<>();
		for (Observation observation : rdsList) {
			ObservationDto observationDto = new ObservationDto();
			observationDto.setId(observation.getId());
			observationDto.setOlLineItemId(observation.getOlLineItem().getId());
		//	observationDto.setApplicableTestId(observation.getLineItemApplicableTest().getId());
			observationDto.setProdId(observation.getProduct().getId());
			observationDto.setCreatedDatetime(observation.getCreatedDatetime().toString());
			observationDto.setSubmittedDate(observation.getSubmittedDate() != null
					? new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT).format(observation.getSubmittedDate())
					: null);

//				rdsDto.setDevId(rds.getDevId());
//				rdsDto.setSpecId(rds.getSpec().getId());
//				rdsDto.setObs(rds.getObs());
//				rdsDto.setRem(rds.getRemarks());
//				rdsDtoList.add(rdsDto);
		}

		return observationDtoList;
	}

	public List<ObservationDto> getSepcDetByDevIdAndLogId(Integer devId, Integer productId, Integer loginId) {
		List<Object[]> observationList = observationRepository.getSepcDetByDevIdAndLogId(devId,productId,loginId);
		if (observationList.isEmpty()) {
			return Collections.emptyList();
		}

		List<ObservationDto> observationDtoList = new ArrayList<>();
		for (Object[] obj : observationList) {
			ObservationDto dto = new ObservationDto();
			dto.setId(Integer.parseInt(obj[0].toString()));					
			dto.setOlLineItemId(Integer.parseInt(obj[4].toString()));
			dto.setSubmittedDate(obj[6] != null ? obj[6].toString() : null);
			dto.setProdId(Integer.parseInt(obj[7].toString()));			
			
			ObsRemDto obsRemDto = new ObsRemDto();
			obsRemDto.setSpecId(Integer.parseInt(obj[3].toString()));			
			obsRemDto.setObs(obj[1] != null ? obj[1].toString() : null);
			obsRemDto.setRem(obj[2] != null ? obj[2].toString() : null);
			obsRemDto.setResult(obj[11] != null ? obj[11].toString() : null);
			
			  if (obj[8] != null) {
		            try {
		                obsRemDto.setObsValue((Double) Double.parseDouble(obj[8].toString()));
		            } catch (NumberFormatException e) {
		                obsRemDto.setObsValue(null);
		            }
		        } else {
		            obsRemDto.setObsValue(null);
		        }
			  
			dto.setDevId(obj[9] != null ? (int) obj[9] : null);
			dto.setDevName(obj[10] != null ? obj[10].toString() : null);
			
			dto.setObsRemDtos(Collections.singletonList(obsRemDto)); // Wrap in a list
			
			observationDtoList.add(dto);
		}
		return observationDtoList;
	}
	
	public boolean isValidRemarks(String remarks) {
		if (remarks == null) {
			return true;
		}
		String allowedCharactersRegex = "^[a-zA-Z0-9 .,_-]+$";
		Pattern pattern = Pattern.compile(allowedCharactersRegex);
		return pattern.matcher(remarks).matches();
	}

	public List<ObservationDto> getObsByPageNo(Integer pageNo) {
		List<Observation> observationList = observationRepository.getObsByPageNo(pageNo);
		if (observationList.isEmpty()) {
			return Collections.emptyList();
		}

		List<ObservationDto> observationDtoList = new ArrayList<>();
		for (Observation observation : observationList) {
			ObservationDto dto = new ObservationDto();
			dto.setId(observation.getId());
			dto.setOlLineItemId(observation.getOlLineItem().getId());
			dto.setProdId(observation.getProduct().getId());
			dto.setCreatedDatetime(observation.getCreatedDatetime().toString());
			dto.setSubmittedDate(observation.getSubmittedDate() != null
					? new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT).format(observation.getSubmittedDate())
					: null);
			
			dto.setDevId(observation.getLineItemLotsRegister().getId());
			dto.setDevName(observation.getLineItemLotsRegister().getRegisterNo());
			dto.setPageNo(observation.getPageNo());
			dto.setFaiId(observation.getFai().getId());
			ObsRemDto obsRemDto = new ObsRemDto();
			obsRemDto.setSpecId(observation.getSpec().getId());
			obsRemDto.setObs(observation.getObs());
			obsRemDto.setRem(observation.getRemarks());
			obsRemDto.setObsValue(observation.getObsValue());
			obsRemDto.setResult(observation.getResult());
			obsRemDto.setSpecVal(observation.getSpec().getSpecVal());
			obsRemDto.setTestId(observation.getSpec().getTest().getId());
			obsRemDto.setTestName(observation.getSpec().getTest().getTestDesc());
			dto.setObsRemDtos(Collections.singletonList(obsRemDto)); 

			Integer maxPageNo = faiRepository.findMaxPageNo(observation.getFai().getId());
			dto.setMaxPageNo(maxPageNo);
			observationDtoList.add(dto);
		}
		return observationDtoList;
	}

	public List<ObservationDto> getObsByFaiId(Integer faiId, Integer pageNo) {
		List<Observation> observationList = observationRepository.getObsByFaiIdPageNo(faiId, pageNo);
		if (observationList.isEmpty()) {
			return Collections.emptyList();
		}

		List<ObservationDto> observationDtoList = new ArrayList<>();
		for (Observation observation : observationList) {
			ObservationDto dto = new ObservationDto();
			dto.setId(observation.getId());
			dto.setOlLineItemId(observation.getOlLineItem().getId());
			dto.setProdId(observation.getProduct().getId());
			dto.setCreatedDatetime(observation.getCreatedDatetime().toString());
			dto.setSubmittedDate(observation.getSubmittedDate() != null
					? new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT).format(observation.getSubmittedDate())
					: null);

			dto.setDevId(observation.getLineItemLotsRegister().getId());
			dto.setDevName(observation.getLineItemLotsRegister().getRegisterNo());
			//dto.setPageNo(observation.getPageNo());
			//dto.setFaiId(observation.getFai().getId());
			ObsRemDto obsRemDto = new ObsRemDto();
			obsRemDto.setSpecId(observation.getSpec().getId());
			obsRemDto.setObs(observation.getObs());
			obsRemDto.setRem(observation.getRemarks());
			obsRemDto.setObsValue(observation.getObsValue());
			obsRemDto.setResult(observation.getResult());
			if(observation.getResult().equalsIgnoreCase("Pass")) {
				obsRemDto.setResultId(0);
			}else {
				obsRemDto.setResultId(1);
			}
			//obsRemDto.setSpecVal(observation.getSpec().getSpecVal());
			//obsRemDto.setTestId(observation.getSpec().getTest().getId());
			//obsRemDto.setTestName(observation.getSpec().getTest().getTestDesc());
			obsRemDto.setMin(observation.getSpec().getMin());
			obsRemDto.setMax(observation.getSpec().getMax());
			dto.setObsRemDtos(Collections.singletonList(obsRemDto));
			observationDtoList.add(dto);
		}
		return observationDtoList;
	}


}
