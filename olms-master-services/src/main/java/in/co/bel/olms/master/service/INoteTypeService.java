package in.co.bel.olms.master.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.SupplierDto;
import in.co.bel.olms.master.entity.INoteType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.INoteTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class INoteTypeService {

	private final INoteTypeRepository iNoteTypeRepository;

	private final Mapper mapper;

	
	/**
	 * 
	 * @param iNoteTypeDto
	 * @return
	 */
	public INoteType saveINoteType(SupplierDto iNoteTypeDto) {

		INoteType iNoteType = mapper.map(iNoteTypeDto, INoteType.class);
		iNoteType.setCreatedDate(new Date());
		return iNoteTypeRepository.save(iNoteType);

	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public INoteType getINoteType(int id) {

		Optional<INoteType> iNoteTypeOptional = iNoteTypeRepository.findById(id);
		if (iNoteTypeOptional.isPresent()) {
			return iNoteTypeOptional.get();
		} else {
			throw new DataNotFoundException("INoteType not found with Id: " + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<INoteType> getAllINoteTypes() {

		return iNoteTypeRepository.findByStatus(true);

	}

}
