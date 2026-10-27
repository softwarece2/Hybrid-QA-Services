package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.MergeSubSlip;

public interface MergeSubSlipRepository extends JpaRepository<MergeSubSlip, Integer>{

	
	// Insp auth to be taken from the line item not from the so
  @Query(value = " select " +
      " mo.merge_ol_no, " +
      " mo.created_at, " +
      " so.order_no, " +
      " TO_CHAR(so.order_date, 'DD-MM-YYYY') as so_date, " +
      " ic.category, " +
      " mo.id , " +
      " o.id as orgid, " +
      " o.organization_name, " +
//      " sl.id as soli, " +
      " pn.part_no, " +
      " s.id as statusId, " +
      " s.status " +
      "from tdms.merge_submission_slip mo " +
      "join tdms.merge_sslip_mapping olm on olm.merge_sslip_id = mo.id " +
      "join tdms.submission_slip ol on ol.id = olm.sslip_id " +
      "join tdms.sslip_line_item olt on olt.sub_slip_id = ol.id " +
      "join tdms.so_line_item sl on sl.id = olt.so_line_item_id " +
      "join mdms.organization o on o.id = sl.insp_auth " +
      "join tdms.supply_order so on so.id = sl.so_id " +
      "join mdms.part_number pn on pn.id = sl.part_number_id " +
      "join mdms.item_category ic on ic.id = pn.item_category_id " +
      "join tdms.sub_slip_allotment ssa on ssa.sub_slip_id = ol.id and ssa.alloted_to = :empId " +
      "left join mdms.status s on s.id = mo.status " +
      "where mo.status IN :statusIds " +
      "group by  " +
      " mo.merge_ol_no,   " +
      " mo.created_at,    " +
      " so.order_no,  " +
      " so.order_date, " +
      " ic.category,    " +
      " mo.id , " +
      " o.id , " +
      " o.organization_name , " +
//      " sl.id, " +
      " pn.part_no," +
      " s.id, " +
      " s.status " +
      " order by " +
      " mo.id desc;", nativeQuery = true)
  public List<Object[]> getAllMergedOls(int empId, List<Integer> statusIds);
  
  
  @Query(value = " select distinct " +
      " mo.merge_ol_no, " +
      " mo.created_at, " +
      " so.order_no, " +
      " TO_CHAR(so.order_date, 'DD-MM-YYYY') as so_date, " +
      " ic.category, " +
      " mo.id , " +
      " o.id as orgid, " +
      " o.organization_name, " +
//      " sl.id as soli, " +
      " pn.part_no, " +
      " s.id as statusId, " +
      " s.status " +
      "from tdms.merge_submission_slip mo " +
      "join tdms.lot_allotment lom on lom.merge_sslip_id = mo.id and lom.alloted_to = :empId "+
      "join tdms.merge_sslip_mapping olm on olm.merge_sslip_id = mo.id " +
      "join tdms.submission_slip ol on ol.id = olm.sslip_id " +
      "join tdms.sslip_line_item olt on olt.sub_slip_id = ol.id " +
      "join tdms.so_line_item sl on sl.id = olt.so_line_item_id " +
      "join mdms.organization o on o.id = sl.insp_auth " +
      "join tdms.supply_order so on so.id = sl.so_id " +
      "join mdms.part_number pn on pn.id = sl.part_number_id " +
      "join mdms.item_category ic on ic.id = pn.item_category_id " +
      "left join mdms.status s on s.id = mo.status " +
      "where mo.status IN :statusIds " +
      "group by  " +
      " mo.merge_ol_no,   " +
      " mo.created_at,    " +
      " so.order_no,  " +
      " so.order_date, " +
      " ic.category,    " +
      " mo.id , " +
      " o.id, " +
      " o.organization_name , " +
//      " sl.id, " +
      " pn.part_no," +
      " s.id, " +
      " s.status " +
      "order by " +
      "mo.id desc;", nativeQuery = true)
  public List<Object[]> getAllMergedOlsDOA(@Param("statusIds") List<Integer> statusIds, @Param("empId") Integer empId);
	
	@Query(value = "SELECT so.order_no, so.order_date, ol.ol_number, ol.offer_date, lotr.serial_no " +
	        "FROM tdms.submission_slip ol " +
	        "JOIN tdms.ol_line_item olt ON olt.offer_letter_id = ol.id " +
//	        "JOIN tdms.line_item_lots_register lotr ON lotr.ol_line_item_id = olt.id " +
	        "JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id " +
	        "JOIN tdms.line_item_serial_no lotr ON lotr.so_line_item_id = sl.id " +
	        "JOIN tdms.supply_order so ON so.id = sl.so_id " +
	        "WHERE ol.id IN (:olIds) AND lotr.ol_id = ol.id ", nativeQuery = true) 
	public List<Object[]> getAllOlDetails(@Param("olIds") List<Integer> olIds);

