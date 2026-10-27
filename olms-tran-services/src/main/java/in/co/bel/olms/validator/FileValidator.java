package in.co.bel.olms.validator;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class FileValidator {
	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "jpg", "jpeg", "png", "gif", "doc", "docx", "xls", "xlsx",
            "dwg", "dxf", "ai", "svg", "cdr", "skp", "psd","txt");
	public static boolean checkFile(@RequestBody MultipartFile file) {
		
		   if (file.isEmpty()) {
	            return false;
	        }

	        String fileName = file.getOriginalFilename();
	        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

	        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
	            return false;
	        }

	        return true;

     
}
}