package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.ApplicableTestDto;
import in.co.bel.olms.master.entity.ApplicableTest;
import in.co.bel.olms.master.service.ApplicableTestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applicableTest")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ApplicableTestController {

	private final ApplicableTestService applicableTestService;

	private final Mapper mapper;
	
	Gson gson = new Gson();

	/**
	 * 
	 * @return List<ApplicableTestDto>
	 */
	
	@GetMapping("/getAll")
	public ResponseEntity<List<ApplicableTestDto>> getAllApplicableTests() {

		List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();
		List<ApplicableTest> allApplicableTests = applicableTestService.getAllApplicableTests();
		for (ApplicableTest applicableTest : allApplicableTests) {
			ApplicableTestDto applicableTestDto = mapper.map(applicableTest, ApplicableTestDto.class);
			applicableTestDtos.add(applicableTestDto);
		}
		return ResponseEntity.ok(applicableTestDtos);

	}
	
	@PostMapping("/save")
	public ResponseEntity<String> saveApplicableTest(@RequestBody @Valid ApplicableTestDto applicableTestDto) {
		Boolean name = applicableTestService.existsByname(applicableTestDto.getTestName());
		if (name) {
			return new ResponseEntity<String>("Applicable Test already exists.", HttpStatus.CONFLICT);
		}
		ApplicableTest savedApplicableTests = applicableTestService.saveApplicableTest(applicableTestDto);
		ApplicableTestDto savedApplicableTestsDto = mapper.map(savedApplicableTests, ApplicableTestDto.class);
		return new ResponseEntity<>(gson.toJson(savedApplicableTestsDto), HttpStatus.CREATED);
	}	

	
	@PostMapping("/update")
	public ResponseEntity<String> updateApplicableTest(@RequestBody @Valid ApplicableTestDto applicableTestDto) {
		ApplicableTestDto savedApplicableTestDto = null;
		try {
			if (applicableTestDto != null) {

				ApplicableTest savedApplicableTest = applicableTestService.updateApplicableTest(applicableTestDto);
				savedApplicableTestDto = mapper.map(savedApplicableTest, ApplicableTestDto.class);
				return new ResponseEntity<String>(gson.toJson(savedApplicableTestDto), HttpStatus.CREATED);

			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/getAllActiveTest")
	public ResponseEntity<List<ApplicableTestDto>> getAllActiveApplicableTests() {
	
		List<ApplicableTestDto> activeApplicableTestDtos = new ArrayList<>();
		List<ApplicableTest> allActiveApplicableTests = applicableTestService.getAllActiveApplicableTest();
		for (ApplicableTest activeApplicableTest : allActiveApplicableTests) {
			ApplicableTestDto activeApplicableTestDto = mapper.map(activeApplicableTest, ApplicableTestDto.class);
			activeApplicableTestDtos.add(activeApplicableTestDto);
		}
		return ResponseEntity.ok(activeApplicableTestDtos);
	
	}
	
	@GetMapping("/getAllApplicableTestBySalesGrpId/{salesGroupId}")
	public ResponseEntity<List<ApplicableTestDto>> getAllApplicableTestBySalesGrpId(@PathVariable int salesGroupId) {
		
		List<ApplicableTestDto> applicableTestDtos = applicableTestService.getApplicableTestsBySalesGroupId(salesGroupId);
		return ResponseEntity.ok(applicableTestDtos);
		
	}
	
	 @GetMapping("/getAllActiveAppTestBySalesGrpId/{salesGroupId}")
	    public ResponseEntity<List<ApplicableTestDto>> getActiveApplicableTestsBySalesGrpId(@PathVariable int salesGroupId) {
	        
	        List<ApplicableTestDto> applicableTestDtos = applicableTestService.getAllActiveApplicableTestBySalesGrpId(salesGroupId);
	        
	        return ResponseEntity.ok(applicableTestDtos);
	    }

		@GetMapping("/getAllTests/{lineItemId}")
		public ResponseEntity<List<ApplicableTestDto>> getAllTests(@PathVariable int lineItemId) {

			List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();
			List<ApplicableTest> allApplicableTests = applicableTestService.getAllTests(lineItemId);
			for (ApplicableTest applicableTest : allApplicableTests) {
				ApplicableTestDto applicableTestDto = mapper.map(applicableTest, ApplicableTestDto.class);
				applicableTestDtos.add(applicableTestDto);
			}
			return ResponseEntity.ok(applicableTestDtos);

		}
}
