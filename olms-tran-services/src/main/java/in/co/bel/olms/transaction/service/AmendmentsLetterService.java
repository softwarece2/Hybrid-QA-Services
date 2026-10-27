package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.AMENDMENT_LETTER_TYPE;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.AmendmentsLetterDto;
import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.entity.AmendmentsLetterAttachment;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.AmendmentsLetterRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.validator.FileValidator;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmendmentsLetterService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final AmendmentsLetterRepository amendmentsLetterRepository;

	private final SupplyOrderRepository soRepository;

	private final SoLineItemRepository soLiRepository;

	private final FileStorageService fileStorageService;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(AmendmentsLetterService.class);

	public List<AmendmentsLetterDto> getByIdAndType(Integer id, Integer type) {
		
		if (type == null) {
			throw new DataNotValidException("Type cannot be null.");
		} else {
			List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
			if(!validTypes.contains(type)) {
				throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
			}
		}
		
		List<AmendmentsLetterDto> amendmentsLetterDtoList = new ArrayList<>();
		List<AmendmentsLetter> amendmentsLetters = new ArrayList<>();

		if (type == AMENDMENT_LETTER_TYPE.SUPPLY_ORDER) {
			soRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("Supply order not found for id: " + id));
			amendmentsLetters = amendmentsLetterRepository.getBySoId(id);
			if (amendmentsLetters.isEmpty())
				throw new DataNotFoundException("Amendment letters not found against supply order id " + id);

		} else if (type == AMENDMENT_LETTER_TYPE.SO_LINE_ITEM) {
			soLiRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("SoLineItem not found for id: " + id));
			amendmentsLetters = amendmentsLetterRepository.getBySoLiId(id);
			if (amendmentsLetters.isEmpty())
				throw new DataNotFoundException("Amendment letters not found against so-line-item id " + id);
		}

		for (AmendmentsLetter amendmentsLetter : amendmentsLetters) {
			AmendmentsLetterDto amendmentsLetterDto = mapper.map(amendmentsLetter, AmendmentsLetterDto.class);
			List<String> uploadedFilesUrl = new ArrayList<>();
			List<AmendmentsLetterAttachment> attachments = amendmentsLetter.getAttachments();
			if (attachments != null) {
				for (AmendmentsLetterAttachment j : attachments) {
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + j.getFileName();
					uploadedFilesUrl.add(fileDownloadUri);
					uploadedFilesUrl.add(j.getFileName());
				}
				amendmentsLetterDto.setFileDownloadUri(uploadedFilesUrl);
			}
			amendmentsLetterDtoList.add(amendmentsLetterDto);
		}
		return amendmentsLetterDtoList;

	}

	public AmendmentsLetterDto saveAmendmentsLetterData(AmendmentsLetterDto amendmentsLetterDto, MultipartFile[] attachments) {
		String letterNo = amendmentsLetterDto.getLetterNo();
        String soNumber = "";
		SupplyOrder so = new SupplyOrder();
		String subject = amendmentsLetterDto.getSubject();
		String amendment = amendmentsLetterDto.getAmendments();
		String remarks = amendmentsLetterDto.getRemarks();
		Integer type = amendmentsLetterDto.getType();
        Integer supplyOrderId = amendmentsLetterDto.getSupplyOrderId();
        if(supplyOrderId != null) {
            so = soRepository.findById(supplyOrderId).orElse(null);
            soNumber = so!=null? so.getOrderNo(): null ;	
        }

		if (StringUtils.isNotBlank(letterNo)) {
			if (!InputNameValidation.checkString(letterNo)) 
				throw new DataNotValidException("Invalid letter no");
		}
		
		if (StringUtils.isNotBlank(subject)) {
			if (!InputNameValidation.checkString(subject)) 
				throw new DataNotValidException("Invalid subject");
		}
		
		if (StringUtils.isNotBlank(amendment)) {
			if (!InputNameValidation.checkString(amendment)) 
				throw new DataNotValidException("Invalid amendment");
		}
		
		if (StringUtils.isNotBlank(remarks)) {
			if (!InputNameValidation.checkRemarks(remarks)) 
				throw new DataNotValidException("Invalid remarks");
		}
		
//		if (type == null) {
//			throw new DataNotValidException("Type cannot be null.");
//		} else {
//			List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
//			if(!validTypes.contains(type)) {
//				throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
//			}
//		}

		// Modified by: Shridhar R, Date: 13-10-2025
		if (amendmentsLetterRepository.existsByLetterNoAndSupplyOrderId(letterNo,supplyOrderId)) {
			throw new DataNotValidException("Amendments Letter for this letter no. of this supply order:" + soNumber+  "already exists.");
		}

		AmendmentsLetter amendmentsLetter = mapper.map(amendmentsLetterDto, AmendmentsLetter.class);

		List<AmendmentsLetterAttachment> amendmentsLetterAttachment = new ArrayList<>();
		if (attachments != null) {

			List<String> uploadedFilesUrl = new ArrayList<>();
			if (attachments != null && attachments.length > 0) {
				for (int i = 0; i < attachments.length; i++) {
					if (FileValidator.checkFile(attachments[i])) {
						String fileName = fileStorageService.storeFile(attachments[i], amendmentsLetter.getId(), i);
						if (StringUtils.isNotBlank(fileName)
								&& !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
							AmendmentsLetterAttachment attachment = new AmendmentsLetterAttachment();
							attachment.setFileName(fileName);
							attachment.setAmendmentsLetter(amendmentsLetter);
							String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR
									+ "files/" + fileName;
							uploadedFilesUrl.add(fileDownloadUri);
							amendmentsLetterAttachment.add(attachment);

						}
					}
				}
			}
			amendmentsLetterDto.setFileDownloadUri(uploadedFilesUrl);
		}
		amendmentsLetter.setAttachments(amendmentsLetterAttachment);
		amendmentsLetterRepository.save(amendmentsLetter);
		amendmentsLetterDto.setId(amendmentsLetter.getId());
		return amendmentsLetterDto;

	}
}
