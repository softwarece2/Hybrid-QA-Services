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

import in.co.bel.olms.master.dto.SectionsDto;
import in.co.bel.olms.master.entity.Sections;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SectionsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sections")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SectionsController {

	private final SectionsService sectionsService;

	private final Mapper mapper;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SectionsDto> getSectionsById(@PathVariable int id) throws DataNotFoundException {
		
		Sections sections = sectionsService.getSectionsById(id);
		SectionsDto sectionsDto = mapper.map(sections, SectionsDto.class);
		return ResponseEntity.ok(sectionsDto);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SectionsDto>> getAllSections() {
		List<SectionsDto> sectionsDtoList = new ArrayList<>();
		List<Sections> sections = sectionsService.getAllSections();
		for(Sections section : sections) {
			SectionsDto sectionsDto = mapper.map(section, SectionsDto.class);
			sectionsDtoList.add(sectionsDto);
		}
		return ResponseEntity.ok(sectionsDtoList);
	}

}
