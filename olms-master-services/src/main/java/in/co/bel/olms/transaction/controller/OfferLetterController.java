package in.co.bel.olms.transaction.controller;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.service.OfferLetterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/offerLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OfferLetterController {

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<OfferLetterDto> getOfferLetterById(@PathVariable int id) throws DataNotFoundException{
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(id);
		OfferLetterDto offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
		return ResponseEntity.ok(offerLetterDto);
	}
}
