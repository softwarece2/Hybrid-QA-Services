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

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyGiFormDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyGiForm;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyGiFormService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutinyGiForm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyGiFormController {

	private final ChecklistScrutinyGiFormService checklistScrutinyGiFormService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyGiFormDto> getChecklistScrutinyGiFormById(@PathVariable int id) {
		ChecklistScrutinyGiForm checklistScrutinyGiForm = checklistScrutinyGiFormService.getChecklistScrutinyGiFormById(id);
		ChecklistScrutinyGiFormDto checklistScrutinyGiFormDto = mapper.map(checklistScrutinyGiForm, ChecklistScrutinyGiFormDto.class);
		return ResponseEntity.ok(checklistScrutinyGiFormDto);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyGiFormDto>> getAllChecklistScrutinyGiForm() {
		List<ChecklistScrutinyGiFormDto> checklistScrutinyGiFormDtoList = new ArrayList<>();
		List<ChecklistScrutinyGiForm> checklistScrutinyGiFormList = checklistScrutinyGiFormService.getAllChecklistScrutinyGiForms();
		for(ChecklistScrutinyGiForm checklistScrutinyGiForm : checklistScrutinyGiFormList) {
			ChecklistScrutinyGiFormDto checklistScrutinyGiFormDto = mapper.map(checklistScrutinyGiForm, ChecklistScrutinyGiFormDto.class);
			checklistScrutinyGiFormDtoList.add(checklistScrutinyGiFormDto);
		}
		return ResponseEntity.ok(checklistScrutinyGiFormDtoList);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutinyGiForm(@RequestBody List<ChecklistScrutinyGiFormDto> dtos) {
		Boolean updatedStatus = checklistScrutinyGiFormService.updateChecklistScrutinyGiForm(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {
			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
			}
	
}
