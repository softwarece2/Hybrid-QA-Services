package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.dto.Dash.GraphSpecDto;
import in.co.bel.olms.ate.dto.Dash.SpecDto;
import in.co.bel.olms.ate.repository.SpecRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AteDashService {

	private final SpecRepository specRepository;

	public GraphSpecDto getGraphSpecs(Integer productId, Integer testId, Integer loginId) {
		GraphSpecDto dto = new GraphSpecDto();
	    Object count = specRepository.getGraphCountByProdIdTestId(productId, testId);
		List<Object[]> specs = specRepository.getAllSpecsFrGrByProdIdTestId( productId , testId);
        dto.setTotalGraphs(Integer.parseInt(count.toString()));
        List<SpecDto> specDtos = new ArrayList<>();
		for (Object[] specbo: specs) {			
			SpecDto dtos = new SpecDto();
			dtos.setSpecId(specbo[0] != null ? Integer.parseInt(specbo[0].toString()) : null);
			dtos.setGraphTitle(specbo[1] != null ? specbo[1].toString() : null);
			dtos.setUnit(specbo[2] != null ? specbo[2].toString() : null);
			dtos.setMin(specbo[3] != null ? Double.parseDouble(specbo[3].toString()) : null);
			dtos.setMax(specbo[4] != null ? Double.parseDouble(specbo[4].toString()) : null);
			dtos.setObsTypeId(specbo[5] != null ? Integer.parseInt(specbo[5].toString()) : null);
			specDtos.add(dtos);			
		}	
		dto.setSpecDtos(specDtos);
		return dto;
	}
}
