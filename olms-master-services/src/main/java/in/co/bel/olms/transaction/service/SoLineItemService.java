package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.LineItemDto;
import in.co.bel.olms.transaction.dto.SoLineItemDto;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 24-11-22
 */

@Service
@RequiredArgsConstructor
public class SoLineItemService {

	private final SoLineItemRepository soLineItemRepository;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(SoLineItemService.class);

	public boolean saveSoLineItems(List<SoLineItemDto> soLineItemDtos) {

		List<SOLineItem> soLineItems = new ArrayList<>();
		try {
			for (SoLineItemDto soLineItemDto : soLineItemDtos) {
				SOLineItem soLineItem = mapper.map(soLineItemDto, SOLineItem.class);
				soLineItem.setCreatedDateTime(new Date());
				BigDecimal itemValue = soLineItem.getUnitCost().multiply(soLineItem.getOrderQty());
				soLineItem.setItemValue(itemValue);
				soLineItems.add(soLineItem);
			}
			soLineItemRepository.saveAll(soLineItems);
			return true;
		} catch (Exception e) {
			logger.error("Error in saving so line item details {}", e.getMessage());
			return false;
		}

	}

	public SOLineItem saveLineItem(LineItemDto lineItemDto) {

		SOLineItem lineItem = mapper.map(lineItemDto, SOLineItem.class);
		lineItem.setCreatedDateTime(new Date());
		return soLineItemRepository.save(lineItem);

	}

	public SOLineItem getSoLineItemById(int id) {
		Optional<SOLineItem> soLineItemOptional = soLineItemRepository.findById(id);
		if (soLineItemOptional.isPresent()) {
			return soLineItemOptional.get();
		} else {
			throw new DataNotFoundException("SoLineItem not found by Id: " + id);
		}
	}

	public List<SOLineItem> getSoLineItemsBySoId(int soId) {
		
		return soLineItemRepository.getSoLineItemsBySoId(soId);
		
	}
}
