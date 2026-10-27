package in.co.bel.olms.transaction.service;

import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.EnvTestReviewDto;
import in.co.bel.olms.transaction.entity.EnvTestReview;
import in.co.bel.olms.transaction.repository.EnvTestReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvTestReviewService {

	private final EnvTestReviewRepository envTestReviewRepo;

	private final Mapper mapper;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public EnvTestReview getEnvTestReviewById(int id) {

		Optional<EnvTestReview> envTestOptional = envTestReviewRepo.findById(id);
		if (envTestOptional.isPresent()) {
			return envTestOptional.get();
		} else {
			throw new DataNotFoundException("EnvTestReview not found by Id: " + id);
		}
	}
	
	/**
	 * 
	 * @param envTestReviewDto
	 * @return
	 */
	public EnvTestReview saveEnvTestReview(EnvTestReviewDto envTestReviewDto) {
		EnvTestReview envTestReview = mapper.map(envTestReviewDto, EnvTestReview.class);
		return envTestReviewRepo.save(envTestReview);
	}

}
