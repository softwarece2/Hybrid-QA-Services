package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.dto.IsAteDto;
import in.co.bel.olms.master.dto.OrgCqaeMappingDto;
import in.co.bel.olms.master.service.OrgCqaeMappingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orgCqae")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
@RequiredArgsConstructor
public class OrgCqaeMappingController {

	private final OrgCqaeMappingService orgCqaeMappingService;
	
	private static final Logger logger = LoggerFactory.getLogger(OrgCqaeMappingController.class);

	@GetMapping("/getOrgCqaeById")
	 public ResponseEntity<List<OrgCqaeMappingDto>> getOrgCqaeById() {
		try {
			return ResponseEntity.ok(orgCqaeMappingService.getOrgCqae());
		}
		catch (Exception e) {
            logger.error("Error fetching Inspection Agency by id: {} {}",   e.getMessage(), e);
            throw e;
        }
	}

	@GetMapping("/getOrgIbIgById")
	 public ResponseEntity<List<OrgCqaeMappingDto>> getOrgIbIgById() {
		try {
			return ResponseEntity.ok(orgCqaeMappingService.getOrgIbIg());
		}
		catch (Exception e) {
            logger.error("Error fetching firms by id: {} {}",   e.getMessage(), e);
            throw e;
        }
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> saveOrgInspectionAgencyMapping(@RequestBody OrgCqaeMappingDto orgCqaeMappingDto) {
	    Map<Boolean, String> savedStatus = orgCqaeMappingService.saveOrgInspectionAgencyMapping(orgCqaeMappingDto);
	    if (savedStatus.containsKey(true)) {
	        return new ResponseEntity<>(savedStatus.get(true), HttpStatus.CREATED);
	    } else {
	    	return new ResponseEntity<>(savedStatus.get(false), HttpStatus.CONFLICT);
	    }
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateOrgInspectionAgencyMapping(@RequestBody OrgCqaeMappingDto orgCqaeMappingDto) {
	    Map<Boolean, String> savedStatus = orgCqaeMappingService.updateOrgInspectionAgencyMapping(orgCqaeMappingDto);
	    
	    if (savedStatus.containsKey(true)) {
	        return new ResponseEntity<>(savedStatus.get(true), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(savedStatus.get(false), HttpStatus.BAD_REQUEST);
	    }
	}
	
	@GetMapping("/getAteById/{id}")
	 public ResponseEntity<List<IsAteDto>> getAteById(@PathVariable int id) {
		try {
			List<IsAteDto> ateDtos = orgCqaeMappingService.getAtebyOlId(id);
			return ResponseEntity.ok(ateDtos);
		}
		catch (Exception e) {
           logger.error("Error fetching firms by id: {} {}",   e.getMessage(), e);
           throw e;
       }
	}
	
	@GetMapping("/getFirmAndAssociatedInspectionAgencies")
	public String getFirmAndAssociatedInspectionAgencies() {
		String returnVal = "";
		Gson gson = new Gson();
		try {
			List<OrgCqaeMappingDto> cqaeMappingDtos = new ArrayList<>();
			List<Object[]> getResponse = orgCqaeMappingService.getOrgAndInspAgencyIds();
			if (!getResponse.isEmpty()) {
				for (Object[] item : getResponse) {
					OrgCqaeMappingDto dto = new OrgCqaeMappingDto();
					dto.setId((int) item[0]);
					dto.setOrganizationName(item[1] != null ? item[1].toString() : null);
					if (item[2] != null) {

						List<Integer> inspAgencyIds = Stream.of(item[2].toString().split(",")).map(Integer::parseInt)
								.collect(Collectors.toList());
						List<Object[]> agencyData = orgCqaeMappingService.getInspAgencyDetailsByIdList(inspAgencyIds);
						List<OrgCqaeMappingDto> agencyDtoList = new ArrayList<>();
						for (int i = 0; i < agencyData.size(); i++) {
							Object[] agency = agencyData.get(i);
							OrgCqaeMappingDto agencyDto = new OrgCqaeMappingDto();
							agencyDto.setId((int) agency[0]);
							agencyDto.setOrganizationName(item[1] != null ? agency[1].toString() : null);
							agencyDtoList.add(agencyDto);
						}

						dto.setInspectionAgencies(agencyDtoList);
						cqaeMappingDtos.add(dto);
					}

				}
			}
			returnVal = gson.toJson(cqaeMappingDtos);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			returnVal = ApplicationConstants.FAILURE;
		}
		return returnVal;
	}
}
