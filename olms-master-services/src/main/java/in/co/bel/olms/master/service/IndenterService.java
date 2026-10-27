package in.co.bel.olms.master.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.IndenterDto;
import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.IndenterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndenterService {

	private final IndenterRepository indenterRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @param indenterDto
	 * @return
	 */
	public Indenter saveIndenter(IndenterDto indenterDto) {

		Indenter indenter = mapper.map(indenterDto, Indenter.class);
		indenter.setCreatedDate(new Date());
		return indenterRepository.save(indenter);

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Indenter getIndenter(int id) {

		Optional<Indenter> indenterOptional = indenterRepository.findById(id);
		if (indenterOptional.isPresent()) {
			return indenterOptional.get();
		} else {
			throw new DataNotFoundException("Indenter not found with Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<Indenter> getAllIndenters() {

		List<Indenter> result = indenterRepository.findAll();
		result.sort(Comparator.comparing(Indenter::getName));

		return result;

	}

	public Indenter updateIndenter(IndenterDto indenterDto) {

		Optional<Indenter> indenterOptional = indenterRepository.findById(indenterDto.getId());
		if (indenterOptional.isPresent()) {
			Indenter indenter = indenterOptional.get();

			if (!indenterDto.getName().equals(indenter.getName())) {

				if (indenterRepository.existsByname(indenterDto.getName())) {
					throw new IllegalArgumentException("Indenter name already exists.");
				}
			}

			if (!indenterDto.getShortName().equals(indenter.getShortName())) {

				if (indenterRepository.existsByshortName(indenterDto.getShortName())) {
					throw new IllegalArgumentException("Indenter Shortname already exists.");
				}
			}

			indenter.setActive(indenterDto.getActive());
			indenter.setAddress(indenterDto.getAddress());
			indenter.setShortName(indenterDto.getShortName());
			indenter.setModifiedDate(new Date());
			indenter.setName(indenterDto.getName());

			return indenterRepository.save(indenter);

		} else {
			throw new DataNotFoundException("Indenter not found with Id: " + indenterDto.getId());
		}

	}

	/**
	 * 
	 * @return
	 */
	public List<Indenter> getAllActiveIndenters() {

		return indenterRepository.findAllActiveIndenter();

	}

	/**
	 * 
	 * @return
	 */
	public Boolean existsByname(String name) {
		return indenterRepository.existsByname(name);
	}

	public Boolean existsByshortName(String shortName) {
		return indenterRepository.existsByshortName(shortName);
	}

}
