package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.dto.SbuCameraIpMappingDto;
import in.co.bel.olms.ate.entity.SbuCameraTestMapping;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.ate.repository.SbuCameraTestMappingRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import in.co.bel.olms.master.entity.SbuCameraIpMapping;
import in.co.bel.olms.master.repository.SbuCameraIpMappingRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SbuCameraTestMappingService {

	private final SbuCameraTestMappingRepository sbuCameraTestMappingRepository;

	private final SbuCameraIpMappingRepository sbuCameraIpMappingRepository;

	private final TestRepository testRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SbuCameraTestMappingService.class);
	
	private static final String ALREADY_EXIST = "' already exists.";
	
	@Transactional	
	public String save (List<SbuCameraIpMappingDto> dtos) {
		Set<String> incomingIps = new HashSet<>();
		Set<String> incomingSbus = new HashSet<>();
		
		// PRE-VALIDATION 
	    for (SbuCameraIpMappingDto dto : dtos) {
	        // Check duplicates in list
	        if (dto.getCameraIp() != null && !incomingIps.add(dto.getCameraIp())) {
	            return "CONFLICT: Duplicate Camera Ip found while saving: " + dto.getCameraIp();
	        }
	        if (dto.getSbuName() != null && !incomingSbus.add(dto.getSbuName())) {
	            return "CONFLICT: Duplicate Camera Name found while saving: " + dto.getSbuName();
	        }

	        // Check duplicates in DB
	        if (dto.getId() == null || dto.getId() == 0) {
	            if (sbuCameraIpMappingRepository.existsByCameraIp(dto.getCameraIp())) {
	                return "CONFLICT: Camera Ip '" + dto.getCameraIp() + ALREADY_EXIST;
	            }
	            if (sbuCameraIpMappingRepository.existsBySbuName(dto.getSbuName())) {
	                return "CONFLICT: Camera Name '" + dto.getSbuName() + ALREADY_EXIST;
	            }
	        }
	    }
		
		for(SbuCameraIpMappingDto dto : dtos) {
			Integer camId=0;
			if(dto.getId() != null)camId=dto.getId();
			Optional<SbuCameraIpMapping>CamOptional = sbuCameraIpMappingRepository.findById(camId);
			SbuCameraIpMapping camera = new SbuCameraIpMapping();
			if(CamOptional.isEmpty()){
				camera.setCameraIp(dto.getCameraIp());
				camera.setCreatedDatetime(new Date());	
				camera.setSalesGrpId(dto.getSalesGrpId());
				camera.setSbuName(dto.getSbuName());
				sbuCameraIpMappingRepository.save(camera);
			}
			else camera = CamOptional.get();
			List<Test> tests = dto.getTests();
			for (Test test : tests) {
                Optional<Test> testOptional = testRepository.findById(test.getId());
                if (testOptional.isPresent()) {
                    Test applicableTest = testOptional.get();
                    SbuCameraTestMapping testCameraMap = new SbuCameraTestMapping();
                    testCameraMap.setTestId(applicableTest);
                    testCameraMap.setCamId(camera);
                    sbuCameraTestMappingRepository.save(testCameraMap);
                } else {
                    logger.error("Test with ID {} not found.  Skipping.", test.getId());
                }
            }
			
		}
    	return "SUCCESS: Data Entered";
	}
	
	public List<SbuCameraIpMappingDto> get () {
		List<SbuCameraIpMapping> allCameras = sbuCameraIpMappingRepository.findAll();
		List<SbuCameraIpMappingDto> dtoList = new ArrayList<>();
		
		for(SbuCameraIpMapping camera : allCameras) {
			String testNameConcat = "";
			Integer camId = camera.getId();
			List<Object[]> TestMappingList = sbuCameraTestMappingRepository.findByCamId(camId);
			List<Test> tests = new ArrayList<>();
			for (Object[] testMap: TestMappingList) {	
				Integer testId = testMap != null ? Integer.parseInt(testMap[0].toString()) : 0;
		    	Optional<Test>TestOptional = testRepository.findById(testId);
		    	Test test = new Test();
		    	if(TestOptional.isPresent()) {
		    		test = TestOptional.get();
		    		testNameConcat += test.getTestDesc() + "  ";
		    	}
		    	tests.add(test);
				
			}
			SbuCameraIpMappingDto dto = new SbuCameraIpMappingDto();
			dto.setCameraIp(camera.getCameraIp());
			dto.setId(camera.getId());
			dto.setCreatedDatetime(camera.getCreatedDatetime());
			dto.setSbuName(camera.getSbuName());
			dto.setTests(tests);
			dto.setTestName(testNameConcat);
			dtoList.add(dto);

			
		}
		return dtoList;
	}
	
    public String update(SbuCameraIpMappingDto dto) {
        Optional<SbuCameraIpMapping> cameraOptional = sbuCameraIpMappingRepository.findById(dto.getId());

        if (cameraOptional.isEmpty()) {
            return "Camera not found.";
        }

        SbuCameraIpMapping camera = cameraOptional.get();
        
        // --- Duplicate record validation ---
        // Check if Camera IP is already taken by another ID
        if (sbuCameraIpMappingRepository.existsByCameraIpAndIdNot(dto.getCameraIp(), dto.getId())) {
            return "Update failed: Camera IP '" + dto.getCameraIp() + ALREADY_EXIST;
        }

        // Check if SBU Name is already taken by another ID
        if (sbuCameraIpMappingRepository.existsBySbuNameAndIdNot(dto.getSbuName(), dto.getId())) {
            return "Update failed: Camera Name '" + dto.getSbuName() + ALREADY_EXIST;
        }

        camera.setCameraIp(dto.getCameraIp());
        camera.setSalesGrpId(dto.getSalesGrpId());
        camera.setSbuName(dto.getSbuName());
        camera.setModifiedDatetime(new Date());
        sbuCameraIpMappingRepository.save(camera);

        sbuCameraTestMappingRepository.deleteAllByCamId(camera.getId());

        List<Test> tests = dto.getTests();
        if (tests != null) {
            for (Test test : tests) {
                Optional<Test> testOptional = testRepository.findById(test.getId());
                if (testOptional.isPresent()) {
                    Test applicableTest = testOptional.get();
                    SbuCameraTestMapping testCameraMap = new SbuCameraTestMapping();
                    testCameraMap.setTestId(applicableTest);
                    testCameraMap.setCamId(camera);
                    sbuCameraTestMappingRepository.save(testCameraMap);
                } else {
                	logger.error("Test with ID {} not found.  Skipping.", test.getId());
                }
            }
        }
        return "Data Updated";
    }
}

