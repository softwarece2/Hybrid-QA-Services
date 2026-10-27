package in.co.bel.olms.master.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.ApplicableTestDto;
import in.co.bel.olms.master.entity.ApplicableTest;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ApplicableTestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicableTestService {

	private final ApplicableTestRepository applicableTestRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @return
	 */
	public List<ApplicableTest> getAllApplicableTests() {
		return applicableTestRepository.findAll();
	}
	
	public Boolean existsByname(String name) {
		return applicableTestRepository.existsBytestName(name);
	}
	
	public ApplicableTest saveApplicableTest(ApplicableTestDto applicableTestDto) {
		ApplicableTest applicableTest = mapper.map(applicableTestDto, ApplicableTest.class);
		return applicableTestRepository.save(applicableTest);
	}
	
	public ApplicableTest updateApplicableTest (ApplicableTestDto applicableTestDto) {
		Optional<ApplicableTest> applicableTestOptional = applicableTestRepository.findById(applicableTestDto.getId());
		if (applicableTestOptional.isPresent()) {
			ApplicableTest applicableTest =  applicableTestOptional.get();			
				applicableTest.setTestName(applicableTestDto.getTestName());
				applicableTest.setActive(applicableTestDto.getActive());				
						
			return applicableTestRepository.save(applicableTest);
		} else {
			throw new DataNotFoundException("Applicable Test not found with Id: " + applicableTestDto.getId());
		}
	}
	
	public List<ApplicableTest> getAllActiveApplicableTest() {
		return applicableTestRepository.findAllActiveAppTest();
	}
	
	public List<ApplicableTestDto> getApplicableTestsBySalesGroupId(int salesGroupId) {        
        List<ApplicableTest> applicableTests = applicableTestRepository.findBySalesGroupId(salesGroupId);
       
        List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();
        for (ApplicableTest applicableTest : applicableTests) {
            ApplicableTestDto applicableTestDto = mapper.map(applicableTest, ApplicableTestDto.class);
            applicableTestDtos.add(applicableTestDto);
        }
        return applicableTestDtos;
    }
	
	public List<ApplicableTestDto> getAllActiveApplicableTestBySalesGrpId(int salesGroupId) {
       
        List<ApplicableTest> applicableTests = applicableTestRepository.findAllActiveAppTestBySalesGrpId(salesGroupId);

        List<ApplicableTestDto> applicableTestDtos = new ArrayList<>();
        for (ApplicableTest applicableTest : applicableTests) {
            ApplicableTestDto applicableTestDto = mapper.map(applicableTest, ApplicableTestDto.class);
            applicableTestDtos.add(applicableTestDto);
        }

        return applicableTestDtos;
    }
	
	public List<ApplicableTest> getAllTests(int lineItemId) {
		return applicableTestRepository.getAllTests(lineItemId);
		 
	}
}
