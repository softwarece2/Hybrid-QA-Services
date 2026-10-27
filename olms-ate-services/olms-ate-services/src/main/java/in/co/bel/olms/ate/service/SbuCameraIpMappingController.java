package in.co.bel.olms.ate.service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.ate.constants.ApplicationConstants;
import in.co.bel.olms.ate.dto.SbuCameraIpMappingDto;
import in.co.bel.olms.ate.dto.SbuCameraTestMappingDto;
import in.co.bel.olms.master.entity.SbuCameraIpMapping;
import in.co.bel.olms.master.repository.SbuCameraIpMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sbuCameraMapping")
@RequiredArgsConstructor
public class SbuCameraIpMappingController {

	private final SbuCameraIpMappingRepository sbuCameraIpMappingRepository;

	private final SbuCameraTestMappingService sbuCameraTestMappingService;

	@Value("${file.camera-domain-url}")
	private String cameraDomainUrl;

//	@PostMapping("/save")
//	public ResponseEntity<Object> save(@RequestBody List<SbuCameraIpMappingDto> dtoList) {
//		try {
//			List<SbuCameraIpMapping> mappingList = dtoList.stream().map(dto -> {
//				SbuCameraIpMapping mapping = mapper.map(dto, SbuCameraIpMapping.class);
//				mapping.setCreatedDatetime(new Date());
//				return mapping;
//			}).collect(Collectors.toList());
//			sbuCameraIpMappingRepository.saveAll(mappingList);
//			return ResponseEntity.ok(ApplicationConstants.SUCCESS);
//		} catch (Exception e) {
//			log.error("/videoLiMapping/save Exception: {}", e.getMessage());
//			return ResponseEntity.internalServerError().body("Internal Server Error");
//		}
//	}
//	
	@PostMapping("/save")
	public ResponseEntity<String> saveByTests(@RequestBody List<SbuCameraIpMappingDto> dtoLists) {
//    	List<SbuCameraIpMappingDto> partNumIpMappingDtos = gson.fromJson(dtos, new TypeToken<List<SbuCameraIpMappingDto>>() {
//        }.getType());
		String result = sbuCameraTestMappingService.save(dtoLists);
		
		if (result.startsWith("CONFLICT:")) {
	        // Remove the "CONFLICT:" prefix and return 409
	        String errorMessage = result.substring("CONFLICT:".length());
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
	    } else if (result.startsWith("SUCCESS:")) {
	        // Remove the "SUCCESS:" prefix and return 201
	        String successMessage = result.substring("SUCCESS:".length());
	        return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
	    } else {
	        // Fallback for any other string
	        return ResponseEntity.ok(result);
	    }
	}

//	
//	@GetMapping("/getBySaleGrpId")
//	public ResponseEntity<Object> getBySaleGrpId(@RequestParam Integer salesGrpId) {
//		try {
//			List<SbuCameraIpMapping> mappingList = sbuCameraIpMappingRepository.findBySalesGrpId(salesGrpId);
//			List<SbuCameraIpMappingDto> dtoList = mappingList.stream().map(dto -> mapper.map(dto, SbuCameraIpMappingDto.class)).collect(Collectors.toList());
//			return ResponseEntity.ok(dtoList);
//		} catch (Exception e) {
//			log.error("/videoLiMapping/getBySaleGrpId Exception: {}", e.getMessage());
//			return ResponseEntity.internalServerError().body("Internal Server Error");
//		}
//	}

