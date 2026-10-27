package in.co.bel.olms.transaction.repository;

import in.co.bel.olms.transaction.entity.SurpriseCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurpriseCheckRepository extends JpaRepository<SurpriseCheck, Integer> {

    @Query(value = "SELECT sc.inspection_date," +
            "       sg.NAME as firm_name," +
            "       sli.line_item_no," +
            "       sli.description," +
            "       so.order_no," +
            "       sc.individual_responsible," +
            "       sc.test_carried," +
            "       sc.cross_verify_by," +
            "       sc.cross_verify_date," +
            "       sc.remarks " +
            "FROM   tdms.surprise_check sc " +
            "       LEFT JOIN tdms.so_line_item sli " +
            "              ON sc.line_item_id = sli .id " +
            "       LEFT JOIN tdms.supply_order so " +
            "              ON sli.so_id = so .id " +
            "       LEFT JOIN mdms.sales_group sg " +
            "              ON so.sales_grp_id = sg.id " +
            "WHERE  so.organization_id = :orgId " +
            "       AND To_timestamp(Cast(sc.inspection_date AS TEXT), 'YYYY-MM-DD') >= " +
            "           To_timestamp(:fromDate, 'YYYY-MM-DD') " +
            "       AND To_timestamp(Cast(sc.inspection_date AS TEXT), 'YYYY-MM-DD') <= " +
            "           To_timestamp(:toDate, 'YYYY-MM-DD')", nativeQuery = true)
    List<Object[]> getSurpriseCheckRegister(int orgId, String fromDate, String toDate);
}
