package in.co.bel.olms.iol.ate.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.iol.ate.dto.AteProductStatsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AteStatsService {

	private final ProductsRepository prodRepo;

	private final ProductMappingRepository prodTestMappingRepo;

	Gson gson = new Gson();

	public String getStatsByProdId(Integer productId, Date from, Date to) {

		if (!prodRepo.existsById(productId))
			throw new IllegalArgumentException("Product does not exist with id: " + productId);

		List<Object[]> testLists = prodTestMappingRepo.getStatsByProdId(productId, from, to);

		List<AteProductStatsDto> returnLists = testLists.stream().map(test -> {
			AteProductStatsDto dto = new AteProductStatsDto();
			dto.setTestId(test[0] != null ? ((Number) test[0]).intValue() : 0);
			dto.setTestName(String.valueOf(test[1]));
			dto.setPassed(test[2] != null ? ((Number) test[2]).intValue() : 0);
			dto.setFailed(test[3] != null ? ((Number) test[3]).intValue() : 0);
			dto.setTotal(test[4] != null ? ((Number) test[4]).intValue() : 0);
			return dto;
		}).collect(Collectors.toList());

		return gson.toJson(returnLists);
	}

}
