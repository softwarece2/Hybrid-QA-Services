package in.co.bel.olms.master.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.Sbu;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SbuRepository;


@Component
public class SbuDao {

	@Autowired
	private SbuRepository sbuRepository;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Sbu getSbuById(int id) {
		Optional<Sbu> sbuOptional = sbuRepository.findById(id);
		if(sbuOptional.isPresent()) {
			return sbuOptional.get();
		} else {
			throw new DataNotFoundException("Sbu not found by Id: " + id);
		}
	}
	
	
}
