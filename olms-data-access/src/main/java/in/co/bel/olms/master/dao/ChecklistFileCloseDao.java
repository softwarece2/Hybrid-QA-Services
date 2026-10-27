package in.co.bel.olms.master.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.ChecklistFileClose;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ChecklistFileCloseRepository;

@Component
public class ChecklistFileCloseDao{
	
	@Autowired
	ChecklistFileCloseRepository checklistRepository;
	
	public ChecklistFileClose getChecklist(int id) {
	Optional<ChecklistFileClose> fileOptional = checklistRepository.findById(id);
	if (fileOptional.isPresent()) {
		return fileOptional.get();
	} else {
		throw new DataNotFoundException("file not found with Id: " + id);
	}
	
}
}
