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

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyQaclSparesDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQaclSpares;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyQaclSparesService;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/checklistScrutinyQaclSpares")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyQaclSparesController {

	private final ChecklistScrutinyQaclSparesService checklistScrutinyQaclSparesService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyQaclSparesDto> getChecklistScrutinyGiFormById(@PathVariable int id) {
		ChecklistScrutinyQaclSpares checklistScrutinyQaclSpares = checklistScrutinyQaclSparesService.getChecklistScrutinyQaclSparesById(id);
		ChecklistScrutinyQaclSparesDto checklistScrutinyQaclSparesDto = mapper.map(checklistScrutinyQaclSpares, ChecklistScrutinyQaclSparesDto.class);
		return ResponseEntity.ok(checklistScrutinyQaclSparesDto);
	}
	
	
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyQaclSparesDto>> getAllChecklistScrutinyGiForm() {
		List<ChecklistScrutinyQaclSparesDto> checklistScrutinyQaclSparesDtos = new ArrayList<>();
		List<ChecklistScrutinyQaclSpares> checklistScrutinyQaclSparess = checklistScrutinyQaclSparesService.getAllChecklistScrutinyQaclSpares();
		for(ChecklistScrutinyQaclSpares checklistScrutinyQaclSpares  : checklistScrutinyQaclSparess) {
			ChecklistScrutinyQaclSparesDto checklistScrutinyQaclSparesDto = mapper.map(checklistScrutinyQaclSpares, ChecklistScrutinyQaclSparesDto.class);
			checklistScrutinyQaclSparesDtos.add(checklistScrutinyQaclSparesDto);
		}
		return ResponseEntity.ok(checklistScrutinyQaclSparesDtos);
	}
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutinyQaclSpares(@RequestBody List<ChecklistScrutinyQaclSparesDto> dtos) {
		Boolean updatedStatus = checklistScrutinyQaclSparesService.updateChecklistScrutinyQaclSpares(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {

			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	
			}
	
}
