package in.co.bel.olms.master.service.checklist.army;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.checklist.army.ChecklistScrutinyTenderDto;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyTender;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyTenderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistScrutinyTenderService {

	private final ChecklistScrutinyTenderRepository checklistScrutinyTenderRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(ChecklistScrutinyTenderService.class);

	private final Mapper mapper;

	public ChecklistScrutinyTender getChecklistScrutinyById(int id) {
		Optional<ChecklistScrutinyTender> checklistScrutinyTenderOptional = checklistScrutinyTenderRepo.findById(id);
		if (checklistScrutinyTenderOptional.isPresent()) {
			return checklistScrutinyTenderOptional.get();
		} else {
			throw new DataNotFoundException("Checklist Scrutiny Tender not found by Id: " + id);
		}
	}

	public List<ChecklistScrutinyTender> getAllChecklistScrutiny() {
		return checklistScrutinyTenderRepo.findAll();
	}
	public Boolean updateChecklistScrutiny(List<ChecklistScrutinyTenderDto> dtos) {
		boolean returnStatus = false;
		try {
		 List<ChecklistScrutinyTender> list=new ArrayList<>();
		for(ChecklistScrutinyTenderDto dto:dtos) {
       if(dto.getId()!=null) {
		Optional<ChecklistScrutinyTender> checklistScrutinyTenderOptional = checklistScrutinyTenderRepo.findById(dto.getId());
		if (checklistScrutinyTenderOptional.isPresent()) {
			ChecklistScrutinyTender checklistScrutinyTender=checklistScrutinyTenderOptional.get();
			checklistScrutinyTender.setReference(dto.getReference());
			checklistScrutinyTender.setStatus(dto.getStatus());
			checklistScrutinyTender.setSlNo(dto.getSlNo());	
			checklistScrutinyTender.setObsType("yes_no");
			checklistScrutinyTender.setSplit(true);			
		   checklistScrutinyTenderRepo.save(checklistScrutinyTender);     		
	     }
		} 
		else {
			ChecklistScrutinyTender checklistScrutinyTender=  mapper.map(dto,ChecklistScrutinyTender.class);
			 checklistScrutinyTenderRepo.save(checklistScrutinyTender);		
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
