package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.service.OlLineItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/olLineItem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlLineItemController {

	private final OlLineItemService olLineItemService;

	private final Mapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<OlLineItemDto> getOlLineItemById(@PathVariable int id) throws DataNotFoundException {
		OlLineItem olLineItem = olLineItemService.getOlLineItemById(id);
		OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
		return ResponseEntity.ok(olLineItemDto);
	}
	
	
    @GetMapping("getByOlId/{olId}")
	public ResponseEntity <List<OlLineItemDto>> getOlLineItemByOlId(@PathVariable int olId)  {
		List<OlLineItemDto> olLineItemDtos=new ArrayList<>();
	
	List<OlLineItem> olLineItems = olLineItemService.getOlLineItemByOlId(olId);
	for (OlLineItem olLineItem : olLineItems) {
		OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
		olLineItemDtos.add(olLineItemDto);
	}
	
		return ResponseEntity.ok(olLineItemDtos);
	}

}
