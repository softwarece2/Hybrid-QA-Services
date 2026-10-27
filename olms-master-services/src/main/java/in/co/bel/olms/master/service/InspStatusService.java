package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.InspStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.InspStatusRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspStatusService {

	private final InspStatusRepository inspStatusRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public InspStatus getInspStatusById(int id) {
		Optional<InspStatus> inspStatusOptional = inspStatusRepository.findById(id);
		if(inspStatusOptional.isPresent()) {
			return inspStatusOptional.get();
		} else {
			throw new DataNotFoundException("InspStatus not found Exception by Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<InspStatus> getAllInspStatus() {
		return inspStatusRepository.findAll();
	}
}
