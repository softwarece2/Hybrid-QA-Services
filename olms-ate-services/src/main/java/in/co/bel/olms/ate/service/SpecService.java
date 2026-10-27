package in.co.bel.olms.ate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.dto.ApplicableTestDto;
import in.co.bel.olms.ate.dto.ProductMappingDto;
import in.co.bel.olms.ate.dto.SpecDto;
import in.co.bel.olms.ate.dto.SpecValueDto;
import in.co.bel.olms.ate.dto.StdTestOrApplicableTestDto;
import in.co.bel.olms.ate.dto.TestByMstDto;
import in.co.bel.olms.ate.dto.TestDetDto;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.repository.MainTypeRepository;
import in.co.bel.olms.ate.repository.ObsTypeRepository;
import in.co.bel.olms.ate.repository.ObsValueRepository;
import in.co.bel.olms.ate.repository.ParamTypeRepository;
import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.ate.repository.SpecTestTypeRepository;
import in.co.bel.olms.ate.repository.TestByMstRepository;
import in.co.bel.olms.ate.repository.TestBySpecMapRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import in.co.bel.olms.ate.repository.UnitsRepository;
import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import in.co.bel.olms.transaction.entity.OlLineItemAttachment;
import in.co.bel.olms.transaction.repository.LineItemApplicableTestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecService {

	private final SpecRepository specRepository;

	private final ProductMappingRepository productMappingRepository;

	private final ProductsRepository productsRepository;

	private final TestRepository testRepository;

	private final UnitsRepository unitsRepository;

	private final ParamTypeRepository paramTypeRepository;

	private final ObsTypeRepository obsTypeRepository;

	private final ObsValueRepository obsValueRepository;

	private final LineItemApplicableTestRepository lineItemApplicableTestRepository;

	private final SpecTestTypeRepository specTestTypeRepository;

	private final MainTypeRepository mainTypeRepository;

	private final SpecStatusService specStatusService;

	private final TestBySpecMapRepository testBySpecMapRepository;

	private final TestByMstRepository testByMstRepository;

	private final Mapper mapper;

	@Value("${file.domain-url}")
	private String domainUrl;
	@Value("${spring.application.name}")
	private String applicationName;

	public List<SpecDto> getSpecsByProdId(int prodId) {

		List<SpecDto> specDtos = new ArrayList<>();
		List<Object[]> specs = specRepository.getSpecsByProdIdGpId(prodId);
		for (Object[] obj : specs) {
			SpecDto dto = new SpecDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setSpecVal(obj[1].toString());
			if (obj[2] != null) {
				dto.setMin(new BigDecimal(obj[2].toString()));
			} else {
				dto.setMin(null);
			}
			if (obj[3] != null) {
				dto.setMax(new BigDecimal(obj[3].toString()));
			} else {
				dto.setMax(null);
			}

			dto.setProdId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : null);
			dto.setTestId(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			dto.setLogInId(obj[6] != null ? Integer.parseInt(obj[6].toString()) : null);
			dto.setTestName(obj[7] != null ? obj[7].toString() : null);
			
			Boolean specStatus = specStatusService.getLatestStatus(Integer.parseInt(obj[0].toString()));
			dto.setStatus(specStatus != null ? specStatus : true );
			specDtos.add(dto);
		}

		return specDtos;
	}

	public List<ProductMappingDto> getProductTestDet(Integer prodId) {
		List<Object[]> productList = productMappingRepository.getProductTestDet(prodId);

		List<ProductMappingDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductMappingDto dto = new ProductMappingDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setLogInId(Integer.parseInt(obj[1].toString()));
			dto.setProudctName((obj[2].toString() + "- " + dto.getLogInId()));
			productMappingDtos.add(dto);
		}

		return productMappingDtos;

	}

	public List<Products> getAllProduct() {
		List<Products> productList = productsRepository.findAll();
		return productList;
	}

	public StdTestOrApplicableTestDto getStdOrtApplicableTests(Integer olLineItemId, Integer productId) {
		StdTestOrApplicableTestDto stdTestOrApplicableTestDto = new StdTestOrApplicableTestDto();

		if (olLineItemId != null) {
			List<LineItemApplicableTest> applicableTests = lineItemApplicableTestRepository
					.getApplicableTestsByLineItemId(olLineItemId);
			List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();

			for (LineItemApplicableTest lineItemApplicableTest : applicableTests) {
				ApplicableTestDto applicableTestDto = new ApplicableTestDto();
				applicableTestDto.setId(lineItemApplicableTest.getId());
				applicableTestDto.setApplicableTestId(lineItemApplicableTest.getApplicableTest().getId());
				applicableTestDto.setTestName(lineItemApplicableTest.getApplicableTest().getTestName());
				applicableTestDto.setRemarks(lineItemApplicableTest.getRemarks());
				applicableTestDto.setIsAte(lineItemApplicableTest.getIsAte());
				applicableTestDto.setIsRmt(lineItemApplicableTest.getIsRmt());

				if (lineItemApplicableTest.getIsAte() != null) {
					applicableTestDto.setIsAteStr(lineItemApplicableTest.getIsAte() ? "Yes" : "No");
				} else {
					applicableTestDto.setIsAteStr(null);
				}

				if (lineItemApplicableTest.getIsRmt() != null) {
					applicableTestDto.setIsRmtStr(lineItemApplicableTest.getIsRmt() ? "Yes" : "No");
				} else {
					applicableTestDto.setIsRmtStr(null);
				}

				List<OlLineItemAttachment> olLineItemAttachments = lineItemApplicableTest.getLineItemAttachments();
				Map<String, String> firmAttachmentURLs = new HashMap<>();
				Map<String, String> inspectorAttachmentURLs = new HashMap<>();

				if (olLineItemAttachments != null) {
					for (OlLineItemAttachment olLineItemAttachment : olLineItemAttachments) {
						String createdBy = olLineItemAttachment.getCreatedBy();
						applicableTestDto.setCreatedBy(createdBy);
					}
				}

				applicableTestDto.setFirmAttachmentURLs(firmAttachmentURLs);
				applicableTestDto.setInspectorAttachmentURLs(inspectorAttachmentURLs);
				applicableTestDtos.add(applicableTestDto);
			}

			stdTestOrApplicableTestDto.setApplicableTestDtos(applicableTestDtos);
		}

		if (productId != null) {
			List<Object[]> productList = productMappingRepository.getProductTestDet(productId);
			List<ProductMappingDto> productMappingDtos = new ArrayList<>();

			for (Object[] obj : productList) {
				ProductMappingDto dto = new ProductMappingDto();
				dto.setProductId(Integer.parseInt(obj[0].toString()));
				dto.setLogInId(Integer.parseInt(obj[1].toString()));
				dto.setProudctName(obj[2].toString() + " - " + dto.getLogInId());
				// dto.setDevId(Integer.parseInt(obj[3].toString()));
				productMappingDtos.add(dto);
			}

			stdTestOrApplicableTestDto.setStdProdctTest(productMappingDtos);
		}

		return stdTestOrApplicableTestDto;
	}

	
	public List<SpecValueDto> getSpecsByProdouctId(Integer prodId, Integer testId, Boolean active) {
		List<Spec> specs = new ArrayList<>();
		
		specs = active != null ? specRepository.getAllActiveSpecsByProdIdTestId(prodId, testId, active) : specRepository.getAllSpecsByProdIdTestId(prodId ,testId);
		
		
		Map<Integer, List<Spec>> specsByTestId = new HashMap<>();
		for (Spec spec : specs) {
			Integer currTestId = spec.getTest().getId();
			specsByTestId.computeIfAbsent(currTestId, k -> new ArrayList<>()).add(spec);
		}
		
		List<SpecValueDto> specValueDtoList = new ArrayList<>();
		for (Map.Entry<Integer, List<Spec>> entry : specsByTestId.entrySet()) {
			Integer currTestId = entry.getKey();
			List<Spec> testSpecs = entry.getValue();

			List<SpecDto> specDtos = testSpecs.stream().map(spec -> {
				SpecDto dto = new SpecDto();
				mapper.map(spec, dto);
				Boolean status = specStatusService.getLatestStatus(spec.getId());
				dto.setStatus(status != null ? status : true);
				
				try {
					dto.setMainType(spec.getMainType().getMainType());
				} catch (Exception e) {
					dto.setMainType("N/A");
				}
				
				try {
					dto.setTestType(spec.getTestType().getTestType());
				} catch (Exception e) {
					dto.setTestType("N/A");
				}
				
				List<TestByMstDto> testByMstDtos = testBySpecMapRepository.getTestBySpecMap(prodId, currTestId, spec.getId()).stream()
				        .map(objArray -> {
				            TestByMstDto testByDto = new TestByMstDto();
				            testByDto.setId((Integer)objArray[0]);
				            testByDto.setTestBy((String)objArray[1]);
				            return testByDto;
				           
				        }).collect(Collectors.toList());
				
				if (testByMstDtos != null && !testByMstDtos.isEmpty()) {
                   dto.setTestByMstDtos(testByMstDtos);
                } else {
                    dto.setTestByMstDtos(new ArrayList<>()); // Set empty list if no data found
                }
				
				return dto;
			}).collect(Collectors.toList());

			Spec firstSpec = testSpecs.get(0);
			String testName = (firstSpec != null && firstSpec.getTest() != null) ? firstSpec.getTest().getTestDesc()
					: null;

			SpecValueDto specValueDto = new SpecValueDto();
			specValueDto.setTestId(currTestId);
			specValueDto.setTestName(testName);
			specValueDto.setSpecDtos(specDtos);
			specValueDtoList.add(specValueDto);
		}

		return specValueDtoList;
	}
	
	public List<SpecValueDto> getSpecsByProductId(Integer prodId, Boolean active) {
		List<Spec> specs = new ArrayList<>();
		
		specs = active != null ? specRepository.getAllActiveSpecsByProdId(prodId, active) : specRepository.getAllSpecsByProdId(prodId);
		
		
		Map<Integer, List<Spec>> specsByTestId = new HashMap<>();
		for (Spec spec : specs) {
			Integer currTestId = spec.getTest().getId();
			specsByTestId.computeIfAbsent(currTestId, k -> new ArrayList<>()).add(spec);
		}
		
		List<SpecValueDto> specValueDtoList = new ArrayList<>();
		for (Map.Entry<Integer, List<Spec>> entry : specsByTestId.entrySet()) {
			Integer currTestId = entry.getKey();
			List<Spec> testSpecs = entry.getValue();

			List<SpecDto> specDtos = testSpecs.stream().map(spec -> {
				SpecDto dto = new SpecDto();
				mapper.map(spec, dto);
				Boolean status = specStatusService.getLatestStatus(spec.getId());
				dto.setStatus(status != null ? status : true);
				
				try {
					dto.setMainType(spec.getMainType().getMainType());
				} catch (Exception e) {
					dto.setMainType("N/A");
				}
				
				try {
					dto.setTestType(spec.getTestType().getTestType());
				} catch (Exception e) {
					dto.setTestType("N/A");
				}
				
				List<TestByMstDto> testByMstDtos = testBySpecMapRepository.getTestBySpecMap(prodId, currTestId, spec.getId()).stream()
				        .map(objArray -> {
				            TestByMstDto testByDto = new TestByMstDto();
				            testByDto.setId((Integer)objArray[0]);
				            testByDto.setTestBy((String)objArray[1]);
				            return testByDto;
				           
				        }).collect(Collectors.toList());
				
				if (testByMstDtos != null && !testByMstDtos.isEmpty()) {
                   dto.setTestByMstDtos(testByMstDtos);
                } else {
                    dto.setTestByMstDtos(new ArrayList<>()); // Set empty list if no data found
                }
				
				return dto;
			}).collect(Collectors.toList());

			Spec firstSpec = testSpecs.get(0);
			String testName = (firstSpec != null && firstSpec.getTest() != null) ? firstSpec.getTest().getTestDesc()
					: null;

			SpecValueDto specValueDto = new SpecValueDto();
			specValueDto.setTestId(currTestId);
			specValueDto.setTestName(testName);
			specValueDto.setSpecDtos(specDtos);
			specValueDtoList.add(specValueDto);
		}

		return specValueDtoList;
	}

	public List<Spec> getSpecsByProdnTestId(int prodId, int testId) {
		List<Spec> specs = specRepository.getAllSpecsByProdIdTestId(prodId ,testId);
		
		return specs;
	}
	
	public List<ProductMappingDto> getTestsByProdId(int prodId) {
	
		List<Object[]> productList = productMappingRepository.getTestsByProdId(prodId);

		List<ProductMappingDto> productMappingDtos = new ArrayList<>();

		for (Object[] obj : productList) {
			ProductMappingDto dto = new ProductMappingDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setProductId(Integer.parseInt(obj[1].toString()));
			dto.setTestId(Integer.parseInt(obj[2].toString()));
			dto.setTestName(obj[3].toString());
			dto.setSeq(Integer.parseInt(obj[4].toString()));
			productMappingDtos.add(dto);
		}

		return productMappingDtos;
	}
	
	
	public StdTestOrApplicableTestDto getFaiSpecsByProdId(Integer productId) {
		StdTestOrApplicableTestDto stdTestOrApplicableTestDto = new StdTestOrApplicableTestDto();

		if (productId != null) {

			List<Object[]> productList = specRepository.getSpecsByProdIdGpId(productId);
			List<SpecDto> specDtoList = new ArrayList<>();

			for (Object[] obj : productList) {

				SpecDto specDto = new SpecDto();
				specDto.setId((Integer) obj[0]);
				specDto.setSpecVal((String) obj[1]);
				if (obj[2] != null) {
					specDto.setMin(new BigDecimal(obj[2].toString()));
				} else {
					specDto.setMin(null);
				}
				if (obj[3] != null) {
					specDto.setMax(new BigDecimal(obj[3].toString()));
				} else {
					specDto.setMax(null);
				}

				specDto.setProdId((Integer) obj[4]);
				specDto.setTestId((Integer) obj[5]);
				specDto.setLogInId((Integer) obj[6]);
				specDto.setTestName((String) obj[7]);
				specDtoList.add(specDto);
			}
			stdTestOrApplicableTestDto.setSpecDtos(specDtoList);

		}

		return stdTestOrApplicableTestDto;
	}

	public List<TestDetDto> getTestDetailsForProdIdDevId(int prodId, int devId) {
		return null;
		
		//List<TestDetDto> test

	}

	public void addNewSpec(SpecDto specDto) throws DataIntegrityViolationException {
		
		if(specDto.getProdId() == null || specDto.getTestId() == null) {
			throw new IllegalArgumentException("Invalid payload");
		}
		
		if(!productsRepository.existsById(specDto.getProdId()))
			throw new IllegalArgumentException("Product not found for id : " + specDto.getProdId());
		
		if(!testRepository.existsById(specDto.getTestId()))
			throw new IllegalArgumentException("Test not found for id : " + specDto.getProdId());
		
		Spec newSpec = mapper.map(specDto, Spec.class);
		
		specRepository.save(newSpec);
		
	}

	public Map<String, List<?>> getAllFields() {

		Map<String, List<?>> field = new HashMap<>();
		
		field.put("units", unitsRepository.findAll(Sort.by("id").ascending()));
        field.put("paramType", paramTypeRepository.findAll(Sort.by("id").ascending()));
        field.put("obsType", obsTypeRepository.findAll(Sort.by("id").ascending()));
        field.put("obsValue", obsValueRepository.findAll(Sort.by("id").ascending()));
        field.put("testType", specTestTypeRepository.findAll(Sort.by("id").ascending()));
        field.put("mainType", mainTypeRepository.findAll(Sort.by("id").ascending()));
        field.put("testBy", testByMstRepository.findAll(Sort.by("id").ascending()));
		
		return field;
	}

