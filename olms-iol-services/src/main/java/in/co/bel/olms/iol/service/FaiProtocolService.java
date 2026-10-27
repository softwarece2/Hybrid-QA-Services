package in.co.bel.olms.iol.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.dto.FaiProtocolDto;
import in.co.bel.olms.iol.dto.FaiProtocolHdrDto;
import in.co.bel.olms.iol.entity.ColdTest;
import in.co.bel.olms.iol.entity.FaiProtocol;
import in.co.bel.olms.iol.repository.ColdTestRepository;
import in.co.bel.olms.iol.repository.FaiProtocolRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FaiProtocolService {
	
	private static final Logger logger = LoggerFactory.getLogger(FaiProtocolService.class);

	private final FaiProtocolRepository faiProtocolRepository;

	private final ColdTestRepository coldTestRepository;

	private final Mapper mapper;

	public FaiProtocolHdrDto getByApplicableTestId(int olId) {

		List<FaiProtocol> faiProtocolList = faiProtocolRepository.findAll();
		List<FaiProtocolDto> dtos = new ArrayList<>();
		List<ColdTest> coldTestOptional = coldTestRepository.findByOfferLetterId(olId);

		FaiProtocolHdrDto  hdrDto= new FaiProtocolHdrDto();
		
		hdrDto.setNoOfDevice(coldTestOptional.size());
		
		for(  ColdTest coldTest : coldTestOptional ) {
			hdrDto.getDeviceName().add(coldTest.getInspectionRound().getLineItemLotsRegister().getRegisterNo());				
		}
		for (FaiProtocol faiProtocol : faiProtocolList) {				
			FaiProtocolDto faiDto = mapper.map(faiProtocol, FaiProtocolDto.class);
						
			for(  ColdTest coldTest : coldTestOptional ) {
				faiDto = getColValue(faiDto, coldTest);				
			}			
			
			if(!faiDto.getObsValue().isEmpty())
			{
				dtos.add(faiDto);
			}
			
		}
		
		
		Collections.sort(dtos, new Comparator<FaiProtocolDto>() {
			public int compare(FaiProtocolDto f1, FaiProtocolDto f2) {
				return f1.getSlNo().compareTo(f2.getSlNo());
			}
		});
		hdrDto.setFaiDtos(dtos);
		return hdrDto;
	}

	private FaiProtocolDto getColValue(FaiProtocolDto faiDto, ColdTest coldTest) {
		switch (faiDto.getSlNo()) {
		case 219:
			faiDto.getObsValue().add(getVoltage(coldTest));
			break;
		case 220:
			String phCur = getPhaseCur(coldTest);
			faiDto.getObsValue().add(phCur);
			break;
		case 228:
			faiDto.getObsValue().add(getSignalRotataryTrans120mv(coldTest));
			break;
		case 231:
			faiDto.getObsValue().add(clockWiseSoftValueP1(coldTest));
			break;
		case 232:
			faiDto.getObsValue().add(clockWiseSoftValueP2(coldTest));
			break;
		case 233:
			faiDto.getObsValue().add(clockWiseHardValueP1(coldTest));
			break;
		case 234:
			faiDto.getObsValue().add(clockWiseHardValueP2(coldTest));
			break;
		case 235:
			faiDto.getObsValue().add(antiClockWiseSoftValueP1(coldTest));
			break;
		case 236:
			faiDto.getObsValue().add(antiClockWiseSoftValueP2(coldTest));
			break;
		case 237:
			faiDto.getObsValue().add(antiClockWiseHardValueP1(coldTest));
			break;
		case 238:
			faiDto.getObsValue().add(antiClockWiseHardValueP2(coldTest));
			break;
		case 247:
			faiDto.getObsValue().add(mechSpeedForwardAndBackward9V(coldTest));
			break;
		case 248:
			faiDto.getObsValue().add(mechSpeedForwardAndBackward14v(coldTest));
			break;
		case 258:
			faiDto.getObsValue().add(getProjectileSpeedLessThan2sec(coldTest));
			break;
		case 259:
			faiDto.getObsValue().add(getProjectileSpeedLessThan5sec(coldTest));
			break;
		default:
			break;
		}

		return faiDto;
	}


	private String getVoltage(ColdTest coldTest) {
		return coldTest.getVolt() + " V";
	}

	private String getPhaseCur(ColdTest coldTest) {
		
		return "Phase Current R - " + coldTest.getPhCurrR() + " Amp, Phase Current S -  "
				+ coldTest.getPhCurrS() + " Amp, Phase Current T - " + coldTest.getPhCurrT() + " Amp";
	}

	private String getProjectileSpeedLessThan2sec(ColdTest coldTest) {
		return " APDS- HEF ≤ 2.5 sec" + coldTest.getProjSpApdsHef() + " Sec\n" + " HEF- HEAT ≤ 2.5 sec"
				+ coldTest.getProjSpHefHeat() + " Sec\n" + " HEAT-APDS ≤ 2.5 sec" + coldTest.getProjSpHeatApds()
				+ " Sec";
	}

	private String getProjectileSpeedLessThan5sec(ColdTest coldTest) {
		return " APDS-HEAT ≤ 5sec " + coldTest.getProjSpApdsHeat() + " Sec\n" + " HEAT-HEF ≤ 5sec "
				+ coldTest.getProjSpHeatHef() + " Sec\n" + " HEF-APDS ≤ 5sec " + coldTest.getProjSpHefApds() + " Sec";
	}

	private String getSignalRotataryTrans120mv(ColdTest coldTest) {
		return coldTest.getSignalRotaryTrans() + " mV";
	}

	private String clockWiseSoftValueP1(ColdTest coldTest) {
		return "P1 " + coldTest.getVoltSoftClockP1() + " V";
	}

	private String clockWiseSoftValueP2(ColdTest coldTest) {
		return "P2 " + coldTest.getVoltSoftClockP2() + " V";
	}

	private String clockWiseHardValueP1(ColdTest coldTest) {
		return "P1 " + coldTest.getVoltHardClockP1() + " V";
	}

	private String clockWiseHardValueP2(ColdTest coldTest) {
		return "P2 " + coldTest.getVoltHardClockP2() + " V";
	}

	private String antiClockWiseSoftValueP1(ColdTest coldTest) {
		return "P1 " + coldTest.getVoltSoftAntiClockP1() + " V";
	}

	private String antiClockWiseSoftValueP2(ColdTest coldTest) {
		return "P2 " + coldTest.getVoltSoftAntiClockP2() + " V";
	}

	private String antiClockWiseHardValueP1(ColdTest coldTest) {
		return "P1 " + coldTest.getVoltHardAntiClockP1() + " V";
	}

	private String antiClockWiseHardValueP2(ColdTest coldTest) {
		return "P2 " + coldTest.getVoltHardAntiClockP2() + " V";
	}

	private String mechSpeedForwardAndBackward9V(ColdTest coldTest) {
		return "Forward "  + coldTest.getProjSpApdsHeat() + " Sec ," + "Backward "+ coldTest.getProjSpHeatHef() + " Sec";
	}

	private String mechSpeedForwardAndBackward14v(ColdTest coldTest) {
		return "Forward "  + coldTest.getMechSp14vForw() + " Sec , " + "Backward "+ coldTest.getMechSp14vBackw() + " Sec";

	}
}
