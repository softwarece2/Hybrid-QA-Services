package in.co.bel.olms.ate.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import in.co.bel.olms.ate.constants.StringLiteralConstants;
import in.co.bel.olms.ate.dto.ObsRemDto;
import in.co.bel.olms.ate.dto.ObservationDto;
import in.co.bel.olms.ate.dto.ProductSpecMappingDto;
import in.co.bel.olms.ate.dto.RdsRecDto;
import in.co.bel.olms.ate.entity.Observation;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.repository.ObservationRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObservationService {

	private static final Logger logger = LoggerFactory.getLogger(ObservationService.class);

	private final ObservationRepository observationRepository;

	private final SpecRepository specRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final ProductsRepository productsRepository;

	private final LineItemLotsRegisterRepository  olLineItemSerialNoRepository ;

	private final Mapper mappers;

	Gson gson = new Gson();

	SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
	
	private static final String OL_LINE_ITEM_ID = StringLiteralConstants.OL_LINEITEM_ID_ERROR_STRING;
	private static final String OL_LINE_ITEM_SL_NO_ID = StringLiteralConstants.OL_LINEITEM_ID_SERIAL_NO_ERROR_STRING;
//	private static final String APPLICABLE_TEST_ID = StringLiteralConstants.APPLICABLE_TEST_ID_ERROR_STRING;
//	private static final String DEV_ID = StringLiteralConstants.DEV_ID_ERROR_STRING;

	public ResponseEntity<String> saveAteObs(@RequestBody List<ObservationDto> dtoList, Boolean isSubmit)
			throws JsonMappingException, ParseException {
		boolean isValidSpec = false;
		Set<String> devProdIdsInRequest = new HashSet<>();
		List<Observation> existingRdsForDevId = new ArrayList<>();
		List<Observation> existingRds = new ArrayList<>();
		boolean hasInvalidId = false;
		Integer prodsId = dtoList.get(0).getProdId();
	//	String devId = dtoList.get(0).getDevId();
		Integer olLineItemId = dtoList.get(0).getOlLineItemId();
	//	Integer applicableTestId = dtoList.get(0).getApplicableTestId();
		Integer olLineItemSlNoId = dtoList.get(0).getDevId();
		
	//	existingRdsForDevId = observationRepository.findRdsByDevId(devId);

//		if (existingRdsForDevId != null && !existingRdsForDevId.isEmpty()) {
//
//			if (!existingRdsForDevId.get(0).getProduct().getId().equals(dtoList.get(0).getProdId())) {
//				return ResponseEntity.badRequest().body("Error: The same product with devId "
//						+ dtoList.get(0).getDevId() + " already exists for a different prodId: "
//						+ existingRdsForDevId.get(0).getProduct().getId() + ".\n" + "       "
//						+ "You cannot save the same devId for a different prodId: " + dtoList.get(0).getProdId());
//			}
//		}
		
		String subString1 = StringLiteralConstants.OL_LINEITEM_ID_NOT_PRESENT_STRING;
		String subString2 = StringLiteralConstants.OL_LINEITEM_ID_SERIAL_NO_NOT_PRESENT_STRING;
//		String subString3 = StringLiteralConstants.APPLICABLE_TEST_NOT_PRESENT_STRING;
//		String subString4 = StringLiteralConstants.DEV_ID_ALREADY_EXIST_STRING;
		
		for (ObservationDto dto : dtoList) {
			Optional<OlLineItem> olLineItem = olLineItemRepository.findById(dto.getOlLineItemId());
//			Optional<LineItemApplicableTest> applicableTest = itemApplicableTestRepository
//					.findById(dto.getApplicableTestId());
//			
			Optional<LineItemLotsRegister> oLLineItemSerialNo = olLineItemSerialNoRepository.findById(dto.getDevId());

//			if (dto.getDevId() != null && dto.getProdId() != null && dto.getId() == 0) {
//				String devProdPair = dto.getDevId() + "-" + dto.getProdId();
//				devProdIdsInRequest.add(devProdPair);
//			}

//			if (!devId.equals(dto.getDevId())) {
//				return ResponseEntity.badRequest().body("Error: All DTOs must have the same devId in the request.");
//			}

			if (!prodsId.equals(dto.getProdId())) {
				return ResponseEntity.badRequest().body("Error: All DTOs must have the same productId in the request.");
			}
			if (!olLineItemId.equals(dto.getOlLineItemId())) {
				return ResponseEntity.badRequest()
						.body("Error: All DTOs must have the same olLineItemId in the request.");
			}
//			if (!applicableTestId.equals(dto.getApplicableTestId())) {
//				return ResponseEntity.badRequest()
//						.body("Error: All DTOs must have the same applicableTestId in the request.");
//			}
			
			if (!olLineItemSlNoId.equals(dto.getDevId())) {
				return ResponseEntity.badRequest()
						.body("Error: All DTOs must have the same olLineItemSlNoId in the request.");
			}

	//		existingRds = observationRepository.findRdsByDevId(dto.getDevId());

			if (dto.getId() == 0 && isSubmit.equals(true)) {
//				if (!existingRds.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(DEV_ID + dto.getDevId() + subString4);
//				}
				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}
//				if (applicableTest.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(APPLICABLE_TEST_ID + dto.getApplicableTestId() + subString3);
//				}
				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId() + subString2);
				}
			}

			if (dto.getId() != 0 && isSubmit.equals(true)) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}
