package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.Inspector;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.InspectorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectorService {

	private final InspectorRepository inspectorRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Inspector getInspectorById(int id) {
		Optional<Inspector> inspectorOptional = inspectorRepository.findById(id);
		if(inspectorOptional.isPresent()) {
			return inspectorOptional.get();
		} else {
			throw new DataNotFoundException("Inspector not found with Id: " + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Inspector> getAllInspector() {
		return inspectorRepository.findAll();
	}

}
