//
//package in.co.bel.olms.transaction.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import in.co.bel.olms.transaction.entity.MergeINote;
//
///**
// * @author Shridhar R
// * @since 02-01-2026
// */
//
//public interface MergeINoteRepository extends JpaRepository<MergeINote, Integer> {
//
//	
//	@Query(value = "SELECT DISTINCT mi.merge_inote_no , inm.internal_inote_id, itn.offer_letter_no , itn.offer_letter_date , itn.inote_date, mi.created_at, " 
//			+ " itn.supply_order_no, TO_CHAR(itn.supply_order_date, 'DD-MM-YYYY') AS so_date , co.category , mi.id , lotr.register_no  "
//			+ " FROM tdms.merge_inote mi "
//			+ " JOIN tdms.merge_inote_ol_mapping inm on inm.merge_inote_no_id = mi.id   "
//			+ " JOIN tdms.internal_inote itn ON itn.id = inm.internal_inote_id "
//			+ " JOIN tdms.ol_line_item olt ON olt.offer_letter_id = itn.offer_letter_id  "
//			+ " JOIN tdms.line_item_lots_register lotr ON lotr.ol_line_item_id = olt.id "
//			+ " JOIN tdms.internal_inote_line_item inol ON inol.internal_inote_id = itn.id "
//			+ " JOIN tdms.ol_line_item ol ON inol.ol_line_item_id = ol.id "
//			+ " JOIN tdms.so_line_item sl ON sl.id = ol.so_line_item_id "
//			+ " JOIN mdms.item_category co ON co.id = sl.item_category_id "
//			+ " ORDER BY mi.created_at  ", nativeQuery = true )
//	public List<Object[]> getAllMergedINotes();
//	
//	
//	@Query(value = "SELECT itn.supply_order_no ,itn.supply_order_date, itn.offer_letter_no , itn.offer_letter_date , itn.inote_date, itn.inote_no, lotr.register_no "
//			+ "		    FROM tdms.internal_inote itn"
//			+ "			 JOIN tdms.ol_line_item olt ON olt.offer_letter_id = itn.offer_letter_id "
//			+ "			 JOIN tdms.line_item_lots_register lotr ON lotr.ol_line_item_id = olt.id "
//			+ "			 where itn.id IN (:inoteIds)", nativeQuery = true)
//    public List<Object[]> getAllINoteDetails(@Param("inoteIds") List<Integer> inoteIds);
//
//    @Query(value = "SELECT DISTINCT mi.merge_inote_no , inm.internal_inote_id, itn.offer_letter_no , itn.offer_letter_date , itn.inote_date, mi.created_at, " 
//			+ " itn.supply_order_no, TO_CHAR(itn.supply_order_date, 'DD-MM-YYYY') AS so_date , co.category , mi.id , lotr.register_no  "
//			+ " FROM tdms.merge_inote mi "
//			+ " JOIN tdms.merge_inote_ol_mapping inm on inm.merge_inote_no_id = mi.id   "
//			+ " JOIN tdms.internal_inote itn ON itn.id = inm.internal_inote_id "
//			+ " JOIN tdms.ol_line_item olt ON olt.offer_letter_id = itn.offer_letter_id  "
//			+ " JOIN tdms.line_item_lots_register lotr ON lotr.ol_line_item_id = olt.id "
//			+ " JOIN tdms.internal_inote_line_item inol ON inol.internal_inote_id = itn.id "
//			+ " JOIN tdms.ol_line_item ol ON inol.ol_line_item_id = ol.id "
//			+ " JOIN tdms.so_line_item sl ON sl.id = ol.so_line_item_id "
//			+ " JOIN mdms.item_category co ON co.id = sl.item_category_id"
//			+ " WHERE mi.id = :mergedINoteId "
//			+ " ORDER BY mi.created_at  ", nativeQuery = true )
//	public List<Object[]> getMergedINoteDetById(@Param("mergedINoteId") Integer mergedINoteId);
//}
