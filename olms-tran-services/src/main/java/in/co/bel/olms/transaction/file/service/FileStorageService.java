package in.co.bel.olms.transaction.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.transaction.file.exception.FileStorageException;
import in.co.bel.olms.transaction.file.exception.MyFileNotFoundException;
import in.co.bel.olms.transaction.file.util.FileStorageProperties;
import in.co.bel.olms.validator.FileValidator;

/**
 * @author Awadhesh Verma
 * @since Feb 14, 2023 The Service class for storing the files
 */

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            logger.error("Unable to create directory to store the file {}", ex.getMessage());
            throw new FileStorageException("Could not create the directory to upload");
        }
    }

    //	function to store the file
    public String storeFile(MultipartFile file, Integer id, int index, String... fileNameStr) {

        String returnString = "";
        if (file != null) {
            String message = null;
            try {
                // Check if var arg length is 0, then taking the fileName from first argument
                String fileOriginalName = fileNameStr.length == 0 ? file.getOriginalFilename() : fileNameStr[0];
//				String[] fileNameArray = fileOriginalName.split("\\.");
//				String fileExtension = fileNameArray[fileNameArray.length - 1];
//				String fileName = null;
//				long date = new Date().getTime();
//				fileName = id + "_" + ++index + "_" + date + "." + fileExtension;
                String fileName = fileOriginalName;
                try {
                    Path targetLocation = this.fileStorageLocation.resolve(fileName);
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    returnString = fileName;
                } catch (IOException ex) {
                    logger.error("Could not store file {}: {}", fileName, ex.getMessage());
                    throw new FileStorageException("Could not store file" + fileName + ". Please try again!", ex);
                }
            } catch (Exception e) {
                message = ApplicationConstants.FAILURE;
                return message;
            }
        }
        return returnString;
    }

    //	function to load the file
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            logger.error("File not found {}: {}", fileName, ex.getMessage());
            throw new MyFileNotFoundException("File not found " + fileName);
        }
    }

    public String deleteFile(String fileName) {

        String returnString = ApplicationConstants.FAILURE;
        if (fileName != null) {
            String message = null;
            try {
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.deleteIfExists(targetLocation);
                returnString = ApplicationConstants.SUCCESS;
            } catch (IOException ex) {
                logger.error("Could not delete file {}: {}", fileName, ex.getMessage());
                return message;
            } catch (Exception e) {
                logger.error("Could not delete file {}: {}", fileName, e.getMessage());
                return message;
            }
        }
        return returnString;
    }
    
    public String storeFileNew(MultipartFile file, String... fileNameStr) {

        String returnString = "";
        if (file != null) {
        	if (FileValidator.checkFile(file)) {
            String message = null;
            try {
                // Check if var arg length is 0, then taking the fileName from first argument
                String fileOriginalName = fileNameStr.length == 0 ? file.getOriginalFilename() : fileNameStr[0];
                String fileName = fileOriginalName;
                try {
                    Path targetLocation = this.fileStorageLocation.resolve(fileName);
                    
                    if (Files.exists(targetLocation)) {
                    	fileName = generateUniqueFileName(fileName);
                        targetLocation = this.fileStorageLocation.resolve(fileName);
                    }
                    
                    file.transferTo(targetLocation.toFile());
                    returnString = fileName;
                } catch (IOException ex) {
                    logger.error("Could not store file {}: {}", fileName, ex.getMessage());
                    throw new IllegalArgumentException("Could not store file" + fileName + ". Please try again!", ex);
                }
            } catch (Exception e) {
                message = "failure";
                return message;
            }
          }
        }
        return returnString;
        
    }
    
 // Helper method to generate a unique file name
    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        String nameWithoutExtension = originalFileName.substring(0, dotIndex > 0 ? dotIndex : originalFileName.length());
        String[] str = nameWithoutExtension.split("\\(");
        
        // Get all files in the folder
        List<Path> files = new ArrayList<>();
        try (Stream<Path> fileStream = Files.walk(this.fileStorageLocation)) {
            files = fileStream.filter(Files::isRegularFile).toList();
        } catch (IOException e) {
        	logger.error("error: {}",e.getMessage());
		}
        
    	int i = 1;
    	if(str.length > 1) {
    		i = Integer.valueOf(String.valueOf(str[1].charAt(0)));
    	}
    	for(Path file : files) {
    		String fileName = file.getFileName().toString();
    		String newFileName = nameWithoutExtension + " (" + i + ")" + extension;
    		if(fileName.equals(newFileName)) {
    			i++;
    		}
    	}
    	
    	return nameWithoutExtension.trim() + " (" + i + ")" + extension;
    }
}
