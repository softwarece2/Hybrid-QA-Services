package in.co.bel.olms.rpt.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.rpt.dto.ReportsDto;
import in.co.bel.olms.service.ReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ReportsController {

	private final ReportsService reportsService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@GetMapping("/getReport/{from}/{end}/{roleId}")
	public ResponseEntity<List<ReportsDto>> getByDate(@PathVariable String from, @PathVariable String end, @PathVariable int roleId){
		try {
			List<ReportsDto> dtos = reportsService.getReportsDataByDates(sdf.parse(from), sdf.parse(end), roleId);
			return ResponseEntity.ok(dtos);
		}catch(Exception e) {
			log.error("Error occurred while fetching reports for roleId: {}. Error: {}", roleId, e.getMessage(), e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	
	
}
