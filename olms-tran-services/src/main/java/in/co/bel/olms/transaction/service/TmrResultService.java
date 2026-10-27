package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.TmrResultDto;
import in.co.bel.olms.transaction.entity.TmrResult;
import in.co.bel.olms.transaction.repository.TmrResultRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TmrResultService {

	private final TmrResultRepository resultRepository;

	private final Mapper mapper;

	public List<TmrResult> saveTmrResult(List<TmrResultDto> dtos) {
		List<TmrResult> results = new ArrayList<>();
		for (TmrResultDto dto : dtos) {
			TmrResult tmrResult = mapper.map(dto, TmrResult.class);
			tmrResult.setCreatedDatetime(new Date());
			tmrResult.setRemarks(dto.getRemarks());
			tmrResult.setCreatedBy(dto.getCreatedBy());
			results.add(tmrResult);
		}
		return resultRepository.saveAll(results);

	}

	public List<TmrResultDto> getByOlLineItemId(int olLineItemId) {
		List<TmrResult> results = resultRepository.findByOllineItemId(olLineItemId);
		List<TmrResultDto> dtos = new ArrayList<>();
		if (results.size() != 0) {
			for (TmrResult result : results) {
				TmrResultDto dto = mapper.map(result, TmrResultDto.class);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public List<TmrResultDto> getByCreatedBy(String createdBy, int olLineItemId) {
		List<TmrResult> results = resultRepository.findByCreatedBy(createdBy, olLineItemId);
		List<TmrResultDto> dtos = new ArrayList<>();
		if (results.size() != 0) {
			for (TmrResult result : results) {
				TmrResultDto dto = mapper.map(result, TmrResultDto.class);
				dtos.add(dto);
			}
		}
		return dtos;
	}

}