	@Query(value = "SELECT DISTINCT   mo.merge_ol_no,  olm.sslip_id,    ol.ol_number, "
            + "    ol.offer_date,    mo.created_at,    so.order_no,     TO_CHAR(so.order_date, 'DD-MM-YYYY') AS so_date, "
            + "    ic.category,     mo.id,    lis.serial_no , pn.part_no, pn.material_description, sl.registration_no "
            + "FROM  tdms.merge_submission_slip mo "
            + "JOIN  tdms.merge_sslip_mapping olm ON olm.merge_sslip_id = mo.id "
            + " JOIN tdms.sub_slip_serial_no  sss on sss.sub_slip_id=olm.sslip_id "
            + " JOIN tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
            + " JOIN tdms.line_item_serial_no lis ON lis.id = wd.line_item_serial_no_id "
            + " JOIN  tdms.submission_slip ol ON ol.id = olm.sslip_id "
            + "JOIN tdms.sslip_line_item olt ON olt.sub_slip_id = ol.id  "
            + "JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id  "
            + "JOIN tdms.line_item_serial_no lotr ON lotr.so_line_item_id = sl.id  "
            + "JOIN tdms.supply_order so ON so.id = sl.so_id  "
            + "JOIN  mdms.part_number pn ON pn.id = sl.part_number_id  "
            + "JOIN mdms.item_category ic ON ic.id = pn.item_category_id "      
            + " WHERE mo.id = :mergedOlId and sss.final_status='pass' "
            + " ORDER BY   mo.created_at  " , nativeQuery = true )   
          public List<Object[]> getMergedOlDetById(@Param("mergedOlId") Integer mergedOlId);
	
          @Query(value = "select Distinct(lis.serial_no) , sli.registration_no, sli.order_qty "
                  + "from tdms.merge_submission_slip mss"
                  + " left join tdms.merge_sslip_mapping mssm on mssm.merge_sslip_id=mss.id "
                  + "left join tdms.sub_slip_serial_no  sss on sss.sub_slip_id=mssm.sslip_id "
                  + "left join tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
                  + "left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id "
                  + " left join tdms.so_line_item sli on sli.id = lis.so_line_item_id"
                  + " where sss.final_status='pass' and mssm.merge_sslip_id= :mergedOlId ORDER BY lis.serial_no", nativeQuery = true)
           public List<Object[]> getMergedOlPassedRegNosById(@Param("mergedOlId") Integer mergedOlId);
           
           @Query(value = "select Distinct(lis.serial_no) , sli.registration_no, sli.order_qty "
                   + "from tdms.internal_inote int "
                   + " left join tdms.merge_sslip_mapping mssm on mssm.merge_sslip_id = int.merge_sslip_id  "
                   + "left join tdms.sub_slip_serial_no  sss on sss.sub_slip_id=mssm.sslip_id "
                   + "left join tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
                   + "left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id "
                   + " left join tdms.so_line_item sli on sli.id = lis.so_line_item_id"
                   + " where sss.final_status='pass' and int.id= :internalInoteId ORDER BY lis.serial_no", nativeQuery = true)
            public List<Object[]> getPassedRegNosByInternalInoteId(@Param("internalInoteId") Integer internalInoteId);

	
	@Query(value = "select Distinct(lis.serial_no) , sli.registration_no, sli.order_qty 	"
			+ "from tdms.merge_submission_slip mss "
			+ "left join tdms.merge_sslip_mapping mssm on mssm.merge_sslip_id=mss.id "
			+ "left join tdms.sub_slip_serial_no  sss on sss.sub_slip_id=mssm.sslip_id "
			+ "left join tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
			+ "left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id "
			+ " left join tdms.so_line_item sli on sli.id = lis.so_line_item_id"
			+ " where sss.final_status='fail' and mssm.merge_sslip_id= :mergedOlId ORDER BY lis.serial_no", nativeQuery = true)
	public List<Object[]> getMergedOlfailedRegNosById(@Param("mergedOlId") Integer mergedOlId);

	@Query(value = "select distinct sss.id, lis.serial_no, sli.id as sslipLiId, ir.round_id , ir.proudct_id, soli.registration_no  from tdms.merge_submission_slip mss "
			+ "join tdms.merge_sslip_mapping msm on msm.merge_sslip_id = mss.id "
			+ "JOIN tdms.sub_slip_serial_no  sss on sss.sub_slip_id=msm.sslip_id "
			+ "JOIN tdms.warrant_details wd ON wd.id = sss.warrant_details_id "
			+ "JOIN tdms.category ct ON ct.id = wd.category_id "
			+ "JOIN tdms.so_line_item soli ON soli.id = ct.so_line_item_id "
			+ "JOIN tdms.line_item_serial_no lis ON lis.id = wd.line_item_serial_no_id "
			+ "join tdms.submission_slip ss on ss.id = msm.sslip_id "
			+ "join tdms.sslip_line_item sli on sli.sub_slip_id = ss.id "
			+ "join ate.insp_round ir on ir.sslip_serial_no_id = sss.id "
			+ "where msm.merge_sslip_id = :mergeSslipId AND sss.final_status='pass'", nativeQuery = true)
	public List<Object[]> getRegsByMergeSslipId(Integer mergeSslipId);

    @Query(value = "select sslip_id from tdms.merge_sslip_mapping where merge_sslip_id= :lotId ", nativeQuery = true)
	public List<Integer> findsubSlipsByMergedId(Integer lotId);
}
