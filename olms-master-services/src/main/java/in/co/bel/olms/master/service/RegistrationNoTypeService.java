package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.RegistrationNoType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.RegistrationNoTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationNoTypeService {

	private final RegistrationNoTypeRepository registrationNoTypeRepository;

	public List<RegistrationNoType> getAllRegistrationNoType() {
		return registrationNoTypeRepository.findAll();
	}

	public RegistrationNoType getRegistrationNoTypeById(int id) {
		Optional<RegistrationNoType> regType = registrationNoTypeRepository.findById(id);
		if (regType.isPresent()) {
			return regType.get();
		} else {
			throw new DataNotFoundException("RegistrationNoType not present for the RegistrationNoType Id " + id);
		}
	}
}
