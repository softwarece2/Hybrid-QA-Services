package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.INoteTypeDto;
import in.co.bel.olms.master.dto.NatureOfInspectionDto;
import in.co.bel.olms.master.entity.NatureOfInspection;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.NatureOfInspectionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/natureOfInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class NatureOfInspectionController {

	private final NatureOfInspectionService natureOfInspectionService;

	private final Mapper mapper;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<NatureOfInspectionDto> getNatureOfInspectionById(@PathVariable int id) throws DataNotFoundException {
		
		NatureOfInspection natureOfInspection = natureOfInspectionService.getNatureOfInspectionById(id);
		NatureOfInspectionDto natureOfInspectionDto = mapper.map(natureOfInspection, NatureOfInspectionDto.class);
		return ResponseEntity.ok(natureOfInspectionDto);
		
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<INoteTypeDto>> getAllNatureOfInspection() {
		List<INoteTypeDto> natureOfInspectionDtoList = new ArrayList<>();
		List<NatureOfInspection> natureOfInspections = natureOfInspectionService.getAllNatureOfInspection();
		for(NatureOfInspection natureOfInspection : natureOfInspections) {
			INoteTypeDto  natureOfInspectionDto = mapper.map(natureOfInspection, INoteTypeDto.class);
			natureOfInspectionDtoList.add(natureOfInspectionDto);
		}
		return ResponseEntity.ok(natureOfInspectionDtoList);
	}

}
