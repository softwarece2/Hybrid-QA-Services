package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.WarrantType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.WarrantTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarrantTypeService {

	private final WarrantTypeRepository warrantTypeRepository;
	
	
	public List<WarrantType> getAllWarrantType() {		
		return warrantTypeRepository.findAll();
	}

	public WarrantType getWarrantTypeById(int id) {
		Optional<WarrantType> warrantType = warrantTypeRepository.findById(id);
		if (warrantType.isPresent()) {
			return warrantType.get();
		} else {
			throw new DataNotFoundException("WarrantType not present for the warrant Type Id " + id);
		}
	}
	
}
