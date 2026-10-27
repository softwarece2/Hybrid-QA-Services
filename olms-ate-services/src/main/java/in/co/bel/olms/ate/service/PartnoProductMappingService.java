package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.dto.PartnoProductMappingDto;
import in.co.bel.olms.ate.dto.ProductEPNumberMappingDto;
import in.co.bel.olms.ate.entity.PartnoProductMapping;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.repository.PartnoProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.PartNumberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnoProductMappingService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnoProductMappingService.class);
	
	private final Mapper mapper;

    private final PartnoProductMappingRepository partnoProductMappingRepository;

    private final ProductsRepository productsRepository;

    private final PartNumberRepository partNumberRepository;

    @Transactional
    public PartnoProductMapping mapPartnumberToProduct(PartnoProductMappingDto partnoProductMappingDto) {

        if (partnoProductMappingDto == null || partnoProductMappingDto.getProductId() == null || partnoProductMappingDto.getPartNumberId() == null) {
        	logger.error("Product ID and Part Number ID cannot be null.");
            throw new IllegalArgumentException("Product ID and Part Number ID cannot be null.");
        }
//        if (partnoProductMappingRepository.existsByProductId(partnoProductMappingDto.getProductId())) {
//            logger.error("Error: Product already mapped.");
//            throw new IllegalArgumentException("Product already mapped to a Part Number.");
//        }

        if (partnoProductMappingRepository.existsByPartNumberId(partnoProductMappingDto.getPartNumberId())) {
            logger.error("Error: Part Number already mapped.");
            throw new IllegalArgumentException("Product already mapped to a Material No./EP Code No.");
        }
        Products product = productsRepository.findById(partnoProductMappingDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID"));        
        PartNumber partNumber = partNumberRepository.findById(partnoProductMappingDto.getPartNumberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Part Number ID"));       
        PartnoProductMapping mapping = new PartnoProductMapping();
        mapping.setProduct(product);
        mapping.setPartNumber(partNumber);
        partnoProductMappingRepository.save(mapping);
        return mapping;
    }
    public List<PartnoProductMappingDto> mapPartnumbersToProducts(List<PartnoProductMappingDto> partnoProductMappingDtos, Integer productId) {

        List<PartnoProductMapping> entitiesToSave = new ArrayList<>();

        for (PartnoProductMappingDto dto : partnoProductMappingDtos) {	          
        	PartnoProductMapping entity = mapper.map(dto, PartnoProductMapping.class);
        	if (dto.getPartNumberId()==null) {
                throw new IllegalArgumentException("Part number cannot be null");
            }
        	Optional<PartNumber> partNumberOptional = partNumberRepository.findById(dto.getPartNumberId());
        	if (!partNumberOptional.isPresent()) {
                throw new IllegalArgumentException("Invalid Part Number ID.");
            }
//        	  if (partnoProductMappingRepository.existsByProductId(dto.getProductId())) {
//                  logger.error("Error: Product already mapped.");
//                  throw new IllegalArgumentException("Product already mapped to a Part Number.");
//              }

              if (partnoProductMappingRepository.existsByPartNumberId(dto.getPartNumberId())) {
                  logger.error("Error: Part Number already mapped.");
                  throw new IllegalArgumentException("Product already mapped to a Material No./EP Code No.");
              }
//            entity.setCreatedDatetime(new Date());
            Optional<Products> productOptional = productsRepository.findById(productId);
            if (productOptional.isPresent()) {
                entity.setProduct(productOptional.get());
            } else {	                
                throw new DataNotFoundException("Invalid product ID");
            }

            entitiesToSave.add(entity);
        }
        try {
        List<PartnoProductMapping> savedEntities = partnoProductMappingRepository.saveAll(entitiesToSave);
        return savedEntities.stream()
            .map(entity -> mapper.map(entity, PartnoProductMappingDto.class))
            .collect(Collectors.toList());
        }
        catch (DataIntegrityViolationException e) {
        	 throw new DataIntegrityViolationException("Duplicate product and part number combination found.");
        }
    }
   
    
    public List<PartnoProductMappingDto> getAllMappings() {
        return partnoProductMappingRepository.findAll().stream()
                .map(entity -> mapper.map(entity, PartnoProductMappingDto.class))
                .collect(Collectors.toList());
    }
    
    public List<ProductEPNumberMappingDto> getAllProductWithEpCode(){
    	List<ProductEPNumberMappingDto> productEPNumberMappingDtos = new ArrayList<>();
			List<PartnoProductMapping> partnoProductMapping = partnoProductMappingRepository.findAll(Sort.by("product_id").ascending());
			for (PartnoProductMapping li: partnoProductMapping) {
				ProductEPNumberMappingDto dto = new ProductEPNumberMappingDto();
				dto.setProductId(li.getProduct().getId());
				dto.setPartNumberId(li.getPartNumber().getId());
				dto.setProductNameEpCode(li.getProduct().getProdName()+ " - " + li.getPartNumber().getPartNo()) ;
				productEPNumberMappingDtos.add(dto);
			}
			
//					partnoProductMappingRepository.findAll().stream()
//	                .map(entity -> mapper.map(entity, ProductEPNumberMappingDto.class))
//	                .collect(Collectors.toList());
//			for(ProductEPNumberMappingDto dto: productEPNumberMappingDto) {
//				dto.setProductNameEpCode(dto.);
//			}
			return productEPNumberMappingDtos ;
	    }

    
    @Transactional
    public void deleteMappingById(Integer id) {
        partnoProductMappingRepository.deleteById(id);
    }

    @Transactional
    public PartnoProductMappingDto updateMapping(PartnoProductMappingDto partnoProductMappingDto) {
        PartnoProductMapping existingMapping = partnoProductMappingRepository.findById(partnoProductMappingDto.getId())
                .orElseThrow(() -> new DataNotFoundException("Mapping not found with id: " + partnoProductMappingDto.getId()));

        Products product = productsRepository.findById(partnoProductMappingDto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Invalid Product ID"));

        PartNumber partNumber = partNumberRepository.findById(partnoProductMappingDto.getPartNumberId())
                .orElseThrow(() -> new DataNotFoundException("Invalid Part Number ID"));
//        if (partnoProductMappingRepository.existsByProductId(partnoProductMappingDto.getProductId())) {
//            if (!existingMapping.getProduct().getId().equals(partnoProductMappingDto.getProductId())) {
//                throw new IllegalArgumentException("Product already mapped to another Part Number.");
//            }
//        }

        if (partnoProductMappingRepository.existsByPartNumberId(partnoProductMappingDto.getPartNumberId())) {          
            if (!existingMapping.getPartNumber().getId().equals(partnoProductMappingDto.getPartNumberId())) {
                throw new IllegalArgumentException("Part Number already mapped to another Product.");
            }
        }
        existingMapping.setProduct(product);
        existingMapping.setPartNumber(partNumber);       
        partnoProductMappingRepository.save(existingMapping);
      

        return mapper.map(existingMapping, PartnoProductMappingDto.class); 
    }
}