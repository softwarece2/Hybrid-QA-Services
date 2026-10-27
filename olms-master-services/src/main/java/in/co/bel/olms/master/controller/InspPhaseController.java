package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.InspPhaseAcitivitiesDto;
import in.co.bel.olms.master.dto.InspPhaseDto;
import in.co.bel.olms.master.entity.InspPhaseActivities;
import in.co.bel.olms.master.service.InspPhaseService;
import lombok.RequiredArgsConstructor;

// Related to work order assessment code
@RestController
@RequestMapping("/inspPhase")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspPhaseController {

	private final InspPhaseService inspPhaseService;

	private final Mapper mapper;

	Gson gson = new Gson();
	
	
	
	@GetMapping("/getAllInspPhaseActivities")
	public ResponseEntity<InspPhaseDto> getAllInspPhaseActivities(){
		try {
			List<InspPhaseActivities> activities = inspPhaseService.getAllInspPhaseaActivities();
			return ResponseEntity.ok(getGroupedSortedInPhases(activities));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	
	@GetMapping("/getAllPreInspPhaseActivities")
	public ResponseEntity<InspPhaseDto> getAllPreInspPhaseActivities(){
		try {			
			List<InspPhaseActivities> activities = inspPhaseService.getPreInspPhaseActivities();
			return ResponseEntity.ok(getGroupedSortedInPhases(activities));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	private InspPhaseDto getGroupedSortedInPhases(List<InspPhaseActivities> activities) {
		String regex = "[0-9]+";  // regex to check if string contains only digits
	    Pattern pattern = Pattern.compile(regex);  // compiles the regex
		List<InspPhaseAcitivitiesDto> acitivitiesDtos = new ArrayList<>();
		for(InspPhaseActivities act : activities) {
			InspPhaseAcitivitiesDto dto = mapper.map(act, InspPhaseAcitivitiesDto.class);
	        Matcher matcherText = pattern.matcher(dto.getSlNo());
	        dto.setBold(matcherText.matches());
			acitivitiesDtos.add(dto);
		}
		acitivitiesDtos.sort(Comparator.comparing(InspPhaseAcitivitiesDto::getSlNo));
		InspPhaseDto inspPhaseDto = new InspPhaseDto();
		inspPhaseDto.setPrePhase(acitivitiesDtos.stream().filter(item -> item.getInspPhaseId() == 1).collect(Collectors.toList()));
		inspPhaseDto.setInPhase(acitivitiesDtos.stream().filter(item -> item.getInspPhaseId() == 2).collect(Collectors.toList()));
		inspPhaseDto.setPostPhase(acitivitiesDtos.stream().filter(item -> item.getInspPhaseId() == 3).collect(Collectors.toList()));		
		return inspPhaseDto;
	}
}
