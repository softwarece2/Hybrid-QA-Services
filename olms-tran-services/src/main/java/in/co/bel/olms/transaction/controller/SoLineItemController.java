package in.co.bel.olms.transaction.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataInUseException;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlTmrFileDto;
import in.co.bel.olms.transaction.dto.SoLineItemDto;
import in.co.bel.olms.transaction.dto.SoLineItemInputDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.service.so.SoLineItemService;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 25-11-22
 */

@RestController
@RequestMapping("/soLineItem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoLineItemController {
	
	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final SoLineItemService soLineItemService;
	
	private final HmisAppSecurity hmisAppSecurity = new HmisAppSecurity();

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(SoLineItemController.class);

	@GetMapping("/{id}")
	public ResponseEntity<SoLineItemDto> getSoLineItemById(@PathVariable int id) throws DataNotFoundException {
		SOLineItem soLineItem = soLineItemService.getSoLineItemById(id);
		SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
		return ResponseEntity.ok(soLineItemDto);
	}

	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/save")
	public ResponseEntity<String> saveSoLineItems(@RequestBody @Valid SoLineItemInputDto soLineItemInputDto, @RequestHeader("Authorization") String token) {
		
		Integer empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
		
		Map<Boolean, String> savedStatus = soLineItemService.saveSoLineItems(soLineItemInputDto, empId);
		if (savedStatus.containsKey(true)) {
			String returnString = savedStatus.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);
		} else {
			String returnString = savedStatus.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getAll")
	public ResponseEntity<List<SoLineItemDto>> getAllSoLineItems() {

		List<SoLineItemDto> soLineItemDtos = new ArrayList<>();
		List<SOLineItem> soLineItems = soLineItemService.getAllSoLineItems();
		for (SOLineItem soLineItem : soLineItems) {
			SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
			soLineItemDtos.add(soLineItemDto);
		}
		return ResponseEntity.ok(soLineItemDtos);

	}

	@GetMapping("/getBySoId/{soId}")
	public ResponseEntity<List<SoLineItemDto>> getSoLineItemsBySoId(@PathVariable int soId) {
		try {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);
		List<SoLineItemDto> soLineItemDtos = new ArrayList<>();
		List<SOLineItem> soLineItems = soLineItemService.getSoLineItemsBySoId(soId);
		for (SOLineItem soLineItem : soLineItems) {
			SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
			
			//Modified By: Chirag, Date: 12-10-26
			if(soLineItem.getWarrPeriodType() != null) {
				soLineItemDto.setWarrPeriodType(soLineItem.getWarrPeriodType().getWarrantType());
			}
			soLineItemDto.setDescription(soLineItem.getPartNumber().getMaterialDescription());
			if(soLineItemDto.getDeliveryDate() != null)
				soLineItemDto.setDeliveryDt(dateFormat.format(soLineItemDto.getDeliveryDate())  );
				
			String consignees=soLineItemService.getConsigneesBySliId(soLineItemDto.getId());
			List<Integer> consigneeIds =soLineItemService.getConsigneesIdBySliId(soLineItemDto.getId());
			soLineItemDto.setConsignee(consignees);
			soLineItemDto.setConsigneeIds(consigneeIds);
			Boolean underWarranty = soLineItem.getIsUnderWarranty();
			if (underWarranty != null) {
				String underWarrantyStr = soLineItem.getIsUnderWarranty().booleanValue() ? "Yes" : "No";
				soLineItemDto.setUnderWarrantyStr(underWarrantyStr);
				if(Boolean.TRUE.equals(soLineItem.getIsUnderWarranty())) {
					int warrantyDuration = soLineItem.getDuration();
					String durationType = soLineItem.getDurationType().getName();
					soLineItemDto.setWarrantyStr(warrantyDuration+" "+durationType);
				}
			}
			if(soLineItem.getTmrFileName() != null) {
				String fileName = soLineItem.getTmrFileName();
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + fileName;
				soLineItemDto.setTmrFileUrl(fileDownloadUri);
			}
			soLineItemDto.setConsignee(consignees);
			List<OlTmrFileDto> olTmrFileDtos = new ArrayList<>();
			List<OlLineItem> olLineItems = soLineItem.getOlLineItems();
			BigDecimal clearedQty = BigDecimal.ZERO;
			for (OlLineItem olLineItem : olLineItems) {
				if (olLineItem.getClearedQty() != null) {
					clearedQty = clearedQty.add(olLineItem.getClearedQty()) ;
				}
				if (olLineItem.getTmrFileName() != null) {
					OfferLetter offerLetter = olLineItem.getOfferLetter();
					String fileName = olLineItem.getTmrFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + fileName;
					OlTmrFileDto olTmrFileDto = new OlTmrFileDto();
					olTmrFileDto.setOlNo(offerLetter.getOlNumber());
					olTmrFileDto.setOfferDate(dateFormat.format(offerLetter.getOfferDate()));
					olTmrFileDto.setTmrFileUrl(fileDownloadUri);
					olTmrFileDtos.add(olTmrFileDto);
				}
			}
			//Calculating and setting the remaining qty for the item
			 BigDecimal remainingQty = soLineItem.getOrderQty().subtract(clearedQty);
//			int remainingQty =soLineItemService.getRemainingQtyBySoLiId(soLineItem.getId());
			soLineItemDto.setRemainingQty(remainingQty.compareTo(BigDecimal.ZERO)>0 ? remainingQty : BigDecimal.ZERO);
			if(soLineItem.getUnitCost() != null) {
				soLineItemDto.setUnitCostStr(df.format(soLineItem.getUnitCost()));
			}
			soLineItemDto.setSupplyTypeName(soLineItem.getSupplyType().getSoType());
			soLineItemDto.setRegistrationNoName(soLineItem.getRegistrationNo()!= null? soLineItem.getRegistrationNo().getRegType(): null);
			if (soLineItem.getOrderTypeId() != null) {
			    soLineItemDto.setOrderTypeName(soLineItem.getOrderTypeId().getOrderType());
			} else {
			    soLineItemDto.setOrderTypeName(null); // or "N/A"
			}
			soLineItemDto.setOlTmrFileDtos(olTmrFileDtos);
			soLineItemDto.setIsRegistrationNo(soLineItem.getSupplyOrder().getIsRegistrationNo());
			if (soLineItem.getStatusId()!=null){
				soLineItemDto.setStatus(soLineItem.getStatusId().getStatus());
			}
			if (soLineItem.getPartNumber() != null) {
				soLineItemDto.setPartNo(soLineItem.getPartNumber().getPartNo());
			}
			if (soLineItem.getDeptId() != null) {
				soLineItemDto.setDepartmentName(soLineItem.getDeptId().getDepartmentName());
			}
			soLineItemDtos.add(soLineItemDto);
		}
		return ResponseEntity.ok(soLineItemDtos);

	}catch(Exception e) {
		logger.error("Error occurred in getting SoLineItemsBySoId {}", e.getMessage());
		return null;
	}
	}
	
	@GetMapping("/getBySaleOrderId/{saleOrderId}")
	public ResponseEntity<List<SoLineItemDto>> getSoLineItemsBySaleOrderId(@PathVariable int saleOrderId) {

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<SoLineItemDto> soLineItemDtos = new ArrayList<>();
		List<SOLineItem> soLineItems = soLineItemService.getSoLineItemsBySaleOrderId(saleOrderId);
		for (SOLineItem soLineItem : soLineItems) {
			SoLineItemDto soLineItemDto = mapper.map(soLineItem, SoLineItemDto.class);
			soLineItemDto.setGcs(soLineItem.getIsGcs());
			Boolean underWarranty = soLineItem.getIsUnderWarranty();
			if (underWarranty != null) {
				String underWarrantyStr = soLineItem.getIsUnderWarranty().booleanValue() ? "Yes" : "No";
				soLineItemDto.setUnderWarrantyStr(underWarrantyStr);if(Boolean.TRUE.equals(soLineItem.getIsUnderWarranty())) {
					int warrantyDuration = soLineItem.getDuration();
					String durationType = soLineItem.getDurationType().getName();
					soLineItemDto.setWarrantyStr(warrantyDuration+" "+durationType);
				}
			}
			BigDecimal clearedQty = BigDecimal.ZERO;
			List<OlLineItem> olLineItems = soLineItem.getOlLineItems();
			for (OlLineItem olLineItem : olLineItems) {
				if (olLineItem.getClearedQty() != null) {
					clearedQty = clearedQty.add(olLineItem.getClearedQty()) ;
				}
			}
			BigDecimal remainingQty = soLineItem.getOrderQty().subtract(clearedQty); 
			soLineItemDto.setRemainingQty(remainingQty.compareTo(BigDecimal.ZERO) > 0 ? remainingQty : BigDecimal.ZERO);
			if(soLineItem.getUnitCost() != null) {
				soLineItemDto.setUnitCostStr(df.format(soLineItem.getUnitCost()));
			}
			soLineItemDtos.add(soLineItemDto);
		}
		return ResponseEntity.ok(soLineItemDtos);

	}
		
	
	 @PreAuthorize("hasAnyAuthority('PLANNER')")
	 @DeleteMapping("/delete/{id}")
	    public ResponseEntity<String> deleteLineItem(@PathVariable int id) {
	        try {
	        	soLineItemService.deleteLineItem(id);
	            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	        } catch (DataInUseException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	        catch (Exception e) {     
	        	 logger.error("Delete LineItem error: ", e);
	        	 logger.error("Error deleting line item with id {}: {}", id, e.getMessage());
	            return new ResponseEntity<>("Error deleting", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	 }
	 
	 @GetMapping("/getBySoLineItemId/{soLineItemId}")
	 public ResponseEntity<List<SoLineItemDto>> getSoLineItemDetails(@PathVariable int soLineItemId) throws ParseException 
	 {  
     	List<SoLineItemDto>soLineItemDtos=soLineItemService.getSoLineItemDetails(soLineItemId);
		return ResponseEntity.ok(soLineItemDtos);
      
	 }
	 
		@PostMapping("/edit")
		public ResponseEntity<SoLineItemDto> editSoLineItem(
				@RequestParam(value = "soLineItemStr", required = true) String soLineItemStr,
				@RequestParam(value = "tmrFile", required = false) MultipartFile tmrFile
				) {
			try {

			Gson gson = new Gson();
			SoLineItemDto soLineItemDto = gson.fromJson(soLineItemStr, new TypeToken<SoLineItemDto>() {
			}.getType());
			SoLineItemDto updatedItemDto = soLineItemService.editSoLineItem(soLineItemDto, tmrFile);
			if (updatedItemDto != null) {

				return new ResponseEntity<>(updatedItemDto, HttpStatus.ACCEPTED);
			}}catch(Exception e) {
				logger.error("Edit LineItem Error: {}", e.getMessage());
			
				
			}
//			} else {
//				return new ResponseEntity<>(updatedItemDto, HttpStatus.INTERNAL_SERVER_ERROR);
	//
//			}
			return null;
		

		}
}
