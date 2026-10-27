package in.co.bel.olms.master.controller.checklist;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.ChecklistOlDto;
import in.co.bel.olms.master.entity.checklist.ChecklistOlIB;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.checklist.ChecklistOlIbService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 25-04-2023
 */

@RestController
@RequestMapping("/checklistOl/ib")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistOlIBController {

	private final ChecklistOlIbService checklistOlService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistOlDto> getChecklistOlById(@PathVariable int id) throws DataNotFoundException {
		
		ChecklistOlIB checklistOl = checklistOlService.getChecklistOlById(id);
		ChecklistOlDto checklistOlDto = mapper.map(checklistOl, ChecklistOlDto.class);
		return ResponseEntity.ok(checklistOlDto);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistOlDto>> getAllChecklistOlDto() {
		List<ChecklistOlDto> checklistOlDtoList = new ArrayList<>();
		List<ChecklistOlIB> checklistOls = checklistOlService.getAllChecklistOl();
		for(ChecklistOlIB checklistOl: checklistOls) {
			ChecklistOlDto checklistOlDto = mapper.map(checklistOl, ChecklistOlDto.class);
			checklistOlDtoList.add(checklistOlDto);
		}
		return ResponseEntity.ok(checklistOlDtoList);
	}

}