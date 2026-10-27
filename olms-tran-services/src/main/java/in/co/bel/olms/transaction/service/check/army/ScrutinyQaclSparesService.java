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
import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQaclSpares;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.SoStatusRepository;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyQaclSparesRepository;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclSparesDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclSparesDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclSpares;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclSparesAttachment;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclSparesDet;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclSparesAttachmentRepo;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclSparesDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyQaclSparesRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyQaclSparesService {

    private final OfferLetterService offerLetterService; 

    private final OfferLetterRepository offerLetterRepository ;

    private final Mapper mapper;
    
    private final ScrutinyQaclSparesRepository scrutinyQaclSparesRepository;

    private final ScrutinyQaclSparesDetRepository scrutinyQaclSparesDetRepository;

    private final ChecklistScrutinyQaclSparesRepository checklistScrutinyQaclSparesRepository;

	private final SoStatusRepository soStatusRepository;

    private final EntityValidator entityValidator;

	private final SupplyOrderService supplyOrderService;

	private final ScrutinyQaclSparesAttachmentRepo scrutinyQaclSparesAttachmentRepo;
    
    @Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final FileStorageService fileStorageService;	

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyQaclSparesService.class);
    
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ScrutinyQaclSpares saveScrutinyQaclSpares(@Valid ScrutinyQaclSparesDto scrutinyQaclSparesDto) {
        ScrutinyQaclSpares scrutinyQaclSpares;
       
        if (scrutinyQaclSparesDto.getId() != null) {
            Optional<ScrutinyQaclSpares> scrutinyQaclSparesOptional = scrutinyQaclSparesRepository.findById(scrutinyQaclSparesDto.getId());
            if (scrutinyQaclSparesOptional.isPresent()) {
                scrutinyQaclSpares = scrutinyQaclSparesOptional.get();
            } else {
                throw new DataNotFoundException("Save : Scrutiny QACL Spares not found by Id: " + scrutinyQaclSparesDto.getId());
            }
        } else {
            scrutinyQaclSpares = mapper.map(scrutinyQaclSparesDto, ScrutinyQaclSpares.class);
			Optional<OfferLetter> offerLetterOptional = offerLetterRepository.findById(scrutinyQaclSparesDto.getOfferLetterId());

			if (offerLetterOptional.isPresent()) {
				OfferLetter offerLetter = offerLetterOptional.get();
				scrutinyQaclSpares.setOfferLetter(offerLetter);
			}

			 scrutinyQaclSpares.setCreatedDatetime(new Date());
        }

        
        boolean isValid = entityValidator.isValidEntity(scrutinyQaclSpares);
        if (isValid) {
            scrutinyQaclSpares = scrutinyQaclSparesRepository.save(scrutinyQaclSpares);
        } else {
            throw new DataNotValidException("ScrutinyQaclSpares save:" + scrutinyQaclSpares.toString());
        }

        scrutinyQaclSpares = prepareScrutinyQaclSparesObj(scrutinyQaclSparesDto, scrutinyQaclSpares);


        return scrutinyQaclSpares;
    }

    private ScrutinyQaclSpares prepareScrutinyQaclSparesObj(ScrutinyQaclSparesDto scrutinyQaclSparesDto, ScrutinyQaclSpares scrutinyQaclSpares) {
        List<ScrutinyQaclSparesDet> scrutinyQaclSparesDets = new ArrayList<>();

        for (ScrutinyQaclSparesDetDto scrutinyQaclSparesDetDto : scrutinyQaclSparesDto.getQaclSparesDetDtos()) {
            ScrutinyQaclSparesDet scrutinyQaclSparesDet;

            if (scrutinyQaclSparesDetDto.getId() == null) {
                scrutinyQaclSparesDet = mapper.map(scrutinyQaclSparesDetDto, ScrutinyQaclSparesDet.class);
            } else {
                Optional<ScrutinyQaclSparesDet> scrutinyQaclSparesDetOpt = scrutinyQaclSparesDetRepository
                        .findById(scrutinyQaclSparesDetDto.getId());
                if (scrutinyQaclSparesDetOpt.isPresent()) {
                    scrutinyQaclSparesDet = scrutinyQaclSparesDetOpt.get();
                } else {
                    scrutinyQaclSparesDet = mapper.map(scrutinyQaclSparesDetDto, ScrutinyQaclSparesDet.class);
                }
            }

            Optional<ChecklistScrutinyQaclSpares> checklistScrutinyQaclSparesOptional = checklistScrutinyQaclSparesRepository
                    .findById(scrutinyQaclSparesDetDto.getChecklistScrutinyQaclSparesId());

            if (checklistScrutinyQaclSparesOptional.isPresent()) {
                scrutinyQaclSparesDet.setChecklistScrutinyQaclSpares(checklistScrutinyQaclSparesOptional.get());
            } else {
                throw new DataNotFoundException("ScrutinyQaclSpares save: checklist not found by Id: "
                        + scrutinyQaclSparesDetDto.getChecklistScrutinyQaclSparesId());
            }

            scrutinyQaclSparesDet.setObsSts(scrutinyQaclSparesDetDto.getObsSts());
            scrutinyQaclSparesDet.setRemarks(scrutinyQaclSparesDetDto.getRemarks());
            scrutinyQaclSparesDet.setScrutinyQaclSpares(scrutinyQaclSpares);

            boolean isValid = entityValidator.isValidEntity(scrutinyQaclSparesDet);
            if (isValid) {
                scrutinyQaclSparesDets.add(scrutinyQaclSparesDet);
            } else {
                throw new DataNotValidException("ScrutinyQaclSpares save: det " + scrutinyQaclSparesDet.toString());
            }
        }

        scrutinyQaclSparesDets = scrutinyQaclSparesDetRepository.saveAll(scrutinyQaclSparesDets);
        scrutinyQaclSpares.setScrutinyQaclSparesDets(scrutinyQaclSparesDets);
        return scrutinyQaclSpares;
    }

    public List<ScrutinyQaclSparesDetDto> prepareQaclSparesDets(List<ScrutinyQaclSparesDet> scrutinyQaclSparesDets,ScrutinyQaclSpares scrutinyQaclSpares) {
        List<ScrutinyQaclSparesDetDto> scrutinyQaclSparesDetDtos = new ArrayList<>();

        for (ScrutinyQaclSparesDet scrutinyQaclSparesDet : scrutinyQaclSparesDets) {
            ScrutinyQaclSparesDetDto scrutinyDetDto = new ScrutinyQaclSparesDetDto();
            scrutinyDetDto.setId(scrutinyQaclSparesDet.getId());
            ChecklistScrutinyQaclSpares checklist = scrutinyQaclSparesDet.getChecklistScrutinyQaclSpares();
            scrutinyDetDto.setChecklistScrutinyQaclSparesId(checklist.getId());
            scrutinyDetDto.setSlNo(checklist.getSlNo());
            scrutinyDetDto.setScrutinyQaclSparesId(scrutinyQaclSpares.getId());
            scrutinyDetDto.setReference(checklist.getReference());
            if (StringUtils.isNotBlank(scrutinyDetDto.getRemarks())) {
            scrutinyDetDto.setRemarks(scrutinyQaclSparesDet.getRemarks());
            } else {
            	scrutinyDetDto.setRemarks("");
            }           
            
            scrutinyDetDto.setObsType(checklist.getObsType());
        	if (StringUtils.isNotBlank(scrutinyDetDto.getObsSts())) 
			{
            scrutinyDetDto.setObsSts(scrutinyQaclSparesDet.getObsSts());
			} else {
				scrutinyDetDto.setObsSts("");
			}

            scrutinyQaclSparesDetDtos.add(scrutinyDetDto);
        }

     
        return scrutinyQaclSparesDetDtos;
    }

    public ScrutinyQaclSparesDto getScrutinyQaclSparesByOlId(Integer olId) {
        ScrutinyQaclSparesDto scrutinyQaclSparesDto = new ScrutinyQaclSparesDto();

        OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
        ScrutinyQaclSpares scrutinyQaclSpares = scrutinyQaclSparesRepository.getScrutinyQaclSparesByOfferLetter(offerLetter);
        List<ScrutinyQaclSparesDetDto> qaclSparesDetDtos = new ArrayList<>();
        if (scrutinyQaclSpares == null) {
            scrutinyQaclSparesDto.setId(null);           
            scrutinyQaclSparesDto.setSubmittedDate(null);
            List<ChecklistScrutinyQaclSpares> chkQaclSpares = checklistScrutinyQaclSparesRepository.findAll();
            for (ChecklistScrutinyQaclSpares checklist : chkQaclSpares) {
                ScrutinyQaclSparesDetDto detDto = new ScrutinyQaclSparesDetDto();
                detDto.setId(0);
                detDto.setChecklistScrutinyQaclSparesId(checklist.getId());
                detDto.setSlNo(checklist.getSlNo());
                detDto.setReference(checklist.getReference());
                detDto.setObsType(checklist.getObsType());
                detDto.setRemarks("");
                detDto.setObsSts("");
                qaclSparesDetDtos.add(detDto);
            }
          

        } else {
            scrutinyQaclSparesDto = mapper.map(scrutinyQaclSpares, ScrutinyQaclSparesDto.class);
            OfferLetter splyOrder = scrutinyQaclSpares.getOfferLetter();
            scrutinyQaclSparesDto.setOfferLetterId(splyOrder.getId());
            if(scrutinyQaclSpares.getSoStatus() != null) {
            	scrutinyQaclSparesDto.setSoStatus(scrutinyQaclSpares.getSoStatus().getStatus());
            	scrutinyQaclSparesDto.setSoStatusId(scrutinyQaclSpares.getSoStatus().getId());
			}
            scrutinyQaclSparesDto.setQaoSubmittedDate(scrutinyQaclSpares.getRemarksQaoDate());
			if(null!=scrutinyQaclSpares.getSubmittedDate())
			{
				scrutinyQaclSparesDto.setSubmittedDate(dateFormat.format(scrutinyQaclSpares.getSubmittedDate()));
			}
			List<Object[]> response = scrutinyQaclSparesAttachmentRepo.getByQaclIdId(scrutinyQaclSpares.getId());
			if (response != null) {
				List<String> attachment = new ArrayList<>();
				for (Object[] obj : response) {
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + obj[2].toString();

					attachment.add(fileDownloadUri);
					scrutinyQaclSparesDto.setQalFileUploads(attachment);
				}
			}
          
            for (ScrutinyQaclSparesDet detBo : scrutinyQaclSpares.getScrutinyQaclSparesDets()) {
                ScrutinyQaclSparesDetDto detDto = new ScrutinyQaclSparesDetDto();
                ChecklistScrutinyQaclSpares checklist = detBo.getChecklistScrutinyQaclSpares();
                detDto.setId(detBo.getId());
                detDto.setChecklistScrutinyQaclSparesId(checklist.getId());
                detDto.setSlNo(checklist.getSlNo());
                detDto.setReference(checklist.getReference());
                detDto.setRemarks(detBo.getRemarks());
                detDto.setObsSts(detBo.getObsSts());
                detDto.setObsType(checklist.getObsType());

                qaclSparesDetDtos.add(detDto);
            }           
        }
        Collections.sort(qaclSparesDetDtos, new Comparator<ScrutinyQaclSparesDetDto>() {
			@Override
			public int compare(ScrutinyQaclSparesDetDto o1, ScrutinyQaclSparesDetDto o2) {

				return o1.getChecklistScrutinyQaclSparesId().compareTo(o2.getChecklistScrutinyQaclSparesId());
			}

		});

		scrutinyQaclSparesDto.setQaclSparesDetDtos(qaclSparesDetDtos);
		int supplyOrderId = offerLetter.getSaleOrder().getSupplyOrder().getId();
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(supplyOrderId);
		scrutinyQaclSparesDto.setOrderDate(supplyOrder.getOrderDate());
		scrutinyQaclSparesDto.setOrderNo(supplyOrder.getOrderNo());
		scrutinyQaclSparesDto.setDescription(supplyOrder.getDescription());
		scrutinyQaclSparesDto.setOfferDate(offerLetter.getOfferDate());
		scrutinyQaclSparesDto.setOlNumber(offerLetter.getOlNumber());
        return scrutinyQaclSparesDto;
    }

    public ScrutinyQaclSpares submitScrutinyQaclSpares(@Valid ScrutinyQaclSparesDto scrutinyQaclSparesDto) {
        ScrutinyQaclSpares scrutinyQaclSpares = saveScrutinyQaclSpares(scrutinyQaclSparesDto);
        scrutinyQaclSpares.setSubmittedDate(new Date());

        boolean isValid = entityValidator.isValidEntity(scrutinyQaclSpares);
        if (isValid) {
            scrutinyQaclSpares = scrutinyQaclSparesRepository.save(scrutinyQaclSpares);
        } else {
            throw new DataNotFoundException("ScrutinyQaclSpares Submit:" + scrutinyQaclSpares.toString());
        }

        //Notification and Observation logic can be added here
        return scrutinyQaclSpares;
    }

    public Boolean updateQaclSparesByQAO(ScrutinyQaclSparesDto scrutinyQaclDto, MultipartFile[] attachments) {
		boolean returnStatus = false;
		try {
			if (scrutinyQaclDto != null) {

				Optional<ScrutinyQaclSpares> scrutinyQaclOptional = scrutinyQaclSparesRepository.findById(scrutinyQaclDto.getId());
				if (scrutinyQaclOptional.isPresent()) {
					ScrutinyQaclSpares scrutinyQaclSpares = scrutinyQaclOptional.get();
					scrutinyQaclSpares.setRemarksQao(scrutinyQaclDto.getRemarksQao());
					Optional<SoStatus> soStatusOptional   = soStatusRepository.findById(scrutinyQaclDto.getSoStatusId());
					if (soStatusOptional.isPresent()) {
						scrutinyQaclSpares.setSoStatus(soStatusOptional.get());
					}
					scrutinyQaclSpares.setRemarksQaoDate(new Date());
					scrutinyQaclSpares = scrutinyQaclSparesRepository.save(scrutinyQaclSpares);

					List<ScrutinyQaclSparesAttachment> scrutinyQaclAttachments = new ArrayList<>();
					if (attachments != null && attachments.length > 0) {
						saveScrutinyQaclSparesAttachment(attachments, scrutinyQaclSpares, scrutinyQaclAttachments);
					}
					scrutinyQaclSparesAttachmentRepo.saveAll(scrutinyQaclAttachments);
					returnStatus = true;
				}
			}

		} catch (Exception e) {
			logger.error("Error while updating supply order approval ", e);
			logger.error("Error in updating supply order approval {}", e.getMessage());
		}
		return returnStatus;

	}
    
	private void saveScrutinyQaclSparesAttachment(MultipartFile[] files, ScrutinyQaclSpares scrutinyQaclSpares,
			List<ScrutinyQaclSparesAttachment> scrutinyQaclAttachments) {
		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(files[i], scrutinyQaclSpares.getId(), i);
			if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				ScrutinyQaclSparesAttachment attachment = new ScrutinyQaclSparesAttachment();
				attachment.setCreatedDate(date);
				attachment.setFileName(fileName);
				attachment.setScrutinyQaclSpares(scrutinyQaclSpares);
				scrutinyQaclAttachments.add(attachment);
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ fileName;
				uploadedFilesUrl.add(fileDownloadUri);
			}
		}
	}

}