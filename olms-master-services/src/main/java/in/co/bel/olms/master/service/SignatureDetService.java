package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.SignatureDet;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SignatureDetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignatureDetService {

	private final SignatureDetRepository signatureDetRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public SignatureDet getSignatureDetById(int id) {
		Optional<SignatureDet> signatureDetOptional = signatureDetRepository.findById(id);
		if(signatureDetOptional.isPresent()) {
			return signatureDetOptional.get();
		} else {
			throw new DataNotFoundException("signatureDet not found by Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SignatureDet> getAllSignatureDet() {
		return signatureDetRepository.findAll();
	}

}
