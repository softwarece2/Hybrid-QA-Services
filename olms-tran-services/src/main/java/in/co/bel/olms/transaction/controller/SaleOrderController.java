package in.co.bel.olms.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.dto.SaleOrderDto;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.service.so.SaleOrderService;
import lombok.RequiredArgsConstructor;

/**
 * SupplyOrderController class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-01-30
 */

@RestController
@RequestMapping("/saleOrder")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SaleOrderController {
	
	private static final Logger logger =LoggerFactory.getLogger(SaleOrderController.class);

	private final SaleOrderService saleOrderService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@GetMapping("/getAll")
	public ResponseEntity<List<SaleOrderDto>> getAllSaleOrders() {

		List<SaleOrderDto> saleOrderDtos = new ArrayList<>();
		List<SaleOrder> saleOrders = saleOrderService.getAllSaleOrders();
		for (SaleOrder saleOrder : saleOrders) {
			SaleOrderDto saleOrderDto = mapper.map(saleOrder, SaleOrderDto.class);
			saleOrderDtos.add(saleOrderDto);
		}
		return ResponseEntity.ok(saleOrderDtos);

	}
	
	@GetMapping("/getBySoId/{soId}")
	public ResponseEntity<List<SaleOrderDto>> getSaleOrdersBySoId(@PathVariable int soId) {

		List<SaleOrderDto> saleOrderDtos = new ArrayList<>();
		List<SaleOrder> saleOrders = saleOrderService.getSaleOrdersBySoId(soId);
		for (SaleOrder saleOrder : saleOrders) {
			SaleOrderDto saleOrderDto = mapper.map(saleOrder, SaleOrderDto.class);
			saleOrderDtos.add(saleOrderDto);
		}
		return ResponseEntity.ok(saleOrderDtos);

	}
	
	@GetMapping("/{soId}/warrants")
	public ResponseEntity<List<CategoryDto>> getWarrantsBySoId(@PathVariable Integer soId){
		try {
			List<CategoryDto> responseDtos = saleOrderService.getWarrantsBySoId(soId);
			return ResponseEntity.ok(responseDtos);
		} catch (Exception e) {
			return null;
		}
	}

}
