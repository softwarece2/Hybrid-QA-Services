package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.Sections;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SectionsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionsService {

	private final SectionsRepository sectionsRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Sections getSectionsById(int id) {
		Optional<Sections> sectionsOptional = sectionsRepository.findById(id);
		if(sectionsOptional.isPresent()) {
			return sectionsOptional.get();
		} else {
			throw new DataNotFoundException("Sections not found by Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<Sections> getAllSections() {
		return sectionsRepository.findAll();
	}
}
