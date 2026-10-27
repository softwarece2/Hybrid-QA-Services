package in.co.bel.olms.master.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.OrganizationType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OrganizationTypeRepository;

/**
 * 
 * @author Manasa B
 *
 */

@Component
public class OrganizationTypeDao {

	@Autowired
	OrganizationTypeRepository organizationTypeRepository;
	/**
	 * @author Manasa B
	 * @param orgId
	 * @return
	 */
	public OrganizationType getOrganizationTypeById(int id) {
		Optional<OrganizationType> organizationType = organizationTypeRepository.findById(id);
		if (organizationType.isPresent()) {
			return organizationType.get();
		} else {
			throw new DataNotFoundException("Organization Type not present for the Id " + id);
		}
	}
	
}
