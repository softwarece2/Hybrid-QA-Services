package in.co.bel.olms.master.controller;

import java.util.List;

import javax.validation.Valid;

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

import in.co.bel.olms.master.dto.DeptDropDto;
import in.co.bel.olms.master.dto.EpCodeDto;
import in.co.bel.olms.master.dto.PartNumberDto;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.PartNumberService;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/partNumber")
@RequiredArgsConstructor
public class PartNumberController {

	private final PartNumberService partNumberService;
	
	private static final Logger logger = LoggerFactory.getLogger(PartNumberController.class); 

	@PostMapping("/save")
	public ResponseEntity<PartNumberDto> savePartNumber(@RequestBody @Valid PartNumberDto partNumberDTO) {
		try {
		PartNumberDto savedPartNumber = partNumberService.save(partNumberDTO);
		return new ResponseEntity<>(savedPartNumber, HttpStatus.CREATED);
		} catch(RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		}
	

	@GetMapping("/getAll")
	public ResponseEntity<List<PartNumberDto>> getAllPartNumbers() {
		try {
			return new ResponseEntity<>(partNumberService.getAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error: {}", e.getMessage());
			return null;
		}
	}

	@GetMapping("/getAllActive")
	public ResponseEntity<List<PartNumberDto>> getAllActivePartNumbers() {
		try {
			return new ResponseEntity<>(partNumberService.getAllActive(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error: {}", e.getMessage());
			return null;
		}
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<PartNumberDto> getPartNumberById(@PathVariable Integer id) {
		try {
			PartNumberDto partNumberDTO = partNumberService.getById(id);
			if (partNumberDTO != null) {
				return new ResponseEntity<>(partNumberDTO, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (DataNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/update")
	public ResponseEntity<String> updatePartNumber(@RequestBody @Valid PartNumberDto partNumberDTO) {
		try {
			return ResponseEntity.ok(partNumberService.update(partNumberDTO));
		} catch (DataNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			logger.error("Error updating part number: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/getDetailsBypartNumberId/{partNumberId}")
	public ResponseEntity<List<EpCodeDto>> getDetailsBypartNumber(@PathVariable Integer partNumberId) {
		try {
			return ResponseEntity.ok(partNumberService.getDetailsBypartNumber(partNumberId));

		} catch (Exception e) {
			logger.error("Error updating part number: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	 @GetMapping("/getAllDepartmentNames/{partNumberId}")
		public ResponseEntity<List<DeptDropDto>> getAllDepartmentNames(@PathVariable Integer partNumberId) {
		try {
		 return ResponseEntity.ok(partNumberService.getAllDepartmentNames(partNumberId));
			} catch (Exception e) {
				logger.error("Error updating part number: {}", e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
}