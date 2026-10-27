package in.co.bel.olms.transaction.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.LineItemApplicableTestDto;
import in.co.bel.olms.transaction.dto.LineItemDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.dto.RegNosDetailsDto;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.service.OlLineItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/olLineItem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlLineItemController {

    @Value("${file.domain-url}")
    private String domainUrl;

    @Value("${spring.application.name}")
    private String applicationName;

    private final OlLineItemService olLineItemService;
    
	private final OlLineItemRepository olLineItemRepository;

    private final Mapper mapper;
    
    private static final Logger logger =LoggerFactory.getLogger(OlLineItemController.class);

    /**
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<OlLineItemDto> getOlLineItemById(@PathVariable int id) throws DataNotFoundException {
        OlLineItem olLineItem = olLineItemService.getOlLineItemById(id);
        OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
        return ResponseEntity.ok(olLineItemDto);
    }

    @GetMapping("/getByOlId/{olId}")
    public ResponseEntity<List<OlLineItemDto>> getOlLineItemByOlId(@PathVariable int olId) {
        List<OlLineItemDto> olLineItemDtos = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        List<OlLineItem> olLineItems = olLineItemService.getOlLineItemByOlId(olId);
        for (OlLineItem olLineItem : olLineItems) {
            OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
            
            // Author: Shridhar
            // gets sample tested,sample selected count for faiSample and sample for OL
            List<Object[]> sample = olLineItemRepository.getOLLineItemsSampleCountByOl(olLineItem.getId());
            for (Object[] obj: sample ) {
    			olLineItemDto.setSampleSelectedCount(Integer.parseInt(obj[1].toString())) ;
    			olLineItemDto.setFaiSampleSelectedCount(Integer.parseInt(obj[2].toString()));
    			olLineItemDto.setTestedSampleCount(Integer.parseInt(obj[3].toString()));
    			olLineItemDto.setTestedFaiSampleCount(Integer.parseInt(obj[4].toString()));
            }

			
            if (olLineItem.getTmrFileName() != null) {
                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + olLineItem.getTmrFileName();
                olLineItemDto.setUpdatedTmrFileUrl(fileDownloadUri);
            }
            SOLineItem soLineItem = olLineItem.getSoLineItem();
            if (soLineItem != null) {
                String underWarrantyStr = soLineItem.getIsUnderWarranty() != null ? soLineItem.getIsUnderWarranty().booleanValue() ? "Yes" : "No" : null;
                olLineItemDto.setUnderWarrantyStr(underWarrantyStr);
                if (Boolean.TRUE.equals(soLineItem.getIsUnderWarranty())) {
                    int warrantyDuration = soLineItem.getDuration();
                    String durationType = soLineItem.getDurationType().getName();
                    olLineItemDto.setWarrantyStr(warrantyDuration + " " + durationType);
                }
                if (soLineItem.getUnitCost() != null) {
                    olLineItemDto.setUnitCostStr(df.format(soLineItem.getUnitCost()));
                }
                if (soLineItem.getTmrFileName() != null) {
                    String fileName = soLineItem.getTmrFileName();
                    String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
                    olLineItemDto.setTmrFileUrl(fileDownloadUri);
                }
                olLineItemDto.setIsImported(soLineItem.getImported());
                olLineItemDto.setPartNo(soLineItem.getPartNumber().getPartNo());
                olLineItemDto.setPartNumberId(soLineItem.getPartNumber().getId());
                olLineItemDto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
                olLineItemDto.setItemCategoryName(soLineItem.getPartNumber().getItemCategory().getCategory());
                Integer soLineItemId=soLineItem.getId();
                Integer productId=olLineItemRepository.getBysoLineItemId(soLineItemId);
                olLineItemDto.setProductId(productId);
               
            }
            olLineItemDtos.add(olLineItemDto);
        }
        return ResponseEntity.ok(olLineItemDtos);
    }

    @GetMapping("/getByOlIdList/{olIds}")
    public ResponseEntity<List<OlLineItemDto>> getOlLineItemsByOlIdList(@PathVariable List<Integer> olIds) {
        List<OlLineItemDto> olLineItemDtos = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        List<OlLineItem> olLineItems = olLineItemService.getbyOlIdList(olIds);
        for (OlLineItem olLineItem : olLineItems) {
            OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
            if (olLineItem.getTmrFileName() != null) {
                String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + olLineItem.getTmrFileName();
                olLineItemDto.setUpdatedTmrFileUrl(fileDownloadUri);
            }
            SOLineItem soLineItem = olLineItem.getSoLineItem();
            if (soLineItem != null) {
                String underWarrantyStr = soLineItem.getIsUnderWarranty() != null ? soLineItem.getIsUnderWarranty().booleanValue() ? "Yes" : "No" : null;
                olLineItemDto.setUnderWarrantyStr(underWarrantyStr);
                if (Boolean.TRUE.equals(soLineItem.getIsUnderWarranty())) {
                    int warrantyDuration = soLineItem.getDuration();
                    String durationType = soLineItem.getDurationType().getName();
                    olLineItemDto.setWarrantyStr(warrantyDuration + " " + durationType);
                }
                if (soLineItem.getUnitCost() != null) {
                    olLineItemDto.setUnitCostStr(df.format(soLineItem.getUnitCost()));
                }
                if (soLineItem.getTmrFileName() != null) {
                    String fileName = soLineItem.getTmrFileName();
                    String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT+ fileName;
                    olLineItemDto.setTmrFileUrl(fileDownloadUri);
                }
                if(olLineItem.getOfferLetter().getConsignee()!=null)             
                {             
                olLineItemDto.setConsigneeName( olLineItem.getOfferLetter().getConsignee().getName()); 
                }
                olLineItemDto.setIsImported(soLineItem.getImported());
            }
            olLineItemDtos.add(olLineItemDto);
        }
        return ResponseEntity.ok(olLineItemDtos);
    }

    @PostMapping("/updateTmrSheet")
    public ResponseEntity<OlLineItemDto> updateTmrSheet(
            @RequestParam(value = "olLineItemStr", required = true) String olLineItemStr,
            @RequestParam(value = "tmrFile", required = false) MultipartFile tmrFile) {

        Gson gson = new Gson();
        OlLineItemDto olLineItemDto = gson.fromJson(olLineItemStr, new TypeToken<OlLineItemDto>() {
        }.getType());
        OlLineItemDto updatedItemDto = olLineItemService.updateTmrSheet(olLineItemDto, tmrFile);
        if (updatedItemDto != null) {

            return new ResponseEntity<>(updatedItemDto, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(updatedItemDto, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/updateHologramDetails")
    public ResponseEntity<String> updateHologramDetails(@RequestBody LineItemDto lineItemDto) {

        Map<Boolean, String> savedStatus = olLineItemService.updateHologramDetails(lineItemDto.getHologramDtos());
        if (savedStatus.containsKey(true)) {
            String returnString = savedStatus.get(true);
            return new ResponseEntity<>(returnString, HttpStatus.CREATED);
        } else {
            String returnString = savedStatus.get(false);
            return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // This method stores the uploaded reports/documents against each OL line item in file system and DB
    // Awadhesh Verma
    @PostMapping("/updateApplicableTests")
    public ResponseEntity<String> updateApplicableTests(
            @RequestParam(value = "applicableTestDtoStr", required = true) String applicableTestDtoStr,
            @RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {

        Gson gson = new Gson();
        try {
            LineItemApplicableTestDto lineItemApplicableTestDto = gson.fromJson(applicableTestDtoStr, new TypeToken<LineItemApplicableTestDto>() {
            }.getType());
            Map<Integer, List<MultipartFile>> attachmentMap = new HashMap<>();
            Map<String, String> attachmentFileNameMap = new HashMap<>();
            Integer attachmentId = null;
            if (attachments != null) {
            for (int i = 0; i < attachments.length; i++) {
                Resource resource = attachments[i].getResource();
                String attachmentIdStr = resource.getFilename();
                // File name is appended with double underscore(__) followed by attachmentId.
                int lastIndexOfSeparator = attachmentIdStr.lastIndexOf("__");
                if (lastIndexOfSeparator != -1) {
                    // extracting the attachment id
                    String fileAttachmentId = attachmentIdStr.substring(lastIndexOfSeparator + 2);
                    // extracting the original file name
                    String fileName = attachmentIdStr.substring(0, lastIndexOfSeparator);
                    attachmentId = Integer.valueOf(fileAttachmentId);
                    attachmentFileNameMap.put(fileAttachmentId, fileName);
                    if (attachmentMap.containsKey(attachmentId)) {
                        List<MultipartFile> multipartFiles = attachmentMap.get(attachmentId);
                        MultipartFile multipartFile = attachments[i];
                        multipartFiles.add(multipartFile);
                        attachmentMap.put(attachmentId, multipartFiles);
                    } else {
                        attachmentMap.put(attachmentId, Arrays.asList(attachments[i]));
                    }
                }
            }
            } else {
            	attachmentMap.put(attachmentId, new ArrayList<>());
            }
            Map<Boolean, String> savedStatus = olLineItemService.updateApplicableTests(lineItemApplicableTestDto, attachmentMap, attachmentFileNameMap);
            if (savedStatus.containsKey(true)) {
                String returnString = savedStatus.get(true);
                return new ResponseEntity<>(returnString, HttpStatus.CREATED);
            } else {
                String returnString = savedStatus.get(false);
                return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            String returnString = "Request failed. Plz try again.";
            return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getApplicableTestsByLineItemId/{olLineItemId}")
    public ResponseEntity<LineItemApplicableTestDto> getApplicableTestsByLineItemId(@PathVariable int olLineItemId) throws DataNotFoundException {
        LineItemApplicableTestDto applicableTestDto = olLineItemService.getApplicableTestsByLineItemId(olLineItemId);
        return ResponseEntity.ok(applicableTestDto);
    }
    
    
    @PostMapping("/editLineItem")
    public ResponseEntity<Object> editLineItem(@RequestBody OlLineItemDto lineItemDto){
    	try {
    		String response = olLineItemService.editLineItem(lineItemDto);
    		return ResponseEntity.ok(response);    		
    	} catch(IllegalArgumentException e) {
    		log.error("/olLineItem/editLineItem IllegalArgumentException: {}", e.getMessage());
    		return ResponseEntity.badRequest().body(e.getMessage());
    	} catch(Exception e) {
    		log.error("/olLineItem/editLineItem Exception: {}", e.getMessage());
    		return ResponseEntity.internalServerError().body("Internal Server Error");
    	}
    }
    
    @PostMapping("/updateLineItemsQty")
    public ResponseEntity<Object> updateLineItemsQty(@RequestBody List<OlLineItemDto> olLineItemDtos) {
        try {
            String response = olLineItemService.updateLineItemsByQty(olLineItemDtos);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("/olLineItem/updateLineItemsQty IllegalArgumentException: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("/olLineItem/updateLineItemsQty Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
    
    @GetMapping("/getDataByOlLiId/{olLiId}")
    public ResponseEntity<List<RegNosDetailsDto>> getSubSlipItemSerialNoRoundIdByOlLineItemId(@PathVariable Integer olLiId) {
        try {
            List<Object[]> response = olLineItemService.getSubSlipItemSerialNoRoundIdByOlLineItemId(olLiId);
            List<RegNosDetailsDto> dtos = new ArrayList<>();

            for (Object[] row : response) {
                RegNosDetailsDto dto = new RegNosDetailsDto();
                dto.setSubSlipSerialNoId((Integer)row[0]); // Assuming the first element is the list of SerialNo
                dto.setSupSlipId((Integer) row[1]); // Assuming the second element is sub_slip_id
                dto.setSsLiId((Integer) row[2]); // Assuming the third element is sub_slip_line_item_id
                dto.setRoundId((Integer) row[3]); // Assuming the fourth element is round_id
                dtos.add(dto);
            }

            return new ResponseEntity<>(dtos, HttpStatus.OK); // Return the list of DTOs, and a 200 OK status.

        } catch (Exception e) {
            logger.error("Error getting data by OL Line Item ID: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //Return 500 if exception occurs
        }
    }

}
