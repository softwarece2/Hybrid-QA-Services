package in.co.bel.olms.transaction.service.so;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.SoPartList;
import in.co.bel.olms.transaction.repository.SoPartListRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoPartListService {

	private final SoPartListRepository soPartListRepository;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public SoPartList getSoPartListById(int id) {
		Optional<SoPartList> soPartListOptional = soPartListRepository.findById(id);
		if(soPartListOptional.isPresent()) {
			return soPartListOptional.get();
		} else {
			throw new DataNotFoundException("SoPartList not found by Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SoPartList> getAllSoPartList() {
		return soPartListRepository.findAll();
	}
}
