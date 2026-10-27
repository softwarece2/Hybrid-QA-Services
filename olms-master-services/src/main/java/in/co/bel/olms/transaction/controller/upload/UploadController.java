package in.co.bel.olms.transaction.controller.upload;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.transaction.dto.upload.UploadDto;
import in.co.bel.olms.transaction.service.upload.SOUploadService;
import in.co.bel.olms.upload.util.FileUploadUtil;
import in.co.bel.olms.upload.util.UploadUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 27-10-22
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UploadController {
	private final Logger logger =LoggerFactory.getLogger(UploadController.class);
	
	Gson gson = new Gson();

	private final SOUploadService soUploadService;
	
	/**
	 * Upload the Supply Order data
	 */
	@PostMapping("/soData")
	public String soData(@RequestParam("lastUpdatedBy") int lastUpdatedBy,
			@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") @Valid String fileName) {
		String retValue = "";
		try {
			String prefix = UploadUtil.getFilePrefx();
			String filecode = FileUploadUtil.saveFile(fileName, multipartFile, prefix,
					ApplicationConstants.LOCATION_SO);
			UploadDto uploadDto = new UploadDto();
			uploadDto.setFileName(filecode);
			uploadDto.setLastUpdatedBy(lastUpdatedBy);
			retValue = soUploadService.uploadSOData(uploadDto);
		} catch (IOException e) {
			 logger.error("Error occurred while uploading soData: {}", e.getMessage());
		}
		return retValue;
	}
	
	/**
	 * Upload the Offer Letter data
	 */
	@PostMapping("/olData")
	public String olData(@RequestParam("lastUpdatedBy") int lastUpdatedBy,
			@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName") @Valid String fileName) {
		String retValue = "";
		try {
			String prefix = UploadUtil.getFilePrefx();
			String filecode = FileUploadUtil.saveFile(fileName, multipartFile, prefix,
					ApplicationConstants.LOCATION_OL);
			UploadDto uploadDto = new UploadDto();
			uploadDto.setFileName(filecode);
			uploadDto.setLastUpdatedBy(lastUpdatedBy);
			retValue = soUploadService.uploadSOData(uploadDto);
		} catch (IOException e) {
			logger.error("Error occurred while uploading LI Data for file: {}", fileName, e);
		}
		return retValue;
	}
}
