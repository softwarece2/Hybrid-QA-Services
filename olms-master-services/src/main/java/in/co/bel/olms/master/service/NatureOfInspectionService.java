package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.NatureOfInspection;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.NatureOfInspectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NatureOfInspectionService {

	private final NatureOfInspectionRepository natureOfInspectionRepository;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public NatureOfInspection getNatureOfInspectionById(int id) {
		Optional<NatureOfInspection> natureOfInspectionOptional = natureOfInspectionRepository.findById(id);
		if(natureOfInspectionOptional.isPresent()) {
			return natureOfInspectionOptional.get();
		} else {
			throw new DataNotFoundException("NatureOfInspection not found by Id:" + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<NatureOfInspection> getAllNatureOfInspection() {
		return natureOfInspectionRepository.findAll();
	}

}
