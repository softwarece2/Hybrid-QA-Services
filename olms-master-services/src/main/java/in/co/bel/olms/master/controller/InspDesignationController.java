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

import in.co.bel.olms.master.dto.InspDesignationDto;
import in.co.bel.olms.master.entity.EmpDesignation;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmpDesignationRepository;
import in.co.bel.olms.master.service.InspDesignationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/EmpDesignation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspDesignationController {

	private final InspDesignationService inspDesignationService;

	private final EmpDesignationRepository empDesignationRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<InspDesignationDto> getInspDesignationById(@PathVariable int id) throws DataNotFoundException {
		
		EmpDesignation EmpDesignation = inspDesignationService.getInspDesignationById(id);
		InspDesignationDto inspDesignationDto = mapper.map(EmpDesignation, InspDesignationDto.class);
		return ResponseEntity.ok(inspDesignationDto);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<InspDesignationDto>> getAllInspDesignations() {
		List<InspDesignationDto> inspDesignationDtoList = new ArrayList<>();
		List<EmpDesignation> inspDesignations = inspDesignationService.getAllInspDesignation();
		for(EmpDesignation EmpDesignation : inspDesignations) {
			InspDesignationDto inspDesignationDto = mapper.map(EmpDesignation, InspDesignationDto.class);
			inspDesignationDtoList.add(inspDesignationDto);
		}
		return ResponseEntity.ok(inspDesignationDtoList);
	}
	
	@GetMapping("/getAllDesignations/{orgId}")
	public ResponseEntity<List<InspDesignationDto>> getDesignations(int orgId) {
		List<InspDesignationDto> inspDesignationDtoList = new ArrayList<>();
		List<EmpDesignation> inspDesignations = empDesignationRepository.findAll();
		for(EmpDesignation EmpDesignation : inspDesignations) {
			InspDesignationDto desgn=new InspDesignationDto();
			desgn.setId(EmpDesignation.getId());
			desgn.setName(EmpDesignation.getDesignation());
			inspDesignationDtoList.add(desgn);
		}
		return ResponseEntity.ok(inspDesignationDtoList);
	}

}
