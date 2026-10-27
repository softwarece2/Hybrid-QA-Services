package in.co.bel.olms.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.service.OfferLetterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coveringLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CoveringLetterController {
	
	private final OfferLetterService offerLetterService;
	
	private static final Logger logger =LoggerFactory.getLogger( CoveringLetterController.class);

	@PostMapping("/save")
	public ResponseEntity<String> saveINote(@RequestBody INoteDto iNoteDto) {
		try {
			String reponse = offerLetterService.saveINoteV2(iNoteDto);
			return ResponseEntity.ok(reponse);
		}catch(Exception e) {
			 logger.error("Error occurred while saving INote: {}", iNoteDto, e);
			return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
		}
	}
}
