package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.OrderType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OrderTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderTypeService {

	private final OrderTypeRepository orderTypeRepository;

	public List<OrderType> getAllOrderType() {		
		return orderTypeRepository.findAll();
	}


	
	public OrderType getOrderTypeById(int id) {
		Optional<OrderType> orderType = orderTypeRepository.findById(id);
		if (orderType.isPresent()) {
			return orderType.get();
		} else {
			throw new DataNotFoundException("Oder Type not present for the order type Id " + id);
		}
	}
	
	
}
