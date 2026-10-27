package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.transaction.entity.Qcc;

@Repository
public interface QccRepository extends JpaRepository<Qcc, Integer> {
	
    @Query(value = "SELECT * FROM TDMS.QCC qcc WHERE merged_sslip_id = :mergedSubSlipId " , nativeQuery = true)
	Optional<Qcc> findByMergedSubSlipId(@Param("mergedSubSlipId")Integer mergedSubSlipId);
    
    
	@Query(value = "SELECT\r\n"
			+ "    pn.cat_part_no,\r\n"
			+ "    pn.nsn_part_no,\r\n"
			+ "    ic.category,\r\n"
			+ "    sli.registration_no,\r\n"
			+ "    string_agg(\r\n"
			+ "        DISTINCT to_char(sli.order_type_date, 'DD-MM-YYYY'),\r\n"
			+ "        ', '\r\n"
			+ "    ) AS order_type_date, \r\n"
			+ "   pn.part_no "
			+ "FROM TDMS.QCC qcc\r\n"
			+ "LEFT JOIN tdms.merge_submission_slip mss\r\n"
			+ "    ON mss.id = qcc.merged_sslip_id\r\n"
			+ "LEFT JOIN tdms.merge_sslip_mapping msm\r\n"
			+ "    ON msm.merge_sslip_id = mss.id\r\n"
			+ "LEFT JOIN tdms.sub_slip_serial_no sss\r\n"
			+ "    ON sss.sub_slip_id = msm.sslip_id\r\n"
			+ "LEFT JOIN tdms.warrant_details wd\r\n"
			+ "    ON wd.id = sss.warrant_details_id\r\n"
			+ "LEFT JOIN tdms.category c\r\n"
			+ "    ON c.id = wd.category_id\r\n"
			+ "LEFT JOIN tdms.so_line_item sli\r\n"
			+ "    ON sli.id = c.so_line_item_id\r\n"
			+ "LEFT JOIN mdms.part_number pn\r\n"
			+ "    ON pn.id = sli.part_number_id\r\n"
			+ "LEFT JOIN mdms.item_category ic\r\n"
			+ "    ON ic.id = pn.item_category_id\r\n"
			+ "WHERE merged_sslip_id = :mergedSubSlipId\r\n"
			+ "GROUP BY\r\n"
			+ "    pn.cat_part_no,\r\n"
			+ "    pn.nsn_part_no,\r\n"
			+ "    ic.category,\r\n"
			+ "    sli.registration_no,"
			+ "    pn.part_no;", nativeQuery = true)
	List<Object[]> findSoLineItemDetailsFromQccMergeSslipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);
    
    

  @Query(value = "select "
      + " string_agg(distinct c.category_name, ', ') as category_name, "
      + " string_agg(distinct pn.material_description, ', ') as material_description, "
      + " string_agg(distinct pn.drawing_no, ', ') as drawing_no, "
      + " string_agg(distinct so.sale_order_no, ', ') as work_order_no, "
      + " COUNT(distinct(lis.serial_no)) AS total_sum, "
      + " string_agg(distinct po.order_no, ', ') as order_no, "
      + " string_agg(distinct lis.serial_no, ', ') as reg_nos, "
      + " string_agg(distinct sli.order_type_no, ', ') as order_type, "
      + " COALESCE(To_char(MAX(po.order_date), 'dd-mm-yyyy'), '') AS offer_date, "
      + " string_agg(distinct pn.cat_part_no, ', ') as cat_part_no ,"
      + " string_agg(distinct pn.nsn_part_no, ', ') as nsn_part_no  ,"
      + " string_agg(distinct ic.category, ', ') as category , "
      + " string_agg(distinct to_char(sli.order_type_date, 'DD-MM-YYYY'), ', ') as order_type_date ,"
      + " string_agg(distinct pn.part_no, ', ') as part_number, "
      + " string_agg(distinct CAST(sli.registration_no AS text), ', ') as reg_no_type "
      + " from tdms.merge_submission_slip mss "
      + " left join tdms.merge_sslip_mapping msm on msm.merge_sslip_id = mss.id "
      + " JOIN tdms.sub_slip_serial_no sss on sss.sub_slip_id=msm.sslip_id "
      + " JOIN tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
      + " JOIN tdms.line_item_serial_no lis ON lis.id = wd.line_item_serial_no_id "
      + " left join tdms.submission_slip ss on ss.id = msm.sslip_id "
      + " left join tdms.category c on c.id = ss.warrant_id "
      + " left join tdms.so_line_item sli on sli.id = c.so_line_item_id "
      + " left join tdms.sale_order so on sli.sale_order_id = so.id "
      + " left join tdms.line_item_serial_no lisn on lisn.so_line_item_id = sli.id "
      + " left join tdms.supply_order po on po.id = sli.so_id "
      + " left join mdms.part_number pn on pn.id = sli.part_number_id "
      +"  left join mdms.item_category ic on ic.id = pn.item_category_id "
      + " where msm.merge_sslip_id = :mergedSubSlipId AND sss.final_status='pass' "
      + " group by msm.merge_sslip_id ", nativeQuery = true)
  List<Object[]> findQccByMergeSSlipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);

  @Query(value = "select distinct  sli.order_type_no \r\n" + "     from tdms.merge_submission_slip mss  \r\n"
		   + "     left join tdms.merge_sslip_mapping msm on msm.merge_sslip_id = mss.id  \r\n"
		   + "     left join tdms.submission_slip ss on ss.id = msm.sslip_id  \r\n"
		   + "     left join tdms.category c on c.id = ss.warrant_id  \r\n"
		   + "     left join tdms.so_line_item sli on sli.id = c.so_line_item_id  \r\n"
		   + "     where msm.merge_sslip_id = :mergedSubSlipId \r\n"
		   + "     group by  sli.order_type_no", nativeQuery = true)
	String findIfdByMergeSSlipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);
}
