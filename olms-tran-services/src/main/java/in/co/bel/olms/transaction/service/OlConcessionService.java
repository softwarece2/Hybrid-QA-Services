package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.OlConcessionDto;
import in.co.bel.olms.transaction.entity.OlConcession;
import in.co.bel.olms.transaction.repository.OlConcessionRepository;
import lombok.RequiredArgsConstructor;

/**
 * OlConcessionService service class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-07-06
 */

@Service
@RequiredArgsConstructor
public class OlConcessionService {

	private final OlConcessionRepository olConcessionRepository;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(OlConcessionService.class);

	public Map<Boolean, String> saveOlConcession(List<OlConcessionDto> olConcessionDtos) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			List<OlConcession> olConcessions = new ArrayList<>();
			for (OlConcessionDto olConcessionDto : olConcessionDtos) {
				OlConcession olConcession = mapper.map(olConcessionDto, OlConcession.class);
				olConcession.setCreatedDate(todayDate);
				olConcessions.add(olConcession);
			}
			olConcessionRepository.saveAll(olConcessions);
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (Exception e) {
			logger.error("Error in saving Concession details {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public List<OlConcession> getOLConcessionsByOlIds(List<Integer> olIds) {
		return olConcessionRepository.findByOfferLetterId(olIds);
	}

}
