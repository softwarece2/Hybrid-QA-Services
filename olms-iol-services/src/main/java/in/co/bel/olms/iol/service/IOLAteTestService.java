package in.co.bel.olms.iol.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.ate.repository.ProductMappingRepository;
import in.co.bel.olms.iol.ate.dto.InspTestMappingDto;
import in.co.bel.olms.iol.ate.dto.InspectionRoundDto;
import in.co.bel.olms.iol.ate.service.InspectionRoundService;
import in.co.bel.olms.iol.constants.ApplicationConstants;
import in.co.bel.olms.iol.dto.AteTestProtocolDto;
import in.co.bel.olms.iol.dto.ColdTestDto;
import in.co.bel.olms.iol.dto.IolDataDto;
import in.co.bel.olms.iol.dto.MechSp14vDto;
import in.co.bel.olms.iol.dto.MechSp9vDto;
import in.co.bel.olms.iol.dto.VoltDto;
import in.co.bel.olms.iol.entity.AteTest;
import in.co.bel.olms.iol.entity.AteTestProtocol;
import in.co.bel.olms.iol.entity.ColdTest;
import in.co.bel.olms.iol.entity.DeviceStatusDto;
import in.co.bel.olms.iol.entity.InspTestMapping;
import in.co.bel.olms.iol.repository.AteTestProtocolRepository;
import in.co.bel.olms.iol.repository.AteTestRepository;
import in.co.bel.olms.iol.repository.ColdTestRepository;
import in.co.bel.olms.iol.repository.InspTestMappingRepository;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IOLAteTestService {

	private static final Logger logger = LoggerFactory.getLogger(IOLAteTestService.class);

	private final AteTestRepository ateTestRepository;

	private final AteTestProtocolRepository ateTestProtocolRepository;

	private final ColdTestRepository coldTestRepository;

	private final Mapper mapper;

	Gson gson = new Gson();

	private final IOLAteTestValidation iolAteTestValidate;

	private final OlLineItemRepository olLineItemRepository;

	private final IOLColdTestService iolColdTestService;

	private final InspectionRoundService inspectionRoundService;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final OlStatusRepository olStatusRepository;

	private final InspTestMappingRepository inspTestMappingRepository;

	private final SubSlipLineItemRepository subSlipLineItemRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final OLLineItemSerialNoRepository olLineItemSerialNoRepository;

	private final ProductMappingRepository productMappingRepository;

	/**
	 * 
	 * @param testId
	 * @return
	 */
	public List<AteTestProtocolDto> getIolAteTestByTestId(int testId) {

		List<AteTest> applicableTests = ateTestRepository.findByApplicableTestId(testId);

		List<AteTestProtocol> tests = new ArrayList<>();

		for (AteTest ateTest : applicableTests) {
			tests.addAll(ateTestProtocolRepository.findbyAteTestId(ateTest.getId()));
		}
		List<AteTestProtocolDto> dtos = new ArrayList<>();
		for (AteTestProtocol item : tests) {
			AteTestProtocolDto dto = mapper.map(item, AteTestProtocolDto.class);
			Optional<AteTest> ateTestOptional = ateTestRepository.findById(dto.getAteTestId());
			AteTest ateTest = ateTestOptional.get();
			dto.setTestId(ateTest.getLineItemApplTest().getId());
			dtos.add(dto);
		}

		return dtos;
	}

	@Transactional
	public ResponseEntity<String> saveEnvironmentalTest(int id,  int testId,
                                                      int productId, int devId,
                                                      int productTypeId, int testType, int hotCold, Integer olLineItemId, Integer subSlipLineItemId, String iolAteTestfilename, MultipartFile iolAteTestfile, int submittedById, Boolean isQa) throws Exception {
		String res = null;
		try {
			InputStream inputStream = iolAteTestfile.getInputStream();
			Row row;
			Cell cell;
			try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
				XSSFSheet sheet = workbook.getSheetAt(0);

				ColdTest test = new ColdTest();
				test.setCreatedDatetime(new Date());
//				Optional<LineItemApplicableTest> applicableTestOptional = itemApplicableTestRepository.findById(testId);				
//				if (applicableTestOptional.get() != null) {
//					LineItemApplicableTest lineItemApplicableTest = applicableTestOptional.get();
//				    test.setLineItemApplicableTest(lineItemApplicableTest);
//				} else {
//					test.setLineItemApplicableTest(null);
//				}
//				

        InspectionRoundDto inspDto = new InspectionRoundDto();
        inspDto.setDevId(devId);
        inspDto.setOlLineItemId(olLineItemId);
        inspDto.setSubSlipLineItemId(subSlipLineItemId);
        inspDto.setProductId(productId);
//			       inspDto.setTestId(testId);
			   //    inspDto.setApprovedById(approvedById);
			       
			       
				InspectionRound savedInspectionRound = null;
				Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(id);

				
//				if(id == 0) {
//					InspectionRound inspectionRound = inspectionRoundService.populateInspectionRound(inspDto, true, savedInspectionRound);
//					inspectionRound.setCreatedDatetime(new Date());
//					savedInspectionRound = inspectionRoundRepository.save(inspectionRound);
//				} else {
//					Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(id);
//					if(inspectionRoundOptional.isPresent()) {
//						savedInspectionRound = inspectionRoundOptional.get();
//					} else { 
//						throw new DataNotFoundException("Save : Inspection Id not found: " + id);
//					}
//				}
//				
				

        if (olLineItemId != null) {
          OlLineItem olLineItem = olLineItemRepository.findById(olLineItemId)
              .orElseThrow(() -> new IllegalArgumentException("Save : OlLineItem Id not found: " + olLineItemId));
          test.setOlLineItem(olLineItem);
          inspDto.setOlId(olLineItem.getOfferLetter().getId());
        }

        if (subSlipLineItemId != null) {
          SubSlipLineItem subSlipLineItem = subSlipLineItemRepository.findById(subSlipLineItemId)
              .orElseThrow(() -> new IllegalArgumentException("Save : SubSlipLineItem Id not found: " + subSlipLineItemId));
          test.setSubSlipLineItem(subSlipLineItem);
				}
				
				try {
				row = sheet.getRow(2);
				cell = row.getCell(1);
				String devNo = getDevNo(cell);
				if(devNo != null && !devNo.isEmpty()) {
				//test.setDeviceNo(.getInspectionRound().getLineItemLotsRegister().getRegisterNo());
				} else {
					return ResponseEntity.badRequest().body("Missing device number");
				}
				} catch (Exception e) {
					logger.error("Error: ", e);
					return ResponseEntity.badRequest().body("Missing device number");
				}
				
				cell = row.getCell(7);
				String testDt = getDate(cell);
				if (testDt != null) {
				test.setTestDate(testDt);
				} else {
					test.setTestDate(null);
				}
				
				row = sheet.getRow(4);
				cell = row.getCell(3);
				Double volt = getVolt(cell);
				if (volt != null) {
				test.setVolt(volt);
				} else {
					test.setVolt(null);
				}
				
				row = sheet.getRow(5);
				cell = row.getCell(3);
				Double phCurrR = getPhCurr(cell);
				if (phCurrR != null) {
				test.setPhCurrR(phCurrR);
				} else {
					test.setPhCurrR(null);
				}

				cell = row.getCell(6);
				Double phCurrS = getPhCurr(cell);
				if (phCurrS != null) {
				test.setPhCurrS(phCurrS);
				} else {
					test.setPhCurrS(null);
				}

				cell = row.getCell(9);
				Double phCurrT = getPhCurr(cell);
				if (phCurrT != null) {
				test.setPhCurrT(phCurrT);
				} else {
					test.setPhCurrT(null);
				}

				row = sheet.getRow(6);
				cell = row.getCell(3);
				String ocularHeat = getStringVal(cell);
				if (ocularHeat != null) {
				test.setOcularHeating(ocularHeat);
				} else {
					test.setOcularHeating(null);
				}

				row = sheet.getRow(7);
				cell = row.getCell(3);
				String commander = getStringVal(cell);
				if (commander != null) {
				test.setCommander(commander);
				} else {
					test.setCommander(null);
				}

				row = sheet.getRow(8);
				cell = row.getCell(3);
				String autoMan = getStringVal(cell);
				if (autoMan != null) {
				test.setAutoMan(autoMan);
				} else {
					test.setAutoMan(null);
				}

				row = sheet.getRow(9);
				cell = row.getCell(3);
				Double rcTime = getTimeRcworkOff(cell);
				if (rcTime != null) {
				test.setRcTime(rcTime);
				} else {
					test.setRcTime(null);
				}
				
				cell = row.getCell(6);
				String rcBid = getHyphensBid(cell);
				if (rcBid != null) {
				test.setRcTimeBid(rcBid);
				} else {
					test.setRcTimeBid(null);
				}
				
				cell = row.getCell(8);
				Integer rcTimeDs = getRcWorkOffTimeDs(cell);
				if (rcTimeDs != null) {
				test.setRcTimeDs(rcTimeDs);
				} else {
					test.setRcTimeDs(null);
				}

				row = sheet.getRow(10);
				cell = row.getCell(3);
				Double workOffDTime = getTimeRcworkOff(cell);
				if (workOffDTime != null) {
				test.setWorkOffDTime(workOffDTime);
				} else {
					test.setWorkOffDTime(null);
				}

				cell = row.getCell(6);
				String workBid = getHyphensBid(cell);
				if (workBid != null) {
				test.setWorkOffDBid(workBid);
				} else {
					test.setWorkOffDBid(null);
				}

				cell = row.getCell(8);
				Integer workOffDs = getRcWorkOffTimeDs(cell);
				if (workOffDs != null) {
				test.setWorkOffDDs(workOffDs);
				} else {
					test.setWorkOffDDs(null);
				}

				row = sheet.getRow(11);
				cell = row.getCell(3);
				String failSign = getHyphensBid(cell);
				if (failSign != null) {
				test.setFailureSingle(failSign);
				} else {
					test.setFailureSingle(null);
				}

				cell = row.getCell(6);
				String failSignBid = getHyphensBid(cell);
				if (failSignBid != null) {
				test.setFailureSingleBid(failSignBid);
				} else {
					test.setFailureSingleBid(null);
				}

				cell = row.getCell(8);
				String failSignDs = getHyphensBid(cell);
				if (failSignDs != null) {
				test.setFailureSingleDs(failSignDs);
				} else {
					test.setFailureSingleDs(null);
				}

				row = sheet.getRow(12);
				cell = row.getCell(3);
				Double mutliArresting = getNumericVal(cell);
				if (mutliArresting != null) {
				test.setMutliArresting(mutliArresting);
				} else {
					test.setMutliArresting(null);
				}

				row = sheet.getRow(13);
				cell = row.getCell(4);
				Double signalRotaryTrans = getNumericVal(cell);
				if (signalRotaryTrans != null) {
				test.setSignalRotaryTrans(signalRotaryTrans);
				} else {
					test.setSignalRotaryTrans(null);
				}

				row = sheet.getRow(14);
				cell = row.getCell(3);
				String fireContact = getStringVal(cell);
				if (fireContact != null) {
				test.setFireContact(fireContact);
				} else {
					test.setFireContact(null);
				}
				
				cell = row.getCell(4);
				String fireContactBid = getStringVal(cell);
				if (fireContactBid != null) {
				test.setFireContactBid(fireContactBid);
				} else {
					test.setFireContactBid(null);
				}

				cell = row.getCell(7);
				String fireContactDs = getStringVal(cell);
				if (fireContactDs != null) {
				test.setFireContactDs(fireContactDs);
				} else {
					test.setFireContactDs(null);
				}

				row = sheet.getRow(15);
				cell = row.getCell(3);
				Double compensation = getNumericVal(cell);
				if (compensation != null) {
				test.setCompensation(compensation);
				} else {
					test.setCompensation(null);
				}

				row = sheet.getRow(16);
				cell = row.getCell(3);
				String compMgFire = getStringVal(cell);
				if (compMgFire != null) {
				test.setCompMgFire(compMgFire);
				} else {
					test.setCompMgFire(null);
				}

				row = sheet.getRow(18);
				cell = row.getCell(3);
				Double voltSoftClockP1 = getLastDouble(cell);
				if (voltSoftClockP1 != null) {
				test.setVoltSoftClockP1(voltSoftClockP1);
				} else {
					test.setVoltSoftClockP1(null);
				}

				cell = row.getCell(6);
				Double voltSoftAntiClockP1 = getLastDouble(cell);
				if (voltSoftAntiClockP1 != null) {
				test.setVoltSoftAntiClockP1(voltSoftAntiClockP1);
				} else {
					test.setVoltSoftAntiClockP1(null);
				}

				row = sheet.getRow(19);
				cell = row.getCell(3);
				Double voltSoftClockP2 = getLastDouble(cell);
				if (voltSoftClockP2 != null) {
				test.setVoltSoftClockP2(voltSoftClockP2);
				} else {
					test.setVoltSoftClockP2(null);
				}
				
				cell = row.getCell(6);
				Double voltSoftAntiClockP2 = getLastDouble(cell);
				if (voltSoftAntiClockP2 != null) {
				test.setVoltSoftAntiClockP2(voltSoftAntiClockP2);
				} else {
					test.setVoltSoftAntiClockP2(null);
				}

				row = sheet.getRow(20);
				cell = row.getCell(3);
				Double voltHardClockP1 = getLastDouble(cell);
				if (voltHardClockP1 != null) {
				test.setVoltHardClockP1(voltHardClockP1);
				} else {
					test.setVoltHardClockP1(null);
				}

				cell = row.getCell(6);
				Double voltHardAntiClockP1 = getLastDouble(cell);
				if (voltHardAntiClockP1 != null) {
				test.setVoltHardAntiClockP1(voltHardAntiClockP1);
				} else {
					test.setVoltHardAntiClockP1(null);
				}

				row = sheet.getRow(21);
				cell = row.getCell(3);
				Double voltHardClockP2 = getLastDouble(cell);
				if (voltHardClockP2 != null) {
				test.setVoltHardClockP2(voltHardClockP2);
				} else {
					test.setVoltHardClockP2(null);
				}

				cell = row.getCell(6);
				Double voltHardAntiClockP2 = getLastDouble(cell);
				if (voltHardAntiClockP2 != null) {
				test.setVoltHardAntiClockP2(voltHardAntiClockP2);
				} else {
					test.setVoltHardAntiClockP2(null);
				}

				row = sheet.getRow(22);
				cell = row.getCell(3);
				Double voltZeroClockP1 = getLastDouble(cell);
				if (voltZeroClockP1 != null) {
				test.setVoltZeroClockP1(voltZeroClockP1);
				} else {
					test.setVoltZeroClockP1(null);
				}

				cell = row.getCell(6);
				Double voltZeroAntiClockP1 = getLastDouble(cell);
				if (voltZeroAntiClockP1 != null) {
				test.setVoltZeroAntiClockP1(voltZeroAntiClockP1);
				} else {
					test.setVoltZeroAntiClockP1(null);
				}

				row = sheet.getRow(23);
				cell = row.getCell(3);
				String safeContPt5v = getAlphabeticCharacters(cell);
				if (safeContPt5v != null) {
				test.setSafeContPt5v(safeContPt5v);
				} else {
					test.setSafeContPt5v(null);
				}

				cell = row.getCell(6);
				String safeContPt9v = getAlphabeticCharacters(cell);
				if (safeContPt9v != null) {
				test.setSafeContPt9v(safeContPt9v);
				} else {
					test.setSafeContPt9v(null);
				}

				row = sheet.getRow(24);
				cell = row.getCell(3);
				String blocking = getStringVal(cell);
				if (blocking != null) {
				test.setBlocking(blocking);
				} else {
					test.setBlocking(null);
				}

				row = sheet.getRow(26);
				cell = row.getCell(3);
				Double maxCurrSpeed = getNumericVal(cell);
				if (maxCurrSpeed != null) {
				test.setMaxUpwCurrSpeed(maxCurrSpeed);
				} else {
					test.setMaxUpwCurrSpeed(null);
				}

				cell = row.getCell(10);
				Double maxUpwCurrP1 = getNumericVal(cell);
				if (maxUpwCurrP1 != null) {
				test.setMaxUpwCurrP1(maxUpwCurrP1);
				} else {
					test.setMaxUpwCurrP1(null);
				}

				row = sheet.getRow(27);
				cell = row.getCell(3);
				Double maxDownCurrSpeed = getNumericVal(cell);
				if (maxDownCurrSpeed != null) {
				test.setMaxDownCurrSpeed(maxDownCurrSpeed);
				} else {
					test.setMaxDownCurrSpeed(null);
				}

				cell = row.getCell(10);
				Double maxDownCurrP2 = getNumericVal(cell);
				if (maxDownCurrP2 != null) {
				test.setMaxDownCurrP2(maxDownCurrP2);
				} else {
					test.setMaxDownCurrP2(null);
				}

				row = sheet.getRow(28);
				cell = row.getCell(3);
				Double minAimSpeedUp = getNumericVal(cell);
				if (minAimSpeedUp != null) {
				test.setMinAimSpeedUp(minAimSpeedUp);
				} else {
					test.setMinAimSpeedUp(null);
				}

				cell = row.getCell(6);
				Double minAimSpeedDown = getNumericVal(cell);
				if (minAimSpeedDown != null) {
				test.setMinAimSpeedDown(minAimSpeedDown);
				} else {
					test.setMinAimSpeedDown(null);
				}

				row = sheet.getRow(29);
				cell = row.getCell(3);
				Integer evenessSightUp = getNumericValInt(cell);
				if (evenessSightUp != null) {
				test.setEvenessSightUp(evenessSightUp);
				} else {
					test.setEvenessSightUp(null);
				}

				cell = row.getCell(6);
				Integer evenessSightDown = getNumericValInt(cell);
				if (evenessSightDown != null) {
				test.setEvenessSightDown(evenessSightDown);
				} else {
					test.setEvenessSightDown(null);
				}

				row = sheet.getRow(30);
				cell = row.getCell(3);
				String zeroSetting = getHyphensBid(cell);
				if (zeroSetting != null) {
				test.setZeroSetting(zeroSetting);
				} else {
					test.setZeroSetting(null);
				}

				row = sheet.getRow(32);
				cell = row.getCell(3);
				Double mechSp9vForw = getTimeRcworkOff(cell);
				if (mechSp9vForw != null) {
				test.setMechSp9vForw(mechSp9vForw);
				} else {
					test.setMechSp9vForw(null);
				}

				cell = row.getCell(6);
				Double mechSp9vBack = getTimeRcworkOff(cell);
				if (mechSp9vBack != null) {
				test.setMechSp9vBackw(mechSp9vBack);
				} else {
					test.setMechSp9vBackw(null);
				}

				row = sheet.getRow(33);
				cell = row.getCell(3);
				Double mechSp14vForw = getTimeRcworkOff(cell);
				if (mechSp14vForw != null) {
				test.setMechSp14vForw(mechSp14vForw);
				} else {
					test.setMechSp14vForw(null);
				}

				cell = row.getCell(6);
				Double mechSp14vBack = getTimeRcworkOff(cell);
				if (mechSp14vBack != null) {
				test.setMechSp14vBackw(mechSp14vBack);
				} else {
					test.setMechSp14vBackw(null);
				}

				row = sheet.getRow(34);
				cell = row.getCell(3);
				Double mechSpPhCurrR = getPhCurrMechSp(cell);
				if (mechSpPhCurrR != null) {
				test.setMechSpPhCurrR(mechSpPhCurrR);
				} else {
					test.setMechSpPhCurrR(null);
				}

				cell = row.getCell(6);
				Double mechSpPhCurrS = getPhCurrMechSp(cell);
				if (mechSpPhCurrS != null) {
				test.setMechSpPhCurrS(mechSpPhCurrS);
				} else {
					test.setMechSpPhCurrS(null);
				}

				cell = row.getCell(9);
				Double mechSpPhCurrT = getPhCurrMechSp(cell);
				if (mechSpPhCurrT != null) {
				test.setMechSpPhCurrT(mechSpPhCurrT);
				} else {
					test.setMechSpPhCurrT(null);
				}

				row = sheet.getRow(35);
				cell = row.getCell(3);
				Double driftSpeedPl0Deg = getDegDouble(cell);
				if (driftSpeedPl0Deg != null) {
				test.setDriftSpeedPl0Deg(driftSpeedPl0Deg);
				} else {
					test.setDriftSpeedPl0Deg(null);
				}

				cell = row.getCell(6);
				Double driftSpeedMi5Deg = getDegDouble(cell);
				if (driftSpeedMi5Deg != null) {
				test.setDriftSpeedMi5Deg(driftSpeedMi5Deg);
				} else {
					test.setDriftSpeedMi5Deg(null);
				}

				cell = row.getCell(9);
				Double driftSpeedPl15Deg = getDegDouble(cell);
				if (driftSpeedPl15Deg != null) {
				test.setDriftSpeedPl15Deg(driftSpeedPl15Deg);
				} else {
					test.setDriftSpeedPl15Deg(null);
				}

				row = sheet.getRow(36);
				cell = row.getCell(3);
				String illumination = getStringVal(cell);
				if (illumination != null) {
				test.setIllumination(illumination);
				} else {
					test.setIllumination(null);
				}

				row = sheet.getRow(38);
				cell = row.getCell(3);
				Double projSpApdsHef = getNumericVal(cell);
				if (projSpApdsHef != null) {
				test.setProjSpApdsHef(projSpApdsHef);
				} else {
					test.setProjSpApdsHef(null);
				}

				cell = row.getCell(9);
				Double projSpApdsHeat = getTimeRcworkOff(cell);
				if (projSpApdsHeat != null) {
				test.setProjSpApdsHeat(projSpApdsHeat);
				} else {
					test.setProjSpApdsHeat(null);
				}

				row = sheet.getRow(39);
				cell = row.getCell(3);
				Double projSpHefHeat = getTimeRcworkOff(cell);
				if (projSpHefHeat != null) {
				test.setProjSpHefHeat(projSpHefHeat);
				} else {
					test.setProjSpHefHeat(null);
				}

				cell = row.getCell(9);
				Double projSpHeatHef = getTimeRcworkOff(cell);
				if (projSpHeatHef != null) {
				test.setProjSpHeatHef(projSpHeatHef);
				} else {
					test.setProjSpHeatHef(null);
				}

				row = sheet.getRow(40);
				cell = row.getCell(3);
				Double projSpHeatApds = getTimeRcworkOff(cell);
				if (projSpHeatApds != null) {
				test.setProjSpHeatApds(projSpHeatApds);
				} else {
					test.setProjSpHeatApds(null);
				}

				cell = row.getCell(9);
				Double projSpHefApds = getTimeRcworkOff(cell);
				if (projSpHefApds != null) {
				test.setProjSpHefApds(projSpHefApds);
				} else {
					test.setProjSpHefApds(null);
				}

				row = sheet.getRow(41);
				cell = row.getCell(3);
				Integer parallelism = getNumericValInt(cell);
				if (parallelism != null) {
				test.setParallelism(parallelism);
				} else {
					test.setParallelism(null);
				}

				row = sheet.getRow(42);
				cell = row.getCell(3);
				String torque = getHyphensBid(cell);
				if (torque !=  null) {
				test.setTorqHandwheel(torque);
				} else {
					test.setTorqHandwheel(null);
				}
				
				if (testType == 0 || testType == 1) {
				    test.setTestType(testType);
				} else {
					test.setTestType(null);
				}
				
				if (hotCold == 0 || hotCold == 1) {
				    test.setHotCold(hotCold);
				} else {
					test.setHotCold(null);
				}
				
				
				ColdTestDto dto = mapper.map(test, ColdTestDto.class);
				DeviceStatusDto statusDto = iolColdTestService.validateDto(dto);
				String devSts = statusDto.getStatus()== 0 ? "fail": "pass";
				if(inspectionRoundOptional.isPresent()) {
					savedInspectionRound = inspectionRoundOptional.get();
					inspDto.setRoundId(savedInspectionRound.getRoundId());
					inspDto.setIsQa(isQa);
					inspDto.setOlLineItemId(olLineItemId);
					InspectionRound inspectionRound = inspectionRoundService.populateInspectionRound(inspDto, true, savedInspectionRound);
					inspectionRound.setSubmittedDate(new Date());
					inspectionRound.setStatus(devSts);
					savedInspectionRound = inspectionRoundRepository.save(inspectionRound);
				} else { 
					throw new DataNotFoundException("Save : Inspection Id not found: " + id);
				}
				test.setInspectionRound(savedInspectionRound);
				if (statusDto != null && statusDto.getStatus() == 1) {
				    test.setStatus(statusDto.getStatus());
				    test.setRemarks(statusDto.getRemarks());
				    test.setSubmittedDate(new Date());
				    coldTestRepository.save(test);
				}
				if (statusDto != null && statusDto.getStatus() == 0) {
					test.setStatus(statusDto.getStatus());
				    test.setRemarks(statusDto.getRemarks());
				    test.setSubmittedDate(new Date());
				    coldTestRepository.save(test);
				    
				} else {
					coldTestRepository.save(test);
				    res = gson.toJson(dto);
				}
				
				
				
				InspTestMappingDto inspTestDto = new InspTestMappingDto();
				inspTestDto.setInspId(savedInspectionRound.getId());
			    inspTestDto.setTestId(testId);
				inspTestDto.setSubmittedById(submittedById);
			    Optional<OlStatus> olstatus= olStatusRepository.findStatus(devSts.toLowerCase());
				inspTestDto.setTestStatusId(olstatus.get().getId());
				InspTestMapping inspTestMapping = mapper.map(inspTestDto, InspTestMapping.class);
				inspTestMapping.setSubmittedDate(test.getSubmittedDate());
				InspTestMapping savedInspTestMapping = inspTestMappingRepository.save(inspTestMapping);
				InspTestMappingDto savedInspTestDto = mapper.map(savedInspTestMapping, InspTestMappingDto.class);
				logger.debug("Saved InspTestDto: {} ", savedInspTestDto);
			    if (isQa == null || !isQa) {
			        SubSlipSerialNo subSlipSerialNo = subSlipSerialNoRepository.findById(devId)
			            .orElseThrow(() -> new IllegalArgumentException("Status : Dev Id not found: " + dto.getDevId()));
            String devStatus = getDeviceStatus(productId, devId, olLineItemId, isQa, null);
			        subSlipSerialNo.setFinalStatus(devStatus);
			        subSlipSerialNoRepository.save(subSlipSerialNo);
			      } else {
			        OLLineItemSerialNo oLLineItemSerialNo = olLineItemSerialNoRepository.findById(devId)
			            .orElseThrow(() -> new IllegalArgumentException("Status : Dev Id not found: " + dto.getDevId()));
            String devStatus = getDeviceStatus(productId, devId, olLineItemId, isQa, oLLineItemSerialNo.getIsFaiSample());
			        oLLineItemSerialNo.setStatus(devStatus);
			        olLineItemSerialNoRepository.save(oLLineItemSerialNo);
			      }	
				return ResponseEntity.ok(gson.toJson(dto));
			}
		} catch (FileNotFoundException e1) {
			logger.error("File not found ", e1);
		} catch (IOException e) {
			logger.error("IO Error during processing: ", e);
		}
		return ResponseEntity.ok(res);
	}

  private String getDeviceStatus(Integer productId, Integer devId, Integer olLiId, Boolean isQa, Boolean isFaiSample) {
		    String statusFinal = ApplicationConstants.INPROCESS;
//		    Integer productId = dto.getProductId();
//		    Integer devId = dto.getDevId();
//		    Boolean isQa = dto.getIsQa();

		    try {
		      List<Integer> requiredTestIds = Boolean.TRUE.equals(isQa) && Boolean.TRUE.equals(isFaiSample)
		          ? inspectionRoundRepository.getRequiredTestIdsByFai(productId)
		          : productMappingRepository.getTestIdsByProdId(productId);

		      List<Integer> performedTestIds = Boolean.TRUE.equals(isQa)
              ? inspectionRoundRepository.findTestListByDevIdQa(devId, olLiId)
		          : inspectionRoundRepository.findTestListByDevId(devId);

		      boolean setsAreEqual = new HashSet<>(requiredTestIds).equals(new HashSet<>(performedTestIds));

		      List<InspTestMapping> inspTestMappings = isQa
              ? inspTestMappingRepository.findByIdsInQa(performedTestIds, devId, olLiId)
		          : inspTestMappingRepository.findByIdsIn(performedTestIds, devId);

		      Set<Integer> statusIds = inspTestMappings.stream()
		          .map(m -> m.getTestStatusId().getId())
		          .collect(Collectors.toSet());

		      Map<Integer, String> statusMap = olStatusRepository.findAllById(statusIds).stream()
		          .collect(Collectors.toMap(OlStatus::getId, OlStatus::getStatus));

		      List<String> statuses = inspTestMappings.stream()
		          .map(m -> statusMap.get(m.getTestStatusId().getId()))
		          .filter(Objects::nonNull)
		          .collect(Collectors.toList());

		      if (!setsAreEqual) {
		        boolean hasFail = statuses.stream()
		            .anyMatch(s -> s.equalsIgnoreCase(ApplicationConstants.FAIL));
		        statusFinal = hasFail ? ApplicationConstants.FAIL : ApplicationConstants.INPROCESS;
		      } else {
		        boolean allPass = statuses.stream()
		            .allMatch(s -> s.equalsIgnoreCase(ApplicationConstants.PASS));
		        statusFinal = allPass ? ApplicationConstants.PASS : ApplicationConstants.FAIL;
		      }

		    } catch (Exception e) {
		      logger.error("Error calculating device status for DevId: {}", devId, e);
		    }
		    return statusFinal;
		  
		}

	private Double getPhCurrMechSp(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractMechSpPhCurrLastNumericValue(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private Double getDegDouble(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractDouble(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private String getAlphabeticCharacters(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractAlphabeticCharacters(cell.getStringCellValue());
		} else {
			return "";
		}
	}

	private Double getLastDouble(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractLastDouble(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private Double getNumericVal(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumericValue(cell.getStringCellValue());
		} else {
			return null;
		}
	}
	
	private Integer getNumericValInt(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumericValues(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private String getHyphensBid(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractHyphens(cell.getStringCellValue());
		} else {
			return "";
		}
	}

	private Integer getRcWorkOffTimeDs(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumericValueDs(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private Double getTimeRcworkOff(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumericValueFromTime(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private String getStringVal(Cell cell) {
		if (cell != null) {
			return cell.getStringCellValue();
		} else {
			return "";
		}
	}

	private Double getPhCurr(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumericValuePhCur(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private Double getVolt(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractNumFromVolt(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private String getDate(Cell cell) {
		if (cell != null) {
			return iolAteTestValidate.extractDate(cell.getStringCellValue());
		} else {
			return "";
		}
	}

	private String getDevNo(Cell cell){
		String dev = "";
		if (cell != null && !StringUtils.isEmpty(cell.getStringCellValue())) {
			dev = cell.getStringCellValue().substring(12);
			if (dev != null) {
				return dev;
			} else {
				dev = "";
			}
		}
		return dev;
	}

	/**
	 * 
	 * @param testId
	 * @return
	 */

	public List<ColdTestDto> getColdTestByTestAndOlLineItemId(int testId, int olLineItemId) {

		List<ColdTest> coldTests = coldTestRepository.findByApplicableTestAndOlLineItemId(testId, olLineItemId);
		logger.info("Number of ColdTest records found: {}", coldTests.size());

		List<ColdTestDto> dtos = new ArrayList<>();

		for (ColdTest coldTest : coldTests) {
			ColdTestDto dto = new ColdTestDto();

			dto.setId(coldTest.getId());
			dto.setHotCold(coldTest.getHotCold());
			dto.setTestType(coldTest.getTestType());
			dto.setDeviceNo(coldTest.getInspectionRound().getLineItemLotsRegister().getRegisterNo());
			dto.setTestDate(coldTest.getTestDate());
			dto.setVolt(coldTest.getVolt());
			dto.setPhCurrR(coldTest.getPhCurrR());
			dto.setPhCurrS(coldTest.getPhCurrS());
			dto.setPhCurrT(coldTest.getPhCurrT());
			dto.setOcularHeating(coldTest.getOcularHeating());
			dto.setCommander(coldTest.getCommander());
			dto.setAutoMan(coldTest.getAutoMan());
			dto.setRcTime(coldTest.getRcTime());
			dto.setRcTimeBid(coldTest.getRcTimeBid());
			dto.setRcTimeDs(coldTest.getRcTimeDs());
			dto.setWorkOffDTime(coldTest.getWorkOffDTime());
			dto.setWorkOffDBid(coldTest.getWorkOffDBid());
			dto.setWorkOffDDs(coldTest.getWorkOffDDs());
			dto.setFailureSingle(coldTest.getFailureSingle());
			dto.setFailureSingleBid(coldTest.getFailureSingleBid());
			dto.setFailureSingleDs(coldTest.getFailureSingleDs());
			dto.setMutliArresting(coldTest.getMutliArresting());
			dto.setSingnalRotaryTrans(coldTest.getSignalRotaryTrans());
			dto.setFireContact(coldTest.getFireContact());
			dto.setFireContactBid(coldTest.getFireContactBid());
			dto.setFireContactDs(coldTest.getFireContactDs());
			dto.setCompensation(coldTest.getCompensation());
			dto.setCompMgFire(coldTest.getCompMgFire());
			dto.setVoltSoftClockP1(coldTest.getVoltSoftClockP1());
			dto.setVoltSoftAntiClockP1(coldTest.getVoltSoftAntiClockP1());
			dto.setVoltSoftClockP2(coldTest.getVoltSoftClockP2());
			dto.setVoltSoftAntiClockP2(coldTest.getVoltSoftAntiClockP2());
			dto.setVoltHardClockP1(coldTest.getVoltHardClockP1());
			dto.setVoltHardAntiClockP1(coldTest.getVoltHardAntiClockP1());
			dto.setVoltHardClockP2(coldTest.getVoltHardClockP2());
			dto.setVoltHardAntiClockP2(coldTest.getVoltHardAntiClockP2());
			dto.setVoltZeroClockP1(coldTest.getVoltZeroClockP1());
			dto.setVoltZeroAntiClockP1(coldTest.getVoltZeroAntiClockP1());
			dto.setSafeContPt5v(coldTest.getSafeContPt5v());
			dto.setSafeContPt9v(coldTest.getSafeContPt9v());
			dto.setBlocking(coldTest.getBlocking().trim());
			dto.setMaxUpwCurrSpeed(coldTest.getMaxUpwCurrSpeed());
			dto.setMaxUpwCurrP1(coldTest.getMaxUpwCurrP1());
			dto.setMaxDownCurrSpeed(coldTest.getMaxDownCurrSpeed());
			dto.setMaxDownCurrP2(coldTest.getMaxDownCurrP2());
			dto.setMinAimSpeedUp(coldTest.getMinAimSpeedUp());
			dto.setMinAimSpeedDown(coldTest.getMinAimSpeedDown());
			dto.setEvenessSightUp(coldTest.getEvenessSightUp());
			dto.setEvenessSightDown(coldTest.getEvenessSightDown());
			dto.setZeroSetting(coldTest.getZeroSetting());
			dto.setMechSp9vForw(coldTest.getMechSp9vForw());
			dto.setMechSp9vBackw(coldTest.getMechSp9vBackw());
			dto.setMechSp14vForw(coldTest.getMechSp14vForw());
			dto.setMechSp14vBackw(coldTest.getMechSp14vBackw());
			dto.setMechSpPhCurrR(coldTest.getMechSpPhCurrR());
			dto.setMechSpPhCurrS(coldTest.getMechSpPhCurrS());
			dto.setDriftSpeedPl0Deg(coldTest.getDriftSpeedPl0Deg());
			dto.setDriftSpeedMi5Deg(coldTest.getDriftSpeedMi5Deg());
			dto.setIllumination(coldTest.getIllumination());
			dto.setProjSpApdsHef(coldTest.getProjSpApdsHef());
			dto.setProjSpApdsHeat(coldTest.getProjSpApdsHeat());
			dto.setProjSpHefHeat(coldTest.getProjSpHefHeat());
			dto.setProjSpHeatHef(coldTest.getProjSpHeatHef());
			dto.setProjSpHeatApds(coldTest.getProjSpHeatApds());
			dto.setProjSpHefApds(coldTest.getProjSpHefApds());
			dto.setParallelism(coldTest.getParallelism());
			dto.setTorqHandwheel(coldTest.getTorqHandwheel());
			LineItemApplicableTest lineItemApplicableTest = coldTest.getLineItemApplicableTest();
			if (lineItemApplicableTest != null) {
				dto.setApplicableTestId(lineItemApplicableTest.getId());
			} else {
				logger.warn("LineItemApplicableTest not found for ColdTest with id: {}", coldTest.getId());
			}

			OlLineItem olLineItem = coldTest.getOlLineItem();
			if (olLineItem != null) {
				dto.setOlLineItemId(olLineItem.getId());
			} else {
				logger.warn("olLineItem not found for ColdTest with id: {}", coldTest.getId());
			}

			dtos.add(dto);
		}

		logger.info("Number of ColdTestDto being returned: {}", dtos.size());
		return dtos;
	}

	public List<VoltDto> getPhCurrS(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<VoltDto> phCurrDtos = new ArrayList<>();
		try {
			List<Object[]> phCurrDetails = coldTestRepository.findAteProductPhCurrS(fromDate, toDate);

			if (phCurrDetails != null && !phCurrDetails.isEmpty()) {
				for (Object[] phCurrDet : phCurrDetails) {
					VoltDto phCurrDto = new VoltDto();
					phCurrDto.setLabel((String) phCurrDet[0]);
					phCurrDto.setValue((Double) phCurrDet[1]);
					phCurrDtos.add(phCurrDto);
				}
			} else {
				phCurrDtos.add(new VoltDto());
			}
		} catch (Exception e) {
			 logger.error("Failed to retrieve Phase Current S data for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return phCurrDtos;
	}

	public List<VoltDto> getPhCurrR(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<VoltDto> phCurrDtos = new ArrayList<>();
		try {
			List<Object[]> phCurrDetails = coldTestRepository.findAteProductPhCurrR( fromDate, toDate);

			if (phCurrDetails != null && !phCurrDetails.isEmpty()) {
				for (Object[] phCurrDet : phCurrDetails) {
					VoltDto phCurrDto = new VoltDto();
					phCurrDto.setLabel((String) phCurrDet[0]);
					phCurrDto.setValue((Double) phCurrDet[1]);
					phCurrDtos.add(phCurrDto);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to retrieve Phase Current R data for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return phCurrDtos;
	}

	public List<VoltDto> getPhCurrT(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<VoltDto> phCurrDtos = new ArrayList<>();
		try {
			List<Object[]> phCurrDetails = coldTestRepository.findAteProductPhCurrT( fromDate, toDate);

			if (phCurrDetails != null && !phCurrDetails.isEmpty()) {
				for (Object[] phCurrDet : phCurrDetails) {
					VoltDto phCurrDto = new VoltDto();
					phCurrDto.setLabel((String) phCurrDet[0]);
					phCurrDto.setValue((Double) phCurrDet[1]);
					phCurrDtos.add(phCurrDto);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to retrieve Phase Current T data for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return phCurrDtos;
	}

	public List<MechSp9vDto> getMechSp9v(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<MechSp9vDto> mechSp9vDtos = new ArrayList<>();
		try {
			List<Object[]> mechSp9vDetails = coldTestRepository.findAteProductMechSp9v(fromDate, toDate);

			if (mechSp9vDetails != null && !mechSp9vDetails.isEmpty()) {
				for (Object[] mechSp9vDet : mechSp9vDetails) {
					MechSp9vDto mechSp9vDto = new MechSp9vDto();
					mechSp9vDto.setLabel((String) mechSp9vDet[0]);
					mechSp9vDto.setMechSp9vFor((Double) mechSp9vDet[1]);
					mechSp9vDto.setMechSp9vBack((Double) mechSp9vDet[2]);
					mechSp9vDtos.add(mechSp9vDto);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to retrieve MechSp9v data for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return mechSp9vDtos;
	}
	
	public List<VoltDto> getVoltDevDets(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<VoltDto> voltDtos = new ArrayList<>();
		try {
			List<Object[]> voltDetails = coldTestRepository.findAteProductPhDetVolt( fromDate, toDate);

			if (voltDetails != null && !voltDetails.isEmpty()) {
				for (Object[] voltDet : voltDetails) {
					VoltDto voltDto = new VoltDto();
					voltDto.setLabel((String) voltDet[0]);
					voltDto.setValue((Double) voltDet[1]);
					voltDtos.add(voltDto);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to retrieve Volt Details for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return voltDtos;
	}

	public List<MechSp14vDto> getMechSp14v(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<MechSp14vDto> mechSp14vDtos = new ArrayList<>();
		try {
			List<Object[]> mechSp14vDetails = coldTestRepository.findAteProductMechSp14v(fromDate, toDate);

			if (mechSp14vDetails != null && !mechSp14vDetails.isEmpty()) {
				for (Object[] mechSp14vDet : mechSp14vDetails) {
					MechSp14vDto mechSp14vDto = new MechSp14vDto();
					mechSp14vDto.setLabel((String) mechSp14vDet[0]);
					mechSp14vDto.setMechSp14vFor((Double) mechSp14vDet[1]);
					mechSp14vDto.setMechSp14vBack((Double) mechSp14vDet[2]);
					mechSp14vDtos.add(mechSp14vDto);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to retrieve MechSp14v data for ProductType: {}, from {} to {}", 
	                  productTypeId, fromDate, toDate, e);
		}
		return mechSp14vDtos;
	}

	public List<IolDataDto> getDevDets(int testId, int olLineItemId) {
		List<IolDataDto> iolDetDtos = new ArrayList<>();
		try {
			List<Object[]> iolDetails = coldTestRepository.getDevDet(testId, olLineItemId);

			if (iolDetails != null && !iolDetails.isEmpty()) {
				for (Object[] iolDet : iolDetails) {
					IolDataDto iolDataDto = new IolDataDto();
					if (iolDet[0] != null) {
						iolDataDto.setDevId((Integer) iolDet[0]);
					} else {
						iolDataDto.setDevId(null);
					}
					if (iolDet[1] != null) {
						iolDataDto.setDevNo((String) iolDet[1]);
					} else {
						iolDataDto.setDevNo(null);
					}
					if (iolDet[2] != null) {
						iolDataDto.setStatus((Integer) iolDet[2]);
					} else {
						iolDataDto.setStatus(null);
					}
					if (iolDet[3] != null) {
						iolDataDto.setRemarks((String) iolDet[3]);
					} else {
						iolDataDto.setRemarks(null);
					}
					if (iolDet[4] != null) {
						iolDataDto.setTestDate((String) iolDet[4]);
					} else {
						iolDataDto.setTestDate(null);
					}
					if (iolDet[5] != null) {
						iolDataDto.setTestTypeId((Integer) iolDet[5]);
					} else {
						iolDataDto.setTestTypeId(null);
					}
					iolDetDtos.add(iolDataDto);
				}
			}
		} catch (Exception e) {
			 logger.error("Error occurred while fetching device details for testId: {} and olLineItemId: {}", 
	                  testId, olLineItemId, e);
		}
		return iolDetDtos;
	}
	
	
	public ColdTestDto getByInspRoundId(int inspRoundId, int hotCold) {

		ColdTest coldTest = coldTestRepository.findByInspRndHotCold(inspRoundId, hotCold);
		ColdTestDto dto = new ColdTestDto();
		if (coldTest != null) {
			dto.setId(coldTest.getId());
			dto.setHotCold(coldTest.getHotCold());
			dto.setTestType(coldTest.getTestType());
			String devNo = coldTest.getInspectionRound().getLineItemSerialNo()!=null ? coldTest.getInspectionRound().getLineItemSerialNo().getSerialNo():null;
			dto.setDeviceNo(devNo);
			dto.setTestDate(coldTest.getTestDate());
			dto.setVolt(coldTest.getVolt());
			dto.setPhCurrR(coldTest.getPhCurrR());
			dto.setPhCurrS(coldTest.getPhCurrS());
			dto.setPhCurrT(coldTest.getPhCurrT());
			dto.setOcularHeating(coldTest.getOcularHeating());
			dto.setCommander(coldTest.getCommander());
			dto.setAutoMan(coldTest.getAutoMan());
			dto.setRcTime(coldTest.getRcTime());
			dto.setRcTimeBid(coldTest.getRcTimeBid());
			dto.setRcTimeDs(coldTest.getRcTimeDs());
			dto.setWorkOffDTime(coldTest.getWorkOffDTime());
			dto.setWorkOffDBid(coldTest.getWorkOffDBid());
			dto.setWorkOffDDs(coldTest.getWorkOffDDs());
			dto.setFailureSingle(coldTest.getFailureSingle());
			dto.setFailureSingleBid(coldTest.getFailureSingleBid());
			dto.setFailureSingleDs(coldTest.getFailureSingleDs());
			dto.setMutliArresting(coldTest.getMutliArresting());
			dto.setSingnalRotaryTrans(coldTest.getSignalRotaryTrans());
			dto.setFireContact(coldTest.getFireContact());
			dto.setFireContactBid(coldTest.getFireContactBid());
			dto.setFireContactDs(coldTest.getFireContactDs());
			dto.setCompensation(coldTest.getCompensation());
			dto.setCompMgFire(coldTest.getCompMgFire());
			dto.setVoltSoftClockP1(coldTest.getVoltSoftClockP1());
			dto.setVoltSoftAntiClockP1(coldTest.getVoltSoftAntiClockP1());
			dto.setVoltSoftClockP2(coldTest.getVoltSoftClockP2());
			dto.setVoltSoftAntiClockP2(coldTest.getVoltSoftAntiClockP2());
			dto.setVoltHardClockP1(coldTest.getVoltHardClockP1());
			dto.setVoltHardAntiClockP1(coldTest.getVoltHardAntiClockP1());
			dto.setVoltHardClockP2(coldTest.getVoltHardClockP2());
			dto.setVoltHardAntiClockP2(coldTest.getVoltHardAntiClockP2());
			dto.setVoltZeroClockP1(coldTest.getVoltZeroClockP1());
			dto.setVoltZeroAntiClockP1(coldTest.getVoltZeroAntiClockP1());
			dto.setSafeContPt5v(coldTest.getSafeContPt5v());
			dto.setSafeContPt9v(coldTest.getSafeContPt9v());
			dto.setBlocking(coldTest.getBlocking().trim());
			dto.setMaxUpwCurrSpeed(coldTest.getMaxUpwCurrSpeed());
			dto.setMaxUpwCurrP1(coldTest.getMaxUpwCurrP1());
			dto.setMaxDownCurrSpeed(coldTest.getMaxDownCurrSpeed());
			dto.setMaxDownCurrP2(coldTest.getMaxDownCurrP2());
			dto.setMinAimSpeedUp(coldTest.getMinAimSpeedUp());
			dto.setMinAimSpeedDown(coldTest.getMinAimSpeedDown());
			dto.setEvenessSightUp(coldTest.getEvenessSightUp());
			dto.setEvenessSightDown(coldTest.getEvenessSightDown());
			dto.setZeroSetting(coldTest.getZeroSetting());
			dto.setMechSp9vForw(coldTest.getMechSp9vForw());
			dto.setMechSp9vBackw(coldTest.getMechSp9vBackw());
			dto.setMechSp14vForw(coldTest.getMechSp14vForw());
			dto.setMechSp14vBackw(coldTest.getMechSp14vBackw());
			dto.setMechSpPhCurrR(coldTest.getMechSpPhCurrR());
			dto.setMechSpPhCurrS(coldTest.getMechSpPhCurrS());
			dto.setDriftSpeedPl0Deg(coldTest.getDriftSpeedPl0Deg());
			dto.setDriftSpeedMi5Deg(coldTest.getDriftSpeedMi5Deg());
			dto.setIllumination(coldTest.getIllumination());
			dto.setProjSpApdsHef(coldTest.getProjSpApdsHef());
			dto.setProjSpApdsHeat(coldTest.getProjSpApdsHeat());
			dto.setProjSpHefHeat(coldTest.getProjSpHefHeat());
			dto.setProjSpHeatHef(coldTest.getProjSpHeatHef());
			dto.setProjSpHeatApds(coldTest.getProjSpHeatApds());
			dto.setProjSpHefApds(coldTest.getProjSpHefApds());
			dto.setParallelism(coldTest.getParallelism());
			dto.setTorqHandwheel(coldTest.getTorqHandwheel());

//			OlLineItem olLineItem = coldTest.getOlLineItem();
//			if (olLineItem != null) {
//				dto.setOlLineItemId(olLineItem.getId());
//			} else {
//				logger.warn("olLineItem not found for ColdTest with id: " + coldTest.getId());
//			}
			
			SubSlipLineItem olLineItem = coldTest.getSubSlipLineItem();
			if (olLineItem != null) {
				dto.setSslipLineItemId(olLineItem.getId());
			} else {
				logger.warn("olLineItem not found for ColdTest with id: {}", coldTest.getId());
			}
		}

		return dto;
	}

}
