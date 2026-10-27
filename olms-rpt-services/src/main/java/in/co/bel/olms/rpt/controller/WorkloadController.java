package in.co.bel.olms.rpt.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.rpt.dto.WorkLoadDto;
import in.co.bel.olms.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Manasa B
 * @since 02-05-2023
 */

@RestController
@RequestMapping("/workload")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j 
@RequiredArgsConstructor
public class WorkloadController {

	private final WorkloadService workLoadService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
	@GetMapping("/getByDates/{from}/{end}")
	public ResponseEntity<List<WorkLoadDto>> getByDate(@PathVariable String from, @PathVariable String end){
		try {
			List<WorkLoadDto> dtos = workLoadService.getWorkloadReportsbyDates(sdf.parse(from), sdf.parse(end));
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			log.error("Unexpected error fetching workload reports: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
}
