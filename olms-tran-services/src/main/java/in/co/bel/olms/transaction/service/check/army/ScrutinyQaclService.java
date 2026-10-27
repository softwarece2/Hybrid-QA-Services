package in.co.bel.olms.transaction.service.check.army;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQacl;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.SoStatusRepository;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyQaclRepository;
import in.co.bel.olms.transaction.dto.ReturnOfferLetterDetDto;
import in.co.bel.olms.transaction.dto.ReturnOfferLetterDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQacl;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclAttachment;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclDet;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclAttachmentRepo;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.OlLineItemService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.transaction.service.ol.ReturnOfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyQaclService {

	private final ChecklistScrutinyQaclRepository checklistScrutinyQaclRepository;

	private final ScrutinyQaclRepository qaclRepository;

	private final ScrutinyQaclDetRepository qaclDetRepository;

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;

	private final SupplyOrderService supplyOrderService;

	private final ReturnOfferLetterService returnOfferLetterService;

	private final NotifyUtil notifyUtil;

	private final EmployeeRepository employeeRepository;

	private final ScrutinyQaclAttachmentRepo scrutinyQaclAttachmentRepo ;

	private final FileStorageService fileStorageService;

	private final SoStatusRepository soStatusRepository ;
	
	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

    private final OlLineItemService olLineItemService;
	
    private static final Logger logger =LoggerFactory.getLogger(ScrutinyQaclService.class);	

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param scrutinyQacl
	 * @return
	 */
	public ScrutinyQacl saveScrutinyQacl(@Valid ScrutinyQaclDto scrutinyQaclDto) {
		ScrutinyQacl scrutinyQacl;
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(scrutinyQaclDto.getOfferLetterId());
		if (scrutinyQaclDto.getId() != 0) {
			Optional<ScrutinyQacl> scrutinyQaclOpt = qaclRepository.findById(scrutinyQaclDto.getId());
			if (scrutinyQaclOpt.isPresent()) {
				scrutinyQacl = scrutinyQaclOpt.get();
			} else {
				throw new DataNotFoundException("Save : Scrutiny Qacl not found by  Id: " + scrutinyQaclDto.getId());
			}

		} else {
			scrutinyQacl = mapper.map(scrutinyQaclDto, ScrutinyQacl.class);
			scrutinyQacl.setCreatedDatetime(new Date());
		}
		scrutinyQacl.setOfferLetter(offerLetter);
		boolean isValid = entityValidator.isValidEntity(scrutinyQacl);
		if (isValid) {
			scrutinyQacl = qaclRepository.save(scrutinyQacl);
		} else {
			throw new DataNotValidException("ScrutinyQacl save:" + scrutinyQacl.toString());
		}

		scrutinyQacl = prepareScrutinyQaclObj(scrutinyQaclDto, scrutinyQacl);
		return scrutinyQacl;
	}
	
	/**
	 * 
	 * @param ScrutinyQaclDto
	 * @param ScrutinyQacl
	 * @return
	 */
	private ScrutinyQacl prepareScrutinyQaclObj(@Valid ScrutinyQaclDto scrutinyQaclDto, ScrutinyQacl scrutinyQacl) {
		List<ScrutinyQaclDet> qaclDets = new ArrayList<>();

		for (ScrutinyQaclDetDto detDto : scrutinyQaclDto.getQaclDetDtos()) {
			ScrutinyQaclDet qaclDet;
			if (detDto.getId() == 0) {
				qaclDet = mapper.map(detDto, ScrutinyQaclDet.class);
			} else {
				Optional<ScrutinyQaclDet> qaclDetOptional = qaclDetRepository.findById(detDto.getId());
				if (qaclDetOptional.isPresent()) {
					qaclDet = qaclDetOptional.get();
				} else {
					qaclDet = mapper.map(detDto, ScrutinyQaclDet.class);
				}
			}
			Optional<ChecklistScrutinyQacl> checklistScrutinyQaclOptional  = checklistScrutinyQaclRepository.findById(detDto.getChecklistScrutinyQaclId());
			if (checklistScrutinyQaclOptional.isPresent()) {
				qaclDet.setChecklistScrutinyQacl(checklistScrutinyQaclOptional.get());;
			} else {
				throw new DataNotFoundException(
						"ScrutinyQacl save: checklist not found by Id: " + detDto.getChecklistScrutinyQaclId());
			}
			qaclDet.setObsSts(detDto.getObsSts());
			qaclDet.setRemarks(detDto.getRemarks());
			qaclDet.setScrutinyQacl(scrutinyQacl);

			// Add code for the observation status

			boolean isvalid = entityValidator.isValidEntity(qaclDet);
			if (isvalid) {
				qaclDets.add(qaclDet);
			} else {
				throw new DataNotValidException("ScrutinyQacl save: det " + qaclDet.toString());
			}

		}
		qaclDets = qaclDetRepository.saveAll(qaclDets);
		scrutinyQacl.setScrutinyQaclDets(qaclDets);
		return scrutinyQacl;
	}
	
	/**
	 * submit the scrutiny of QACL
	 * @param scrutinyQacl
	 * @return
	 */

	public ScrutinyQacl submitScrutinyQacl(@Valid ScrutinyQaclDto scrutinyQaclDto) {
		
		ScrutinyQacl scrutinyQacl = saveScrutinyQacl(scrutinyQaclDto);
		scrutinyQacl.setCreatedDatetime(new Date());
		scrutinyQacl.setSubmittedDate(new Date());
		boolean isValid = entityValidator.isValidEntity(scrutinyQacl);
		if (isValid) {
			scrutinyQacl = qaclRepository.save(scrutinyQacl);
		}
		else {
			throw new DataNotFoundException("ScrutinyQacl Submit:" + scrutinyQacl.toString());
		}
		try {
			if (Boolean.TRUE.equals(scrutinyQaclDto.getIsReturnOL())) {
				returnOL(scrutinyQaclDto, scrutinyQacl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return scrutinyQacl;

	}
	
	/**
	 * 
	 * @param olId
	 * @return
	 */
	public ScrutinyQaclDto getScrutinyQaclByOlId(int olid) {
		ScrutinyQaclDto scrutinyQaclDto = new ScrutinyQaclDto();
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olid);
		ScrutinyQacl scrutinyQacl = qaclRepository.getScrutinyQaclByOfferLetter(offerLetter);
		 List<OlLineItem> olLineItems = olLineItemService.getOlLineItemByOlId(olid);
		List<ScrutinyQaclDetDto> qaclDetDtos = new ArrayList<>();
		if (scrutinyQacl == null) {
			scrutinyQaclDto.setId(0);
			scrutinyQaclDto.setSubmittedDate(null);
			List<ChecklistScrutinyQacl> chkSos = checklistScrutinyQaclRepository.findAll();
			for (ChecklistScrutinyQacl checklistScrutinyQacl : chkSos) {
				ScrutinyQaclDetDto qaclDetDto = new ScrutinyQaclDetDto();
				qaclDetDto.setId(0);
				qaclDetDto.setChecklistScrutinyQaclId(checklistScrutinyQacl.getId());
				qaclDetDto.setSlNo(checklistScrutinyQacl.getSlNo());
				qaclDetDto.setReference(checklistScrutinyQacl.getReference());
				qaclDetDto.setSplit(checklistScrutinyQacl.getSplit());
				qaclDetDto.setObsType(checklistScrutinyQacl.getObsType());
				qaclDetDto.setPart(checklistScrutinyQacl.getPart());
				qaclDetDto.setObsSts("");
				qaclDetDto.setRemarks("");
				qaclDetDtos.add(qaclDetDto);
			}

		} else {
			scrutinyQaclDto = mapper.map(scrutinyQacl, ScrutinyQaclDto.class);
			OfferLetter offerLtr = scrutinyQacl.getOfferLetter();
			scrutinyQaclDto.setOfferLetterId(offerLtr.getId());
			if(scrutinyQacl.getSoStatus() != null) {
				scrutinyQaclDto.setSoStatus(scrutinyQacl.getSoStatus().getStatus());
				scrutinyQaclDto.setSoStatusId(scrutinyQacl.getSoStatus().getId());
			}
			scrutinyQaclDto.setQaoSubmittedDate(scrutinyQacl.getQaoSubmittedDate());
			
			List<Object[]> response = scrutinyQaclAttachmentRepo.getByQaclIdId(scrutinyQacl.getId());
			if (response != null) {
				List<String> attachment = new ArrayList<>();
				for (Object[] obj : response) {
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + obj[2].toString();

					attachment.add(fileDownloadUri);
					scrutinyQaclDto.setQalFileUploads(attachment);
				}
			}
				
			if(null!=scrutinyQaclDto.getSubmittedDate())
			{
				scrutinyQaclDto.setSubmittedDate(dateFormat.format(scrutinyQacl.getSubmittedDate()));
			}
			
			
			for (ScrutinyQaclDet qaclDet : scrutinyQacl.getScrutinyQaclDets()) {
				ScrutinyQaclDetDto detDto = new ScrutinyQaclDetDto();
				ChecklistScrutinyQacl qacl = qaclDet.getChecklistScrutinyQacl();
				detDto.setId(qaclDet.getId());
				detDto.setChecklistScrutinyQaclId(qacl.getId());
				detDto.setSlNo(qacl.getSlNo());
				detDto.setReference(qacl.getReference());
				detDto.setSplit(qacl.getSplit());
				detDto.setObsType(qacl.getObsType());
				detDto.setPart(qacl.getPart());
				detDto.setObsSts(qaclDet.getObsSts());
				detDto.setRemarks(qaclDet.getRemarks());
				qaclDetDtos.add(detDto);
			}
		}

		Collections.sort(qaclDetDtos, new Comparator<ScrutinyQaclDetDto>() {
			@Override
			public int compare(ScrutinyQaclDetDto o1, ScrutinyQaclDetDto o2) {

				return o1.getChecklistScrutinyQaclId().compareTo(o2.getChecklistScrutinyQaclId());
			}

		});

		scrutinyQaclDto.setQaclDetDtos(qaclDetDtos);
		int supplyOrderId = offerLetter.getSaleOrder().getSupplyOrder().getId();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(supplyOrderId);
		scrutinyQaclDto.setOrderDate(supplyOrder.getOrderDate());
		scrutinyQaclDto.setOrderNo(supplyOrder.getOrderNo());
		scrutinyQaclDto.setDescription(supplyOrder.getDescription());
		scrutinyQaclDto.setOfferDate(offerLetter.getOfferDate());
		scrutinyQaclDto.setOlNumber(offerLetter.getOlNumber());
		scrutinyQaclDto.setOfferedQty(olLineItems.get(0).getOfferedQty());
		return scrutinyQaclDto;
	}
	
	/**
	 * 
	 * @param scrutinyQaclDets
	 * @return
	 */
	public List<ScrutinyQaclDetDto> prepareQaclDets(List<ScrutinyQaclDet> scrutinyQaclDets) {
		List<ScrutinyQaclDetDto> scrutinyQaclDetDtos = new ArrayList<>();
		for (ScrutinyQaclDet qaclDet : scrutinyQaclDets) {
			ScrutinyQaclDetDto dto = new ScrutinyQaclDetDto();
			dto.setId(qaclDet.getId());
			ChecklistScrutinyQacl checklistScrutinyQacl = qaclDet.getChecklistScrutinyQacl();
			dto.setChecklistScrutinyQaclId(checklistScrutinyQacl.getId());
			dto.setSlNo(checklistScrutinyQacl.getSlNo());
			dto.setReference(checklistScrutinyQacl.getReference());
			if (StringUtils.isNotBlank(qaclDet.getRemarks())) {
				dto.setRemarks(qaclDet.getRemarks());
			} else {
				dto.setRemarks("");
			}
			dto.setObsType(checklistScrutinyQacl.getObsType());
			if (StringUtils.isNotBlank(dto.getObsSts())) 
			{
				dto.setObsSts(dto.getObsSts());
			}		
			else {
				dto.setObsSts("");
			}
			if(checklistScrutinyQacl.getSplit()!=null)
			dto.setSplit(checklistScrutinyQacl.getSplit());
			scrutinyQaclDetDtos.add(dto);
		}
		return scrutinyQaclDetDtos;
	}

	
	private void returnOL(@Valid ScrutinyQaclDto scrutinyQaclDto, ScrutinyQacl scrutinyQacl) {
		try {
			ReturnOfferLetterDto returndto = new ReturnOfferLetterDto();
			returndto.setOfferLetterId(scrutinyQaclDto.getOfferLetterId());
			List<ScrutinyQaclDet> scrutinyQaclDets = scrutinyQacl.getScrutinyQaclDets();
			List<ReturnOfferLetterDetDto> retDetDtos = new ArrayList<>();
			for (ScrutinyQaclDet detoBO : scrutinyQaclDets) {
				if (detoBO.getObsSts().equals("no")) {
					if (StringUtils.isNoneBlank(detoBO.getRemarks())) {
						ReturnOfferLetterDetDto detDto = new ReturnOfferLetterDetDto();
						detDto.setObservation(detoBO.getRemarks());
						retDetDtos.add(detDto);
					} else {
						throw new DataNotValidException(
								"Return OL Observation is empty : " + scrutinyQaclDto.getOfferLetterId());
					}
				}

			}
			if (!retDetDtos.isEmpty()) {
				returndto.setDetDtos(retDetDtos);
				try {
					returnOfferLetterService.saveReturnOfferLetter(returndto);
					try {
						Integer empId = employeeRepository.getEmpIdByolId(scrutinyQacl.getOfferLetter().getId());
						offerLetterService.updateOlStatus(scrutinyQaclDto.getOfferLetterId(),
								ApplicationConstants.OL_RETURNED);
						notifyUtil.NotifySBUnCqae(
								"Offer Letter is returned for the OL no " + scrutinyQacl.getOfferLetter().getOlNumber(), 1,
								scrutinyQacl.getOfferLetter().getOlNumber(), 1, NotificationConstants.OL,
								scrutinyQacl.getOfferLetter().getSaleOrder().getSupplyOrder().getSalesGroup().getId(),
								empId);
					} catch (Exception e) {
						logger.error("Notification error in return Offer Letter for the order no {} ", e.getMessage());
						logger.error("error: ",e);
					}

				} catch (Exception e) {
					logger.error("error: ",e);
				}
			}
		} catch (Exception e) {
			logger.error("error: {}",e.getMessage());
		}
	}

	public Boolean updateQaclByQAO(ScrutinyQaclDto scrutinyQaclDto, MultipartFile[] attachments) {
		boolean returnStatus = false;
		try {
			if (scrutinyQaclDto != null) {

				Optional<ScrutinyQacl> scrutinyQaclOptional = qaclRepository.findById(scrutinyQaclDto.getId());
				if (scrutinyQaclOptional.isPresent()) {
					ScrutinyQacl scrutinyQacl = scrutinyQaclOptional.get();
					scrutinyQacl.setQaoRemarks(scrutinyQaclDto.getQaoRemarks());
					Optional<SoStatus> soStatusOptional   = soStatusRepository.findById(scrutinyQaclDto.getSoStatusId());
					if (soStatusOptional.isPresent()) {
						scrutinyQacl.setSoStatus(soStatusOptional.get());
					}
					scrutinyQacl.setQaoSubmittedDate(new Date());
					 scrutinyQacl = qaclRepository.save(scrutinyQacl);

					List<ScrutinyQaclAttachment> scrutinyQaclAttachments = new ArrayList<>();
					if (attachments != null && attachments.length > 0) {
						saveScrutinyQaclAttachmentt(attachments, scrutinyQacl, scrutinyQaclAttachments);
					}
					scrutinyQaclAttachmentRepo.saveAll(scrutinyQaclAttachments);
					returnStatus = true;
				}
			}

		} catch (Exception e) {
			logger.error("error: ",e);
			logger.error("Error in updating supply order approval {}", e.getMessage());
		}
		return returnStatus;

	}

	private void saveScrutinyQaclAttachmentt(MultipartFile[] files, ScrutinyQacl savedSoApprovalDetails,List<ScrutinyQaclAttachment> scrutinyQaclAttachments) {
		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(files[i], savedSoApprovalDetails.getId(), i);
			if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				ScrutinyQaclAttachment attachment = new ScrutinyQaclAttachment();
				attachment.setCreatedDate(date);
				attachment.setFileName(fileName);
				attachment.setScrutinyQacl(savedSoApprovalDetails);
				scrutinyQaclAttachments.add(attachment);
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ fileName;
				uploadedFilesUrl.add(fileDownloadUri);
			}
		}
	}
	
}
