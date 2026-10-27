package in.co.bel.olms.iol.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.iol.ate.dto.InspectionRoundDto;
import in.co.bel.olms.iol.ate.service.InspectionRoundService;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.dto.ColdTestDto;
import in.co.bel.olms.iol.entity.ColdTest;
import in.co.bel.olms.iol.repository.ColdTestRepository;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.LineItemApplicableTestRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IOLColdTestDetFetchService {

	private static final Logger logger = LoggerFactory.getLogger(IOLColdTestDetFetchService.class);

	private final LineItemApplicableTestRepository itemApplicableTestRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final ColdTestRepository coldTestRepository;

	private final Mapper mapper;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final InspectionRoundService inspectionRoundService;
	
	Gson gson = new Gson();
	
	SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
	
	public ColdTest getColdTestData(ColdTestDto dto) {
		ColdTest coldTest = mapper.map(dto, ColdTest.class);
		Optional<LineItemApplicableTest> applicableTestOptional = itemApplicableTestRepository.findById(dto.getApplicableTestId());
	    Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(dto.getOlLineItemId());
        Optional<ColdTest> coldTestOptional = coldTestRepository.findById(dto.getId());
        
        if (coldTestOptional.isPresent()) {
        	ColdTest existcoldTest = coldTestOptional.get();
        	coldTest.setSubmittedDate(existcoldTest.getSubmittedDate());
        	coldTest.setCreatedDatetime(existcoldTest.getCreatedDatetime());
        } else {
        	coldTest.setSubmittedDate(null);
        	if (dto.getCreatedDatetime() != null) {
        		coldTest.setCreatedDatetime(dto.getCreatedDatetime());
        	} else {
        		coldTest.setCreatedDatetime(null);
        	}
        }
	    
        
        
       InspectionRoundDto inspDto= new InspectionRoundDto ();
       inspDto.setDevId(dto.getDevId());
       inspDto.setOlLineItemId(dto.getOlLineItemId());
       inspDto.setProductId(dto.getProductId());
//       inspDto.setTestId(dto.getTestId());
       inspDto.setApprovedById(dto.getApprovedById());
               
        InspectionRound savedInspectionRound = null;
		if (dto.getId() == 0) {
			InspectionRound inspectionRound = inspectionRoundService.populateInspectionRound(inspDto, true, savedInspectionRound);
			inspectionRound.setCreatedDatetime(new Date());
			savedInspectionRound = inspectionRoundRepository.save(inspectionRound);
		} else {
			Optional<InspectionRound> inspectionRoundOptional = inspectionRoundRepository.findById(dto.getId());
			
			if (inspectionRoundOptional.isPresent()) {
				savedInspectionRound = inspectionRoundOptional.get();
			} else {
				throw new DataNotFoundException("Save : Inspection Id not found:" + dto.getId());
			}
		}
		
		
		coldTest.setInspectionRound(savedInspectionRound);
		
	    if (applicableTestOptional.isPresent()) {
	        coldTest.setLineItemApplicableTest(applicableTestOptional.get());
	    } else {
	        throw new IllegalArgumentException("Invalid applicable test id ");
	    }

	    if (olLineItemOptional.isPresent()) {
	        coldTest.setOlLineItem(olLineItemOptional.get());
	    } else {
	        throw new IllegalArgumentException("Invalid line item id ");
	    }

	    coldTest.setId(dto.getId());
	    coldTest.setTestType(dto.getTestType());
	   
	    coldTest.setTestDate(dto.getTestDate());
	    coldTest.setVolt(dto.getVolt());
	    coldTest.setPhCurrR(dto.getPhCurrR());
	    coldTest.setPhCurrS(dto.getPhCurrS());
	    coldTest.setPhCurrT(dto.getPhCurrT());
	    coldTest.setOcularHeating(dto.getOcularHeating());
	    coldTest.setCommander(dto.getCommander());
	    coldTest.setAutoMan(dto.getAutoMan());
	    coldTest.setRcTime(dto.getRcTime());
	    coldTest.setRcTimeBid(dto.getRcTimeBid());
	    coldTest.setRcTimeDs(dto.getRcTimeDs());
	    coldTest.setWorkOffDTime(dto.getWorkOffDTime());
	    coldTest.setWorkOffDBid(dto.getWorkOffDBid());
	    coldTest.setWorkOffDDs(dto.getWorkOffDDs());
	    coldTest.setFailureSingle(dto.getFailureSingle());
	    coldTest.setFailureSingleBid(dto.getFailureSingleBid());
	    coldTest.setFailureSingleDs(dto.getFailureSingleDs());
	    coldTest.setMutliArresting(dto.getMutliArresting());
	    coldTest.setSignalRotaryTrans(dto.getSingnalRotaryTrans());
	    coldTest.setFireContact(dto.getFireContact());
	    coldTest.setFireContactBid(dto.getFireContactBid());
	    coldTest.setFireContactDs(dto.getFireContactDs());
	    coldTest.setCompensation(dto.getCompensation());
	    coldTest.setCompMgFire(dto.getCompMgFire());
	    coldTest.setVoltSoftClockP1(dto.getVoltSoftClockP1());
	    coldTest.setVoltSoftAntiClockP1(dto.getVoltSoftAntiClockP1());
	    coldTest.setVoltSoftClockP2(dto.getVoltSoftClockP2());
	    coldTest.setVoltSoftAntiClockP2(dto.getVoltSoftAntiClockP2());
	    coldTest.setVoltHardClockP1(dto.getVoltHardClockP1());
	    coldTest.setVoltHardAntiClockP1(dto.getVoltHardAntiClockP1());
	    coldTest.setVoltHardClockP2(dto.getVoltHardClockP2());
	    coldTest.setVoltHardAntiClockP2(dto.getVoltHardAntiClockP2());
	    coldTest.setVoltZeroClockP1(dto.getVoltZeroClockP1());
	    coldTest.setVoltZeroAntiClockP1(dto.getVoltZeroAntiClockP1());
	    coldTest.setSafeContPt5v(dto.getSafeContPt5v());
	    coldTest.setSafeContPt9v(dto.getSafeContPt9v());
	    coldTest.setBlocking(dto.getBlocking());
	    coldTest.setMaxUpwCurrSpeed(dto.getMaxUpwCurrSpeed());
	    coldTest.setMaxUpwCurrP1(dto.getMaxUpwCurrP1());
	    coldTest.setMaxDownCurrSpeed(dto.getMaxDownCurrSpeed());
	    coldTest.setMaxDownCurrP2(dto.getMaxDownCurrP2());
	    coldTest.setMinAimSpeedDown(dto.getMinAimSpeedDown());
	    coldTest.setMinAimSpeedUp(dto.getMinAimSpeedUp());
	    coldTest.setEvenessSightUp(dto.getEvenessSightUp());
	    coldTest.setEvenessSightDown(dto.getEvenessSightDown());
	    coldTest.setZeroSetting(dto.getZeroSetting());
	    coldTest.setMechSp9vForw(dto.getMechSp9vForw());
	    coldTest.setMechSp9vBackw(dto.getMechSp9vBackw());
	    coldTest.setMechSp14vForw(dto.getMechSp14vForw());
	    coldTest.setMechSp14vBackw(dto.getMechSp14vBackw());
	    coldTest.setMechSpPhCurrR(dto.getMechSpPhCurrR());
	    coldTest.setMechSpPhCurrS(dto.getMechSpPhCurrS());
	    coldTest.setMechSpPhCurrT(dto.getMechSpPhCurrT());
	    coldTest.setDriftSpeedPl0Deg(dto.getDriftSpeedPl0Deg());
	    coldTest.setDriftSpeedMi5Deg(dto.getDriftSpeedMi5Deg());
	    coldTest.setDriftSpeedPl15Deg(dto.getDriftSpeedPl15Deg());
	    coldTest.setIllumination(dto.getIllumination());
	    coldTest.setProjSpApdsHef(dto.getProjSpApdsHef());
	    coldTest.setProjSpApdsHeat(dto.getProjSpApdsHeat());
	    coldTest.setProjSpHefHeat(dto.getProjSpHefHeat());
	    coldTest.setProjSpHeatHef(dto.getProjSpHeatHef());
	    coldTest.setProjSpHeatApds(dto.getProjSpHeatApds());
	    coldTest.setProjSpHefApds(dto.getProjSpHefApds());
	    coldTest.setParallelism(dto.getParallelism());
	    coldTest.setTorqHandwheel(dto.getTorqHandwheel());
		return coldTest;
	}
	
	public ColdTestDto getDtoDet(ColdTest coldTest) throws ParseException {
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
		dto.setMechSpPhCurrT(coldTest.getMechSpPhCurrT());
		dto.setDriftSpeedPl0Deg(coldTest.getDriftSpeedPl0Deg());
		dto.setDriftSpeedMi5Deg(coldTest.getDriftSpeedMi5Deg());
		dto.setDriftSpeedPl15Deg(coldTest.getDriftSpeedPl15Deg());
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
		
				
		if (coldTest.getSubmittedDate() != null) {
			dto.setSubmittedDate(sdf.format(coldTest.getSubmittedDate()));
		} else {
			dto.setSubmittedDate(null);
		}
		
		if (coldTest.getCreatedDatetime() != null) {
			dto.setCreatedDatetime(coldTest.getCreatedDatetime());
		} else {
			dto.setCreatedDatetime(null);
		}
		
		if (coldTest.getStatus() != null) {
			dto.setStatus(coldTest.getStatus());
		} else {
			dto.setStatus(null);
		}
		
		if (coldTest.getRemarks() != null) {
			dto.setRemarks(coldTest.getRemarks());
		} else {
			dto.setRemarks(null);
		}
		return dto;
	}
	
}
