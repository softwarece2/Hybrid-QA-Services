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

import in.co.bel.olms.ate.dto.PartnoProductMappingDto;
import in.co.bel.olms.ate.dto.PartnoProductMappingRequest;
import in.co.bel.olms.ate.service.PartnoProductMappingService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/partNoProductMapping")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PartnoProductMappingController {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnoProductMappingService.class);

	private final PartnoProductMappingService partnoProductMappingService;

	@PostMapping("/save")
	public ResponseEntity<String> createMapping(@RequestBody PartnoProductMappingDto mappingDto) {
		try {
			partnoProductMappingService.mapPartnumberToProduct(mappingDto);
			return ResponseEntity.ok("Data submitted successfully");
		} catch (IllegalArgumentException e) {
			logger.error("Error mapping part number to product: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("An unexpected error occurred.");
		}
	}

	@PostMapping("/saveMany")
	public ResponseEntity savePartnoProductMappings(@RequestBody PartnoProductMappingRequest requestBody) {
	    List<PartnoProductMappingDto> mappings = requestBody.getMapper();
	    Integer productId = requestBody.getProductId();

	    if (mappings == null || productId == null) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    try {
	        List<PartnoProductMappingDto> savedMappings = partnoProductMappingService.mapPartnumbersToProducts(mappings, productId);
	        return new ResponseEntity<>(savedMappings, HttpStatus.CREATED);
	    } catch (DataIntegrityViolationException e) {
	        // Handle duplicate combination error
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
	    } catch (DataNotFoundException e) {
	        //Handle invalid product id
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
	    }
	    catch (IllegalArgumentException e){
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<PartnoProductMappingDto>> getAllMappings() {
		try {
			return ResponseEntity.ok(partnoProductMappingService.getAllMappings()); // Correct way to call
																					// ResponseEntity.ok()
		} catch (Exception e) {
			logger.error("Error retrieving mappings: {}", e.getMessage());
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> deleteMapping(@PathVariable Integer id) {
		try {
			partnoProductMappingService.deleteMappingById(id);
			return ResponseEntity.ok("Mapping deleted successfully");
		} catch (DataNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Id provided");
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<String> updateMapping(@RequestBody PartnoProductMappingDto partnoProductMappingDto) {
		try {
			partnoProductMappingService.updateMapping(partnoProductMappingDto);
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