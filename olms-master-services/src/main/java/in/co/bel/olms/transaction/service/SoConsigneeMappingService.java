package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.SoConsigneeMapping;
import in.co.bel.olms.transaction.repository.SoConsigneeMappingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoConsigneeMappingService {

	private final SoConsigneeMappingRepository soConsigneeMappingRepository;
	
	public SoConsigneeMapping getSoConsigneeMappingById(int id) {
		Optional<SoConsigneeMapping> soConsigneeMappingOptional = soConsigneeMappingRepository.findById(id);
			if(soConsigneeMappingOptional.isPresent()) {
				return soConsigneeMappingOptional.get();
			} else {
				throw new DataNotFoundException("SoConsigneeMapping not found by Id:" + id);
			}
	}
	
	public List<SoConsigneeMapping> getAllSoConsigneeMapping() {
		return soConsigneeMappingRepository.findAll();
	}
}
