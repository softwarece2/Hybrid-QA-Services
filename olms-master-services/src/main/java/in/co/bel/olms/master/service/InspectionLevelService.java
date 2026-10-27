package in.co.bel.olms.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.InspectionLevel;
import in.co.bel.olms.master.repository.InspectionLevelRepository;
import lombok.RequiredArgsConstructor;

/**
* Service class for InspectionLevel entity
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-12 
*/

@Service
@RequiredArgsConstructor
public class InspectionLevelService {

	private final InspectionLevelRepository inspLevelRepository;

	public List<InspectionLevel> getInspectionLevels() {
		return inspLevelRepository.findAll();
	} 
}
