package in.co.bel.olms.transaction.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.transaction.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.SampleSelectionDto;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.service.LineItemLotsRegisterService;
import in.co.bel.olms.transaction.service.LineItemSerialNoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/lineItemLots/register")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class LineItemLotsRegisterController {

	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	private final LineItemLotsRegisterService lineItemLotsRegisterService;

	private final LineItemSerialNoService lineItemSerialNoService;

	private static final Logger logger = LoggerFactory.getLogger(LineItemLotsRegisterController.class);
	
	private static final String LOG_ERROR_SAVING_REGISTERS = "Error saving registers: {}";
	
	private static final String LOG_ERROR_UPDATING_REGISTERS = "Error in updating registers: {}";
	
	private static final String RESPONSE_SUCCESS = "success";
	// Saves register numbers for a line item lot.
	//@since 15-09-2026
	@PreAuthorize("hasAnyAuthority('PLANNER')")
    @PostMapping("/save")
    public ResponseEntity<?> saveRegisterNumbers(@RequestBody @Valid LineItemLotsRegisterDto requestDto, @RequestHeader("Authorization") String token) {
        try {
        	Integer empId = appSecurity.getEmpIdFromJwtToken(token);
        	requestDto.setEmpId(empId);
            String response = lineItemLotsRegisterService.saveRegisters(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
        	log.error("Validation/Argument error saving registers for EmpID context: {}", ex.getMessage());
			log.error(LOG_ERROR_SAVING_REGISTERS, ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("System error occurred while saving registers: ", e);
            log.error(LOG_ERROR_SAVING_REGISTERS + e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Gets register numbers by line item lot id.
    @GetMapping("/lineItemLot/{id}")
    public ResponseEntity<?> getRegisterNosByLineItemLotId(@PathVariable Integer id) {
        try {
            List<LineItemLotsRegister> response = lineItemLotsRegisterService.getRegisterNosByLineItemLotId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error("Illegal argument in fetching registers by lineItemLotId: {},error:{} ",id, ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error("Error in fetching registers by lineItemLotId: {},error:{} " ,id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
 // Gets register numbers by ol line item id.
    @GetMapping("/olLineItem/{id}")
    public ResponseEntity<?> getRegisterNosByOlLineItemId(@PathVariable Integer id) {
        try {
            List<LineItemLotsRegisterDto> response = lineItemLotsRegisterService.getRegisterNosByOlLineItemId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error("Illegal argument in getting registers by olLineItemId: {},error:{} ",id, ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error("Error fetching registers by olLineItemId: {},error:{}",id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Gets register numbers by ol line item id.
    @GetMapping("/ol/{id}")
    public ResponseEntity<?> getRegisterNosByOlId(@PathVariable Integer id) {
        try {
            List<LineItemLotsRegister> response = lineItemLotsRegisterService.getRegisterNosByOlId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument in getting registers by olId: {},error:{} ",id, ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in fetching registers by olId: {},error:{}" ,id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Gets register numbers by ol line item id for FAI.
    // All reg nos. for the first round, 'rfr' for subsequent rounds
    @GetMapping("/olLineItem/fai/{id}")
    public ResponseEntity<?> getFAIRegisterNosByOlLineItemId(@PathVariable Integer id) {
        try {
            List<LineItemLotsRegister> response = lineItemLotsRegisterService.getFAIRegisterNosByOlLineItemId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument in getting fai registers by olLineItemId: {},error:{} ",id, ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in fetching fai registers by olLineItemId: {},error:{} " ,id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Deletes a register number by its ID.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRegisterNumbers(@PathVariable Integer id) { 
        try {
            lineItemLotsRegisterService.deleteRegisterNumber(id);
            return new ResponseEntity<>(RESPONSE_SUCCESS, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument in deleting registers: {} ,error:{}",id, ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in deleting registers: {},error:{}" ,id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Deletes register numbers in bulk.
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<?> deleteRegisterNumbersInBulk(@RequestBody LineItemLotsRegisterDto requestDto) { 
        try {
            lineItemLotsRegisterService.deleteRegisterNumbersInBulk(requestDto.getIds());
            return new ResponseEntity<>(RESPONSE_SUCCESS, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument in deleting RegisterNumbers in bulk: {} ", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in deleting RegisterNumbers in bulk: {}" + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Updates the register number for a given ID.
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateRegisterNumber(@RequestBody @Valid LineItemLotsRegisterDto dto, @PathVariable Integer id) {
        try {
            String response = lineItemLotsRegisterService.updateRegisterNumber(id, dto.getRegisterNo());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error(LOG_ERROR_UPDATING_REGISTERS, ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error(LOG_ERROR_UPDATING_REGISTERS, e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Updates the registers (status, ol_line_item_id).
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid LineItemLotsRegisterDto requestDto) {
        try {
            String response = lineItemLotsRegisterService.update(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error(LOG_ERROR_UPDATING_REGISTERS, ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error(LOG_ERROR_UPDATING_REGISTERS + e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Updates the registers (status, ol_line_item_id).
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody @Valid LineItemLotsRegisterDto requestDto) {
        try {
            String response = lineItemLotsRegisterService.updateStatus(requestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Illegal argument in updating status: {} ", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in updating status: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //Retrieves register numbers by SO-Line Item ID.
    @GetMapping("/soLiId/{id}")
    public ResponseEntity<?> getRegisterNumbersBySoLiId(@PathVariable Integer id) {
        try {
            List<LineItemLotsRegister> response = lineItemLotsRegisterService.getRegisterNumbersBySoLiId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error("Illegal argument in fetching registers by SoLiId: {} ", ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error("Error fetching registers by SoLiId: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Gets register numbers by SO-Line Item ID and financial year.
    @GetMapping("/soLiId/{id}/{financialYear}")
    public ResponseEntity<?> getRegisterNosBySoLiIdAndFinancialYear(@PathVariable Integer id, @PathVariable String financialYear) {
        try {
            List<LineItemLotsRegister> response = lineItemLotsRegisterService.getRegisterNosBySoLiIdAndFinancialYear(id, financialYear);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error("Illegal argument in getting registers by SoLiId and FinancialYear: {} ", ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error("Error fetching registers by SoLiId and FinancialYear: {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Gets register numbers by SO-Line Item ID and financial year and groupBy year.
    @GetMapping("/soLiId/{id}/groupBy/{financialYear}")
    public ResponseEntity<?> getRegistersBySoLiIdAndFinancialYear(
    		@PathVariable Integer id, 
    		@PathVariable String financialYear,
    		@RequestParam(value = "status", defaultValue = "pending") String status
    		) {
        try {
            Map<String, List<LineItemLotsRegister>> response = lineItemLotsRegisterService.getRegistersBySoLiIdAndFinancialYear(id, financialYear, status);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Gets financial years for SO-Line Item ID.
    @GetMapping("/soLiId/{id}/financialYears")
    public ResponseEntity<?> getFinancialYearsForSoLiId(@PathVariable Integer id) {
        try {
            List<String> response = lineItemLotsRegisterService.getFinancialYearsForSoLiId(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
			log.error("Illegal argument in getting financial years for soLiId: {} ", ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
            log.error("Error in getting financial years: {}" , e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getRegistrationNosByOlId/{olId}")
    public List<SampleSelectionDto> getSelectedRegistrationNosForOl(@PathVariable Integer olId) {
        return lineItemLotsRegisterService.getSelectedRegistrationNosForOl(olId);
    }
    
    @GetMapping("/getAllRegistrationNosByOlId/{olId}")
    public List<SampleSelectionDto> getAllRegistrationNosForOl(@PathVariable Integer olId) {
        return lineItemLotsRegisterService.getAllRegistrationNosForOl(olId);
    }
    
    @PostMapping("/saveSampleSelection")
    public ResponseEntity<List<LineItemLotsRegister>> saveSampleSelections(@RequestBody LineItemLotsRegisterDto registerDto) {
        try {
            List<LineItemLotsRegister> savedRegisters = lineItemLotsRegisterService.saveSampleSelection(registerDto);
            return new ResponseEntity<>(savedRegisters, HttpStatus.CREATED);
        } catch (Exception e) {
        	 logger.error("Error in saving sample selections: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/deleteSampleSelection") 
    public ResponseEntity<Void> deleteSampleSelection(@RequestParam Integer regId) {
        try {
        	lineItemLotsRegisterService.deleteSampleSelection(regId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } catch (Exception e) {
        	 logger.error("Error deleting sample selections: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/deleteMultipleSampleSelection") 
    public ResponseEntity<Void> deleteMultipleSampleSelection(@RequestBody List<LineItemLotsRegisterDto> lineDtos) {
        try {
        	lineItemLotsRegisterService.deleteMultipleSampleSelections(lineDtos);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } catch (Exception e) {
        	 logger.error("Error deleting mu;tiple sample selections: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
	@DeleteMapping("/deletetest/{id}")
	public ResponseEntity<?> deleteTest(@PathVariable Integer id) {
		try {
			lineItemLotsRegisterService.deleteTest(id);
			return new ResponseEntity<>(RESPONSE_SUCCESS, HttpStatus.OK);
		} catch (IllegalArgumentException ex) {
			log.error("Error fetching tests: {} ", ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Error in deleting tests: " + e.getMessage());
			log.error("Critical error during deletion of test ID {}: ", id, e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}   
	
	@GetMapping("/getAcceptedRegistrationNosByOlId/{olId}")
    public List<SampleSelectionDto> getAllAcceptedRegistrationNosForOl(@PathVariable Integer olId) {
        return lineItemLotsRegisterService.getAllAcceptedRegistrationNosForOl(olId);
    }
    
	
	 @GetMapping("/getProductAndRegNoByOlId/{olId}")
	    public List<SampleSelectionDto> getProductAndRegNoByOlId(@PathVariable Integer olId) {
	        return lineItemLotsRegisterService.getPartNoRegNoByOlId(olId);
	    }
	 @GetMapping("/getNonSampleProductAndRegNosByOlId/{olId}")
	    public List<SampleSelectionDto> getNonSampleProductAndRegNosByOlId(@PathVariable Integer olId) {
	        return lineItemLotsRegisterService.getNonSampleProductAndRegNosByOlId(olId);
	    }
	 @GetMapping("/getFaiSampleProductAndRegNosByOlId/{olId}")
	    public List<SampleSelectionDto> getFaiSampleProductAndRegNosByOlId(@PathVariable Integer olId) {
	        return lineItemLotsRegisterService.getFaiSampleProductAndRegNosByOlId(olId);
	    }
	 
	    //Retrieves register numbers by SO-Line Item ID.
	    @GetMapping("/soLiId/{id}/serialNos")
	    public ResponseEntity<Object> getLineitemSerialNosBySoLiId(@PathVariable Integer id) {
	        try {
	            List<LineItemSerialNoDto> response = lineItemLotsRegisterService.getLineitemSerialNosBySoLiId(id);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (IllegalArgumentException ex) {
	        	log.error("Invalid parameter provided for SoLiId {}: {}", id, ex.getMessage());
				log.error("Error fetching lineitemSerialNos by SoLiId: {} ", ex.getMessage());
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				log.error("Unexpected error retrieving serial numbers for SoLiId {}: ", id, e);
	            log.error("Error in getting lineitemSerialNos by SoLiId: " + e.getMessage());
	            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @PostMapping("/delete/serialNo/{id}")
		public ResponseEntity<String> deleteSerialNo(@PathVariable Integer id){
	    	 String response = lineItemLotsRegisterService.deleteSerialNo(id);

	         if (response.equals("Serial No. deleted succesfully")) {
	             return ResponseEntity.ok().body(response);  // 200 OK
	         } else if (response.equals("serial no. cannot be deleted after mapping ")) {
	             return ResponseEntity.status(409).body(response); // 409 Conflict
	         } else {
	             return ResponseEntity.badRequest().body(response); // 400 Bad Request (shouldn't happen with current service logic, but good to handle)
	         }
		}
	    
	    @GetMapping("/{lisnId}/history")
	    public ResponseEntity<Object> getHistory(@PathVariable Integer lisnId){
	    	try {
	    		List<LineItemSerialNoHistoryDto> historyDtos =  lineItemSerialNoService.getHistory(lisnId);
	    		return ResponseEntity.ok(historyDtos);
			} catch (Exception e) {
				return ResponseEntity.internalServerError().body(e.getMessage());
			}
	    }
	    
}
