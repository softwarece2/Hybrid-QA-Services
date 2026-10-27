package in.co.bel.olms.transaction.service.so;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.ChecklistFileCloseRepository;
import in.co.bel.olms.transaction.dto.FileClosingFormDetDto;
import in.co.bel.olms.transaction.dto.FileClosingFormDto;
import in.co.bel.olms.transaction.entity.FileClosingForm;
import in.co.bel.olms.transaction.entity.FileClosingFormDet;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.FileClosingFormDetRepository;
import in.co.bel.olms.transaction.repository.FileClosingFormRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.validator.EntityValidator;
import in.co.bel.olms.validator.InputValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileClosingFormService {

	private final FileClosingFormRepository fileClosingFormRepo;

	private final Mapper mapper;

	private final SupplyOrderService supplyOrderService;

	private final ChecklistFileCloseRepository checklistfilecloseRepository;

	private final EntityValidator entityValidator;

	private final FileClosingFormDetRepository fileClosingFormDetRepo;
	
	Logger logger = LoggerFactory.getLogger(FileClosingFormService.class);
	/**
	 * 
	 * @param fileClosingFormDto
	 * @return
	 */
	
	
	public FileClosingForm submitFile(FileClosingFormDto fileClosingFormDto) {
		FileClosingForm fileClosingForm = saveFileClosingForms(fileClosingFormDto);
		fileClosingForm.setCreatedDatetime(new Date());
		fileClosingForm.setSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(fileClosingForm);
		if (isvalid) {
			fileClosingForm = fileClosingFormRepo.save(fileClosingForm);
		} else {
			throw new DataNotValidException("FileClosingForm submit:  " + fileClosingForm.toString());
		}
		return fileClosingForm;
	}
	
	private boolean isValidDto(FileClosingFormDto fileClosingFormDto) {

		for (FileClosingFormDetDto detDto : fileClosingFormDto.getDetDtos()) {
			if (StringUtils.isNotBlank(detDto.getRemarks())) {
				if (!InputValidation.checkRemark(detDto.getRemarks())) {
					throw new DataNotValidException("FileClosingForm det remarks:  " + detDto.getRemarks());
				}
			}
		}

		return true;
	}
	
	public FileClosingForm saveFileClosingForms(FileClosingFormDto fileClosingFormDto) {
		isValidDto(fileClosingFormDto);

		FileClosingForm fileClosingForm = mapper.map(fileClosingFormDto, FileClosingForm.class);

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fileClosingFormDto.getSupplyOrderId());
		fileClosingForm.setSupplyOrder(supplyOrder);
		fileClosingForm.setCreatedDatetime(new Date());
		boolean isvalid = entityValidator.isValidEntity(fileClosingForm);
		if (isvalid) {
			fileClosingForm = fileClosingFormRepo.save(fileClosingForm);
		} else {
			throw new DataNotValidException("FileClosingForm save:  " + fileClosingForm.toString());
		}

		List<FileClosingFormDet> fileClosingFormDets = prepareDetBos(fileClosingFormDto.getDetDtos(), fileClosingForm);
		fileClosingFormDets = fileClosingFormDetRepo.saveAll(fileClosingFormDets);
		fileClosingForm.setfileclosingFormDets(fileClosingFormDets);
		isvalid = entityValidator.isValidEntity(fileClosingForm);
		if (isvalid) {
			fileClosingForm = fileClosingFormRepo.save(fileClosingForm);
		} else {
			throw new DataNotValidException("FileClosingForm save:  " + fileClosingForm.toString());
		}
		return fileClosingForm;
	}
	
	
	private List<FileClosingFormDet> prepareDetBos(List<FileClosingFormDetDto> fileClosingFormDetDtos, FileClosingForm fileClosingForm) {
		List<FileClosingFormDet> fileDets = new ArrayList<>();
		for (FileClosingFormDetDto detDto : fileClosingFormDetDtos) {
			FileClosingFormDet detBo;

			if (detDto.getId() == 0) {
				detBo = new FileClosingFormDet();
			} else {
				Optional<FileClosingFormDet> detBoopt = fileClosingFormDetRepo.findById(detDto.getId());
				if (detBoopt.isPresent()) {
					detBo = detBoopt.get();
				} else {
					detBo = new FileClosingFormDet();
				}
			}

			Optional<ChecklistFileClose> checkfileClose = checklistfilecloseRepository.findById(detDto.getChecklistId());
			if (checkfileClose.isPresent()) {
				detBo.setChecklistFileClose(checkfileClose.get());
			} else {
				throw new DataNotFoundException("FileClosingForm save: checklist not found by Id: " + detDto.getChecklistId());
			}
			detBo.setRemarks(detDto.getRemarks());
			detBo.setObsSts(detDto.getObsSts());
			detBo.setFileClosingForm(fileClosingForm);

			boolean isvalid = entityValidator.isValidEntity(detBo);
			if (isvalid) {
				fileDets.add(detBo);
			} else {
				throw new DataNotValidException("FileClosingForm save: det " + detBo.toString());
			}
		}
		return fileDets;
	}
	
	public FileClosingFormDto convertBoToentity(FileClosingForm fileClosingForm)
	{
		FileClosingFormDto fileClosingFormDto = mapper.map(fileClosingForm, FileClosingFormDto.class);
		try {
			if(fileClosingForm.getCreatedDatetime() != null ) {
				fileClosingFormDto.setCreatedDatetime(fileClosingForm.getCreatedDatetime());
			}
		} catch (Exception e) {
			logger.error("Error occured {}", e.getMessage());
		}	
		List<FileClosingFormDetDto> fileClosingFormDetDtos = prepareDetBoToDto(fileClosingForm.getfileclosingFormDets());
		fileClosingFormDto.setDetDtos(fileClosingFormDetDtos);
		return fileClosingFormDto;
	}
	
	public List<FileClosingFormDetDto> prepareDetBoToDto(List<FileClosingFormDet> fileClosingFormDets) {

		List<FileClosingFormDetDto> fileDetDtos = new ArrayList<>();
		for (FileClosingFormDet fileCloseDet : fileClosingFormDets) {
			FileClosingFormDetDto fileDetDto = new FileClosingFormDetDto();
			fileDetDto.setId(fileCloseDet.getId());
			ChecklistFileClose check = fileCloseDet.getChecklistFileClose();
			fileDetDto.setChecklistId(check.getId());
			fileDetDto.setSlNo(check.getSlNo());
			fileDetDto.setReference(check.getReference());
			if (StringUtils.isNotBlank(fileCloseDet.getRemarks())) {
				fileDetDto.setRemarks(fileCloseDet.getRemarks());
			} else {
				fileDetDto.setRemarks("");
			}
			fileDetDto.setObsType(check.getObsType());
			fileDetDto.setObsSts(fileCloseDet.getObsSts());
			fileDetDto.setSplit(check.getSplit());
			fileDetDtos.add(fileDetDto);
		}
		Collections.sort(fileDetDtos, new Comparator<FileClosingFormDetDto>() {
			@Override
			public int compare(FileClosingFormDetDto o1, FileClosingFormDetDto o2) {
				return o1.getChecklistId().compareTo(o2.getChecklistId());
			}
		});
		return fileDetDtos;
	}
	
	
	public FileClosingFormDto getFileCloseFormById(int soId) {
		FileClosingFormDto dto = new FileClosingFormDto();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		FileClosingForm fileclosingForm = fileClosingFormRepo.getFileCloseFormBySupplyOrder(supplyOrder);
		
		List<FileClosingFormDetDto> detDto = new ArrayList<>();
		if(fileclosingForm == null) {
			dto.setId(0);
			List<ChecklistFileClose> checkfile = checklistfilecloseRepository.findAll();
			for(ChecklistFileClose cf : checkfile) {
				FileClosingFormDetDto fileDetdto = new FileClosingFormDetDto();
				fileDetdto.setId(0);
				fileDetdto.setChecklistId(cf.getId());
				fileDetdto.setSlNo(cf.getSlNo());
				fileDetdto.setReference(cf.getReference());
				fileDetdto.setSplit(cf.getSplit());
				fileDetdto.setObsType(cf.getObsType());
				fileDetdto.setObsSts("");
				fileDetdto.setRemarks("");
				detDto.add(fileDetdto);
			}
		}else {
			dto = mapper.map(fileclosingForm, FileClosingFormDto.class);
			SupplyOrder so = fileclosingForm.getSupplyOrder();
			dto.setSupplyOrderId(so.getId());
			for(FileClosingFormDet detForm : fileclosingForm.getfileclosingFormDets()) {
				FileClosingFormDetDto detdtoForm = new FileClosingFormDetDto();
				ChecklistFileClose checkfileclose = detForm.getChecklistFileClose();
				detdtoForm.setId(detForm.getId());
				detdtoForm.setChecklistId(checkfileclose.getId());
				detdtoForm.setSlNo(checkfileclose.getSlNo());
				detdtoForm.setReference(checkfileclose.getReference());
				detdtoForm.setSplit(checkfileclose.getSplit());
				detdtoForm.setObsType(checkfileclose.getObsType());
				detdtoForm.setObsSts(detForm.getObsSts());
				detdtoForm.setRemarks(detForm.getRemarks());
				detDto.add(detdtoForm);
			}
		}
		
		Collections.sort(detDto,new Comparator<FileClosingFormDetDto>() {
			@Override
			public int compare(FileClosingFormDetDto o1,FileClosingFormDetDto o2) {
				return o1.getChecklistId().compareTo(o2.getChecklistId());
			}
		});
		
		dto.setDetDtos(detDto);
		return dto;
	}
}
