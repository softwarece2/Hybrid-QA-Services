package in.co.bel.olms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import in.co.bel.olms.rpt.dto.WorkLoadDto;
import in.co.bel.olms.transaction.repository.WorkLoadOlRepository;
import in.co.bel.olms.transaction.repository.WorkLoadRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkloadService {

	private final WorkLoadOlRepository workLoadOlRepository;

	private final WorkLoadRepository workLoadRepository;
	
	public List<WorkLoadDto> getWorkloadReportsbyDates(Date fromDate, Date toDate){
		List<WorkLoadDto> dtos = new ArrayList<>();
		
		
		Map<Integer, WorkLoadDto> retVal = new HashMap<>(); 
		List<Object[]> dbResp = workLoadRepository.getwrkLoadDataByDate(fromDate, toDate);
		for(Object[] obj : dbResp) {
			WorkLoadDto dto = new WorkLoadDto();
			dto.setSoOrderNo(obj[1] != null ? obj[1].toString() : null);
			dto.setEstMandays(obj[2] != null ? (int) Float.parseFloat(obj[2].toString()) : 0);
			dto.setActualMandays(obj[3] != null ? (int) Float.parseFloat(obj[3].toString()) : 0);
		//	dtos.add(dto);
			
			retVal.put((int) Float.parseFloat(obj[0].toString()), dto);
		}
		
		
		List<Object[]> dbRespol = workLoadOlRepository.getwrkLoadDataByDate(fromDate, toDate);
		for(Object[] obj : dbRespol) {			
			
			WorkLoadDto dto = retVal.getOrDefault((int) Float.parseFloat(obj[0].toString()), new WorkLoadDto() );
			dto.setSoOrderNo(obj[1] != null ? obj[1].toString() : null);
			dto.setEstMandays( dto.getEstMandays() + (          obj[2] != null ? (int) Float.parseFloat(obj[2].toString()) : 0));
			dto.setActualMandays( dto.getActualMandays() + (obj[3] != null ? (int) Float.parseFloat(obj[3].toString()) : 0));
			dtos.add(dto);
			
			retVal.put( (int) Float.parseFloat(obj[0].toString()), dto);
		}
		

		ArrayList<WorkLoadDto> valueList = new ArrayList<WorkLoadDto>(retVal.values());
		return valueList;
	}
	
	
	
}
