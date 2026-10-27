package in.co.bel.olms.master.service.checklist.army;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyThreeTierDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyThreeTier;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyThreeTierRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyThreeTierService {

	private final ChecklistScrutinyThreeTierRepository checklistScrutinyThreeTierRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyTenderService.class);

	private final Mapper mapper;
	
	public ChecklistScrutinyThreeTier getChecklistScrutinyThreeTierById(int id) {
		Optional<ChecklistScrutinyThreeTier> checklistScrutinyThreeTierOptional = checklistScrutinyThreeTierRepository.findById(id);
		if(checklistScrutinyThreeTierOptional.isPresent()) {
			return checklistScrutinyThreeTierOptional.get();
		} else {
			throw new DataNotFoundException("Checklist scrutiny three tier data not found by Id: " + id);
		}
	}
	
	
	public List<ChecklistScrutinyThreeTier> getAllChecklistScrutinyThreeTiers() {
		return checklistScrutinyThreeTierRepository.findAll();
	}
	
	public Boolean updateChecklistScrutinyThreeTier(List<ChecklistScrutinyThreeTierDto> dtos) {
		boolean returnStatus = false;
		try {
		for(ChecklistScrutinyThreeTierDto dto:dtos) {
       if(dto.getId()!=null) {
		Optional<ChecklistScrutinyThreeTier> checklistScrutinyTenderOptional = checklistScrutinyThreeTierRepository.findById(dto.getId());
		if (checklistScrutinyTenderOptional.isPresent()) {
			ChecklistScrutinyThreeTier checklistScrutinyThreeTier=checklistScrutinyTenderOptional.get();
			checklistScrutinyThreeTier.setReference(dto.getReference());
			checklistScrutinyThreeTier.setStatus(dto.getStatus());
			checklistScrutinyThreeTier.setSlNo(dto.getSlNo());	
			checklistScrutinyThreeTier.setObsType("yes_no");
			checklistScrutinyThreeTier.setSplit(true);			
			checklistScrutinyThreeTierRepository.save(checklistScrutinyThreeTier);     		
	     }
		} 
		else {
			ChecklistScrutinyThreeTier checklistScrutinyThreeTier=  mapper.map(dto,ChecklistScrutinyThreeTier.class);
			checklistScrutinyThreeTierRepository.save(checklistScrutinyThreeTier);		
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