//				if (applicableTest.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(APPLICABLE_TEST_ID + dto.getApplicableTestId() + subString3);
//				}
				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId() + subString2);
				}
			}

			if (dto.getId() == 0 && isSubmit.equals(false)) {

//				if (!existingRds.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(DEV_ID + dto.getDevId() + subString4);
//				}

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}
//				if (applicableTest.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(APPLICABLE_TEST_ID + dto.getApplicableTestId() + subString3);
//				}
			} else if (dto.getId() != 0) {

				if (olLineItem.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_ID + dto.getOlLineItemId() + subString1);
				}
//				if (applicableTest.isEmpty()) {
//					return ResponseEntity.badRequest()
//						.body(APPLICABLE_TEST_ID + dto.getApplicableTestId() + subString3);
//				}
				if (oLLineItemSerialNo.isEmpty()) {
					return ResponseEntity.badRequest()
						.body(OL_LINE_ITEM_SL_NO_ID + dto.getApplicableTestId() + subString2);
				}
			}

			ProductSpecMappingDto specMappingDto = new ProductSpecMappingDto();
			specMappingDto.setProductId(dto.getProdId());
			specMappingDto.setSpecId(dto.getObsRemDtos().get(0).getSpecId());

//			if (dto.getProdId() == 22 || dto.getProdId() == 32 || dto.getProdId() == 35 || dto.getProdId() == 37) {
//				isValidSpec = validateProductDistSpecMapping(specMappingDto);
//				if (!isValidSpec) {
//					return ResponseEntity.badRequest().body("Error: Product " + dto.getProdId()
//							+ " is not mapped with Spec " + dto.getObsRemDtos().get(0).getSpecId());
//				}
//			} else {
//				isValidSpec = validateProductSpecMapping(specMappingDto);
//				if (!isValidSpec) {
//					return ResponseEntity.badRequest().body("Error: Product " + dto.getProdId()
//							+ " is not mapped with Spec " + dto.getObsRemDtos().get(0).getSpecId());
//				}
//			}
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
			List<ObservationDto> savedRdsDtos = new ArrayList<>();
			if (hasInvalidId = dtoList.stream().anyMatch(dto -> dto.getId() == 0)) {
				for (ObservationDto dto : dtoList) {
					Observation observation = new Observation();
					observation.setCreatedDatetime(new Date());
					observation.setOlLineItem(olLineItemRepository.findById(dto.getOlLineItemId()).orElseThrow());
//					observation.setLineItemApplicableTest(
//							itemApplicableTestRepository.findById(dto.getApplicableTestId()).orElseThrow());
					observation.setProduct(productsRepository.findById(dto.getProdId()).orElseThrow());
					observation.setLineItemLotsRegister(olLineItemSerialNoRepository.findById(dto.getDevId()).orElseThrow());
					
					if (dto.getSubmittedDate() != null) {
						observation.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
					} else {
						observation.setSubmittedDate(null);
					}
					Optional<Spec> specOptional = specRepository.findById(dto.getObsRemDtos().get(0).getSpecId());
					if (specOptional.isEmpty()) {
						return ResponseEntity.badRequest()
								.body("Spec ID " + dto.getObsRemDtos().get(0).getSpecId() + " not found.");
					}
					observation.setSpec(specOptional.get());
					observation.setObs(dto.getObsRemDtos().get(0).getObs());
					observation.setObsValue(dto.getObsRemDtos().get(0).getObsValue());
					
					//observation.setRemarks(dto.getObsRemDtos().get(0).getRem());

					if (isValidRemarks(dto.getObsRemDtos().get(0).getRem())) {
						observation.setRemarks(dto.getObsRemDtos().get(0).getRem());
					} else {
						return ResponseEntity.badRequest().body("Please enter remarks without special characters");
					}
					if (dto.getObsRemDtos().get(0).getResultId() != null && dto.getObsRemDtos().get(0).getResultId() == 0) {
						observation.setResult("Pass");
					} else {
						observation.setResult("Fail");
					}
				//	observation.setDevId(dto.getDevId());
					Observation newEntity = observationRepository.save(observation);

					ObservationDto rdsDtos = mappers.map(newEntity, ObservationDto.class);
					List<ObsRemDto> lObsRemDtos = new ArrayList<>();
					ObsRemDto obsRemDto = new ObsRemDto();
					obsRemDto.setSpecId(newEntity.getSpec().getId());
					obsRemDto.setObs(newEntity.getObs());
					obsRemDto.setObsValue(newEntity.getObsValue());
					obsRemDto.setRem(newEntity.getRemarks());
					obsRemDto.setResult(newEntity.getResult());
					lObsRemDtos.add(obsRemDto);
					rdsDtos.setObsRemDtos(lObsRemDtos);
					savedRdsDtos.add(rdsDtos);
				}
			} else {

				for (ObservationDto dto : dtoList) {
					if (dto.getId() != 0) {
						Optional<Observation> existingEntityOptional = observationRepository.findById(dto.getId());
						if (existingEntityOptional.isPresent()) {
							Observation existingEntity = existingEntityOptional.get();

						//	existingEntity.setDevId(dto.getDevId());
							
							
							existingEntity.setCreatedDatetime(existingEntity.getCreatedDatetime());
							if (dto.getSubmittedDate() != null) {
								existingEntity.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
							}
							if (existingEntityOptional.get().getCreatedDatetime() != null) {
								dto.setCreatedDatetime(existingEntityOptional.get().getCreatedDatetime().toString());
							}

							List<ObsRemDto> updatedObsRemDtos = new ArrayList<>();

							for (ObsRemDto obsRemDto : dto.getObsRemDtos()) {
								Optional<Spec> specOptional = specRepository.findById(obsRemDto.getSpecId());
								if (specOptional.isPresent()) {
									Spec spec = specOptional.get();

									Optional<Observation> existingRdsOptional = observationRepository
											.findBySpecIdAndDevId(spec.getId(), existingEntity.getLineItemLotsRegister().getId());
									Observation updatedEntity;
									if (existingRdsOptional.isPresent()) {
										updatedEntity = existingRdsOptional.get();
									} else {
										updatedEntity = new Observation();
										updatedEntity.setLineItemLotsRegister(existingEntity.getLineItemLotsRegister());
										if (existingEntity.getCreatedDatetime() != null) {
											updatedEntity.setCreatedDatetime(existingEntity.getCreatedDatetime());
										} else {
											updatedEntity.setCreatedDatetime(new Date());
										}
										updatedEntity.setSpec(spec);
									}

									updatedEntity.setObs(obsRemDto.getObs());
									updatedEntity.setObsValue(obsRemDto.getObsValue());
								//	updatedEntity.setRemarks(obsRemDto.getRem());
									
									if (dto.getObsRemDtos().get(0).getResultId() != null && dto.getObsRemDtos().get(0).getResultId() == 0) {
										updatedEntity.setResult("Pass");
									} else {
										updatedEntity.setResult("Fail");
									}
									
									
									if (isValidRemarks(dto.getObsRemDtos().get(0).getRem())) {
										updatedEntity.setRemarks(dto.getObsRemDtos().get(0).getRem());
									} else {
										return ResponseEntity.badRequest().body("Please enter remarks without special characters");
									}

									if (dto.getSubmittedDate() != null) {
										updatedEntity.setSubmittedDate(sdf.parse(dto.getSubmittedDate()));
									}
									updatedEntity = observationRepository.save(updatedEntity);

									ObsRemDto savedObsRemDto = new ObsRemDto();
									savedObsRemDto.setSpecId(spec.getId());
									savedObsRemDto.setObs(updatedEntity.getObs());
									savedObsRemDto.setRem(updatedEntity.getRemarks());
									savedObsRemDto.setObsValue(updatedEntity.getObsValue());
									savedObsRemDto.setResult(updatedEntity.getResult());

									updatedObsRemDtos.add(savedObsRemDto);
								} else {
									return ResponseEntity.badRequest()
											.body("Spec ID " + obsRemDto.getSpecId() + " not found.");
								}
							}
							dto.setObsRemDtos(updatedObsRemDtos);
							savedRdsDtos.add(dto);
						}
					}
				}
			}
			return ResponseEntity.ok(gson.toJson(savedRdsDtos));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving RDS records." + e.getMessage());

		}
	}

	public ResponseEntity<String> submitAteObs(List<ObservationDto> dtoList, Boolean isSubmit)
			throws JsonMappingException, ParseException {
		ResponseEntity<String> res = saveAteObs(dtoList, isSubmit);
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
		            //Try parsing to Double, if fails, use null
		            try {
		                obsRemDto.setObsValue((Double) Double.parseDouble(obj[8].toString()));
		            } catch (NumberFormatException e) {
		                obsRemDto.setObsValue(null);
		            }
		        } else {
		            obsRemDto.setObsValue(null);
		            //Or set to 0.0, will depend on business requirement
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

}
