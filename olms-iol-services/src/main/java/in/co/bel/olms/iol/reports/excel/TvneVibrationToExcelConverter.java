package in.co.bel.olms.iol.reports.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.reports.dto.ReportRequest;
import in.co.bel.olms.iol.reports.dto.ReportsDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.Organization;

@Service
public class TvneVibrationToExcelConverter {

	public ByteArrayOutputStream populateExcelForTvneVibration(List<ReportsDto> passes,
			ReportRequest repReq, Organization orgObj, Employee empObj) throws IOException, ParseException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("TvneVibrationReportsheet");
	
		Font customfont = workbook.createFont();
		customfont.setBold(true);
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setWrapText(true);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		CellStyle dateCellStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		dateCellStyle.setDataFormat(dataFormat.getFormat(StringLiteralConstants.IST_DATE_FORMAT));
		dateCellStyle.setWrapText(true);
		dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
		dateCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		dateCellStyle.setBorderTop(BorderStyle.THIN);
		dateCellStyle.setBorderBottom(BorderStyle.THIN);
		dateCellStyle.setBorderLeft(BorderStyle.THIN);
		dateCellStyle.setBorderRight(BorderStyle.THIN);

		CellStyle customCellStyle = workbook.createCellStyle();
		customCellStyle.setDataFormat(dataFormat.getFormat(StringLiteralConstants.IST_DATE_FORMAT));
		customCellStyle.setWrapText(true);
		customCellStyle.setFont(customfont);
		customCellStyle.setAlignment(HorizontalAlignment.CENTER);
		customCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		customCellStyle.setBorderTop(BorderStyle.THIN);
		customCellStyle.setBorderBottom(BorderStyle.THIN);
		customCellStyle.setBorderLeft(BorderStyle.THIN);
		customCellStyle.setBorderRight(BorderStyle.THIN);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		headerStyle.setFont(font);

