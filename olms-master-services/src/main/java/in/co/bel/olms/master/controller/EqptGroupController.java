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

import in.co.bel.olms.master.dto.EqptGroupDto;
import in.co.bel.olms.master.entity.EqptGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.EqptGroupService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/eqptGroup")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EqptGroupController {

	private final EqptGroupService eqptGroupService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<EqptGroupDto> getEqptGroupById(@PathVariable int id) throws DataNotFoundException {
		
		EqptGroup eqptGroup = eqptGroupService.getEqptGroupById(id);
		EqptGroupDto eqptGroupDto = mapper.map(eqptGroup, EqptGroupDto.class);
		return ResponseEntity.ok(eqptGroupDto);
	}
	
	/**
	 * 
	 * @return
	 */
	
	@GetMapping("/getAll")
	public ResponseEntity<List<EqptGroupDto>> getAllEqptGroup() {
		List<EqptGroupDto> eqptGroupDtoList = new ArrayList<>();
		List<EqptGroup> eqptGroups = eqptGroupService.getAllEqptGroup();
		for(EqptGroup eqptGroup : eqptGroups) {
			EqptGroupDto eqptGroupDto = mapper.map(eqptGroup, EqptGroupDto.class);
			eqptGroupDtoList.add(eqptGroupDto);
		}
		return ResponseEntity.ok(eqptGroupDtoList);
	}

}
