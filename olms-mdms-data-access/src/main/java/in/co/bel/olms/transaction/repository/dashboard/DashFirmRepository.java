package in.co.bel.olms.transaction.repository.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.SupplyOrder;

public interface DashFirmRepository extends JpaRepository<SupplyOrder, Integer>{

	@Query(value = "SELECT count(OL.ID), O.id, O.organization_name, O.logo FROM TDMS.OFFER_LETTER OL LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id LEFT JOIN mdms.organization O ON SG.organization_id = O.id LEFT JOIN mdms.ol_status ols ON OL.ol_status_id = ols.id where O.on_boarded = 5 GROUP BY O.id, O.organization_name, O.logo ORDER BY O.organization_name ASC", nativeQuery = true)
	List<Object[]> getAllDashFirms();
	
	@Query(value = "SELECT O.id AS organization_id,PR.project_name, PO.value AS project_value "
			+ "FROM  mdms.organization O "
			+ "JOIN  mdms.sales_group SG ON SG.organization_id = O.id "
			+ "JOIN  tdms.supply_order PO ON PO.sales_grp_id = SG.id "
			+ "JOIN  tdms.project PR ON PO.ID =  PR.supply_order_id "
			+ "WHERE "
			+ "    PO.value = (SELECT MAX(PO2.value) "
			+ "        FROM TDMS.SUPPLY_ORDER PO2 "
			+ "        JOIN mdms.sales_group SG2 ON PO2.sales_grp_id = SG2.id "
			+ "        WHERE SG2.organization_id = O.id )"
			+ "ORDER BY  O.organization_name ASC;", nativeQuery = true)
	List<Object[]> getProjectDetailsValues();
}
