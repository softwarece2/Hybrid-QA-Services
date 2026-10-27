package in.co.bel.olms.transaction.service;

import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.transaction.dto.QacDistributionDto;
import in.co.bel.olms.transaction.entity.INote;
import in.co.bel.olms.transaction.entity.QacDistribution;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.QacDistributionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QacDistributionService {

	private final QacDistributionRepository qacDistributionRepository;

	private final INoteRepository iNoteRepository;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(QacDistributionService.class);
	
	@Transactional
	public ResponseEntity<String> saveQacDistribution(QacDistributionDto qacDistributionDto) {
		if(qacDistributionDto.getInoteId() == null ) {
			throw new IllegalArgumentException("INote Id cannot be null ");
		}
		try {
			INote existingInote = iNoteRepository.findById(qacDistributionDto.getInoteId()).orElse(null);
			if(existingInote == null) {
				throw new IllegalArgumentException("INote does not exist");
			}else {
				QacDistribution newQacDistribution = new QacDistribution();
				newQacDistribution= mapper.map(qacDistributionDto, QacDistribution.class);
				String distributionNames = qacDistributionDto.getDistributionName().stream()
					    .filter(name -> name != null && !name.isEmpty()) // Optional: filter out nulls/empty strings
					    .collect(Collectors.joining(", "));
				
				newQacDistribution.setDistributionName(distributionNames);
				qacDistributionRepository.save(newQacDistribution);
				return ResponseEntity.ok("Data Submitted successfully");
			}
			
		}catch(Exception e) {
			logger.error("error saving warrant details: {}" , e.getMessage());
			return ResponseEntity.internalServerError().body("error saving warrant details" + e.getMessage());
		}
		

	}

}
