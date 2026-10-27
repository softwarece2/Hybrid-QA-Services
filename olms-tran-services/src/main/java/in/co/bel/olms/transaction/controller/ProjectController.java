package in.co.bel.olms.transaction.controller;

import java.util.*;

import javax.validation.Valid;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.dto.ProjectDto;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.dto.ProjectDataDto;
import in.co.bel.olms.transaction.dto.ProjectExcelDto;
import in.co.bel.olms.transaction.dto.ProjectSummaryDto;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.service.so.ProjectService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ProjectController {

	private final  ProjectService projectService;

	private final Mapper mapper;
	
	private static final Logger logger =LoggerFactory.getLogger(ProjectController.class);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")	
	public ResponseEntity<ProjectDto> getProjectById(@PathVariable int id) throws DataNotFoundException {
	
	Project project = projectService.getProjectById(id);
	ProjectDto projectDto = mapper.map(project, ProjectDto.class);
		return ResponseEntity.ok(projectDto);
	}
	
	@GetMapping("/getProjectBySoId/{supplyOrderId}")
	public ResponseEntity<List<ProjectDataDto>> getProjectBySoId(@PathVariable int supplyOrderId)throws DataNotFoundException{
		
		return ResponseEntity.ok(projectService.getProjectBySoId(supplyOrderId));
		
	}
	
	@GetMapping("/getProjectForSalesGrp/{empId}")
	public ResponseEntity<List<ProjectDataDto>> getProjectForSalesGrp(@PathVariable int empId)throws DataNotFoundException{
		
		return ResponseEntity.ok(projectService.getProjectForSalesGrp(empId));
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<ProjectDto>> getAllProject() {
		List<ProjectDto> projectDtoList = new ArrayList<>();
		List<Project> projects = projectService.getAllProject();
		for(Project project : projects) {
			ProjectDto projectDto = mapper.map(project, ProjectDto.class);
			projectDtoList.add(projectDto);
		}
		return ResponseEntity.ok(projectDtoList);
	}
	
	@GetMapping("/getAllByOrgId/{orgId}/{salesGrpId}")
	public ResponseEntity<List<KeyValuePair>> getAllByOrgId(@PathVariable int orgId, @PathVariable int salesGrpId) {
		List<KeyValuePair> projectList = new ArrayList<>();
		List<Object[]> projects = projectService.getAllByOrgId(orgId, salesGrpId);
		for (Object[] objectArray : projects) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId((int) objectArray[0]);
			keyValuePair.setName(objectArray[1].toString());
			projectList.add(keyValuePair);
		}
		return ResponseEntity.ok(projectList);
	}
	
	
	/**
	 * 
	 * @param projectDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/saveProject")
	public ResponseEntity<String> saveProjects(@RequestBody @Valid ProjectExcelDto projectDto, @RequestHeader(name = "Authorization") String token) {
		try {
			HmisAppSecurity appSecurity = new HmisAppSecurity();
			int empId = appSecurity.getEmpIdFromJwtToken(token);
		Map<Boolean, String> response  = projectService.saveProjectsV3(projectDto, empId);
			if(response.containsKey(true)) {
				String returnString = response.get(true);
				return new ResponseEntity<>(returnString, HttpStatus.CREATED);	
			}
			else {
				String returnString = response.get(false);
				return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch(DataNotValidException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + de.getMessage());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}
	
	@GetMapping("/getAllForChart")
	public ResponseEntity<List<KeyValuePair>> getAllProjectsForChart() {
		List<KeyValuePair> projectList = new LinkedList<>();
		List<Object[]> projects = projectService.findAllProjects();
		for (Object[] objectArr : projects) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId((int) objectArr[0]);
			keyValuePair.setName(objectArr[1].toString());
			projectList.add(keyValuePair);
		}
		return ResponseEntity.ok(projectList);
	}
	
	// req: Add service for the get project by sbu id
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/updateProject")
	public ResponseEntity<String> updateProject(@RequestBody @Valid CategoryDto projectDto) {
		
		Map<Boolean, String> response  = projectService.updateProjectsV3(projectDto);
		if(response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.OK);	
		}
		else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@GetMapping("/deleteSaleOrder/{saleOrderId}")
	public ResponseEntity<String> deleteSaleOrder(@PathVariable int saleOrderId) {
		
		Map<Boolean, String> response  = projectService.deleteSaleOrder(saleOrderId);
		if(response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.OK);	
		}
		else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getAllByFirmId/{orgId}")
	public ResponseEntity<List<KeyValuePair>> getAllByFirmId(@PathVariable int orgId) {
		List<KeyValuePair> projectList = new ArrayList<>();
		List<Object[]> projects = projectService.getAllByFirmId(orgId);
		for (Object[] objectArray : projects) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId((int) objectArray[0]);
			keyValuePair.setName(objectArray[1].toString());
			projectList.add(keyValuePair);
		}
		return ResponseEntity.ok(projectList);
	}
	
	@GetMapping("/getProjectSummaryByPoId/{poId}")
	public ResponseEntity<List<ProjectSummaryDto>> getProjectSummaryByPoId(@PathVariable int poId)throws DataNotFoundException{
		
		return ResponseEntity.ok(projectService.getProjectSummaryByPoId(poId));
		
	}

}
