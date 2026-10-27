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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.InspectionAuthorityDto;
import in.co.bel.olms.master.entity.InspectionAuthority;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.InspectionAuthorityService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspectionAuth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspectionAuthorityController {

	private final InspectionAuthorityService inspectionAuthService;

	private final Mapper mapper;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<InspectionAuthorityDto>> getAllUsers() {

		List<InspectionAuthorityDto> inspectionAuthorityDtos = new ArrayList<>();
		List<InspectionAuthority> inspectionAuthorities = inspectionAuthService.getAllInspectionAuthorities();
		for (InspectionAuthority inspectionAuthority : inspectionAuthorities) {
			InspectionAuthorityDto inspectionAuthorityDto = mapper.map(inspectionAuthority, InspectionAuthorityDto.class);
			inspectionAuthorityDtos.add(inspectionAuthorityDto);
		}
		return ResponseEntity.ok(inspectionAuthorityDtos);

	}

	
	/**
	 * 
	 * @param inspectionAuthorityDto
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<InspectionAuthorityDto> saveInspectionAuthority(@RequestBody @Valid InspectionAuthorityDto inspectionAuthorityDto) {

		InspectionAuthority savedInspectionAuthority = inspectionAuthService.saveInspectionAuthority(inspectionAuthorityDto);
		InspectionAuthorityDto savedSupplierDto = mapper.map(savedInspectionAuthority, InspectionAuthorityDto.class);
		return new ResponseEntity<>(savedSupplierDto, HttpStatus.CREATED);

	}

	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<InspectionAuthorityDto> getInspectionAuthority(@PathVariable int id) throws DataNotFoundException {

		InspectionAuthority inspectionAuthority = inspectionAuthService.getInspectionAuthority(id);
		InspectionAuthorityDto inspectionAuthorityDto = mapper.map(inspectionAuthority, InspectionAuthorityDto.class);
		return ResponseEntity.ok(inspectionAuthorityDto);

	}

}
