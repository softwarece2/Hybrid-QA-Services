//package in.co.bel.olms.iol.service;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import in.co.bel.core.constants.ApplicationConstants;
//import in.co.bel.olms.iol.dto.ColdTestUploadDto;
//import in.co.bel.olms.iol.dto.ColdTestUploadMappingDto;
//import in.co.bel.olms.iol.entity.ColdTest;
//import in.co.bel.olms.iol.entity.ColdTestUpload;
//import in.co.bel.olms.iol.entity.ColdTestUploadMapping;
//import in.co.bel.olms.iol.repository.ColdTestRepository;
//import in.co.bel.olms.iol.repository.ColdTestUploadMapRepository;
//import in.co.bel.olms.iol.repository.ColdTestUploadRepository;
//import in.co.bel.olms.master.exception.DataNotFoundException;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ColdTestFileUploadService {
//
//	@Value("${file.testupload-dir}")
//	private String testFileUploadPath;
//
//	private final ColdTestRepository coldTestRepository;;
//
//	private final ColdTestUploadRepository coldTestUploadRepository;
//
//	private final ColdTestUploadMapRepository coldTestUploadMapRepository;
//
//	public String uploadImages(int testId, MultipartFile[] testFiles) {
//		try {
//			Path uploadPath = Paths.get(testFileUploadPath);
//			Files.createDirectories(uploadPath);
//
//			Optional<ColdTest> coldTestOptional = coldTestRepository.findById(testId);
//			if (!coldTestOptional.isPresent()) {
//				return "Cold Test ID not found";
//			}
//
//			ColdTest coldTest = coldTestOptional.get();
//			ColdTestUpload coldTestUpload = new ColdTestUpload();
//			coldTestUpload.setColdTest(coldTest);
//			coldTestUpload.setCreatedDateTime(new Date());
//			coldTestUpload = coldTestUploadRepository.save(coldTestUpload);
//
//			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//			int fileCounter = 1;
//
//			for (MultipartFile testFile : testFiles) {
//				String originalFilename = testFile.getOriginalFilename();
//				if (originalFilename == null) {
//					return "FAILURE: Original filename is null";
//				}
//
//				String mimeType = testFile.getContentType();
//				if (!mimeType.equals("image/png") && !mimeType.equals("image/jpeg")) {
//					return "PNG and JPEG files are allowed.";
//				}
//
//				String extension = "";
//				int index = originalFilename.lastIndexOf('.');
//				if (index > 0) {
//					extension = originalFilename.substring(index);
//				}
//
//				String baseFilename = originalFilename.substring(0, index);
//				String newFilename = baseFilename + "_" + timestamp + "_" + fileCounter + extension;
//
//				Path filePath = uploadPath.resolve(newFilename);
//				Files.copy(testFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//				ColdTestUploadMapping coldTestUploadMapping = new ColdTestUploadMapping();
//				coldTestUploadMapping.setColdTestFileUpload(coldTestUpload);
//				coldTestUploadMapping.setFileName(newFilename);
//				coldTestUploadMapping.setFilePath(filePath.toString());
//				coldTestUploadMapping.setCreatedDateTime(new Date());
//
//				coldTestUploadMapRepository.save(coldTestUploadMapping);
//				fileCounter++;
//			}
//
//			return ApplicationConstants.SUCCESS;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ApplicationConstants.FAILURE;
//		}
//	}
//
//	public List<ColdTestUploadDto> getUploadImagesByTestId(int testId) {
//		List<ColdTestUpload> coldTestUploads = coldTestUploadRepository.getUploadImagesByTestId(testId);
//		List<ColdTestUploadDto> TestFileUploadDtos = new ArrayList<>();
//
//		for (ColdTestUpload testFileUpload : coldTestUploads) {
//			ColdTestUploadDto TestFileUploadDto = new ColdTestUploadDto();
//			TestFileUploadDto.setId(testFileUpload.getId());
//			TestFileUploadDto.setAcceptanceTestId(testFileUpload.getColdTest().getId());
//
//			List<ColdTestUploadMappingDto> uploadMappingDTOs = new ArrayList<>();
//			for (ColdTestUploadMapping testUploadMapping : testFileUpload.getColdTestUploadMappings()) {
//				ColdTestUploadMappingDto uploadMappingDTO = new ColdTestUploadMappingDto();
//				uploadMappingDTO.setId(testUploadMapping.getId());
//				uploadMappingDTO.setFileName(testUploadMapping.getFileName());
//				uploadMappingDTO.setFilePath(testUploadMapping.getFilePath());
//				uploadMappingDTO.setCreatedDateTime(testUploadMapping.getCreatedDateTime());
//				uploadMappingDTOs.add(uploadMappingDTO);
//			}
//
//			TestFileUploadDto.setColdTestUploadMappingDtos(uploadMappingDTOs);
//			TestFileUploadDtos.add(TestFileUploadDto);
//		}
//
//		return TestFileUploadDtos;
//	}
//
//	public byte[] getUploadImagesById(int id) throws IOException {
//		Optional<ColdTestUploadMapping> coldTestUploadMappingOptional = coldTestUploadMapRepository.findById(id);
//
//		if (!coldTestUploadMappingOptional.isPresent()) {
//			throw new DataNotFoundException("Upload with the specified ID not found");
//		}
//
//		ColdTestUploadMapping upload = coldTestUploadMappingOptional.get();
//		Path uploadPath = Paths.get(upload.getFilePath());
//		return Files.readAllBytes(uploadPath);
//	}
//}
//
//
