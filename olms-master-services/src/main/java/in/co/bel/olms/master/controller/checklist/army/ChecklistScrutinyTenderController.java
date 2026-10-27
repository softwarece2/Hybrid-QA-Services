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

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyTenderDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyTender;
import in.co.bel.olms.master.service.checklist.army.ChecklistScrutinyTenderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistScrutinyTender")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistScrutinyTenderController {

	private final ChecklistScrutinyTenderService checklistScrutinyTenderService;

	private final Mapper mapper;

	@GetMapping("/{id}")
	public ResponseEntity<ChecklistScrutinyTenderDto> getChecklistScrutinyById(@PathVariable int id) {
		ChecklistScrutinyTender checklistScrutiny = checklistScrutinyTenderService.getChecklistScrutinyById(id);
		ChecklistScrutinyTenderDto checklistScrutinyTenderDto = mapper.map(checklistScrutiny,
				ChecklistScrutinyTenderDto.class);
		return ResponseEntity.ok(checklistScrutinyTenderDto);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<ChecklistScrutinyTenderDto>> getAllChecklistScrutiny() {
		List<ChecklistScrutinyTenderDto> checklistScrutinyTenderDtoList = new ArrayList<>();
		List<ChecklistScrutinyTender> checklistScrutinyTenders = checklistScrutinyTenderService
				.getAllChecklistScrutiny();
		for (ChecklistScrutinyTender checklistScrutinyTender : checklistScrutinyTenders) {
			ChecklistScrutinyTenderDto checklistScrutinyTenderDto = mapper.map(checklistScrutinyTender,
					ChecklistScrutinyTenderDto.class);
			checklistScrutinyTenderDtoList.add(checklistScrutinyTenderDto);
		}
		return ResponseEntity.ok(checklistScrutinyTenderDtoList);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Boolean> updateChecklistScrutiny(@RequestBody List<ChecklistScrutinyTenderDto> dtos) {
		Boolean updatedStatus = checklistScrutinyTenderService.updateChecklistScrutiny(dtos);
//	 supplyOrderService.updateSupplyOrder(supplyOrderDto);
		if (updatedStatus.booleanValue()) {

			return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	
			}
	

}
