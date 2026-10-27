package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.transaction.entity.SupplyOrder;

@Repository
public interface SoDetRepository extends JpaRepository<SupplyOrder, Integer>{

	@Query(value = "SELECT COUNT(org.id) , org.id, org.organization_name " +
            "FROM tdms.supply_order AS so " +
            "LEFT JOIN mdms.sales_group AS sg ON so.sales_grp_id = sg.id " +
            "LEFT JOIN mdms.organization AS org ON sg.organization_id = org.id " +
            "WHERE TO_CHAR(so.order_date, 'MM-YYYY') = TO_CHAR(TO_DATE(:month || '-' || :year, 'MM-YYYY'), 'MM-YYYY') " +
            "GROUP BY org.id, org.organization_name " +
            "ORDER BY org.id ASC", nativeQuery = true)
	List<Object[]> getSoDet(@Param("month") Integer month, @Param("year") Integer year);
}
