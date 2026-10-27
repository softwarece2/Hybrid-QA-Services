package in.co.bel.olms.master.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.entity.InspectionLevel;
import in.co.bel.olms.master.service.InspectionLevelService;
import lombok.RequiredArgsConstructor;

/**
* Controller class for InspectionLevel
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-12 
*/

@RestController
@RequestMapping("/inspLevel")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspectionLevelController {

	private final InspectionLevelService inspLevelService;
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionLevelController.class);

	@GetMapping("/getAll")
	public ResponseEntity<Set<KeyValuePair>> getInspectionLevels() {

		Set<KeyValuePair> keyValuePairs = new HashSet<>();
		try {
			List<InspectionLevel> inspectionLevels = inspLevelService.getInspectionLevels();
			for (InspectionLevel inspectionLevel : inspectionLevels) {
				KeyValuePair keyValuePair = new KeyValuePair();
				keyValuePair.setId(inspectionLevel.getId());
				keyValuePair.setName(inspectionLevel.getLevelOfInspection());
				keyValuePairs.add(keyValuePair);
			}
			return ResponseEntity.ok(keyValuePairs);
		}
		catch (Exception e) {
			logger.error("Error in fetching inspection levels due to: {}",e.getMessage());
			return new ResponseEntity<>(keyValuePairs, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
