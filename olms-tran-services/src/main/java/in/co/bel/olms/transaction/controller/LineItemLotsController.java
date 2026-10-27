package in.co.bel.olms.transaction.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.LineItemDetailsDto;
import in.co.bel.olms.transaction.dto.LineItemLotsDto;
import in.co.bel.olms.transaction.entity.LineItemLots;
import in.co.bel.olms.transaction.service.LineItemLotsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lineItemlots")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class LineItemLotsController {
	
	private final LineItemLotsService lineItemLotsService;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(LineItemLotsController.class);
	
	@GetMapping("/findBySoLiId/{soLiId}")
	public ResponseEntity<LineItemLotsDto> getLineItemLotsBySoLiId(@PathVariable int soLiId) throws DataNotFoundException {
	    List<LineItemLots> iLs = lineItemLotsService.getItemLotsBySoLiId(soLiId);
	    if (iLs.isEmpty()) {
	        return ResponseEntity.ok(null);
	    }
	    LineItemLotsDto consolidatedDto = new LineItemLotsDto();
	    consolidatedDto.setSoLineItemId(soLiId);
	    consolidatedDto.setCreatedDatetime(iLs.get(0).getCreatedDatetime());
	    List<LineItemDetailsDto> lineItems = iLs.stream()
	            .map(lot -> mapper.map(lot, LineItemDetailsDto.class))
	            .toList();
	    consolidatedDto.setLineItems(lineItems);
	    return ResponseEntity.ok(consolidatedDto);
	}

	@GetMapping("/financialYears")
	public ResponseEntity<?> getAllFinancialYears() {
		try {
			List<String> financialYears = lineItemLotsService.getAllFinancialYears();
			return ResponseEntity.ok().body(financialYears);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			 logger.error("Error while fetching financial-years: {}", e.getMessage(),e);
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching financial-years");
	    }
	}

	@GetMapping("/findBySoLiId/{soLiId}/{financialYear}")
	public ResponseEntity<LineItemLotsDto> getItemLotsBySoLiIdAndFinancialYear(@PathVariable int soLiId, @PathVariable String financialYear) throws DataNotFoundException {
	    List<LineItemLots> iLs = lineItemLotsService.getItemLotsBySoLiIdAndFinancialYear(soLiId, financialYear);
	    if (iLs.isEmpty()) {
	        return ResponseEntity.ok(null);
	    }
	    LineItemLotsDto consolidatedDto = new LineItemLotsDto();
	    consolidatedDto.setSoLineItemId(soLiId);
	    consolidatedDto.setCreatedDatetime(iLs.get(0).getCreatedDatetime());
	    List<LineItemDetailsDto> lineItems = iLs.stream()
	            .map(lot -> mapper.map(lot, LineItemDetailsDto.class))
	            .toList();
	    consolidatedDto.setLineItems(lineItems);
	    return ResponseEntity.ok(consolidatedDto);
	}
	
    @PostMapping("/save")
    public ResponseEntity<?> saveLineItemLots(@RequestBody @Valid LineItemLotsDto requestDto) {
        try {
            if (requestDto.getSoLineItemId() == null) {
                return ResponseEntity.badRequest().body("soLineItemId is required");
            }
            List<LineItemLots> iLs = lineItemLotsService.getItemLotsBySoLiId(requestDto.getSoLineItemId());
            for(LineItemLots lots:iLs) {
            	if(requestDto.getLineItems().get(0).getUnitPrice()==(lots.getUnitPrice())){
            		if(requestDto.getLineItems().get(0).getFinancialYear().equals(lots.getFinancialYear())) {
            			 return ResponseEntity.badRequest().body("Unit Price already exist for this Financial year");
            		}
            	}
            }
            List<LineItemLots> savedItemLots = lineItemLotsService.saveOrUpdateLineItemLots(requestDto);
            // Consolidate into a single LineItemLotsDto
            LineItemLotsDto consolidatedDto = new LineItemLotsDto();
            consolidatedDto.setSoLineItemId(requestDto.getSoLineItemId());
            if (!savedItemLots.isEmpty()) {
                consolidatedDto.setCreatedDatetime(savedItemLots.get(0).getCreatedDatetime());
            }
            List<LineItemDetailsDto> lineItems = savedItemLots.stream()
                    .map(lot -> mapper.map(lot, LineItemDetailsDto.class))
                    .toList();
            consolidatedDto.setLineItems(lineItems);
            return new ResponseEntity<>(consolidatedDto, HttpStatus.CREATED);
        } catch (DataNotValidException de) {
            return ResponseEntity.badRequest().body(de.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PostMapping("/update")
    public ResponseEntity<?> updateLineItemLots(@RequestBody @Valid LineItemLotsDto requestDto) {
        try {
            // Validate soLineItemId in the requestDto matches the path parameter
            if (requestDto.getSoLineItemId()==null) {
                return ResponseEntity.badRequest().body("soLineItemId in path and request body do not match.");
            }
            // Fetch existing LineItemLots
            List<LineItemLots> existingLots = lineItemLotsService.getItemLotsBySoLiId(requestDto.getSoLineItemId());
            // Check if any lots exist for the given soLineItemId
            if (existingLots.isEmpty()) {
                return ((BodyBuilder) ResponseEntity.notFound()).body("No Line Item Lots found for soLineItemId: " + requestDto.getSoLineItemId());
            }
            
            //Modified By: Chirag, Date: 10-10-26 
            if (!requestDto.getLineItems().isEmpty()) {
                LineItemDetailsDto updatedItem = requestDto.getLineItems().get(0);
                
                for (LineItemLots lot : existingLots) {
                    // Skip validation if this is the SAME record that is updating
                    if (lot.getId().equals(updatedItem.getId())) {
                        continue; 
                    }

                    // Check for duplicates in OTHER records
                    boolean isSamePrice = Double.compare(updatedItem.getUnitPrice(), lot.getUnitPrice()) == 0;
                    boolean isSameYear = updatedItem.getFinancialYear().equals(lot.getFinancialYear());

                    if (isSamePrice && isSameYear) {
                        return ResponseEntity.badRequest().body("Unit Price already exists for this Financial year.");
                    }
                }
            }	
            
            // Update the existing lots.  This assumes the requestDto contains
            // the updated information for *all* existing lots.
            List<LineItemLots> updatedLots = lineItemLotsService.updateLineItemLots(existingLots, requestDto);
            // Consolidate into a single LineItemLotsDto (optional, revisit if not needed for your API)
            LineItemLotsDto consolidatedDto = new LineItemLotsDto();
            consolidatedDto.setSoLineItemId(requestDto.getSoLineItemId());  // Use the path parameter value
            if (!updatedLots.isEmpty()) {
                consolidatedDto.setCreatedDatetime(updatedLots.get(0).getCreatedDatetime());
            }
            List<LineItemDetailsDto> lineItems = updatedLots.stream()
                    .map(lot -> mapper.map(lot, LineItemDetailsDto.class))
                    .toList();
            consolidatedDto.setLineItems(lineItems);
            return new ResponseEntity<>(consolidatedDto, HttpStatus.OK); // Use HttpStatus.OK
        } catch (DataNotValidException de) {
            return ResponseEntity.badRequest().body(de.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
	@DeleteMapping("/delete/{soLiId}/{id}")
    public ResponseEntity<String> deleteLineItemLot(@PathVariable Integer soLiId, @PathVariable Integer id) {
        try {
            lineItemLotsService.deleteLineItemLot(soLiId, id);
            return ResponseEntity.ok().body("success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
        	logger.error("Error deleting line item lot: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting line item lot");
        }
    }
}

