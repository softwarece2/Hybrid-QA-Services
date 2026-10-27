package in.co.bel.olms.dgqa.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.dgqa.dto.OlDetDto;
import in.co.bel.olms.transaction.repository.OlDetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
@RequiredArgsConstructor
public class OlDetService {

	private final OlDetRepository olDetRepository;
	
	 public List<OlDetDto> getOlDets(int month, int year) {
	        List<OlDetDto> olDetDtos = new ArrayList<>();
	        try {
	        	List<Object[]> olDetails = olDetRepository.getOlDet(month, year);
	        	
	        	if (olDetails != null && !olDetails.isEmpty()) {
	                for (Object[] olDet : olDetails) {
	                	OlDetDto olDetDto = new OlDetDto();
	                	olDetDto.setCount((BigInteger) olDet[0]);
	                	olDetDto.setId((Integer) olDet[1]);
	                	olDetDto.setOrgName((String) olDet[2]);
	                	olDetDtos.add(olDetDto);
	                }
	        	}
	        } catch(Exception e) {
	        	log.error("Error occurred while fetching Ol details for month: {} and year: {}", month, year, e);
	        }
	        return olDetDtos;
      }

}
