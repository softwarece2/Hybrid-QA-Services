package in.co.bel.olms.ate.controller;

import java.util.List;

import in.co.bel.olms.ate.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.FaiDto;
import in.co.bel.olms.ate.service.FaiService;

@RestController
@RequestMapping("/faiDetails")
@RequiredArgsConstructor
public class FaiController {

    private final FaiService faiService;
    
    private static final Logger logger = LoggerFactory.getLogger(FaiController.class);

    @PostMapping("/submit")
    public ResponseEntity<String> saveFai(@RequestBody FaiDto faiDto) {
        try {
            String savedFai = faiService.saveFai(faiDto);
            return ResponseEntity.ok(savedFai);
        } catch (Exception e) {
            logger.error("Error saving FAI: {}", e.getMessage(),e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addRegNos")
    public ResponseEntity<String> addRegNos(@RequestBody FaiDto faiDto) {
        try {
            String savedFai = faiService.addRegNoToFai(faiDto);
            return ResponseEntity.ok(savedFai);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException saving FAI: {}", e.getMessage(),e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving FAI: {}", e.getMessage(),e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("getByOlId/{olLineItemId}") 
    public ResponseEntity<List<FaiDto>> getFaiByOlLineItemId(@PathVariable Integer olLineItemId) {
        try {
            List<FaiDto> faiList = faiService.getFaiByOlLineItemId(olLineItemId);
            return ResponseEntity.ok(faiList); 
        } catch (Exception e) {         
            return ResponseEntity.badRequest().build(); 
        }
    }


    @GetMapping("getByOlIdForQa/{olLineItemId}")
    public ResponseEntity<List<FaiDto>> getFaiByOlLineItemIdForQa(@PathVariable Integer olLineItemId) {
        try {
            List<FaiDto> faiList = faiService.getFaiByOlLineItemIdFroQa(olLineItemId);
            return ResponseEntity.ok(faiList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping("/saveSampleSelection")
    public ResponseEntity<List<LineItemLotsRegister>> saveSampleSelections(@RequestBody LineItemLotsRegisterDto registerDto) {
        try {
            List<LineItemLotsRegister> savedRegisters = faiService.saveSampleSelection(registerDto);
            return new ResponseEntity<>(savedRegisters, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error saving sample selections: {}", e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveFaiSampleSelection")
    public ResponseEntity<List<LineItemLotsRegister>> saveFaiSampleSelections(@RequestBody LineItemLotsRegisterDto registerDto) {
        try {
            List<LineItemLotsRegister> savedRegisters = faiService.saveFaiSampleSelection(registerDto);
            return new ResponseEntity<>(savedRegisters, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error saving sample selections: {}", e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}