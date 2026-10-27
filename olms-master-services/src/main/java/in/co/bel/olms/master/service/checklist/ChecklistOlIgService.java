package in.co.bel.olms.master.service.checklist;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.checklist.ChecklistOlIG;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.ChecklistOlIGRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistOlIgService {

	private final ChecklistOlIGRepository checklistOlRepository;

	public ChecklistOlIG getChecklistOlById(int id) {
		
		Optional<ChecklistOlIG> ChecklistOlOptional = checklistOlRepository.findById(id);
		if(ChecklistOlOptional.isPresent()) {
			return ChecklistOlOptional.get();
		} else {
			throw new DataNotFoundException("checklistOl not found with Id: " + id);
		}
	}
	
	public List<ChecklistOlIG> getAllChecklistOl() {
		return checklistOlRepository.findAll();
				
	}
}
