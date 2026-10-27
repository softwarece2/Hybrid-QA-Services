package in.co.bel.olms.iol.ate.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.iol.ate.dto.AteGraphDataDto;
import in.co.bel.olms.iol.ate.service.Tvne1paService;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.service.CpkOrCpV2Service;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ateDashBoard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AteDashBoardController {

	private final Tvne1paService tvne1paService;

	private final SpecRepository specRepository;

	private final CpkOrCpV2Service cpkOrCpV2Service;

	@GetMapping("/getGraphData")
	public List<AteGraphDataDto> getAllColdTestsDevDetails(@RequestParam Integer productId,@RequestParam Integer specId, @RequestParam Integer testId,@RequestParam String fromDate, @RequestParam String toDate)
			throws DataNotFoundException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		List<AteGraphDataDto> dtos = null;
		Optional<Spec> spec   = specRepository.findById(specId);
//		Double min = spec.get().getMin() != null ? spec.get().getMin().doubleValue() : null;
//		Double max = spec.get().getMax() !=null ? spec.get().getMax().doubleValue() : null;
    	BigDecimal min = spec.get().getMin();
    	BigDecimal max = spec.get().getMax();
		switch (productId) {
		case 101:
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 102:
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 103: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 104: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 105: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 106: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 107: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 108: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 109: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 110: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 111: 
			
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 112: 
			
            if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			
			break;
		case 117: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 116: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 125: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 118: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 124: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 123: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;
		case 120: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 121: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 122: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
		case 126: 
			if(spec.get().getObsType().getId() == 1 || spec.get().getObsType().getId() == 2 ){

				dtos = tvne1paService.getGraphData(specId, from, to);
//				Double cpk= cpkOrCpV2Service.calculateCpkValue(dtos, min, max);
				BigDecimal cpk = cpkOrCpV2Service.calculateBigDecCpkValue(dtos, min , max);
				BigDecimal cp = cpkOrCpV2Service.calculateBigDecCpValue(dtos, min , max);
				BigDecimal mean = cpkOrCpV2Service.calculateBigDecMean(dtos);
				BigDecimal stdDev = cpkOrCpV2Service.calculateBigDecStandardDeviation(dtos,mean);
				BigDecimal median = cpkOrCpV2Service.calculateBigDecMedian(dtos);

				dtos = tvne1paService.getGraphDataV2(specId, from, to,cpk,cp,mean,stdDev,median);
			}
            else{
            	dtos = tvne1paService.getGraphData(specId, from, to);
            
            }
			break;	
	
			
		}		
		return dtos;

	}
}
