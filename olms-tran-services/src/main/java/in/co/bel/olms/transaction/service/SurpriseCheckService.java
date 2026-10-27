package in.co.bel.olms.transaction.service;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.entity.SurpriseCheck;
import in.co.bel.olms.transaction.repository.SurpriseCheckRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for registers
 *
 * @author Awadhesh Verma
 * @since 26-08-2023
 */

@Service
@RequiredArgsConstructor
public class SurpriseCheckService {

    private final SurpriseCheckRepository surpriseCheckRepository;

    private final Mapper mapper;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LoggerFactory.getLogger(SurpriseCheckService.class);

    public Map<Boolean, String> saveSurpriseCheck(SurpriseCheckDto surpriseCheckDto) {

        Map<Boolean, String> returnMap = new HashMap<>();
        
        String testCarried = surpriseCheckDto.getTestCarried();
        String crossVerifyBy = surpriseCheckDto.getCrossVerifyBy();
        String remarks = surpriseCheckDto.getRemarks();
        String individualResponsible = surpriseCheckDto.getIndividualResponsible();
        
        try {
        	
        	if(StringUtils.isNotBlank(testCarried)) {
        		if(!InputNameValidation.checkString(testCarried)) {
        			throw new DataNotValidException("Invalid test carried");
        		}
        	}
        	
        	if(StringUtils.isNotBlank(crossVerifyBy)) {
        		if(!InputNameValidation.checkString(crossVerifyBy)) {
        			throw new DataNotValidException("Invalid cross verified by");
        		}
        	}
        	
        	if(StringUtils.isNotBlank(remarks)) {
        		if(!InputNameValidation.checkRemarks(remarks)) {
        			throw new DataNotValidException("Invalid remarks");
        		}
        	}
        	
        	if(StringUtils.isNotBlank(individualResponsible)) {
        		if(!InputNameValidation.checkString(individualResponsible)) {
        			throw new DataNotValidException("Invalid individual responsible");
        		}
        	}
        	
        	
            SurpriseCheck surpriseCheck = mapper.map(surpriseCheckDto, SurpriseCheck.class);
            Date crossVerifyDate = format.parse(surpriseCheckDto.getCrossVerifyDate());
            surpriseCheck.setCrossVerifyDate(crossVerifyDate);
            Date inspectionDate = format.parse(surpriseCheckDto.getInspectionDate());
            surpriseCheck.setInspectionDate(inspectionDate);
            surpriseCheck.setCreatedDate(new Date());
            surpriseCheckRepository.save(surpriseCheck);
            returnMap.put(true, "Data saved successfully.");
            return returnMap;
            
        } catch(DataNotValidException de) {
        	logger.error("Validation failed: {}", de.getMessage());
        	throw de;
        } catch (Exception e) {
            logger.error("Error in saving surprise check details {}", e.getMessage());
            returnMap.put(false, "Some error occurred. Plz try again.");
            return returnMap;
        }

    }
}
