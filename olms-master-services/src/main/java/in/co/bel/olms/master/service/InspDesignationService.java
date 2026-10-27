package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.EmpDesignation;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmpDesignationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspDesignationService {

	private final EmpDesignationRepository inspDesignationRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public EmpDesignation getInspDesignationById(int id) {	
		Optional<EmpDesignation> inspDesignationOptional = inspDesignationRepository.findById(id);
		if(inspDesignationOptional.isPresent()) {
			return inspDesignationOptional.get();
		} else {
			throw new DataNotFoundException("InspDesignation Not Found By Id: " + id);
		}
				
	}
	
	/**
	 * 
	 * @return
	 */
	public List<EmpDesignation> getAllInspDesignation() {
		return inspDesignationRepository.findAll();
	}
}
