package in.co.bel.olms.dgqa.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.dgqa.dto.OlDashBoardDto;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlDashBoardService {

	private final OfferLetterRepository offerLetterRepository;

	public OlDashBoardDto getOlDashBoard() {

		List<Object[]> resultList = offerLetterRepository.getDashBoardOlDetails();
		Object[] result = resultList.get(0);
		OlDashBoardDto olDashBoardDto = new OlDashBoardDto();
		olDashBoardDto.setNoOfOl(((Number) result[0]).intValue());
		olDashBoardDto.setValueOfOl(new BigDecimal(result[1].toString()));
		return olDashBoardDto;
	}

}
