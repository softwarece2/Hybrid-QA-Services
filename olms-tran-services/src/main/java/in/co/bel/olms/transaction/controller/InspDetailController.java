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
import in.co.bel.olms.transaction.dto.InspDetailDto;
import in.co.bel.olms.transaction.entity.InspDetail;
import in.co.bel.olms.transaction.service.InspDetailService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspDetail")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspDetailController {

	private final InspDetailService inspDetailService;
	
	private static final Logger logger = LoggerFactory.getLogger(InspDetailController.class);
	  
	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("{id}")
	public ResponseEntity<InspDetailDto> getInspDetail(@PathVariable int id) throws DataNotFoundException {	
		InspDetail inspDetail = inspDetailService.getInspDetailById(id);
		InspDetailDto inspDetailDto = mapper.map(inspDetail, InspDetailDto.class);
		return ResponseEntity.ok(inspDetailDto);
	}

}
