package in.co.bel.olms.master.controller.checklist.army;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyPreInspectionDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyPreInspection;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyPreInspectionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutinyPreInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyPreInspectionController {

	private final ChecklistScrutinyPreInspectionService checklistScrutinyPreInspectionService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyPreInspectionDto> getChecklistScrutinyPreInspectionById(@PathVariable int id) {
		ChecklistScrutinyPreInspection checklistScrutinyPreInspection = checklistScrutinyPreInspectionService.getChecklistScrutinyPreInspectionById(id);
		ChecklistScrutinyPreInspectionDto checklistScrutinyPreInspectionDto = mapper.map(checklistScrutinyPreInspection, ChecklistScrutinyPreInspectionDto.class);
		return ResponseEntity.ok(checklistScrutinyPreInspectionDto);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyPreInspectionDto>> getAllChecklistScrutinyPreInspection() {
		List<ChecklistScrutinyPreInspectionDto> checklistScrutinyPreInspectionDtoList = new ArrayList<>();
		List<ChecklistScrutinyPreInspection> checklistScrutinyPreInspectionList = checklistScrutinyPreInspectionService.getAllChecklistScrutinyPreInspections();
		for(ChecklistScrutinyPreInspection checklistScrutinyPreInspection : checklistScrutinyPreInspectionList) {
			ChecklistScrutinyPreInspectionDto checklistScrutinyPreInspectionDto = mapper.map(checklistScrutinyPreInspection, ChecklistScrutinyPreInspectionDto.class);
			checklistScrutinyPreInspectionDtoList.add(checklistScrutinyPreInspectionDto);
		}
		return ResponseEntity.ok(checklistScrutinyPreInspectionDtoList);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutinyPreInspection(@RequestBody List<ChecklistScrutinyPreInspectionDto> dtos) {
		Boolean updatedStatus = checklistScrutinyPreInspectionService.updateChecklistScrutinyPreInspection(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {
			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
			}
}
