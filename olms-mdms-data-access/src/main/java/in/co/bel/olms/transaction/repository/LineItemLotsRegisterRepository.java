package in.co.bel.olms.transaction.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.LineItemLots;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;

public interface LineItemLotsRegisterRepository extends JpaRepository<LineItemLotsRegister, Integer>{
	
	@Query(value = "SELECT * FROM tdms.line_item_lots_register where line_item_lot_id =:id order by id ASC", nativeQuery = true)
	List<LineItemLotsRegister> getRegisterNosByLineItemLotId(@Param("id") Integer id);
	
	@Query(value = "SELECT * FROM tdms.line_item_lots_register where ol_line_item_id =:id order by id ASC", nativeQuery = true)
	List<LineItemLotsRegister> getRegisterNosByOlLineItemId(@Param("id") Integer id);

	@Query(value = "SELECT reg.* FROM tdms.line_item_lots_register reg " +
			"left join tdms.ol_line_item olli on olli.id = reg.ol_line_item_id " +
			"where olli.offer_letter_id =:id order by reg.id ASC", nativeQuery = true)
	List<LineItemLotsRegister> getRegisterNosByOlId(@Param("id") Integer id);

	@Query(value = "select reg.* from tdms.line_item_lots_register reg " +
			"left join ate.fai fai on fai.ol_line_item_id = reg.ol_line_item_id  " +
			"where (fai.id is null and reg.ol_line_item_id = :olLineItemId) or " +
			"(reg.ol_line_item_id = :olLineItemId and fai.id is not null and reg.status = 'rfr') ", nativeQuery = true)
	List<LineItemLotsRegister> getFAIRegisterNosByOlLineItemId(Integer olLineItemId);
	
	@Query(value = "SELECT r.* " +
	        "FROM tdms.line_item_lots l " +
	        "JOIN tdms.line_item_lots_register r ON l.id = r.line_item_lot_id " +
	        "WHERE l.so_line_item_id = :soLiId " +
	        "  AND (l.financial_year = :financialYear AND r.status = 'pending' OR " +
	        "       (l.financial_year < :financialYear AND r.status = 'pending')) " +
	        "ORDER BY r.id ASC", nativeQuery = true)
	List<LineItemLotsRegister> getRegisterNosBySoLiIdAndFinancialYear(@Param("soLiId") Integer soLiId, @Param("financialYear") String financialYear);
	
	
	@Query(value = "SELECT serial_no FROM tdms.line_item_serial_no WHERE so_line_item_id = :soLiId ORDER BY id ASC", nativeQuery = true)
    List<LineItemLotsRegister> getRegisterNosBySoLiId(@Param("soLiId") Integer soLiId); 
	
	@Query(value = "SELECT DISTINCT l.financial_year FROM tdms.line_item_lots l " +
	        "WHERE l.so_line_item_id = :soLiId " +
	        "ORDER BY l.financial_year ASC", nativeQuery = true)
    List<String> getFinancialYearsForSoLiId(@Param("soLiId") Integer soLiId); 
	
	// send the registers that are matching the "pending" status and status sent via Param. 
	@Query(value = "SELECT l.financial_year, r.* " +
	        " FROM tdms.line_item_lots l " +
	        " JOIN tdms.line_item_lots_register r ON l.id = r.line_item_lot_id " +
	        " WHERE l.so_line_item_id = :soLiId " +
	        " AND (l.financial_year = :financialYear AND (r.status = 'pending' OR r.status = :status) OR " +
	        "       (l.financial_year < :financialYear AND (r.status = 'pending' OR r.status = :status))) " +
	        " ORDER BY l.financial_year DESC", nativeQuery = true)
	List<Object[]> getRegistersBySoLiIdAndFinancialYear(@Param("soLiId") Integer soLiId, @Param("financialYear") String financialYear, @Param("status") String status);
	
	@Query(value = "SELECT COUNT(*) FROM tdms.line_item_lots_register where line_item_lot_id = :liId", nativeQuery = true)
	Integer findCountById(@Param("liId") Integer liId);

