package in.co.bel.olms.transaction.service.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.transaction.dto.upload.UploadDto;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 24-11-22
 */

@Service
@RequiredArgsConstructor
public class LIUploadService {

	private final SoLineItemRepository soLineItemRepository;

	private final SupplyOrderService supplyOrderService;
	
	private static final Logger logger = LoggerFactory.getLogger(LIUploadService.class);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@Transactional
	public String uploadLIData(UploadDto dto) {
		List<SOLineItem> liList = new ArrayList<>();

		try {
			Date todayDate = new Date();
			String filePath = dto.getFileName();
			try (XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath))) {
				XSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> iterator = sheet.iterator();
				int i = 0;
				int rowCount = 0;

				int slnoIndex = 0;
				int descIndex = 1;
				int partnoIndex = 2;
				int costIndex = 3;
				int qtyIndex = 4;
				int valueIndex = 5;

				// Add code for supply order details
				SupplyOrder so=supplyOrderService.getSupplyOrder(dto.getSupplyOrderId());
				while (iterator.hasNext()) {
					SOLineItem li = new SOLineItem();

					Row nextRow = iterator.next();
					Cell cell = nextRow.getCell(slnoIndex);
					li.setLineItemNo(cell.getStringCellValue());

					cell = nextRow.getCell(descIndex);
					li.setDescription(cell.getStringCellValue());

					cell = nextRow.getCell(partnoIndex);
					li.setPartNo(cell.getStringCellValue());
					
					cell = nextRow.getCell(costIndex);
					li.setUnitCost(BigDecimal.valueOf(cell.getNumericCellValue()));
					
					cell = nextRow.getCell(qtyIndex);
				//	li.setOrderQty(Integer. cell.getNumericCellValue());
					
					cell = nextRow.getCell(valueIndex);
					li.setItemValue(BigDecimal.valueOf(cell.getNumericCellValue()));				
					
					li.setSupplyOrder(so);
					li.setCreatedDateTime(todayDate);

					liList.add(li);
					i++;
					rowCount++;
					if ((i % 100) == 0) {
						soLineItemRepository.saveAll(liList);
						liList = new ArrayList<>();
						i = 0;
						logger.debug("Row count: {}", i); 
					} else if (!iterator.hasNext()) {
						soLineItemRepository.saveAll(liList);
						logger.debug("Row count: {} total: {}", i, rowCount); 
					}
				}
			} catch (InvalidFormatException e) {
				logger.error("Invalid Format: ", e);
			}
		} catch (FileNotFoundException e1) {
			logger.error("File not found: ", e1);
		} catch (IOException e) {
			logger.error("IO Exception: ", e);
		}
		return ApplicationConstants.SUCCESS;
	}
}
