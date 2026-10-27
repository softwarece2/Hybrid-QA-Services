package in.co.bel.olms.master.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.InspectionAuthorityDto;
import in.co.bel.olms.master.entity.InspectionAuthority;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.InspectionAuthorityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionAuthorityService {

	private final InspectionAuthorityRepository inspectionAuthRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @param inspectionAuthorityDto
	 * @return
	 */
	public InspectionAuthority saveInspectionAuthority(InspectionAuthorityDto inspectionAuthorityDto) {

		InspectionAuthority inspectionAuthority = mapper.map(inspectionAuthorityDto, InspectionAuthority.class);
		inspectionAuthority.setCreatedDate(new Date());
		return inspectionAuthRepository.save(inspectionAuthority);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public InspectionAuthority getInspectionAuthority(int id) {

		Optional<InspectionAuthority> inspectionAuthorityOptional = inspectionAuthRepository.findById(id);
		if (inspectionAuthorityOptional.isPresent()) {
			return inspectionAuthorityOptional.get();
		} else {
			throw new DataNotFoundException("InspectionAuthority not found with Id: " + id);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<InspectionAuthority> getAllInspectionAuthorities() {
		return inspectionAuthRepository.findAll();
	}
}
