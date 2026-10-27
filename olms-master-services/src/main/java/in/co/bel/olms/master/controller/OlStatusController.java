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

import in.co.bel.olms.master.dto.OlStatusDto;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.OlStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/olStatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlStatusController {

	private final OlStatusService olStatusService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<OlStatusDto> getOlStatusById(@PathVariable int id) throws DataNotFoundException {
		OlStatus olStatus = olStatusService.getOlStatusById(id);
		OlStatusDto olStatusDto = mapper.map(olStatus, OlStatusDto.class);
		return ResponseEntity.ok(olStatusDto);
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<OlStatusDto>> getAllOlStatus() {
		List<OlStatusDto> olStatusDtoList = new ArrayList<>();
		List<OlStatus> olStatuses = olStatusService.getAllOlStatus();
		for(OlStatus olStatus : olStatuses) {
			OlStatusDto olStatusDto = mapper.map(olStatus, OlStatusDto.class);
			olStatusDtoList.add(olStatusDto);
		}
		return ResponseEntity.ok(olStatusDtoList);
	}
}
