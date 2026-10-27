package in.co.bel.olms.transaction.controller.check.army;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclSparesDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclSparesDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclSpares;
import in.co.bel.olms.transaction.service.check.army.ScrutinyQaclSparesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyQaclSpares")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyQaclSparesController {

	private final ScrutinyQaclSparesService qaclSparesService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyQaclSparesController.class);
	/**
    
     * @param 
     * @return 
     */
//    @PreAuthorize("hasAnyAuthority('INSP')")
    @PostMapping("/save")
    public ResponseEntity<ScrutinyQaclSparesDto> saveScrutinyQaclSpares(@RequestBody @Valid ScrutinyQaclSparesDto scrutinyQaclSparesDto) {
        try {
            logger.info("Saving scrutiny spares: {}", scrutinyQaclSparesDto.toString());
            ScrutinyQaclSpares scrutinyQaclSpares = qaclSparesService.saveScrutinyQaclSpares(scrutinyQaclSparesDto);
            scrutinyQaclSparesDto = mapper.map(scrutinyQaclSpares, ScrutinyQaclSparesDto.class);
            scrutinyQaclSparesDto.setOfferLetterId(scrutinyQaclSpares.getOfferLetter().getId());
            List<ScrutinyQaclSparesDetDto> qaclSparesDetDtos = qaclSparesService.prepareQaclSparesDets(scrutinyQaclSpares.getScrutinyQaclSparesDets(),scrutinyQaclSpares);
            scrutinyQaclSparesDto.setQaclSparesDetDtos(qaclSparesDetDtos);
            return new ResponseEntity<>(scrutinyQaclSparesDto, HttpStatus.CREATED);
        } catch (DataNotFoundException ex) {
            logger.error("Error saving ScrutinyQaclSpares: {}", ex.getMessage());
            return new ResponseEntity<>(new ScrutinyQaclSparesDto(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error saving ScrutinyQaclSpares: {}", e.getMessage());
            return new ResponseEntity<>(new ScrutinyQaclSparesDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param scrutinyQaclSparesDto 
     * @return 
     */
//    @PreAuthorize("hasAnyAuthority('INSP')")
    @PostMapping("/submit")
    public ResponseEntity<ScrutinyQaclSparesDto> submitScrutinyQaclSpares(@RequestBody @Valid ScrutinyQaclSparesDto scrutinyQaclSparesDto) {
        try {
            ScrutinyQaclSpares scrutinyQaclSpares = qaclSparesService.submitScrutinyQaclSpares(scrutinyQaclSparesDto);
            scrutinyQaclSparesDto = mapper.map(scrutinyQaclSpares, ScrutinyQaclSparesDto.class);
            scrutinyQaclSparesDto.setSubmittedDate(dateFormat.format(scrutinyQaclSpares.getSubmittedDate()));
            List<ScrutinyQaclSparesDetDto> qaclSparesDetDtos = qaclSparesService.prepareQaclSparesDets(scrutinyQaclSpares.getScrutinyQaclSparesDets(),scrutinyQaclSpares);
            scrutinyQaclSparesDto.setQaclSparesDetDtos(qaclSparesDetDtos);
            return new ResponseEntity<>(scrutinyQaclSparesDto, HttpStatus.CREATED);
        } catch (DataNotFoundException ex) {
            logger.error("Error submitting ScrutinyQaclSpares: {}", ex.getMessage());
            return new ResponseEntity<>(new ScrutinyQaclSparesDto(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error submitting ScrutinyQaclSpares: {}", e.getMessage());
            return new ResponseEntity<>(new ScrutinyQaclSparesDto(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
  
     * @param soid
     * @return The ScrutinyQaclSparesDto.
     * @throws DataNotFoundException if the ScrutinyQaclSpares is not found.
     */
    @GetMapping("/ol/{olid}") 
    public ResponseEntity<ScrutinyQaclSparesDto> getByOlId(@PathVariable int olid) throws DataNotFoundException {
        ScrutinyQaclSparesDto dto = qaclSparesService.getScrutinyQaclSparesByOlId(olid);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping("/updateQaclSparesByQAO")
	public ResponseEntity<Boolean> updateQaclSparesByQAO(@RequestParam(value = "scrutinyQacl", required = true) String scrutinyQacl,
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
		
		Gson gson = new Gson();
		ScrutinyQaclSparesDto scrutinyQaclDto = gson.fromJson(scrutinyQacl, new TypeToken<ScrutinyQaclSparesDto>() {
		}.getType());
		
		Boolean returnStatus = qaclSparesService.updateQaclSparesByQAO(scrutinyQaclDto , attachments);
		if (returnStatus.booleanValue()) {

			return new ResponseEntity<>(returnStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(returnStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
