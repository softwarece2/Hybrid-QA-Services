package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.dto.DepartmentToProductMappingDto;
import in.co.bel.olms.ate.entity.DepartmentToProductMapping;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.repository.DepartmentProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentProductMappingService {

    private static final Logger _logger_ = LoggerFactory.getLogger(DepartmentProductMappingService.class);
    
    private final Mapper mapper;

    private final DepartmentProductMappingRepository departmentProductMappingRepository;

    private final DepartmentRepository departmentRepository;

    private final ProductsRepository productsRepository;
  
    @Transactional
    public DepartmentToProductMapping mapDepartmentToProduct(DepartmentToProductMappingDto mappingDto) {
        if (mappingDto == null || mappingDto.getDepartmentId() == null || mappingDto.getProductId() == null) {
            _logger_.error("Department ID and Product ID cannot be null.");
            throw new IllegalArgumentException("Product ID and Department ID cannot be null.");
        }
        if (departmentProductMappingRepository.existsByProductId(mappingDto.getProductId())) {
            _logger_.error("Error: Product already mapped.");
            throw new IllegalArgumentException("Product already mapped to a Department.");
        }
//
//        if (departmentProductMappingRepository.existsByDepartmentId(mappingDto.getDepartmentId())) {
//            _logger_.error("Error: Department already mapped.");
//            throw new IllegalArgumentException("Department already mapped to a Product.");
//        }

        Products product = productsRepository.findById(mappingDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID"));
        Department department = departmentRepository.findById(mappingDto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Department ID"));
        
        if (departmentProductMappingRepository.existsByDepartmentIdAndProductId(department.getId(), product.getId())) {
            _logger_.error("Mapping already exists for department {} and product {}", department.getId(), product.getId());
            throw new IllegalArgumentException("Mapping already exists for the selected department and product.");
        }

        DepartmentToProductMapping mapping = new DepartmentToProductMapping();
        mapping.setDepartment(department);
        mapping.setProduct(product);
        departmentProductMappingRepository.save(mapping);
        return mapping;
    }

    public List<DepartmentToProductMappingDto> getAllMappings() {
        return departmentProductMappingRepository.findAll().stream()
                .map(entity -> mapper.map(entity, DepartmentToProductMappingDto.class))
                .collect(Collectors.toList());
    }
    
   public List<DepartmentToProductMappingDto> getDeptProdAndEpCode(){
	   List<Object[]> dpm = new ArrayList<>();
	   
	   dpm = departmentProductMappingRepository.getDeptProdAndEpCode();
	   
    List<DepartmentToProductMappingDto> dtoList = new ArrayList<>();
    for (Object[] result : dpm) {
    	DepartmentToProductMappingDto deptProdDto = new DepartmentToProductMappingDto();
    	deptProdDto.setId((Integer) result[0]); 
    	deptProdDto.setDepartmentId((Integer) result[1]);
    	deptProdDto.setProductId((Integer) result[2]);
    	deptProdDto.setDepartmentName((String) result[3]);
    	deptProdDto.setProductName((String) result[4]);
//    	deptProdDto.setPartNo((String) result[5]);
//    	deptProdDto.setMaterialDescription((String) result[6]);
//    	deptProdDto.setCatPartNo((String) result[7]);
//    	deptProdDto.setNsnPartNo((String) result[8]);    	
        dtoList.add(deptProdDto);
    }
    return dtoList;
}


    @Transactional
    public void deleteMappingById(Integer id) {
        departmentProductMappingRepository.deleteById(id);
    }

    @Transactional
    public DepartmentToProductMappingDto updateMapping(DepartmentToProductMappingDto departmentToProductMappingDto) {
        DepartmentToProductMapping existingMapping = departmentProductMappingRepository.findById(departmentToProductMappingDto.getId())
                .orElseThrow(() -> new DataNotFoundException("Mapping not found with id: " + departmentToProductMappingDto.getId()));

        Department department = departmentRepository.findById(departmentToProductMappingDto.getDepartmentId())
                .orElseThrow(() -> new DataNotFoundException("Invalid Department ID"));

        Products product = productsRepository.findById(departmentToProductMappingDto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Invalid Product ID"));

        existingMapping.setDepartment(department);
        existingMapping.setProduct(product);
        departmentProductMappingRepository.save(existingMapping);
        return mapper.map(existingMapping, DepartmentToProductMappingDto.class);
    }   
    
}