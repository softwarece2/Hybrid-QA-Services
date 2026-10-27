package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.PartList;
import in.co.bel.olms.transaction.repository.PartListRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartListService {

	private final PartListRepository partListRepository;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	
	public PartList getPartListById(int id) {
		Optional<PartList> partListOptional = partListRepository.findById(id);
		if(partListOptional.isPresent()) {
			return partListOptional.get();
		} else {
			throw new DataNotFoundException("PartList is not found by Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	public List<PartList> getAllPartList() {
		return partListRepository.findAll();
	}

}
