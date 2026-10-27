package in.co.bel.olms.transaction.controller;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.EnvTestReviewDto;
import in.co.bel.olms.transaction.entity.EnvTestReview;
import in.co.bel.olms.transaction.service.EnvTestReviewService;
import lombok.RequiredArgsConstructor;
import in.co.bel.olms.constants.ApplicationConstants;

@RestController
@RequestMapping("/envTestReview")
@RequiredArgsConstructor
public class EnvTestReviewController {

	private final EnvTestReviewService envTestReviewService;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(EnvTestReviewController.class);
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<EnvTestReviewDto> getEnvTestReviewById(@PathVariable int id) throws DataNotFoundException {

		EnvTestReview envTestReview = envTestReviewService.getEnvTestReviewById(id);
		EnvTestReviewDto envTestReviewDto = mapper.map(envTestReview, EnvTestReviewDto.class);
		return ResponseEntity.ok(envTestReviewDto);
	}

	/**
	 * 
	 * @param envTestReviewDto
	 * @return
	 */
	@PostMapping("/saveEnvTestReview")
	public ResponseEntity<String> saveEnvTestReview(@RequestBody @Valid EnvTestReviewDto envTestReviewDto) {
		try {
			envTestReviewService.saveEnvTestReview(envTestReviewDto);
			return new ResponseEntity<>(ApplicationConstants.SUCCESS, HttpStatus.CREATED);
		} catch (DataNotValidException e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
