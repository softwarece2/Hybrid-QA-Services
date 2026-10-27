package in.co.bel.olms.master.service.checklist.army;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyGiFormDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyGiForm;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyGiFormRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyGiFormService {

	private final ChecklistScrutinyGiFormRepository checklistScrutinyGiFormRepository;	
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyTenderService.class);

	private final Mapper mapper;
	
	public ChecklistScrutinyGiForm getChecklistScrutinyGiFormById(int id) {
		Optional<ChecklistScrutinyGiForm> checklistScrutinyGiFormOptional = checklistScrutinyGiFormRepository.findById(id);
		if(checklistScrutinyGiFormOptional.isPresent()) {
			return checklistScrutinyGiFormOptional.get();
		} else {
			throw new DataNotFoundException("Checklist Scrutiny Gi Form data not found by Id: " + id);
		}
	}
	
	
	public List<ChecklistScrutinyGiForm> getAllChecklistScrutinyGiForms() {
		return checklistScrutinyGiFormRepository.findAll();
	}
	
	public Boolean updateChecklistScrutinyGiForm(List<ChecklistScrutinyGiFormDto> dtos) {
		boolean returnStatus = false;
		try {
		for(ChecklistScrutinyGiFormDto dto:dtos) {
       if(dto.getId()!=null) {
		Optional<ChecklistScrutinyGiForm> checklistScrutinyTenderOptional = checklistScrutinyGiFormRepository.findById(dto.getId());
		if (checklistScrutinyTenderOptional.isPresent()) {
			ChecklistScrutinyGiForm checklistScrutinyGiForm=checklistScrutinyTenderOptional.get();
			checklistScrutinyGiForm.setReference(dto.getReference());
			checklistScrutinyGiForm.setStatus(dto.getStatus());
			checklistScrutinyGiForm.setSlNo(dto.getSlNo());	
			checklistScrutinyGiForm.setObsType("yes_no");
			checklistScrutinyGiForm.setSplit(true);			
			checklistScrutinyGiFormRepository.save(checklistScrutinyGiForm);     		
	     }
		} 
		else {
			ChecklistScrutinyGiForm checklistScrutinyGiForm=  mapper.map(dto,ChecklistScrutinyGiForm.class);
			checklistScrutinyGiFormRepository.save(checklistScrutinyGiForm);		
		}
		
		returnStatus = true;
		}
		
		return returnStatus;
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("Error in updating supply orders {}", e.getMessage());
		return false;
	}
	}

}
