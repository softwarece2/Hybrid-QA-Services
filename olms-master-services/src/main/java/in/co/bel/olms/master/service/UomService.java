package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.UomDto;
import in.co.bel.olms.master.entity.Uom;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.UomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UomService {

	private final UomRepository uomRepository;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Uom getUomById(int id) {
		Optional<Uom> uomOptional = uomRepository.findById(id);
		if(uomOptional.isPresent()) {
			return uomOptional.get();
		} else {
			throw new DataNotFoundException("UOM not found by Id: " +id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Uom> getAllUom() {
		return uomRepository.findAll();
	}
	
	public Boolean existsByUnit(String unit) {
		return uomRepository.existsByUnit(unit);
	}
	
	public Uom saveUom(UomDto uomDto) {

		Uom uom = mapper.map(uomDto, Uom.class);
		return uomRepository.save(uom);

	}

	public Uom updateUom(UomDto uomDto) {

			Optional<Uom> uomOptional = uomRepository.findById(uomDto.getId());
			if (uomOptional.isPresent()) {
				Uom uom =  uomOptional.get();
				uom.setActive(uomDto.getActive());
				uom.setUnit(uomDto.getUnit());
				
				return uomRepository.save(uom);
			
		} else {
			throw new DataNotFoundException("Uom not found with Id: " + uomDto.getId());
		}

	}
	
	public List<Uom> getAllActiveUom() {
		return uomRepository.findAllActiveUom();
	}


	public Boolean existsByUnitAndIdNot(String unit, int id) {
		return uomRepository.existsByUnitAndIdNot(unit,id);
	}
}
