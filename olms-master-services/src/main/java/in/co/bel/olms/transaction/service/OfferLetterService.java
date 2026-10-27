package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferLetterService {

	private final OfferLetterRepository offerLetterRepository;

	public OfferLetter getOfferLetterById(int id) {

		Optional<OfferLetter> offerLetterOptional = offerLetterRepository.findById(id);
		if (offerLetterOptional.isPresent()) {
			return offerLetterOptional.get();
		} else {
			throw new DataNotFoundException("OfferLetter not found by Id: " + id);
		}
	}

	public List<OfferLetter> getAllOfferLetter() {
		return offerLetterRepository.findAll();
	}
}
