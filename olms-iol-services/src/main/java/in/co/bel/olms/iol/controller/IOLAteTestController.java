package in.co.bel.olms.iol.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.iol.ate.service.InspectionRoundService;
import in.co.bel.olms.iol.dto.AteTestProtocolDto;
import in.co.bel.olms.iol.dto.CkpOrCpDto;
import in.co.bel.olms.iol.dto.ColdTestDto;
import in.co.bel.olms.iol.dto.IolDataDto;
import in.co.bel.olms.iol.dto.MechSp14vDto;
import in.co.bel.olms.iol.dto.MechSp9vDto;
import in.co.bel.olms.iol.dto.VoltDto;
import in.co.bel.olms.iol.jwt.JwtUtils;
import in.co.bel.olms.iol.service.CpkOrCpService;
import in.co.bel.olms.iol.service.IOLAteTestService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import in.co.bel.olms.iol.constants.StringLiteralConstants;

@RestController
@RequestMapping("/iol")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class IOLAteTestController {

	private final IOLAteTestService iolAteTestService;

	private final CpkOrCpService cpkOrCpService;

	private final JwtUtils jwtUtils;

	private final InspectionRoundService inspectionRoundService;

	//Changing on 08-09-2025
	@PostMapping("/uploadIolColdTest")
	public ResponseEntity<String> saveColdTest(
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "testId", required = true) int testId,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "devId", required = true) int devId,		
			
			@RequestParam(value = "productTypeId", required = true) int productTypeId,
      @RequestParam(value = "olLineItemId", required = false) Integer olLineItemId,
      @RequestParam(value = "subSlipLineItemId", required = false) Integer subSlipLineItemId,
			@RequestParam(value = "hotCold", required = true) int hotCold,
			@RequestParam(value = "testType", required = true) int testType,
			@RequestParam(value = "IolAteTestfilename", required = true) String iolAteTestfilename,
			@RequestParam(value = "IolAteTestfile", required = false) MultipartFile iolAteTestfile,
			@RequestHeader(name = "Authorization") String token) throws Exception {
		   
		if(token != null && token.startsWith(StringLiteralConstants.BEARER)) {
			token = token.substring(7);
		}
		Map<String, Object> map = jwtUtils.parseToken(token);
		int submittedById = jwtUtils.getEmpIdFromJwtToken(map);
		Boolean isQa = inspectionRoundService.isQaEmployee(submittedById);
		if (isQa) {
			isQa = true;
		} else {
			isQa = false;
		}

		return iolAteTestService.saveEnvironmentalTest(id, testId, productId, devId, productTypeId, testType, hotCold, olLineItemId, subSlipLineItemId,
				iolAteTestfilename, iolAteTestfile ,submittedById, isQa );
	}

	/**
	 * 
	 * @param testId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("getIolAteTestByTestId/{testId}")
	public ResponseEntity<List<AteTestProtocolDto>> getIolAteTestByTestId(@PathVariable int testId)
			throws DataNotFoundException {
		List<AteTestProtocolDto> dtos = iolAteTestService.getIolAteTestByTestId(testId);
		return ResponseEntity.ok(dtos);

	}

	/**
	 * 
	 * @param testId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("getColdTestByTestId/{testId}/{olLineItemId}")
	public ResponseEntity<List<ColdTestDto>> getColdTestByTestAndOlLineItemId(@PathVariable int testId,
			@PathVariable int olLineItemId) throws DataNotFoundException {
		List<ColdTestDto> dtos = iolAteTestService.getColdTestByTestAndOlLineItemId(testId, olLineItemId);
		return ResponseEntity.ok(dtos);

	}

	/**
	 * Modified to return single object and differentiate with hot and cold test
	 * @author Manasa B
	 * @since 25-10-25
	 * @param inspRoundId
	 * @param hotCold
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("getByInspRoundId/{inspRoundId}/{hotCold}")
	public ResponseEntity<ColdTestDto> getByInspRoundId(@PathVariable int inspRoundId, @PathVariable  int hotCold) throws DataNotFoundException {
		ColdTestDto dto = iolAteTestService.getByInspRoundId(inspRoundId, hotCold);
		return ResponseEntity.ok(dto);
	}

	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/getVoltDet")
	public CkpOrCpDto getVoltDevDets(@RequestParam Integer productTypeId,
		@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		List<VoltDto> voltDtos = iolAteTestService.getVoltDevDets(productTypeId, testTypeId, from, to);
		CkpOrCpDto ckpOrCpDto = new CkpOrCpDto();
		Double ckp = cpkOrCpService.calculateCpkVolt(voltDtos);
		Double cp = cpkOrCpService.calculateCpVolt(voltDtos);
		Double mean = cpkOrCpService.calculateMean(voltDtos);
		Double stdDev = cpkOrCpService.calculateStandardDeviation(voltDtos,mean);
		Double median = cpkOrCpService.calculateMedian(voltDtos);
		ckpOrCpDto.setVoltDtos(voltDtos);
		ckpOrCpDto.setCkp(ckp);
		ckpOrCpDto.setCp(cp);
		ckpOrCpDto.setMean(mean);
		ckpOrCpDto.setStdDev(stdDev);
		ckpOrCpDto.setMedian(median);
		return ckpOrCpDto;
	}

	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/getPhCurrS")
	public CkpOrCpDto getPhCurrS(@RequestParam Integer productTypeId,
			@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		List<VoltDto> voltDtos = iolAteTestService.getPhCurrS(productTypeId, testTypeId, from, to);
		CkpOrCpDto ckpOrCpDto = new CkpOrCpDto();
		Double ckp = cpkOrCpService.calculateCpkPhCurrS(voltDtos);
		Double cp = cpkOrCpService.calculateCpPhCurrS(voltDtos);
		Double mean = cpkOrCpService.calculateMean(voltDtos);
		Double stdDev = cpkOrCpService.calculateStandardDeviation(voltDtos,mean);
		Double median = cpkOrCpService.calculateMedian(voltDtos);
		ckpOrCpDto.setVoltDtos(voltDtos);
		ckpOrCpDto.setCkp(ckp);
		ckpOrCpDto.setCp(cp);
		ckpOrCpDto.setMean(mean);
		ckpOrCpDto.setStdDev(stdDev);
		ckpOrCpDto.setMedian(median);
		return ckpOrCpDto;
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/getPhCurrR")
	public CkpOrCpDto getPhCurrR(@RequestParam Integer productTypeId,
			@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		List<VoltDto> voltDtos = iolAteTestService.getPhCurrR(productTypeId, testTypeId, from, to);
		CkpOrCpDto ckpOrCpDto = new CkpOrCpDto();
		Double ckp = cpkOrCpService.calculateCpkPhCurrR(voltDtos);
		Double cp = cpkOrCpService.calculateCpPhCurrR(voltDtos);
		Double mean = cpkOrCpService.calculateMean(voltDtos);
		Double stdDev = cpkOrCpService.calculateStandardDeviation(voltDtos,mean);
		Double median = cpkOrCpService.calculateMedian(voltDtos);
		ckpOrCpDto.setVoltDtos(voltDtos);
		ckpOrCpDto.setCkp(ckp);
		ckpOrCpDto.setCp(cp);
		ckpOrCpDto.setMean(mean);
		ckpOrCpDto.setStdDev(stdDev);
		ckpOrCpDto.setMedian(median);
		return ckpOrCpDto;
	}

	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/getPhCurrT")
	public CkpOrCpDto getPhCurrT(@RequestParam Integer productTypeId,
			@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		List<VoltDto> voltDtos = iolAteTestService.getPhCurrT(productTypeId, testTypeId, from, to);
		CkpOrCpDto ckpOrCpDto = new CkpOrCpDto();
		Double ckp = cpkOrCpService.calculateCpkPhCurrT(voltDtos);
		Double cp = cpkOrCpService.calculateCpPhCurrT(voltDtos);
		Double mean = cpkOrCpService.calculateMean(voltDtos);
		Double stdDev = cpkOrCpService.calculateStandardDeviation(voltDtos,mean);
		Double median = cpkOrCpService.calculateMedian(voltDtos);
		ckpOrCpDto.setVoltDtos(voltDtos);
		ckpOrCpDto.setCkp(ckp);
		ckpOrCpDto.setCp(cp);
		ckpOrCpDto.setMean(mean);
		ckpOrCpDto.setStdDev(stdDev);
		ckpOrCpDto.setMedian(median);
		return ckpOrCpDto;
	}

	@GetMapping("/getMechSp9v")
	public List<MechSp9vDto> getMechSp9v(@RequestParam Integer productTypeId,
			@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		return iolAteTestService.getMechSp9v(productTypeId, testTypeId, from, to);
	}

	@GetMapping("/getMechSp14v")
	public List<MechSp14vDto> getMechSp14v(@RequestParam Integer productTypeId,
			@RequestParam Integer testTypeId, @RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		return iolAteTestService.getMechSp14v(productTypeId, testTypeId, from, to);
	}

	@GetMapping("/getDevDets/{testId}/{olLineItemId}")
	public List<IolDataDto> getDevDets(@PathVariable int testId, @PathVariable int olLineItemId) {
		return iolAteTestService.getDevDets(testId, olLineItemId);
	}

}
