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

import in.co.bel.olms.master.dto.InspectorDto;
import in.co.bel.olms.master.entity.Inspector;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.InspectorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspector")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspectorController {

	private final InspectorService inspectorService;

	private final Mapper mapper;

	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<InspectorDto> getInspectorById(@PathVariable int id) throws DataNotFoundException {
		Inspector inspector = inspectorService.getInspectorById(id);
		InspectorDto inspectorDto = mapper.map(inspector, InspectorDto.class);
		return ResponseEntity.ok(inspectorDto);
	}

	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<InspectorDto>> getAllInspectors() {
		List<InspectorDto> inspectorDtoList = new ArrayList<>();
		List<Inspector> inspectors = inspectorService.getAllInspector();
		for (Inspector inspector : inspectors) {
			InspectorDto inspectorDto = mapper.map(inspector, InspectorDto.class);
			inspectorDtoList.add(inspectorDto);
		}
		return ResponseEntity.ok(inspectorDtoList);
	}

}
