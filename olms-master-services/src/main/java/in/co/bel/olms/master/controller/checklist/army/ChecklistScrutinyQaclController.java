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

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyQaclDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQacl;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyQaclService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutinyQacl")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyQaclController {

	private final ChecklistScrutinyQaclService checklistScrutinyQaclService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyQaclDto> getChecklistScrutinyGiFormById(@PathVariable int id) {
		ChecklistScrutinyQacl checklistScrutinyQacl = checklistScrutinyQaclService.getChecklistScrutinyQaclById(id);
		ChecklistScrutinyQaclDto checklistScrutinyQaclDto = mapper.map(checklistScrutinyQacl, ChecklistScrutinyQaclDto.class);
		return ResponseEntity.ok(checklistScrutinyQaclDto);
	}
	
	@GetMapping("/getBypart/{partId}")
	public ResponseEntity<List<ChecklistScrutinyQaclDto>> getBypart(@PathVariable int partId) {
		List<ChecklistScrutinyQacl> qaclList = checklistScrutinyQaclService.getBypart(partId);
		List<ChecklistScrutinyQaclDto> dtos = new ArrayList<>();
		for(ChecklistScrutinyQacl qacl:qaclList) {
			ChecklistScrutinyQaclDto dto=mapper.map(qacl, ChecklistScrutinyQaclDto.class);
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyQaclDto>> getAllChecklistScrutinyGiForm() {
		List<ChecklistScrutinyQaclDto> checklistScrutinyQaclDtos = new ArrayList<>();
		List<ChecklistScrutinyQacl> checklistScrutinyQacls = checklistScrutinyQaclService.getAllChecklistScrutinyQacl();
		for(ChecklistScrutinyQacl checklistScrutinyQacl  : checklistScrutinyQacls) {
			ChecklistScrutinyQaclDto checklistScrutinyQaclDto = mapper.map(checklistScrutinyQacl, ChecklistScrutinyQaclDto.class);
			checklistScrutinyQaclDtos.add(checklistScrutinyQaclDto);
		}
		return ResponseEntity.ok(checklistScrutinyQaclDtos);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutiny(@RequestBody List<ChecklistScrutinyQaclDto> dtos) {
		Boolean updatedStatus = checklistScrutinyQaclService.updateChecklistScrutiny(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {
			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
			}
}

