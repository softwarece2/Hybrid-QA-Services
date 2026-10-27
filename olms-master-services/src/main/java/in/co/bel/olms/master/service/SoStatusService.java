package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SoStatusRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoStatusService {

	private final SoStatusRepository soStatusRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public SoStatus getSoStatusById(int id) {
		Optional<SoStatus> soStatusOptional = soStatusRepository.findById(id);
		if (soStatusOptional.isPresent()) {
			return soStatusOptional.get();
		} else {
			throw new DataNotFoundException("SoStatus not found by Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<SoStatus> getAllSoStatus() {
		return soStatusRepository.findAll();
	}
}
