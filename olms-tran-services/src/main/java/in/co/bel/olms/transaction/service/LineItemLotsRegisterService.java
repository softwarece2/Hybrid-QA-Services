package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.dozer.Mapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.repository.SerialNoStatusRepository;
import in.co.bel.olms.transaction.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.SampleSelectionDto;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.LineItemLots;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoHistoryRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineItemLotsRegisterService {

    private final LineItemLotsRegisterRepository lineItemLotsRegisterRepository;

    private final LineItemSerialNoRepository lineItemSerialNoRepository;
    
    private final LineItemSerialNoHistoryRepository lineItemSerialNoHistoryRepository;

    private final OlLineItemRepository olLineItemRepository;

    private final SerialNoStatusRepository serialNoStatusRepository;

    private final LineItemSerialNoService lineItemSerialNoService;

    private final SoLineItemRepository soLineItemRepository;

    private final Mapper mapper;
    
    private static final String NOT_FOUND_SUFFIX = " not found";
    
    private static final String REGISTER_ENTRY_NOT_FOUND_PREFIX = "Register entry with ID ";
    
    private static final String INVALID_REGISTER_ID_PREFIX = "Invalid register Id: ";
  
    /**
     * Saves register numbers for a given line item lot.
     * @param requestDto
     * @return "success" upon successful registration.
     */
    @Transactional
    public String saveRegisters(LineItemLotsRegisterDto requestDto) {
    	
    	Integer soLineItemId = requestDto.getSoLineItemId();		//fetch the so line item row obj
    	
    	List<String> existRegNos = lineItemSerialNoRepository.findSrNoBySoLineItemId(soLineItemId);		// Fetch existing register numbers from DB
    
    	
    	List<String> registerNos = requestDto.getRegisterNoList();
      
    	
    	Optional<SOLineItem> solineop= soLineItemRepository.findById(soLineItemId);
    	SOLineItem soline =solineop.get();
    	
    	//Commented on 31-07-26 to merger the code to be rechecked
    	// Code to be rechecked for new changes
    	BigDecimal totalCount = BigDecimal.valueOf(existRegNos.size() + registerNos.size());
    	if (totalCount.compareTo(soline.getOrderQty()) > 0) {
    		throw new IllegalArgumentException("ERROR: The number of Registration nos. should not be more than the total quantity ");
    	}
    	    	
    	soLineItemRepository.updateStatus(soline.getId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.IN_PROGRESS);

        
        if (registerNos == null || registerNos.isEmpty()) {
        	throw new IllegalArgumentException("ERROR: No register numbers provided.");
        }
        
        Integer serialNoStatusId = SERIAL_NO_STATUS_CONSTANTS.CREATED;
        SerialNoStatus status1 = serialNoStatusRepository.findById(serialNoStatusId)
        		.orElseThrow(() -> new IllegalArgumentException("Serial Status with ID " + serialNoStatusId + NOT_FOUND_SUFFIX));
        
        List<LineItemSerialNo> lineItemSerialNoList = new ArrayList<>();
        
        for (String registerNo : registerNos) {
        	if (existRegNos != null && existRegNos.contains(registerNo)) {
                throw new IllegalArgumentException("ERROR: Register numbers generated are already exists for line item lot ID " + soLineItemId);
            }
        	try {
	            LineItemSerialNo lineItemSerialNo = new LineItemSerialNo();
	            lineItemSerialNo.setSerialNo(registerNo);
	            
	            
	    		lineItemSerialNo.setStatus(status1);
	            lineItemSerialNo.setCreatedAt(new Date());	
	            lineItemSerialNo.setSoLineItem(soline);
	            lineItemSerialNoList.add(lineItemSerialNo);
	            lineItemSerialNo = lineItemSerialNoRepository.save(lineItemSerialNo);
	            
	            LineItemSerialNoHistoryDto historyDto = new LineItemSerialNoHistoryDto();
	            historyDto.setCreatedById(requestDto.getEmpId());
	            historyDto.setLineItemSerialNoId(lineItemSerialNo.getId());
	            historyDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.CREATED);
	            lineItemSerialNoService.addHistory(historyDto);
	            
	        } catch (DataIntegrityViolationException e) {
	            throw new IllegalArgumentException("ERROR: Register number: " + registerNo + " already exists for line item lot ID " );
	        }
        }
       
        return "success";
    }
    
    
    /**
     * Saves register numbers for a given Ol / Submission slip.
     * DAte 30-01-2026
     * Manasa
     * @param requestDto
     * @return "success" upon successful registration.
     */
    @Transactional
    public String saveRegisterByOl(LineItemLotsRegisterDto requestDto) {    	 	
    	Integer olLineItemId = requestDto.getOlLineItemId();  	
    	
    	Optional<OlLineItem> ollineop= olLineItemRepository.findById(olLineItemId);
    	OlLineItem olline =ollineop.get();      
        for (Integer regNo : requestDto.getIds()) 
        {
            try {
               LineItemLotsRegister  lineItemLotsRegister = new LineItemLotsRegister();
              //get serial no obj 
              LineItemSerialNo ser= lineItemSerialNoRepository.getById(regNo);
              lineItemLotsRegister.setLineItemSerialNo(ser); 
              lineItemLotsRegister.setOlLineItemId(olline);  
              lineItemLotsRegister.setStatus("pending");
              lineItemLotsRegisterRepository.save(lineItemLotsRegister);
            } catch (DataIntegrityViolationException e) {  //Specific exception for unique constraint violation
            	log.error("message: " + e.getMessage());
                throw new IllegalArgumentException("ERROR: Register number: " + regNo + " already exists for line item lot ID " );
            }
        }
		return "success";
    }

	/**
     * Updates the register number for a given ID.
     * @param id The ID of the register to update.
     * @param registerNumber The new register number.
     * @return A success message if updated.
     */
    @Transactional
    public String updateRegisterNumber(Integer id, String registerNumber) {
        if (id == null) {
            throw new IllegalArgumentException("ERROR: ID is required to update a record.");
        }
        if (registerNumber == null || registerNumber.isEmpty()) {
            throw new IllegalArgumentException("ERROR: Please enter the new register number to update.");
        }

        LineItemLotsRegister registerDetails = lineItemLotsRegisterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(REGISTER_ENTRY_NOT_FOUND_PREFIX + id + NOT_FOUND_SUFFIX));

        registerDetails.setRegisterNo(registerNumber);

        try {
            lineItemLotsRegisterRepository.save(registerDetails);
        } catch (DataIntegrityViolationException e) { 
            log.error("Data integrity violation: Register number already exists", e);
            throw new IllegalArgumentException("ERROR: Register number '" + registerNumber + "' already exists.");
        } catch (ConstraintViolationException e) {
            log.error("Data access error:", e);
            throw new IllegalArgumentException("ERROR: Register number '" + registerNumber + "' already exists. Please use another.");
        }


        return "Register number updated successfully";
    }
    
    /**
     * Updates one or more registers.
     * @param requestDto DTO containing the IDs and ol_line_item_id / status.
     * @return A success message after updating the registers.
     */
    public String update(LineItemLotsRegisterDto requestDto) {
        if (requestDto.getIds() == null || requestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("ERROR: IDs are required to update a record.");
        }

        List<LineItemLotsRegister> registerDetailsList = new ArrayList<>();

        for (Integer id : requestDto.getIds()) {
            LineItemLotsRegister registerDetails = lineItemLotsRegisterRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(REGISTER_ENTRY_NOT_FOUND_PREFIX + id + NOT_FOUND_SUFFIX));

            if (requestDto.getStatus() != null) {
                registerDetails.setStatus(requestDto.getStatus().toLowerCase());
            }
            
            if (requestDto.getOlLineItemId() == null) {
                registerDetails.setOlLineItemId(null); 
            } else {
                OlLineItem olLineItem = new OlLineItem();
                olLineItem.setId(requestDto.getOlLineItemId());
                registerDetails.setOlLineItemId(olLineItem);
            }
            registerDetailsList.add(registerDetails);
        }

        lineItemLotsRegisterRepository.saveAll(registerDetailsList);

        return "Register(s) updated successfully";
    }



    /**
     * Updates one or more registers.
     * @param requestDto DTO containing the IDs and ol_line_item_id / status.
     * @return A success message after updating the registers.
     */
    public String updateStatus(LineItemLotsRegisterDto requestDto) {
        if (requestDto.getIds() == null || requestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("ERROR: IDs are required to update a record.");
        }

        List<LineItemLotsRegister> registerDetailsList = new ArrayList<>();

        for (Integer id : requestDto.getIds()) {
            LineItemLotsRegister registerDetails = lineItemLotsRegisterRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(REGISTER_ENTRY_NOT_FOUND_PREFIX + id + NOT_FOUND_SUFFIX));

            if (requestDto.getStatus() != null) {
                registerDetails.setStatus(requestDto.getStatus().toLowerCase());
            }


            registerDetailsList.add(registerDetails);
        }

        lineItemLotsRegisterRepository.saveAll(registerDetailsList);

        return "Register(s) updated successfully";
    }
    
    /**
     * Retrieves register numbers by line item lot ID.
     * @param lineItemLotId The ID of the line item lot.
     * @return A list of registers associated with the line item lot.
     */
    public List<LineItemLotsRegister> getRegisterNosByLineItemLotId(Integer lineItemLotId) {
        if (lineItemLotId == null) {
            throw new IllegalArgumentException("ERROR: No line-Item-Lot ID provided.");
        }
        
        List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository.getRegisterNosByLineItemLotId(lineItemLotId);
        
        if(registerNumbers.isEmpty() || registerNumbers == null) {
        	log.error("No registers numbers found for the given line-item-lot id: " + lineItemLotId);
        	return List.of(); // returns an empty list
        }
        
		return registerNumbers;
                
    }
    
    /**
     * Retrieves register numbers by line item lot ID.
     * @param lineItemLotId The ID of the line item lot.
     * @return A list of registers associated with the line item lot.
     */
	public List<LineItemLotsRegisterDto> getRegisterNosByOlLineItemId(Integer olLineItemId) {
		if (olLineItemId == null) {
			throw new IllegalArgumentException("ERROR: No ol-line-Item ID provided.");
		}

		List<LineItemLotsRegisterDto> lineItemLotsRegisterDtos = new ArrayList<>();

		List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository
				.getRegisterNosByOlLineItemId(olLineItemId);
		for (LineItemLotsRegister lineItemLotsRegister : registerNumbers) {

			LineItemLotsRegisterDto dto = new LineItemLotsRegisterDto();
			dto.setId(lineItemLotsRegister.getId());
			dto.setRegisterNo(lineItemLotsRegister.getLineItemSerialNo().getSerialNo());
			dto.setStatus(lineItemLotsRegister.getStatus());
			dto.setOlLineItemId(lineItemLotsRegister.getOlLineItemId().getId());
			dto.setIsRunning(lineItemLotsRegister.getIsRunning());
			dto.setIsSample(lineItemLotsRegister.getIsSample());
			if (lineItemLotsRegister.getQaStatus() != null) {
				dto.setQaStatus(lineItemLotsRegister.getQaStatus().getStatus());
				dto.setQaStatusId(lineItemLotsRegister.getQaStatus().getId());
			} else {
				dto.setQaStatus(null);
				dto.setQaStatusId(null);
			}
			lineItemLotsRegisterDtos.add(dto);
		}

		if (registerNumbers.isEmpty() || registerNumbers == null) {
			log.error("No registers numbers found for the given ol-line-item id: " + olLineItemId);
			return List.of(); // returns an empty list
		}

		return lineItemLotsRegisterDtos;

	}

    /**
     * Retrieves register numbers by line item lot ID.
     * @param olId The ID of the line item lot.
     * @return A list of registers associated with the line item lot.
     */
    public List<LineItemLotsRegister> getRegisterNosByOlId(Integer olId) {
        if (olId == null) {
            throw new IllegalArgumentException("ERROR: No ol-line-Item ID provided.");
        }

        List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository.getRegisterNosByOlId(olId);

        if(registerNumbers.isEmpty() || registerNumbers == null) {
            log.error("No registers numbers found for the given ol id: " + olId);
            return List.of(); // returns an empty list
        }

        return registerNumbers;

    }

    public List<LineItemLotsRegister> getFAIRegisterNosByOlLineItemId(Integer olLineItemId) {
        if (olLineItemId == null) {
            throw new IllegalArgumentException("ERROR: No ol-line-Item ID provided.");
        }

        List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository.getFAIRegisterNosByOlLineItemId(olLineItemId);

        if(registerNumbers.isEmpty() || registerNumbers == null) {
            log.error("No fai registers numbers found for the given olLineItemId: " + olLineItemId);
            return List.of(); // returns an empty list
        }

        return registerNumbers;

    }
    
    /**
     * Retrieves register numbers by SO-Line Item ID and financial year.
     * @param soLiId The ID of the SO-Line Item.
     * @param financialYear 
     * @return A list of registers associated with the SO-Line Item and financial year.
     */
    public List<LineItemLotsRegister> getRegisterNosBySoLiIdAndFinancialYear(Integer soLiId, String financialYear) {
        if (soLiId == null) {
            throw new IllegalArgumentException("ERROR: No so-line-Item ID provided.");
        }
        
        List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository.getRegisterNosBySoLiIdAndFinancialYear(soLiId, financialYear);
        
        if(registerNumbers.isEmpty() || registerNumbers == null) {
        	log.error("No registers numbers found for the given so-line-item id: " + soLiId + " and financial year: " + financialYear);
        	return List.of(); // returns an empty list
        }
        
		return registerNumbers;
                
    }
    
    /**
     * Retrieves register numbers by SO-Line Item ID.
     * @param soLiId The ID of the SO-Line Item.
     * @return A list of registers associated with the SO-Line Item.
     */
    public List<LineItemLotsRegister> getRegisterNumbersBySoLiId(Integer soLiId) {
        if (soLiId == null) {
            throw new IllegalArgumentException("ERROR: No so-line-Item ID provided.");
        }
        
        List<LineItemLotsRegister> registerNumbers = lineItemLotsRegisterRepository.getRegisterNosBySoLiId(soLiId);
        
        if(registerNumbers.isEmpty() || registerNumbers == null) {
        	log.error("No registers numbers found for the given soLiId: " + soLiId);
        	return List.of(); // returns an empty list
        }
        
		return registerNumbers;
                
    }
    
    /**
     * Deletes a register number by its ID.
     * @param id The ID of the register to delete.
     */
    @Transactional
	@Modifying
    public void deleteRegisterNumber(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("No register IDs found in the request");
        }

        lineItemLotsRegisterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(INVALID_REGISTER_ID_PREFIX + id));

        String serialNo1=getRegisterNo(id);
        lineItemSerialNoRepository.deleteBySerialNo(serialNo1);

        lineItemLotsRegisterRepository.deleteById(id);
    }
    private String getRegisterNo(int id) {
    	return  lineItemLotsRegisterRepository.findRegisterNo(id);
	}
    /**
     * Deletes register numbers in bulk.
     * @param registerIds A list of register IDs to delete.
     */
    @Transactional
	@Modifying
    public void deleteRegisterNumbersInBulk( List<Integer> registerIds) {
        if (registerIds == null || registerIds.isEmpty()) {
            throw new IllegalArgumentException("No register IDs found in the request");
        }

        for(Integer registerId : registerIds) {
             lineItemLotsRegisterRepository.findById(registerId).orElseThrow(() -> new IllegalArgumentException(INVALID_REGISTER_ID_PREFIX + registerId));
        }

        lineItemLotsRegisterRepository.deleteAllById(registerIds);
    }

    /**
     * Retrieves financial years for the SO-Line Item ID.
     * @param soLiId The ID of the SO-Line Item.
     * @return A list of financial years associated with the SO-Line Item.
     */
    public List<String> getFinancialYearsForSoLiId(Integer soLiId) {
        if (soLiId == null) {
            throw new IllegalArgumentException("ERROR: No so-line-Item ID provided.");
        }
        
        List<String> financialYears = lineItemLotsRegisterRepository.getFinancialYearsForSoLiId(soLiId);
        
        if(financialYears.isEmpty() || financialYears == null) {
        	log.error("No financial years found for the given so-line-item id: " + soLiId);
        	return List.of(); // returns an empty list
        }
        
		return financialYears;
                
    }
    
    /**
     * Retrieves register numbers by SO-Line Item ID and financial year and groups them year wise.
     * @param soLiId The ID of the SO-Line Item.
     * @param financialYear 
     * @return A list of registers associated with the SO-Line Item and financial year.
     */
    public Map<String, List<LineItemLotsRegister>> getRegistersBySoLiIdAndFinancialYear(Integer soLiId, String financialYear, String status) {
        if (soLiId == null) {
            throw new IllegalArgumentException("ERROR: No so-line-Item ID provided.");
        }

        List<Object[]> results = lineItemLotsRegisterRepository.getRegistersBySoLiIdAndFinancialYear(soLiId, financialYear, status);
        Map<String, List<LineItemLotsRegister>> registersByYear = new HashMap<>();

        // result[0] -> Financial Year, result[1] -> id, result[2] -> register_no, result[3] -> line_item_lot_id, result[4] -> status
        for (Object[] result : results) {
            String year = (String) result[0]; 

            LineItemLotsRegister register = new LineItemLotsRegister();
            register.setId((Integer) result[1]); 
            register.setRegisterNo((String) result[2]);
            
            Integer lineItemLotId = (Integer) result[3];  
            LineItemLots lineItemLots = new LineItemLots();
            lineItemLots.setId(lineItemLotId);
            register.setLineItemLotsId(lineItemLots);

            register.setStatus((String) result[4]);
            
            registersByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(register);
        }

        return registersByYear;
    }

    
	public void deleteTest(Integer id) {
		
		 if (id == null) {
	            throw new IllegalArgumentException("No IDs found in the request");
	        }
	        lineItemLotsRegisterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(INVALID_REGISTER_ID_PREFIX + id));
	        lineItemLotsRegisterRepository.setolLineItemIdAsnull(id);	
	}
    
    public List<SampleSelectionDto> getSelectedRegistrationNosForOl(Integer olId) {       
        return lineItemLotsRegisterRepository.getSelectedRegisterNosForOl(olId)
                .stream()
                .map(resultArray -> {
                	SampleSelectionDto dto = new SampleSelectionDto();
                    dto.setRegId((Integer) resultArray[0]);
                    dto.setOlLineItemId((Integer) resultArray[1]);
                    dto.setRegNo((String) resultArray[2]); 
                    dto.setSoLineItemNo((String) resultArray[3]);
                    return dto;
                })
                .toList();
    }
    
    public List<SampleSelectionDto> getAllRegistrationNosForOl(Integer olId) {       
        return lineItemLotsRegisterRepository.getAllRegisterNosForOl(olId)
                .stream()
                .map(resultArray -> {
                	SampleSelectionDto dto = new SampleSelectionDto();
                    dto.setRegId((Integer) resultArray[0]);
                    dto.setOlLineItemId((Integer) resultArray[1]);
                    dto.setRegNo((String) resultArray[2]); 
                    dto.setSoLineItemNo((String) resultArray[3]);
                    return dto;
                })
                .toList();
    }
     
	@Transactional
	public List<LineItemLotsRegister> saveSampleSelection(LineItemLotsRegisterDto registerDto) {
		List<LineItemLotsRegister> savedEntities = new ArrayList<>();
	    if (registerDto.getIds() == null || registerDto.getIds().isEmpty()) {
	        return savedEntities; 
	    }	    
	    List<LineItemLotsRegister> lotsRegister = lineItemLotsRegisterRepository.findAllById(registerDto.getIds()); 
	    InternalINote iNote = new InternalINote();
	    for (LineItemLotsRegister registers : lotsRegister) {
	    	registers.setIsSample(true);
	        if (registerDto.getSampleQty() != null) {
	        	iNote.setSampleQty(registerDto.getSampleQty());
	        }
	    }
	    savedEntities = lineItemLotsRegisterRepository.saveAll(lotsRegister);
	    return savedEntities;
	}

    @Transactional
	public void deleteSampleSelection(Integer regId) {
		Optional<LineItemLotsRegister> optionalRegister = lineItemLotsRegisterRepository.findById(regId);
		LineItemLotsRegister existingRegister = optionalRegister.orElseThrow(() -> {
			return new IllegalArgumentException("Line Item Lots Register with ID " + regId + " not found.");
		});
		existingRegister.setIsSample(false);
		lineItemLotsRegisterRepository.save(existingRegister);
	}
    
    @Transactional
    public void deleteMultipleSampleSelections(List<LineItemLotsRegisterDto> lineDtos) {
        if (lineDtos == null || lineDtos.isEmpty()) {           
            return; 
        }       
        List<Integer> ids = lineDtos.stream()
                .map(LineItemLotsRegisterDto::getId)
                .toList();

        List<LineItemLotsRegister> lotsRegister = lineItemLotsRegisterRepository.findAllById(ids);     

        for (LineItemLotsRegister register : lotsRegister) {
            register.setIsSample(false);
        }
        lineItemLotsRegisterRepository.saveAll(lotsRegister);
    }
    
    public List<SampleSelectionDto> getAllAcceptedRegistrationNosForOl(Integer olId) {       
        return lineItemLotsRegisterRepository.getAllAcceptedRegisterNosForOl(olId)
                .stream()
                .map(resultArray -> {
                	SampleSelectionDto dto = new SampleSelectionDto();
                    dto.setRegId((Integer) resultArray[0]);
                    dto.setOlLineItemId((Integer) resultArray[1]);
                    dto.setRegNo((String) resultArray[2]); 
                    dto.setSoLineItemNo((String) resultArray[3]);
                    return dto;
                })
                .toList();
    }

	public List<SampleSelectionDto> getPartNoRegNoByOlId(Integer olId) {
		return lineItemLotsRegisterRepository.getProductAndRegNosByOlId(olId).stream().map(resultArray -> {
			SampleSelectionDto dto = new SampleSelectionDto();
			dto.setRegId((Integer) resultArray[0]);
			dto.setOlLineItemId((Integer) resultArray[1]);
			dto.setRegNo((String) resultArray[2]);
			dto.setSoLineItemNo((String) resultArray[3]);
			dto.setProductId((Integer) resultArray[5]);
			dto.setStatus((String) resultArray[6]);
			if(resultArray[7]!= null ) {
				dto.setQaStatusId((Integer) resultArray[7]);
			    dto.setQaStatus((String) resultArray[8]);
			}
			else {
				dto.setQaStatusId(null);
			    dto.setQaStatus(null);
			}
			return dto;
		}).toList();
	}
	public List<SampleSelectionDto> getNonSampleProductAndRegNosByOlId(Integer olId) {
		return lineItemLotsRegisterRepository.getNonSampleProductAndRegNosByOlId(olId).stream().map(resultArray -> {
			SampleSelectionDto dto = new SampleSelectionDto();
			dto.setRegId((Integer) resultArray[0]);
			dto.setOlLineItemId((Integer) resultArray[1]);
			dto.setRegNo((String) resultArray[2]);
			dto.setSoLineItemNo((String) resultArray[3]);
			dto.setProductId((Integer) resultArray[5]);
			dto.setStatus((String) resultArray[6]);
			if(resultArray[7]!= null ) {
				dto.setQaStatusId((Integer) resultArray[7]);
			    dto.setQaStatus((String) resultArray[8]);
			}
			else {
				dto.setQaStatusId(null);
			    dto.setQaStatus(null);
			}
			return dto;
		}).toList();
	}
	public List<SampleSelectionDto> getFaiSampleProductAndRegNosByOlId(Integer olId) {
		return lineItemLotsRegisterRepository.getFaiSampleProductAndRegNosByOlId(olId).stream().map(resultArray -> {
			SampleSelectionDto dto = new SampleSelectionDto();
			dto.setRegId((Integer) resultArray[0]);
			dto.setOlLineItemId((Integer) resultArray[1]);
			dto.setRegNo((String) resultArray[2]);
			dto.setSoLineItemNo((String) resultArray[3]);
			dto.setProductId((Integer) resultArray[5]);
			dto.setStatus((String) resultArray[6]);
			if(resultArray[7]!= null ) {
				dto.setQaStatusId((Integer) resultArray[7]);
			    dto.setQaStatus((String) resultArray[8]);
			}
			else {
				dto.setQaStatusId(null);
			    dto.setQaStatus(null);
			}
			
			return dto;
		}).toList();
	}
	
    /**
     * Retrieves register numbers by SO-Line Item ID.
     * @param soLiId The ID of the SO-Line Item.
     * @return A list of registers associated with the SO-Line Item.
     */
    public List<LineItemSerialNoDto> getLineitemSerialNosBySoLiId(Integer soLiId) {
        if (soLiId == null) {
            throw new IllegalArgumentException("ERROR: No so-line-Item ID provided.");
        }
        List<LineItemSerialNo> registerNumbers = lineItemSerialNoRepository.getLineitemSerialNosBySoLiId(soLiId);        
        if(registerNumbers.isEmpty() || registerNumbers == null) {
        	log.error("No registers numbers found for the given soLineItemId: " + soLiId);
        	return List.of(); // returns an empty list
        }
        return registerNumbers.stream().map(regNo -> mapper.map(regNo,LineItemSerialNoDto.class)).toList();     
    }

    @Transactional
	public String deleteSerialNo(Integer id) {
		
		LineItemSerialNo serialNo = lineItemSerialNoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Serial No not found for given id"));
		if(serialNo.getStatus().getId()== 1) {
			//Modified By: Chirag
			//Date: 09-10-26
			//Delete line_item_sl_no_history records first
			lineItemSerialNoHistoryRepository.deleteByLineItemSerialNoId(id);
			
			//then delete line_item_sl_no
			lineItemSerialNoRepository.delete(serialNo);
			return "Serial No. deleted succesfully";
		}
		else {
			log.error("serial no. cannot be deleted after mapping " + id);
			return "serial no. cannot be deleted after mapping ";
		}
			
	}
}
