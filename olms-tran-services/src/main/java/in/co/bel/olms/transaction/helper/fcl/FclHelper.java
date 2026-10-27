package in.co.bel.olms.transaction.helper.fcl;

import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.transaction.dto.FclDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.so.SupplyOrderHelper;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 27-03-2023
 */
@Component
@RequiredArgsConstructor
public class FclHelper {

	private final SupplyOrderHelper supplyOrderHelper;
	
	
	public FclDto getSoDetails(FclDto fclDto, SupplyOrder supplyOrder)
	{
		fclDto.setSupplyOrderId(supplyOrder.getId());
		fclDto.setSoDate(supplyOrder.getOrderDate().toString());
		fclDto.setFileNo(supplyOrder.getFileNo());
		SalesGroup salesGroup = supplyOrder.getSalesGroup();
		if (salesGroup != null) {
			fclDto.setSalesGrpName(salesGroup.getName());
		}
		try {
			fclDto.setOpa(supplyOrderHelper.getOpa(supplyOrder));
		} catch (Exception e1) {
			fclDto.setOpa("");
		}
		fclDto.setProjectName(  supplyOrderHelper.getProjectName(supplyOrder));
		return fclDto;
	}
	
}
