package in.co.bel.olms.master.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dto.IsAteDto;
import in.co.bel.olms.master.dto.OrgCqaeMappingDto;
import in.co.bel.olms.master.dto.TestDto;
import in.co.bel.olms.master.entity.OrgCqaeMapping;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.repository.OrgCqaeMappingRepository;
import in.co.bel.olms.master.repository.OrganizationRepository;
import in.co.bel.olms.transaction.entity.OlLineItemAttachment;
import in.co.bel.olms.transaction.repository.OlItemAttachmentRepository;
import in.co.bel.olms.transaction.service.OlLineItemService;
import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class OrgCqaeMappingService {
	
//	@Value("${file.domain-url}")
	private String domainUrl;
	@Value("${spring.application.name}")
	private String applicationName;
	
	private static final Logger logger = LoggerFactory.getLogger(OrgCqaeMappingService.class);

	private final OrgCqaeMappingRepository orgCqaeMappingRepository;

	private final OlItemAttachmentRepository olItemAttachmentRepository;

	private final OrganizationRepository organizationRepository;

	public List<OrgCqaeMappingDto> getOrgCqae() {

		try {
			List<OrgCqaeMappingDto> orgCqaeMappingDtos = new ArrayList<>();
			List<Object[]> orgDetails = orgCqaeMappingRepository.findByorgId(ApplicationConstants.INSPECTION_AGENCY);

			for (Object[] objectArray : orgDetails) {
				OrgCqaeMappingDto orgCqaeMappingDto = new OrgCqaeMappingDto();
				orgCqaeMappingDto.setId(Integer.parseInt(objectArray[0].toString()));
				orgCqaeMappingDto.setOrganizationName(objectArray[1].toString());
				orgCqaeMappingDtos.add(orgCqaeMappingDto);
			}

			return orgCqaeMappingDtos;
		}

		catch (Exception e) {
			logger.error("Error in fetching Organisation Names: {}", e.getMessage());
			throw e;
		}
	}

	public List<OrgCqaeMappingDto> getOrgIbIg() {

		try {
			List<OrgCqaeMappingDto> orgCqaeMappingDtos = new ArrayList<>();

			List<Object[]> orgDetails = orgCqaeMappingRepository.findByorgIbIgId(ApplicationConstants.IB,
					ApplicationConstants.IG);

			for (Object[] objectArray : orgDetails) {
				OrgCqaeMappingDto orgCqaeMappingDto = new OrgCqaeMappingDto();
				orgCqaeMappingDto.setId(Integer.parseInt(objectArray[0].toString()));
				orgCqaeMappingDto.setOrganizationName(objectArray[1].toString());
				orgCqaeMappingDto.setActive(objectArray[2].toString());
				orgCqaeMappingDtos.add(orgCqaeMappingDto);
			}

			return orgCqaeMappingDtos;
		}

		catch (Exception e) {
			logger.error("Error in fetching Organisation Names: {}", e.getMessage());
			throw e;
		}
	}

	
	public Map<Boolean, String> saveOrgInspectionAgencyMapping(OrgCqaeMappingDto dto) {
	    Map<Boolean, String> returnMap = new HashMap<>();
	    try {
	        Optional<Organization> orgOptional = organizationRepository.findById(dto.getId());
	        if (orgOptional.isEmpty()) {
	            returnMap.put(false, "No Organization found with given Id");
	            return returnMap;
	        }

	        // Check if mapping exists BEFORE doing anything
	        if (orgCqaeMappingRepository.countByOrganizationId(dto.getId()) > 0) {
	            returnMap.put(false, "This firm is already mapped. Please use the Edit option.");
	            return returnMap;
	        }

	        // If mapping doesn't exist then it's safe to save
	        executeMappingLogic(orgOptional.get(), dto.getInspectionAgencyIds(), returnMap, "Saved successfully.");
	        
	    } catch (Exception e) {
	        logger.error("Exception in saving: {}", e.getMessage());
	        returnMap.put(false, "Some error occurred. Plz try later.");
	    }
	    return returnMap;
	}

	public Map<Boolean, String> updateOrgInspectionAgencyMapping(OrgCqaeMappingDto dto) {
	    Map<Boolean, String> returnMap = new HashMap<>();
	    try {
	        Optional<Organization> orgOptional = organizationRepository.findById(dto.getId());
	        if (orgOptional.isEmpty()) {
	            returnMap.put(false, "No Organization found with given Id");
	            return returnMap;
	        }

	        // For Update, deleting the old ones
	        orgCqaeMappingRepository.deleteByOrgId(dto.getOldId());
	        executeMappingLogic(orgOptional.get(), dto.getInspectionAgencyIds(), returnMap, "Updated successfully.");

	    } catch (Exception e) {
	        logger.error("Exception in updating: {}", e.getMessage());
	        returnMap.put(false, "Some error occurred. Plz try later.");
	    }
	    return returnMap;
	}

	// Helper code for Mapping
	private void executeMappingLogic(Organization org, List<Integer> list, Map<Boolean, String> returnMap, String successMsg) {
	    List<Organization> agencies = organizationRepository.findAllById(list);
	    List<OrgCqaeMapping> mappings = new ArrayList<>();
	    for (Organization agency : agencies) {
	        OrgCqaeMapping mapping = new OrgCqaeMapping();
	        mapping.setOrganization(org);
	        mapping.setCqaeOrg(agency);
	        mappings.add(mapping);
	    }
	    orgCqaeMappingRepository.saveAll(mappings);
	    returnMap.put(true, successMsg);
	}
	
	public List<IsAteDto> getAtebyOfferLetterId(int id)
	{
		try {
		List<IsAteDto>isAteDtos= new ArrayList<>();
		List<Object[]> ate=orgCqaeMappingRepository.getAteById(id);
		
		for(Object[] objectArray: ate)
		{IsAteDto isAteDto= new IsAteDto();
		
		String testName= objectArray[9] != null ? objectArray[9].toString() : " ";
			
			if(objectArray[0]==null)
				isAteDto.setOLLineItemId(null);
				else
			isAteDto.setOLLineItemId(Integer.parseInt(objectArray[0].toString()));
			
			Map<String, String> firmAttachmentURLs = new HashMap<>();
			Map<String, String> inspectorAttachmentURLs = new HashMap<>();
			//String fileName=objectArray[12] != null ?objectArray[12].toString() : " ";
			if(objectArray[13]!=null) {
			Optional<OlLineItemAttachment> ol=olItemAttachmentRepository.findById(Integer.parseInt(objectArray[13].toString()));
			String createdBy = ol.get().getCreatedBy();
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + ol.get().getFileName();
			if (createdBy != null && createdBy.equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_FIRM)) {
				firmAttachmentURLs.put(ol.get().getFileName(), fileDownloadUri);
			} else if (createdBy != null && createdBy.equalsIgnoreCase(ApplicationConstants.OL_ITEM_ATTACHMENT_BY_INSPECTOR)) {
				inspectorAttachmentURLs.put(ol.get().getFileName(), fileDownloadUri);
			}
		//	isAteDto.setFirmAttachmentURLs(firmAttachmentURLs);
			//isAteDto.setInspectorAttachmentURLs(inspectorAttachmentURLs);
			}
			String lineItemNo =objectArray[10] != null ? objectArray[10].toString() : " ";
			String partNo =objectArray[11] != null ? objectArray[11].toString() : " ";
			isAteDto.setLineItemNo(lineItemNo);
			//isAteDto.setPartNo(partNo);
			isAteDtos.add(isAteDto);
			
		}
		return isAteDtos;
		}
		catch (Exception e) {
			logger.error("Error in fetching ATE Names: {}", e.getMessage());
			throw e;
		}
		
	}
	
	
	public List<IsAteDto> getAtebyOlId(int id)
	{
		try {
		
		List<Integer>ollineItemIds=orgCqaeMappingRepository.getlineitems(id);
		List<IsAteDto> ateDtos=new ArrayList<>();
		for(Integer item: ollineItemIds)
		{
			IsAteDto isAteDto= new IsAteDto();
			List<TestDto> testDto=new  ArrayList<>();
			if(item!=null) {
			List<Object[]>objectArray=orgCqaeMappingRepository.getByOllineitemId(item);
			
			for(Object[] obj: objectArray)
			{ 
				TestDto dto=new TestDto();
				
				Integer testId= obj[2] != null? (Integer) obj[2] : 0;
				dto.setId(testId);
				
				String testName= obj[3] != null ? obj[3].toString() : " ";
				dto.setTestname(testName);
				
				if(obj[0]==null)
					isAteDto.setOLLineItemId(null);
					else
				isAteDto.setOLLineItemId(Integer.parseInt(obj[0].toString()));
				
				String lineItemNo =obj[1] != null ? obj[1].toString() : " ";
				isAteDto.setLineItemNo(lineItemNo);
				testDto.add(dto);
				
			}
			isAteDto.setTestDtos(testDto);
			ateDtos.add(isAteDto);		
			}
		}
		return ateDtos;
		}
		catch (Exception e) {
			logger.error("Error in fetching ATE Names: {}", e.getMessage());
			throw e;
		}
		
	}

	public List<Object[]> getOrgAndInspAgencyIds() {

		return orgCqaeMappingRepository.getOrgAndInspAgencyIds();
	}

	public List<Object[]> getInspAgencyDetailsByIdList(List<Integer> inspAgencyIds) {

		return orgCqaeMappingRepository.getInspAgencyDetailsByIdList(inspAgencyIds);
	}
}
