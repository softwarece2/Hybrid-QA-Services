package in.co.bel.olms.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.master.entity.PartNumber;

@Repository
public interface PartNumberRepository extends JpaRepository<PartNumber, Integer> {
	Optional<PartNumber> findByPartNo(String partNo);

	@Query(value = " select * from mdms.part_number where id=:partNoId ", nativeQuery = true)
	Optional<PartNumber> findByPartNoId(Integer partNoId);

	List<PartNumber> findByActiveTrue();

	    @Query(value = "SELECT " +
	            "    pn.part_no, " +
	            "    pn.material_description, " +
	            "    sl.line_item_no, " +
	            "    CASE WHEN so.order_no IS NOT NULL THEN so.order_no || ', ' || TO_CHAR(sl.created_datetime, 'dd-mm-yyyy') ELSE NULL END AS order_details, " +
	            "    sl.order_qty, " +
	            "    i.name AS indentor, " +
	            "    og.organization_name AS IA, " +
	            "    STRING_AGG(cog.name, ', ' ORDER BY cog.name) AS consignees, " +
	            "    so.order_no AS orderNo " +
	            "FROM " +
	            
	            "    tdms.so_line_item sl " +
	            "LEFT JOIN " +
	            "    tdms.supply_order so ON sl.so_id = so.id " +
	            "LEFT JOIN " +
	            "    mdms.indenter i ON so.indenter_id = i.id " +
	            "LEFT JOIN " +
	            "    mdms.organization og ON og.id = sl.insp_auth " +
	            "LEFT JOIN " +
	            "    tdms.so_consignee_mapping c ON so.id = c.supply_order_id " +
	            "LEFT JOIN " +
	            "    mdms.consignee cog ON c.consignee_id = cog.id " +
	            " LEFT JOIN " +
	            "    mdms.part_number pn ON pn.id = sl.part_number_id " +
	            "WHERE (:partNumberId IS NULL OR sl.part_number_id = :partNumberId)" +
	            "GROUP BY " +
	            "    pn.part_no, " +
	            "    pn.material_description, " +
	            "    sl.line_item_no, " +
	            "    sl.order_qty, " +
	            "    so.order_no, " +
	            "    sl.created_datetime, " +
	            "    i.name, " +
	            "    og.organization_name " +
	            "ORDER BY " +
	            "    sl.created_datetime DESC", nativeQuery = true)
	    List<Object[]> getDetailsBypartNumber(@Param("partNumberId") Integer partNumberId);

	    @Query(value = "SELECT d.section_code FROM mdms.department d "
	    		+ "LEFT JOIN ate.department_product_mapping dpm ON d.id = dpm.department_id "
	    		+ "LEFT JOIN ate.partno_product_mapping ppm ON dpm.product_id = ppm.product_id "
	    		+ "LEFT JOIN mdms.part_number pn ON ppm.part_number_id = pn.id "
	    		+ "WHERE pn.id = :partNumberId", nativeQuery = true)
	    List<Integer>findSectionCodeByPartNumberId(@Param("partNumberId") Integer partNumberId);
	    
	    @Query(value="SELECT material_description from mdms.part_number where part_no=:partNumber ", nativeQuery=true)
	    String findMaterialDescriptionByPartNumberId(@Param("partNumber") String partNumber);
	    
	    @Query(value = "select id,department_name \r\n"
	    		+ "from mdms.department where section_code:sectionCode", nativeQuery = true)
	   String finddeptIdName(@Param("sectionCode") Integer partNumberId);
	    
	    @Query(value="SELECT d.id ,d.department_name FROM mdms.department d \r\n"
	    		+ "	    		 LEFT JOIN ate.department_product_mapping dpm ON d.id = dpm.department_id \r\n"
	    		+ "	    		 LEFT JOIN ate.partno_product_mapping ppm ON dpm.product_id = ppm.product_id \r\n"
	    		+ "	    		 LEFT JOIN mdms.part_number pn ON ppm.part_number_id = pn.id \r\n"
	    		+ "	    		 WHERE pn.id = :partNumberId",nativeQuery = true)
	     List<Object[]> findDepartmentByPartNumberId(@Param("partNumberId") Integer partNumberId);

			@Query(value = "SELECT * FROM mdms.part_number p WHERE UPPER(p.part_no) = UPPER(:partNo)", nativeQuery = true)
			Optional<PartNumber> findByPartNoIgnoreCase(@Param("partNo") String partNo);
	
}