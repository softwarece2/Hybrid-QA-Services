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

import in.co.bel.olms.master.dto.ChecklistScrutinyDto;
import in.co.bel.olms.master.entity.checklist.ChecklistScrutinySO;
import in.co.bel.olms.master.service.checklist.ChecklistScrutinySoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutiny")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinySoController {

	private final ChecklistScrutinySoService checklistScrutinyService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyDto> getChecklistScrutinyById(@PathVariable int id) {
		ChecklistScrutinySO checklistScrutiny = checklistScrutinyService.getChecklistScrutinyById(id);
		ChecklistScrutinyDto checklistScrutinyDto = mapper.map(checklistScrutiny, ChecklistScrutinyDto.class);
		return ResponseEntity.ok(checklistScrutinyDto);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyDto>> getAllChecklistScrutiny() {
		List<ChecklistScrutinyDto> checklistScrutinyDtoList = new ArrayList<>();
		List<ChecklistScrutinySO> checklistScrutinys = checklistScrutinyService.getAllChecklistScrutiny();
		for(ChecklistScrutinySO checklistScrutiny : checklistScrutinys) {
			ChecklistScrutinyDto checklistScrutinyDto = mapper.map(checklistScrutiny, ChecklistScrutinyDto.class);
			checklistScrutinyDtoList.add(checklistScrutinyDto);
		}
		return ResponseEntity.ok(checklistScrutinyDtoList);
	}

}
