package in.co.bel.olms.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.OrganizationType;
import in.co.bel.olms.master.repository.OrganizationTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationTypeService {

	private final OrganizationTypeRepository organizationTypeRepository;
	
	public List<OrganizationType> getAllOrganizationType() {		
		return organizationTypeRepository.findAllActiveOrg();
	}
}
