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
import in.co.bel.olms.transaction.dto.PartListDto;
import in.co.bel.olms.transaction.entity.PartList;
import in.co.bel.olms.transaction.service.PartListService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/partList")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PartListController {
	
	private final PartListService partListService;
	
	private static final Logger logger =LoggerFactory.getLogger(PartListController.class);

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PartListDto> getPartListById(@PathVariable int id) throws DataNotFoundException {
		PartList partList = partListService.getPartListById(id);
		PartListDto partListDto = mapper.map(partList, PartListDto.class);
		return ResponseEntity.ok(partListDto);
	}
}
