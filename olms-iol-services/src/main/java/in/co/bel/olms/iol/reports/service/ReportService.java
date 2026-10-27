package in.co.bel.olms.iol.reports.service;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.iol.dto.ProductTypeDto;
import in.co.bel.olms.iol.dto.TestTypeDto;
import in.co.bel.olms.iol.entity.ProductType;
import in.co.bel.olms.iol.entity.TestType;
import in.co.bel.olms.iol.repository.ProductTypeRepository;
import in.co.bel.olms.iol.repository.TestTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final ProductTypeRepository productTypeRepository;
	
	private final TestTypeRepository testTypeRepository;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	Gson gson = new Gson();

	public String getAllProductTypes() {
		try {
			List<ProductType> entity = productTypeRepository.findAll();
			List<ProductTypeDto> dtos = new ArrayList<>();
			for (ProductType data : entity) {
				ProductTypeDto dto = mapper.map(data, ProductTypeDto.class);
				dtos.add(dto);
			}
			return gson.toJson(dtos);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return ApplicationConstants.FAILURE;
		}
	}

	public String getTestByProductType(int productTypeId) {
		try {
			List<TestType> testTypeList = testTypeRepository.getTestByProductType(productTypeId);
			List<TestTypeDto> dtoList = new ArrayList<>();
			for (TestType type : testTypeList) {
				TestTypeDto dto = mapper.map(type, TestTypeDto.class);
				dtoList.add(dto);
			}
			return gson.toJson(dtoList);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return ApplicationConstants.FAILURE;
		}
	}
}
