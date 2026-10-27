package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.dto.AddProductDto;
import in.co.bel.olms.ate.dto.ProductsDto;
import in.co.bel.olms.ate.entity.ProductMapping;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	private final ProductsRepository productsRepository;

	private final TestRepository testRepository;

	private final SpecRepository specRepository;

	private final ProductMappingRepository prodMappingRepository;

	private final Mapper mapper;
	
	public List<ProductsDto> getAllProducts(){
			List<Products> prods = productsRepository.findAll();
			
			List<ProductsDto> prodDtos= prods.stream().map(prod -> mapper.map(prod, ProductsDto.class)).collect(Collectors.toList());
			
			return prodDtos;
	}

	@Transactional
	public String addNewProduct(AddProductDto addProductDto) {
		
		if(addProductDto.getRefProdId() == null || !productsRepository.existsById(addProductDto.getRefProdId())) 
			throw new IllegalArgumentException("Given reference Product ID is either null or does not exists");

		String prodName = addProductDto.getProdName().trim();
		
		if(prodName.isEmpty() || prodName == null) 
			throw new IllegalArgumentException("Product name is required.");
		
		if(productsRepository.existsByProdName(prodName))
			throw new IllegalArgumentException("A Product exists with this name: " + prodName);
			
		Products newProd = new Products();
		newProd.setProdName(prodName);
		newProd.setProdFullName(addProductDto.getProdFullName());
		newProd = productsRepository.save(newProd);
		
		List<Test> originalTests = testRepository.findAllById(addProductDto.getTestIds());
		
		List<Test> newTestsToSave = new ArrayList<>();
				
		originalTests.forEach(test -> {
			Test newTest = new Test();
			String testName = StringUtils.substringAfterLast(test.getTestDesc(), " - ");
			newTest.setTestDesc(prodName + " - " + testName);
			newTest.setIsFai(test.getIsFai());
			newTestsToSave.add(newTest);
		});
		
		List<Test> savedNewTests = testRepository.saveAll(newTestsToSave);
		
		List<Spec> newSpecsToSave = new ArrayList<>();
		List<ProductMapping> newProductMappings = new ArrayList<>();
	    
	    for (int i = 0; i < originalTests.size(); i++) {
	        Test oldTest = originalTests.get(i);
	        Test newTest = savedNewTests.get(i);
	        
	        Optional<ProductMapping> originalMapping = prodMappingRepository
	                .findByProductIdAndTestId(addProductDto.getRefProdId(), oldTest.getId());
	        
	        ProductMapping newProdMapping = new ProductMapping();
	        newProdMapping.setProduct(newProd);
	        newProdMapping.setTest(newTest);
	        newProdMapping.setSeq(originalMapping.map(ProductMapping::getSeq).orElse(i + 1));
	        newProductMappings.add(newProdMapping);

	        if (oldTest.getSpecs() != null) {
	            for (Spec oldSpec : oldTest.getSpecs()) {
	                Spec newSpec = new Spec();
	                newSpec.setSpecVal(oldSpec.getSpecVal());
	                newSpec.setMin(oldSpec.getMin());
	                newSpec.setMax(oldSpec.getMax());
	                newSpec.setParamCode(oldSpec.getParamCode());
	                newSpec.setTyClauseNo(oldSpec.getTyClauseNo());
	                newSpec.setObsValue(oldSpec.getObsValue());
	                newSpec.setSlNo(oldSpec.getSlNo());
	                newSpec.setActive(oldSpec.getActive());
	                newSpec.setProduct(newProd);
	                newSpec.setTest(newTest);
	                newSpec.setParamType(oldSpec.getParamType());
	                newSpec.setUnits(oldSpec.getUnits());
	                newSpec.setObsType(oldSpec.getObsType());
	                newSpec.setTestType(oldSpec.getTestType());
	                newSpec.setMainType(oldSpec.getMainType());

	                newSpecsToSave.add(newSpec);
	            }
	        }
	    }

	    specRepository.saveAll(newSpecsToSave);
    	prodMappingRepository.saveAll(newProductMappings);
	    
	    return "Product created successfully with ID: " + newProd.getId();
	}
}
