package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.OrganizationTypeDto;
import in.co.bel.olms.master.entity.OrganizationType;
import in.co.bel.olms.master.service.OrganizationTypeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orgType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OrganizationTypeController {

	private final OrganizationTypeService organizationTypeService;

	private final Mapper mapper;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<OrganizationTypeDto>> getAllorgType(){
		List<OrganizationTypeDto> organizationTypeDtoList = new ArrayList<> ();
		List<OrganizationType> organizationTypes = organizationTypeService.getAllOrganizationType();
		for(OrganizationType organizationType : organizationTypes) {
			OrganizationTypeDto organizationTypeDto = mapper.map(organizationType, OrganizationTypeDto.class);
			organizationTypeDtoList.add(organizationTypeDto);
		}
		return ResponseEntity.ok(organizationTypeDtoList);
	}
}