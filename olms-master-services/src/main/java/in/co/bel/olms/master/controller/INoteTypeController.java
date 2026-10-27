package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.INoteTypeDto;
import in.co.bel.olms.master.dto.SupplierDto;
import in.co.bel.olms.master.entity.INoteType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.INoteTypeService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/iNoteType")
@RequiredArgsConstructor
public class INoteTypeController {

	private final INoteTypeService iNoteTypeService;

	private final Mapper mapper;

	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<INoteTypeDto>> getAllINoteTypes() {

		List<INoteTypeDto> iNoteTypeDtoList = new ArrayList<>();
		List<INoteType> iNoteTypes = iNoteTypeService.getAllINoteTypes();
		for (INoteType iNoteType : iNoteTypes) {
			INoteTypeDto iNoteTypeDto = mapper.map(iNoteType, INoteTypeDto.class);
			iNoteTypeDtoList.add(iNoteTypeDto);
		}
		return ResponseEntity.ok(iNoteTypeDtoList);

	}

	
	
	/**
	 * 
	 * @param iNoteTypeDto
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<INoteTypeDto> saveINoteType(@RequestBody @Valid SupplierDto iNoteTypeDto) {

		INoteType savedINoteType = iNoteTypeService.saveINoteType(iNoteTypeDto);
		INoteTypeDto savedINoteTypeDto = mapper.map(savedINoteType, INoteTypeDto.class);
		return new ResponseEntity<>(savedINoteTypeDto, HttpStatus.CREATED);

	}

	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<INoteTypeDto> getINoteType(@PathVariable int id) throws DataNotFoundException {

		INoteType iNoteType = iNoteTypeService.getINoteType(id);
		INoteTypeDto iNoteTypeDto = mapper.map(iNoteType, INoteTypeDto.class);
		return ResponseEntity.ok(iNoteTypeDto);

	}

}
