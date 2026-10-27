package in.co.bel.olms.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dao.OrganizationDao;
import in.co.bel.olms.master.dto.FirmsDto;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.dto.OrganizationDto;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OrganizationRepository;
import in.co.bel.olms.master.repository.OrganizationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for Organization
 * @author Manasa B
 * @author VizagTeam
 * @since 08-12-2022
 * 
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {

	private final OrganizationRepository organizationRepository;

	private final OrganizationDao organizationDao;

	private final OrganizationTypeRepository organizationTypeRepository;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Organization getOrganization (int id) {		
		return organizationDao.getOrganizationByOrgId(id);		
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Boolean existsByOrganizationName(String organizationName) {
		return organizationRepository.existsByOrganizationName(organizationName);
	}
	
	
	public Boolean existsBymobile(String mobile) {
		return organizationRepository.existsBymobile(mobile);
	}
	
	
	public Boolean existsByemail(String email) {
		return organizationRepository.existsByemail(email);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Organization> getAllOrganization() {		
		return organizationRepository.findAll();
	}
	
	
	/**
	 * 
	 * @param Organization
	 * @return
	 */

	public Organization saveOrganization(Organization Organization) {
		return  organizationRepository.save(Organization);
	}
			
	public Organization updateOrganization(OrganizationDto organizationDto) {
	  
	    Optional<Organization> orgOptional = organizationRepository.findById(organizationDto.getId());	    
	    if (orgOptional.isPresent()) {
	        Organization existingOrg = orgOptional.get();
	        if (!existingOrg.getOrganizationName().equals(organizationDto.getOrganizationName())) {
	            if (organizationRepository.existsByOrganizationNameAndIdNot(organizationDto.getOrganizationName(), organizationDto.getId())) {
	                throw new RuntimeException("Organization Name already exists.");
	            }
	        }

	        // Check Mobile
	        if (!existingOrg.getMobile().equals(organizationDto.getMobile())) {
	            if (organizationRepository.existsBymobileAndIdNot(organizationDto.getMobile(), organizationDto.getId())) {
	                throw new RuntimeException("Mobile Number already exists.");
	            }
	        }

	        // Check Email
	        if (!existingOrg.getEmail().equals(organizationDto.getEmail())) {
	            if (organizationRepository.existsByemailAndIdNot(organizationDto.getEmail(), organizationDto.getId())) {
	                throw new RuntimeException("Email is already exists.");
	            }
	        }

	        
	        existingOrg.setAddress(organizationDto.getOrganizationAddress());
	        existingOrg.setEmail(organizationDto.getEmail());
	        existingOrg.setMobile(organizationDto.getMobile());
	        existingOrg.setOrganizationName(organizationDto.getOrganizationName());
	        existingOrg.setActive(organizationDto.isActive()); 
	        existingOrg.setModifiedDatetime(new Date());
	        existingOrg.setStdCode(organizationDto.getStdCode());
	        
	        organizationTypeRepository.findById(organizationDto.getOrganizationTypeId())
	                .ifPresent(existingOrg::setOrganizationType);

	        return organizationRepository.save(existingOrg);
	    } else {
	        throw new DataNotFoundException("Organization not found with Id: " + organizationDto.getId());
	    }
	}

	public List<Organization> getDGQAHierarchy() {
		return organizationRepository.getDGQAHierarchy();
	}
	
	public List<KeyValuePair> getAssociatedInspectionAgencies(int firmId) {
		List<KeyValuePair> agencyNames = new ArrayList<>();
		try {
			List<Object[]> inspectionAgencies = organizationRepository.getAssociatedInspectionAgencies(firmId);
			for (Object[] inspectionAgency : inspectionAgencies) {
				KeyValuePair keyValuePair = new KeyValuePair();
				int agencyId = (int) inspectionAgency[0];
				String agencyName = (String) inspectionAgency[1].toString();
				keyValuePair.setId(agencyId);
				keyValuePair.setName(agencyName);
				agencyNames.add(keyValuePair);
			}
			return agencyNames;
		} catch (Exception e) {
			log.error("Error in fetching inspection agencies: {}", e.getMessage());
		}
		return agencyNames;
	}
	
	
	public List<FirmsDto> getAllFirms() {
		List<FirmsDto> firmsDtos = new ArrayList<>();

        try {
            List<Object[]> firmDetails = organizationRepository.getAllFirms();

            if (firmDetails != null && !firmDetails.isEmpty()) {
                for (Object[] firmDet : firmDetails) {
                    FirmsDto firmDto = new FirmsDto();
                    firmDto.setFirmId((Integer) firmDet[0]);
                    firmDto.setFirmNm((String) firmDet[1]);
                    firmsDtos.add(firmDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return firmsDtos;
	}
	
	public List<Organization> getOrgByTypeId(int orgTypeId){
		return organizationRepository.getOrgByTypeId(orgTypeId);
	}
	
	public List<KeyValuePair> getOrgAndAssociatedInspectionAgencies(int firmId) {
		List<KeyValuePair> agencyNames = new ArrayList<>();
		try {
			List<Object[]> inspectionAgencies = organizationRepository.getOrgAndAssociatedInspectionAgencies(firmId);
			for (Object[] inspectionAgency : inspectionAgencies) {
				KeyValuePair keyValuePair = new KeyValuePair();
				int agencyId = (int) inspectionAgency[0];
				String agencyName = (String) inspectionAgency[1].toString();
				keyValuePair.setId(agencyId);
				keyValuePair.setName(agencyName);
				agencyNames.add(keyValuePair);
			}
			return agencyNames;
		} catch (Exception e) {
			log.error("Error in fetching inspection agencies: {}", e.getMessage());
		}
		return agencyNames;
	}
}
