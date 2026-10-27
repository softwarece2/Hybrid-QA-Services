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
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyThreeTier;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.checklist.army.ChecklistScrutinyThreeTierRepository;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyThreeTierDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyThreeTierDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyThreeTier;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyThreeTierDet;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyThreeTierDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyThreeTierRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrutinyThreeTierService {

	private final ChecklistScrutinyThreeTierRepository checklistScrutinyThreeTierRepository;

	private final ScrutinyThreeTierRepository scrutinyThreeTierRepository;

	private final ScrutinyThreeTierDetRepository scrutinyThreeTierDetRepository;

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;

	private final EntityValidator entityValidator;

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyThreeTierService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	public ScrutinyThreeTier saveScrutinyThreeTier(ScrutinyThreeTierDto scrutinyThreeTierDto) {

		List<ScrutinyThreeTierDet> scrutinyThreeTierDetList;
		ScrutinyThreeTier scrutinyThreeTier;
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(scrutinyThreeTierDto.getOfferLetterId());
		if (scrutinyThreeTierDto.getId() != 0) {

			Optional<ScrutinyThreeTier> scrutinyThreeTierOpt = scrutinyThreeTierRepository
					.findById(scrutinyThreeTierDto.getId());
			if (scrutinyThreeTierOpt.isPresent()) {
				scrutinyThreeTier = scrutinyThreeTierOpt.get();
				scrutinyThreeTier.setModifiedDatetime(new Date());
			} else {
				throw new DataNotFoundException(
						"Save : Scrutiny three tier not found by  Id: " + scrutinyThreeTierDto.getId());
			}

		} else {
			scrutinyThreeTier = mapper.map(scrutinyThreeTierDto, ScrutinyThreeTier.class);
			scrutinyThreeTier.setCreatedDatetime(new Date());
			scrutinyThreeTier.setOfferLetter(offerLetter);
		}
		scrutinyThreeTier.setOfferLetter(offerLetter);
		boolean isValid = entityValidator.isValidEntity(scrutinyThreeTier);
		if (isValid) {
			scrutinyThreeTier = scrutinyThreeTierRepository.save(scrutinyThreeTier);
		} else {
			throw new DataNotValidException("ScrutinyThreeTier save:" + scrutinyThreeTier.toString());
		}

		scrutinyThreeTierDetList = prepareScrutinyThreeTierObj(scrutinyThreeTierDto, scrutinyThreeTier);
		scrutinyThreeTier.setScrutinyThreeTierDets(scrutinyThreeTierDetList);
		return scrutinyThreeTier;
	}

	private List<ScrutinyThreeTierDet> prepareScrutinyThreeTierObj(ScrutinyThreeTierDto scrutinyThreeTierDto,
			ScrutinyThreeTier scrutinyThreeTier) {
		List<ScrutinyThreeTierDet> scrutinyThreeTierDets = new ArrayList<>();
		for (ScrutinyThreeTierDetDto detDto : scrutinyThreeTierDto.getScrutinyThreeTierDetDtos()) {
			ScrutinyThreeTierDet detThreeTier;
			if (detDto.getId() == 0) {
				detThreeTier = mapper.map(detDto, ScrutinyThreeTierDet.class);
				detThreeTier.setCreatedDatetime(new Date());
			} else {
				Optional<ScrutinyThreeTierDet> detBoopt = scrutinyThreeTierDetRepository.findById(detDto.getId());
				if (detBoopt.isPresent()) {
					detThreeTier = detBoopt.get();
				} else {
					detThreeTier = mapper.map(detDto, ScrutinyThreeTierDet.class);
					detThreeTier.setCreatedDatetime(new Date());
				}
			}

			Optional<ChecklistScrutinyThreeTier> checkbo = checklistScrutinyThreeTierRepository
					.findById(detDto.getChecklistScrutinyThreeTierId());
			if (checkbo.isPresent()) {
				detThreeTier.setChecklistScrutinyThreeTier(checkbo.get());
			} else {
				throw new DataNotFoundException("ScrutinyThreeTier save: checklist not found by Id: "
						+ detDto.getChecklistScrutinyThreeTierId());
			}
			detThreeTier.setObsSts(detDto.getObsSts());
			detThreeTier.setRemarks(detDto.getRemarks());
			detThreeTier.setScrutinyThreeTier(scrutinyThreeTier);

			// Add code for the observation status

			boolean isvalid = entityValidator.isValidEntity(detThreeTier);
			if (isvalid) {
				scrutinyThreeTierDets.add(detThreeTier);
			} else {
				throw new DataNotValidException("ScrutinySo save: det " + detThreeTier.toString());
			}

		}

		scrutinyThreeTierDets = scrutinyThreeTierDetRepository.saveAll(scrutinyThreeTierDets);
		return scrutinyThreeTierDets;
	}

	/**
	 * 
	 * @param scrutinySoDets
	 * @return
	 */
	public List<ScrutinyThreeTierDetDto> prepareThreeTierDets(List<ScrutinyThreeTierDet> scrutinyThreeTierDets) {
		List<ScrutinyThreeTierDetDto> scrutinyThreeTierDetDtos = new ArrayList<>();
		for (ScrutinyThreeTierDet scrutinyThreeTierDet : scrutinyThreeTierDets) {
			ScrutinyThreeTierDetDto scrutinyThreeTierDetDto = new ScrutinyThreeTierDetDto();
			scrutinyThreeTierDetDto.setId(scrutinyThreeTierDet.getId());
			ChecklistScrutinyThreeTier checkTt = scrutinyThreeTierDet.getChecklistScrutinyThreeTier();
			scrutinyThreeTierDetDto.setChecklistScrutinyThreeTierId(checkTt.getId());
			scrutinyThreeTierDetDto.setSlNo(checkTt.getSlNo());
			scrutinyThreeTierDetDto.setReference(checkTt.getReference());
			scrutinyThreeTierDetDto.setScrutinyThreeTierId(scrutinyThreeTierDet.getScrutinyThreeTier().getId());
			scrutinyThreeTierDetDto.setLastUpdatedBy(scrutinyThreeTierDet.getLastUpdatedBy());
			if (StringUtils.isNotBlank(scrutinyThreeTierDet.getRemarks())) {
				scrutinyThreeTierDetDto.setRemarks(scrutinyThreeTierDet.getRemarks());
			} else {
				scrutinyThreeTierDetDto.setRemarks("");
			}
			scrutinyThreeTierDetDto.setObsType(checkTt.getObsType());
			if (StringUtils.isNotBlank(scrutinyThreeTierDetDto.getObsSts())) {
				scrutinyThreeTierDetDto.setObsSts(scrutinyThreeTierDet.getObsSts());
			} else {
				scrutinyThreeTierDetDto.setObsSts("");
			}

			scrutinyThreeTierDetDto.setSplit(checkTt.getSplit());
			scrutinyThreeTierDetDtos.add(scrutinyThreeTierDetDto);
		}
		Collections.sort(scrutinyThreeTierDetDtos, new Comparator<ScrutinyThreeTierDetDto>() {
			public int compare(ScrutinyThreeTierDetDto o1, ScrutinyThreeTierDetDto o2) {
				return o1.getChecklistScrutinyThreeTierId().compareTo(o2.getChecklistScrutinyThreeTierId());
			}
		});
		return scrutinyThreeTierDetDtos;
	}

	/**
	 * 
	 * @author Manasa B
	 * @since 24-02-2023
	 * @param soId
	 * @return
	 */
	public ScrutinyThreeTierDto getScrutinyThreeTierByOlId(int olId) {
	    ScrutinyThreeTierDto scrutinyThreeTierDto = new ScrutinyThreeTierDto();
	    
	    try {
	        OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
	        ScrutinyThreeTier scrutinyThreeTier = scrutinyThreeTierRepository.getScrutinyThreeTierByOfferLetter(offerLetter);

	        List<ScrutinyThreeTierDetDto> inspThreeTierDetDto = new ArrayList<>();

	        if (offerLetter == null || scrutinyThreeTier == null) {
	            // Handle case where SupplyOrder or ScrutinyThreeTier is null
	            scrutinyThreeTierDto.setId(0);
	            scrutinyThreeTierDto.setSubmittedDate(null);

	            // Fetch all checklist items
	            List<ChecklistScrutinyThreeTier> chkSThreeTier = checklistScrutinyThreeTierRepository.findAll();
	            for (ChecklistScrutinyThreeTier checklistScrutinyThreeTier : chkSThreeTier) {
	                ScrutinyThreeTierDetDto detDto = new ScrutinyThreeTierDetDto();
	                detDto.setId(0);
	                detDto.setChecklistScrutinyThreeTierId(checklistScrutinyThreeTier.getId());
	                detDto.setSlNo(checklistScrutinyThreeTier.getSlNo());
	                detDto.setReference(checklistScrutinyThreeTier.getReference());
	                detDto.setSplit(checklistScrutinyThreeTier.getSplit());
	                detDto.setObsType(checklistScrutinyThreeTier.getObsType());
	                detDto.setObsSts("");
	                detDto.setRemarks("");
	                inspThreeTierDetDto.add(detDto);
	            }
	        } else {
	            scrutinyThreeTierDto = mapper.map(scrutinyThreeTier, ScrutinyThreeTierDto.class);
	            scrutinyThreeTierDto.setOfferLetterId(offerLetter.getId());
	            if (scrutinyThreeTier.getSubmittedDate() != null) {
	                scrutinyThreeTierDto.setSubmittedDate(dateFormat.format(scrutinyThreeTier.getSubmittedDate()));
	            }
	            
	            // Populate DTO with details from ScrutinyThreeTier
	            for (ScrutinyThreeTierDet detBo : scrutinyThreeTier.getScrutinyThreeTierDets()) {
	                ScrutinyThreeTierDetDto detDto = new ScrutinyThreeTierDetDto();
	                ChecklistScrutinyThreeTier checklistScrutinyThreeTier = detBo.getChecklistScrutinyThreeTier();
	                detDto.setId(detBo.getId());
	                detDto.setChecklistScrutinyThreeTierId(checklistScrutinyThreeTier.getId());
	                detDto.setSlNo(checklistScrutinyThreeTier.getSlNo());
	                detDto.setReference(checklistScrutinyThreeTier.getReference());
	                detDto.setSplit(checklistScrutinyThreeTier.getSplit());
	                detDto.setObsType(checklistScrutinyThreeTier.getObsType());
	                detDto.setScrutinyThreeTierId(scrutinyThreeTier.getId());
	                detDto.setLastUpdatedBy(scrutinyThreeTier.getLastUpdatedBy());
	                detDto.setObsSts(detBo.getObsSts());
	                detDto.setRemarks(detBo.getRemarks());
	                inspThreeTierDetDto.add(detDto);
	            }
	        }

	        // Sort the list
	        Collections.sort(inspThreeTierDetDto, new Comparator<ScrutinyThreeTierDetDto>() {
	            @Override
	            public int compare(ScrutinyThreeTierDetDto o1, ScrutinyThreeTierDetDto o2) {
	                return o1.getChecklistScrutinyThreeTierId().compareTo(o2.getChecklistScrutinyThreeTierId());
	            }
	        });

	        scrutinyThreeTierDto.setScrutinyThreeTierDetDtos(inspThreeTierDetDto);
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching the ScrutinyThreeTier ", e);
	        return null;
	    }

	    return scrutinyThreeTierDto;
	}

	
	public ScrutinyThreeTier submitScrutinyThreeTier(@Valid ScrutinyThreeTierDto scrutinyThreeTierDto) {
		ScrutinyThreeTier scrutinyThreeTier = saveScrutinyThreeTier(scrutinyThreeTierDto);
		scrutinyThreeTier.setSubmittedDate(new Date());
		boolean isValid = entityValidator.isValidEntity(scrutinyThreeTier);
		if (isValid) {
			scrutinyThreeTier = scrutinyThreeTierRepository.save(scrutinyThreeTier);
		} else {
			throw new DataNotFoundException("ScrutinyThreeTier Submit:" + scrutinyThreeTier.toString());
		}
		return scrutinyThreeTier;
	}
}
