package in.co.bel.olms.transaction.service.inote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.InternalINoteRepository;
import in.co.bel.olms.util.TdmsUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InoteUtilService {

	private final InternalINoteRepository internalINoteRepository;

	private final INoteRepository iNoteRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(InoteUtilService.class);

	public String getNextInotenumber(int deptId, String secCode)
	{
		String fY = TdmsUtil.getFinancialYearStr();
		int inoteCountInCurrentFY = internalINoteRepository.getInoteCountInCurrentFY(deptId);
		//cal fr external count
		int extInoteCountInCurrentFy = iNoteRepository.getInoteCountInCurrentFY(deptId);
		
		int totalInoteCount = inoteCountInCurrentFY + extInoteCountInCurrentFy;
		//final = add
		
		logger.info("inoteCountInCurrentFY {} extInoteCountInCurrentFy {} totalInoteCount {}", 
			    inoteCountInCurrentFY, 
			    extInoteCountInCurrentFy, 
			    totalInoteCount
			);
		String inoteNo = "I" + secCode + fY + (String.format("%05d", totalInoteCount + 1));
				
		return inoteNo;
	}
	
}
