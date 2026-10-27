package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OlStatusRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlStatusService {

	private final OlStatusRepository olStatusRepository;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public OlStatus getOlStatusById(int id) {
		Optional<OlStatus> olStatusOptional = olStatusRepository.findById(id);
		if(olStatusOptional.isPresent()) {
			return olStatusOptional.get();
		} else {
			throw new DataNotFoundException("OlStatus not found by Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<OlStatus> getAllOlStatus() {
		return olStatusRepository.findAll();
	}
}
