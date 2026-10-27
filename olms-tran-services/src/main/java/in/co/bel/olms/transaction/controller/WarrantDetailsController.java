package in.co.bel.olms.transaction.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.WarrantCommonDto;
import in.co.bel.olms.transaction.dto.WarrantDetailsDto;
import in.co.bel.olms.transaction.service.WarrantDetailsService;
import lombok.RequiredArgsConstructor;

/**
 * @author Shridhar R
 * @version 1.0
 * @since 20-01-2026
 * Warrant Details Controller
 */

@RestController
@RequestMapping("/warrant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WarrantDetailsController {
	
	private static final Logger logger =LoggerFactory.getLogger(WarrantDetailsController.class);
	
	private final WarrantDetailsService warrantDetailsService;
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	@PostMapping("/save")
	private ResponseEntity<String> saveWarrantDetials(@RequestBody WarrantDetailsDto reqDto, @RequestHeader(name = "Authorization") String token) throws Exception {
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			reqDto.setCreatedById(empId);
			return warrantDetailsService.saveWarrantDetials(reqDto);
		} catch(IllegalArgumentException e) {
			logger.error("invalid argument : {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
			
		} catch (Exception e) {
			logger.error("Failed to save: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Failed to save");
		}
	}
	
	@GetMapping("/getAll")
	private ResponseEntity<List<WarrantDetailsDto>> getAllWarrantDetails(){
		List<WarrantDetailsDto> dtos = warrantDetailsService.getAllWarrantDetails();
		return ResponseEntity.ok(dtos);		
	}
	
	
	// Added by Manasa
	@GetMapping("/getBySoLineId/{soliId}")
	public ResponseEntity<List<CategoryDto>> getBySoLineId(@PathVariable int soliId)throws DataNotFoundException{
		
		return ResponseEntity.ok(warrantDetailsService.getBySoLineId(soliId));
		
	}
	
	@GetMapping("/getAllWarrants")
	public ResponseEntity<List<CategoryDto>> getAllWarrants( @RequestParam(required = false) List<Integer> statusIds)throws DataNotFoundException{
	
		List<CategoryDto> categoryDtos=warrantDetailsService.getAllWarrants(statusIds);
		Collections.sort(categoryDtos, Comparator.comparing(CategoryDto::getId).reversed());
		return ResponseEntity.ok(categoryDtos);
	}
	
	@GetMapping("/getNameBySoLiId/{soLiId}")
	public ResponseEntity<List<Map<String, Object>>> getWarrantNameBySoLiId(@PathVariable Integer soLiId) throws DataNotFoundException{
		try {
			List<Map<String, Object>> maps = warrantDetailsService.getWarrantData(soLiId);
			return ResponseEntity.ok(maps);
		} catch (Exception e) {
			 logger.error("Error retrieving warrant name for soLiId {}: ", soLiId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getRegs/{soLiId}")
	public ResponseEntity<List<LineItemSerialNoDto>> getRegs(@PathVariable Integer soLiId, 
			@RequestParam(name = "type", defaultValue = "mapped") String type,
			@RequestParam(name = "warrantId", required = false) Integer warrantId) throws DataNotFoundException{
		try {
			List<LineItemSerialNoDto> maps = warrantDetailsService.getRegs(soLiId, warrantId, type);
			return ResponseEntity.ok(maps);
		} catch (Exception e) {
			logger.error("Error retrieving regs for soLiId: {}, type: {}, warrantId: {}. Error: ", 
	                  soLiId, type, warrantId, e);
	        
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getRegsByWarrantIds") // warrantIds === categoryIds
	public ResponseEntity<Object> getRegsByWarrantIds( @RequestParam("warrantIds") List<Integer> warrantIds) throws DataNotFoundException{
		try {
			List<LineItemSerialNoDto> regNos = warrantDetailsService.getRegsByWarrantIds(warrantIds);
			return ResponseEntity.ok(regNos);
		} catch (Exception e) {
			logger.error("Error retrieving regs for provided warrantIds {}: ", warrantIds, e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@GetMapping("/getWarrantDataForTCreation/{warrantId}")
	public ResponseEntity<Object> getWarrantDataForTCreation(@PathVariable(name = "warrantId") Integer warrantId){
		try {
			WarrantCommonDto regNos = warrantDetailsService.getWarrantDataForTCreation(warrantId);
			return ResponseEntity.ok().body(regNos);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@GetMapping("/getSubSlipDataByWarrantId/{warrantId}")
	public ResponseEntity<Object> getSubSlipDataByWarrantId(@PathVariable Integer warrantId) {
		try {
			List<WarrantCommonDto> regNos = warrantDetailsService.getSubSlipDataByWarrantId(warrantId);
			return ResponseEntity.ok().body(regNos);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/getSubSlipDataForTUpdation/{sSlipId}")
	public ResponseEntity<Object> getSubSlipDataForTUpdation(@PathVariable(name = "sSlipId") Integer sSlipId) {
		try {
			WarrantCommonDto  regNos = warrantDetailsService.getSubSlipDataForTUpdation(sSlipId);
			return ResponseEntity.ok().body(regNos);
		} catch (Exception e) {
			 logger.error("Error retrieving sub-slip data for sSlipId {}: ", sSlipId, e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	// Service to un-map the mapped serial no. - Naveen (11-02-26)
	@PostMapping("/unmap/serialNo")
	public ResponseEntity<String> unmapSerialNo(@RequestBody WarrantDetailsDto dto, @RequestHeader(name = "Authorization") String token){
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			dto.setCreatedById(empId);
			String response = warrantDetailsService.unmapSerialNo(dto);
			return ResponseEntity.ok().body(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@GetMapping("/getWarrantById/{warrantId}")
	public ResponseEntity<List<CategoryDto>> getWarrantyById(@PathVariable Integer warrantId)throws DataNotFoundException{
		return ResponseEntity.ok(warrantDetailsService.getWarrantById(warrantId));
	}	
	
	
	
	//Get warrants by emp id - i.e filtered by the dept id and products mapped
	@GetMapping("/getWarrantsByHos")
	public ResponseEntity<List<CategoryDto>> getWarrantsByHos(@RequestHeader(name = "Authorization") String token, @RequestParam(required = false) List<Integer> statusIds) {
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Integer empRoleId = appSecurity.getRoleIdFromJwtToken(token);

			List<CategoryDto> response = warrantDetailsService.getWarrantyByHosEmpId(empId, empRoleId,statusIds);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			 logger.error("Error retrieving warrants by HOS : {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	// Get warrant by alloted emp id
	@GetMapping("/getAllotedWarrants")
  public ResponseEntity<List<CategoryDto>> getAllotedWarrantys(@RequestHeader(name = "Authorization") String token, @RequestParam(required = false) List<Integer> statusIds) {
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);

      List<CategoryDto> response = warrantDetailsService.getAllotedWarrants(empId, statusIds);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error retrieving allotted warrants : {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

}
