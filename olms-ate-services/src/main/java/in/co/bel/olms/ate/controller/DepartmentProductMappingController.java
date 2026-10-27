package in.co.bel.olms.ate.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.DepartmentToProductMappingDto;
import in.co.bel.olms.ate.service.DepartmentProductMappingService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/deptProdMap")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Use specific origins in production
@RequiredArgsConstructor
public class DepartmentProductMappingController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentProductMappingController.class);

    private final DepartmentProductMappingService deptProductMappingService;

    @PostMapping("/save")
    public ResponseEntity<?> createMapping(@RequestBody DepartmentToProductMappingDto mappingDto) {
        try {
            deptProductMappingService.mapDepartmentToProduct(mappingDto);
            return ResponseEntity.ok("Data submitted successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error mapping part number to product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<DepartmentToProductMappingDto>> getAllMappings() { 
    	try {
            return ResponseEntity.ok(deptProductMappingService.getAllMappings());
        } catch (Exception e) {
            logger.error("Error retrieving mappings: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
    
    @GetMapping("/getAllWithEpCode")
	public ResponseEntity<List<DepartmentToProductMappingDto>> getAllEpCode() {
		return ResponseEntity.ok(deptProductMappingService.getDeptProdAndEpCode());

	}

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteMapping(@PathVariable Integer id) { 
        try {
            deptProductMappingService.deleteMappingById(id);
            return ResponseEntity.ok("Mapping deleted successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Id provided");
        }
    }

	@PostMapping("/update")
	public ResponseEntity<String> updateMapping(@RequestBody DepartmentToProductMappingDto mappingDto) {
		try {
			deptProductMappingService.updateMapping(mappingDto);
			return ResponseEntity.ok("Data updated succesfully");
		} catch (DataNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DataIntegrityViolationException e) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); 
	    } catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mapping error");
		}
	}
}