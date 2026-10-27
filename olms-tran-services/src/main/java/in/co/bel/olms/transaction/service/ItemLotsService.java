package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.AMENDMENT_LETTER_TYPE;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.ItemLotsDto;
import in.co.bel.olms.transaction.entity.ItemLots;
import in.co.bel.olms.transaction.entity.ItemLotsAttachment;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.ItemLotsRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.validator.FileValidator;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemLotsService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final ItemLotsRepository itemLotsRepository;

	private final SupplyOrderRepository soRepository;

	private final SoLineItemRepository soLiRepository;

	private final FileStorageService fileStorageService;

	private final Mapper mapper;

	public List<ItemLotsDto> getSoItemLotsByIdAndType(Integer id, Integer type) {

		if (type == null) {
			throw new DataNotValidException("Type cannot be null.");
		} else {
			List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
			if(!validTypes.contains(type)) {
				throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
			}
		}
		
		List<ItemLotsDto> itemLotsDtos = new ArrayList<>();
		List<ItemLots> itemLots = new ArrayList<>();

		if (type == AMENDMENT_LETTER_TYPE.SUPPLY_ORDER) {
			soRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("Supply order not found for id: " + id));
			itemLots = itemLotsRepository.getSoItemLotsBySoId(id);
			if (itemLots.isEmpty())
				throw new DataNotFoundException("Item lots not found against supply order id " + id);

		} else if (type == AMENDMENT_LETTER_TYPE.SO_LINE_ITEM) {
			soLiRepository.findById(id)
					.orElseThrow(() -> new DataNotFoundException("SoLineItem not found for id: " + id));
			itemLots = itemLotsRepository.getSoItemLotsBySoLiId(id);
			if (itemLots.isEmpty())
				throw new DataNotFoundException("Item lots not found against so-line-item id " + id);
		}
		
		for (ItemLots i : itemLots) {
			ItemLotsDto item = mapper.map(i, ItemLotsDto.class);
			List<String> uploadedFilesUrl = new ArrayList<>();
			List<ItemLotsAttachment> attachments = i.getAttachments();
			if (attachments != null) {
				for (ItemLotsAttachment j : attachments) {

				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + j.getFileName();
				uploadedFilesUrl.add(fileDownloadUri);
					uploadedFilesUrl.add(j.getFileName());

				}
				item.setFileDownloadUri(uploadedFilesUrl);
			}
			itemLotsDtos.add(item);
		}
		return itemLotsDtos;
	}

	public ItemLotsDto saveItemLots(ItemLotsDto itemLotsDto, MultipartFile[] itemLotFiles) {
		String soNumber = "";
		String ilLetterNo = itemLotsDto.getIlLetterNo();
		Date letterDate = itemLotsDto.getIlLetterDate();
		String lotNo = itemLotsDto.getLotNo();
		Integer supplyOrderId = itemLotsDto.getSupplyOrderId();
		SupplyOrder so = new SupplyOrder();
		Integer soLiId = itemLotsDto.getSoLiId();
		Integer type = itemLotsDto.getType();
		if(supplyOrderId != null) {
		    so = soRepository.findById(supplyOrderId).orElse(null);
		    soNumber = so !=null ? so.getOrderNo(): null;	
		}

		if (ilLetterNo != null && !ilLetterNo.isEmpty()) {
			
			if(itemLotsRepository.existsByIlLetterNoAndSupplyOrderId(ilLetterNo, supplyOrderId)) {
				throw new DataNotValidException("Letter no. already exists for this supply order: "  + soNumber);
				
			}
			if (!InputNameValidation.checkString(ilLetterNo)) {
				throw new DataNotValidException("Letter no is invalid.");
			}
		}

		if (lotNo != null && !lotNo.isEmpty()) {
			if(itemLotsRepository.existsByLotNoAndSupplyOrderId(lotNo, supplyOrderId)) {
				throw new DataNotValidException("Lot no. already exists for this supply order: " + soNumber );
			}
			
			if (!InputNameValidation.checkString(lotNo)) {
				throw new DataNotValidException("Lot no is invalid.");
			}
		}

		if (type == null) {
			throw new DataNotValidException("Type cannot be null.");
		} else {
			List<Integer> validTypes = List.of(AMENDMENT_LETTER_TYPE.SUPPLY_ORDER, AMENDMENT_LETTER_TYPE.SO_LINE_ITEM);
			if(!validTypes.contains(type)) {
				throw new DataNotValidException("Type: " + type + " is not valid type (" + validTypes + ").");
			}
		}

		// Modified by: Shridhar R, Date: 13-10-2025
		if(type == AMENDMENT_LETTER_TYPE.SUPPLY_ORDER) {
			if(supplyOrderId == null) {
				throw new IllegalArgumentException("supplyOrderId cannot be null for type supply-order");
			} 
			
			if (itemLotsRepository.existsByIlLetterNoAndLotNoAndIlLetterDateAndSupplyOrderId(ilLetterNo, lotNo, letterDate, supplyOrderId)) {
				throw new IllegalStateException("Item Lot already exists.");
			}
		} else if(type == AMENDMENT_LETTER_TYPE.SO_LINE_ITEM) {
			if(soLiId == null) {
				throw new IllegalArgumentException("SoLiId cannot be null for type so-line-item");
			} 
			
			if (itemLotsRepository.existsByIlLetterNoAndLotNoAndIlLetterDateAndSoLineItemId(ilLetterNo, lotNo, letterDate, soLiId)) {
				throw new IllegalStateException("Item Lot already exists.");
			}
		}

		ItemLots itemLots = mapper.map(itemLotsDto, ItemLots.class);
		List<ItemLotsAttachment> itemLotAttachments = new ArrayList<>();
		if (itemLotFiles != null) {

			List<String> uploadedFilesUrl = new ArrayList<>();
			for (int i = 0; i < itemLotFiles.length; i++) {
				if (FileValidator.checkFile(itemLotFiles[i])) {
					String fileName = fileStorageService.storeFile(itemLotFiles[i], itemLots.getId(), i);
					if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
						ItemLotsAttachment attachment = new ItemLotsAttachment();
						attachment.setFileName(fileName);
						attachment.setItemLots(itemLots);
						String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
								+ fileName;
						uploadedFilesUrl.add(fileDownloadUri);
						itemLotAttachments.add(attachment);
					}
				}
			}
			itemLotsDto.setFileDownloadUri(uploadedFilesUrl);
		}
		itemLots.setAttachments(itemLotAttachments);
		itemLotsRepository.save(itemLots);
		itemLotsDto.setId(itemLots.getId());
		return itemLotsDto;

	}
}
