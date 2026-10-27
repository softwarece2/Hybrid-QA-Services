package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.OfferLetterDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internalOfferLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InternalOfferLetterControlller {

    private final InternalOfferLetterService internalOfferLetterService;

    private static final Logger logger = LoggerFactory.getLogger(InternalOfferLetterControlller.class);
//    @PostMapping("/saveInternalOl")
//    public ResponseEntity<String> saveInternalOl(@RequestBody @Valid OfferLetterDto offerLetterDto) {
//
//        Map<Boolean, String> response = internalOfferLetterService.saveInternalOl(offerLetterDto);
//
//        if (response.containsKey(true)) {
//            String returnString = response.get(true);
//            return new ResponseEntity<>(returnString, HttpStatus.CREATED);
//        } else {
//            String returnString = response.get(false);
//            return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/getInternalOlByOlId/{id}")
    public ResponseEntity<OfferLetterDto> getInternalOlByOlId(@PathVariable int id) {
        return ResponseEntity.ok(internalOfferLetterService.getInternalOlByOlId(id));
    }

    @GetMapping("/getInternalOlByEmpId/{empId}")
    public ResponseEntity<List<OfferLetterDto>> getInternalOlByEmpId(@PathVariable int empId) {
        try {
            List<OfferLetterDto> offerLetterDtos = internalOfferLetterService.getInternalOlByEmpId(empId);
            return ResponseEntity.ok(offerLetterDtos);
        } catch (Exception e) {
        	 logger.error("Error retrieving internal offer letter for Employee ID: {}", empId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
