package in.co.bel.olms.transaction.controller;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.SoPartListDto;
import in.co.bel.olms.transaction.entity.SoPartList;
import in.co.bel.olms.transaction.service.so.SoPartListService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soPartList")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoPartListController {
	
	private final SoPartListService soPartListService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */ 
	
	@GetMapping("/{id}")
	public ResponseEntity<SoPartListDto> getSoPartListById(@PathVariable int id) throws DataNotFoundException {
		SoPartList soPartList = soPartListService.getSoPartListById(id);
		SoPartListDto soPartListDto = mapper.map(soPartList, SoPartListDto.class);
		return ResponseEntity.ok(soPartListDto);
	}

}
