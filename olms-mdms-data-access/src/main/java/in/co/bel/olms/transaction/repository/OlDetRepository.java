package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.transaction.entity.OfferLetter;

@Repository
public interface OlDetRepository extends JpaRepository<OfferLetter, Integer>{

	@Query(value = "SELECT COUNT(org.id) , org.id, org.organization_name " +
            "FROM tdms.offer_letter AS ofl " +
            "LEFT JOIN tdms.sale_order AS sa ON ofl.sale_order_id = sa.id " +
            "LEFT JOIN tdms.supply_order AS so ON sa.supply_order_id = so.id " +
            "LEFT JOIN mdms.sales_group AS sg ON so.sales_grp_id = sg.id " +
            "LEFT JOIN mdms.organization AS org ON sg.organization_id = org.id " +
            "WHERE TO_CHAR(ofl.offer_date, 'MM-YYYY') = TO_CHAR(TO_DATE(:month || '-' || :year, 'MM-YYYY'), 'MM-YYYY') " +
            "GROUP BY org.id, org.organization_name " +
            "ORDER BY org.id ASC", nativeQuery = true)
	List<Object[]> getOlDet(@Param("month") Integer month, @Param("year") Integer year);
}