		SimpleDateFormat originalFormat = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		SimpleDateFormat targetFormat = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);

		Date fromDate = originalFormat.parse(repReq.getFromDate());
		String formattedFromDate = targetFormat.format(fromDate);

		Date toDate = originalFormat.parse(repReq.getToDate());
		String formattedToDate = targetFormat.format(toDate);
		
		// Create headers
		Row headerRowOrgName = sheet.createRow(1);
		Row headerRowDeptName = sheet.createRow(2);
		Row headerDepts = sheet.createRow(3);

		Row headerRowFromDate = sheet.createRow(5);
		Row headerRowToDate = sheet.createRow(6);
		Row headerRowReportGenBy = sheet.createRow(7);
		Row headerRowReportGenOn = sheet.createRow(8);
		Row headerRow1 = sheet.createRow(10);
		Cell cellFromDate = headerRowFromDate.createCell(1);
		cellFromDate.setCellValue("From Date");
		cellFromDate.setCellStyle(customCellStyle);
		
		Cell dateCell1 = headerRowFromDate.createCell(2);
		dateCell1.setCellValue(formattedFromDate);
		dateCell1.setCellStyle(dateCellStyle);

		Cell cellToDate = headerRowToDate.createCell(1);
		cellToDate.setCellValue("To Date");
		cellToDate.setCellStyle(customCellStyle);

		Cell dateCell2 = headerRowToDate.createCell(2);
		dateCell2.setCellValue(formattedToDate);
		dateCell2.setCellStyle(dateCellStyle);

		Cell orgNameCell = headerRowOrgName.createCell(1);
		orgNameCell.setCellValue(orgObj.getOrganizationName());
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
		for (int row = 1; row <= 1; row++) {
			Row currentRow = sheet.getRow(row);
			if (currentRow == null) {
				currentRow = sheet.createRow(row);
			}
			for (int column = 1; column <= 3; column++) {
				Cell currentCell = currentRow.getCell(column);
				if (currentCell == null) {
					currentCell = currentRow.createCell(column);
				}
				currentCell.setCellStyle(customCellStyle);
			}
		}

		Cell deptsNameCell = headerRowDeptName.createCell(1);
		deptsNameCell.setCellValue("Iol-Tvne-Department-Vibration Report");
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));

		for (int row = 2; row <= 2; row++) {
			Row currentRow = sheet.getRow(row);
			if (currentRow == null) {
				currentRow = sheet.createRow(row);
			}
			for (int column = 1; column <= 3; column++) {
				Cell currentCell = currentRow.getCell(column);
				if (currentCell == null) {
					currentCell = currentRow.createCell(column);
				}
				currentCell.setCellStyle(customCellStyle);
			}
		}

		Cell deptsCell = headerDepts.createCell(1);
		deptsCell.setCellValue("");
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 3));

		for (int row = 3; row <= 3; row++) {
			Row currentRow = sheet.getRow(row);
			if (currentRow == null) {
				currentRow = sheet.createRow(row);
			}
			for (int column = 1; column <= 3; column++) {
				Cell currentCell = currentRow.getCell(column);
				if (currentCell == null) {
					currentCell = currentRow.createCell(column);
				}
				currentCell.setCellStyle(customCellStyle);
			}
		}
		
		Cell cellReportGeneratedBy = headerRowReportGenBy.createCell(1);
		cellReportGeneratedBy.setCellValue("Report generated by:");
		cellReportGeneratedBy.setCellStyle(customCellStyle);

		Cell reportGeneratedByCell = headerRowReportGenBy.createCell(2);
		reportGeneratedByCell.setCellValue(empObj.getEname());
		reportGeneratedByCell.setCellStyle(dateCellStyle);
		
		Cell cellReportGeneratedDate = headerRowReportGenOn.createCell(1);
		cellReportGeneratedDate.setCellValue("Report generated on:");
		cellReportGeneratedDate.setCellStyle(customCellStyle);

		Cell reportGeneratedDateCell = headerRowReportGenOn.createCell(2);
		Date currentDate = new Date();
		SimpleDateFormat istDateFormat = new SimpleDateFormat(StringLiteralConstants.DATE_TIME_FORMAT);
		istDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		String formattedDate = istDateFormat.format(currentDate);
		reportGeneratedDateCell.setCellValue(formattedDate);
		for (int row = 8; row <= 8; row++) {
			Row currentRow = sheet.getRow(row);
			if (currentRow == null) {
				currentRow = sheet.createRow(row);
			}
			for (int column = 2; column <= 2; column++) {
				Cell currentCell = currentRow.getCell(column);
				if (currentCell == null) {
					currentCell = currentRow.createCell(column);
				}
				currentCell.setCellStyle(dateCellStyle);
			}
		}

		// Create header rows
		Cell cellSeNo = headerRow1.createCell(1);
		cellSeNo.setCellValue("S No");
		cellSeNo.setCellStyle(headerStyle);

		Cell cellDevNm = headerRow1.createCell(2);
		cellDevNm.setCellValue("Device name");
		cellDevNm.setCellStyle(headerStyle);

		Cell cellTestDt = headerRow1.createCell(3);
		cellTestDt.setCellValue("Tested on");
		cellTestDt.setCellStyle(headerStyle);

		CellStyle stringStyle = workbook.createCellStyle();
		stringStyle.setWrapText(true);
		stringStyle.setAlignment(HorizontalAlignment.LEFT);
		stringStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		stringStyle.setBorderTop(BorderStyle.THIN);
		stringStyle.setBorderBottom(BorderStyle.THIN);
		stringStyle.setBorderLeft(BorderStyle.THIN);
		stringStyle.setBorderRight(BorderStyle.THIN);

		CellStyle numericStyle = workbook.createCellStyle();
		numericStyle.setWrapText(true);
		numericStyle.setAlignment(HorizontalAlignment.RIGHT);
		numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		numericStyle.setBorderTop(BorderStyle.THIN);
		numericStyle.setBorderBottom(BorderStyle.THIN);
		numericStyle.setBorderLeft(BorderStyle.THIN);
		numericStyle.setBorderRight(BorderStyle.THIN);

		int rowIndex = 11;
		sheet.setColumnWidth(1, 6200);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		List<Integer> columnIndexes = Arrays.asList(1, 2, 3);
		for (ReportsDto pass : passes) {
			Row row = sheet.createRow(rowIndex);
			
			Cell seNoCell = row.createCell(1);
			seNoCell.setCellValue(pass.getSeNo());
			seNoCell.setCellStyle(numericStyle);

			Cell devNmCell = row.createCell(2);
			if (pass.getDevName() != null) {
				devNmCell.setCellValue(pass.getDevName());
			} else {
				devNmCell.setCellValue("Device name not available");
			}
			devNmCell.setCellStyle(stringStyle);

			Cell testDtCell = row.createCell(3);
			if (pass.getTestedDt() != null) {
				testDtCell.setCellValue(pass.getTestedDt());
			} else {
				testDtCell.setCellValue("Tested Date not available");
			}
			testDtCell.setCellStyle(stringStyle);
			    
			rowIndex++;
		}
		for (int columnNumber = 1; columnNumber < 4; columnNumber++) {
			if (!columnIndexes.contains(Integer.valueOf(columnNumber)))
				sheet.autoSizeColumn(columnNumber);
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		return outputStream;
	}
}
