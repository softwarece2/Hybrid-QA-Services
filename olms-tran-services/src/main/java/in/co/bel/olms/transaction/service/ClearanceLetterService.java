package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.ClearanceLetterDto;
import in.co.bel.olms.transaction.dto.ClearanceLetterItemDto;
import in.co.bel.olms.transaction.dto.ClearanceLetterPointDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.entity.ClearanceLetter;
import in.co.bel.olms.transaction.entity.ClearanceLetterItem;
import in.co.bel.olms.transaction.entity.ClearanceLetterPoint;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.ClearanceLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClearanceLetterService {

	private final ClearanceLetterRepository clearanceLetterRepo;

	private final OlLineItemRepository olLineItemRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(ClearanceLetterService.class);
	
	private final Mapper mapper;
	
	public Map<Boolean, String> save(ClearanceLetterDto clearanceLetterDto) {
		List<ClearanceLetterPointDto> clearanceLetterPtDto = clearanceLetterDto.getClearanceLetterPointDtos();
		 	
		String copyTo = clearanceLetterDto.getCopyTo();
		String addressedTo = clearanceLetterDto.getAddressedTo();
		
		Map<Boolean, String> returnMap = new HashMap<>();
		try {
			
			if(StringUtils.isNotBlank(copyTo)) {
				if(!InputNameValidation.checkString(copyTo)) {
					throw new DataNotValidException("Invalid copy to");
				}
			}
			
			if(StringUtils.isNotBlank(addressedTo)) {
				if(!InputNameValidation.checkString(addressedTo)) {
					throw new DataNotValidException("Invalid addressed to");
				}
			}
			
			for(ClearanceLetterPointDto item : clearanceLetterPtDto) {
				String contentText = item.getContentText();
				List<ClearanceLetterItemDto> clearanceLetterItemDtos = item.getClearanceLetterItemDtos();
				if(StringUtils.isNotBlank(contentText)) {
					if(!InputNameValidation.checkString(contentText)) {
						throw new DataNotValidException("Invalid content text");
					}
				}
				for(ClearanceLetterItemDto dtos : clearanceLetterItemDtos) {
					String hologramNo = dtos.getHologramNo();
					String unitSlNo = dtos.getUnitSlNo();
					if(StringUtils.isNotBlank(hologramNo)) {
						if(!InputNameValidation.checkString(hologramNo)) {
							throw new DataNotValidException("Invalid Hologram No");
						}
					}
					if(StringUtils.isNotBlank(unitSlNo)) {
						if(!InputNameValidation.checkString(unitSlNo)) {
							throw new DataNotValidException("Invalid Unit Sl No");
						}
					}
				}
			}
			
			
			ClearanceLetter clearanceLetter = mapper.map(clearanceLetterDto, ClearanceLetter.class);
			List<ClearanceLetterPointDto> clearanceLetterPointDtos = clearanceLetterDto.getClearanceLetterPointDtos();
			List<ClearanceLetterPoint> clearanceLetterPoints = new ArrayList<>();
			for (ClearanceLetterPointDto clearanceLetterPointDto : clearanceLetterPointDtos) {
				ClearanceLetterPoint clearanceLetterPoint = mapper.map(clearanceLetterPointDto, ClearanceLetterPoint.class);
				clearanceLetterPoint.setClearanceLetter(clearanceLetter);
				List<ClearanceLetterItem> clearanceLetterItems = new ArrayList<>();
				List<ClearanceLetterItemDto> clearanceLetterItemDtos = clearanceLetterPointDto.getClearanceLetterItemDtos();
				if(clearanceLetterItemDtos != null && !clearanceLetterItemDtos.isEmpty()) {
					for (ClearanceLetterItemDto clearanceLetterItemDto : clearanceLetterItemDtos) {
						ClearanceLetterItem clearanceLetterItem = mapper.map(clearanceLetterItemDto, ClearanceLetterItem.class);
						clearanceLetterItem.setClearanceLetterPoint(clearanceLetterPoint);
						clearanceLetterItems.add(clearanceLetterItem);
					}
				}
				clearanceLetterPoint.setClearanceLetterItems(clearanceLetterItems);
				clearanceLetterPoints.add(clearanceLetterPoint);

			}
			clearanceLetter.setClearanceLetterPoints(clearanceLetterPoints);
			clearanceLetter.setCreatedDate(new Date());
			clearanceLetterRepo.save(clearanceLetter);
			String respString = "Data saved successfully";
			returnMap.put(true, respString);
			return returnMap;
			} catch (DataNotValidException de) {
				logger.error("Validation failed: {}", de.getMessage());
				throw de;
			} catch (Exception e) {
				logger.error("Error occured during saving  ", e);
				logger.error("Error in saving clearance letter {}", e.getMessage());
				String respString = "Some error occurred. Plz try again.";
				returnMap.put(false, respString);
				return returnMap;
			}

	}
	
	public ClearanceLetterDto  getClearanceLetter(int id) {

		ClearanceLetter clearanceLetter = clearanceLetterRepo.findByOlId(id);
			ClearanceLetterDto clearanceLetterDto=mapper.map(clearanceLetter, ClearanceLetterDto.class);
			List<ClearanceLetterItemDto> clearanceLetterItemDto=new ArrayList<>();
			List<ClearanceLetterPointDto> clearanceLetterPointDto=new ArrayList<>();
			
			for(ClearanceLetterPoint clrPoint: clearanceLetter.getClearanceLetterPoints()) {
				
				ClearanceLetterPointDto clrPointDto=mapper.map(clrPoint, ClearanceLetterPointDto.class);
				
				if( !clrPoint.getClearanceLetterItems().isEmpty()) {
				for(ClearanceLetterItem clrItem: clrPoint.getClearanceLetterItems())
				{
					ClearanceLetterItemDto clrItemDto=mapper.map(clrItem, ClearanceLetterItemDto.class);
					List<OlLineItem> olLineItems = olLineItemRepo.getLineItemsByOl(clrItem.getOlLineItem().getId());
					for (OlLineItem olLineItem : olLineItems) {
						OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
						clrItemDto.setDescription(olLineItemDto.getDescription());
						clrItemDto.setPartNo(olLineItemDto.getPartNo());
					
					
					clearanceLetterItemDto.add(clrItemDto);
				}
				
				}
				clrPointDto.setClearanceLetterItemDtos(clearanceLetterItemDto);
				
				clearanceLetterPointDto.add(clrPointDto);
				}
				else {
					clearanceLetterPointDto.add(clrPointDto);
					
				}
			}	
			clearanceLetterDto.setClearanceLetterPointDtos(clearanceLetterPointDto);	
			return clearanceLetterDto;
	}
	
	public ClearanceLetterDto  getAddress(int id) {
		
		List<Object[]>address= clearanceLetterRepo.getAddressByOlId(id);
		ClearanceLetterDto  clearanceLetterDto = new ClearanceLetterDto();
		  for (Object[] addr : address) {
		clearanceLetterDto.setSalesGrpName(addr[0]!=null?addr[0].toString():"");
		clearanceLetterDto.setSupplierName(addr[1] != null ? addr[1].toString() : "");
		clearanceLetterDto.setAddress(addr[2] != null ? addr[2].toString() : "");
		clearanceLetterDto.setFileNo(addr[3] != null ? addr[3].toString() : "");
		clearanceLetterDto.setCqaeName(addr[4] != null ? addr[4].toString() : "");

		  }
		  return clearanceLetterDto;
		  
	}

	
	
}
