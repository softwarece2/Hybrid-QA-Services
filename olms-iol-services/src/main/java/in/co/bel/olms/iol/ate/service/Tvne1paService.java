package in.co.bel.olms.iol.ate.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import in.co.bel.olms.iol.ate.dto.AteGraphDataDto;
import in.co.bel.olms.iol.ate.dto.InspTestMappingDto;
import in.co.bel.olms.iol.ate.dto.InspectionRoundDto;
import in.co.bel.olms.iol.ate.dto.ProdAteDto;
import in.co.bel.olms.iol.ate.dto.ProdDto;
import in.co.bel.olms.iol.ate.dto.TestInfoDto;
import in.co.bel.olms.iol.ate.dto.TestValDto;
import in.co.bel.olms.iol.ate.entity.AteTestReportUpload;
import in.co.bel.olms.iol.ate.entity.Tvne1pa;
import in.co.bel.olms.iol.ate.repository.Tvne1paRepository;
import in.co.bel.olms.iol.constants.ApplicationConstants;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.entity.InspTestMapping;
import in.co.bel.olms.iol.repository.InspTestMappingRepository;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tvne1paService {
	
	private static final Logger logger = LoggerFactory.getLogger(Tvne1paService.class);

	private final Mapper mapper;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final SpecRepository specRepository;

	private final Tvne1paRepository tvne1paRepository;

	private final TestRepository testRepository;

	private final ProductMappingRepository productMappingRepository;

	private final OlStatusRepository olStatusRepository;

	private final InspTestMappingRepository inspTestMappingRepository;

	private final InspectionRoundService inspectionRoundService;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final AteReportUploadService ateUploadService;

	private final OLLineItemSerialNoRepository olLineItemSerialNoRepository;

	private final OlLineItemRepository olLineItemRepo;

	private final OfferLetterRepository olRepo;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final SubSlipLineItemRepository subSlipLineItemRepository;

	Gson gson = new Gson();

	@Transactional
	public ResponseEntity<String> saveTvne1pa(@Valid InspectionRoundDto dto) {

		InspectionRound savedInspectionRound = null;

		Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(dto.getId());
		if (inspectionRoundOptional.isPresent()) {

			savedInspectionRound = inspectionRoundOptional.get();
			savedInspectionRound = inspectionRoundService.populateInspectionRound(dto, true, savedInspectionRound);
			savedInspectionRound = inspectionRoundRepository.save(savedInspectionRound);
		} else {
			throw new DataNotFoundException("Save : Inspection Id not found: " + dto.getId());
		}

		List<Tvne1pa> tvne1pas = new ArrayList<>();
		for (ProdAteDto tvne1paDto : dto.getProdAteDtos()) {
			Tvne1pa tvne1pa = mapper.map(tvne1paDto, Tvne1pa.class);
			tvne1pa.setInspectionRound(savedInspectionRound);
			Optional<Spec> specOptional = specRepository.findById(tvne1paDto.getSpecId());
			if (specOptional.isPresent()) {
				tvne1pa.setSpec(specOptional.get());
			} else {
				throw new DataNotFoundException("Save : Spec Id not found: " + tvne1paDto.getSpecId());
			}

			Optional<Test> testOptional = testRepository.findById(tvne1paDto.getTestId());
			if (testOptional.isPresent()) {
				tvne1pa.setTest(testOptional.get());
			} else {
				throw new DataNotFoundException("Save : Test Id not found: " + tvne1paDto.getTestId());
			}
			tvne1pas.add(tvne1pa);
		}

		List<Tvne1pa> savedTvne1pa = tvne1paRepository.saveAll(tvne1pas);

		InspectionRoundDto savedDto = mapper.map(savedInspectionRound, InspectionRoundDto.class);
		savedDto.setProdAteDtos(savedTvne1pa.stream().map(tvne1pa1 -> mapper.map(tvne1pa1, ProdAteDto.class))
				.collect(Collectors.toList()));

		return ResponseEntity.ok(gson.toJson(savedDto));
	}

	@Transactional
	public InspectionRoundDto submitTvne1pa(InspectionRoundDto dto) {
	    try {
	        InspectionRound savedInspectionRound = null;
	        String testStatus = ApplicationConstants.FAIL;
	        Boolean isQa = null;

	        // 1. Determine Test Status
	        if (ApplicationConstants.RFR_ENABLE == 1) {
	            List<ProdAteDto> dtos = dto.getProdAteDtos();
	            if (dtos == null || dtos.isEmpty()) {
	                throw new IllegalArgumentException("ProdAteDtos list is empty");
	            }
	            
	            boolean hasFail = dtos.stream().anyMatch(d -> d.getStatus().equalsIgnoreCase(ApplicationConstants.FAIL));
	            boolean hasPass = dtos.stream().anyMatch(d -> d.getStatus().equalsIgnoreCase(ApplicationConstants.PASS));

	            if (hasFail) {
	                testStatus = ApplicationConstants.FAIL;
	            } else if (hasPass) {
	                testStatus = ApplicationConstants.PASS;
	            } else {
	                throw new IllegalArgumentException("No pass or fail status found in ProdAteDtos");
	            }
	        }

	        // 2. Process Inspection Round
	        InspectionRound inspectionRound = inspectionRoundRepository.findById(dto.getId())
	                .orElseThrow(() -> new IllegalArgumentException("Save : InspRound Id not found: " + dto.getId()));
	        
	        dto.setCreatedDatetime(inspectionRound.getCreatedDatetime());
	        savedInspectionRound = inspectionRoundService.populateInspectionRound(dto, true, inspectionRound);
	        savedInspectionRound.setSubmittedDate(new Date());
	        savedInspectionRound.setStatus(testStatus);
	        savedInspectionRound.setIsQa(dto.getIsQa());
	        isQa = inspectionRound.getIsQa();
	        inspectionRoundRepository.save(savedInspectionRound);

	        // 3. Process Test Mapping

	            InspTestMappingDto inspTestDto = new InspTestMappingDto();
	            inspTestDto.setInspId(savedInspectionRound.getId());
	            inspTestDto.setTestId(dto.getProdAteDtos().get(0).getTestId());
	            inspTestDto.setSubmittedById(dto.getSubmittedById());

	            Optional<OlStatus> olstatus = olStatusRepository.findStatus(testStatus.toLowerCase());
	            if (olstatus.isPresent()) {
	                inspTestDto.setTestStatusId(olstatus.get().getId());
	            } else {
	                throw new IllegalStateException("Status mapping not found for: " + testStatus);
	            }

	            InspTestMapping inspTestMapping = mapper.map(inspTestDto, InspTestMapping.class);
	            inspTestMapping.setSubmittedDate(new Date());
	            inspTestMappingRepository.save(inspTestMapping);


	        // 4. Update Device/Serial Status
	        if (dto.getIsQa() == null || !dto.getIsQa()) {
	            SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findById(dto.getDevId())
	                    .orElseThrow(() -> new IllegalArgumentException("Status : Dev Id not found: " + dto.getDevId()));
	            String devStatus = getDeviceStatus(dto, null);
	            subSlipSerialNo.setFinalStatus(devStatus);
	            subSlipSerialNoRepository.save(subSlipSerialNo);
	        } else {
	            OLLineItemSerialNo oLLineItemSerialNo = olLineItemSerialNoRepository.findById(dto.getDevId())
	                    .orElseThrow(() -> new IllegalArgumentException("Status : Dev Id not found: " + dto.getDevId()));
	            String devStatus = getDeviceStatus(dto, oLLineItemSerialNo.getIsFaiSample());
	            oLLineItemSerialNo.setStatus(devStatus);
	            olLineItemSerialNoRepository.save(oLLineItemSerialNo);
	        }

	        // 5. Process TVNE1PA records
	        List<Tvne1pa> tvne1pas = new ArrayList<>();
	        for (ProdAteDto tvne1paDto : dto.getProdAteDtos()) {
	            Spec spec = specRepository.findById(tvne1paDto.getSpecId())
	                    .orElseThrow(() -> new IllegalArgumentException("Save : Spec Id not found: " + tvne1paDto.getSpecId()));
	            
	            Tvne1pa tvne1pa = new Tvne1pa();
	            if (tvne1paDto.getId() != null && tvne1paDto.getId() != 0) {
	                tvne1pa = tvne1paRepository.findById(tvne1paDto.getId())
	                        .orElseThrow(() -> new IllegalArgumentException("Tvne1pa record not found for id: " + tvne1paDto.getId()));
	            }
	            mapper.map(tvne1paDto, tvne1pa);
	            tvne1pa.setInspectionRound(savedInspectionRound);
	            tvne1pa.setSpec(spec);
	            tvne1pas.add(tvne1pa);
	        }
	        List<Tvne1pa> savedTvne1pa = tvne1paRepository.saveAll(tvne1pas);


//	        if (ApplicationConstants.RFR_ENABLE == 1) {
//	            if (Boolean.TRUE.equals(isQa)) {
//	                // updateQaStatusForQa logic here
//	            } else {
//	                // updateStatusForNonQa logic here
//	            }
//	        }

	        // 6. Update Offer Letter / Submission Slip Status
	        if (dto.getOlLineItemId() != null) {
	            OlLineItem olLi = olLineItemRepo.findById(dto.getOlLineItemId())
	                    .orElseThrow(() -> new IllegalArgumentException("Ol line item not found"));
	            olRepo.updateStatus(olLi.getOfferLetter().getId(), 5);
	        }

	        if (dto.getSubSlipLineItemId() != null) {
	            SubSlipLineItem sli = subSlipLineItemRepository.findById(dto.getSubSlipLineItemId())
	                    .orElseThrow(() -> new IllegalArgumentException("SubSlip line item not found"));
	            submissionSlipRepository.updateStatus(sli.getSubmissionSlip().getId(), 5);
	        }

	        InspectionRoundDto savedDto = mapper.map(savedInspectionRound, InspectionRoundDto.class);
	        savedDto.setProdAteDtos(savedTvne1pa.stream()
	                .map(tvne1pa -> mapper.map(tvne1pa, ProdAteDto.class))
	                .collect(Collectors.toList()));

	        return savedDto;

	    } catch (Exception e) {
            // preserve stack trace and triggers ROLLBACK
            throw new RuntimeException(e.getMessage(), e);
        }
	  }

	public InspectionRoundDto getTvne1paByDevId(Integer devId, Integer prodId, Integer testId, Integer roundId,
                                              Boolean isQa, Boolean qa, Integer olLiId) {
		List<ProdAteDto> prodAteDtos = new ArrayList<>();
		InspectionRoundDto dto = new InspectionRoundDto();
		List<Tvne1pa> tvne1paList;

		if (isQa != null && isQa || (qa != null && qa)) {
			if (qa != null && qa) {
				tvne1paList = tvne1paRepository.getByDevIdProdTestRndIsQa1(devId, prodId, testId, roundId, qa, olLiId);
			} else {
				tvne1paList = tvne1paRepository.getByDevIdProdTestRndIsQa1(devId, prodId, testId, roundId, isQa, olLiId);
			}
		} else {
			tvne1paList = tvne1paRepository.getByDevIdProdTestRndIsQa(devId, prodId, testId, roundId, isQa);
		}

		if (tvne1paList.isEmpty()) {
			List<Spec> specValueDtoList = specRepository.getActiveSpecsByProdIdTestId(prodId, testId);

			for (Spec specbo : specValueDtoList) {
				ProdAteDto proddto = new ProdAteDto();
				proddto.setId(0);
				proddto.setSpecId(specbo.getId());
				proddto.setSpecVal(specbo.getSpecVal());
				proddto.setMin(specbo.getMin());
				proddto.setMax(specbo.getMax());
				proddto.setParamType(specbo.getParamType() != null ? specbo.getParamType().getParamType() : null);
				proddto.setTyClauseNo(specbo.getTyClauseNo());
				if (specbo.getObsType() == null) {
					proddto.setObsTypeId(null);
					proddto.setObsType(null);
				} else {
					proddto.setObsTypeId(specbo.getObsType().getId());
					proddto.setObsType(specbo.getObsType().getType());
				}
				proddto.setObsValue(specbo.getObsValue());

				try {
					proddto.setTestType(specbo.getTestType().getTestType());
				} catch (Exception e) {
					proddto.setTestType("N/A");
				}

				try {
					proddto.setMainType(specbo.getMainType().getMainType());
				} catch (Exception e) {
					proddto.setMainType("N/A");
				}

				try {
					proddto.setUnit(specbo.getUnits().getUints());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				List<AteTestReportUpload> uploadedReports = ateUploadService.getAllUploadedReports(
	                    devId, prodId, testId, roundId, specbo.getId(), isQa, olLiId);
	            
	            for (AteTestReportUpload report : uploadedReports) {
	                ateUploadService.addFileToDto(report, proddto);
	            }

				prodAteDtos.add(proddto);
			}

		} else {
			for (Tvne1pa bo : tvne1paList) {
				ProdAteDto proddto = new ProdAteDto();
				List<AteTestReportUpload> uploadedReports = ateUploadService.getAllUploadedReports(
		                devId, prodId, testId, roundId, bo.getSpec().getId(), isQa, olLiId);

		        // 2. If reports exist, attach each one as a file detail entry
		        if (uploadedReports != null && !uploadedReports.isEmpty()) {
		            for (AteTestReportUpload report : uploadedReports) {
		                // Use the helper method to populate the FileDetailDto inside the proddto
		                ateUploadService.addFileToDto(report, proddto);
		            }
		        }
				proddto.setId(bo.getId());
				proddto.setSpecId(bo.getSpec().getId());
				if (bo.getSpec().getObsType() == null) {
					proddto.setObsTypeId(null);
				} else {
					proddto.setObsTypeId(bo.getSpec().getObsType().getId());
				}
				Spec specbo = bo.getSpec();
				proddto.setSpecId(specbo.getId());
				proddto.setSpecVal(specbo.getSpecVal());
				proddto.setMin(specbo.getMin());
				proddto.setMax(specbo.getMax());
				if (specbo.getParamType() != null)
					proddto.setParamType(specbo.getParamType().getParamType());
				proddto.setTyClauseNo(specbo.getTyClauseNo());
				if (specbo.getObsType() == null) {
					proddto.setObsTypeId(null);
					proddto.setObsType(null);
				} else {
					proddto.setObsTypeId(specbo.getObsType().getId());
					proddto.setObsType(specbo.getObsType().getType());
				}

				proddto.setObsValue(specbo.getObsValue());
				proddto.setRemarks(bo.getRemarks());
				proddto.setObsValTx(bo.getObsValue());

				try {
					proddto.setTestType(specbo.getTestType().getTestType());
				} catch (Exception e) {
					proddto.setTestType("N/A");
				}

				try {
					proddto.setMainType(specbo.getMainType().getMainType());
				} catch (Exception e) {
					proddto.setMainType("N/A");
				}

				try {
					proddto.setStatus(bo.getStatus());
				} catch (Exception e) {
					proddto.setStatus("N/A");
				}

				try {
					proddto.setUnit(specbo.getUnits().getUints());
				} catch (Exception e) {
					proddto.setUnit(null);
				}
				proddto.setActiveStatus(bo.getActiveStatus());
				prodAteDtos.add(proddto);
			}

			Tvne1pa obj = tvne1paList.get(0);
			Integer inspId = obj.getInspectionRound().getId();
			Integer testId2 = obj.getTest().getId();

			InspTestMapping inspTestMapping = inspTestMappingRepository.findByInspTestId(inspId, testId2);

			if (inspTestMapping != null) {
				try {
					dto.setSubmittedDate(inspTestMapping.getSubmittedDate());
					dto.setSubmittedBy(inspTestMapping.getSubmittedBy().getEname());

				} catch (Exception e) {
					logger.error("Error in fetching the submitted details {}", e.getMessage());
				}
			}
		}
		dto.setProdAteDtos(prodAteDtos);
		return dto;
	}
	public List<AteGraphDataDto> getGraphDataV2(Integer specId, Date from, Date to,BigDecimal cpk,BigDecimal cp,BigDecimal mean, BigDecimal stdDev,BigDecimal median) {
		List<AteGraphDataDto> ateGraphDataDtos = new ArrayList<>();
		
		
		List<Object[]> tvne1paGraphData = tvne1paRepository.getGraphData(specId, from, to);
		for (Object[] obj : tvne1paGraphData) {
			AteGraphDataDto ateGraphDataDto = new AteGraphDataDto();
			ateGraphDataDto.setRegNol(obj[0] != null ? obj[0].toString() : null);
			
			//if obs type id is 1 and 2
			ateGraphDataDto.setObsValue(obj[1] != null ? obj[1].toString() : null);

			if(Integer.parseInt(obj[3].toString()) ==1 || Integer.parseInt(obj[3].toString()) ==2) {
				ateGraphDataDto.setValue(obj[1] != null && !obj[1].toString().trim().isEmpty() ? Double.parseDouble(obj[1].toString()) : null);
				
			}
			if(Integer.parseInt(obj[3].toString()) ==1 || Integer.parseInt(obj[3].toString()) ==2) {
				ateGraphDataDto.setValue2(obj[1] != null && !obj[1].toString().trim().isEmpty() ? new BigDecimal(obj[1].toString()) : null);				
			}			
			ateGraphDataDto.setStatus(obj[2] != null ? obj[2].toString() : null);
			ateGraphDataDto.setCpk(cpk);
			ateGraphDataDto.setCp(cp);
			ateGraphDataDto.setMean(mean);
			ateGraphDataDto.setStdDev(stdDev);
			ateGraphDataDto.setMedian(median);
			ateGraphDataDtos.add(ateGraphDataDto);
		}

		return ateGraphDataDtos;
	}
	public List<AteGraphDataDto> getGraphData(Integer specId, Date from, Date to) {
		List<AteGraphDataDto> ateGraphDataDtos = new ArrayList<>();
		
		
		List<Object[]> tvne1paGraphData = tvne1paRepository.getGraphData(specId, from, to);
		for (Object[] obj : tvne1paGraphData) {
			AteGraphDataDto ateGraphDataDto = new AteGraphDataDto();
			ateGraphDataDto.setRegNol(obj[0] != null ? obj[0].toString() : null);
			
			//if obs type id is 1 and 2
			ateGraphDataDto.setObsValue(obj[1] != null ? obj[1].toString() : null);

			if(Integer.parseInt(obj[3].toString()) ==1 || Integer.parseInt(obj[3].toString()) ==2) {
				ateGraphDataDto.setValue(obj[1] != null && !obj[1].toString().trim().isEmpty() ? Double.parseDouble(obj[1].toString()) : null);				
			}
			
			if(Integer.parseInt(obj[3].toString()) ==1 || Integer.parseInt(obj[3].toString()) ==2) {
				ateGraphDataDto.setValue2(obj[1] != null && !obj[1].toString().trim().isEmpty() ? new BigDecimal(obj[1].toString()) : null);				
			}

			
			ateGraphDataDto.setStatus(obj[2] != null ? obj[2].toString() : null);
			
			ateGraphDataDtos.add(ateGraphDataDto);
		}

		return ateGraphDataDtos;
	}

	public List<TestInfoDto> getTestInfo(Integer prodId, Integer devId) {

		List<Integer> testIdList = productMappingRepository.getTestIdsByProdId(prodId);
		List<TestInfoDto> dtos = new ArrayList<>();
		for (Integer testId : testIdList) {
			List<Object[]> testInfoList = tvne1paRepository.getTestInfo(prodId, devId, testId);
			TestInfoDto dto = new TestInfoDto();
//          if (testInfoList != null && !testInfoList.isEmpty()) {
			dto = getTestDto(testInfoList, testId);
			dtos.add(dto);
//          } else {
//
//              TestInfoDto emptyDto = new TestInfoDto();
//              emptyDto.setStatus("");
//              dtos.add(emptyDto);
//          }
		}
		return dtos;
	}

	private TestInfoDto getTestDto(List<Object[]> testInfoList, Integer testId) {
		TestInfoDto dto = new TestInfoDto();
		dto.setTestId(testId);

		if (testInfoList != null && !testInfoList.isEmpty()) {
			Object[] data = testInfoList.get(0); // Use the first element for date and ID
			String dateString = data[0].toString();
			String submittedById = data[1].toString();

			SimpleDateFormat inputFormat = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
			SimpleDateFormat outputFormat = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);

			try {
				Date date = inputFormat.parse(dateString);
				String formattedDate = outputFormat.format(date);
				dto.setSubmittedDate(formattedDate);
			} catch (ParseException e) {
				logger.error("Error parsing date: {}", e.getMessage());
				dto.setSubmittedDate(null); // Handle parsing errors
			}

			dto.setSubmittedById(submittedById);

			// Determine overall status
			List<String> statuses = testInfoList.stream().map(arr -> (String) arr[2]) // Assuming status is at index 2
					.collect(Collectors.toList());

			boolean hasFail = statuses.stream().anyMatch("Fail"::equalsIgnoreCase);

			if (hasFail) {
				dto.setStatus("Fail");
			} else if (statuses.isEmpty()) {
				dto.setStatus("");
			} else {
				dto.setStatus("Pass");
			}

		} else {
			// Handle the case where testInfoList is empty
			dto.setSubmittedDate(null);
			dto.setSubmittedById(null);
			dto.setStatus("");
		}

		return dto;
	}

	public ProdDto getAllTestValByProdIdDevId(Integer devId, Integer prodId, Integer roundId, Boolean isQa) {

		List<Object[]> prodDet = null;
		if (isQa != null && isQa) {
			prodDet = tvne1paRepository.getAllTestListByPrdIdIsQa(devId, prodId, roundId);
		} else {
			prodDet = tvne1paRepository.getAllTestListByPrdId(devId, prodId, roundId);
		}
		ProdDto prodDto = new ProdDto();
		List<TestValDto> testDtos = new ArrayList<>();
		for (Object[] obj : prodDet) {
			TestValDto testDto = new TestValDto();
			prodDto.setInspId((obj[0] != null) ? Integer.parseInt(obj[0].toString()) : null);
			prodDto.setRoundId((obj[1] != null) ? Integer.parseInt(obj[1].toString()) : null);
			prodDto.setProdId((obj[2] != null) ? Integer.parseInt(obj[2].toString()) : null);
			testDto.setTestId((obj[3] != null) ? Integer.parseInt(obj[3].toString()) : null);
			testDto.setTestName((obj[4] != null) ? obj[4].toString() : null);
			testDto.setSpecId((obj[5] != null) ? Integer.parseInt(obj[5].toString()) : null);
			testDto.setSpecVal((obj[6] != null) ? obj[6].toString() : null);
			testDto.setMin((obj[7] != null) ? obj[7].toString() : null);
			testDto.setMax((obj[8] != null) ? obj[8].toString() : null);
			testDto.setObsValue((obj[9] != null) ? (obj[9].toString()) : null);
			testDto.setObsTypeId((obj[10] != null) ? Integer.parseInt(obj[10].toString()) : null);
			testDto.setObservation((obj[11] != null) ? obj[11].toString() : null);
			testDto.setStatus((obj[12] != null) ? obj[12].toString() : null);
			testDto.setParamCode((obj[13] != null) ? obj[13].toString() : null);
			testDto.setParamTypeId((obj[14] != null) ? Integer.parseInt(obj[14].toString()) : null);
			testDto.setTyClauseNo((obj[15] != null) ? obj[15].toString() : null);
			testDto.setParamType((obj[16] != null) ? obj[16].toString() : null);
			testDto.setUnit((obj[17] != null) ? obj[17].toString() : null);
			testDto.setSubmittedBy(obj[18] != null ? obj[18].toString() : null);

			testDtos.add(testDto);
		}
		prodDto.setTestValDto(testDtos);
		return prodDto;
	}

	private String getDeviceStatus(InspectionRoundDto dto, Boolean isFaiSample) {
		String statusFinal = ApplicationConstants.INPROCESS;
		Integer productId = dto.getProductId();
		Integer devId = dto.getDevId();
		Boolean isQa = dto.getIsQa();
		Integer olLiId = dto.getOlLineItemId();

		logger.debug("olLiId: {}", olLiId);

		try {
			List<Integer> requiredTestIds = Boolean.TRUE.equals(isQa) && Boolean.TRUE.equals(isFaiSample)
					? inspectionRoundRepository.getRequiredTestIdsByFai(productId)
					: productMappingRepository.getTestIdsByProdId(productId);

			List<Integer> performedTestIds = Boolean.TRUE.equals(isQa)
          ? inspectionRoundRepository.findTestListByDevIdQa(devId, olLiId)
					: inspectionRoundRepository.findTestListByDevId(devId);

			logger.debug("requiredTestIds: {}", requiredTestIds);
			logger.debug("performedTestIds: {}", performedTestIds);
			boolean setsAreEqual = new HashSet<>(requiredTestIds).equals(new HashSet<>(performedTestIds));

			List<InspTestMapping> inspTestMappings = isQa
          ? inspTestMappingRepository.findByIdsInQa(performedTestIds, devId, olLiId)
					: inspTestMappingRepository.findByIdsIn(performedTestIds, devId);

			Set<Integer> statusIds = inspTestMappings.stream().map(m -> m.getTestStatusId().getId())
					.collect(Collectors.toSet());

			Map<Integer, String> statusMap = olStatusRepository.findAllById(statusIds).stream()
					.collect(Collectors.toMap(OlStatus::getId, OlStatus::getStatus));

			List<String> statuses = inspTestMappings.stream().map(m -> statusMap.get(m.getTestStatusId().getId()))
					.filter(Objects::nonNull).collect(Collectors.toList());

			if (!setsAreEqual) {
				boolean hasFail = statuses.stream().anyMatch(s -> s.equalsIgnoreCase(ApplicationConstants.FAIL));
				statusFinal = hasFail ? ApplicationConstants.FAIL : ApplicationConstants.INPROCESS;
			} else {
				boolean allPass = statuses.stream().allMatch(s -> s.equalsIgnoreCase(ApplicationConstants.PASS));
				statusFinal = allPass ? ApplicationConstants.PASS : ApplicationConstants.FAIL;
			}

		} catch (Exception e) {
			logger.error("Error calculating device status for DevId:{} = {} ", devId, e.getMessage());
		}
		return statusFinal;
	}
}
