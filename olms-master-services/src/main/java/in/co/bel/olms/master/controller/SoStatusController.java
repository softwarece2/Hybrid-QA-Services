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

import in.co.bel.olms.master.dto.SoStatusDto;
import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SoStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soStatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoStatusController {

	private final SoStatusService soStatusService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SoStatusDto> getSoStatusById(@PathVariable int id) throws DataNotFoundException {
		SoStatus soStatus = soStatusService.getSoStatusById(id);
		SoStatusDto soStatusDto = mapper.map(soStatus, SoStatusDto.class);
		return ResponseEntity.ok(soStatusDto);
	}
		
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SoStatusDto>> getAllSoStatus() {
		List<SoStatusDto> soStatusDtoList = new ArrayList<>();
		List<SoStatus> soStatuses = soStatusService.getAllSoStatus();
		for(SoStatus soStatus : soStatuses) {
			SoStatusDto soStatusDto = mapper.map(soStatus, SoStatusDto.class);
			soStatusDtoList.add(soStatusDto);
		}
		return ResponseEntity.ok(soStatusDtoList);
	}
}
