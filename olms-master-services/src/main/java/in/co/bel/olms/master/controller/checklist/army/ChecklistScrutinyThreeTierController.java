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

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyThreeTierDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyThreeTier;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyThreeTierService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutinyThreeTier")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyThreeTierController {

	private final ChecklistScrutinyThreeTierService checklistScrutinyThreeTierService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyThreeTierDto> getChecklistScrutinyThreeTierById(@PathVariable int id) {
		ChecklistScrutinyThreeTier checklistScrutinyThreeTier = checklistScrutinyThreeTierService.getChecklistScrutinyThreeTierById(id);
		ChecklistScrutinyThreeTierDto checklistScrutinyThreeTierDto = mapper.map(checklistScrutinyThreeTier, ChecklistScrutinyThreeTierDto.class);
		return ResponseEntity.ok(checklistScrutinyThreeTierDto);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyThreeTierDto>> getAllChecklistScrutinyThreeTier() {
		List<ChecklistScrutinyThreeTierDto> checklistScrutinyThreeTierDtoList = new ArrayList<>();
		List<ChecklistScrutinyThreeTier> checklistScrutinyThreeTierList = checklistScrutinyThreeTierService.getAllChecklistScrutinyThreeTiers();
		for(ChecklistScrutinyThreeTier checklistScrutinyThreeTier : checklistScrutinyThreeTierList) {
			ChecklistScrutinyThreeTierDto checklistScrutinyThreeTierDto = mapper.map(checklistScrutinyThreeTier, ChecklistScrutinyThreeTierDto.class);
			checklistScrutinyThreeTierDtoList.add(checklistScrutinyThreeTierDto);
		}
		return ResponseEntity.ok(checklistScrutinyThreeTierDtoList);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutinyThreeTier(@RequestBody List<ChecklistScrutinyThreeTierDto> dtos) {
		Boolean updatedStatus = checklistScrutinyThreeTierService.updateChecklistScrutinyThreeTier(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {
			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
			}
	
}
