package in.co.bel.olms.transaction.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.repository.PartnoProductMappingRepository;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.transaction.dto.OLLineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.SampleSelectionDto;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OLLineItemSerialNoService {

	private final Mapper mapper;

	private final OLLineItemSerialNoRepository oLLineItemSerialNoRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final PartnoProductMappingRepository partnoProductMappingRepository;

	private static final Logger logger = LoggerFactory.getLogger(OLLineItemSerialNoService.class);

	public List<String> generateSerialNumbers(String prefix, int startNumber, int endNumber, String postfix) {
	        List<String> serialNumbers = new ArrayList<>();

	        if (prefix == null || prefix.isEmpty()) {
	            log.error("Prefix cannot be null or empty.");
	            return serialNumbers; 
	        }

	        if (startNumber > endNumber) {
	            log.error("Start number cannot be greater than end number.");
	            return serialNumbers; 
	        }

	        if (startNumber <= 0) {
	            log.error("Start number must be greater than zero.");
	            return serialNumbers;
	        }
	   
	        for (int i = startNumber; i <= endNumber; i++) {
	            String serialNumber = prefix + i + postfix;
	            serialNumbers.add(serialNumber);
	        }

	        log.debug("Generated serial numbers: {}", serialNumbers); 
	        return serialNumbers;
	    }
	  
	  
	  public List<OLLineItemSerialNoDto> saveOlLineItemSerialDetails(List<OLLineItemSerialNoDto> dtoList) { 
		    List<OLLineItemSerialNoDto> responseData = new ArrayList<>();

		    for (OLLineItemSerialNoDto dto : dtoList) {
		        List<OLLineItemSerialNo> existingRecords =oLLineItemSerialNoRepository.findBySerialNoIdAndOlLineItemId(dto.getSerialNoId(), dto.getOlLineItemId());
	        	OLLineItemSerialNo olLineItemSerialNo;

		        
		        
		        if (!existingRecords.isEmpty()) {
		            olLineItemSerialNo = existingRecords.get(0);
		            olLineItemSerialNo.setActive(dto.getActive());
//		            olLineItemSerialNo.setSerialNo(dto.getSerialNo());
		        } else {
		            olLineItemSerialNo = mapper.map(dto, OLLineItemSerialNo.class);
		            Optional<OlLineItem> optionalLineItem = olLineItemRepository.findById(dto.getOlLineItemId());
		            optionalLineItem.ifPresent(olLineItem -> olLineItemSerialNo.setOlLineItem(olLineItem));
		            // olLineItemSerialNo.setCreatedDateTime(now); 
		        }
		        OLLineItemSerialNo savedOlLineItemSerialNo = oLLineItemSerialNoRepository.save(olLineItemSerialNo);
		        OLLineItemSerialNoDto savedDto = mapper.map(savedOlLineItemSerialNo, OLLineItemSerialNoDto.class);
		        savedDto.setOlLineItemId(savedOlLineItemSerialNo.getOlLineItem() != null 
                        ? savedOlLineItemSerialNo.getOlLineItem().getId() 
                        : null);
		        responseData.add(savedDto);
		    }
		    return responseData;
		}
	  
		public List<OLLineItemSerialNoDto> getOlLineItemSerialDetailsByLineItemId(Integer olLineItemId) {
		    // Fetch all OlLineItemSerialDetails by OlLineItem ID
		    List<OLLineItemSerialNo> olLineItemSerialNosList = oLLineItemSerialNoRepository.findByOlLineItemIdOrderBySerialNoIdAsc(olLineItemId);

		    return olLineItemSerialNosList.stream()
		            .map(olLineItemSerialNo -> {
		            	OLLineItemSerialNoDto dto = mapper.map(olLineItemSerialNo, OLLineItemSerialNoDto.class);
		                // If needed, set the olLineItemId explicitly
		                dto.setOlLineItemId(olLineItemSerialNo.getOlLineItem() != null ? olLineItemSerialNo.getOlLineItem().getId() : null);
		                dto.setStatus(olLineItemSerialNo.getStatus());
		                return dto;
		            })
		            .toList();
		
		}
		
		public List<OLLineItemSerialNoDto> getOlLineItemSerialDetailsByLineItemIdForFirm(Integer olLineItemId) {
		    // Fetch all OlLineItemSerialDetails by OlLineItem ID
		    List<OLLineItemSerialNo> olLineItemSerialNosList = oLLineItemSerialNoRepository.findByOlLineItemIdOrderBySerialNoIdAsc(olLineItemId);
		
		    return olLineItemSerialNosList.stream()
		            .map(olLineItemSerialNo -> {
		            	OLLineItemSerialNoDto dto = mapper.map(olLineItemSerialNo, OLLineItemSerialNoDto.class);
		                // If needed, set the olLineItemId explicitly
		                dto.setOlLineItemId(olLineItemSerialNo.getOlLineItem() != null ? olLineItemSerialNo.getOlLineItem().getId() : null);
                  dto.setStatus(olLineItemSerialNo.getStatus());
		                return dto;
		            })
		            .toList();
		
		}
		

		public List<OLLineItemSerialNoDto> getUnSampledOlLineItemSerialDetailsByLineItemId(Integer olLineItemId) {
			List<Object[]> regnos = oLLineItemSerialNoRepository.findUnSampledByOlLineItemId(olLineItemId);
			List<OLLineItemSerialNoDto> olDtos = new ArrayList<>();
			for (Object[] obj : regnos) {
				OLLineItemSerialNoDto olDto = new OLLineItemSerialNoDto();
				olDto.setId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : null);
        olDto.setSerialNoId(obj[1] != null ? Integer.parseInt(obj[1].toString()) : null);
        olDto.setSerialNo(obj[2] != null ? obj[2].toString() : null);
        olDto.setOlLineItemId(obj[3] != null ? Integer.parseInt(obj[3].toString()) : null);
        olDto.setLineItemNo(obj[4] != null ? obj[4].toString() : null);
        olDto.setStatus(obj[5] != null ? obj[5].toString() : null);
				olDtos.add(olDto);
			}
			return olDtos;
		}

		public List<OLLineItemSerialNoDto> getSampledOlLineItemSerialDetailsByLineItemId(Integer olLineItemId) {
			List<Object[]> regnos = oLLineItemSerialNoRepository.findSampledByOlLineItemId(olLineItemId);
			List<OLLineItemSerialNoDto> olDtos = new ArrayList<>();
			for (Object[] obj : regnos) {
				OLLineItemSerialNoDto olDto = new OLLineItemSerialNoDto();
				olDto.setId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : null);
				olDto.setSerialNo(obj[1] != null ? obj[1].toString() : null);
				olDto.setOlLineItemId(obj[2] != null ? Integer.parseInt(obj[2].toString()) : null);
				olDto.setLineItemNo(obj[3] != null ? obj[3].toString() : null);
        olDto.setStatus(obj[4] != null ? obj[4].toString() : null);
				olDto.setOlId(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
				olDto.setSerialNoId(obj[6]!=null ? Integer.parseInt(obj[6].toString()): null);
				olDto.setProductId(obj[7]!=null ? Integer.parseInt(obj[7].toString()): null);
				olDtos.add(olDto);
			}
			return olDtos;
		}

		public List<OLLineItemSerialNoDto> getUnSampledForFaiOlLineItemSerialDetailsByLineItemId(Integer olLineItemId) {
			
//			
			PartNumber partNumber = olLineItemRepository.findById(olLineItemId).orElse(null).getSoLineItem().getPartNumber();
			Integer productId = partnoProductMappingRepository.findProductNoByPartNumberId(partNumber.getId());
			List<Object[]> regnos = oLLineItemSerialNoRepository.findUnSampledForFaiByOlLineItemId(olLineItemId);
			List<OLLineItemSerialNoDto> olDtos = new ArrayList<>();
			for (Object[] obj : regnos) {
				OLLineItemSerialNoDto olDto = new OLLineItemSerialNoDto();
				olDto.setId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : null);
        olDto.setSerialNoId(obj[1] != null ? Integer.parseInt(obj[1].toString()) : null);
        olDto.setSerialNo(obj[2] != null ? obj[2].toString() : null);
        olDto.setOlLineItemId(obj[3] != null ? Integer.parseInt(obj[3].toString()) : null);
        olDto.setLineItemNo(obj[4] != null ? obj[4].toString() : null);
        olDto.setOlId(obj[7] != null ? Integer.parseInt(obj[7].toString()) : null);
        olDto.setProductId(obj[8]!=null ? Integer.parseInt(obj[8].toString()): null);
				olDtos.add(olDto);
			}
			return olDtos;
		}



		public List<OLLineItemSerialNoDto> getFaiOlLineItemSerialDetailsByLineItemId(Integer olLineItemId) {
			List<Object[]> regnos = oLLineItemSerialNoRepository.findFaiByOlLineItemId(olLineItemId);
			List<OLLineItemSerialNoDto> olDtos = new ArrayList<>();
			for (Object[] obj : regnos) {
				OLLineItemSerialNoDto olDto = new OLLineItemSerialNoDto();
				olDto.setId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : null);
				olDto.setSerialNo(obj[1] != null ? obj[1].toString() : null);
				olDto.setOlLineItemId(obj[2] != null ? Integer.parseInt(obj[2].toString()) : null);
				olDto.setLineItemNo(obj[3] != null ? obj[3].toString() : null);
        olDto.setStatus(obj[4] != null ? obj[4].toString() : null);
				olDto.setOlId(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
				olDto.setSerialNoId(obj[6] != null ? Integer.parseInt(obj[6].toString()) : null);
				olDtos.add(olDto);
			}
			return olDtos;
		}

  public String updateSampleStatus(OLLineItemSerialNoDto reqDto) {

    if (reqDto.getIds() == null || reqDto.getIds().isEmpty())
      throw new IllegalArgumentException("Ol serial no Ids cannot be null.");

    if (reqDto.getIsSample() == null && reqDto.getIsFaiSample() == null)
      throw new IllegalArgumentException("Either isSample or isFaiSample value is required.");

		List<OLLineItemSerialNo> existingRecords = oLLineItemSerialNoRepository.findBySerialNoIds(reqDto.getIds());
		
		if (existingRecords.get(0).getOlLineItem().getSoLineItem().getRegistrationNo()
				.getId() != ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
			

    for (OLLineItemSerialNo record : existingRecords) {
      Boolean newSample = reqDto.getIsSample() != null ? reqDto.getIsSample() : null;
      Boolean newFaiSample = reqDto.getIsFaiSample() != null ? reqDto.getIsFaiSample() : null;

      if (newSample != null) {
        if (newSample && Boolean.TRUE.equals(record.getIsFaiSample()))
          throw new IllegalArgumentException("Cannot set as Sample: This record is already marked as an FAI Sample.");

        record.setIsSample(newSample);
      }

      if (newFaiSample != null) {
        if (newFaiSample && Boolean.TRUE.equals(record.getIsSample()))
          throw new IllegalArgumentException("Cannot set as FAI Sample: This record is already marked as a Sample.");

        record.setIsFaiSample(newFaiSample);
      }
    }

			oLLineItemSerialNoRepository.saveAll(existingRecords);
		}

		return "Data updated successfully";

  }

		public List<SampleSelectionDto> getProductAndRegNoByOlId(Integer olId) {
			return oLLineItemSerialNoRepository.getProductAndRegNosByOlId(olId).stream().map(resultArray -> {
				SampleSelectionDto dto = new SampleSelectionDto();
				dto.setRegId((Integer) resultArray[0]);
				dto.setOlLineItemId((Integer) resultArray[1]);
				dto.setRegNo((String) resultArray[2]);
				dto.setSoLineItemNo((String) resultArray[3]);
				dto.setProductId((Integer) resultArray[5]);
				dto.setStatus((String) resultArray[7]);
//				if(resultArray[8]!= null ) {
//					dto.setQaStatusId((Integer) resultArray[7]);
//				    dto.setQaStatus((String) resultArray[8]);
//				}
//				else {
//					dto.setQaStatusId(null);
//				    dto.setQaStatus(null);
//				}
				
				return dto;
			}).toList();
		}
}
