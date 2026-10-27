package in.co.bel.olms.iol.ate.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

import com.google.gson.Gson;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.iol.ate.dto.InspectionRoundDto;
import in.co.bel.olms.iol.ate.dto.ProdDto;
import in.co.bel.olms.iol.ate.dto.ProductTestMapDto;
import in.co.bel.olms.iol.ate.dto.SubmittedTestsDto;
import in.co.bel.olms.iol.ate.dto.TestInfoDto;
import in.co.bel.olms.iol.ate.service.InspectionRoundService;
import in.co.bel.olms.iol.ate.service.Tvne1paService;
import in.co.bel.olms.iol.jwt.JwtUtils;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import in.co.bel.olms.iol.constants.StringLiteralConstants;

@RestController
@RequestMapping("/ate")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AteProductController {

	private static final Logger logger = LoggerFactory.getLogger(AteProductController.class);

	private final Tvne1paService tvne1paService;

	private final InspectionRoundService inspectionRoundService;

	private final HmisAppSecurity hmisAppSecurity = new HmisAppSecurity();

	private final JwtUtils jwtUtils;

	Gson gson = new Gson();
	
	public static final String DTO_IS_NULL = "Dto is null";

	@PostMapping("/save")
	public ResponseEntity<String> saveAte(@RequestBody InspectionRoundDto dto) {
		try {
			 return tvne1paService.saveTvne1pa(dto);
		} catch (DataNotFoundException e) {
			logger.error("saveAte IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to save ATE", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save");
		}
	}

	@PostMapping("/submit")
	public ResponseEntity<String> submitAte(@RequestBody InspectionRoundDto dto,
			@RequestHeader(name = "Authorization") String token) throws Exception {
		try {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		int submittedById = jwtUtils.getEmpIdFromJwtToken(map);
		dto.setSubmittedById(submittedById);
		Boolean isQa = inspectionRoundService.isQaEmployee(submittedById);
		if (isQa) {
			isQa = true;
		} else {
			isQa = false;
		}
		dto.setIsQa(isQa);
		
		 InspectionRoundDto result = tvne1paService.submitTvne1pa(dto);
		 return ResponseEntity.ok(gson.toJson(result));
		}catch (RuntimeException e) {
	        logger.error("Submit test Error: {} | Cause: {}", e.getMessage(), e.getCause());

	        // If IllegalArgumentException, return 400 (User error)
	        // Otherwise, return 500 (System error)
	        HttpStatus status = (e.getCause() instanceof IllegalArgumentException) 
	                            ? HttpStatus.BAD_REQUEST 
	                            : HttpStatus.INTERNAL_SERVER_ERROR;

	        return ResponseEntity.status(status).body(e.getMessage());

	    } catch (Exception e) {
	        logger.error("Unexpected system error", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
	    }
	}

	@GetMapping("/getAteByDevIdProdIdTestId/{devId}/{prodId}/{testId}/{roundId}")
	public ResponseEntity<String> getAteByDevIdProdIdTestId(@PathVariable Integer devId, @PathVariable Integer prodId,
			@PathVariable Integer testId, @PathVariable Integer roundId,
                                                          @RequestHeader(name = "Authorization") String token,
                                                          @RequestParam(name = "olLiId", required = true) Integer olLiId) throws DataNotFoundException {


		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		if (isQa) {
			isQa = true;
		} else {
			isQa = null;
		}
		InspectionRoundDto dto = tvne1paService.getTvne1paByDevId(devId, prodId, testId, roundId,
        isQa, null, olLiId);
		return ResponseEntity.ok(gson.toJson(dto));
	}

	@GetMapping("/getAteByDevIdProdIdTestId/v2/{devId}/{prodId}/{testId}/{roundId}")
	public ResponseEntity<String> getAteByDevIdProdIdTestIdv2(@PathVariable Integer devId, @PathVariable Integer prodId,
			@PathVariable Integer testId, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token,
                                                            @RequestParam(name = "olLiId", required = false) Integer olLiId,
                                                            @RequestParam(name = "qa", required = false) Boolean qa)
      throws DataNotFoundException {

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);
//	  Integer roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);
		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		if ((qa != null && !qa) || !isQa) {
			isQa = null;
		}

		InspectionRoundDto dto = tvne1paService.getTvne1paByDevId(devId, prodId, testId, roundId,
        isQa, qa, olLiId);

		return ResponseEntity.ok(gson.toJson(dto));
	}

	@GetMapping("/getRoundId/{devId}/{prodId}/{olLineItem}")
	public ResponseEntity<List<InspectionRoundDto>> getRoundId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem,
			@RequestHeader(name = "Authorization") String token) throws DataNotFoundException {

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		List<InspectionRoundDto> dto = inspectionRoundService.getRoundId(devId, prodId, olLineItem, empId);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/getNextRoundId/{devId}/{prodId}/{lineItemId}/{isQa}")
	public ResponseEntity<List<InspectionRoundDto>> getNextRoundId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer lineItemId, @PathVariable Boolean isQa,
			@RequestHeader(name = "Authorization") String token) {
		try {
			if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
				token = token.substring(7);
			}
			Map<String, Object> map = jwtUtils.parseToken(token);
			Integer empId = jwtUtils.getEmpIdFromJwtToken(map);
			boolean isQaPrimitive = Boolean.TRUE.equals(isQa);
			if (isQaPrimitive) {
				List<InspectionRoundDto> dto = inspectionRoundService.getNextRoundId(devId, prodId, lineItemId, empId);
				if (dto == null) {
					logger.info(DTO_IS_NULL);
					return ResponseEntity.notFound().build();
				}
				return ResponseEntity.ok(dto);
			} else {
				List<InspectionRoundDto> dto = inspectionRoundService.getNextRoundIdBySsLiId(devId, prodId, lineItemId,
						empId);
				if (dto == null) {
					logger.info(DTO_IS_NULL);
					return ResponseEntity.notFound().build();
				}
				return ResponseEntity.ok(dto);
			}
		} catch (DataNotFoundException e) {
			logger.error("Data not found: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Unexpected error in getNextRoundId", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getNextRoundId/{devId}/{prodId}/{olLineItem}")
	public ResponseEntity<List<InspectionRoundDto>> getNextRoundId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem,
			@RequestHeader(name = "Authorization") String token) {
		try {
			if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
				token = token.substring(7);
			}
			Map<String, Object> map = jwtUtils.parseToken(token);
			Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

			List<InspectionRoundDto> dto = inspectionRoundService.getNextRoundId(devId, prodId, olLineItem, empId);
			if (dto == null) {
				logger.info(DTO_IS_NULL);
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(dto);
		} catch (DataNotFoundException e) {
			logger.error("Data not found in getNextRoundId: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			 logger.error("Unexpected error in getNextRoundId", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getNextRoundId/{devId}/{prodId}/subSlipLineItem/{ssLiId}")
  public ResponseEntity<Object> getNextRoundIdBySsLiId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer ssLiId,
			@RequestHeader(name = "Authorization") String token) {
		try {
      int empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
			List<InspectionRoundDto> dto = inspectionRoundService.getNextRoundIdBySsLiId(devId, prodId, ssLiId, empId);
			return ResponseEntity.ok(dto);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	// Add new service to getListofRoundIds
	// @GetMapping("/getListofRoundIds/{devId}/{prodId}/{lineItemiId}/{isQa}")
	// if ( isQA)
	// Call "/getListofRoundIds/{devId}/{prodId}/{olLineItem}")
	// else
	// call ("/getListofRoundIds/{devId}/{prodId}/subSlipLineItem/{ssLiId}")
	@GetMapping("/getListofRoundIds/{devId}/{prodId}/{lineItemId}/{isQa}")
	public ResponseEntity<List<InspectionRoundDto>> getListofRounds(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer lineItemId, @PathVariable Boolean isQa,
			@RequestHeader(name = "Authorization") String token) {
		try {
			if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
				token = token.substring(7);
			}
			Map<String, Object> map = jwtUtils.parseToken(token);
			Integer empId = jwtUtils.getEmpIdFromJwtToken(map);
			boolean isQaPrimitive = Boolean.TRUE.equals(isQa);
			if (isQaPrimitive) {
				List<InspectionRoundDto> dto = inspectionRoundService.getListofRoundIds(devId, prodId, lineItemId,
						empId, isQa);
				if (dto == null) {
					logger.info(DTO_IS_NULL);
					return ResponseEntity.notFound().build();
				}
				return ResponseEntity.ok(dto);
			} else {
				List<InspectionRoundDto> dto = inspectionRoundService.getListofRoundIdsBySSlipLineItem(devId, prodId,
						lineItemId, empId);
				if (dto == null) {
					logger.info(DTO_IS_NULL);
					return ResponseEntity.notFound().build();
				}
				return ResponseEntity.ok(dto);
			}
		} catch (DataNotFoundException e) {
			logger.error("Data not found in getListofRounds: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Unexpected error in getListofRounds", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getListofRoundIds/{devId}/{prodId}/{olLineItem}")
	public ResponseEntity<List<InspectionRoundDto>> getListofRoundIds(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem,
			@RequestHeader(name = "Authorization") String token) {
		try {
			if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
				token = token.substring(7);
			}
			Map<String, Object> map = jwtUtils.parseToken(token);
			Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

			List<InspectionRoundDto> dto = inspectionRoundService.getListofRoundIds(devId, prodId, olLineItem, empId, null);
			if (dto == null) {
				logger.info(DTO_IS_NULL);
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(dto);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getListofRoundIds/{devId}/{prodId}/subSlipLineItem/{ssLiId}")
	public ResponseEntity<List<InspectionRoundDto>> getListofRoundIdsBySslipLineItem(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer ssLiId,
			@RequestHeader(name = "Authorization") String token) {
		try {
			if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
				token = token.substring(7);
			}
			Map<String, Object> map = jwtUtils.parseToken(token);
			Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

			List<InspectionRoundDto> dto = inspectionRoundService.getListofRoundIdsBySSlipLineItem(devId, prodId,
					ssLiId, empId);
			if (dto == null) {
				logger.info(DTO_IS_NULL);
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(dto);
		} catch (DataNotFoundException e) {
			logger.error("Data not found : {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Unexpected error ", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	/**
	 * @ @author Shridhar R get all rounds when isQa is null
	 */

	@GetMapping("/getListofRoundIdsQa/{devId}/{prodId}/{olLineItem}")
	public ResponseEntity<List<InspectionRoundDto>> getListofRoundIds(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem) {
		try {
			List<InspectionRoundDto> dto = inspectionRoundService.getListofRoundIdsQa(devId, prodId, olLineItem);
			if (dto == null) {
				logger.info(DTO_IS_NULL);
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(dto);
		} catch (DataNotFoundException e) {
			logger.error("Data not found in getListofRoundsQa: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			 logger.error("Unexpected error in getListofRoundsQa", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("getTestInfo/{prodId}/{devId}")
	public List<TestInfoDto> getTestInfo(@PathVariable Integer prodId, @PathVariable Integer devId)
			throws DataNotFoundException {
		return tvne1paService.getTestInfo(prodId, devId);
	}

	@GetMapping("/getAllTestListByPrdId/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<ProductTestMapDto>> getAllTestListByPrdId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token) throws DataNotFoundException {

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		if (isQa) {
			isQa = true;
		} else {
			isQa = null;
		}

		List<ProductTestMapDto> dto = inspectionRoundService.getAllTestListByPrdId(devId, prodId, olLineItem, roundId,
				isQa);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/getAllTestListByPrdId/{devId}/{prodId}/ssLiId/{ssLiId}/{roundId}")
	public ResponseEntity<List<ProductTestMapDto>> getAllTestListByPrdIdAndSsLiId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer ssLiId, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token,
			@RequestParam(name = "showQa", required = false) Boolean qa) throws DataNotFoundException {

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		Boolean isQa = inspectionRoundService.isQaEmployee(empId);

		if ((qa != null && !qa) || !isQa) {
			isQa = null;
		}

		List<ProductTestMapDto> dto = inspectionRoundService.getAllTestListByPrdIdAndSsLiid(devId, prodId, ssLiId,
				roundId, isQa);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/getAllTestListByPrdIdIsFai/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<ProductTestMapDto>> getAllTestListByPrdIdIsFai(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token) throws DataNotFoundException {

		List<ProductTestMapDto> dto = inspectionRoundService.getAllTestListByPrdIdIsFai(devId, prodId, olLineItem,
				roundId);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	/**
	 * @ @author Shridhar R get all test list when QA is null
	 */

	@GetMapping("/getAllTestListByPrdIdQa/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<ProductTestMapDto>> getAllTestListByPrdId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId)
			throws DataNotFoundException {

		List<ProductTestMapDto> dto = inspectionRoundService.getAllTestListByPrdIdQa(devId, prodId, olLineItem,
				roundId);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}
	
	
	/**
	 * @ @author Shridhar R get all test list in QA filtered by device id sampled or other than sample
	 */
	@GetMapping("/getAllTestListInQa/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<ProductTestMapDto>> getAllTestListInQa(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token) throws DataNotFoundException {

		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		if (isQa) {
			isQa = true;
		} else {
			isQa = null;
		}

		List<ProductTestMapDto> dto = inspectionRoundService.getAllTestListInQa(devId, prodId, olLineItem, roundId,
				isQa);
		if (dto == null) {
			logger.info(DTO_IS_NULL);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	/**
	 * @ @author Shridhar R print service for getting all spec and its obs values
	 */

	@GetMapping("/getAllTestValByProdIdDevId/{devId}/{prodId}/{roundId}")
	public ResponseEntity<ProdDto> getAllTestValByProdIdDevId(@PathVariable Integer devId, 
			@PathVariable Integer prodId,
			@PathVariable Integer roundId, @RequestHeader(name = "Authorization") String token,
			@RequestParam(name = "showQa", required = false) Boolean showQa, // for qc user to view QA results
			@RequestParam(name = "showQc", required = false) Boolean showQc  // for qa user to view QC results
			)  
	
			throws DataNotFoundException {
		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

	    Boolean employeeIsQa = inspectionRoundService.isQaEmployee(empId);
	    
	    Boolean isQa = null;
	    
	    if (Boolean.TRUE.equals(showQc)) {
	    	isQa = false;
	    } else if (Boolean.TRUE.equals(employeeIsQa) || Boolean.TRUE.equals(showQa)) {
	        isQa = true;
	    }
		
		ProdDto prodDto = tvne1paService.getAllTestValByProdIdDevId(devId, prodId, roundId, isQa);
		return ResponseEntity.ok(prodDto);
	}

	/**
	 * @ @author Shridhar R print service for getting all spec and its obs values
	 */

	@GetMapping("/getAllSubmittedTestStatusByProdIdDevId/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<SubmittedTestsDto>> getAllSubmittedTestStatusByProdIdDevId(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId,
			@RequestHeader(name = "Authorization") String token,
			@RequestParam(name = "showQa", required = false) Boolean showQa,
			@RequestParam(name = "qa", required = false) Boolean qa) throws DataNotFoundException {
		if (token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		Integer empId = jwtUtils.getEmpIdFromJwtToken(map);

		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		if ((showQa != null && !showQa) || !isQa)
			isQa = null;

		List<SubmittedTestsDto> submittedTestsDto = null;
		submittedTestsDto = inspectionRoundService.getAllSubmittedTestStatusByProdIdDevId(devId, prodId, olLineItem,
				roundId, isQa, showQa, qa);

		return ResponseEntity.ok(submittedTestsDto);
	}

	/**
	 * @ @author Shridhar R print service for getting all spec and its obs values
	 *   for isQa null
	 */

	@GetMapping("/getAllSubmittedTestStatusByProdIdDevIdQa/{devId}/{prodId}/{olLineItem}/{roundId}")
	public ResponseEntity<List<SubmittedTestsDto>> getAllSubmittedTestStatusByProdIdDevIdQa(@PathVariable Integer devId,
			@PathVariable Integer prodId, @PathVariable Integer olLineItem, @PathVariable Integer roundId)
			throws DataNotFoundException {
		List<SubmittedTestsDto> submittedTestsDto = null;
		submittedTestsDto = inspectionRoundService.getAllSubmittedTestStatusByProdIdDevIdQa(devId, prodId, olLineItem,
				roundId);

		return ResponseEntity.ok(submittedTestsDto);
	}

}
