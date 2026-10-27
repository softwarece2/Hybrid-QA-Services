package in.co.bel.olms.transaction.controller;


import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.SoConsigneeMappingDto;
import in.co.bel.olms.transaction.entity.SoConsigneeMapping;
import in.co.bel.olms.transaction.service.so.SoConsigneeMappingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soConsigneeMapping")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoConsigneeMappingController {

	private static final Logger logger = LoggerFactory.getLogger(SoConsigneeMappingController.class);

	private final SoConsigneeMappingService soConsigneeMappingService;
	
	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<SoConsigneeMappingDto> getSoConsigneeMappingDtoById(@PathVariable int id) throws DataNotFoundException {
		SoConsigneeMapping soConsigneeMapping = soConsigneeMappingService.getSoConsigneeMappingById(id);
		SoConsigneeMappingDto soConsigneeMappingDto = mapper.map(soConsigneeMapping, SoConsigneeMappingDto.class);
		return ResponseEntity.ok(soConsigneeMappingDto);
	}
}
