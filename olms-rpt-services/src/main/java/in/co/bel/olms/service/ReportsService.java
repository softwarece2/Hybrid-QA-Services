package in.co.bel.olms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.rpt.dto.ReportsDto;
import in.co.bel.olms.transaction.repository.ReportsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportsService {

	private final ReportsRepository  reportsRepository;
	
	public List<ReportsDto> getReportsDataByDates(Date fromDate, Date toDate, int empRoleId){
		List<ReportsDto> dtos = new ArrayList<>();
		
		List<Object[]> dbResp = reportsRepository.getReportsDataByDate(fromDate, toDate,empRoleId);
		for(Object[] obj : dbResp) {
			ReportsDto dto = new ReportsDto();
			dto.setSoOrderNo(obj[1] != null ? obj[1].toString() : null);
			dto.setInspectorName(obj[2] != null ? obj[2].toString() : null);
			dto.setConsigneeName(obj[3] != null ? obj[3].toString() : null);
			dto.setIndentorName(obj[4] != null ? obj[4].toString() : null);
			dto.setProjectName(obj[5] != null ? obj[5].toString() : null);
			
				
			dtos.add(dto);
			
		}
		
		

	
		return dtos;
	}
	
	
	

}
