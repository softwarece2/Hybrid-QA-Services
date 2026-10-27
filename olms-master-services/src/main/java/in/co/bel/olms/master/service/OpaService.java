package in.co.bel.olms.master.service;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.OpaDto;
import in.co.bel.olms.master.entity.Opa;
import in.co.bel.olms.master.repository.OpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpaService {

	private final OpaRepository opaRepository;

	private final Mapper mapper;
	
	public Opa saveOpa(OpaDto opaDto) {
		Opa opa = mapper.map(opaDto, Opa.class);
		return  opaRepository.save(opa);
	}
	
	public List<Opa> getAllOpa() {		
		return opaRepository.findAll();
	}
	
	public Boolean existsByopaName(String opaName) {
		return opaRepository.existsByopaName(opaName);
	}
	
	public Boolean existsByopashortName(String opashortName) {
		return opaRepository.existsByopashortName(opashortName);
	}
	
}
