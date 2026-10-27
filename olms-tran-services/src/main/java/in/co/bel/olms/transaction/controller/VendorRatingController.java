package in.co.bel.olms.transaction.controller;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.VendorRatingDto;
import in.co.bel.olms.transaction.entity.VendorRating;
import in.co.bel.olms.transaction.service.VendorRatingService;
import lombok.RequiredArgsConstructor;
import in.co.bel.olms.constants.ApplicationConstants;

@RestController
@RequestMapping("/vendorRating")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class VendorRatingController {

	private final VendorRatingService vendorRatingService;
	
	private static final Logger logger = LoggerFactory.getLogger(VendorRatingController.class);

	private final Mapper mapper;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<VendorRatingDto> getVendorRatingById(@PathVariable int id) throws DataNotFoundException {

		VendorRating vendorRating = vendorRatingService.getVendorRatingById(id);
		VendorRatingDto vendorRatingDto = mapper.map(vendorRating, VendorRatingDto.class);
		return ResponseEntity.ok(vendorRatingDto);
	}

	/**
	 * 
	 * @param vendorRatingDto
	 * @return
	 */
	@PostMapping("/saveVendorRating")
	public ResponseEntity<String> saveVendorRating(@RequestBody @Valid VendorRatingDto vendorRatingDto) {

		try {
			vendorRatingService.saveVendorRating(vendorRatingDto);
			return new ResponseEntity<>(ApplicationConstants.SUCCESS, HttpStatus.CREATED);
		} catch (DataNotValidException e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
