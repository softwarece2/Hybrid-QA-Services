package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.OrganizationType;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Integer> {

	@Query(value = "select * from mdms.organization_type where active = true", nativeQuery = true)
	List<OrganizationType> findAllActiveOrg();

}
