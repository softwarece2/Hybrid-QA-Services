package in.co.bel.olms.ate.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.ProductEPNumberMappingDto;
import in.co.bel.olms.ate.dto.ProductMappingDto;
import in.co.bel.olms.ate.dto.SpecDto;
import in.co.bel.olms.ate.dto.SpecValueDto;
import in.co.bel.olms.ate.dto.StdTestOrApplicableTestDto;
import in.co.bel.olms.ate.dto.TestDetDto;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.service.PartnoProductMappingService;
import in.co.bel.olms.ate.service.SpecService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spec")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SpecController {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnoProductMappingService.class);

	private final SpecService specService;
	
	private final PartnoProductMappingService partnoProductMappingService;

	@GetMapping("/getSpecsByProdId/{prodId}")
	public List<SpecDto> getSpecsByProdId(@PathVariable int prodId) {
		return specService.getSpecsByProdId(prodId);
	}

	@GetMapping("/getProductTestDet/{prodId}")
	public List<ProductMappingDto> getProductTestDet(@PathVariable Integer prodId) {
		return specService.getProductTestDet(prodId);
	}
	
	
	@GetMapping("/getAllProduct")
	public List<Products> getAllProduct() {
		return specService.getAllProduct();
	}
	
	@GetMapping("/getAllProductWithEpCode")
	public ResponseEntity<List<ProductEPNumberMappingDto>> getAllProductWithEpCode() {
		try {
			return ResponseEntity.ok(partnoProductMappingService.getAllProductWithEpCode()); 
																					
		} catch (Exception e) {
			logger.error("Error retrieving mappings: {}", e.getMessage());
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	
	@GetMapping("/getStdOrtApplicableTests")
    public ResponseEntity<?> getStdOrtApplicableTests( @RequestParam(required = false) Integer olLineItemId, @RequestParam(required = false) Integer productId ) {
        try {
            StdTestOrApplicableTestDto applicableTestDto = specService.getStdOrtApplicableTests(olLineItemId, productId);
            return ResponseEntity.ok(applicableTestDto);
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage(),e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

	@GetMapping("/getSpecsByProdIdTestId/{prodId}/{testId}")
	public List<SpecValueDto> getSpecsByProdIdTestId(@PathVariable int prodId,@PathVariable int testId, @RequestParam(required = false) Boolean active) {
		return specService.getSpecsByProdouctId(prodId, testId, active);
	}
	 
	@GetMapping("/getSpecsByProdId/v1/{prodId}")
	public List<SpecValueDto> getSpecsByProdId(@PathVariable int prodId, @RequestParam(required = false) Boolean active) {
	    return specService.getSpecsByProductId(prodId, active);
	}
	 
	@GetMapping("/getTestsByProdId/{prodId}")
	public List<ProductMappingDto> getTestsByProdId(@PathVariable int prodId) {
	    return specService.getTestsByProdId(prodId);
	}
	 
	@GetMapping("/getTestDetailsForProdIdDevId/{prodId}/{devId}")
	public List<TestDetDto> getTestDetailsForProdIdDevId(@PathVariable int prodId , @PathVariable int devId ) {
	    return specService.getTestDetailsForProdIdDevId(prodId, devId);
	}
	 
	@GetMapping("/getFaiSpecsByProdId")
	public ResponseEntity<?> getFaiSpecsByProdId(@RequestParam(required = false) Integer productId) {
		try {

			StdTestOrApplicableTestDto applicableTestDto = specService.getFaiSpecsByProdId(productId);
			return ResponseEntity.ok(applicableTestDto);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
 
	@PostMapping("/add")
	public ResponseEntity<String> addSpec(@RequestBody SpecDto specDto){
		try {
			specService.addNewSpec(specDto);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException ae) {
			ae.printStackTrace();
			return ResponseEntity.badRequest().body(ae.getMessage());
		} catch(DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	 
	 @GetMapping("/getAllFields")
	 public ResponseEntity<Map<String, List<?>>> getAllFields(){
		 
		 try {
			 return ResponseEntity.ok(specService.getAllFields());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	 }
	 
	 /*
	  *  updated by: SANJAY MP
	  *  
	  * 
	  */
//	 @PostMapping("/update")
//	 public ResponseEntity<String> updateSpecs(@RequestBody List<SpecDto> updateData){
//		 try {
//			String response = specService.updateSpecsMany(updateData); 
//			return ResponseEntity.ok(response);
//		} catch (IllegalArgumentException ae) {
//			ae.printStackTrace();
//			return ResponseEntity.badRequest().body(ae.getMessage());
//		} catch(DataIntegrityViolationException e) {
//			return ResponseEntity.badRequest().body(e.getRootCause().getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.internalServerError().body(e.getMessage());
//		}
//	 }
}
