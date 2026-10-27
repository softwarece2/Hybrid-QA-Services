package in.co.bel.olms.transaction.helper.fcm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import in.co.bel.olms.transaction.dto.FcmDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.so.SupplyOrderHelper;

import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 * @since 28-03-2023
 */
@Component
@RequiredArgsConstructor
public class FcmHelper {

	private final SupplyOrderHelper supplyOrderHelper;
	private static final Logger logger = LoggerFactory.getLogger(FcmHelper.class);
	/**
	 * 
	 * @param fcmDto
	 * @param supplyOrder
	 * @return
	 */
	public FcmDto getSoDetails(FcmDto fcmDto, SupplyOrder supplyOrder)
	{
		fcmDto.setSupplyOrderId(supplyOrder.getId());
		fcmDto.setSoOrderNo(supplyOrder.getOrderNo());
		fcmDto.setSoDate(supplyOrder.getOrderDate().toString());
		try {
			fcmDto.setOpa(supplyOrderHelper.getOpa(supplyOrder));
		} catch (Exception e1) {
			fcmDto.setOpa("");
		}
		fcmDto.setItemDescription(supplyOrder.getDescription());			
		
		fcmDto.setNoOfLots(String.valueOf(supplyOrder.getLotNo())); 
		try {
			fcmDto.setDateOfReceipt(supplyOrderHelper.getDateOfReceipt(supplyOrder));
		} catch (Exception e) {
			fcmDto.setDateOfReceipt("");
			logger.error("error: {}",e.getMessage());
		}
		if (supplyOrder.getNoOfItems() != null) {
			fcmDto.setNoOfItems(String.valueOf(supplyOrder.getNoOfItems()));
		} else {
			fcmDto.setNoOfItems("");
		}
		
		try {
			fcmDto.setDeliveryDate(supplyOrderHelper.getDeliveryDate(supplyOrder) );
		} catch (Exception e) {
			logger.error("error: {}",e.getMessage());
		}
		
		return fcmDto;
	}
	
}
