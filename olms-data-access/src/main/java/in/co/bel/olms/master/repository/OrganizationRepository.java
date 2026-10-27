package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.Organization;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

	@Query(value = "SELECT * FROM MDMS.ORGANIZATION O LEFT JOIN MDMS.ORG_CQAE_MAPPING OCM ON O.ID = OCM.CQAE_ID WHERE OCM.ORG_ID = :orgId And OCM.cqae_id = :agencyId LIMIT 1", nativeQuery = true)
	Organization getCqaeOrg(@Param("orgId") int orgId, @Param("agencyId") int agencyId);

	Boolean existsByOrganizationName(String organizationName);

	Boolean existsBymobile(String mobile);

	Boolean existsByemail(String email);
	
	@Query(value = "SELECT O.* "
						+ "FROM TDMS.OFFER_LETTER OL "
						+ "LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID "
						+ "LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID "
						+ "LEFT JOIN MDMS.ORGANIZATION O ON PO.ORGANIZATION_ID = O.ID "
					+ "WHERE OL.ID = :olId", nativeQuery = true)
	Organization getOrgByOlId(@Param("olId") int olId);

	@Query(value = "select * from mdms.ORGANIZATION where organization_type_id = 1", nativeQuery = true)
	List<Organization> getDGQAHierarchy();
	
	@Query(value="SELECT * FROM mdms.ORGANIZATION WHERE id=:id", nativeQuery=true)
	public Organization findOrgById(int id);
	
	@Query(value = "SELECT o.id, o.organization_name FROM mdms.organization o WHERE id IN (SELECT cqae_id FROM mdms.org_cqae_mapping ocm WHERE ocm.org_id = :firmId)", nativeQuery = true)
	List<Object[]> getAssociatedInspectionAgencies(int firmId);
	
	@Query(value = "select org.id as firm_id, org.organization_name as firm_nm from mdms.ORGANIZATION org where org.organization_type_id = 2 OR org.organization_type_id = 3", nativeQuery = true)
	List<Object[]> getAllFirms();
	
	@Query(value = "SELECT * FROM mdms.organization where organization_type_id=:orgTypeId ", nativeQuery = true)
	List<Organization> getOrgByTypeId(@Param("orgTypeId") int orgTypeId);
	
	
	@Query(value = "	   SELECT "
			+ "    f.id AS entity_id, "
			+ "    f.organization_name AS entity_name, "
			+ "    'Firm' AS entity_type "
			+ "    FROM "
			+ "    mdms.organization f "
			+ "    WHERE "
			+ "    f.id = :firmId "	
			+ "	   UNION ALL "	
			+ "    SELECT "
			+ "    i.id AS entity_id, "
			+ "    i.organization_name AS entity_name, "
			+ "    'Inspection Agency' AS entity_type "
			+ "    FROM "
			+ "    mdms.organization i "
			+ "    JOIN "
			+ "    mdms.org_cqae_mapping ocm ON i.id = ocm.cqae_id "
			+ "    WHERE "
			+ "    ocm.org_id = :firmId", nativeQuery = true)
List<Object[]> getOrgAndAssociatedInspectionAgencies(int firmId);

Boolean existsByOrganizationNameAndIdNot(String organizationName, Integer id);

Boolean existsBymobileAndIdNot(String mobile, Integer id);

Boolean existsByemailAndIdNot(String email, Integer id);

}
