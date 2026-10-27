package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.INoteChartValuePair;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.service.so.ProjectService;
import lombok.RequiredArgsConstructor;

/**
 * INoteDashboardService service class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-05-13
 */

@Service
@RequiredArgsConstructor
public class INoteDashboardService {

	private final INoteRepository iNoteRepository;

	private final ProjectService projectService;

	private static final Logger logger = LoggerFactory.getLogger(INoteDashboardService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public List<INoteChartValuePair> getINoteDetailsForChart(int selectedFY, Integer term) {
		
		List<INoteChartValuePair> iNoteChartValuePairs = new LinkedList<>();
		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1); // Financial year starts on April 1st of selectedFY
		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);
		
		List<Object[]> inoteDetails = null;
		if(term == 1) { // Yearly
			inoteDetails = iNoteRepository.getYearWiseINotes(fyStartDate, fyEndDate);
			if (inoteDetails != null && !inoteDetails.isEmpty()) {

				for (Object[] inoteObjArr : inoteDetails) {
					INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
					iNoteChartValuePair.setKey("FY "+selectedFY);
					iNoteChartValuePair.setFormIV(new BigDecimal(inoteObjArr[0].toString()).intValue());
					iNoteChartValuePair.setINote(new BigDecimal(inoteObjArr[1].toString()).intValue());
					iNoteChartValuePairs.add(iNoteChartValuePair);
				}
			}
		}
		else if(term == 2) {// Monthly for selected Financial year
			
			inoteDetails = iNoteRepository.getMonthWiseINotes(fyStartDate, fyEndDate);
			prepareChartObj(inoteDetails, iNoteChartValuePairs);
			List<String> availableMonthsInResult = iNoteChartValuePairs.stream()
					.map(iNoteChartValuePair -> iNoteChartValuePair.getKey()).collect(Collectors.toList());

			List<String> sortedMonthsList = Arrays.asList("APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
					"JAN", "FEB", "MAR");

			for (String monthName : sortedMonthsList) {
				if (!availableMonthsInResult.contains(monthName)) {
					INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
					iNoteChartValuePair.setKey(monthName);
					iNoteChartValuePair.setINote(0);
					iNoteChartValuePair.setFormIV(0);
					iNoteChartValuePairs.add(iNoteChartValuePair);
				}
			}
			sortFYMonthWise(iNoteChartValuePairs, sortedMonthsList);
		}
		else if(term == 3){ // Quarterly for selected Financial year
			
			inoteDetails = iNoteRepository.getQtrWiseINotes(fyStartDate, fyEndDate);
			prepareChartObj(inoteDetails, iNoteChartValuePairs);
			List<String> availableQtrsInResult = iNoteChartValuePairs.stream()
					.map(iNoteChartValuePair -> iNoteChartValuePair.getKey()).collect(Collectors.toList());

			List<String> sortedQtrsList = Arrays.asList("Qtr 1", "Qtr 2", "Qtr 3", "Qtr 4");

			for (String qtrName : sortedQtrsList) {
				if (!availableQtrsInResult.contains(qtrName)) {
					INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
					iNoteChartValuePair.setKey(qtrName);
					iNoteChartValuePair.setINote(0);
					iNoteChartValuePair.setFormIV(0);
					iNoteChartValuePairs.add(iNoteChartValuePair);
				}
			}
			iNoteChartValuePairs.sort((o1, o2) -> o1.getKey().compareTo(o2.getKey()));
			
		}
		
		return iNoteChartValuePairs;
	}
	
	private void prepareChartObj(List<Object[]> resultSet, List<INoteChartValuePair> iNoteChartValuePairs) {

		if (resultSet != null && !resultSet.isEmpty()) {

			for (Object[] inoteObjArr : resultSet) {

				INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
				iNoteChartValuePair.setKey(inoteObjArr[0].toString());
				iNoteChartValuePair.setFormIV(new BigDecimal(inoteObjArr[1].toString()).intValue());
				iNoteChartValuePair.setINote(new BigDecimal(inoteObjArr[2].toString()).intValue());

				iNoteChartValuePairs.add(iNoteChartValuePair);
			}
		}
	}
	
		
	private void sortFYMonthWise(List<INoteChartValuePair> objectsToOrder, List<String> orderedObjects) {

		HashMap<String, Integer> indexMap = new HashMap<>();
		int index = 0;
		for (String month : orderedObjects) {
			indexMap.put(month, index);
			index++;
		}

		Collections.sort(objectsToOrder, (o1, o2) -> {

			Integer leftIndex = indexMap.get(o1.getKey());
			Integer rightIndex = indexMap.get(o2.getKey());
			if (leftIndex == null) {
				return -1;
			}
			if (rightIndex == null) {
				return 1;
			}

			return Integer.compare(leftIndex, rightIndex);
		});
	}

	public List<INoteChartValuePair> getINoteDetailsProjectWise(Integer projectId) {
		
		List<INoteChartValuePair> iNoteChartValuePairs = new LinkedList<>();
		Project project = projectService.getProjectById(projectId);
		List<Object[]> inoteDetails = iNoteRepository.getINoteDetailsProjectWise(projectId);
		if (inoteDetails != null && !inoteDetails.isEmpty()) {

			for (Object[] inoteObjArr : inoteDetails) {

				INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
				iNoteChartValuePair.setKey(project.getProjectName());
				iNoteChartValuePair.setFormIV(new BigDecimal(inoteObjArr[0].toString()).intValue());
				iNoteChartValuePair.setINote(new BigDecimal(inoteObjArr[1].toString()).intValue());

				iNoteChartValuePairs.add(iNoteChartValuePair);
			}
		}
		return iNoteChartValuePairs;
	}

	public List<INoteChartValuePair> getINoteDetailsForDGQAChart(int selectedFY, Integer orgId) {

		List<INoteChartValuePair> iNoteChartValuePairs = new LinkedList<>();
		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1); // Financial year starts on April 1st of selectedFY
		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);

		List<Object[]> inoteDetails = null;
		inoteDetails = iNoteRepository.getINoteDetailsForDGQAChart(orgId, fyStartDate, fyEndDate);
		if (inoteDetails != null && !inoteDetails.isEmpty()) {

			for (Object[] inoteObjArr : inoteDetails) {
				INoteChartValuePair iNoteChartValuePair = new INoteChartValuePair();
				iNoteChartValuePair.setKey("FY " + selectedFY);
				iNoteChartValuePair.setFormIV(new BigDecimal(inoteObjArr[0].toString()).intValue());
				iNoteChartValuePair.setINote(new BigDecimal(inoteObjArr[1].toString()).intValue());
				iNoteChartValuePairs.add(iNoteChartValuePair);
			}
		}
		return iNoteChartValuePairs;
	}

	public List<Map<String, String>> getINoteValueByFY(Integer orgId, Integer selectedFY) {

		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1); // Financial year starts on April 1st of
		// selectedFY
		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);

		List<Map<String, String>> inoteDataList = new LinkedList<>();

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<Object[]> inoteData = iNoteRepository.getINoteValueByFYForDGQA(fyStartDate, fyEndDate);
		for (Object[] obj : inoteData) {
			Map<String, String> dataMap = new LinkedHashMap<>();
			dataMap.put("key", obj[0].toString());
			double value = new BigDecimal(obj[1].toString()).doubleValue();
			dataMap.put("value", df.format(value));
			inoteDataList.add(dataMap);
		}

		return inoteDataList;
	}

}
