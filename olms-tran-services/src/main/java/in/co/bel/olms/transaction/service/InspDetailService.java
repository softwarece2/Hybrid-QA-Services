package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.InspDetail;
import in.co.bel.olms.transaction.repository.InspDetailRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspDetailService {
	
	private final InspDetailRepository inspDetailRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	
	public InspDetail getInspDetailById(int id) {
		Optional<InspDetail> inspDetailOptional = inspDetailRepository.findById(id);
		if(inspDetailOptional.isPresent()) {
			return inspDetailOptional.get();
		} else {
			throw new DataNotFoundException("inspDetail not found By Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	public List<InspDetail> getAllInspDetails() {
		return inspDetailRepository.findAll();
	}
}
