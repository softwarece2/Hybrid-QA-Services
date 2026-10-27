package in.co.bel.olms.master.service.checklist;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.checklist.ChecklistOlIB;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.ChecklistOlIBRepository;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 25-04-2023
 */

@Service
@RequiredArgsConstructor
public class ChecklistOlIbService {
	
	private final ChecklistOlIBRepository checklistOlRepository;

	public ChecklistOlIB getChecklistOlById(int id) {
		
		Optional<ChecklistOlIB> ChecklistOlOptional = checklistOlRepository.findById(id);
		if(ChecklistOlOptional.isPresent()) {
			return ChecklistOlOptional.get();
		} else {
			throw new DataNotFoundException("checklistOl ib not found with Id: " + id);
		}
	}
	
	public List<ChecklistOlIB> getAllChecklistOl() {
		return checklistOlRepository.findAll();
				
	}
}
