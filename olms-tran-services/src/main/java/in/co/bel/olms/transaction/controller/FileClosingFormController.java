package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.FileClosingFormDetDto;
import in.co.bel.olms.transaction.dto.FileClosingFormDto;
import in.co.bel.olms.transaction.entity.FileClosingForm;
import in.co.bel.olms.transaction.entity.FileClosingFormDet;
import in.co.bel.olms.transaction.service.so.FileClosingFormService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */
@RestController
@RequestMapping("/fileClosingForm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FileClosingFormController {

	private final FileClosingFormService fileClosingFormService;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(FileClosingFormController.class);

	/**
	 * 
	 * @param fileClosingFormDto
	 * @return
	 */
	@PostMapping("/saveFileClosingForm")
	public ResponseEntity<FileClosingFormDto> saveFileClosingForm(@RequestBody @Valid FileClosingFormDto fileClosingFormDto) { 	
		try {
			FileClosingForm fileClosingForm = fileClosingFormService.saveFileClosingForms(fileClosingFormDto);
			fileClosingFormDto = fileClosingFormService.convertBoToentity(fileClosingForm);			
			FileClosingFormDto dto = new FileClosingFormDto();
			List<FileClosingFormDetDto> fileClosingFormDetDtos = new ArrayList<>();
			dto = mapper.map(fileClosingForm, FileClosingFormDto.class);
			for (FileClosingFormDet fileDet : fileClosingForm.getfileclosingFormDets()) {
				FileClosingFormDetDto detDto = new FileClosingFormDetDto();
				ChecklistFileClose checkfileclose = fileDet.getChecklistFileClose();
				detDto.setId(fileDet.getId());
				detDto.setChecklistId(checkfileclose.getId());
				detDto.setSlNo(checkfileclose.getSlNo());
				detDto.setReference(checkfileclose.getReference());
				if (!StringUtils.isNotBlank(fileDet.getRemarks())) {

					detDto.setRemarks("");
				} else {

					detDto.setRemarks(fileDet.getRemarks());
				}
				detDto.setObsType(checkfileclose.getObsType());
				detDto.setObsSts(fileDet.getObsSts());
				detDto.setSplit(checkfileclose.getSplit());

				fileClosingFormDetDtos.add(detDto);
			}
			Collections.sort(fileClosingFormDetDtos, new Comparator<FileClosingFormDetDto>() {
				@Override
				public int compare(FileClosingFormDetDto o1, FileClosingFormDetDto o2) {
					return o1.getChecklistId().compareTo(o2.getChecklistId());
				}
			});
			dto.setDetDtos(fileClosingFormDetDtos);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(" Save FileClosingForm Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new FileClosingFormDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Save FileClosingForm Error: {} ", e.getMessage());
			return new ResponseEntity<>(new FileClosingFormDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 
	 * @param fileClosingFormDto
	 * @return
	 */
	//TODO: code for submit to be added
	@PostMapping("/submitFileClosingForm")
	public ResponseEntity<FileClosingFormDto> submitFileClosingForm(@RequestBody @Valid FileClosingFormDto fileClosingFormDto) {
		
		try {
			FileClosingForm fileClosingForm = fileClosingFormService.submitFile(fileClosingFormDto);
			FileClosingFormDto dto = new FileClosingFormDto();
			List<FileClosingFormDetDto> fileDetDtos = new ArrayList<>();
			dto = mapper.map(fileClosingForm, FileClosingFormDto.class);
			for (FileClosingFormDet fileDet : fileClosingForm.getfileclosingFormDets())
			{
				FileClosingFormDetDto detDto = new FileClosingFormDetDto();
				ChecklistFileClose cf = fileDet.getChecklistFileClose();
				detDto.setId(fileDet.getId());
				detDto.setChecklistId(cf.getId());
				detDto.setSlNo(cf.getSlNo());
				detDto.setReference(cf.getReference());
				if (!StringUtils.isNotBlank(fileDet.getRemarks())) {
				} else {
					detDto.setRemarks(fileDet.getRemarks());
				}
				detDto.setObsType(cf.getObsType());
				detDto.setObsSts(fileDet.getObsSts());
				detDto.setSplit(cf.getSplit());
				fileDetDtos.add(detDto);
			}
			Collections.sort(fileDetDtos, new Comparator<FileClosingFormDetDto>() 
			{
				@Override
				public int compare(FileClosingFormDetDto o1, FileClosingFormDetDto o2) 
				{
					return o1.getChecklistId().compareTo(o2.getChecklistId());
				}
			});

			dto.setDetDtos(fileDetDtos);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Submit FileClosingForm Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new FileClosingFormDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Submit FileClosingForm Error: {} ", e.getMessage());
			return new ResponseEntity<>(new FileClosingFormDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/so/{soId}")
	public ResponseEntity<FileClosingFormDto> getByClosingForm(@PathVariable int soId) throws DataNotFoundException {
		FileClosingFormDto fcm = fileClosingFormService.getFileCloseFormById(soId);
		return ResponseEntity.ok(fcm);
	}
}
