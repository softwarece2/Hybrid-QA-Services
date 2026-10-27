package in.co.bel.olms.transaction.service.dashboard;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.dashboard.DashFirmDto;
import in.co.bel.olms.transaction.repository.dashboard.DashFirmRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashFirmService {
	
	private final DashFirmRepository dashFirmRepository;

	private static final Logger logger = LoggerFactory.getLogger(DashFirmService.class);

	public List<DashFirmDto> getAllDashFirms() {
		Map<Integer, DashFirmDto> resultMap = new HashMap<>();

		try {
			List<Object[]> dashFirmDetails = dashFirmRepository.getAllDashFirms();

			if (dashFirmDetails != null && !dashFirmDetails.isEmpty()) {
				for (Object[] dashFormDet : dashFirmDetails) {
					Integer orgId = (Integer) dashFormDet[1];
					if (!resultMap.containsKey(orgId)) {
						DashFirmDto dashFirmDto = new DashFirmDto();
						dashFirmDto.setCountOl((BigInteger) dashFormDet[0]);
						dashFirmDto.setOrgId((Integer) dashFormDet[1]);
						dashFirmDto.setOrgName((String) dashFormDet[2]);
						dashFirmDto.setLogo((byte[]) dashFormDet[3]);
						dashFirmDto.setPending(0);
						resultMap.put(orgId, dashFirmDto);
					}
				}
			}
			List<Object[]> projectDetails = dashFirmRepository.getProjectDetailsValues();
			if (projectDetails != null && !projectDetails.isEmpty()) {
				for (Object[] projectDetail : projectDetails) {
					Integer orgId = (Integer) projectDetail[0];
					String projectName = (String) projectDetail[1];
					Double projectValue = (Double) projectDetail[2];
					if (resultMap.containsKey(orgId)) {
						DashFirmDto dashFirmDto = resultMap.get(orgId);
						dashFirmDto.setProjName(projectName);
						dashFirmDto.setProjectValue(projectValue);
						resultMap.put(orgId, dashFirmDto);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in fetching the dash firms{} ", e.getMessage());
			logger.error("Error occured while fetching the dash firms ", e);
		}
		// Sorting descending order of project values
		List<DashFirmDto> resultList = new ArrayList<>(resultMap.values());
		resultList.sort(Comparator.comparing(DashFirmDto::getProjectValue).reversed());
		return resultList;
	}
}
