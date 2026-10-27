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

import in.co.bel.olms.transaction.dto.CoveringLetterDto;
import in.co.bel.olms.transaction.dto.CoveringLetterPointDto;
import in.co.bel.olms.transaction.entity.CoveringLetter;
import in.co.bel.olms.transaction.entity.CoveringLetterPoint;
import in.co.bel.olms.transaction.repository.CoveringLetterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoveringLetterService {

	private final CoveringLetterRepository coveringLetterRepo;

	private static final Logger logger = LoggerFactory.getLogger(CoveringLetterService.class);

	private final Mapper mapper;

//	public OlLineItem getOlLineItemById(int id) {
//		Optional<OlLineItem> olLineItemOptional = coveringLetterRepo.findById(id);
//		if (olLineItemOptional.isPresent()) {
//			return olLineItemOptional.get();
//		} else {
//			throw new DataNotFoundException("OlLineItem not found By Id: " + id);
//
//		}
//	}
	
	public Map<Boolean, String> save(CoveringLetterDto coveringLetterDto) {
		Map<Boolean, String> returnMap = new HashMap<>();
		try {
			CoveringLetter coveringLetter = mapper.map(coveringLetterDto, CoveringLetter.class);
			List<CoveringLetterPointDto> coveringLetterPointDtos = coveringLetterDto.getCoveringLetterPointDtos();
			List<CoveringLetterPoint> clearanceLetterPoints = new ArrayList<>();
			for (CoveringLetterPointDto coveringLetterPointDto : coveringLetterPointDtos) {
				CoveringLetterPoint coveringLetterPoint = mapper.map(coveringLetterPointDto, CoveringLetterPoint.class);
				coveringLetterPoint.setCoveringLetter(coveringLetter);;
				clearanceLetterPoints.add(coveringLetterPoint);
			}
			coveringLetter.setCoveringLetterPoints(clearanceLetterPoints);
			coveringLetter.setCreatedDate(new Date());
			coveringLetterRepo.save(coveringLetter);
			String respString = "Data saved successfully";
			returnMap.put(true, respString);
			return returnMap;
		} catch (Exception e) {
			logger.error("Error occured during saving  ", e);
			logger.error("Error in saving clearance letter {}", e.getMessage());
			String respString = "Some error occurred. Plz try again.";
			returnMap.put(false, respString);
			return returnMap;
		}
	}
	
	public CoveringLetterDto  getClearanceLetter(int id) {
		CoveringLetter coveringLetter = coveringLetterRepo.findByFileNoteId(id);
			CoveringLetterDto coveringLetterDto=mapper.map(coveringLetter, CoveringLetterDto.class);
			List<CoveringLetterPointDto> coveringLetterPointDto=new ArrayList<>();
			for(CoveringLetterPoint cvrPoint: coveringLetter.getCoveringLetterPoints()) {	
				CoveringLetterPointDto cvrPointDto=mapper.map(cvrPoint, CoveringLetterPointDto.class);			
				coveringLetterPointDto.add(cvrPointDto);
				}
			coveringLetterDto.setCoveringLetterPointDtos(coveringLetterPointDto);

			return coveringLetterDto;
	}
}
