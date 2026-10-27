package in.co.bel.olms.iol.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.dto.FinalTestDto;
import in.co.bel.olms.iol.entity.ColdTest;
import in.co.bel.olms.iol.repository.ColdTestRepository;


@Service
public class FinalTestService {
	
	Logger logger = LoggerFactory.getLogger(FinalTestService.class);

	@Autowired
	private ColdTestRepository coldTestRepository;

	public FinalTestDto getFinalTestBytestId(int testId) {
		FinalTestDto finalTestDto = new FinalTestDto();
		Optional<ColdTest> coldTestOptional = coldTestRepository.findById(testId);
		ColdTest coldTest = coldTestOptional.get();
		finalTestDto.setSwitching27V(coldTest.getVolt());
		finalTestDto.setPhasecurrR(coldTest.getPhCurrR());
		finalTestDto.setPhasecurrS(coldTest.getPhCurrS());
		finalTestDto.setPhasecurrT(coldTest.getPhCurrT());
		finalTestDto.setIndicationCommander(coldTest.getCommander());
		finalTestDto.setProjApdsHef(coldTest.getProjSpApdsHef());
		finalTestDto.setProjSpApdsHeat(coldTest.getProjSpApdsHeat());
		finalTestDto.setProjSpHeatApds(coldTest.getProjSpHeatApds());
		finalTestDto.setProjSpHeatHef(coldTest.getProjSpHeatHef());
		finalTestDto.setProjSpHefApds(coldTest.getProjSpHefApds());
		finalTestDto.setProjSpHefHeat(coldTest.getProjSpHefHeat());
		finalTestDto.setMechSpPhCurrR(coldTest.getMechSpPhCurrR());


		finalTestDto.setMaxAimingSpeedUpward(coldTest.getMaxUpwCurrSpeed());
		finalTestDto.setMaxAimingSpeedDownward(coldTest.getMaxDownCurrSpeed());
		finalTestDto.setMinAimingSpeedUpward(coldTest.getMinAimSpeedUp());
		finalTestDto.setMinAimingSpeedDownward(coldTest.getMinAimSpeedDown());
		finalTestDto.setVoltageCWSoftP1(coldTest.getVoltSoftClockP1());
		finalTestDto.setVoltageCWHardP1(coldTest.getVoltHardClockP1());
		finalTestDto.setVoltageACWSoftP1(coldTest.getVoltSoftAntiClockP1());
		finalTestDto.setVoltageACWHardP1(coldTest.getVoltHardAntiClockP1());
		finalTestDto.setOcularHeating(coldTest.getOcularHeating());
		finalTestDto.setRechargeTime(coldTest.getRcTime());
		finalTestDto.setBlocking(coldTest.getBlocking());
		finalTestDto.setWorkOffDistanceTime(coldTest.getWorkOffDTime());
		finalTestDto.setWorkOffDBid(coldTest.getWorkOffDBid());
		finalTestDto.setWorkOffDDs(coldTest.getWorkOffDDs());
		return finalTestDto;

	}

}
