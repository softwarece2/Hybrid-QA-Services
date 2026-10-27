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
import in.co.bel.olms.master.entity.QapReference;
import in.co.bel.olms.master.service.QapReferenceService;
import lombok.RequiredArgsConstructor;

/**
* Controller class for QapReference
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-17 
*/

@RestController
@RequestMapping("/qapRef")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class QapReferenceController {

	private final QapReferenceService qapReferenceService;
	
	private static final Logger logger = LoggerFactory.getLogger(QapReferenceController.class);

	@GetMapping("/getAll")
	public ResponseEntity<Set<KeyValuePair>> getQapReferences() {

		Set<KeyValuePair> keyValuePairs = new HashSet<>();
		try {
			List<QapReference> qapReferences = qapReferenceService.getQapReferences();
			for (QapReference qapReference : qapReferences) {
				KeyValuePair keyValuePair = new KeyValuePair();
				keyValuePair.setId(qapReference.getId());
				keyValuePair.setName(qapReference.getSystemName());
				keyValuePairs.add(keyValuePair);
			}
			return ResponseEntity.ok(keyValuePairs);
		}
		catch (Exception e) {
			logger.error("Error in fetching QAP Reference due to: {}",e.getMessage());
			return new ResponseEntity<>(keyValuePairs, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
