package in.co.bel.olms.ate.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.constants.RegistrationConstants;
import in.co.bel.olms.ate.dto.AddProductDto;
import in.co.bel.olms.ate.dto.ProductsDto;
import in.co.bel.olms.ate.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	private final ProductService productService;
	
	@GetMapping("/")
	public ResponseEntity<?> getAllProducts(){
		try {
			List<ProductsDto> res = productService.getAllProducts();
			return ResponseEntity.ok().body(res);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("saveFai IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to retreive products", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to retreive products");
		}
	}
	
	@GetMapping("/getStatus")
    public ResponseEntity<List<String>> getStatus() {
        List<String> statuses = Arrays.asList(
                RegistrationConstants.ACCEPTED,
                RegistrationConstants.REJECTED,
                RegistrationConstants.RFR,
                RegistrationConstants.PENDING,
                RegistrationConstants.INPROCESS,
                RegistrationConstants.PASS,
                RegistrationConstants.FAIL
        );
        return ResponseEntity.ok(statuses);
    }
	
	@PostMapping("/new")
	public ResponseEntity<String> addNewProduct(@RequestBody AddProductDto addProductDto) {
		try {
			String resp = productService.addNewProduct(addProductDto);
			return ResponseEntity.ok().body(resp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
}
