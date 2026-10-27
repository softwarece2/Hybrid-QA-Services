package in.co.bel.olms.transaction.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.SubSlipDetByInspDto;
import in.co.bel.olms.transaction.dto.SubSlipDto;
import in.co.bel.olms.transaction.dto.SubSlipLineItemDto;
import in.co.bel.olms.transaction.dto.SubSlipSerialNoDto;
import in.co.bel.olms.transaction.service.SubSlipService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subSlip")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SubSlipController {

	private final SubSlipService subSlipService;
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
    private static final Logger logger = LoggerFactory.getLogger(SubSlipController.class);

	@PreAuthorize("hasAnyAuthority('HOS','GIC')")
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody  SubSlipDto subSlipDto, @RequestHeader(name = "Authorization") String token ) {

		Integer empId = appSecurity.getEmpIdFromJwtToken(token);
		subSlipDto.setEmployeeId(empId);
		Map<Boolean, String> response = subSlipService.saveOfferLetterWarrant(subSlipDto);

		if (response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);
		} else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/getSubSlipBySalesGrpId")
	public ResponseEntity<List<SubSlipDto>> getSubSlipStatusBySalesGrpId(
			 @RequestParam(required = true) Integer salesGrpId, @RequestParam(required = false) List<Integer> statusIds) {
		try {
			List<SubSlipDto> subSlipDtos = subSlipService.getSubSlipsBySalesGrpId(salesGrpId, null, statusIds);
			return ResponseEntity.ok(subSlipDtos);
		} catch (Exception e) {
			 logger.error("Error in retrieving sub-slips for salesGrpId: {}", salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	
	@GetMapping("/getSubSlipBySalesGrpIdEmpId")
	public ResponseEntity<List<SubSlipDto>> getSubSlipBySalesGrpIdEmpId(
			 @RequestParam(required = true) Integer salesGrpId,  @RequestHeader(name = "Authorization") String token, @RequestParam(required = false) List<Integer> statusIds) {
		Integer empId = appSecurity.getEmpIdFromJwtToken(token);
		try {
			List<SubSlipDto> subSlipDtos = subSlipService.getSubSlipBySalesGrpIdEmpId(salesGrpId, null, empId, statusIds);
			return ResponseEntity.ok(subSlipDtos);
		} catch (Exception e) {
			 logger.error("Error in getSubSlipBySalesGrpIdEmpId for SalesGrpId: {}", salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getSubSlipBySalesGrpIdForDOA")
	public ResponseEntity<List<SubSlipDto>> getSubSlipBySalesGrpIdForDOA( @RequestParam(required = true) String roleId, @RequestParam(required = true) Integer salesGrpId, @RequestParam(required = false) List<Integer> statusIds){
		try {
			List<SubSlipDto> subSlipDtos = subSlipService.getSubSlipBySalesGrpIdForDOA(roleId, salesGrpId, statusIds);
			return ResponseEntity.ok(subSlipDtos);
		} catch (Exception e) {
			logger.error("Error in getSubSlipBySalesGrpIdForDOA for RoleId: {} and SalesGrpId: {}", roleId, salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	

	@GetMapping("/getSubSlipBySalesGrpWarrantId")
	public ResponseEntity<List<SubSlipDto>> getSubSlipBySalesGrpWarrantId(
			 @RequestParam(required = true) Integer salesGrpId, @RequestParam Integer warrantId, @RequestParam(required = false) List<Integer> statusIds) {
		try {
			List<SubSlipDto> subSlipDtos = subSlipService.getSubSlipsBySalesGrpId(salesGrpId, warrantId, statusIds);
			return ResponseEntity.ok(subSlipDtos);
		} catch (Exception e) {
			 logger.error("Error retrieving sub-slips for SalesGrpId: {} and WarrantId: {}", salesGrpId, warrantId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getSubSlip/warrant/{warrantId}")
	public ResponseEntity<List<SubSlipDto>> getSubSlipStatusByWarrantId(@PathVariable Integer warrantId ) {
		try {
			List<SubSlipDto> subSlipDtos = subSlipService.getSubSlipsByWarrantId(warrantId);
			return ResponseEntity.ok(subSlipDtos);
		} catch (Exception e) {
			  logger.error("Failed to fetch sub-slips for warrantId: {}", warrantId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	
	
	@GetMapping("/getAllottedSubSlipsByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getAllottedSubSlipsByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = subSlipService.getAllottedSubSlipsByStatusForFirm(orgId,empId);
			Collections.sort(offerLetterDtos, Comparator.comparing(OfferLetterDto::getId).reversed());
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
		    logger.error("Error fetching allotted sub-slips for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}
	

	@GetMapping("/getAllottedSubSlipsApprovalByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getAllottedSubSlipsApprovalByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = subSlipService.getAllottedSubSlipsApprovalByStatusForFirm(orgId,empId);
//			Collections.sort(offerLetterDtos, Comparator.comparing(OfferLetterDto::getId).reversed());
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Error retrieving approval sub-slips for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}
	
    /**
     * @author Shridhar R
     * get All Inotes List By InspOrgId service	
     */
    
    @GetMapping("/getAllSubSlipListByInspOrgId")
    public ResponseEntity<List<SubSlipDetByInspDto>> getAllSubSlipListByInspOrgId() {
        try {
        	List<SubSlipDetByInspDto> olDto = subSlipService.getAllSubSlipListByInspOrgId();
            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("Error occurred while fetching all sub-slips for Inspection Org {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/getAllPassedSubSlipListByInspOrgId")
    public ResponseEntity<List<SubSlipDetByInspDto>> getAllPassedSubSlipListByInspOrgId(@RequestHeader(name = "Authorization")String token) throws Exception {
        try {
        	int empId = appSecurity.getEmpIdFromJwtToken(token);
        	List<SubSlipDetByInspDto> olDto = subSlipService.getAllPassedSubSlipListByInspOrgId(empId);
            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("Error retrieving passed sub-slip list: {}", e.getMessage()); 
            return ResponseEntity.internalServerError().body(null);
        }
    }

    //Code to be changed
    @GetMapping("/getLineItemsByid/{sslipId}")
	public ResponseEntity<List<SubSlipLineItemDto>> getLineItemsByOl(@PathVariable int sslipId) {

		List<SubSlipLineItemDto> olLineItemDtos = subSlipService.getLineItemsByOl(sslipId);
		return ResponseEntity.ok(olLineItemDtos);
	}
    
    @GetMapping("/subSlipLineItem/{lineItemId}/registers")
    public ResponseEntity<?> getRegisterNosByOlLineItemId(@PathVariable Integer lineItemId) {
        try {
            List<SubSlipSerialNoDto> response = subSlipService.getRegisterNosBySubSlipLineItemId(lineItemId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasAnyAuthority('HOS','GIC')")
    @GetMapping("/deleteSubSlip/{id}")
    public ResponseEntity<Object> deleteSubSlip(@PathVariable int id) {
        try {
            subSlipService.deleteSubSlip(id);
            return ResponseEntity.ok(ApplicationConstants.SUCCESS);
        } catch (Exception e) {
            logger.error("/subSlip/deleteSubSlip Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to delete full record: " + e.getMessage());
        }
    }
    
    /**
     * Edit Submission Slip	
     * 
     * @author Chirag Swamy
     * @since 10/04/2026
     * 
     */
    @PreAuthorize("hasAnyAuthority('HOS','GIC')")
    @PostMapping("/update")
    public ResponseEntity<?> updateOfferLetter(@RequestBody SubSlipDto subSlipDto, @RequestHeader(name = "Authorization") String token) {
        Integer empId = appSecurity.getEmpIdFromJwtToken(token);
        subSlipDto.setEmployeeId(empId);
        
        Map<Boolean, String> response = subSlipService.updateOfferLetterWarrant(subSlipDto);
        
        if (response.containsKey(true)) {
            return new ResponseEntity<>(response.get(true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response.get(false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
