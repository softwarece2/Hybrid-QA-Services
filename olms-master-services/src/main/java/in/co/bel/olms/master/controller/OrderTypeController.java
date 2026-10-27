package in.co.bel.olms.master.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.OrderTypeDto;
import in.co.bel.olms.master.entity.OrderType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.OrderTypeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orderType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OrderTypeController {

	private final OrderTypeService orderTypeService;

	private final Mapper mapper;

	@GetMapping("/getAll")
	public ResponseEntity<List<OrderTypeDto>> getAllOrderType() {
		List<OrderType> orderTypes = orderTypeService.getAllOrderType();
		List<OrderTypeDto> orderTypeDtoList = orderTypes.stream()
				.map(orderType -> mapper.map(orderType, OrderTypeDto.class)).collect(Collectors.toList());
		return ResponseEntity.ok(orderTypeDtoList);

	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<OrderTypeDto> getOrderTypeById(@PathVariable int id) throws DataNotFoundException {
		OrderType orderType = orderTypeService.getOrderTypeById(id);
		OrderTypeDto orderTypeDto = mapper.map(orderType, OrderTypeDto.class);
		return ResponseEntity.ok(orderTypeDto);
	}
}
