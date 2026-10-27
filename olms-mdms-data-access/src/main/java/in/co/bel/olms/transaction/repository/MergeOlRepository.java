
package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.co.bel.olms.transaction.entity.MergeOl;

/**
 * @author Shridhar R
 * @since 19-01-2026
 */

public interface MergeOlRepository extends JpaRepository<MergeOl, Integer> {

	
	@Query(value = "SELECT\r\n"
			+ "    mo.merge_ol_no,\r\n"
			+ "    STRING_AGG(DISTINCT cast(olm.offer_letter_id AS TEXT), ',') AS offer_letter_ids,\r\n"
			+ "    STRING_AGG(DISTINCT ol.ol_number, ',') AS ol_numbers,\r\n"
//			+ "    ol.offer_date,\r\n"
			+ "    mo.created_at,\r\n"
			+ "    so.order_no,\r\n"
			+ "    TO_CHAR(so.order_date, 'DD-MM-YYYY') AS so_date,\r\n"
			+ "    ic.category,\r\n"
			+ "    mo.id,\r\n"
			+ "    STRING_AGG(DISTINCT lotr.serial_no, ',') AS serial_numbers,\r\n"
			+" so.organization_id,\r\n"
			+ "    org.organization_name "
			+ "FROM\r\n"
			+ "    tdms.merge_ol mo\r\n"
			+ "JOIN\r\n"
			+ "    tdms.merge_ol_mapping olm ON olm.merge_ol_no_id = mo.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.offer_letter ol ON ol.id = olm.offer_letter_id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.ol_line_item olt ON olt.offer_letter_id = ol.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.so_line_item sl ON sl.id = olt.so_line_item_id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.line_item_serial_no lotr ON lotr.so_line_item_id = sl.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.supply_order so ON so.id = sl.so_id\r\n"
			+ "JOIN\r\n"
			+ "    mdms.part_number pn ON pn.id = sl.part_number_id\r\n"
			+ "JOIN\r\n"
			+ "    mdms.item_category ic ON ic.id = pn.item_category_id\r\n"
			+"    LEFT JOIN\r\n"
			+ " mdms.organization org ON org.id=so.organization_id "
			+ "GROUP BY\r\n"
			+ "    mo.merge_ol_no,\r\n"
//			+ "    ol.offer_date,\r\n"
			+ "    mo.created_at,\r\n"
			+ "    so.order_no,\r\n"
			+ "    so.order_date,\r\n"
			+ "    ic.category,\r\n"
			+ "    mo.id,\r\n"
			+" so.organization_id,\r\n"
			+ "  org.organization_name "
			+ "ORDER BY\r\n"
			+ "    mo.created_at ;" , nativeQuery = true )			
	public List<Object[]> getAllMergedOls();
	
	@Query(value = "SELECT so.order_no, "
	        + "    so.order_date, "
	        + "    ol.ol_number, "
	        + "    ol.offer_date, "
	        + "    STRING_AGG(DISTINCT lis.serial_no, ',') AS serial_numbers, "
	        + "    lis.sub_slip_id, "
	        + "  pn.part_no, "
	        + "  pn.material_description, "
	        + " sl.registration_no "
	        + "FROM tdms.sub_slip_serial_no ssn "
	        + "LEFT JOIN tdms.warrant_details wd ON wd.id = ssn.warrant_details_id "
	        + "LEFT JOIN tdms.line_item_serial_no lis ON lis.id = wd.line_item_serial_no_id "
	        + "LEFT JOIN tdms.submission_slip ol ON ol.id = lis.sub_slip_id "
	        + "JOIN tdms.sslip_line_item olt ON olt.sub_slip_id = ol.id "
	        + "JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id "
	        + "JOIN tdms.supply_order so ON so.id = sl.so_id "
	        + "JOIN mdms.part_number pn ON pn.id = sl.part_number_id "
	        + "WHERE lis.sub_slip_id IN (:olIds) "
	        + "  AND ssn.final_status = 'pass' "
	        + "GROUP BY"
	        + "    so.order_no, "
	        + "    so.order_date, "
	        + "    pn.part_no, "
	        + "    ol.ol_number, "
	        + "    pn.material_description,"
	        + "    ol.offer_date, "
	        + "    lis.sub_slip_id, "
	        + "    sl.registration_no ", nativeQuery = true)
	      public List<Object[]> getAllOlDetails(@Param("olIds") List<Integer> olIds);
	
	@Query(value = "SELECT DISTINCT\r\n"
			+ "    mo.merge_ol_no,\r\n"
			+ "    olm.offer_letter_id,\r\n"
			+ "    ol.ol_number,\r\n"
			+ "    ol.offer_date,\r\n"
			+ "    mo.created_at,\r\n"
			+ "    so.order_no,\r\n"
			+ "    TO_CHAR(so.order_date, 'DD-MM-YYYY') AS so_date,\r\n"
			+ "    ic.category,\r\n"
			+ "    mo.id,\r\n"
			+ "    lotr.serial_no,\r\n"
			+"       pn.part_no, "
			+"    pn.material_description, "
			+ "  sl.registration_no "
			+ "FROM\r\n"
			+ "    tdms.merge_ol mo\r\n"
			+ "JOIN\r\n"
			+ "    tdms.merge_ol_mapping olm ON olm.merge_ol_no_id = mo.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.offer_letter ol ON ol.id = olm.offer_letter_id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.ol_line_item olt ON olt.offer_letter_id = ol.id\r\n"
//			+ "JOIN\r\n"
//			+ "    tdms.line_item_lots_register lotr ON lotr.ol_line_item_id = olt.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.so_line_item sl ON sl.id = olt.so_line_item_id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.line_item_serial_no lotr ON lotr.so_line_item_id = sl.id\r\n"
			+ "JOIN\r\n"
			+ "    tdms.supply_order so ON so.id = sl.so_id\r\n"
			+ "JOIN\r\n"
			+ "    mdms.part_number pn ON pn.id = sl.part_number_id\r\n"
			+ "JOIN\r\n"
			+ "    mdms.item_category ic ON ic.id = pn.item_category_id\r\n"
//			+ "JOIN\r\n"
//			+ "    mdms.item_category co ON co.id = sl.item_category_id\r\n"             
			+ " WHERE mo.id = :mergedOlId "
			+ "ORDER BY\r\n"
			+ "    mo.created_at \r\n" , nativeQuery = true )			
	public List<Object[]> getMergedOlDetById(@Param("mergedOlId") Integer mergedOlId);

	@Query(value = "SELECT count(*) FROM tdms.merge_submission_slip where department_id = :deptId GROUP BY department_id ", nativeQuery = true)
	Integer getLotCountRows(Integer deptId);

	@Query(value="select string_agg(lis.serial_no, ', ') from tdms.sub_slip_serial_no ssn "
		+ " left join tdms.warrant_details wd on wd.id = ssn.warrant_details_id "
		+ " left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id "
		+ " where lis.sub_slip_id in (:ssIds) and ssn.final_status <> 'pass' ", nativeQuery = true)
	public String checkAllTestPassed(List<Integer> ssIds);
}
