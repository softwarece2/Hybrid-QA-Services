package in.co.bel.olms.iol.ate.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import in.co.bel.olms.iol.ate.entity.Tpdk1;
import in.co.bel.olms.iol.ate.repository.Tpdk1Repository;
import in.co.bel.olms.iol.constants.ApplicationConstants;
import in.co.bel.olms.iol.entity.InspTestMapping;
import in.co.bel.olms.iol.repository.InspTestMappingRepository;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Tpdk1Service {
	
	private static final Logger logger = LoggerFactory.getLogger(Tpdk1Service.class);

	private final Mapper mapper;

	private final InspectionRoundService inspectionRoundService;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final SpecRepository specRepository;

	private final Tpdk1Repository tpdk1Repository;

	private final TestRepository testRepository;

	private final ProductMappingRepository productMappingRepository;

	private final OlStatusRepository olStatusRepository ;

	private final InspTestMappingRepository inspTestMappingRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final AteReportUploadService ateUploadService;

	private final OLLineItemSerialNoRepository olLineItemSerialNoRepository;
	
	Gson gson = new Gson();

	public ResponseEntity<String> saveTpdk1(@Valid InspectionRoundDto dto) {
		
		InspectionRound savedInspectionRound = null;

			Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(dto.getId());
			if (inspectionRoundOptional.isPresent()) {
				
				savedInspectionRound = inspectionRoundService.populateInspectionRound(dto, true, savedInspectionRound);
				savedInspectionRound = inspectionRoundOptional.get();
				savedInspectionRound = inspectionRoundRepository.save(savedInspectionRound);
			} else {
				throw new DataNotFoundException("Save : Inspection Id not found: " + dto.getId());
			}
	
		List<Tpdk1> tpdk1s = new ArrayList<>();
		for (ProdAteDto tpdk1Dto : dto.getProdAteDtos()) {
			Tpdk1 tpdk1 = mapper.map(tpdk1Dto, Tpdk1.class);
			tpdk1.setInspectionRound(savedInspectionRound);
			Optional<Spec> specOptional = specRepository.findById(tpdk1Dto.getSpecId());
			if (specOptional.isPresent()) {
				tpdk1.setSpec(specOptional.get());
			} else {
				throw new DataNotFoundException("Save : Spec Id not found: " + tpdk1Dto.getSpecId());
			}
			
			Optional<Test> testOptional= testRepository.findById(tpdk1Dto.getTestId());
			if (testOptional.isPresent()) {
				tpdk1.setTest(testOptional.get());
			} else {
				throw new DataNotFoundException("Save : Test Id not found: " + tpdk1Dto.getTestId());
			}
			tpdk1s.add(tpdk1);
		}

		List<Tpdk1> savedTpdk1 = tpdk1Repository.saveAll(tpdk1s);

		InspectionRoundDto savedDto = mapper.map(savedInspectionRound, InspectionRoundDto.class);
		savedDto.setProdAteDtos(savedTpdk1.stream()
				.map(tpdk1 -> mapper.map(tpdk1, ProdAteDto.class)).collect(Collectors.toList()));

		return ResponseEntity.ok(gson.toJson(savedDto));
	}

	public ResponseEntity<String> submitTpdk1(InspectionRoundDto dto) {
		InspectionRound savedInspectionRound = null;

		String devSts="fail";
		Boolean isQa = null ;
		if (ApplicationConstants.RFR_ENABLE == 1) {
				Optional<ProdAteDto> tpdk1OptionalFail = dto.getProdAteDtos().stream() 
						.filter(tpdk1 -> tpdk1.getStatus().equalsIgnoreCase("fail")).findFirst();
				if (tpdk1OptionalFail.isPresent()) {
					devSts="Fail";
				} else {
					Optional<ProdAteDto> tpdk1OptionalPass = dto.getProdAteDtos().stream()
							.filter(tpdk1 -> tpdk1.getStatus().equalsIgnoreCase("pass")).findFirst();
					if (tpdk1OptionalPass.isPresent()) {						
						devSts="Pass";
					} else {
						throw new DataNotFoundException("No pass or fail status found: ");
					}
				}
		}
		

			Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(dto.getId());
			if (!inspectionRoundOptional.isPresent()) {
				throw new DataNotFoundException("Save : InspRound Id not found: " + dto.getId());
			}
			InspectionRound inspectionRound = inspectionRoundOptional.get();
			dto.setCreatedDatetime(inspectionRound.getCreatedDatetime());
			savedInspectionRound = inspectionRoundService.populateInspectionRound(dto, false, inspectionRound);	
			savedInspectionRound.setSubmittedDate(new Date());	
			savedInspectionRound.setStatus(devSts);
			
			if(devSts.equalsIgnoreCase("fail")) {
				
				if(!dto.getIsQa()) { 
				SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findById(dto.getDevId())
						.orElseThrow(() -> new DataNotFoundException("Status : Dev Id not found: " + dto.getDevId()));
				subSlipSerialNo.setFinalStatus(devSts);
				subSlipSerialNoRepository.save(subSlipSerialNo);
			} else {
          OLLineItemSerialNo oLLineItemSerialNo = olLineItemSerialNoRepository.findFirstBySerialNoIdOrderByCreatedAtDesc(dto.getDevId()).orElseThrow(() -> new DataNotFoundException("Status : Dev Id not found: " + dto.getDevId()));
				oLLineItemSerialNo.setStatus(devSts);
				olLineItemSerialNoRepository.save(oLLineItemSerialNo);
			}
	}
			savedInspectionRound.setIsQa(dto.getIsQa());
			isQa = inspectionRound.getIsQa();
			savedInspectionRound = inspectionRoundRepository.save(savedInspectionRound);
		
		List<Tpdk1> tpdk1s = new ArrayList<>();
		for (ProdAteDto tpdk1Dto : dto.getProdAteDtos()) {
			if (tpdk1Dto.getId() == 0) {
				Tpdk1 tpdk1 = mapper.map(tpdk1Dto, Tpdk1.class);
				tpdk1.setInspectionRound(savedInspectionRound);
				Optional<Spec> specOptional = specRepository.findById(tpdk1Dto.getSpecId());
				if (specOptional.isPresent()) {
					tpdk1.setSpec(specOptional.get());
				} else {
					throw new DataNotFoundException("Save : Spec Id not found: " + tpdk1Dto.getSpecId());
				}
				tpdk1s.add(tpdk1);
			} else {
				Optional<Tpdk1> tpdk1Optional = tpdk1Repository.findById(tpdk1Dto.getId());
				if (tpdk1Optional.isPresent()) {
					Tpdk1 tpdk1 = mapper.map(tpdk1Dto, Tpdk1.class);
					tpdk1.setInspectionRound(savedInspectionRound);
					Optional<Spec> specOptional = specRepository.findById(tpdk1Dto.getSpecId());
					if (specOptional.isPresent()) {
						tpdk1.setSpec(specOptional.get());
					} else {
						throw new DataNotFoundException("Save : Spec Id not found: " + tpdk1Dto.getSpecId());
					}
					tpdk1s.add(tpdk1);
				}
			}
		}
		List<Tpdk1> savedTpdk1 = tpdk1Repository.saveAll(tpdk1s);
		
//		LineItemLotsRegister lineItemLotsRegister = savedInspectionRound.getLineItemLotsRegister();

		  if (ApplicationConstants.RFR_ENABLE== 1) {	          

				if (Boolean.TRUE.equals(isQa) ) {					

//	                updateQaStatusForQa(lineItemLotsRegister, savedTpdk1);
	            } else {
//
//	                updateStatusForNonQa(lineItemLotsRegister, savedTpdk1);
	            }
	        }
//	        lineItemLotsRegisterRepository.save(lineItemLotsRegister);

		InspectionRoundDto savedDto = mapper.map(savedInspectionRound, InspectionRoundDto.class);
		savedDto.setProdAteDtos(savedTpdk1.stream()
				.map(tpdk1 -> mapper.map(tpdk1, ProdAteDto.class)).collect(Collectors.toList()));
		
		InspTestMappingDto inspTestDto = new InspTestMappingDto();
        inspTestDto.setInspId(savedInspectionRound.getId());
        inspTestDto.setTestId(dto.getProdAteDtos().get(0).getTestId());
        inspTestDto.setSubmittedById(dto.getSubmittedById());
        Optional<OlStatus> olstatus= olStatusRepository.findStatus(devSts.toLowerCase());
        inspTestDto.setTestStatusId(olstatus.get().getId());
        InspTestMapping inspTestMapping = mapper.map(inspTestDto, InspTestMapping.class);
        inspTestMapping.setSubmittedDate(new Date());
        InspTestMapping savedInspTestMapping = inspTestMappingRepository.save(inspTestMapping);
        InspTestMappingDto savedInspTestDto = mapper.map(savedInspTestMapping, InspTestMappingDto.class);
		return ResponseEntity.ok(gson.toJson(savedDto));
	}

	public InspectionRoundDto getTpdk1ByDevId(Integer devId, Integer prodId, Integer testId, Integer roundId,
			Boolean isQa) {

		List<ProdAteDto> prodAteDtos = new ArrayList<>();
		InspectionRoundDto dto = new InspectionRoundDto();
		List<Tpdk1> tpdk1sList;
		if (isQa != null && isQa) {
			tpdk1sList = tpdk1Repository.getByDevIdProdTestRndIsQa1(devId, prodId, testId, roundId, isQa);
		} else {
			tpdk1sList = tpdk1Repository.getByDevIdProdTestRndIsQa(devId, prodId, testId, roundId, isQa);
		}

		if (tpdk1sList.isEmpty()) {
			List<Spec> specValueDtoList = specRepository.getActiveSpecsByProdIdTestId(prodId, testId);

			for (Spec specbo : specValueDtoList) {
				ProdAteDto proddto = new ProdAteDto();
				proddto.setId(0);
				proddto.setSpecId(specbo.getId());
				proddto.setSpecVal(specbo.getSpecVal());
				proddto.setMin(specbo.getMin());
				proddto.setMax(specbo.getMax());
				try {
					proddto.setParamType(specbo.getParamType().getParamType());
				} catch (Exception e) {
					proddto.setParamType("N/A");
				}
				proddto.setTyClauseNo(specbo.getTyClauseNo());
				proddto.setObsTypeId(specbo.getObsType().getId());
				proddto.setObsType(specbo.getObsType().getType());
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
	                    devId, prodId, testId, roundId, specbo.getId(), isQa , null);
	            
	            // Attach all files to the DTO's fileDetails list
	            for (AteTestReportUpload report : uploadedReports) {
	                ateUploadService.addFileToDto(report, proddto);
	            }
				
				prodAteDtos.add(proddto);
			}
		} else {
			for (Tpdk1 bo : tpdk1sList) {
				ProdAteDto proddto = new ProdAteDto();

				List<AteTestReportUpload> uploadedReports = ateUploadService.getAllUploadedReports(
		                devId, 
		                prodId, 
		                testId, 
		                roundId, 
		                bo.getSpec().getId(), 
		                isQa,
		                null
		        );

		        // 2. If reports exist, attach each one as a file detail entry
		        if (uploadedReports != null && !uploadedReports.isEmpty()) {
		            for (AteTestReportUpload report : uploadedReports) {
		                // Use the helper method to populate the FileDetailDto inside the proddto
		                ateUploadService.addFileToDto(report, proddto);
		            }
		        }
				proddto.setId(bo.getId());
				proddto.setSpecId(bo.getSpec().getId());
				proddto.setObsTypeId(bo.getSpec().getObsType().getId());
				Spec specbo = bo.getSpec();
				proddto.setSpecId(specbo.getId());
				proddto.setSpecVal(specbo.getSpecVal());
				proddto.setMin(specbo.getMin());
				proddto.setMax(specbo.getMax());
//				proddto.setParamType(specbo.getParamType().getParamType());
				proddto.setTyClauseNo(specbo.getTyClauseNo());
				proddto.setObsTypeId(specbo.getObsType().getId());
				proddto.setObsType(specbo.getObsType().getType());
				proddto.setObsValue(specbo.getObsValue());
				proddto.setRemarks(bo.getRemarks());
				proddto.setObsValTx(bo.getObsValue());
				
				
				try {
					proddto.setParamType(specbo.getParamType().getParamType());
				} catch (Exception e) {
					proddto.setParamType("N/A");
				}
				
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

			Tpdk1 obj = tpdk1sList.get(0);
			Integer inspId = obj.getInspectionRound().getId();
			Integer testid = obj.getTest().getId();

			InspTestMapping inspTestMapping = inspTestMappingRepository.findByInspTestId(inspId, testid);

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

	public List<AteGraphDataDto> getGraphData(Integer specId, Date from, Date to) {
		List<AteGraphDataDto> ateGraphDataDtos = new ArrayList<>();
		List<Object[]> tifcsGraphData = tpdk1Repository.getGraphData(specId ,from ,to);
		for (Object[] obj : tifcsGraphData) {
			AteGraphDataDto ateGraphDataDto = new AteGraphDataDto();
			ateGraphDataDto.setRegNol(obj[0] != null ? obj[0].toString() : null);
			ateGraphDataDto.setObsValue(obj[1] != null ? obj[1].toString() : null);
			ateGraphDataDto.setStatus(obj[2] != null ? obj[2].toString() : null);
			ateGraphDataDtos.add(ateGraphDataDto);
		}

		return ateGraphDataDtos;
	}
	public List<TestInfoDto> getTestInfo(Integer prodId, Integer devId){

        List<Integer> testIdList = productMappingRepository.getTestIdsByProdId(prodId);
        List<TestInfoDto> dtos = new ArrayList<>(); 
        for (Integer testId : testIdList) {
            List<Object[]> testInfoList = tpdk1Repository.getTestInfo(prodId, devId, testId);
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
//  	    System.out.println("**************");
//           System.out.println(testId);
  	    if (testInfoList != null && !testInfoList.isEmpty()) {
  	        Object[] data = testInfoList.get(0); // Use the first element for date and ID
  	        String dateString = data[0].toString();
  	        String submittedById = data[1].toString();

  	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
  	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

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
  	        List<String> statuses = testInfoList.stream()
  	                                           .map(arr -> (String) arr[2]) // Assuming status is at index 2
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
			prodDet = tpdk1Repository.getAllTestListByPrdIdIsQa(devId, prodId , roundId);
		} else {
			prodDet = tpdk1Repository.getAllTestListByPrdId(devId, prodId, roundId);
		}
       ProdDto prodDto = new ProdDto();
       List<TestValDto> testDtos = new ArrayList<>();
       for(Object[] obj: prodDet) {
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
    	   testDto.setSubmittedBy(obj[18]!= null ? obj[18].toString(): null);
    	   
    	   testDtos.add(testDto);
       }
       prodDto.setTestValDto(testDtos);
		return prodDto;
	}
}
