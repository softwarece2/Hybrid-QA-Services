package in.co.bel.olms.iol.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.iol.dto.FaiProtocolHdrDto;
import in.co.bel.olms.iol.service.FaiProtocolService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/iolFai")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class IOLFaiProtocolController {

	private static final Logger logger = LoggerFactory.getLogger(IOLFaiProtocolController.class);

	private final FaiProtocolService faiProtocolService;
	
	@GetMapping("getIolFaiByTestId/{olId}")
	public ResponseEntity<FaiProtocolHdrDto> getIolAteTestByTestId(@PathVariable int olId)
			throws DataNotFoundException {
		FaiProtocolHdrDto dtos = faiProtocolService.getByApplicableTestId(olId);
		return ResponseEntity.ok(dtos);
	}
}
