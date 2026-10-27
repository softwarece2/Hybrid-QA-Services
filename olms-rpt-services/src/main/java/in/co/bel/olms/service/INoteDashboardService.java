package in.co.bel.olms.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import in.co.bel.olms.dgqa.dto.INoteDashBoardDto;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.util.FYMonthSorter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class INoteDashboardService {

	private final INoteRepository iNoteRepository;

	private final SalesGroupRepository salesGroupRepository;

	public List<Map<String, String>> getINoteValueBySalesGrpId(Integer salesGrpId, Integer selectedFY) {

		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1); // Financial year starts on April 1st of
																	// selectedFY
		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);

		List<Map<String, String>> inoteDataList = new LinkedList<>();

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<String> availableMonthsInResult = new ArrayList<>();
		List<Object[]> inoteData = iNoteRepository.getINoteValueBySalesGrpId(salesGrpId, fyStartDate, fyEndDate);
		for (Object[] obj : inoteData) {
			Map<String, String> dataMap = new LinkedHashMap<>();
			String monthName = obj[0].toString();
			dataMap.put("key", monthName);
			availableMonthsInResult.add(monthName);
			double value = new BigDecimal(obj[1].toString()).doubleValue();
			dataMap.put("value", df.format(value));
			inoteDataList.add(dataMap);
		}

		List<String> sortedMonthsList = Arrays.asList("APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
				"JAN", "FEB", "MAR");

		for (String monthName : sortedMonthsList) {
			if (!availableMonthsInResult.contains(monthName)) {

				Map<String, String> dataMap = new LinkedHashMap<>();
				dataMap.put("key", monthName);
				dataMap.put("value", "0");
				inoteDataList.add(dataMap);
			}
		}
		FYMonthSorter.sortList(inoteDataList, sortedMonthsList);

		return inoteDataList;
	}

	public List<Map<String, String>> getINoteValueByFY(Integer orgId, Integer selectedFY) {

		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1); // Financial year starts on April 1st of
																	// selectedFY
		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);

		List<Map<String, String>> inoteDataList = new LinkedList<>();

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<String> availableSalesGrpInResult = new ArrayList<>();
		List<Object[]> inoteData = iNoteRepository.getINoteValueByFY(orgId, fyStartDate, fyEndDate);
		for (Object[] obj : inoteData) {
			Map<String, String> dataMap = new LinkedHashMap<>();
			dataMap.put("key", obj[0].toString());
			double value = new BigDecimal(obj[1].toString()).doubleValue();
			dataMap.put("value", df.format(value));
			inoteDataList.add(dataMap);
		}

		List<String> salesGrpList = new LinkedList<>();
		List<SalesGroup> salesGroups = salesGroupRepository.findByActive(true);
		for (SalesGroup salesGroup : salesGroups) {
			salesGrpList.add(salesGroup.getName());
		}

		for (String salesGroupName : salesGrpList) {
			if (!availableSalesGrpInResult.contains(salesGroupName)) {

				Map<String, String> dataMap = new LinkedHashMap<>();
				dataMap.put("key", salesGroupName);
				dataMap.put("value", "0");
				inoteDataList.add(dataMap);
			}
		}

		return inoteDataList;
	}
	
	
	public INoteDashBoardDto getINoteDashBoard() {
		List<Object[]> resultList = iNoteRepository.getInoteDashboardDetails();
		Object[] result = resultList.get(0);
		INoteDashBoardDto iNoteDashBoardDto = new INoteDashBoardDto();
		iNoteDashBoardDto.setNoOfInote(((Number) result[0]).intValue());
		iNoteDashBoardDto.setValueOfInote(new BigDecimal(result[1].toString()));
		return iNoteDashBoardDto;

	}

}
