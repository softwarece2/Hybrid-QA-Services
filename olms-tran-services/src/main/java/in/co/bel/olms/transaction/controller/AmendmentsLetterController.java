package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.AmendmentsLetterDto;
import in.co.bel.olms.transaction.service.AmendmentsLetterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/amendmentsLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AmendmentsLetterController {	

    private final AmendmentsLetterService amendmentsLetterService;
    
    @GetMapping("/type/{type}/id/{id}")
    public ResponseEntity<Object> getByIdAndType(@PathVariable Integer type, @PathVariable Integer id) {

        List<AmendmentsLetterDto> amendmentsLetterDto = null;
        try {
            amendmentsLetterDto = amendmentsLetterService.getByIdAndType(id, type);
            return ResponseEntity.ok(amendmentsLetterDto);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
       
    @PostMapping("/save")
    public ResponseEntity<String> saveAmendmentsLetterData(
            @RequestParam(value = "amendmentsLetterDtoStr", required = true) String amendmentsLetterDtoStr,
            @RequestParam(value = "attachments", required = false) MultipartFile[] attachments ) {
        Gson gson = new Gson();
        AmendmentsLetterDto amendmentsLetterDto= gson.fromJson(amendmentsLetterDtoStr, new TypeToken<AmendmentsLetterDto>(){}.getType());
        
        try {
            AmendmentsLetterDto savedAmendmentsLetterDto = amendmentsLetterService.saveAmendmentsLetterData(amendmentsLetterDto, attachments);
            
            return new ResponseEntity<>(gson.toJson(savedAmendmentsLetterDto), HttpStatus.CREATED);
        } catch (DataNotValidException de) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Error-Message", de.getMessage());
            return ResponseEntity.badRequest().headers(headers).body(de.getMessage());
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Error-Message", e.getMessage());
            return ResponseEntity.internalServerError().headers(headers).body(e.getMessage());
        }
    }

}
