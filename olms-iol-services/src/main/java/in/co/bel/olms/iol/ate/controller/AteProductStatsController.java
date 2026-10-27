package in.co.bel.olms.iol.ate.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.iol.ate.service.AteStatsService;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ate/stats")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AteProductStatsController {

	private final AteStatsService ateStatsService;
	
	@GetMapping("/prod/{productId}")
	public ResponseEntity<String> getStats(
			@PathVariable(name = "productId") Integer productId,
			@RequestParam(required = true) @DateTimeFormat(pattern = StringLiteralConstants.DATE_FORMAT)  Date fromDate,
			@RequestParam(required = true) @DateTimeFormat(pattern = StringLiteralConstants.DATE_FORMAT)  Date toDate){
		try {
			String resp = ateStatsService.getStatsByProdId(productId, fromDate, toDate);
			return ResponseEntity.ok(resp);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
}
