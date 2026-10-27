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

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyPreInspection;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.SoStatusRepository;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyPreInspectionRepository;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyPreInspectionDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyPreInspectionDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyPreInspection;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyPreInspectionAttachment;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyPreInspectionDet;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyPreInspectionAttachmentRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyPreInspectionDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyPreInspectionRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyPreInspectionService {

	private final ChecklistScrutinyPreInspectionRepository checklistScrutinyPreInspectionRepository;

	private final ScrutinyPreInspectionRepository scrutinyPreInspectionRepository;

	private final ScrutinyPreInspectionDetRepository scrutinyPreInspectionDetRepository;

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;

	private final FileStorageService fileStorageService;

	private final SoStatusRepository soStatusRepository ;

	private final ScrutinyPreInspectionAttachmentRepository scrutinyPreInspectionAttachmentRepository;
	
	@Value("${file.domain-url}")
	private String domainUrl;
	
	@Value("${spring.application.name}")
	private String applicationName;

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyPreInspectionService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	public ScrutinyPreInspection saveScrutinyPreInspection(ScrutinyPreInspectionDto scrutinyPreInspectionDto) {

		List<ScrutinyPreInspectionDet> scrutinyPreInspectionDetList;
		ScrutinyPreInspection scrutinyPreInspection;
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(scrutinyPreInspectionDto.getOfferLetterId());
		if (scrutinyPreInspectionDto.getId() != 0) {

			Optional<ScrutinyPreInspection> scrutinyPreInspectionOpt = scrutinyPreInspectionRepository.findById(scrutinyPreInspectionDto.getId());
			if (scrutinyPreInspectionOpt.isPresent()) {
				scrutinyPreInspection = scrutinyPreInspectionOpt.get();
				scrutinyPreInspection.setModifiedDatetime(new Date());
			} else {
				throw new DataNotFoundException(
						"Save : Scrutiny Pre Inspection not found by  Id: " + scrutinyPreInspectionDto.getId());
			}

		} else {
			scrutinyPreInspection = mapper.map(scrutinyPreInspectionDto, ScrutinyPreInspection.class);
			scrutinyPreInspection.setCreatedDatetime(new Date());
		}
		scrutinyPreInspection.setOfferLetter(offerLetter);
		boolean isValid = entityValidator.isValidEntity(scrutinyPreInspection);
		if (isValid) {
			scrutinyPreInspection = scrutinyPreInspectionRepository.save(scrutinyPreInspection);
		} else {
			throw new DataNotValidException("ScrutinyPreInspection save:" + scrutinyPreInspection.toString());
		}

		scrutinyPreInspectionDetList = prepareScrutinyPreInspectionObj(scrutinyPreInspectionDto, scrutinyPreInspection);
		scrutinyPreInspection.setScrutinyPreInspectionDets(scrutinyPreInspectionDetList);
		return scrutinyPreInspection;
	}

	private List<ScrutinyPreInspectionDet> prepareScrutinyPreInspectionObj(ScrutinyPreInspectionDto scrutinyPreInspectionDto,
			ScrutinyPreInspection scrutinyPreInspection) {
		List<ScrutinyPreInspectionDet> scrutinyPreInspectionDets = new ArrayList<>();
		for (ScrutinyPreInspectionDetDto detDto : scrutinyPreInspectionDto.getScrutinyPreInspectionDetDtos()) {
			ScrutinyPreInspectionDet detPreInspection;
			if (detDto.getId() == 0) {
				detPreInspection = mapper.map(detDto, ScrutinyPreInspectionDet.class);
				detPreInspection.setCreatedDatetime(new Date());
			} else {
				Optional<ScrutinyPreInspectionDet> detBoopt = scrutinyPreInspectionDetRepository.findById(detDto.getId());
				if (detBoopt.isPresent()) {
					detPreInspection = detBoopt.get();
				} else {
					detPreInspection = mapper.map(detDto, ScrutinyPreInspectionDet.class);
					detPreInspection.setCreatedDatetime(new Date());
				}
			}

			Optional<ChecklistScrutinyPreInspection> checkbo = checklistScrutinyPreInspectionRepository
					.findById(detDto.getChecklistScrutinyPreInspectionId());
			if (checkbo.isPresent()) {
				detPreInspection.setChecklistScrutinyPreInspection(checkbo.get());
			} else {
				throw new DataNotFoundException(
						"ScrutinyPreInspection save: checklist not found by Id: " + detDto.getChecklistScrutinyPreInspectionId());
			}
			detPreInspection.setObsSts(detDto.getObsSts());
			detPreInspection.setRemarks(detDto.getRemarks());
			detPreInspection.setScrutinyPreInspection(scrutinyPreInspection);

			// Add code for the observation status

			boolean isvalid = entityValidator.isValidEntity(detPreInspection);
			if (isvalid) {
				scrutinyPreInspectionDets.add(detPreInspection);
			} else {
				throw new DataNotValidException("ScrutinyPreInspection save: det " + detPreInspection.toString());
			}

		}

		scrutinyPreInspectionDets = scrutinyPreInspectionDetRepository.saveAll(scrutinyPreInspectionDets);
		return scrutinyPreInspectionDets;
	}

	/**
	 * 
	 * @param scrutinySoDets
	 * @return
	 */
	public List<ScrutinyPreInspectionDetDto> preparePreInspectionDets(List<ScrutinyPreInspectionDet> scrutinyPreInspectionDets) {
		List<ScrutinyPreInspectionDetDto> scrutinyPreInspectionDetDtos = new ArrayList<>();
		for (ScrutinyPreInspectionDet scrutinyPreInspectionDet : scrutinyPreInspectionDets) {
			ScrutinyPreInspectionDetDto scrutinyPreInspectionDetDto = new ScrutinyPreInspectionDetDto();
			scrutinyPreInspectionDetDto.setId(scrutinyPreInspectionDet.getId());
			ChecklistScrutinyPreInspection checkPreInspection = scrutinyPreInspectionDet.getChecklistScrutinyPreInspection();
			scrutinyPreInspectionDetDto.setChecklistScrutinyPreInspectionId(checkPreInspection.getId());
			scrutinyPreInspectionDetDto.setSlNo(checkPreInspection.getSlNo());
			scrutinyPreInspectionDetDto.setReference(checkPreInspection.getReference());
			scrutinyPreInspectionDetDto.setScrutinyPreInspectionId(scrutinyPreInspectionDet.getScrutinyPreInspection().getId());
			scrutinyPreInspectionDetDto.setLastUpdatedBy(scrutinyPreInspectionDet.getLastUpdatedBy());
			if (StringUtils.isNotBlank(scrutinyPreInspectionDet.getRemarks())) {
				scrutinyPreInspectionDetDto.setRemarks(scrutinyPreInspectionDet.getRemarks());
			} else {
				scrutinyPreInspectionDetDto.setRemarks("");
			}
			scrutinyPreInspectionDetDto.setObsType(checkPreInspection.getObsType());
			if (StringUtils.isNotBlank(scrutinyPreInspectionDet.getObsSts())) {
				scrutinyPreInspectionDetDto.setObsSts(scrutinyPreInspectionDet.getObsSts());
			} else {
				scrutinyPreInspectionDetDto.setObsSts("");
			}

			scrutinyPreInspectionDetDto.setSplit(checkPreInspection.getSplit());
			scrutinyPreInspectionDetDtos.add(scrutinyPreInspectionDetDto);
		}
		Collections.sort(scrutinyPreInspectionDetDtos, new Comparator<ScrutinyPreInspectionDetDto>() {
			public int compare(ScrutinyPreInspectionDetDto o1, ScrutinyPreInspectionDetDto o2) {
				return o1.getChecklistScrutinyPreInspectionId().compareTo(o2.getChecklistScrutinyPreInspectionId());
			}
		});
		return scrutinyPreInspectionDetDtos;
	}

	/**
	 * 
	 * @author Manasa B
	 * @since 24-02-2023
	 * @param soId
	 * @return
	 */
	public ScrutinyPreInspectionDto getScrutinyPreInspectionByOlId(int olId) {
		ScrutinyPreInspectionDto scrutinyPreInspectionDto = new ScrutinyPreInspectionDto();
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
		ScrutinyPreInspection scrutinyPreInspection = scrutinyPreInspectionRepository.getScrutinyPreInspectionByOfferLetter(offerLetter);
		
		List<ScrutinyPreInspectionDetDto> inspPreInspectionDetDto = new ArrayList<>();
		if (scrutinyPreInspection == null) {
			scrutinyPreInspectionDto.setId(0);
			scrutinyPreInspectionDto.setSubmittedDate(null);
			List<ChecklistScrutinyPreInspection> chkSPreInspection = checklistScrutinyPreInspectionRepository.findAll();
			for (ChecklistScrutinyPreInspection checklistScrutinyPreInspection : chkSPreInspection) {
				ScrutinyPreInspectionDetDto detDto = new ScrutinyPreInspectionDetDto();
				detDto.setId(0);
				detDto.setChecklistScrutinyPreInspectionId(checklistScrutinyPreInspection.getId());
				detDto.setSlNo(checklistScrutinyPreInspection.getSlNo());
				detDto.setReference(checklistScrutinyPreInspection.getReference());
				detDto.setSplit(checklistScrutinyPreInspection.getSplit());
				detDto.setObsType(checklistScrutinyPreInspection.getObsType());
				detDto.setObsSts("");
				detDto.setRemarks("");
				inspPreInspectionDetDto.add(detDto);
			}

		} else {
			scrutinyPreInspectionDto = mapper.map(scrutinyPreInspection, ScrutinyPreInspectionDto.class);
			OfferLetter ol = scrutinyPreInspection.getOfferLetter();
			scrutinyPreInspectionDto.setOfferLetterId(ol.getId());
			if (null != scrutinyPreInspection.getSubmittedDate()) {
				scrutinyPreInspectionDto.setSubmittedDate(dateFormat.format(scrutinyPreInspection.getSubmittedDate()));
			}
			for (ScrutinyPreInspectionDet detBo : scrutinyPreInspection.getScrutinyPreInspectionDets()) {
				ScrutinyPreInspectionDetDto detDto = new ScrutinyPreInspectionDetDto();
				ChecklistScrutinyPreInspection checklistScrutinyPreInspection = detBo.getChecklistScrutinyPreInspection();
				detDto.setId(detBo.getId());
				detDto.setChecklistScrutinyPreInspectionId(checklistScrutinyPreInspection.getId());
				detDto.setSlNo(checklistScrutinyPreInspection.getSlNo());
				detDto.setReference(checklistScrutinyPreInspection.getReference());
				detDto.setSplit(checklistScrutinyPreInspection.getSplit());
				detDto.setObsType(checklistScrutinyPreInspection.getObsType());
				detDto.setScrutinyPreInspectionId(scrutinyPreInspection.getId());
				detDto.setLastUpdatedBy(scrutinyPreInspection.getLastUpdatedBy());
				detDto.setObsSts(detBo.getObsSts());
				detDto.setRemarks(detBo.getRemarks());
				inspPreInspectionDetDto.add(detDto);
			}
		}

		Collections.sort(inspPreInspectionDetDto, new Comparator<ScrutinyPreInspectionDetDto>() {
			@Override
			public int compare(ScrutinyPreInspectionDetDto o1, ScrutinyPreInspectionDetDto o2) {

				return o1.getChecklistScrutinyPreInspectionId().compareTo(o2.getChecklistScrutinyPreInspectionId());
			}

		});


		List<String> fileUriList = new ArrayList<>();
		if(scrutinyPreInspection!= null ) {
			if(scrutinyPreInspection.getScrutinyPreInspectionAttachments() != null) {
				for(ScrutinyPreInspectionAttachment attachment : scrutinyPreInspection.getScrutinyPreInspectionAttachments()) {
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + attachment.getFileName();
					fileUriList.add(fileDownloadUri);
				}
			}
			scrutinyPreInspectionDto.setQalFileUploads(fileUriList);
			if(scrutinyPreInspection.getSoStatus() != null) {
				scrutinyPreInspectionDto.setSoStatusId(scrutinyPreInspection.getSoStatus().getId());
				scrutinyPreInspectionDto.setSoStatus(scrutinyPreInspection.getSoStatus().getStatus());
			}
		}

		scrutinyPreInspectionDto.setScrutinyPreInspectionDetDtos(inspPreInspectionDetDto);
		
		return scrutinyPreInspectionDto;
	}

	public ScrutinyPreInspection submitScrutinyPreInspection(@Valid ScrutinyPreInspectionDto scrutinyPreInspectionDto) {
		ScrutinyPreInspection scrutinyPreInspection = saveScrutinyPreInspection(scrutinyPreInspectionDto);
		scrutinyPreInspection.setSubmittedDate(new Date());
		boolean isValid = entityValidator.isValidEntity(scrutinyPreInspection);
		if (isValid) {
			scrutinyPreInspection = scrutinyPreInspectionRepository.save(scrutinyPreInspection);
		} else {
			throw new DataNotFoundException("ScrutinyPreInspection Submit:" + scrutinyPreInspection.toString());
		}
		return scrutinyPreInspection;
	}
	
	
	
	public Boolean updatePreinspectionByQAO(ScrutinyPreInspectionDto scrutinyPreInspectionDto, MultipartFile[] attachments) {
		boolean returnStatus = false;
		try {
			if (scrutinyPreInspectionDto != null) {

				Optional<ScrutinyPreInspection> optional = scrutinyPreInspectionRepository.findById(scrutinyPreInspectionDto.getId());
				if (optional.isPresent()) {
					ScrutinyPreInspection scrutinyPreInspection = optional.get();
					scrutinyPreInspection.setQaoRemarks(scrutinyPreInspectionDto.getQaoRemarks());
					Optional<SoStatus> soStatusOptional   = soStatusRepository.findById(scrutinyPreInspectionDto.getSoStatusId());
					if (soStatusOptional.isPresent()) {
						scrutinyPreInspection.setSoStatus(soStatusOptional.get());
					}
					scrutinyPreInspection.setQaoSubmittedDate(new Date());
					scrutinyPreInspection = scrutinyPreInspectionRepository.save(scrutinyPreInspection);

					List<ScrutinyPreInspectionAttachment> scrutinyQaclAttachments = new ArrayList<>();
					if (attachments != null && attachments.length > 0) {
						saveScrutinyPreinspAttachment(attachments, scrutinyPreInspection, scrutinyQaclAttachments);
					}
					scrutinyPreInspectionAttachmentRepository.saveAll(scrutinyQaclAttachments);
					returnStatus = true;
				}
			}

		} catch (Exception e) {
			logger.error("Error while updating supply order approval ", e);
			logger.error("Error in updating supply order approval {}", e.getMessage());
		}
		return returnStatus;

	}
	
	
	private void saveScrutinyPreinspAttachment(MultipartFile[] files, ScrutinyPreInspection savedSoApprovalDetails,List<ScrutinyPreInspectionAttachment> scrutinyPreInspectionAttachments) {
		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(files[i], savedSoApprovalDetails.getId(), i);
			if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				ScrutinyPreInspectionAttachment attachment = new ScrutinyPreInspectionAttachment();
				attachment.setCreatedDate(date);
				attachment.setFileName(fileName);
				attachment.setScrutinyPreInspection(savedSoApprovalDetails);
				scrutinyPreInspectionAttachments.add(attachment);
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ fileName;
				uploadedFilesUrl.add(fileDownloadUri);
			}
		}
	}
}
