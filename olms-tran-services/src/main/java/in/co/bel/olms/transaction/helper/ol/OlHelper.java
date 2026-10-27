package in.co.bel.olms.transaction.helper.ol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.transaction.entity.OfferLetter;


/**
 * 
 * @author Manasa B
 * @since 12-04-2023
 *
 */
@Component
public class OlHelper {

	private final Logger logger =LoggerFactory.getLogger(OlHelper.class);
	
	public boolean isReturned(OfferLetter offerLetter) {
		try {
			if (null != offerLetter.getReturnOfferLetter()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error checking return status for OfferLetter: ", e);
			return false;
		}
	}

	public boolean isScrutinyDone(OfferLetter offerLetter) {

		try {
			int orgTypeId = offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup() .getOrganization().getOrganizationType().getId();
			switch (orgTypeId) {			
			case ApplicationConstants.OrgType.ID_IG:
				if (null != offerLetter.getInspOlIg()) {
					if(null != offerLetter.getInspOlIg().getSubmittedDate() )
					{
						return true;
					}
					else
					{
						return false;
					}
				} else {
					return false;
				}
			default:
				return false;
			}

		} catch (Exception e) {
			logger.error("Error Occured during checking scrutiny status for OfferLetter: {}", e.getMessage());
			return false;
		}

	}
}
