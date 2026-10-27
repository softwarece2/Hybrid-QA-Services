package in.co.bel.olms.upload.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Manasa B
 * @since 27-10-22
 */
public  class FileUploadUtil {


	
	public static String saveFile(String fileName, MultipartFile multipartFile,String prefix, String pathName) throws IOException
	{
		Path uploadPath = Paths.get(pathName);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
	
	
		String fileCode = prefix;
		String filecompletePath=pathName + "/"+ fileCode + "-" + fileName;
		try (InputStream inputStream = multipartFile.getInputStream()) {
			
			
		Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
		Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
		throw new IOException("Could not save file: " + fileName, ioe);
	}
		return filecompletePath;
	}
}
