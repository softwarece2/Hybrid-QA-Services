package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.SbuDto;
import in.co.bel.olms.master.entity.Sbu;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SbuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sbu")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SbuController {

	private final SbuService sbuService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SbuDto> getSbuById(@PathVariable int id) throws DataNotFoundException {
		Sbu sbu = sbuService.getSbuById(id);
		SbuDto sbuDto = mapper.map(sbu, SbuDto.class);
		return ResponseEntity.ok(sbuDto);
	}
		
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SbuDto>> getAllSbu() {
		List<SbuDto> sbuDtoList = new ArrayList<>();
		List<Sbu> sbus = sbuService.getAllSbu();
		for(Sbu sbu : sbus) {
			SbuDto sbuDto = mapper.map(sbu, SbuDto.class);
			sbuDtoList.add(sbuDto);
		}
		return ResponseEntity.ok(sbuDtoList);
	}
	
	/**
	 * 
	 * @param sbuDto
	 * @return
	 */
	@PostMapping("/saveSbu")
	public ResponseEntity<SbuDto> saveSbu(@RequestBody @Valid SbuDto sbuDto) {
		Sbu savedSbu = sbuService.saveSbu(sbuDto);
		SbuDto savedSbuDto = mapper.map(savedSbu, SbuDto.class);
		return new ResponseEntity<>(savedSbuDto, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param sbuDto
	 * @return
	 */
	@PutMapping("/updateSbu")
	public ResponseEntity<SbuDto> updateSbu(@RequestBody @Valid SbuDto sbuDto) {
		Sbu savedSbu = sbuService.updateSbu(sbuDto);
		SbuDto savedSbuDto = mapper.map(savedSbu, SbuDto.class);
		return new ResponseEntity<>(savedSbuDto, HttpStatus.OK);
	}
}
