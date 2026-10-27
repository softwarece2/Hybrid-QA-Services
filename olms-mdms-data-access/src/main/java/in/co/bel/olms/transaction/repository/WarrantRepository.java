package in.co.bel.olms.transaction.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.SaleOrder;

import javax.transaction.Transactional;

public interface WarrantRepository extends JpaRepository<Category, Integer>{

	
	@Query(value = "SELECT lis.id, lis.serial_no FROM tdms.warrant_details wd "
			+ "left join tdms.line_item_serial_no lis  on  lis.id=wd.line_item_serial_no_id "
			+ "where category_id IN (:warrantIds) ", nativeQuery = true)
	List<SaleOrder> findRegNoByIds(@Param("warrantIds") List<Integer> warrantIds);
	
	
	@Query(value = "SELECT c.id , c.category_name, c.qty, s.ship_name , s.est_date , c.wt_iss_date, o.op_no, s.rev_no \r\n"
			   + "  from  tdms.project p  "
			   + "  left join   tdms.ship s  on p.id = s.project_id "
			   + "  left join tdms.so_line_item sli on sli.id = s.so_line_item_id "
			   + "  left join tdms.category c on c.ship_id = s.id  "
			   + "  left join tdms.operation o on o.warrant_id = c.id  "
			   + "  where sli.id = :soLiId  and category_name is not null  "
			   + "  order by  s.ship_name ", nativeQuery = true)
	List<Object[]> getBySoLineId(@Param("soLiId") int soLiId);

	@Query(value = "SELECT DISTINCT c.id , c.category_name, c.qty, s.ship_name , s.est_date , c.wt_iss_date, o.op_no, s.rev_no, \r\n"
			   + "sli.id as sliId , sale.id as saleId , sale.sale_order_no ,p.part_no,p.drawing_no,p.material_description,sli.order_master_no, sn.status,so.order_no, ic.category, og.organization_name, sli.registration_no from tdms.ship s  \r\n"
			   + "left join tdms.so_line_item sli on sli.id = s.so_line_item_id \r\n"
			   + "left join tdms.sale_order sale on sale.id = sli.sale_order_id \r\n"
			   + "left join tdms.category c on c.ship_id = s.id  \r\n"
			   + "left join tdms.operation o on o.warrant_id = c.id\r\n"
			   + "left join mdms.part_number p on p.id=sli.part_number_id\r\n"
			   + "left join tdms.warrant_details w on w.category_id=c.id\r\n"
			   + "left join mdms.status sn on c.status=sn.id\r\n"
			   + " left join tdms.supply_order so on so.id=sli.so_id "
			   + " left join mdms.item_category ic on ic.id = p.item_category_id "
			   + " left join mdms.organization og on og.id = sli.insp_auth "
			   + "where sn.id IN (:statusIds) "
			   + "order by  s.ship_name ", nativeQuery = true)
	List<Object[]> getAllWarrants( List<Integer> statusIds);
	
	@Query(value = "SELECT c.id , c.category_name, c.qty, s.ship_name , s.est_date , c.wt_iss_date, o.op_no, s.rev_no, \r\n"
			   + "sli.id as sliId , sale.id as saleId , sale.sale_order_no ,p.part_no,p.drawing_no,p.material_description,sli.order_master_no "
			   + " from tdms.ship s  \r\n"
			   + "left join tdms.so_line_item sli on sli.id = s.so_line_item_id \r\n"
			   + "left join tdms.sale_order sale on sale.id = sli.sale_order_id \r\n"
			   + "left join tdms.category c on c.ship_id = s.id  \r\n"
			   + "left join tdms.operation o on o.warrant_id = c.id\r\n"
			   + "left join mdms.part_number p on p.id=sli.part_number_id\r\n"
			   + "where c.id = :warrantId \r\n"
			   + "order by  s.ship_name ", nativeQuery = true)
	List<Object[]> getWarrantById(@Param("warrantId") int warrantId);
	
	List<Category> findBySoLineItemId(Integer soLiId);
	
