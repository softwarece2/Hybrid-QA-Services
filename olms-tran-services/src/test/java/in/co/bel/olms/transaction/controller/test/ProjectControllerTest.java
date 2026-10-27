package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsTranServicesApplication;
//import in.co.bel.olms.master.dto.KeyValuePair;
//import in.co.bel.olms.master.dto.ProjectDto;
//import in.co.bel.olms.transaction.controller.ProjectController;
//import in.co.bel.olms.transaction.dto.ProjectDataDto;
//import in.co.bel.olms.transaction.dto.ProjectExcelDto;
//import in.co.bel.olms.transaction.entity.Project;
//import in.co.bel.olms.transaction.service.so.ProjectService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class ProjectControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(ProjectControllerTest.class);
//	
//	@InjectMocks
//	ProjectController projectController;
//	
//	@Mock
//	ProjectService projectService;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void getProjectByIdValidTest() {
//		logger.info("ProjectControllerTest.getProjectByIdValidTest : Initiated");
//		try {
//			int id = 182;
//			when(projectService.getProjectById(anyInt())).thenReturn(new Project());
//			when(mapper.map(any(Project.class), eq(ProjectDto.class))).thenReturn(new ProjectDto());
//			ResponseEntity<ProjectDto> result = projectController.getProjectById(id);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getProjectByIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getProjectByIdValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getProjectByIdValidTest : Finished");
//	}
//	
//	@Test
//	void getProjectBySoIdValidTest() {
//		logger.info("ProjectControllerTest.getProjectBySoIdValidTest : Initiated");
//		try {
//			int supplyOrderId = 223;
//			List<ProjectDataDto> lProjDto = new ArrayList<>();
//			ProjectDataDto projDto = new ProjectDataDto();
//			projDto.setActive(true);
//			lProjDto.add(projDto);
//			lProjDto.add(projDto);
//			when(projectService.getProjectBySoId(anyInt())).thenReturn(lProjDto);
//			ResponseEntity<List<ProjectDataDto>> result = projectController.getProjectBySoId(supplyOrderId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getProjectBySoIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getProjectBySoIdValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getProjectBySoIdValidTest : Finished");
//	}
//	
//	@Test
//	void getProjectForSalesGrpValidTest() {
//		logger.info("ProjectControllerTest.getProjectForSalesGrpValidTest : Initiated");
//		try {
//			int empId = 101;
//			List<ProjectDataDto> lProjDto = new ArrayList<>();
//			ProjectDataDto projDto = new ProjectDataDto();
//			projDto.setActive(true);
//			lProjDto.add(projDto);
//			lProjDto.add(projDto);
//			when(projectService.getProjectForSalesGrp(anyInt())).thenReturn(lProjDto);
//			ResponseEntity<List<ProjectDataDto>> result = projectController.getProjectForSalesGrp(empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getProjectForSalesGrpValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getProjectForSalesGrpValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getProjectForSalesGrpValidTest : Finished");
//	}
//	
//	@Test
//	void getAllProjectValidTest() {
//		logger.info("ProjectControllerTest.getAllProjectValidTest : Initiated");
//		try {
//			List<Project> lProj = new ArrayList<>();
//			Project proj = new Project();
//			proj.setActive(true);
//			lProj.add(proj);
//			lProj.add(proj);
//			when(projectService.getAllProject()).thenReturn(lProj);
//			when(mapper.map(any(Project.class), eq(ProjectDto.class))).thenReturn(new ProjectDto());
//			ResponseEntity<List<ProjectDto>> result = projectController.getAllProject();
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllProjectValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllProjectValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getAllProjectValidTest : Finished");
//	}
//	
//	@Test
//	void getAllByOrgIdValidTest() {
//		logger.info("ProjectControllerTest.getAllByOrgIdValidTest : Initiated");
//		try {
//			int orgId = 100303;
//			int salesGrpId = 1;
//			Object[] objArr = {199, "VRSAM_MAIN EQUIPMENT"};
//			List<Object[]> lObj = Arrays.asList(objArr, objArr);
//			when(projectService.getAllByOrgId(anyInt(), anyInt())).thenReturn(lObj);
//			ResponseEntity<List<KeyValuePair>> result = projectController.getAllByOrgId(orgId, salesGrpId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllByOrgIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllByOrgIdValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getAllByOrgIdValidTest : Finished");
//	}	
//
//	@Test
//	void saveProjectsValidTest() {
//		logger.info("ProjectControllerTest.saveProjectsValidTest : Initiated");
//		try {
//			Map<Boolean, String> respMap = new HashMap<>();
//			ProjectExcelDto projectDto = new ProjectExcelDto();
//			projectDto.setSupplyOrderId(223);
//			String resp = gson.toJson(projectDto);
//			respMap.put(true, resp);
//			when(projectService.saveProjectsV3(any(ProjectExcelDto.class))).thenReturn(respMap);
//			ResponseEntity<String> result = projectController.saveProjects(projectDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveProjectsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: saveProjectsValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.saveProjectsValidTest : Finished");
//	}
//
//	@Test
//	void saveProjectsSecScenarioTest() {
//		logger.info("ProjectControllerTest.saveProjectsSecScenarioTest : Initiated");
//		try {
//			Map<Boolean, String> respMap = new HashMap<>();
//			respMap.put(false, "Internal Server Failure");
//			when(projectService.saveProjectsV3(any(ProjectExcelDto.class))).thenReturn(respMap);
//			ResponseEntity<String> result = projectController.saveProjects(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveProjectsSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: saveProjectsSecScenarioTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.saveProjectsSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void getAllProjectsForChartValidTest() {
//		logger.info("ProjectControllerTest.getAllProjectsForChartValidTest : Initiated");
//		try {
//			List<Project> lProj = new ArrayList<>();
//			Project proj = new Project();
//			proj.setId(199);
//			proj.setProjectName("VRSAM_MAIN EQUIPMENT");
//			lProj.add(proj);
//			lProj.add(proj);
//			when(projectService.getAllProject()).thenReturn(lProj);
//			ResponseEntity<List<KeyValuePair>> result = projectController.getAllProjectsForChart();
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllProjectsForChartValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllProjectsForChartValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.getAllProjectsForChartValidTest : Finished");
//	}
//	
//	@Test
//	void updateProjectValidTest() {
//		logger.info("ProjectControllerTest.updateProjectValidTest : Initiated");
//		try {
//			Map<Boolean, String> respMap = new HashMap<>();
//			ProjectDataDto projDto = new ProjectDataDto();
//			projDto.setActive(true);
//			String resp = gson.toJson(projDto);
//			respMap.put(true, resp);
//			when(projectService.updateProject(any(ProjectDataDto.class))).thenReturn(respMap);
//			ResponseEntity<String> result = projectController.updateProject(projDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateProjectValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateProjectValidTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.updateProjectValidTest : Finished");
//	}
//	
//	@Test
//	void updateProjectSecScenarioTest() {
//		logger.info("ProjectControllerTest.updateProjectSecScenarioTest : Initiated");
//		try {
//			Map<Boolean, String> respMap = new HashMap<>();
//			ProjectDataDto projDto = new ProjectDataDto();
//			projDto.setActive(true);
//			respMap.put(false, "Internal Server Failure");
//			when(projectService.updateProject(any(ProjectDataDto.class))).thenReturn(respMap);
//			ResponseEntity<String> result = projectController.updateProject(projDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateProjectSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateProjectSecScenarioTest " + e.getMessage());
//		}
//		logger.info("ProjectControllerTest.updateProjectSecScenarioTest : Finished");
//	}
//	
//}