	@GetMapping("/getAll")
	public ResponseEntity<Object> getAll() {
		List<SbuCameraIpMappingDto> result = sbuCameraTestMappingService.get();
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@PostMapping("/update")
	public ResponseEntity<Object> updateBySaleGrpId(@RequestBody SbuCameraIpMappingDto dto) {
		String result = sbuCameraTestMappingService.update(dto);
		if (result.startsWith("Update failed") || result.equals("Camera not found.")) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	    }
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@PostMapping("/edit")
	public ResponseEntity<Object> edit(@RequestBody SbuCameraIpMappingDto dto) {
		try {
			Optional<SbuCameraIpMapping> optional = sbuCameraIpMappingRepository.findById(dto.getId());
			if (optional.isPresent()) {
				SbuCameraIpMapping sbuCameraIpMapping = optional.get();
				sbuCameraIpMapping.setCameraIp(dto.getCameraIp());
				sbuCameraIpMapping.setSbuName(dto.getSbuName());
				sbuCameraIpMapping.setModifiedDatetime(new Date());
				sbuCameraIpMappingRepository.save(sbuCameraIpMapping);
				return ResponseEntity.ok(ApplicationConstants.SUCCESS);
			} else
				return ResponseEntity.badRequest().body("Not found");
		} catch (Exception e) {
			log.error("/videoLiMapping/getBySaleGrpId Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}
	
	
	@GetMapping("/getByIpList/{testId}")
	public ResponseEntity<Object> save(@PathVariable int testId) {
		try {
			// Get IP Camera List from VideoInspection Code
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON);
			headers.set("accept", MediaType.APPLICATION_JSON);
			
			Map<String, Integer> payload = new HashMap<>();
			payload.put("user_id", 4);
	        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(payload, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(cameraDomainUrl + "get_cams", requestEntity, 
					String.class);

			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, SbuCameraTestMappingDto>>() {
			}.getType();
			Map<String, SbuCameraTestMappingDto> res = gson.fromJson(responseEntity.getBody(), type);
			Set<String> ipList = res.keySet();
			
			
			List<Object[]> ipMap = sbuCameraIpMappingRepository.getByIPListandTest(ipList, testId);
			
			Map<String, SbuCameraTestMappingDto> map = new HashMap<>(); 
			for(Object[] objects : ipMap) {
				SbuCameraTestMappingDto dto = new SbuCameraTestMappingDto();
				dto.setName(objects[1].toString());
				String ip = objects[0].toString();
				SbuCameraTestMappingDto pyResDto = res.get(ip);
				dto.setUrl(pyResDto.getUrl());
				map.put(ip, dto);
			}
			
			return ResponseEntity.ok(map);

		} catch (Exception e) {
			log.error("/videoLiMapping/getByIpList Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}
	
	
//===============================Code for Rajesh Python Camera code=================================
//	@GetMapping("/getByIpList/{testId}")
//	public ResponseEntity<Object> save(@PathVariable int testId) {
//		try {
//			// Get IP Camera List from VideoInspection Code
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Content-Type", MediaType.APPLICATION_JSON);
//			headers.set("accept", MediaType.APPLICATION_JSON);
//			
//			Map<String, Integer> payload = new HashMap<>();
//			payload.put("user_id", 4);
//	        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(payload, headers);
//
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<String> responseEntity = restTemplate.postForEntity(cameraDomainUrl + "get_cams", requestEntity, 
//					String.class);
//
//			Gson gson = new Gson();
//			Type type = new TypeToken<Map<String, Object>>() {
//			}.getType();
//			SbuCameraTestMappingDto ipCameraList = gson.fromJson(responseEntity.getBody(), SbuCameraTestMappingDto.class);
////			Map<String, List<String>> ipCameraList1 = new HashMap<>();
////			List<String> ipCameras = new ArrayList<>(Arrays.asList("120.0.0.25", "120.0.0.64", "120.0.0.44"));
////			ipCameraList.put("ip_cams", ipCameras);
////
////			Integer testId = 23;
////			List<Integer> testList = new ArrayList<>(Arrays.asList(23, 24, 25, 26));
////			List<Object[]> ipMap = sbuCameraIpMappingRepository.getByIPList(ipCameraList.get("ip_cams"));
//
////			List<Object[]> ipMap = sbuCameraIpMappingRepository.getByIPListandTest(ipCameraList.get("ip_cams"), testId);
////			List<Object[]> usbMap = sbuCameraIpMappingRepository.getByIPListandTest(ipCameraList.get("usb_cams"),
////					testId);
////			List<Object[]> usbMap = sbuCameraIpMappingRepository.getByIPList(ipCameraList.get("usb_cams"));
////			System.out.print(ipMap1);
////			System.out.print(ipMap1);
//
//			Map<String, Object> map = new HashMap<>();
////			map.putAll(ipCameraList);
//			Map<String, String> tempIPMap = new HashMap<>();
//			List<String> tempIPCam = new ArrayList<>();
//			for (Object[] objects : ipMap) {
//				tempIPMap.put(objects[0].toString(), objects[1].toString());
//				System.out.printf(objects[0].toString(), objects[1].toString());
//				tempIPCam.add(objects[0].toString());
//			}
//			Map<String, String> tempUSBMap = new HashMap<>();
//			List<String> tempUSBCam = new ArrayList<>();
//			for (Object[] objects : usbMap) {
//				tempUSBMap.put(objects[0].toString(), objects[1].toString());
//				tempUSBCam.add(objects[0].toString());
//			}
//			map.put("ip_cams", tempIPCam);
//			map.put("usb_cams", tempUSBCam);
//			map.put("ip_map", tempIPMap);
//			map.put("usb_map", tempUSBMap);
//			return ResponseEntity.ok(map);
//
//		} catch (Exception e) {
//			log.error("/videoLiMapping/getByIpList Exception: {}", e.getMessage());
//			return ResponseEntity.internalServerError().body("Internal Server Error");
//		}
//	}
//	
//	@GetMapping("/getCamsByTestId")
//	public ResponseEntity<Object> getAllCams(@RequestParam Integer testId) {
//		try {
//			List<SbuCameraTestMapping> mapping = sbuCameraTestMappingRepository.findByTestId(testId);
//			List<SbuCameraIpMapping> cameras =  new ArrayList<>();
//			for(SbuCameraTestMapping map: mapping) {
//				SbuCameraIpMapping camera = map.getCamId();
//				if(camera != null) {
//					cameras.add(camera);
//				}
//			}
//			List<SbuCameraIpMappingDto> dtoList = cameras.stream().map(dto -> mapper.map(dto, SbuCameraIpMappingDto.class)).collect(Collectors.toList());
//			return ResponseEntity.ok(dtoList);
//		} catch (Exception e) {
//			log.error(" Exception: {}", e.getMessage());
//			return ResponseEntity.internalServerError().body("Internal Server Error");
//		}
//	}

}