//	@Transactional
//	public String updateSpecsMany(List<SpecDto> specUpdateDto) {
//				
//		if (specUpdateDto == null) {
//			throw new IllegalArgumentException("Invalid payload");
//		}
//		
//		List<Spec> specs = new ArrayList<>();
//		
//		specUpdateDto.forEach(updateDto -> {
//			Spec existingSpec = specRepository.findById(updateDto.getId()).orElse(null);
//			MainType mainType = mainTypeRepository.findById(updateDto.getMainTypeId()).orElse(null);
//			ParamType paramType = paramTypeRepository.findById(updateDto.getParamTypeId()).orElse(null);
//			
//			if (existingSpec == null)
//				throw new IllegalArgumentException("Spec not found for id : " + updateDto.getId());
			
//			existingSpec.setParamCode(updateDto.getParamCode());
//			existingSpec.setMainType(mainType);
//			existingSpec.setParamType(paramType);
//			existingSpec.setSpecVal(updateDto.getSpecVal());
//			existingSpec.setTyClauseNo(updateDto.getTyClauseNo());
//			existingSpec.setMin(updateDto.getMin());
//			existingSpec.setMax(updateDto.getMax());
//			existingSpec.setActive(updateDto.getActive());
//			existingSpec.setObsValue(updateDto.getObsValue());
//			
//			specs.add(existingSpec);
//			
//		});
//		specRepository.saveAll(specs);
//		return "Spec updated successfully";
//	}

}
