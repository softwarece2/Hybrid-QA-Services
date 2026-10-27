package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.ChecklistFileCloseDto;
import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.service.ChecklistFileCloseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checklistfileclose")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChecklistFileCloseController {

	private final ChecklistFileCloseService fileService;

	private final Mapper mapper;
	
	@GetMapping("/getAll")
	public ResponseEntity <List<ChecklistFileCloseDto>> getAllreferences(){
		List<ChecklistFileCloseDto> fileDtoList = new ArrayList<>();
		List<ChecklistFileClose> references = fileService.getAll();
		for (ChecklistFileClose filereferences : references) {
			ChecklistFileCloseDto fileDto = mapper.map(filereferences, ChecklistFileCloseDto.class);
			fileDtoList.add(fileDto);
		}	
		return ResponseEntity.ok(fileDtoList);

	}

}
