package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.EqptGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EqptGroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EqptGroupService {

	private final EqptGroupRepository eqptGroupRepository;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public EqptGroup getEqptGroupById(int id) {
		
		Optional<EqptGroup> eqptGroupOptional = eqptGroupRepository.findById(id);
		
		if(eqptGroupOptional.isPresent()) {
			return eqptGroupOptional.get();
		} else {
			throw new DataNotFoundException("EqptGroup not found with Id:" + id);
		}
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<EqptGroup> getAllEqptGroup() {
		return eqptGroupRepository.findAll();
	}

}
