package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.UomDto;
import in.co.bel.olms.master.entity.Uom;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.UomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/uom")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UomController {

	private final UomService uomService;

	private final Mapper mapper;
	
	Gson gson = new Gson();
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UomDto> getUomById(@PathVariable int id) throws DataNotFoundException{
		Uom uom = uomService.getUomById(id);
		UomDto uomDto = mapper.map(uom, UomDto.class);
		return ResponseEntity.ok(uomDto);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<UomDto>> getAllUoms() {
		List<UomDto> uomDtoList = new ArrayList<>();
		List<Uom> uoms = uomService.getAllUom();
		for(Uom uom : uoms) {
			UomDto uomDto = mapper.map(uom, UomDto.class);
			uomDtoList.add(uomDto);
		}
		return ResponseEntity.ok(uomDtoList);
	}
	
	@GetMapping("/getAllActive")
	public ResponseEntity<List<UomDto>> getAllActiveUoms() {
		List<UomDto> uomDtoList = new ArrayList<>();
		List<Uom> uoms = uomService.getAllActiveUom();
		for(Uom uom : uoms) {
			UomDto uomDto = mapper.map(uom, UomDto.class);
			uomDtoList.add(uomDto);
		}
		return ResponseEntity.ok(uomDtoList);
	}
	
	/**
	 * 
	 * @param uomDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveUom(@RequestBody @Valid UomDto uomDto) {
		Boolean unit = uomService.existsByUnit(uomDto.getUnit());
		if(unit) {
			 return new ResponseEntity<String>("Uom unit already exists.",HttpStatus.CONFLICT);
		}		
		else {
			uomDto.setActive(true);
	Uom savedUom = uomService.saveUom(uomDto);
	UomDto savedUomDto = mapper.map(savedUom, UomDto.class);
	return new ResponseEntity<>(gson.toJson(savedUomDto), HttpStatus.CREATED);
		}
	}

	/**
	 * 
	 * @param uomDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<String> updateUom(@RequestBody @Valid UomDto uomDto) {
		UomDto savedUomDto = null;
		try {
			if (uomDto != null) {
				Boolean unit = uomService.existsByUnitAndIdNot(uomDto.getUnit(),uomDto.getId());
				if(unit) {
					 return new ResponseEntity<String>("Uom unit already exists.",HttpStatus.CONFLICT);
				}	
				else {
				Uom savedUom = uomService.updateUom(uomDto);
				savedUomDto = mapper.map(savedUom, UomDto.class);
				return new ResponseEntity<String>(gson.toJson(savedUomDto), HttpStatus.CREATED);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
