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
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import lombok.RequiredArgsConstructor;

/**
* SupplyOrderReportController class
*
* @author  Manasa B
* @version 1.0
* @since   24-05-2023
*/

@RestController
@RequestMapping("/supplyOrder")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SupplyOrderController {

	private final SupplyOrderService supplyOrderService;

	private final Mapper mapper;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrders() {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		List<SupplyOrder> supplyOrders = supplyOrderService.getAllSupplyOrders();
		for (SupplyOrder supplyOrder : supplyOrders) {
			SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
			supplyOrderDtos.add(supplyOrderDto);
		}
		return ResponseEntity.ok(supplyOrderDtos);

	}

	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SupplyOrderDto> getSupplyOrder(@PathVariable int id) throws DataNotFoundException {

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(id);
		SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
		return ResponseEntity.ok(supplyOrderDto);

	}

}
