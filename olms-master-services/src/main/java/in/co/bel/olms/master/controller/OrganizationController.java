package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.FirmsDto;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.dto.OrganizationDto;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.OrganizationService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller class for Organization 
 * @author Manasa B
 * @author  Awadhesh
 * @since 09-12-2022
 */
@RestController
@RequestMapping("/organization")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OrganizationController {
	
	Gson gson = new Gson();

	private final OrganizationService organizationService;

	private final Mapper mapper;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<OrganizationDto> getOrganization(@PathVariable int id) throws DataNotFoundException {
		Organization organization = organizationService.getOrganization(id);
		OrganizationDto organizationDto = mapper.map(organization, OrganizationDto.class);
		return ResponseEntity.ok(organizationDto);
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<OrganizationDto>> getAllOrganization() {
		List<OrganizationDto> organizationDtoList = new ArrayList<> ();
		List<Organization> organizations = organizationService.getAllOrganization();
		for(Organization organization : organizations) {
			OrganizationDto organizationDto = mapper.map(organization, OrganizationDto.class);
			organizationDtoList.add(organizationDto);
		}
		return ResponseEntity.ok(organizationDtoList);
	}

	//Add service for fetching active organization
	
	/**
	 * 
	 * @param organizationDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveOrg(@RequestBody @Valid OrganizationDto organizationDto) {
		OrganizationDto savedOrgDto = null;
		try {
			if(organizationDto != null ) {
			Boolean name = organizationService.existsByOrganizationName(organizationDto.getOrganizationName());
			Boolean mobile = organizationService.existsBymobile(organizationDto.getMobile());
			Boolean email = organizationService.existsByemail(organizationDto.getEmail());
			if(name) {
				 return new ResponseEntity<String>("Organization Name already exists.",HttpStatus.CONFLICT);
			}
			else if(mobile) {
				return new ResponseEntity<String>("Mobile Number already exists.",HttpStatus.CONFLICT);
			}
			else if(email) {
				return new ResponseEntity<String>("Email is already exists.",HttpStatus.CONFLICT);
			}
			else {
				    Organization org = mapper.map(organizationDto,Organization.class);
					org.setCreatedDatetime(new Date());
				String photoStr = organizationDto.getLogoStr();
				if (photoStr != null) {
					String imgString = photoStr.substring(photoStr.indexOf(",") + 1);
					byte[] byteArray = Base64.getDecoder().decode(imgString);
					org.setLogo(byteArray);
				}
					Organization savedOrg = organizationService.saveOrganization(org);
					savedOrgDto = mapper.map(savedOrg, OrganizationDto.class);
					return new ResponseEntity<String>(gson.toJson(savedOrgDto),HttpStatus.CREATED);
			}
			}
			
		}
		catch(Exception e) {
//			logger.error("Error in saveOrg:{}",e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	/**
	 * 
	 * @param organizationDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<?> updateOrganization(@RequestBody @Valid OrganizationDto organizationDto) {
	    try {
	        Organization savedOrg = organizationService.updateOrganization(organizationDto);
	        OrganizationDto savedOrgDto = mapper.map(savedOrg, OrganizationDto.class);
	        return new ResponseEntity<Object>(gson.toJson(savedOrgDto), HttpStatus.OK); // Use OK for updates
	    } catch (DataNotFoundException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (RuntimeException e) {
	        // This catches our "Organization Name already exists" messages
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	

	@GetMapping("/getDGQAHierarchy")
	public ResponseEntity<List<OrganizationDto>> getDGQAHierarchy() {
		List<OrganizationDto> organizationDtoList = new ArrayList<>();
		List<Organization> organizations = organizationService.getDGQAHierarchy();
		for (Organization organization : organizations) {
			OrganizationDto organizationDto = new OrganizationDto();
			organizationDto.setId(organization.getId());
			organizationDto.setOrganizationName(organization.getOrganizationName());
			if (organization.getParentOrganization() != null) {
				organizationDto.setParentOrgId(organization.getParentOrganization().getId());
			}
			organizationDtoList.add(organizationDto);
		}
		return ResponseEntity.ok(organizationDtoList);
	}
		
	@GetMapping("/getAssociatedInspectionAgencies/{firmId}")
	public ResponseEntity<List<KeyValuePair>> getAssociatedInspectionAgencies(@PathVariable int firmId) {

		return ResponseEntity.ok(organizationService.getAssociatedInspectionAgencies(firmId));
	}
	
	@GetMapping("/getAllFirms")
	public ResponseEntity<List<FirmsDto>> getAllFirms() {
		return ResponseEntity.ok(organizationService.getAllFirms());
	}
	
	@GetMapping("/getByOrgTypeId/{orgTypeId}")
	public ResponseEntity<List<Organization>> getOrgByTypeId(@PathVariable int orgTypeId){
		return ResponseEntity.ok(organizationService.getOrgByTypeId(orgTypeId));
	}
	
	@GetMapping("/getOrgAndAssociatedInspectionAgencies/{firmId}")
	public ResponseEntity<List<KeyValuePair>> getOrgAndAssociatedInspectionAgencies(@PathVariable int firmId) {

		return ResponseEntity.ok(organizationService.getOrgAndAssociatedInspectionAgencies(firmId));
	}
	
	
}
