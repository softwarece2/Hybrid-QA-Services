package in.co.bel.olms.master.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OrganizationRepository;

/**
 * 
 * @author Manasa B
 * @since 08-12-2022
 */


@Component
public class OrganizationDao {

	@Autowired
	OrganizationRepository organizationRepository;
	
	/**
	 * @author Manasa B
	 * @param orgId
	 * @return
	 */
	public int getOrgTypeIdByOrgId(int orgId) {
		Optional<Organization> organization = organizationRepository.findById(orgId);
		if (organization.isPresent()) {
			return organization.get().getOrganizationType().getId();
		} else {
			throw new DataNotFoundException("Org Type Data not found for the organization Id " +  orgId);
		}
	}
	
	
	/**
	 * @author Manasa B
	 * @param orgId
	 * @return
	 */
	public String getOrgNameByOrgId(int orgId) {
		Optional<Organization> organization = organizationRepository.findById(orgId);

		if (organization.isPresent()) {
			return organization.get().getOrganizationName();
		} else {
			throw new DataNotFoundException("Organization not present to fetch name for the org Id " + orgId);
		}
	}
	
	
	/**
	 * @author Manasa B
	 * @param orgId
	 * @return
	 */
	public Organization getOrganizationByOrgId(int orgId) {
		Optional<Organization> organization = organizationRepository.findById(orgId);
		if (organization.isPresent()) {
			return organization.get();
		} else {
			throw new DataNotFoundException("Organization not present for the org Id " + orgId);
		}
	}
	
}
