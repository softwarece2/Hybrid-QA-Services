package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import in.co.bel.olms.transaction.dto.DdExtensionDto;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.DdExtensionAttachment;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.DdExtensionRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.validator.FileValidator;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DdExtensionService {
	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final DdExtensionRepository ddExtensionRepository;

	private final SupplyOrderRepository soRepository;

	private final SoLineItemRepository soLiRepository;

	private final FileStorageService fileStorageService;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(DdExtensionService.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	public List<DdExtension> getDdExtensionOfSo() {
		return ddExtensionRepository.findAll();

	}

	public DdExtension getById(Integer id) {
		DdExtension ddExtension = ddExtensionRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("data not found with Id: " + id));
		return ddExtension;
	}

	public List<DdExtensionDto> getByIdAndType(Integer id, Integer type) {

		if (type == null) {
			throw new DataNotValidException("Type cannot be null.");
		} else {
			List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
			if (!validTypes.contains(type)) {
				throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
			}
		}

		List<DdExtensionDto> ddExtensionDtoList = new ArrayList<>();
		List<DdExtension> ddExtensions = new ArrayList<>();

		if (type == AMENDMENT_LETTER_TYPE.SUPPLY_ORDER) {
			soRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("Supply order not found for id: " + id));
			ddExtensions = ddExtensionRepository.getBySoId(id);
			if (ddExtensions.isEmpty())
				throw new DataNotFoundException("DD Extensions not found against supply order id " + id);

		} else if (type == AMENDMENT_LETTER_TYPE.SO_LINE_ITEM) {
			soLiRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("SoLineItem not found for id: " + id));
			ddExtensions = ddExtensionRepository.getBySoLiId(id);
			if (ddExtensions.isEmpty())
				throw new DataNotFoundException("DD Extensions not found against so-line-item id " + id);
		}

//		ddExtensionRepository.getBySoId(id);
		if (ddExtensions.isEmpty()) {
			throw new DataNotFoundException("ddExtensions not found by id " + id);

		}

		for (DdExtension ddExtension : ddExtensions) {
			DdExtensionDto ddExtensionDto = mapper.map(ddExtension, DdExtensionDto.class);
			ddExtensionDto.setLetterDateStr(sdf.format(ddExtension.getLetterDate()));
			ddExtensionDto.setNewDdDateStr(sdf.format(ddExtension.getNewDdDate()));
			List<String> uploadedFilesUrl = new ArrayList<>();
			List<DdExtensionAttachment> attachments = ddExtension.getAttachments();
			if (attachments != null) {
				for (DdExtensionAttachment j : attachments) {

					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + j.getFileName();
					uploadedFilesUrl.add(fileDownloadUri);
					uploadedFilesUrl.add(j.getFileName());

				}
				ddExtensionDto.setFileDownloadUri(uploadedFilesUrl);
			}
			ddExtensionDtoList.add(ddExtensionDto);

		}

		return ddExtensionDtoList;

	}

	public DdExtensionDto saveDdExtensionData(DdExtensionDto ddExtensionDto, MultipartFile[] ddFiles) {
		try {
//		DdExtension ddExtension=new DdExtension();
			String ddLetterNo = ddExtensionDto.getLetterNo();
			String ddRemarks = ddExtensionDto.getRemarks();
			Date letterDate = ddExtensionDto.getLetterDate();
			Date newDdDate = ddExtensionDto.getNewDdDate();
			Integer supplyOrderId = ddExtensionDto.getSupplyOrderId();
			Integer soLiId = ddExtensionDto.getSoLiId();
			Integer type = ddExtensionDto.getType();
			
			if (StringUtils.isNotBlank(ddLetterNo)) {
				if (!InputNameValidation.checkString(ddLetterNo)) {
					throw new DataNotValidException("Letter no is invalid");
				}
			}

			if (StringUtils.isNotBlank(ddRemarks)) {
				if (!InputNameValidation.checkRemarks(ddRemarks)) {
					throw new DataNotValidException("Remarks is invalid");
				}
			}
			
//			if (type == null) {
//				throw new DataNotValidException("Type cannot be null.");
//			} else {
//				List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
//				if(!validTypes.contains(type)) {
//					throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
//				}
//			}

			// Modified by: Shridhar R, Date: 13-10-2025	
			
			if(type == AMENDMENT_LETTER_TYPE.SUPPLY_ORDER) {
				if(supplyOrderId!= null) {
					if(ddExtensionRepository.existsByLetterNoAndLetterDateAndNewDdDateAndSupplyOrderId(ddLetterNo, letterDate,newDdDate, supplyOrderId)) 
						throw new IllegalStateException("DD Extension for this delivery date already exists.");
				} else {
					throw new IllegalArgumentException("SupplyOrderId cannot be null for type 1");
				}
			}
			
			if(type == AMENDMENT_LETTER_TYPE.SO_LINE_ITEM) {
				if(soLiId!= null) {
					if(ddExtensionRepository.existsByLetterNoAndLetterDateAndNewDdDateAndSoLineItemId(ddLetterNo, letterDate, newDdDate, soLiId)) 
						throw new IllegalStateException("DD Extension for this delivery date already exists.");
				} else {
					throw new IllegalArgumentException("soLiId cannot be null for type 2");
				}
			}
			
			DdExtension ddExtension = mapper.map(ddExtensionDto, DdExtension.class);
			List<DdExtensionAttachment> ddExtensionAttachments = new ArrayList<>();
			if (ddFiles != null) {

				List<String> uploadedFilesUrl = new ArrayList<>();
				for (int i = 0; i < ddFiles.length; i++) {
					if (FileValidator.checkFile(ddFiles[i])) {
						String fileName = fileStorageService.storeFile(ddFiles[i], ddExtension.getId(), i);
						if (StringUtils.isNotBlank(fileName)
								&& !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
							DdExtensionAttachment attachment = new DdExtensionAttachment();
							attachment.setFileName(fileName);
							attachment.setDdExtension(ddExtension);
							String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR
									+ "files/" + fileName;
							uploadedFilesUrl.add(fileDownloadUri);
							ddExtensionAttachments.add(attachment);
						}
					}
				}
				ddExtensionDto.setFileDownloadUri(uploadedFilesUrl);
			}
			ddExtension.setAttachments(ddExtensionAttachments);
			ddExtensionRepository.save(ddExtension);
			ddExtensionDto.setId(ddExtension.getId());
			return ddExtensionDto;

		} catch (Exception e) {
			logger.error("Unexpected error occurred while saving data: {}", e.getMessage(), e);
			throw e;
		}
	}

	public List<DdExtensionDto> getLast2DdExtnBySoId(int soId) {
		List<DdExtension> ddExtensions = ddExtensionRepository.getLast2BySoId(soId);
		List<DdExtensionDto> ddExtensionDtos = new ArrayList<>();
		for (DdExtension extn : ddExtensions) {
			DdExtensionDto dto = mapper.map(extn, DdExtensionDto.class);
			ddExtensionDtos.add(dto);
		}
		return ddExtensionDtos;
	}
	
	public List<DdExtensionDto> getLast2DdExtnBySoLiId(int soLiId) {
		List<DdExtension> ddExtensions = ddExtensionRepository.getLast2BySoLiId(soLiId);
		List<DdExtensionDto> ddExtensionDtos = new ArrayList<>();
		for (DdExtension extn : ddExtensions) {
			DdExtensionDto dto = mapper.map(extn, DdExtensionDto.class);
			ddExtensionDtos.add(dto);
		}
		return ddExtensionDtos;
	}

}
