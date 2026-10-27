package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.LineItemDetailsDto;
import in.co.bel.olms.transaction.dto.LineItemLotsDto;
import in.co.bel.olms.transaction.entity.LineItemLots;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.repository.LineItemLotsRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineItemLotsService {

	private final LineItemLotsRepository lineItemLotsRepository;

	private final SoLineItemRepository soLineItemRepository;

	public List<LineItemLots> getItemLotsBySoLiId(int soLiId) {
		return lineItemLotsRepository.getItemLotsBySoLiId(soLiId);
	}
	
	public List<LineItemLots> getItemLotsBySoLiIdAndFinancialYear(int soLiId, String financialYear) {
		return lineItemLotsRepository.getItemLotsBySoLiIdAndFinancialYear(soLiId, financialYear);
	}

	@Transactional
    public List<LineItemLots> saveOrUpdateLineItemLots(LineItemLotsDto requestDto) {
        Integer soLineItemId = requestDto.getSoLineItemId();
        List<LineItemDetailsDto> lineItems = requestDto.getLineItems();

        SOLineItem soLineItem = soLineItemRepository.findById(soLineItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid so-LineItem-Id: " + soLineItemId));

        BigDecimal totalOrderQty = soLineItem.getOrderQty();

        List<LineItemLots> existingLots = lineItemLotsRepository.getItemLotsBySoLiId(soLineItemId);
        BigDecimal existingOfferedQty = existingLots.stream()
        	    .map(lot -> lot.getOfferedQty() != null ? lot.getOfferedQty() : BigDecimal.ZERO)
        	    .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Calculate total offered quantity with new items
        BigDecimal totalOfferedQty = existingOfferedQty;
        if(lineItems != null) {
        	for (LineItemDetailsDto item : lineItems) {
                // Adjust for updated record (subtract old qty, add new qty)
                if (item.getId() != null) {
                    LineItemLots existingLot = lineItemLotsRepository.findById(item.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid line item lot ID: " + item.getId()));
                    totalOfferedQty = totalOfferedQty.subtract(existingLot.getOfferedQty()) ; 
                }
                totalOfferedQty = totalOfferedQty.add(item.getOfferedQty()) ; 
            }
        }

        if (totalOfferedQty.compareTo(totalOrderQty)> 0) {
            throw new IllegalArgumentException("Total offered quantity exceeds order quantity. " +
                    "Allowed quantity: " + totalOrderQty + ", Remaining quantity: " +
                    (totalOrderQty.subtract(existingOfferedQty)));
        }

        List<LineItemLots> updatedLineItemLots = new ArrayList<>();

        if (lineItems != null) {
            for (LineItemDetailsDto item : lineItems) {
                if (item.getId() != null) {
                    // Update existing record
                    LineItemLots existingLot = lineItemLotsRepository.findById(item.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid line item lot ID: " + item.getId()));
                    existingLot.setOfferedQty(item.getOfferedQty());
                    existingLot.setFinancialYear(item.getFinancialYear());
                    existingLot.setUnitPrice(item.getUnitPrice());
                    existingLot.setModifiedDatetime(new Date());
                    updatedLineItemLots.add(existingLot);
                } else {
                    // Create new entry
                    LineItemLots liLot = new LineItemLots();
                    liLot.setSoLineItem(soLineItem);
                    liLot.setOfferedQty(item.getOfferedQty());
                    liLot.setFinancialYear(item.getFinancialYear());
                    liLot.setUnitPrice(item.getUnitPrice());
                    liLot.setCreatedDatetime(new Date());
                    updatedLineItemLots.add(liLot);
                }
            }
        }

        try {
            return lineItemLotsRepository.saveAll(updatedLineItemLots);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error saving line item lots: " + e.getMessage(), e);
        }
    }

	public void deleteLineItemLot(Integer soLiId, Integer id) {
	    soLineItemRepository.findById(soLiId).orElseThrow(() -> new IllegalArgumentException("Invalid soLineItemId: " + soLiId));
	
	    LineItemLots existingLot = lineItemLotsRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Line item lot with ID " + id + " not found"));
	
	    if (!existingLot.getSoLineItem().getId().equals(soLiId)) {
	        throw new IllegalArgumentException("Line item lot does not belong to specified soLineItemId");
	    }
	    lineItemLotsRepository.delete(existingLot);
	}


	public List<LineItemLots> updateLineItemLots(List<LineItemLots> existingLots, LineItemLotsDto requestDto) {
						
	    Integer soLineItemId = requestDto.getSoLineItemId();
	    List<LineItemDetailsDto> lineItems = requestDto.getLineItems();
	    SOLineItem soLineItem = soLineItemRepository.findById(soLineItemId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid soLiId: " + soLineItemId));

	    BigDecimal totalOrderQty = soLineItem.getOrderQty();
//	    BigDecimal existingOfferedQty = existingLots.stream()
//	            .map(LineItemLots::getOfferedQty)
//	            .reduce(BigDecimal.ZERO, BigDecimal::add);
//	    // Calculate total offered quantity based on the request DTO
//	    BigDecimal totalOfferedQty = existingOfferedQty;
	    
	    //Modified By: Chirag, Date: 10-10-26
	    BigDecimal totalOfferedQty = BigDecimal.ZERO; 
	    if (lineItems != null) {
	        for (LineItemDetailsDto item : lineItems) {
	            totalOfferedQty = totalOfferedQty.add(item.getOfferedQty());
	        }
	    }

	    if (totalOfferedQty.compareTo(totalOrderQty) > 0) {
	        throw new IllegalArgumentException("Total offered quantity exceeds order quantity. " +
	                "Allowed quantity: " + totalOrderQty + ", Requested: " + totalOfferedQty );
	    }

	    List<LineItemLots> updatedLineItemLots = new ArrayList<>();

	    if (lineItems != null) {
	        for (LineItemDetailsDto item : lineItems) {
	            // Find the existing LineItemLots with corresponding ID.
	            LineItemLots existingLot = existingLots.stream()
	                    .filter(lot -> lot.getId().equals(item.getId()))
	                    .findFirst()
	                    .orElse(null);

	            if (existingLot != null) {
	                // Update the existing record
	                existingLot.setOfferedQty(item.getOfferedQty());
	                existingLot.setFinancialYear(item.getFinancialYear());
	                existingLot.setUnitPrice(item.getUnitPrice());
	                existingLot.setModifiedDatetime(new Date());
	                updatedLineItemLots.add(existingLot); // Add to the updated list

	            } else {
	                   //If the existing list does NOT contain element with the Id.
	                   //This part should not run as existingLots already contains the bunch of ItemLots by that SOLineItemId.
	                log.warn("LineItemDetailsDto id {} not found in existing Lots.  Skipping creation.",item.getId());
	            }
	        }
	    }

	    try {
	        return lineItemLotsRepository.saveAll(updatedLineItemLots);
	    } catch (Exception e) {
	        log.error(e.getMessage());
	        throw new RuntimeException("Error saving line item lots: " + e.getMessage(), e);
	    }
	}

  public List<String> getAllFinancialYears() {
    int currentYear = Year.now().getValue();
    List<String> financialYears = new ArrayList<>();

    // Past 10 years
    for (int i = -10; i < 0; i++) {
      int startYear = currentYear + i;
      int endYear = currentYear + i + 1;
      String yearRange = startYear + "-" + (endYear % 100);
      financialYears.add(yearRange);
    }

    // Future 50 years
    for (int i = 0; i < 50; i++) {
      int startYear = currentYear + i;
      int endYear = currentYear + i + 1;
      String yearRange = startYear + "-" + (endYear % 100);
      financialYears.add(yearRange);
    }
    return financialYears;
  }

}