	@Query(value=" select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no  from tdms.line_item_lots_register li "
			+ " left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id "
			+ " left join tdms.offer_letter ol on ol.id = oli.offer_letter_id "
			+ " left join tdms.so_line_item sli on sli.id = oli.so_line_item_id "
			+ " where ol.id = :olId and li.is_sample = true ", nativeQuery = true)
	List<Object[]> getSelectedRegisterNosForOl(@Param("olId") Integer olId);
	
	@Query(value=" select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no  from tdms.line_item_lots_register li "
			+ " left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id "
			+ " left join tdms.offer_letter ol on ol.id = oli.offer_letter_id "
			+ " left join tdms.so_line_item sli on sli.id = oli.so_line_item_id "
			+ " where ol.id = :olId and li.is_sample = false or null", nativeQuery = true)
	List<Object[]> getAllRegisterNosForOl(@Param("olId") Integer olId);

	@Modifying
	@Transactional
	@Query(value="update tdms.line_item_lots_register set ol_line_item_id=null where id=:id",
	           nativeQuery = true)
	void setolLineItemIdAsnull(@Param("id") Integer id);
	
	@Query(value=" select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no  from tdms.line_item_lots_register li "
			+ " left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id "
			+ " left join tdms.offer_letter ol on ol.id = oli.offer_letter_id "
			+ " left join tdms.so_line_item sli on sli.id = oli.so_line_item_id "
			+ " where ol.id = :olId and li.status = 'accepted' ", nativeQuery = true)
	List<Object[]> getAllAcceptedRegisterNosForOl(@Param("olId") Integer olId);

	
	@Query(value = " select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no,sli.part_number_id ,ppm.product_id ,li.status as litemsts,li.qa_status,os.status as ossts"
            + "                  from tdms.line_item_lots_register li "
            + "                     left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id  "
            + "                     left join tdms.offer_letter ol on ol.id = oli.offer_letter_id  "
            + "                     left join tdms.so_line_item sli on sli.id = oli.so_line_item_id  "
            + "                     left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id "
            + "                   left join mdms.ol_status os on os.id = li.qa_status     "
                + "             where ol.id = :olId and li.is_sample = true ", nativeQuery = true)
        List<Object[]> getProductAndRegNosByOlId(@Param("olId") Integer olId);
	
        @Query(value = " select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no,sli.part_number_id ,ppm.product_id ,li.status as litemsts,li.qa_status,os.status as ossts"
                + "                  from tdms.line_item_lots_register li "
                + "                     left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id  "
                + "                     left join tdms.offer_letter ol on ol.id = oli.offer_letter_id  "
                + "                     left join tdms.so_line_item sli on sli.id = oli.so_line_item_id  "
                + "                     left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id "
                + "                   left join mdms.ol_status os on os.id = li.qa_status     "
                    + "             where ol.id = :olId and li.is_sample is null ", nativeQuery = true)
            List<Object[]> getNonSampleProductAndRegNosByOlId(@Param("olId") Integer olId);
            @Query(value = " select li.id, li.ol_line_item_id, li.register_no, sli.line_item_no,sli.part_number_id ,ppm.product_id ,li.status as litemsts,li.qa_status,os.status as ossts"
                    + "                  from tdms.line_item_lots_register li "
                    + "                     left join tdms.ol_line_item oli on oli.id = li.ol_line_item_id  "
                    + "                     left join tdms.offer_letter ol on ol.id = oli.offer_letter_id  "
                    + "                     left join tdms.so_line_item sli on sli.id = oli.so_line_item_id  "
                    + "                     left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id "
                    + "                   left join mdms.ol_status os on os.id = li.qa_status     "
                        + "             where ol.id = :olId and li.is_faisample = true ", nativeQuery = true)
                List<Object[]> getFaiSampleProductAndRegNosByOlId(@Param("olId") Integer olId);

			 @Query(value="SELECT l.so_line_item_id FROM tdms.line_item_lots l join tdms.line_item_lots_register r on r.line_item_lot_id=l.id  WHERE l.id = :lineItemLotId group by l.so_line_item_id", nativeQuery = true)
			    Integer findSoLineItemIdByLineItemLotId(@Param("lineItemLotId") LineItemLots lineItemLotId);
     
			 @Query(value="Select register_no from tdms.line_item_lots_register where id=:id ", nativeQuery = true)
			 String findRegisterNo(int id);

}
