package in.co.bel.olms.transaction.service.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 27-10-22
 */
@Service
@RequiredArgsConstructor
public class SOUploadService {

	private final SupplyOrderRepository supplyOrderRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SOUploadService.class);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	@Transactional
	public String uploadSOData(UploadDto dto)
	{
		List<SupplyOrder> soList = new ArrayList<>();
		
		try {
			Date todayDate = new Date();
			String filePath = dto.getFileName();
			try (XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath))) {
				// Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);
				
				// Iterate through each rows one by one
				Iterator<Row> iterator = sheet.iterator();
				int i = 0;
				int rowCount = 0;
				while(iterator.hasNext()) {
					SupplyOrder so = new SupplyOrder();
					Row nextRow = iterator.next();
					Cell cell = nextRow.getCell(0);
					
					Integer soId = Integer.parseInt(cell.getStringCellValue());
					so.setId(soId);
					cell = nextRow.getCell(1);
					so.setOrderNo(cell.getStringCellValue());
					so.setCreatedDate(todayDate);
					soList.add(so);
					i++;
					rowCount++;
					if((i%100) == 0) {
						supplyOrderRepository.saveAll(soList);
						soList = new ArrayList<>();
						i = 0;
						logger.debug("Row count: {}", i);
					}
					else if(!iterator.hasNext()) {
						supplyOrderRepository.saveAll(soList);
						logger.debug("Row count: {} total: {}", i, rowCount); 
					}
				}
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();	
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return ApplicationConstants.SUCCESS;
				
	}
	
}
