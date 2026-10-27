package in.co.bel.olms.transaction.helper.poobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.dao.ChecklistFileCloseDao;
import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.FileClosingFormDetDto;
import in.co.bel.olms.transaction.dto.FileClosingFormDto;
import in.co.bel.olms.transaction.entity.FileClosingForm;
import in.co.bel.olms.transaction.entity.FileClosingFormDet;
import in.co.bel.olms.transaction.repository.FileClosingFormDetRepository;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileClosingFormHelper {

	private final EntityValidator entityValidator;

	private final ChecklistFileCloseDao checklistFileCloseDao;

	private final FileClosingFormDetRepository fileClosingFormDetRepo;

	private final Mapper mapper;
	
	public List<FileClosingFormDet> createDetBos(List<FileClosingFormDetDto> fileClosingFormDetDtos,
			FileClosingForm fileClosingForm,FileClosingFormDto fileClosingFormDto) {
		FileClosingFormDet fileClosingFormDet;
		List<FileClosingFormDet> filecloseDets = new ArrayList<>();
			for (FileClosingFormDetDto detDto : fileClosingFormDetDtos) {
				//SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fileClosingFormDto.getSupplyOrderId());
				ChecklistFileClose cf = checklistFileCloseDao.getChecklist(detDto.getChecklistId());	
				if(detDto.getId() != 0) {
					Optional<FileClosingFormDet> obsOptional = fileClosingFormDetRepo.findById(detDto.getId());
					if(obsOptional.isPresent()) {
						fileClosingFormDet = obsOptional.get();
					} else { 
						throw new DataNotFoundException("Save : FileClosingFormDetForm not found By Id: " + detDto.getId());
					}
				}
				else {
					fileClosingFormDet = mapper.map(fileClosingFormDetDtos, FileClosingFormDet.class);
				
					fileClosingFormDet = preparefilecloseformDetObsObj(fileClosingFormDetDtos, fileClosingFormDet);
					FileClosingFormDet detBo = new FileClosingFormDet();					
						detBo.setChecklistFileClose(cf);
						detBo.setRemarks(detDto.getRemarks());
						detBo.setObsSts(detDto.getObsSts());
						detBo.setFileClosingForm(fileClosingForm);
	
					boolean isvalid = entityValidator.isValidEntity(detBo);
		
					if (isvalid) {
						filecloseDets.add(detBo);
					} else {
						throw new DataNotValidException("FileClosingForm create: det " + detBo.toString());
					}
				}
			}
			
					return filecloseDets;
		}
	
	private FileClosingFormDet preparefilecloseformDetObsObj(List<FileClosingFormDetDto> fileClosingFormDetDtos,FileClosingFormDet fileclosingFormDet) {
		return fileclosingFormDet;
	}
		
}
