package in.co.bel.olms.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.QapReference;
import in.co.bel.olms.master.repository.QapReferenceRepository;
import lombok.RequiredArgsConstructor;

/**
* Service class for QapReference entity
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-17 
*/

@Service
@RequiredArgsConstructor
public class QapReferenceService {

	private final QapReferenceRepository qapRefRepository;

	public List<QapReference> getQapReferences() {
		return qapRefRepository.findAll();
	} 

}
