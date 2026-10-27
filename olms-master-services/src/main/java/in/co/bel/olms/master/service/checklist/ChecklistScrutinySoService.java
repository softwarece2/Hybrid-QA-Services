package in.co.bel.olms.master.service.checklist;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.checklist.ChecklistScrutinySO;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.ChecklistScrutinySORepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinySoService {

	private final ChecklistScrutinySORepository checklistScrutinyRepo;

	public ChecklistScrutinySO getChecklistScrutinyById(int id) {
		Optional<ChecklistScrutinySO> checklistScrutinyOptional = checklistScrutinyRepo.findById(id);
		if(checklistScrutinyOptional.isPresent()) {
			return checklistScrutinyOptional.get();
		} else {
			throw new DataNotFoundException("Checklist Scrutiny not found by Id: " + id);
		}
	}

	public List<ChecklistScrutinySO> getAllChecklistScrutiny() {
		return checklistScrutinyRepo.findAll();
	}

}
