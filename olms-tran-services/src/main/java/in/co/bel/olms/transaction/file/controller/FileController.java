package in.co.bel.olms.transaction.file.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.transaction.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;

/**
 * @author Awadhesh Verma
 * @since Feb 14, 2023 The controller class for downloading the stored photo files
 * 
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

	private final FileStorageService fileStorageService;

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@GetMapping("/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileStorageService.loadFileAsResource(fileName);

		String contentType = null;

		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.error("Could not determine fileType");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
	
	@PostMapping("/store")
	public String storeFile(@RequestParam("file") MultipartFile file) {
		String store = fileStorageService.storeFileNew(file);
		return store;
	}
	
	@PostMapping("/{fileName:.+}")
	public String deleteFile(@PathVariable String fileName){
		return fileStorageService.deleteFile(fileName);
	}
}
