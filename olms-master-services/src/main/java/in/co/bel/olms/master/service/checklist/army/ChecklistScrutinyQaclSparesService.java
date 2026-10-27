package in.co.bel.olms.master.service.checklist.army;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyQaclSparesDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQaclSpares;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyQaclSparesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyQaclSparesService {

	private final ChecklistScrutinyQaclSparesRepository qaclSparesRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyQaclSparesService.class);

	private final Mapper mapper;
	
	public ChecklistScrutinyQaclSpares getChecklistScrutinyQaclSparesById(int id) {
		Optional<ChecklistScrutinyQaclSpares>checklistScrutinyQaclSparesOptional  = qaclSparesRepository.findById(id);
		if(checklistScrutinyQaclSparesOptional.isPresent()) {
			return checklistScrutinyQaclSparesOptional.get();
		} else {
			throw new DataNotFoundException("Checklist Scrutiny Gi Form data not found by Id: " + id);
		}
	}

	public List<ChecklistScrutinyQaclSpares> getAllChecklistScrutinyQaclSpares() {
		return qaclSparesRepository.findAll();

	}
	public Boolean updateChecklistScrutinyQaclSpares(List<ChecklistScrutinyQaclSparesDto> dtos) {
		boolean returnStatus = false;
		try {
		for(ChecklistScrutinyQaclSparesDto dto:dtos) {
       if(dto.getId()!=null) {
		Optional<ChecklistScrutinyQaclSpares> checklistScrutinyQaclSparesOptional = qaclSparesRepository.findById(dto.getId());
		if (checklistScrutinyQaclSparesOptional.isPresent()) {
			ChecklistScrutinyQaclSpares checklistScrutinyQaclSpares=checklistScrutinyQaclSparesOptional.get();
			checklistScrutinyQaclSpares.setReference(dto.getReference());
//			checklistScrutinyQaclSpares.setStatus(dto.getStatus());
			checklistScrutinyQaclSpares.setSlNo(dto.getSlNo());	
			checklistScrutinyQaclSpares.setObsType("yes_no");
//			checklistScrutinyQaclSpares.setSplit(true);			
			qaclSparesRepository.save(checklistScrutinyQaclSpares);     		
	     }
		} 
		else {
			ChecklistScrutinyQaclSpares checklistScrutinyQaclSpares=  mapper.map(dto,ChecklistScrutinyQaclSpares.class);
			qaclSparesRepository.save(checklistScrutinyQaclSpares);		
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
