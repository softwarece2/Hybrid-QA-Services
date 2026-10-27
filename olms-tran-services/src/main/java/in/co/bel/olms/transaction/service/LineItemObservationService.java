package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.CheckOffListDto;
import in.co.bel.olms.transaction.dto.CheckOffListMainDto;
import in.co.bel.olms.transaction.dto.LineItemObservationAttachmentDto;
import in.co.bel.olms.transaction.dto.LineItemObservationDetailsDto;
import in.co.bel.olms.transaction.dto.LineItemObservationDto;
import in.co.bel.olms.transaction.dto.LineItemObservationFileDto;
import in.co.bel.olms.transaction.entity.LineItemObservation;
import in.co.bel.olms.transaction.entity.LineItemObservationAttachment;
import in.co.bel.olms.transaction.entity.LineItemObservationDetails;
import in.co.bel.olms.transaction.entity.LineItemObservationFile;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.LineItemObservationDetailsRepository;
import in.co.bel.olms.transaction.repository.LineItemObservationRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LineItemObservationService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final LineItemObservationRepository lineItemObservationRepository;

	private final LineItemObservationDetailsRepository observationDetailsRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final EmployeeRepository employeeRepository;

	private static final Logger logger = LoggerFactory.getLogger(LineItemObservationService.class);

	private final FileStorageService fileStorageService;

	private final Mapper mapper;

	@Transactional
	public LineItemObservationDto saveLineItemObservation(LineItemObservationDto lineItemObservationDto,
			CheckOffListMainDto checkOffListDto, MultipartFile[] uploads) {
		try {
			Integer supplyOrderId = lineItemObservationDto.getSupplyOrderId();
			if (supplyOrderId == null) {
				throw new IllegalArgumentException("supplyOrderId cannot be null or empty");
			}

			Optional<SupplyOrder> supplyOrderOpt = supplyOrderRepository.findById(supplyOrderId);
			if (!supplyOrderOpt.isPresent()) {
				throw new IllegalArgumentException("Supply Order with ID " + supplyOrderId + " not found");
			}

			List<LineItemObservationFileDto> savedFilesDtoList = new ArrayList<>();
			LineItemObservation updatedObservation = new LineItemObservation();

			if (lineItemObservationDto.getId() != 0) {
				Optional<LineItemObservation> observationOpt = lineItemObservationRepository
						.findById(lineItemObservationDto.getId());

				if (observationOpt.isPresent()) {
					LineItemObservation observation = observationOpt.get();
					
						LineItemObservationFile file = new LineItemObservationFile();
						file.setRemarks(lineItemObservationDto.getInputRemarks());
						file.setLineItemObservation(observation);
						file.setCreatedDate(new Date());
					
						Optional.ofNullable(lineItemObservationDto.getCreatedBy())
				        .flatMap(employeeRepository::findById)            
				        .ifPresent(file::setCreatedBy);			
						
						Optional.ofNullable(lineItemObservationDto.getSendTo())
				        .flatMap(employeeRepository::findById)            
				        .ifPresent(file::setSendTo);		
						

						LineItemObservationFileDto fileDto = new LineItemObservationFileDto();
						if (uploads != null && uploads.length > 0) {
							List<LineItemObservationAttachment> attachments = new ArrayList<>();
							attachments = saveAttachment(uploads, "InStageQCReply", file, new LineItemObservationFileDto(),
									attachments, checkOffListDto);
	
							observation.setModifiedDate(new Date());
							List<LineItemObservationAttachmentDto> attachmentDtos = attachments.stream().map(att -> {
								LineItemObservationAttachmentDto attDto = new LineItemObservationAttachmentDto();
								attDto.setId(att.getId());
								attDto.setFileName(att.getFileName());
								attDto.setDocName(att.getDocName());
								attDto.setCategory(att.getCategory());
								return attDto;
							}).toList();

							fileDto.setLineItemObservationAttachmentDtos(attachmentDtos);
							fileDto.setUploadedFiles(
									attachments
											.stream().map(att -> domainUrl + applicationName
													+ ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + att.getFileName())
											.toList());
							file.setLineItemObservationAttachments(attachments);
						}
						savedFilesDtoList.add(fileDto);
						
						observation.getLineItemObservationFiles().add(file);
				
						updatedObservation = lineItemObservationRepository.save(observation);

						fileDto.setId(file.getId());
						fileDto.setRemarks(file.getRemarks());
					

					LineItemObservationDto responseDto = mapper.map(updatedObservation, LineItemObservationDto.class);
					responseDto.setLineItemObservationFileDtos(savedFilesDtoList);

					return responseDto;
				}
			}

			else {
				LineItemObservation itemObservation = mapper.map(lineItemObservationDto, LineItemObservation.class);
				itemObservation.setCreatedDate(new Date());

				List<LineItemObservationDetailsDto> detailsDtos = lineItemObservationDto
						.getLineItemObservationDetails();
				List<LineItemObservationDetails> itemObservationDetailsList = new ArrayList<>();

				for (LineItemObservationDetailsDto detailsDto : detailsDtos) {
					LineItemObservationDetails detail = mapper.map(detailsDto, LineItemObservationDetails.class);
					detail.setLineItemObservation(itemObservation);
					itemObservationDetailsList.add(detail);
				}

				itemObservation.setLineItemObservationDetails(itemObservationDetailsList);

				LineItemObservationFile observationFile = new LineItemObservationFile();
				observationFile.setRemarks(lineItemObservationDto.getInputRemarks());
				observationFile.setCreatedDate(new Date());
				
				Optional.ofNullable(lineItemObservationDto.getCreatedBy())
		        .flatMap(employeeRepository::findById)            
		        .ifPresent(observationFile::setCreatedBy);				
				
				Optional.ofNullable(lineItemObservationDto.getSendTo())
		        .flatMap(employeeRepository::findById)            
		        .ifPresent(observationFile::setSendTo);
				
				
				observationFile.setLineItemObservation(itemObservation);

				List<LineItemObservationAttachment> attachments = new ArrayList<>();

				if (uploads != null && uploads.length > 0) {
					attachments = saveAttachment(uploads, "InStageQC", observationFile,
							new LineItemObservationFileDto(), attachments, checkOffListDto);
				}

				observationFile.setLineItemObservationAttachments(attachments);
				itemObservation.setLineItemObservationFiles(Collections.singletonList(observationFile));

				LineItemObservation savedObservation = lineItemObservationRepository.save(itemObservation);

				LineItemObservationFileDto fileDto = new LineItemObservationFileDto();
				fileDto.setId(observationFile.getId());
				fileDto.setRemarks(observationFile.getRemarks());
				fileDto.setUploadedFiles(attachments.stream().map(att -> domainUrl + applicationName
						+ ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + att.getFileName()).toList());

				List<LineItemObservationAttachmentDto> attachmentDtos = attachments.stream().map(att -> {
					LineItemObservationAttachmentDto dto = new LineItemObservationAttachmentDto();
					dto.setId(att.getId());
					dto.setFileName(att.getFileName());
					dto.setDocName(att.getDocName());
					dto.setCategory(att.getCategory());
					return dto;
				}).toList();

				fileDto.setLineItemObservationAttachmentDtos(attachmentDtos);
				savedFilesDtoList.add(fileDto);

				LineItemObservationDto responseDto = mapper.map(savedObservation, LineItemObservationDto.class);
				responseDto.setLineItemObservationFileDtos(savedFilesDtoList);
				return responseDto;
			}

		} catch (IllegalArgumentException ex) {

			logger.error("Error: {}", ex.getMessage());
			throw ex;
		} catch (Exception e) {
			logger.error("Error occured while saving line item mobservation  ", e);
			logger.error("Error in saving line item observation: {}", e.getMessage());
		}

		return lineItemObservationDto;
	}

	private List<LineItemObservationAttachment> saveAttachment(MultipartFile[] files, String category,
			LineItemObservationFile observationFile, LineItemObservationFileDto fileDto,
			List<LineItemObservationAttachment> lineAttachments, CheckOffListMainDto checkOffListDto) {

		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		List<CheckOffListDto> dtos = checkOffListDto.getCheckOffListDtos();
		for (int i = 0; i < files.length; i++) {
			for (CheckOffListDto dto : dtos) {
				String fileName = fileStorageService.storeFile(files[i], observationFile.getId(), i);
				if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
					LineItemObservationAttachment attachment = new LineItemObservationAttachment();
					attachment.setCategory(category);
					attachment.setCreatedDate(date);
					attachment.setFileName(fileName);
					attachment.setDocName(dto.getDocName());
					attachment.setLineItemObservationFile(observationFile);
					lineAttachments.add(attachment);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
							+ fileName;
					uploadedFilesUrl.add(fileDownloadUri);

					// Update CheckOffListDto with the attachment URL
					if (dto.getAttachmentURLs() == null) {
						dto.setAttachmentURLs(new ArrayList<>());
					}
					dto.getAttachmentURLs().add(fileDownloadUri);
				}
			}
		}

		switch (category) {

		case "InStageQC":
			List<String> lineItemAttachmentsList = fileDto.getUploadedFiles();
			lineItemAttachmentsList.addAll(uploadedFilesUrl);
			fileDto.setUploadedFiles(lineItemAttachmentsList);
			break;

		default:
			break;

		}
		return lineAttachments;
	}

	public LineItemObservationDto getByLineItemObservationId(int id) {
		Optional<LineItemObservation> optionalObservation = lineItemObservationRepository.findById(id);

		if (!optionalObservation.isPresent()) {
			return null;
		}

		LineItemObservation observation = optionalObservation.get();
		LineItemObservationDto dto = mapper.map(observation, LineItemObservationDto.class);

		// Map Details
		if (observation.getLineItemObservationDetails() != null) {
			List<LineItemObservationDetailsDto> detailDtos = observation.getLineItemObservationDetails().stream()
					.map(detail -> mapper.map(detail, LineItemObservationDetailsDto.class))
					.toList();
			dto.setLineItemObservationDetails(detailDtos);
		}

		

		    // Map Files and Attachments
		    if (observation.getLineItemObservationFiles() != null) {
		        List<LineItemObservationFileDto> fileDtos = observation.getLineItemObservationFiles().stream().map(file -> {
		        	 List<String> uploadedFilesUrl = new ArrayList<>();
		            LineItemObservationFileDto fileDto = mapper.map(file, LineItemObservationFileDto.class);

		            if (file.getLineItemObservationAttachments() != null) {
		                List<LineItemObservationAttachmentDto> attachmentDtos = file.getLineItemObservationAttachments()
		                        .stream().map(att -> {
		                            String fileDownloadUri = domainUrl + applicationName 
		                                + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + att.getFileName();
		                            uploadedFilesUrl.add(fileDownloadUri);

		                            return mapper.map(att, LineItemObservationAttachmentDto.class);
		                        })
		                        .toList();
		                

		                fileDto.setLineItemObservationAttachmentDtos(attachmentDtos);
		            }
		            fileDto.setUploadedFiles(uploadedFilesUrl);

		            return fileDto;
		        }).toList();

		        dto.setLineItemObservationFileDtos(fileDtos);
		    }

		    // Optional: If you want to return or use uploadedFilesUrl somewhere, you might need to add a setter or log it.
		    // dto.setUploadedFilesUrl(uploadedFilesUrl); // if needed

		    return dto;
		}
	
	
	public List<LineItemObservationDto> getAllSubmittedSOs(int sentToId) {
	    List<Object[]> result = lineItemObservationRepository.findBySendToId(sentToId);
	    Map<Integer, LineItemObservationDto> observationMap = new LinkedHashMap<>();

	    for (Object[] row : result) {
	        // Extract values from query result
	        Integer fId = (Integer) row[0];
	        String fRemarks = (String) row[1];
	        Integer fCreatedById = (Integer) row[2];
	        Date fCreatedDate = (Date) row[3];
	        Integer fObsId = (Integer) row[4];
	        String fCreatedByName = (String) row[5];

	        Integer oId = (Integer) row[6];
	        Integer supplyOrderId = (Integer) row[7];
	        String title = (String) row[9];

	        // Grouping by observation ID
	        LineItemObservationDto observationDto = observationMap.get(oId);
	        if (observationDto == null) {
	            observationDto = new LineItemObservationDto();
	            observationDto.setId(oId);
	            Optional<SupplyOrder> sup = supplyOrderRepository.findById(supplyOrderId);
	            if (sup.isPresent()) {
	                observationDto.setSupplyOrderNo(sup.get().getOrderNo());
	            }
	            observationDto.setSupplyOrderId(supplyOrderId);
	            observationDto.setCreatedBy(fCreatedById);
	            observationDto.setCreatedByName(fCreatedByName);
	            observationDto.setTittle(title);
	            observationDto.setLineItemObservationFileDtos(new ArrayList<>());
	            observationMap.put(oId, observationDto);
	        }

	        // Create and populate File DTO
	        LineItemObservationFileDto fileDto = new LineItemObservationFileDto();
	        fileDto.setId(fId);
	        fileDto.setRemarks(fRemarks);
	        fileDto.setCreatedDate(fCreatedDate);
	        fileDto.setLineItemObsId(fObsId);	        

	        observationDto.getLineItemObservationFileDtos().add(fileDto);
	    }

	    return new ArrayList<>(observationMap.values());
	}




	public LineItemObservationDto getInstageByLineItemIds(List<Integer> ids)  {
		Integer obsId = observationDetailsRepository.findCommonObservationId(ids, ids.size());
		LineItemObservationDto dto = new LineItemObservationDto();
		if (obsId != null) {
			dto = getByLineItemObservationId(obsId);
		}
		return dto;
	}

}
