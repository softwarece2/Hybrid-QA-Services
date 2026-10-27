package in.co.bel.olms.transaction.controller;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.FagEndNoticeDto;
import in.co.bel.olms.transaction.entity.FagEndNotice;
import in.co.bel.olms.transaction.service.so.FagEndNoticeService;
import lombok.RequiredArgsConstructor;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 
 * @author Prateek Dwivedi
 * @since 13-05-2024
 *
 */

@RestController
@RequestMapping("/fagEndNotice")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FagEndNoticeController {

	private final FagEndNoticeService fagEndNoticeService;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(FagEndNoticeController.class);
	
	/**
	 * 
	 * @param fagEndNoticeDto
	 * @return
	 */
	@PostMapping("/saveFagEndNotice")
	public ResponseEntity<FagEndNoticeDto> saveFagEndNotice(@RequestBody @Valid FagEndNoticeDto fagEndNoticeDto) {
		try {
			FagEndNotice fagEndNotice =fagEndNoticeService.saveFagEndNotice(fagEndNoticeDto);
			fagEndNoticeDto = mapper.map(fagEndNotice, FagEndNoticeDto.class);
			return new ResponseEntity<>(fagEndNoticeDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save fagEndNotice error: {} ", ex.getMessage());
			return new ResponseEntity<>(new FagEndNoticeDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save fagEndNotice error: {} ", e.getMessage());
			return new ResponseEntity<>(new FagEndNoticeDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getFagEndNotice/{olid}")
	public ResponseEntity<FagEndNoticeDto> getBySupplyOrder(@PathVariable int olid) throws DataNotFoundException {
		FagEndNoticeDto fagEndNotice = fagEndNoticeService.getScrutinyFagEndNoticeBySoId(olid);
		return ResponseEntity.ok(fagEndNotice);
	}
	
	@PostMapping("/submit")
	public ResponseEntity<FagEndNoticeDto> submitFagEndNotice(@RequestBody @Valid FagEndNoticeDto fagEndNoticeDto) {
		try {		
			FagEndNotice fagEndNotice = fagEndNoticeService.submitFagEndNotice(fagEndNoticeDto);
			fagEndNoticeDto = mapper.map(fagEndNotice, FagEndNoticeDto.class);
			return new ResponseEntity<>(fagEndNoticeDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error("submit FagEndNotice Error :{}", ex.getMessage());
			return new ResponseEntity<>(new FagEndNoticeDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("submit FagEndNotice Error :{}", e.getMessage());
			return new ResponseEntity<>(new FagEndNoticeDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
