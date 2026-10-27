package in.co.bel.olms.transaction.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.WarrantCertificateDto;
import in.co.bel.olms.transaction.service.WarrantCertificateService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/warrantCertificate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WarrantCertificateController {
	
	private static final Logger logger =LoggerFactory.getLogger(WarrantCertificateController.class);
	
	private final WarrantCertificateService warrantCertificatService;

    private final HmisAppSecurity hmisAppSecurity = new HmisAppSecurity();

	@PostMapping("/save")
    public ResponseEntity<String> saveWarrantCertificate(
	        @RequestBody @Valid WarrantCertificateDto warrantCertificateDto) {
	    try {
            String response = warrantCertificatService.saveWarrantCertificate(warrantCertificateDto);
	        return  ResponseEntity.status(HttpStatus.CREATED).body(response); 
	    } 
	    catch (DataNotValidException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(de.getMessage());
	    } catch (Exception e) {
	    	logger.error("Unexpected error while saving warrant certificate: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
	
	@GetMapping("/getAll")
    public ResponseEntity<List<WarrantCertificateDto>> getAllWarrantCertificates() {
		List<WarrantCertificateDto> wcDto = warrantCertificatService.getAllWarrantCertificate();
	return ResponseEntity.ok(wcDto);
    }
	
	@GetMapping("/getWarrantCertificateDetails/{inoteId}")
    public ResponseEntity<List<WarrantCertificateDto>> getWarrantCertificateDetails(@PathVariable Integer inoteId) {
		List<WarrantCertificateDto> wcDto = warrantCertificatService.getWarrantDetailsByInoteId(inoteId);
	return ResponseEntity.ok(wcDto);
    }
	
	@GetMapping("/getWarrantCertificateDetailsInternal/{internalInoteId}")
    public ResponseEntity<List<WarrantCertificateDto>> getInternalInoteWarrantCertificateDetails(@PathVariable Integer internalInoteId) {
		List<WarrantCertificateDto> wcDto = warrantCertificatService.getWarrantDetailsByInternalInoteId(internalInoteId);

	return ResponseEntity.ok(wcDto);
    }

    @PreAuthorize("hasAnyAuthority('DO')")
    @PostMapping("/{iNoteId}/approve")
    public ResponseEntity<String> approveQcc(@PathVariable(name = "iNoteId") Integer iNoteId, @RequestHeader(name = "Authorization") String token) {
        try {
            Integer empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
            Integer roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);

            String response = warrantCertificatService.approveWarrantyCertificate(iNoteId, empId, roleId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(de.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
