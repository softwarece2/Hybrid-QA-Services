package in.co.bel.olms.ate.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.dto.TestDto;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

	private static final Logger logger = LoggerFactory.getLogger(TestService.class);

	private final TestRepository testRepository;

	private final ProductsRepository prodRepository;

	private final Mapper mapper;
	
	public List<TestDto> getAllTests(){
			List<Test> prods = testRepository.findAll();
			
			List<TestDto> testDtos= prods.stream().map(prod -> mapper.map(prod, TestDto.class)).collect(Collectors.toList());
			
			return testDtos;
	}
	
	public List<TestDto> getTestsByProdId(Integer prodId){
		
		if(!prodRepository.existsById(prodId))
			throw new IllegalArgumentException("Product does not exists with id: " + prodId);
		
		List<Test> testLists = testRepository.findAllTestByProdId(prodId);
		
		List<TestDto> testDtos= testLists.stream().map(prod -> {
			TestDto dto = mapper.map(prod, TestDto.class);
			String testName = StringUtils.substringAfterLast(prod.getTestDesc(), " - ");
			dto.setTestName(testName.trim());
			return dto;
		}).collect(Collectors.toList());
		
		return testDtos;
	}
}