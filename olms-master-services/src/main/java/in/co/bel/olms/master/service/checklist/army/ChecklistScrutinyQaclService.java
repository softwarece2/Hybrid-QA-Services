package in.co.bel.olms.master.service.checklist.army;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyQaclDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQacl;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyQaclRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyQaclService {

	private final ChecklistScrutinyQaclRepository qaclRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyQaclService.class);

	private final Mapper mapper;
	
	public ChecklistScrutinyQacl getChecklistScrutinyQaclById(int id) {
		Optional<ChecklistScrutinyQacl>checklistScrutinyQaclOptional  = qaclRepository.findById(id);
		if(checklistScrutinyQaclOptional.isPresent()) {
			return checklistScrutinyQaclOptional.get();
		} else {
			throw new DataNotFoundException("Checklist Scrutiny Gi Form data not found by Id: " + id);
		}
	}

	public List<ChecklistScrutinyQacl> getAllChecklistScrutinyQacl() {
		return qaclRepository.findAll();

	}

	public List<ChecklistScrutinyQacl> getBypart(int partId) {
		List<ChecklistScrutinyQacl> qaclList  = qaclRepository.findBypartId(partId);
		return qaclList;
	}
	public Boolean updateChecklistScrutiny(List<ChecklistScrutinyQaclDto> dtos) {
		boolean returnStatus = false;
		try {
		for(ChecklistScrutinyQaclDto dto:dtos) {
       if(dto.getId()!=null) {
		Optional<ChecklistScrutinyQacl> checklistScrutinyTenderOptional = qaclRepository.findById(dto.getId());
		if (checklistScrutinyTenderOptional.isPresent()) {
			ChecklistScrutinyQacl checklistScrutinyTender=checklistScrutinyTenderOptional.get();
			checklistScrutinyTender.setReference(dto.getReference());
//			checklistScrutinyTender.setStatus(dto.getStatus());
			checklistScrutinyTender.setPart(dto.getPart());
			
			checklistScrutinyTender.setSlNo(dto.getSlNo());	
			checklistScrutinyTender.setObsType("yes_no");
			checklistScrutinyTender.setSplit(true);			
			qaclRepository.save(checklistScrutinyTender);     		
	     }
		} 
		else {
			ChecklistScrutinyQacl checklistScrutinyTender=  mapper.map(dto,ChecklistScrutinyQacl.class);
			qaclRepository.save(checklistScrutinyTender);		
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
