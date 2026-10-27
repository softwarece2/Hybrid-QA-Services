package in.co.bel.olms.master.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.SalesGroupDto;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OrganizationRepository;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesGroupService {

	private final SalesGroupRepository salesGrpRepository;

	private final OrganizationRepository organizationRepository;
	
	/**
	 * 
	 * @param salesGrpDto
	 * @return
	 */
	public SalesGroup saveSalesGroup(SalesGroup salesGroup) {
		return salesGrpRepository.save(salesGroup);

	}
	
	public Boolean existsByName(String name) {
		return salesGrpRepository.existsByname(name);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public SalesGroup getSalesGroup(int id) {

		Optional<SalesGroup> salesGroupOptional = salesGrpRepository.findById(id);
		if (salesGroupOptional.isPresent()) {
			return salesGroupOptional.get();
		} else {
			throw new DataNotFoundException("SalesGroup not found with Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SalesGroup> getAllSalesGroups() {

		return salesGrpRepository.findAll();

	}
	
	@Transactional
	public SalesGroup updateSalesGroup(SalesGroupDto salesGroupDto) {
	
	    SalesGroup existingSalesGroup = salesGrpRepository.findById(salesGroupDto.getId())
	            .orElseThrow(() -> new DataNotFoundException("SalesGroup not found with Id: " + salesGroupDto.getId()));

	    if (!existingSalesGroup.getName().equals(salesGroupDto.getName())) {
	        if (salesGrpRepository.existsByNameAndIdNot(salesGroupDto.getName(), salesGroupDto.getId())) {
	            throw new RuntimeException("Sales Group Name already exists!");
	        }
	    }

	    if (!existingSalesGroup.getCode().equals(salesGroupDto.getCode())) {
	        if (salesGrpRepository.existsByCodeAndIdNot(salesGroupDto.getCode(), salesGroupDto.getId())) {
	            throw new RuntimeException("Sales Group Code already exists!");
	        }
	    }

	    Optional<Organization> orgOpt = organizationRepository.findById(salesGroupDto.getOrganizationId());
	    Organization org = new Organization();
	    if(orgOpt != null) {
	    	org = orgOpt.get();
	    }
	        
	    existingSalesGroup.setActive(salesGroupDto.getActive());
	    existingSalesGroup.setCode(salesGroupDto.getCode());
	    existingSalesGroup.setName(salesGroupDto.getName());
	    existingSalesGroup.setOrganization(org);
	    existingSalesGroup.setModifiedDate(new Date());

	    return salesGrpRepository.save(existingSalesGroup);
	}
}
