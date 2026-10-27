package in.co.bel.olms.iol.feign;

import javax.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "olms-tran-services", configuration=FeignMultipartConfig.class)
public interface FileUploadFeignService {

    @GetMapping("/files/{fileName:.+}")
    ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);
   
    @PostMapping(value="/files/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String storeFile(@RequestPart("file") MultipartFile file);
    
    @PostMapping("/files/{fileName:.+}")
    String deleteFile(@PathVariable String fileName);
    
}
