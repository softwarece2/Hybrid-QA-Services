package in.co.bel.olms.master.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.OrgCqaeMapping;


public interface OrgCqaeMappingRepository extends JpaRepository<OrgCqaeMapping, Integer> {

	
	
	@Query(value="select id,organization_name from mdms.organization o where o.organization_type_id =:id", nativeQuery = true)
	List<Object[]> findByorgId(@Param("id") int id);
	
	@Query(value="select id,organization_name,active from mdms.organization o where o.organization_type_id in (:id1 , :id2)", nativeQuery = true)
	List<Object[]> findByorgIbIgId(@Param("id1") int id1, @Param("id2")int id2);
	
	@Modifying
    @Transactional
	@Query(value = "delete from mdms.org_cqae_mapping where org_id = :orgId",nativeQuery = true)
	void deleteByOrgId(int orgId);
	
	@Query(value="SELECT "
			+ "    oli.id, "
			+ "    oli.so_line_item_id, "
			+ "    oli.tmr_file_name, "
			+ "    oli.hologram_remarks, "
			+ "    oli.offered_qty, "
			+ "    oli.cleared_qty, "
			+ "    oli.priority, "
			+ "    oli.issue_level, "
			+ "    oli.offer_letter_id, "
			+ "    test_name, "
			+ "    sli.line_item_no , "
			//+ "    sli.part_no , " Temporary commented for add line items checking
			+ "    lia.file_name ,"
			+ "    lia.file_name ,"
			+ "    lia.id as  attach_id "
			+ "FROM "
			+ "    tdms.offer_letter AS ol "
			+ "LEFT JOIN "
			+ "    tdms.ol_line_item AS oli ON oli.offer_letter_id = ol.id "
			+ "LEFT JOIN "
			+ "    tdms.so_line_item AS sli ON oli.so_line_item_id  = sli.id "
			+ "LEFT JOIN "
			+ "    tdms.line_item_applicable_test AS liat ON liat.ol_line_item_id = oli.id "
			+ " LEFT JOIN "
			+ "     tdms.ol_line_item_attachment AS lia ON lia.test_id = liat.id "
			+ "LEFT JOIN "
			+ "    mdms.applicable_test AS at2 ON at2.id = liat.applicable_test_id "
			+ "WHERE "
			+ "liat.is_ate = true"
			+ " AND ol.id = :id"
			, nativeQuery = true)
	List<Object[]> getAteById(@Param("id") int id);
	
	@Query(value = "select "
			+ "	ocm.org_id , "
			+ "	o.organization_name , "
			+ "	string_agg(cast ( ocm.cqae_id as text), "
			+ "	',')as insp_agency_id "
			+ "from "
			+ "	mdms.org_cqae_mapping ocm "
			+ "left join mdms.organization o on "
			+ "	ocm.org_id = o.id "
			+ "group by "
			+ "	ocm.org_id, "
			+ "	o.organization_name",nativeQuery = true)
	List<Object[]> getOrgAndInspAgencyIds();

	@Query(value = "select id,organization_name from mdms.organization o where o.id in (:inspAgencyIds)",nativeQuery = true)
	List<Object[]> getInspAgencyDetailsByIdList(List<Integer> inspAgencyIds);

	@Query(value = "SELECT distinct oli.id as ol_line_item_id\r\n"
			+ "			 FROM \r\n"
			+ "			    tdms.offer_letter AS ol \r\n"
			+ "			LEFT JOIN \r\n"
			+ "			   tdms.ol_line_item AS oli ON oli.offer_letter_id = ol.id where oli.id = :id",nativeQuery = true)
	List<Integer> getlineitems(int id);
	
	@Query(value="SELECT  oli.id, sli.line_item_no,at2.id as test_id,at2.test_name\r\n"
			+ "			 \r\n"
			+ "			from\r\n"
			+ "			   tdms.ol_line_item AS oli \r\n"
			+ "			\r\n"
			+ "			LEFT JOIN \r\n"
			+ "			   tdms.line_item_applicable_test AS liat ON liat.ol_line_item_id = oli.id \r\n"
			+ "	        LEFT JOIN \r\n"
			+ "			tdms.so_line_item AS sli ON oli.so_line_item_id  = sli.id \r\n"
			+ "			 LEFT JOIN \r\n"
			+ "			    tdms.ol_line_item_attachment AS lia ON lia.test_id = liat.id \r\n"
			+ "			LEFT JOIN \r\n"
			+ "			    mdms.applicable_test AS at2 ON at2.id = liat.applicable_test_id \r\n"
			+ "			WHERE \r\n"
			+ "		liat.is_ate = true\r\n"
			+ "			 AND oli.offer_letter_id =:id",nativeQuery = true)
	List<Object[]> getByOllineitemId(@Param("id") int id);

	long countByOrganizationId(Integer id);
	
}
