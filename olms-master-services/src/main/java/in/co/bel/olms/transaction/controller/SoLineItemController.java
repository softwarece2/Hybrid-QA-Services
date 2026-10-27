package in.co.bel.olms.transaction.controller;

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

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.SoLineItemDto;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.service.SoLineItemService;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 24-11-22
 */

@RestController
@RequestMapping("/soLineItem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoLineItemController {

	private final SoLineItemService soLineItemService;

	private final Mapper mapper;

	@GetMapping("/{id}")
	public ResponseEntity<SoLineItemDto> getSoLineItemById(@PathVariable int id) throws DataNotFoundException {
		SOLineItem soLineItem = soLineItemService.getSoLineItemById(id);
		SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
		return ResponseEntity.ok(soLineItemDto);
	}

	@PostMapping("/save")
	public ResponseEntity<Boolean> saveSoLineItems(@RequestBody @Valid List<SoLineItemDto> soLineItemDtos) {

		Boolean savedStatus = soLineItemService.saveSoLineItems(soLineItemDtos);
		if (savedStatus.booleanValue()) {
			return new ResponseEntity<>(savedStatus, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(savedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getBySoId/{soId}")
	public ResponseEntity<List<SoLineItemDto>> getSoLineItemsBySoId(@PathVariable int soId) {

		List<SoLineItemDto> soLineItemDtos = new ArrayList<>();
		List<SOLineItem> soLineItems = soLineItemService.getSoLineItemsBySoId(soId);
		for (SOLineItem soLineItem : soLineItems) {
			SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
			soLineItemDto.setDescription(soLineItem.getPartNumber().getMaterialDescription());
			soLineItemDtos.add(soLineItemDto);
		}
		return ResponseEntity.ok(soLineItemDtos);

	}

}
