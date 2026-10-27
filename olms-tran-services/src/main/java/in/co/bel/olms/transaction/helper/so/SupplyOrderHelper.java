package in.co.bel.olms.transaction.helper.so;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import in.co.bel.olms.transaction.entity.Project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import in.co.bel.olms.transaction.entity.SupplyOrder;


/**
 * 
 * @author Manasa B
 * @since 28-03-2023
 */
@Component
public class SupplyOrderHelper {
	
	private final Logger logger =LoggerFactory.getLogger(SupplyOrderHelper.class);

	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param supplyOrder
	 * @return
	 */
	public String getOpa(SupplyOrder supplyOrder) {
		try {
			if (null != supplyOrder.getIndenter()) {
				return supplyOrder.getIndenter().getName();
			} else {
				return "";
			}
		} catch (Exception e1) {
			return "";
		}
	}

	
	
	/**
	 * 
	 * @param supplyOrder
	 * @return
	 */
	public String getDateOfReceipt(SupplyOrder supplyOrder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (supplyOrder.getReceiptDate() != null) {
			try {
				return dateFormat.format(supplyOrder.getReceiptDate());
			} catch (Exception e) {
				logger.error("Error occurred while getting DateOfReceipt: ", e);
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param supplyOrder
	 * @return
	 */
	public String getDeliveryDate(SupplyOrder supplyOrder) {
		if (supplyOrder.getDd() != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return dateFormat.format(supplyOrder.getDd());
			} catch (Exception e) {
				logger.error("Error occurred while getting DeliveryDate: ", e);
				return "";
			}
		} else {
			return "";
		}
	}
	
	
	/**
	 * 
	 * @param supplyOrder
	 * @return
	 */
	public String getProjectName(SupplyOrder supplyOrder) {
		try {
			// Changed after multiple projects for one PO/SO
			List<Project> projects = supplyOrder.getProjects();
			String allProjectNames = "";
			if (!projects.isEmpty()) {

				allProjectNames = projects.stream()
						.map(Project::getProjectName)
						.collect(Collectors.joining(","));
			}
			return allProjectNames;
		} catch (Exception e1) {
			return "";
		}
	}
}
