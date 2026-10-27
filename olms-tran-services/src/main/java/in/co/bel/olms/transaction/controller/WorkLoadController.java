package in.co.bel.olms.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.WorkLoadDto;
import in.co.bel.olms.transaction.service.WorkLoadService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 08-02-2023
 */

@RestController
@RequestMapping("/workload")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WorkLoadController {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkLoadController.class);

	private final WorkLoadService workLoadService;
	
	/**
	 * 
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<WorkLoadDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		try {
			WorkLoadDto workLoad = workLoadService.getWorkLoadBySoId(soid);
			return ResponseEntity.ok(workLoad);
		}catch(DataNotFoundException e) {
			 logger.warn("Workload data not found for SO ID {}: {}", soid, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			 logger.error("Unexpected error retrieving workload for SO ID {}: ", soid, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	
}
