package in.co.bel.olms.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.co.bel.olms.transaction.dto.OlConcessionDto;
import in.co.bel.olms.transaction.entity.OlConcession;
import in.co.bel.olms.transaction.service.OlConcessionService;
import lombok.RequiredArgsConstructor;

/**
 * OlConcessionController class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-07-06
 */

@RestController
@RequestMapping("/olConcession")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlConcessionController {

	private final OlConcessionService olConcessionService;

	private final Mapper mapper;
	
	private static final Logger logger =LoggerFactory.getLogger(OlConcessionController.class);

	private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @GetMapping("/getByOlId")
    public ResponseEntity<List<OlConcessionDto>> getOLConcessionsByOlIds(@RequestParam(value = "olIds", required = true) List<Integer> olIds) {
        List<OlConcessionDto> olConcessionDtos = new ArrayList<>();
        List<OlConcession> olConcessions = olConcessionService.getOLConcessionsByOlIds(olIds);
        for (OlConcession olConcession : olConcessions) {
            OlConcessionDto olConcessionDto = mapper.map(olConcession, OlConcessionDto.class);
            olConcessionDto.setCreatedDateStr(dateFormat.format(olConcession.getCreatedDate()));
            olConcessionDto.setOlNumber(olConcession.getOfferLetter().getOlNumber());
            olConcessionDtos.add(olConcessionDto);
        }
        return ResponseEntity.ok(olConcessionDtos);
    }
	
	@PostMapping("/save")
	public ResponseEntity<String> saveOlConcession(@RequestBody @Valid List<OlConcessionDto> olConcessionDtos) {
		Map<Boolean, String> response = olConcessionService.saveOlConcession(olConcessionDtos);
		if (response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);
		} else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
