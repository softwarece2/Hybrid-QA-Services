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

import in.co.bel.olms.master.dto.SoTypeDto;
import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SoTypeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoTypeController {

	private final SoTypeService soTypeService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SoTypeDto> getSoTypeById(@PathVariable int id) throws DataNotFoundException {
		SoType soType = soTypeService.getSoTypeById(id);
		SoTypeDto soTypeDto = mapper.map(soType, SoTypeDto.class);
		return ResponseEntity.ok(soTypeDto);
	}
		
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SoTypeDto>> getAllSoTypeDto() {
		List<SoTypeDto> soTypeDtoList = new ArrayList<>();
		List<SoType> soTypes = soTypeService.getAllSoType();
		for(SoType soType : soTypes) {
			SoTypeDto soTypeDto = mapper.map(soType, SoTypeDto.class);
			soTypeDtoList.add(soTypeDto);
		}
		return ResponseEntity.ok(soTypeDtoList);
	}
}
