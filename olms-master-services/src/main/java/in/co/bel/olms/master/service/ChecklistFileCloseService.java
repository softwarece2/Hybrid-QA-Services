package in.co.bel.olms.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.repository.ChecklistFileCloseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistFileCloseService {

	private final ChecklistFileCloseRepository checklistfilecloseRepository;

	public List<ChecklistFileClose> getAll(){
		return checklistfilecloseRepository.findAll();
	}
}
