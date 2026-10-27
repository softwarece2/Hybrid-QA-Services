package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlLineItemService {

	private final OlLineItemRepository olLineItemRepository;	
	
	public OlLineItem getOlLineItemById(int id) {
		Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(id);
		if(olLineItemOptional.isPresent()) {
			return olLineItemOptional.get();
		} else {
			throw new DataNotFoundException("OlLineItem not found By Id: " + id);	
		}
	}
	
	public List<OlLineItem> getAllOlLineServices() {
		return olLineItemRepository.findAll();
	}
		
	public List<OlLineItem>  getOlLineItemByOlId(int olId) {
		return olLineItemRepository.getLineItemsByOl(olId);		
	}
}
		


