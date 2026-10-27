package in.co.bel.olms.transaction.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.AolAodDto;
import in.co.bel.olms.transaction.entity.AolAod;
import in.co.bel.olms.transaction.repository.AolAodRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AolAodService {

	private final AolAodRepository aolAodRepository;

	private final Mapper mapper;
	
	private static final Logger logger =LoggerFactory.getLogger(AolAodService.class);
	
	public boolean saveData(AolAodDto aolAodDto) {
		try {
			AolAod aolAod=mapper.map(aolAodDto,AolAod.class);
			aolAod.setCreatedDatetime(new Date());
			aolAodRepository.save(aolAod);
			return true;
		}
		catch(Exception e) {
			logger.error("Error in saving Data: {}",e.getMessage());
			return false;
		}
	}
	
	public List<AolAodDto> getAllByOrgId(int orgId ) {		
		List<AolAodDto> aolAodDtoList=new ArrayList<>();
		List<AolAod> aolAods=aolAodRepository.getByOrgId(orgId);
	
		for (AolAod aolAod : aolAods) {
		    AolAodDto aolAodDto = mapper.map(aolAod, AolAodDto.class);
	
		    // Convert today's date to LocalDate
		    LocalDate today = LocalDate.now();
	
		    // Convert fromDate and toDate to LocalDate
		    LocalDate fromDate = aolAod.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    LocalDate toDate = aolAod.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	
		    aolAodDto.setAsnEmpName(aolAod.getAsnEmp().getEname());
		    String emprole = aolAod.getAsnDesg() + "(" + aolAod.getType() + " )";
		    aolAodDto.setAsnEmpRole(emprole);
		    if ((today.isEqual(fromDate) || today.isAfter(fromDate)) && (today.isEqual(toDate) || today.isBefore(toDate))) {
		        aolAodDto.setActive(true);
		    } else {
		        aolAodDto.setActive(false);
		    }
		    	aolAodDtoList.add(aolAodDto);
			}
		return aolAodDtoList;
	}
}
