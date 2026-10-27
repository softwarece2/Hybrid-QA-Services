package in.co.bel.olms.dgqa.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.dgqa.dto.SoDashBoardDto;
import in.co.bel.olms.dgqa.dto.SoDetDto;
import in.co.bel.olms.transaction.repository.SoDetRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
@RequiredArgsConstructor
public class SoDashBoardService {

	private final SupplyOrderRepository supplyOrderRepository;

	private final SoDetRepository soDetRepository;

	public SoDashBoardDto getSoDashboard() {
		List<Object[]> resultList = supplyOrderRepository.getDashBoardSoDetails();
		Object[] result = resultList.get(0);
		SoDashBoardDto dashboardDto = new SoDashBoardDto();
		dashboardDto.setNoOfSo(((Number) result[0]).intValue());
		dashboardDto.setValueOfSo(new BigDecimal(result[1].toString()));
		return dashboardDto;
	}
	
	public List<SoDetDto> getSoDets(int month, int year) {
        List<SoDetDto> soDetDtos = new ArrayList<>();
        try {
        	List<Object[]> soDetails = soDetRepository.getSoDet(month, year);
        	
        	if (soDetails != null && !soDetails.isEmpty()) {
                for (Object[] soDet : soDetails) {
                	SoDetDto soDetDto = new SoDetDto();
                	soDetDto.setCount((BigInteger) soDet[0]);
                	soDetDto.setId((Integer) soDet[1]);
                	soDetDto.setOrgName((String) soDet[2]);
                	soDetDtos.add(soDetDto);
                }
        	}
        } catch(Exception e) {
        	log.error("Error occurred while fetching so details for month: {} and year: {}", month, year, e);
        }
        return soDetDtos;
  }

}