	@Query(value = "SELECT distinct(c.id), c.category_name, c.qty, s.ship_name, s.est_date, c.wt_iss_date, \r\n"
			   + "     o.op_no, s.rev_no, sli.id as sliId, so.id as saleId, so.sale_order_no, pn.part_no, \r\n"

			   + "     pn.drawing_no, pn.material_description, sli.order_master_no , line_item_no, order_no, stst.status, ic.category, sli.registration_no \r\n"

			   + "     from tdms.ship s \r\n"
			   + "     left join tdms.so_line_item sli on sli.id = s.so_line_item_id \r\n"
			   + "     left join tdms.sale_order so on so.id = sli.sale_order_id \r\n"
			   + "     left join tdms.category c on c.ship_id = s.id    \r\n"
			   + "     left join mdms.part_number pn on pn.id = sli.part_number_id \r\n"
			   + "     left join tdms.operation o on o.warrant_id = c.id      \r\n"
			   + "     left join tdms.warrant_allotment wa on wa.warrant_id = c.id \r\n "
			      + "     left join mdms.status stst on stst.id = c.status "
			      + "       left join mdms.item_category ic ON ic.id = pn.item_category_id "
			   + " where wa.alloted_to = :empId AND stst.id in (:statusIds) order by c.id desc ", nativeQuery = true)
	List<Object[]> getWarrantyByHosEmpId(Integer empId, List<Integer> statusIds) ;
	
	@Query(value = "SELECT distinct c.id, c.category_name, c.qty, s.ship_name, s.est_date, c.wt_iss_date, "
			+ " o.op_no, s.rev_no, sli.id as sliId, sale.id as saleId, sale.sale_order_no, "
			+ " p.part_no, p.drawing_no, p.material_description, sli.order_master_no, ic.category, og.organization_name, sns.status from tdms.ship s "
			+ " left join tdms.so_line_item sli on sli.id = s.so_line_item_id "
			+ " left join mdms.organization og on og.id = sli.insp_auth "
			+ " left join tdms.sale_order sale on sale.id = sli.sale_order_id "
			+ " left join tdms.category c on c.ship_id = s.id "
			+ " left join tdms.operation o on o.warrant_id = c.id "
			+ " left join mdms.part_number p on p.id=sli.part_number_id "
			+ " left join mdms.item_category ic on ic.id = p.item_category_id "
			+ " left join tdms.warrant_allotment wa on wa.warrant_id = c.id "
			+ " left join mdms.status sns on c.status = sns.id"
      + " where wa.alloted_to = :empId AND sns.id in (:statusIds) order by c.id desc", nativeQuery = true)
  List<Object[]> getAllotedWarrants(Integer empId, List<Integer> statusIds);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE tdms.category SET status = :status WHERE id = :id", nativeQuery = true)
	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

	@Modifying
	@Query(value = "UPDATE tdms.category SET status = :status WHERE so_line_item_id = :id", nativeQuery = true)
	void updateStatusBySoLineItem(@Param("id") Integer id, @Param("status") Integer status);
	
	@Modifying
	@Query(value = "UPDATE tdms.category SET status = :status WHERE category_name = :orderNo", nativeQuery = true)
	void updateStatusByWarrantNo(@Param("orderNo") String orderNo, @Param("status") Integer status);

  @Query(value = "SELECT c.* FROM tdms.category c " +
      " LEFT JOIN tdms.submission_slip ss ON ss.warrant_id = c.id " +
      " LEFT JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = ss.id " +
      " LEFT JOIN tdms.offer_letter ol ON ol.merge_sslip_id = msm.merge_sslip_id " +
      " WHERE ol.id = :olId order by c.id asc", nativeQuery = true)
  List<Category> findbyOlId(@Param("olId") Integer olId);

  @Query(value = "SELECT c.* FROM tdms.category c " +
      " LEFT JOIN tdms.submission_slip ss ON ss.warrant_id = c.id " +
      " WHERE ss.id = :sslipId order by c.id asc", nativeQuery = true)
  List<Category> findbySslipId(@Param("sslipId") Integer sslipId);

  @Query(value ="select COALESCE(SUM(inli.accepted), 0) from tdms.internal_inote_line_item inli "
  		+ " left join tdms.internal_inote ini ON ini.id = inli.internal_inote_id "
  		+ " left join tdms.submission_slip ss ON ss.id = ini.sub_slip_id "
  		+ " left join tdms.category cat ON cat.id = ss.warrant_id "
  		+ " WHERE cat.id = :categoryId " , nativeQuery = true)
  BigDecimal findPassedByWarrantIdInternal(Integer categoryId);

  @Query(value =" select COALESCE(SUM(ini.accepted), 0) from tdms.inote_line_item ini "
  		+ " left join tdms.inote int ON int.id = ini.inote_id "
  		+ " left join tdms.offer_letter ol ON ol.id = int.offer_letter_id "
  		+ " left join tdms.merge_sslip_mapping msm ON msm.merge_sslip_id = ol.merge_sslip_id "
  		+ " left join tdms.submission_slip ss ON ss.id = msm.sslip_id "
  		+ " left join tdms.category cat ON cat.id = ss.warrant_id "
  		+ " WHERE cat.id = :categoryId ", nativeQuery = true)
  BigDecimal findPassedBywarrantIdExternal(Integer categoryId);
}
