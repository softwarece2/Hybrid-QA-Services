package in.co.bel.olms.transaction.service.so;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.SaleOrderRepository;
import lombok.RequiredArgsConstructor;

/**
* SupplyOrder service class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-16 
*/

// Saleorder is work order no in OLF project
@Service
@RequiredArgsConstructor
public class SaleOrderService {

	private final SaleOrderRepository saleOrderRepository;

	private final CategoryRepository categoryRepo;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(SaleOrderService.class);

	public List<SaleOrder> getAllSaleOrders() {

		return saleOrderRepository.findAll();

	}
	
	public List<SaleOrder> getSaleOrdersBySoId(int soId) {

		return saleOrderRepository.getSaleOrdersBySoId(soId);

	}

	// Add service to get all the warrants of for the particular sale order ID
	public List<CategoryDto> getWarrantsBySoId(Integer soId) {
		
		List<Category> categories = categoryRepo.findBySaleOrderId(soId);
		
		return categories.stream().map(category -> 
			mapper.map(category, CategoryDto.class)
		).toList();
	}

	
	
}
