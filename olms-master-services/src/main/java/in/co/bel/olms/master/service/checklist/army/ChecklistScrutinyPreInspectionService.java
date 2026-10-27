package in.co.bel.olms.master.service.checklist.army;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyPreInspectionDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyPreInspection;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyPreInspectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyPreInspectionService {

	private final ChecklistScrutinyPreInspectionRepository checklistScrutinyPreInspectionRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyTenderService.class);

	private final Mapper mapper;

	
	public ChecklistScrutinyPreInspection getChecklistScrutinyPreInspectionById(int id) {
		Optional<ChecklistScrutinyPreInspection> checklistScrutinyPreInspectionOptional = checklistScrutinyPreInspectionRepository.findById(id);
		if(checklistScrutinyPreInspectionOptional.isPresent()) {
			return checklistScrutinyPreInspectionOptional.get();
		} else {
			throw new DataNotFoundException("Checklist scrutiny pre inspection data not found by Id: " + id);
		}
	}
	
	public List<ChecklistScrutinyPreInspection> getAllChecklistScrutinyPreInspections() {
		return checklistScrutinyPreInspectionRepository.findAll();
	}
	
	public Boolean updateChecklistScrutinyPreInspection(List<ChecklistScrutinyPreInspectionDto> dtos) {
		boolean returnStatus = false;
		try {
			for(ChecklistScrutinyPreInspectionDto dto:dtos) {
				if(dto.getId()!=null) {
					Optional<ChecklistScrutinyPreInspection> checklistScrutinyTenderOptional = checklistScrutinyPreInspectionRepository.findById(dto.getId());
					if (checklistScrutinyTenderOptional.isPresent()) {
						ChecklistScrutinyPreInspection checklistScrutinyPreInspection=checklistScrutinyTenderOptional.get();
						checklistScrutinyPreInspection.setReference(dto.getReference());
			//			checklistScrutinyPreInspection.setStatus(dto.getStatus());
						checklistScrutinyPreInspection.setSlNo(dto.getSlNo());	
						checklistScrutinyPreInspection.setObsType("yes_no");
						checklistScrutinyPreInspection.setSplit(true);			
						checklistScrutinyPreInspectionRepository.save(checklistScrutinyPreInspection);     		
				     }
				} 
				else {
					ChecklistScrutinyPreInspection checklistScrutinyPreInspection=  mapper.map(dto,ChecklistScrutinyPreInspection.class);
					checklistScrutinyPreInspectionRepository.save(checklistScrutinyPreInspection);		
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
