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

import in.co.bel.olms.master.dto.InspStatusDto;
import in.co.bel.olms.master.entity.InspStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.InspStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspStatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspStatusController {

	private final InspStatusService inspStatusService;

	private final Mapper mapper;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<InspStatusDto> getInspStatusById(@PathVariable int id) throws DataNotFoundException {
		InspStatus inspStatus = inspStatusService.getInspStatusById(id);
		InspStatusDto inspStatusDto = mapper.map(inspStatus, InspStatusDto.class);
		return ResponseEntity.ok(inspStatusDto);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<InspStatusDto>> getAllInspStatus() {
		List<InspStatusDto> inspStatusDtoList = new ArrayList<>();
		List<InspStatus> inspStatuses = inspStatusService.getAllInspStatus();
		for(InspStatus inspStatus : inspStatuses) {
			InspStatusDto inspStatusDto = mapper.map(inspStatus, InspStatusDto.class);
			inspStatusDtoList.add(inspStatusDto);
		}
		return ResponseEntity.ok(inspStatusDtoList);
		
	}
}
