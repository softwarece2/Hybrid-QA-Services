package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.ApplicableTestDto;
import in.co.bel.olms.transaction.dto.LineItemApplicableTestDto;
import in.co.bel.olms.transaction.dto.LineItemHologramDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.OlLineItemAttachment;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.LineItemApplicableTestRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlLineItemService {

	@Value("${file.domain-url}")
	private String domainUrl;
	
	@Value("${spring.application.name}")
	private String applicationName;

	private final OlLineItemRepository olLineItemRepository;

	private final LineItemApplicableTestRepository lineItemApplicableTestRepository;

	private final LineItemLotsRegisterRepository lineItemLotsRegisterRepository;

	private final LineItemLotsRegisterService lineItemLotsRegisterService;

	private final FileStorageService fileStorageService;

	private static final Logger logger = LoggerFactory.getLogger(OlLineItemService.class);

	private final Mapper mapper;

	/**
	 *
	 * @param id
	 * @return
	 */

	public OlLineItem getOlLineItemById(int id) {
		Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(id);
		if (olLineItemOptional.isPresent()) {
			return olLineItemOptional.get();
		} else {
			throw new DataNotFoundException("OlLineItem not found By Id: " + id);

		}
	}

	/**
	 *
	 * @return
	 */

	public List<OlLineItem> getAllOlLineServices() {
		return olLineItemRepository.findAll();
	}

	public List<OlLineItem> getByIdList(Set<Integer> idList) {
		return olLineItemRepository.getbyIdList(idList);
	}

	public List<OlLineItem> getOlLineItemByOlId(int olId) {

		return olLineItemRepository.getLineItemsByOl(olId);

	}

	public List<OlLineItem> getbyOlIdList(List<Integer> olIds) {

		return olLineItemRepository.getbyOlIdList(olIds);

	}

	public OlLineItemDto updateTmrSheet(OlLineItemDto olLineItemDto, MultipartFile tmrFile) {
		try {
			Optional<OlLineItem> lineItemOptional = olLineItemRepository.findById(olLineItemDto.getId());

			if (lineItemOptional.isPresent()) {
				OlLineItem olLineItem = lineItemOptional.get();
				mapper.map(olLineItem, olLineItemDto);
				if (olLineItem.getTmrFileName() != null) {
					fileStorageService.deleteFile(olLineItem.getTmrFileName());
				}
				if (tmrFile != null) {
					String fileName = fileStorageService.storeFile(tmrFile, olLineItem.getId(), 1);
					olLineItem.setTmrFileName(fileName);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
					olLineItemDto.setTmrFileUrl(fileDownloadUri);
				}
				olLineItemRepository.save(olLineItem);
			}
		} catch (Exception e) {
			logger.error("Error in  updating OL Line Item attachments {}", e.getMessage());
		}
		return olLineItemDto;
	}

	public Map<Boolean, String> updateHologramDetails(List<LineItemHologramDto> hologramDtos) {

		Map<Boolean, String> returnMap = new HashMap<>();
		try {
			List<OlLineItem> olLineItems = new ArrayList<>();
			for (LineItemHologramDto lineItemHologramDto : hologramDtos) {

				Optional<OlLineItem> lineItemOptional = olLineItemRepository
						.findById(lineItemHologramDto.getOlLineItemId());

				if (lineItemOptional.isPresent()) {
					OlLineItem olLineItem = lineItemOptional.get();
					olLineItem.setHologramRemarks(lineItemHologramDto.getHologramRemarks());
					olLineItems.add(olLineItem);
				}
			}
			olLineItemRepository.saveAll(olLineItems);
			returnMap.put(true, "Updated successfully.");
			return returnMap;
		} catch (Exception e) {
			logger.error("Error in  updating OL Line Item attachments {}", e.getMessage());
			String respString = "Failed. Plz try again.";
			returnMap.put(false, respString);
			return returnMap;
		}
	}

	public Map<Boolean, String> updateApplicableTests(LineItemApplicableTestDto lineItemApplicableTestDto,
													  Map<Integer, List<MultipartFile>> attachmentMap, Map<String, String> attachmentFileNameMap) {

		Map<Boolean, String> returnMap = new HashMap<>();
		try {
			List<ApplicableTestDto> applicableTestDtos = lineItemApplicableTestDto.getApplicableTestDtos();
				List<LineItemApplicableTest> applicableTests = new ArrayList<>();
			for (ApplicableTestDto applicableTestDto : applicableTestDtos) {
				applicableTestDto.setCreatedBy(lineItemApplicableTestDto.getCreatedBy());
				LineItemApplicableTest lineItemApplicableTest = mapper.map(applicableTestDto, LineItemApplicableTest.class);
				
				if (applicableTestDto.getIsAte() == null) {
		            applicableTestDto.setIsAte(false); // Set to false if unticked
		        }
		       
		        if (applicableTestDto.getIsRmt() == null) {
		            applicableTestDto.setIsRmt(false); // Set to false if unticked 
		        }	   
				
				List<Integer> attachmentIds = applicableTestDto.getAttachmentIds();
				List<MultipartFile> attachments = new ArrayList<>();
				if (attachmentIds != null) {
				for (int attachmentId : attachmentIds) {
					attachments.addAll(attachmentMap.get(attachmentId));
				}
				
				} else {
					attachments.add(null);
			}
			saveLineItemAttachment(attachments, lineItemApplicableTest, applicableTestDto, attachmentFileNameMap);
			applicableTests.add(lineItemApplicableTest);
				
		}
			lineItemApplicableTestRepository.saveAll(applicableTests);
			returnMap.put(true, "Updated successfully.");
			return returnMap;
		}catch (Exception e) {
			logger.error("Error in updating applicable tests {}", e.getMessage());
			String respString = "Request failed. Plz try again.";
			returnMap.put(false, respString);
			return returnMap;
		}
	}

	public LineItemApplicableTestDto getApplicableTestsByLineItemId(int olLineItemId) {

		LineItemApplicableTestDto lineItemApplicableTestDto = new LineItemApplicableTestDto();
		try {
			List<LineItemApplicableTest> applicableTests = lineItemApplicableTestRepository.getApplicableTestsByLineItemId(olLineItemId);
			List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();
			for (LineItemApplicableTest lineItemApplicableTest : applicableTests) {
				ApplicableTestDto applicableTestDto = new ApplicableTestDto();
				applicableTestDto.setId(lineItemApplicableTest.getId());
				applicableTestDto.setApplicableTestId(lineItemApplicableTest.getApplicableTest().getId());
				applicableTestDto.setTestName(lineItemApplicableTest.getApplicableTest().getTestName());
				applicableTestDto.setRemarks(lineItemApplicableTest.getRemarks());
				
				applicableTestDto.setIsAte(lineItemApplicableTest.getIsAte());
				applicableTestDto.setIsRmt(lineItemApplicableTest.getIsRmt());
				
				if (lineItemApplicableTest.getIsAte() != null) {
				applicableTestDto.setIsAteStr(lineItemApplicableTest.getIsAte() ? "Yes" : "No");
				} else {
					applicableTestDto.setIsAteStr(null);
				}
				
				if (lineItemApplicableTest.getIsRmt() != null) {
		        applicableTestDto.setIsRmtStr(lineItemApplicableTest.getIsRmt() ? "Yes" : "No");
				} else {
					applicableTestDto.setIsRmtStr(null);
				}
				
				List<OlLineItemAttachment> olLineItemAttachments = lineItemApplicableTest.getLineItemAttachments();
//				List<String> firmAttachments = new ArrayList<>();
				Map<String, String> firmAttachmentURLs = new HashMap<>();
				Map<String, String> inspectorAttachmentURLs = new HashMap<>();
				for (OlLineItemAttachment olLineItemAttachment : olLineItemAttachments) {
					String createdBy = olLineItemAttachment.getCreatedBy();
					applicableTestDto.setCreatedBy(createdBy);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + olLineItemAttachment.getFileName();
					if (createdBy != null && createdBy.equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_FIRM)) {
						firmAttachmentURLs.put(olLineItemAttachment.getFileName(), fileDownloadUri);
					} else if (createdBy != null && createdBy.equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_INSPECTOR)) {
						inspectorAttachmentURLs.put(olLineItemAttachment.getFileName(), fileDownloadUri);
					}
//					firmAttachments.add(fileDownloadUri);
				}
				applicableTestDto.setFirmAttachmentURLs(firmAttachmentURLs);
				applicableTestDto.setInspectorAttachmentURLs(inspectorAttachmentURLs);
				applicableTestDtos.add(applicableTestDto);
			}
			lineItemApplicableTestDto.setApplicableTestDtos(applicableTestDtos);
		} catch (Exception e) {
			logger.error("Error in fetching applicable tests: {}", e.getMessage());
		}
		return lineItemApplicableTestDto;

	}

	private void saveLineItemAttachment(List<MultipartFile> files, LineItemApplicableTest applicableTest,
										ApplicableTestDto applicableTestDto, Map<String, String> attachmentFileNameMap) {

//		List<String> uploadedFilesUrl = new ArrayList<>();
		List<OlLineItemAttachment> lineItemAttachments = new ArrayList<>();
		Map<String, String> firmAttachmentURLs = new HashMap<>();
		Map<String, String> inspectorAttachmentURLs = new HashMap<>();
		for (MultipartFile multipartFile : files) {
			Resource resource = multipartFile.getResource();
			String attachmentIdStr = resource.getFilename();
			int lastIndexOfSeparator = attachmentIdStr.lastIndexOf("__");
			if (lastIndexOfSeparator != -1) {
				// Split the string based on the last index of "_"
				String fileAttachmentId = attachmentIdStr.substring(lastIndexOfSeparator + 2);
				String originalFileName = attachmentFileNameMap.get(fileAttachmentId);
				String fileName = fileStorageService.storeFile(multipartFile, null, 0, originalFileName);
				if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
					OlLineItemAttachment attachment = new OlLineItemAttachment();
					attachment.setFileName(originalFileName);
					attachment.setCreatedBy(applicableTestDto.getCreatedBy());
					attachment.setLineItemApplicableTest(applicableTest);
					lineItemAttachments.add(attachment);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT+ fileName;
					if (applicableTestDto.getCreatedBy().equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_FIRM)) {
						firmAttachmentURLs.put(fileName, fileDownloadUri);
					} else if (applicableTestDto.getCreatedBy().equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_INSPECTOR)) {
						inspectorAttachmentURLs.put(fileName, fileDownloadUri);
					}
//					uploadedFilesUrl.add(fileDownloadUri);
				}
			}
			applicableTest.setLineItemAttachments(lineItemAttachments);
			applicableTestDto.setFirmAttachmentURLs(firmAttachmentURLs);
			applicableTestDto.setInspectorAttachmentURLs(inspectorAttachmentURLs);
		}
	}

	
	

	public String editLineItem(OlLineItemDto olLineItemDto) {
		Date todayDate = new Date();
		Optional<OlLineItem> optional = olLineItemRepository.findById(olLineItemDto.getId());
		if(optional.isPresent()) {
			OlLineItem olLineItem = optional.get();
			olLineItem.setModifiedDateTime(todayDate);
			olLineItem.setModifiedBy(olLineItemDto.getModifiedBy());
			if (!InputNameValidation.checkString(olLineItemDto.getPriority())) {
				String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
				logger.error(respString);
				throw new IllegalArgumentException(respString);
			}
			olLineItem.setPriority(olLineItemDto.getPriority());
			if (!InputNameValidation.checkString(olLineItemDto.getIssueLevel())) {
				String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
				logger.error(respString);
				throw new IllegalArgumentException(respString);
			}
			
			List<Integer> registerNumbers = olLineItemDto.getRegisterNumbers().getIds();
            List<LineItemLotsRegister> existingRegisterNumbers = lineItemLotsRegisterRepository.getRegisterNosByOlLineItemId(olLineItem.getId());

            List<Integer> existingIds = existingRegisterNumbers.stream()
                    .map(LineItemLotsRegister::getId) 
                    .toList();

            List<Integer> removedRegisters = new ArrayList<>();

            for (Integer existingId : existingIds) {
                if (!registerNumbers.contains(existingId)) {
                    removedRegisters.add(existingId);
                }
            }

            List<LineItemLotsRegisterDto> updateDtos = new ArrayList<>();

            //Update registers
            for (LineItemLotsRegister register : existingRegisterNumbers) {
                if (registerNumbers.contains(register.getId())) {
                    LineItemLotsRegisterDto dto = new LineItemLotsRegisterDto();
                    dto.setIds(List.of(register.getId()));
                    dto.setOlLineItemId(olLineItem.getId());
                    dto.setStatus(register.getStatus());
                    updateDtos.add(dto);
                } else {
                    LineItemLotsRegisterDto dto = new LineItemLotsRegisterDto();
                    dto.setIds(List.of(register.getId()));
                    dto.setOlLineItemId(null);
                    dto.setStatus("pending");
                    updateDtos.add(dto);
                }
            }

            for (LineItemLotsRegisterDto dto : updateDtos) {
                lineItemLotsRegisterService.update(dto);
            }

            //if there are any new ids in the registerNumbers list
            List<Integer> newRegisters = new ArrayList<>();
            for(Integer registerNumber : registerNumbers){
                if(!existingIds.contains(registerNumber)){
                    newRegisters.add(registerNumber);
                }
            }

            if(!newRegisters.isEmpty()){
                for (Integer id : newRegisters) {
                    LineItemLotsRegisterDto newRegisterDto = new LineItemLotsRegisterDto();
                    newRegisterDto.setOlLineItemId(olLineItem.getId());
                    newRegisterDto.setIds(List.of(id));
                    newRegisterDto.setStatus(olLineItemDto.getRegisterNumbers().getStatus());

                    lineItemLotsRegisterService.update(newRegisterDto);
                }
            }
			
			olLineItem.getId();
			olLineItem.setIssueLevel(olLineItemDto.getIssueLevel());
			olLineItem.setOfferedQty(olLineItemDto.getOfferedQty());
			olLineItemRepository.save(olLineItem);
		} else
			throw new IllegalArgumentException("Ol Line Item with id " + olLineItemDto.getId() + "does not exist"); 

		return ApplicationConstants.SUCCESS;
	}

	public String updateLineItemsByQty(List<OlLineItemDto> olLineItemDtos) {
	    Date todayDate = new Date();

	    for (OlLineItemDto lineItemDto : olLineItemDtos) {
	        Optional<OlLineItem> optional = olLineItemRepository.findById(lineItemDto.getId());
	        if (optional.isPresent()) {
	            OlLineItem olLineItem = optional.get();
	            olLineItem.setModifiedDateTime(todayDate);
	            olLineItem.setClearedQty(lineItemDto.getClearedQty());
	            olLineItem.setReaminaingQty(olLineItem.getOfferedQty().subtract(olLineItem.getClearedQty())  );
	            olLineItemRepository.save(olLineItem);
	        } else {
	            throw new IllegalArgumentException("Ol Line Item with id " + lineItemDto.getId() + " does not exist");
	        }
	    }

	    return ApplicationConstants.SUCCESS;
	}
	
	public List<Object[]> getSubSlipItemSerialNoRoundIdByOlLineItemId(int olLineItemId) {
		logger.info("This method was called with olLineItemId: {}", olLineItemId);
		return olLineItemRepository.getSubSlipItemSerialNoRoundId(olLineItemId);
	}
}
