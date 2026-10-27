package in.co.bel.olms.iol.reports.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.iol.reports.dto.ReportFileDto;
import in.co.bel.olms.iol.reports.dto.ReportRequest;
import in.co.bel.olms.iol.reports.dto.ReportsDto;
import in.co.bel.olms.iol.reports.excel.TpdkColdTestToExcelConverter;
import in.co.bel.olms.iol.reports.excel.TvneColdResistanceToExcelConverter;
import in.co.bel.olms.iol.reports.excel.TvneHeatStabilityToExcelConverter;
import in.co.bel.olms.iol.reports.excel.TvneImpactToExcelConverter;
import in.co.bel.olms.iol.reports.excel.TvneLeakTestToExcelConverter;
import in.co.bel.olms.iol.reports.excel.TvneVibrationToExcelConverter;
import in.co.bel.olms.iol.reports.file.ReportFile;
import in.co.bel.olms.iol.reports.query.builder.ReportQueryBuilder;
import in.co.bel.olms.iol.reports.service.TvneReportsService;
import in.co.bel.olms.iol.reports.util.HotColdEnum;
import in.co.bel.olms.iol.reports.util.ProductTypeEnum;
import in.co.bel.olms.iol.reports.util.TestTypeEnum;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ReportController {

	private final ReportQueryBuilder reportQueryBuilder;

	private final OrganizationRepository organizationRepository;

	private final EmployeeRepository employeeRepository;

	private final TvneColdResistanceToExcelConverter tvneColdResistanceToExcelConverter;

	private final TvneHeatStabilityToExcelConverter tvneHeatStabilityToExcelConverter;

	private final TpdkColdTestToExcelConverter tpdkColdTestToExcelConverter;

    private final TvneImpactToExcelConverter tvneImpactToExcelConverter;

	private final TvneVibrationToExcelConverter tvneVibrationToExcelConverter;

	private final TvneLeakTestToExcelConverter tvneLeakTestToExcelConverter;

	private final TvneReportsService tvneReportsService;

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	private final HmisAppSecurity hmisAppSecurity = new HmisAppSecurity();
	
	Gson gson = new Gson();
	
	private String outputFileName = ReportFile.REPORT;

	@PostMapping("/getReport")
	public ResponseEntity<String> getReport(@RequestBody ReportRequest reportRequest)
			throws IOException, ParseException {
		String queryString = reportQueryBuilder.populateQuery(reportRequest);
		logger.info("Query to generate report : {} ", queryString);
		String retValue = "";
		int productId = reportRequest.getProductTypeId();
		if (productId == ProductTypeEnum.TPDK_1.type) {
			retValue = getReportByTpdk(reportRequest, queryString);
		} else if (productId == ProductTypeEnum.TVNE_4B_M1.type) {
			retValue = getReportByTvne(reportRequest, queryString);
		} else if (productId == ProductTypeEnum.TVNE_4B_M2.type) {
			retValue = getReportByTvne(reportRequest, queryString);
		} else if (productId == ProductTypeEnum.TVNE_1PA.type) {
			retValue = getReportByTvne(reportRequest, queryString);
		}
		
		return ResponseEntity.ok(retValue);
		
	}

	private String getReportByTpdk(ReportRequest reportRequest, String queryString) {
		String retValue = "";
		int testId = reportRequest.getTestTypeId();
		if (testId == HotColdEnum.HOT_TEST.type) {
			List<ReportsDto> getData = tvneReportsService.executeColdTestTpdkReportQuery(queryString);
			retValue = gson.toJson(getData);
		} else if (testId == HotColdEnum.COLD_TEST.type) {
			List<ReportsDto> getData = tvneReportsService.executeColdTestTpdkReportQuery(queryString);
			retValue = gson.toJson(getData);
		}
		return retValue;
	}

	private String getReportByTvne(ReportRequest reportRequest, String queryString) {
		String retValue = "";
		int testId = reportRequest.getTestTypeId();
		if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			List<ReportsDto> getData = tvneReportsService.executeColdResistanceReportQuery(queryString);
			retValue = gson.toJson(getData);
		} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
			List<ReportsDto> getData = tvneReportsService.executeHeatStabilityReportQuery(queryString);
			retValue = gson.toJson(getData);
		} else if (testId == TestTypeEnum.IMPACT.type) {
			List<ReportsDto> getData = tvneReportsService.executeImpactReportQuery(queryString);
			retValue = gson.toJson(getData);
		} else if (testId == TestTypeEnum.VIBRATION.type) {
			List<ReportsDto> getData = tvneReportsService.executeVibrationReportQuery(queryString);
			retValue = gson.toJson(getData);
		} else if (testId == TestTypeEnum.LEAK_TEST.type) {
			List<ReportsDto> getData = tvneReportsService.executeLeakTestReportQuery(queryString);
			retValue = gson.toJson(getData);
		}
		return retValue;
	}
	
	@PostMapping("/getReportFile")
	public ResponseEntity<byte[]> getReportFile(@RequestBody ReportRequest reportRequest,
			@RequestHeader(name = "Authorization") String token) throws IOException, ParseException {
		String queryString = reportQueryBuilder.populateQuery(reportRequest);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int orgId = hmisAppSecurity.getOrgIdFromJwtToken(token);
		int roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);
		Organization orgObj = organizationRepository.findOrgById(orgId);
		Employee roleObj = employeeRepository.getEmpByRoleId(roleId);
		if (reportRequest.getProductTypeId() == ProductTypeEnum.TPDK_1.type) {
			ReportFileDto recievedVal = getReportFileByTpdk(reportRequest, queryString, orgObj, roleObj);
			outputStream = recievedVal.getOutputStream();
			outputFileName = recievedVal.getOutputFileName();
		} else if (reportRequest.getProductTypeId() == ProductTypeEnum.TVNE_4B_M1.type) {
			ReportFileDto recievedVal = getReportFileByTvne(reportRequest, queryString, orgObj, roleObj);
			outputStream = recievedVal.getOutputStream();
			outputFileName = recievedVal.getOutputFileName();
		} else if (reportRequest.getProductTypeId() == ProductTypeEnum.TVNE_4B_M2.type) {
			ReportFileDto recievedVal = getReportFileByTvne(reportRequest, queryString, orgObj, roleObj);
			outputStream = recievedVal.getOutputStream();
			outputFileName = recievedVal.getOutputFileName();
		} else if (reportRequest.getProductTypeId() == ProductTypeEnum.TVNE_1PA.type) {
			ReportFileDto recievedVal = getReportFileByTvne(reportRequest, queryString, orgObj, roleObj);
			outputStream = recievedVal.getOutputStream();
			outputFileName = recievedVal.getOutputFileName();
		}
		try {
			HttpHeaders headersResponse = new HttpHeaders();
			headersResponse.add("Content-Disposition", "attachment; filename= " + outputFileName);
			return ResponseEntity.status(HttpStatus.OK).headers(headersResponse).body(outputStream.toByteArray());
		} catch (Exception e) {
			logger.error("Error in creating report {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
 
	@SuppressWarnings("resource")
	private ReportFileDto getReportFileByTpdk(ReportRequest reportRequest, String queryString, Organization orgObj,
			Employee roleObj) throws IOException, ParseException {
		ReportFileDto retVal = null;
		ReportFileDto rfDto = new ReportFileDto();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		long testId = reportRequest.getTestTypeId();
		if (testId == HotColdEnum.HOT_TEST.type) {
			outputStream = tpdkColdTestToExcelConverter.populateExcelForTvneColdTest(tvneReportsService.executeColdTestTpdkReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.HOT_TEST_TPDK_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		} else if (testId == HotColdEnum.COLD_TEST.type) {
			outputStream = tpdkColdTestToExcelConverter.populateExcelForTvneColdTest(tvneReportsService.executeColdTestTpdkReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.COLD_TEST_TPDK_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		}
		return retVal;
	}

	@SuppressWarnings("resource")
	private ReportFileDto getReportFileByTvne(ReportRequest reportRequest, String queryString, Organization orgObj, Employee roleObj) throws IOException, ParseException {
		ReportFileDto retVal = null;
		ReportFileDto rfDto = new ReportFileDto();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		long testId = reportRequest.getTestTypeId();
		if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			outputStream = tvneColdResistanceToExcelConverter.populateExcelForTvneColdResistance(tvneReportsService.executeColdResistanceReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.COLD_RESISTANCE_TVNE_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
			outputStream = tvneHeatStabilityToExcelConverter.populateExcelForTvneHeatStability(tvneReportsService.executeHeatStabilityReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.HEAT_STABILITY_TVNE_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		} else if (testId == TestTypeEnum.IMPACT.type) {
			outputStream = tvneImpactToExcelConverter.populateExcelForTvneImpact(tvneReportsService.executeImpactReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.IMPACT_TVNE_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		} else if (testId == TestTypeEnum.VIBRATION.type) {
			outputStream = tvneVibrationToExcelConverter.populateExcelForTvneVibration(tvneReportsService.executeVibrationReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.VIBRATION_TVNE_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		}  else if (testId == TestTypeEnum.LEAK_TEST.type) {
			outputStream = tvneLeakTestToExcelConverter.populateExcelForTvneLeakTest(tvneReportsService.executeLeakTestReportQuery(queryString), reportRequest, orgObj, roleObj);
			outputFileName = ReportFile.LEAK_TEST_TVNE_REPORT;
			rfDto.setOutputStream(outputStream);
			rfDto.setOutputFileName(outputFileName);
			retVal = rfDto;
		}
		return retVal;
	}
}
