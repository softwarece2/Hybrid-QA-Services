package in.co.bel.olms.transaction.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;

/**
* SupplyOrder service class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-16 
*/

@Service
@RequiredArgsConstructor
public class SupplyOrderService {

	private final SupplyOrderRepository supplyOrderRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @param supplyOrderDto
	 * @return
	 */
	public SupplyOrder saveSupplyOrder(SupplyOrderDto supplyOrderDto) {

		SupplyOrder supplyOrder = mapper.map(supplyOrderDto, SupplyOrder.class);
		supplyOrder.setCreatedDate(new Date());
		return supplyOrderRepository.save(supplyOrder);

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public SupplyOrder getSupplyOrder(int id) {

		Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(id);
		if (supplyOrderOptional.isPresent()) {
			return supplyOrderOptional.get();
		} else {
			throw new DataNotFoundException("SupplyOrder not found with Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SupplyOrder> getAllSupplyOrders() {
		return supplyOrderRepository.findAll();
	}
}
