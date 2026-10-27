package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SoTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoTypeService {

	private final SoTypeRepository soTypeRepository;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public SoType getSoTypeById(int id) {
		Optional<SoType> soTypeOptional = soTypeRepository.findById(id);
		if(soTypeOptional.isPresent()) {
			return soTypeOptional.get();
		} else {
			throw new DataNotFoundException("SoType not found by Id: " + id);
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public List<SoType> getAllSoType() {
		return soTypeRepository.findAll();
	}

}
