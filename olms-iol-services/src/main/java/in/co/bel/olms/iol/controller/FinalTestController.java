package in.co.bel.olms.iol.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.iol.dto.FinalTestDto;
import in.co.bel.olms.iol.service.FinalTestService;
import in.co.bel.olms.master.exception.DataNotFoundException;

@RestController
@RequestMapping("/finalTest")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FinalTestController {
	Logger logger = LoggerFactory.getLogger(FinalTestController.class);

	@Autowired
	private FinalTestService finalTestService;

	@GetMapping("getFinalTestBytestId/{testId}")
	public ResponseEntity<FinalTestDto> getFinalTestBytestId(@PathVariable int testId) throws DataNotFoundException {
		FinalTestDto dto = finalTestService.getFinalTestBytestId(testId);
		return ResponseEntity.ok(dto);

	}

}
