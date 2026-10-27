package in.co.bel.olms.iol.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.IOLConstants;
import in.co.bel.core.constants.IolValidationConstants;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.dto.ColdTestDto;
import in.co.bel.olms.iol.dto.DevDetDto;
import in.co.bel.olms.iol.dto.IolDataDto;
import in.co.bel.olms.iol.entity.ColdTest;
import in.co.bel.olms.iol.entity.DeviceStatusDto;
import in.co.bel.olms.iol.repository.ColdTestRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IOLColdTestService {

	private static final Logger logger = LoggerFactory.getLogger(IOLColdTestService.class);

	private final ColdTestRepository coldTestRepository;

	private final IOLColdTestDetFetchService iolColdTestDetFetchService;

	private IOLColdTestService iolColdTestService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);

	Gson gson = new Gson();

	public String saveColdTest(ColdTestDto dto) throws Exception {
		dto.setCreatedDatetime(new Date());
		ColdTest existantColdTest = coldTestRepository.getColdTestByDevId(dto.getDeviceNo());
		if (existantColdTest != null && dto.getId() == 0) {
			return "The device id already exists please enter a new device id.";
		}

		if (dto.getId() == 0) {
			ColdTest coldTest = iolColdTestDetFetchService.getColdTestData(dto);
			ColdTestDto retDto1 = mapper.map(coldTest, ColdTestDto.class);
			DeviceStatusDto statusDto = iolColdTestService.validateDto(retDto1);
			if (statusDto != null && statusDto.getStatus() == 1) {
				coldTest.setStatus(statusDto.getStatus());
				coldTest.setRemarks(statusDto.getRemarks());
			}
			if (statusDto != null && statusDto.getStatus() == 0) {
				coldTest.setStatus(statusDto.getStatus());
				coldTest.setRemarks(statusDto.getRemarks());
				return gson.toJson(statusDto);
			}
			ColdTest savedColdTest = coldTestRepository.save(coldTest);
			ColdTestDto retDto = mapper.map(savedColdTest, ColdTestDto.class);
			return gson.toJson(retDto);
		} else {
			String res = null;
			ColdTest coldTest = iolColdTestDetFetchService.getColdTestData(dto);
			ColdTestDto retDto = mapper.map(coldTest, ColdTestDto.class);
			DeviceStatusDto statusDto = iolColdTestService.validateDto(retDto);
			if (statusDto != null && statusDto.getStatus() == 1) {
				coldTest.setStatus(statusDto.getStatus());
				coldTest.setRemarks(statusDto.getRemarks());
			}
			if (statusDto != null && statusDto.getStatus() == 0) {
				coldTest.setStatus(statusDto.getStatus());
				coldTest.setRemarks(statusDto.getRemarks());
				return gson.toJson(statusDto);
			} else {
				ColdTest savedColdTest = coldTestRepository.save(coldTest);
				retDto = mapper.map(savedColdTest, ColdTestDto.class);
				res = gson.toJson(retDto);
			}
			return res;
		}
	}

	public String editColdTest(ColdTestDto dto) {
		String res = null;
		ColdTest coldTest = iolColdTestDetFetchService.getColdTestData(dto);
		ColdTestDto retDto = mapper.map(coldTest, ColdTestDto.class);
		DeviceStatusDto statusDto = iolColdTestService.validateDto(retDto);
		if (statusDto != null && statusDto.getStatus() == 1) {
			coldTest.setStatus(statusDto.getStatus());
			coldTest.setRemarks(statusDto.getRemarks());
		}
		if (statusDto != null && statusDto.getStatus() == 0) {
			coldTest.setStatus(statusDto.getStatus());
			coldTest.setRemarks(statusDto.getRemarks());
			coldTestRepository.save(coldTest);
			return gson.toJson(statusDto);
		} else {
			ColdTest savedColdTest = coldTestRepository.save(coldTest);
			retDto = mapper.map(savedColdTest, ColdTestDto.class);
			res = gson.toJson(retDto);
		}
		return res;
	}

	public List<ColdTestDto> getColdTestByDeviceId(int id) throws ParseException {
		logger.info("Fetching ColdTest records for testId: {}", id);
		List<ColdTestDto> dtos = new ArrayList<>();
		ColdTest coldTest = coldTestRepository.findByApplicableTestId(id);
		ColdTestDto dto = iolColdTestDetFetchService.getDtoDet(coldTest);
		dtos.add(dto);
		return dtos;
	}

	public String submitColdTest(ColdTestDto dto) {
		Optional<ColdTest> coldTestOptional = coldTestRepository.findById(dto.getId());
		
		if (!coldTestOptional.isPresent()) {
			throw new IllegalArgumentException("ColdTest with id " + dto.getId() + " not found");
		}

		ColdTest coldTestObj = coldTestOptional.get();
		ColdTestDto retDto = mapper.map(coldTestObj, ColdTestDto.class);
		DeviceStatusDto statusDto = iolColdTestService.validateDto(retDto);
		if (statusDto != null && statusDto.getStatus() == 1) {
			coldTestObj.setStatus(statusDto.getStatus());
			coldTestObj.setRemarks(statusDto.getRemarks());
		}
		if (statusDto != null && statusDto.getStatus() == 0) {
			coldTestObj.setStatus(statusDto.getStatus());
			coldTestObj.setRemarks(statusDto.getRemarks());
			return gson.toJson(statusDto);
		}
		coldTestObj.setSubmittedDate(new Date());
		if (dto.getRemarks() != null && !dto.getRemarks().isEmpty()) {
			coldTestObj.setRemarks(dto.getRemarks());
		} else {
			throw new IllegalArgumentException("Remarks are required for id " + dto.getId());
		}

		if (dto.getStatus() != null) {
			coldTestObj.setStatus(dto.getStatus());
		} else {
			throw new IllegalArgumentException("Status is required for id " + dto.getId());
		}

		if (dto.getVolt() != null)
			coldTestObj.setVolt(dto.getVolt());
		if (dto.getPhCurrR() != null)
			coldTestObj.setPhCurrR(dto.getPhCurrR());
		if (dto.getPhCurrS() != null)
			coldTestObj.setPhCurrS(dto.getPhCurrS());
		if (dto.getPhCurrT() != null)
			coldTestObj.setPhCurrT(dto.getPhCurrT());
		if (dto.getOcularHeating() != null)
			coldTestObj.setOcularHeating(dto.getOcularHeating());
		if (dto.getCommander() != null)
			coldTestObj.setCommander(dto.getCommander());
		if (dto.getAutoMan() != null)
			coldTestObj.setAutoMan(dto.getAutoMan());
		if (dto.getRcTime() != null)
			coldTestObj.setRcTime(dto.getRcTime());
		if (dto.getRcTimeBid() != null)
			coldTestObj.setRcTimeBid(dto.getRcTimeBid());
		if (dto.getRcTimeDs() != null)
			coldTestObj.setRcTimeDs(dto.getRcTimeDs());
		if (dto.getWorkOffDTime() != null)
			coldTestObj.setWorkOffDTime(dto.getWorkOffDTime());
		if (dto.getWorkOffDBid() != null)
			coldTestObj.setWorkOffDBid(dto.getWorkOffDBid());
		if (dto.getWorkOffDDs() != null)
			coldTestObj.setWorkOffDDs(dto.getWorkOffDDs());
		if (dto.getFailureSingle() != null)
			coldTestObj.setFailureSingle(dto.getFailureSingle());
		if (dto.getFailureSingleBid() != null)
			coldTestObj.setFailureSingleBid(dto.getFailureSingleBid());
		if (dto.getFailureSingleDs() != null)
			coldTestObj.setFailureSingleDs(dto.getFailureSingleDs());
		if (dto.getMutliArresting() != null)
			coldTestObj.setMutliArresting(dto.getMutliArresting());
		if (dto.getSingnalRotaryTrans() != null)
			coldTestObj.setSignalRotaryTrans(dto.getSingnalRotaryTrans());
		if (dto.getFireContact() != null)
			coldTestObj.setFireContact(dto.getFireContact());
		if (dto.getFireContactBid() != null)
			coldTestObj.setFireContactBid(dto.getFireContactBid());
		if (dto.getFireContactDs() != null)
			coldTestObj.setFireContactDs(dto.getFireContactDs());
		if (dto.getCompensation() != null)
			coldTestObj.setCompensation(dto.getCompensation());
		if (dto.getCompMgFire() != null)
			coldTestObj.setCompMgFire(dto.getCompMgFire());
		if (dto.getVoltSoftClockP1() != null)
			coldTestObj.setVoltSoftClockP1(dto.getVoltSoftClockP1());
		if (dto.getVoltSoftAntiClockP1() != null)
			coldTestObj.setVoltSoftAntiClockP1(dto.getVoltSoftAntiClockP1());
		if (dto.getVoltSoftClockP2() != null)
			coldTestObj.setVoltSoftClockP2(dto.getVoltSoftClockP2());
		if (dto.getVoltSoftAntiClockP2() != null)
			coldTestObj.setVoltSoftAntiClockP2(dto.getVoltSoftAntiClockP2());
		if (dto.getVoltHardClockP1() != null)
			coldTestObj.setVoltHardClockP1(dto.getVoltHardClockP1());
		if (dto.getVoltHardAntiClockP1() != null)
			coldTestObj.setVoltHardAntiClockP1(dto.getVoltHardAntiClockP1());
		if (dto.getVoltHardClockP2() != null)
			coldTestObj.setVoltHardClockP2(dto.getVoltHardClockP2());
		if (dto.getVoltHardAntiClockP2() != null)
			coldTestObj.setVoltHardAntiClockP2(dto.getVoltHardAntiClockP2());
		if (dto.getVoltZeroClockP1() != null)
			coldTestObj.setVoltZeroClockP1(dto.getVoltZeroClockP1());
		if (dto.getVoltZeroAntiClockP1() != null)
			coldTestObj.setVoltZeroAntiClockP1(dto.getVoltZeroAntiClockP1());
		if (dto.getSafeContPt5v() != null)
			coldTestObj.setSafeContPt5v(dto.getSafeContPt5v());
		if (dto.getSafeContPt9v() != null)
			coldTestObj.setSafeContPt9v(dto.getSafeContPt9v());
		if (dto.getBlocking() != null)
			coldTestObj.setBlocking(dto.getBlocking());
		if (dto.getMaxUpwCurrSpeed() != null)
			coldTestObj.setMaxUpwCurrSpeed(dto.getMaxUpwCurrSpeed());
		if (dto.getMaxUpwCurrP1() != null)
			coldTestObj.setMaxUpwCurrP1(dto.getMaxUpwCurrP1());
		if (dto.getMaxDownCurrSpeed() != null)
			coldTestObj.setMaxDownCurrSpeed(dto.getMaxDownCurrSpeed());
		if (dto.getMaxDownCurrP2() != null)
			coldTestObj.setMaxDownCurrP2(dto.getMaxDownCurrP2());
		if (dto.getMinAimSpeedDown() != null)
			coldTestObj.setMinAimSpeedDown(dto.getMinAimSpeedDown());
		if (dto.getMinAimSpeedUp() != null)
			coldTestObj.setMinAimSpeedUp(dto.getMinAimSpeedUp());
		if (dto.getEvenessSightUp() != null)
			coldTestObj.setEvenessSightUp(dto.getEvenessSightUp());
		if (dto.getEvenessSightDown() != null)
			coldTestObj.setEvenessSightDown(dto.getEvenessSightDown());
		if (dto.getZeroSetting() != null)
			coldTestObj.setZeroSetting(dto.getZeroSetting());
		if (dto.getMechSp9vForw() != null)
			coldTestObj.setMechSp9vForw(dto.getMechSp9vForw());
		if (dto.getMechSp9vBackw() != null)
			coldTestObj.setMechSp9vBackw(dto.getMechSp9vBackw());
		if (dto.getMechSp14vForw() != null)
			coldTestObj.setMechSp14vForw(dto.getMechSp14vForw());
		if (dto.getMechSp14vBackw() != null)
			coldTestObj.setMechSp14vBackw(dto.getMechSp14vBackw());
		if (dto.getMechSpPhCurrR() != null)
			coldTestObj.setMechSpPhCurrR(dto.getMechSpPhCurrR());
		if (dto.getMechSpPhCurrS() != null)
			coldTestObj.setMechSpPhCurrS(dto.getMechSpPhCurrS());
		if (dto.getMechSpPhCurrT() != null)
			coldTestObj.setMechSpPhCurrT(dto.getMechSpPhCurrT());
		if (dto.getDriftSpeedPl0Deg() != null)
			coldTestObj.setDriftSpeedPl0Deg(dto.getDriftSpeedPl0Deg());
		if (dto.getDriftSpeedMi5Deg() != null)
			coldTestObj.setDriftSpeedMi5Deg(dto.getDriftSpeedMi5Deg());
		if (dto.getDriftSpeedPl15Deg() != null)
			coldTestObj.setDriftSpeedPl15Deg(dto.getDriftSpeedPl15Deg());
		if (dto.getIllumination() != null)
			coldTestObj.setIllumination(dto.getIllumination());
		if (dto.getProjSpApdsHef() != null)
			coldTestObj.setProjSpApdsHef(dto.getProjSpApdsHef());
		if (dto.getProjSpApdsHeat() != null)
			coldTestObj.setProjSpApdsHeat(dto.getProjSpApdsHeat());
		if (dto.getProjSpHefHeat() != null)
			coldTestObj.setProjSpHefHeat(dto.getProjSpHefHeat());
		if (dto.getProjSpHeatHef() != null)
			coldTestObj.setProjSpHeatHef(dto.getProjSpHeatHef());
		if (dto.getProjSpHeatApds() != null)
			coldTestObj.setProjSpHeatApds(dto.getProjSpHeatApds());
		if (dto.getProjSpHefApds() != null)
			coldTestObj.setProjSpHefApds(dto.getProjSpHefApds());
		if (dto.getParallelism() != null)
			coldTestObj.setParallelism(dto.getParallelism());
		if (dto.getTorqHandwheel() != null)
			coldTestObj.setTorqHandwheel(dto.getTorqHandwheel());

		coldTestRepository.save(coldTestObj);

		return gson.toJson(mapper.map(coldTestObj, ColdTestDto.class));
	}

	public List<DeviceStatusDto> getDeviceByHotOrColdId(int id) {
		List<DeviceStatusDto> deviceStatusDtoList = new ArrayList<>();
		if (id == IOLConstants.COLD_TEST_ID) {
			List<ColdTest> coldTests = coldTestRepository.getDeviceByHotOrColdId(IOLConstants.COLD_TEST_ID);
			for (ColdTest coldTest : coldTests) {
				DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
				deviceStatusDto.setId(coldTest.getId());
				deviceStatusDto.setDeviceNo(coldTest.getInspectionRound().getLineItemLotsRegister().getRegisterNo());
				deviceStatusDto.setStatus(coldTest.getStatus());
				deviceStatusDto.setTestDate(coldTest.getTestDate());
				if (coldTest.getSubmittedDate() != null) {
				deviceStatusDto.setSubmmittedDate(dateFormat.format(coldTest.getSubmittedDate()));
				} else {
					deviceStatusDto.setSubmmittedDate(null);
				}
				if (coldTest.getRemarks() != null) {
					deviceStatusDto.setRemarks(coldTest.getRemarks());
					} else {
						deviceStatusDto.setRemarks(null);
					}
				deviceStatusDtoList.add(deviceStatusDto);
			}
		} else if (id == IOLConstants.HOT_TEST_ID) {
			List<ColdTest> hotTest = coldTestRepository.getDeviceByHotOrColdId(IOLConstants.HOT_TEST_ID);
			for (ColdTest coldTest : hotTest) {
				DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
				deviceStatusDto.setId(coldTest.getId());
				deviceStatusDto.setDeviceNo(coldTest.getInspectionRound().getLineItemLotsRegister().getRegisterNo());
				deviceStatusDto.setStatus(coldTest.getStatus());
				deviceStatusDto.setTestDate(coldTest.getTestDate());
				if (coldTest.getSubmittedDate() != null) {
					deviceStatusDto.setSubmmittedDate(dateFormat.format(coldTest.getSubmittedDate()));
					} else {
						deviceStatusDto.setSubmmittedDate(null);
					}
				if (coldTest.getRemarks() != null) {
					deviceStatusDto.setRemarks(coldTest.getRemarks());
					} else {
						deviceStatusDto.setRemarks(null);
					}
				deviceStatusDtoList.add(deviceStatusDto);
			}

		} else {
			throw new IllegalArgumentException("Invalid Hot or Cold ID: " + id);
		}

		return deviceStatusDtoList;
	}

	public List<DeviceStatusDto> getSubmittedDetailsByOlId(int olId) {
		List<DeviceStatusDto> deviceStatusDtoList = new ArrayList<>();
		List<ColdTest> coldTests = coldTestRepository.findByOfferLetterId(olId);
		for (ColdTest coldTest : coldTests) {
			DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
			deviceStatusDto.setId(coldTest.getId());
			deviceStatusDto.setDeviceNo(coldTest.getInspectionRound().getLineItemLotsRegister().getRegisterNo());
			deviceStatusDto.setStatus(coldTest.getStatus());
			deviceStatusDto.setRemarks(coldTest.getRemarks());
			if (coldTest.getSubmittedDate() != null) {
				deviceStatusDto.setSubmmittedDate(dateFormat.format(coldTest.getSubmittedDate()));
			}
			deviceStatusDtoList.add(deviceStatusDto);
		}
		return deviceStatusDtoList;
	}

	public DeviceStatusDto validateDto(ColdTestDto dto) {
	    DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
	    StringBuilder remarks = new StringBuilder();
	    int remarkCount = 1;

	    if (dto.getVolt() == null || dto.getVolt() < Double.parseDouble(IolValidationConstants.VOLTAGE_MIN.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The voltage should be >= ").append(IolValidationConstants.VOLTAGE_MIN).append("\n");
	    }
	    if (dto.getVolt() == null || dto.getVolt() > Double.parseDouble(IolValidationConstants.VOLTAGE_MAX.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The voltage should be <= ").append(IolValidationConstants.VOLTAGE_MAX).append("\n");
	    }

	    if (dto.getPhCurrR() == null || dto.getPhCurrR() > Double.parseDouble(IolValidationConstants.PHASE_CURRENT_R_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The phase current R should be <= ").append(IolValidationConstants.PHASE_CURRENT_R_MAX).append("\n");
	    }
	    if (dto.getPhCurrS() == null || dto.getPhCurrS() > Double.parseDouble(IolValidationConstants.PHASE_CURRENT_S_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The phase current S should be <= ").append(IolValidationConstants.PHASE_CURRENT_S_MAX).append("\n");
	    }
	    if (dto.getPhCurrT() == null || dto.getPhCurrT() > Double.parseDouble(IolValidationConstants.PHASE_CURRENT_T_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The phase current T should be <= ").append(IolValidationConstants.PHASE_CURRENT_T_MAX).append("\n");
	    }
	    
	    if(dto.getMaxUpwCurrSpeed()== null || dto.getMaxUpwCurrSpeed()< Double.parseDouble(IolValidationConstants.AIMING_SPEED_CURRENT_UPWARD_MIN.replace(" Amp", ""))) {
	    	remarks.append(remarkCount++).append(". The Upward current speed should be >= ").append(IolValidationConstants.AIMING_SPEED_CURRENT_UPWARD_MIN).append("\n");
	    }
	    
	    if(dto.getMaxUpwCurrSpeed()== null || dto.getMaxDownCurrSpeed()< Double.parseDouble(IolValidationConstants.AIMING_SPEED_CURRENT_DOWNWARD_MIN.replace(" Amp", ""))) {
	    	remarks.append(remarkCount++).append(". The Downward current speed should be >= ").append(IolValidationConstants.AIMING_SPEED_CURRENT_DOWNWARD_MIN).append("\n");
	    }

	    if (dto.getMechSp9vForw() == null || dto.getMechSp9vForw() < Double.parseDouble(IolValidationConstants.MECH_SPEED_9V_FORWARD_MIN.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 9v forward should be >= ").append(IolValidationConstants.MECH_SPEED_9V_FORWARD_MIN).append("\n");
	    }
	    if (dto.getMechSp9vForw() == null || dto.getMechSp9vForw() > Double.parseDouble(IolValidationConstants.MECH_SPEED_9V_FORWARD_MAX.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 9v forward should be <= ").append(IolValidationConstants.MECH_SPEED_9V_FORWARD_MAX).append("\n");
	    }
	    if (dto.getMechSp9vBackw() == null || dto.getMechSp9vBackw() < Double.parseDouble(IolValidationConstants.MECH_SPEED_9V_BACKWARD_MIN.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 9v backward should be >= ").append(IolValidationConstants.MECH_SPEED_9V_BACKWARD_MIN).append("\n");
	    }
	    if (dto.getMechSp9vBackw() == null || dto.getMechSp9vBackw() > Double.parseDouble(IolValidationConstants.MECH_SPEED_9V_BACKWARD_MAX.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 9v backward should be <= ").append(IolValidationConstants.MECH_SPEED_9V_BACKWARD_MAX).append("\n");
	    }

	    if (dto.getMechSp14vForw() == null || dto.getMechSp14vForw() < Double.parseDouble(IolValidationConstants.MECH_SPEED_14V_FORWARD_MIN.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 14v forward should be >= ").append(IolValidationConstants.MECH_SPEED_14V_FORWARD_MIN).append("\n");
	    }
	    if (dto.getMechSp14vForw() == null || dto.getMechSp14vForw() > Double.parseDouble(IolValidationConstants.MECH_SPEED_14V_FORWARD_MAX.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 14v forward should be <= ").append(IolValidationConstants.MECH_SPEED_14V_FORWARD_MAX).append("\n");
	    }
	    if (dto.getMechSp14vBackw() == null || dto.getMechSp14vBackw() < Double.parseDouble(IolValidationConstants.MECH_SPEED_14V_BACKWARD_MIN.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 14v backward should be >= ").append(IolValidationConstants.MECH_SPEED_14V_BACKWARD_MIN).append("\n");
	    }
	    if (dto.getMechSp14vBackw() == null || dto.getMechSp14vBackw() > Double.parseDouble(IolValidationConstants.MECH_SPEED_14V_BACKWARD_MAX.replace(" V", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical Speed 14v backward should be <= ").append(IolValidationConstants.MECH_SPEED_14V_BACKWARD_MAX).append("\n");
	    }

	    if (dto.getMechSpPhCurrR() == null || dto.getMechSpPhCurrR() > Double.parseDouble(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_R_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical speed phase current R should be <= ").append(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_R_MAX).append("\n");
	    }
	    if (dto.getMechSpPhCurrS() == null || dto.getMechSpPhCurrS() > Double.parseDouble(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_S_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical speed phase current S should be <= ").append(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_S_MAX).append("\n");
	    }
	    if (dto.getMechSpPhCurrT() == null || dto.getMechSpPhCurrT() > Double.parseDouble(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_T_MAX.replace(" Amp", ""))) {
	        remarks.append(remarkCount++).append(". The mechanical speed phase current T should be <= ").append(IolValidationConstants.MECH_SPEED_PHASE_CURRENT_T_MAX).append("\n");
	    }

	    if (dto.getProjSpApdsHef() == null || dto.getProjSpApdsHef() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_APDS_HEF_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time APDS-HEF should be <= ").append(IolValidationConstants.PROJ_SPEED_APDS_HEF_MAX).append("\n");
	    }
	    if (dto.getProjSpApdsHeat() == null || dto.getProjSpApdsHeat() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_APDS_HEAT_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time APDS-HEAT should be <= ").append(IolValidationConstants.PROJ_SPEED_APDS_HEAT_MAX).append("\n");
	    }
	    if (dto.getProjSpHefHeat() == null || dto.getProjSpHefHeat() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_HEF_HEAT_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time HEF-HEAT should be <= ").append(IolValidationConstants.PROJ_SPEED_HEF_HEAT_MAX).append("\n");
	    }
	    if (dto.getProjSpHeatHef() == null || dto.getProjSpHeatHef() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_HEAT_HEF_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time HEAT-HEF should be <= ").append(IolValidationConstants.PROJ_SPEED_HEAT_HEF_MAX).append("\n");
	    }
	    if (dto.getProjSpHeatApds() == null || dto.getProjSpHeatApds() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_HEAT_APDS_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time HEAT-APDS should be <= ").append(IolValidationConstants.PROJ_SPEED_HEAT_APDS_MAX).append("\n");
	    }
	    if (dto.getProjSpHefApds() == null || dto.getProjSpHefApds() > Double.parseDouble(IolValidationConstants.PROJ_SPEED_HEF_APDS_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The projectile speed time HEF-APDS should be <= ").append(IolValidationConstants.PROJ_SPEED_HEF_APDS_MAX).append("\n");
	    }
	    
	    if (dto.getRcTime() == null || dto.getRcTime() > Double.parseDouble(IolValidationConstants.RECHARGING_TIME_MAX.replace(" Sec", ""))) {
	        remarks.append(remarkCount++).append(". The recharging time should be <= ").append(IolValidationConstants.RECHARGING_TIME_MAX).append("\n");
	    }

	    if (!remarks.isEmpty()) {
	        deviceStatusDto.setStatus(IOLConstants.REJECTED);
	        deviceStatusDto.setRemarks(remarks.toString());
	    } else {
	        deviceStatusDto.setStatus(IOLConstants.ACCEPTED);
	        deviceStatusDto.setRemarks("Accepted");
	    }

	    return deviceStatusDto;
	}


	public String getColdTestByCreatedDate() {
		ColdTest coldTest = coldTestRepository.getColdTestByCreatedDate();
		ColdTestDto retDto = mapper.map(coldTest, ColdTestDto.class);
		return gson.toJson(retDto);
	}

	public DevDetDto getAllColdTestsDevDetails(int testTypeId, Date fromDate, Date toDate) {
		DevDetDto mainDto = new DevDetDto();
		try {
			List<Object[]> devDetails = coldTestRepository.getAllColdTestsDevDetailsByCreatedDate(testTypeId, fromDate,
					toDate);
			Object result = coldTestRepository.getTheAcceptOrRejectCount();

			if (result != null) {
				Object[] counts = (Object[]) result;
				BigInteger acceptedCount = (BigInteger) counts[0];
				BigInteger rejectedCount = (BigInteger) counts[1];
				BigInteger totCount = acceptedCount.add(rejectedCount);

				if (totCount.compareTo(BigInteger.ZERO) != 0) {
					double acceptedCountDouble = acceptedCount.doubleValue();
					double totCountDouble = totCount.doubleValue();
					double successRate = (acceptedCountDouble / totCountDouble) * 100.0;
					mainDto.setAcceptedCount(acceptedCount);
					mainDto.setRejectedCount(rejectedCount);
					mainDto.setSuccessRate(successRate);
				} else {
					mainDto.setAcceptedCount(BigInteger.ZERO);
					mainDto.setRejectedCount(BigInteger.ZERO);
					mainDto.setSuccessRate(0.0);
				}
			}

			if (devDetails != null && !devDetails.isEmpty()) {
				List<IolDataDto> iolDataList = new ArrayList<>();

				for (Object[] devDet : devDetails) {
					IolDataDto iolDataDto = new IolDataDto();
					if (devDet[0] != null)
						iolDataDto.setDevId((Integer) devDet[0]);
					if (devDet[1] != null)
						iolDataDto.setDevNo((String) devDet[1]);
					if (devDet[2] != null)
						iolDataDto.setStatus((Integer) devDet[2]);
					if (devDet[3] != null)
						iolDataDto.setRemarks((String) devDet[3]);
					if (devDet[4] != null)
						iolDataDto.setTestDate((String) devDet[4]);
					if (testTypeId == 0 || testTypeId == 1)
						iolDataDto.setTestTypeId(testTypeId);

					iolDataList.add(iolDataDto);
				}

				mainDto.setIoldata(iolDataList);
			} else {
				mainDto.setIoldata(new ArrayList<>());
			}
		} catch (Exception e) {
			logger.error("Failed to fetch device details: {}", e.getMessage());
		}
		return mainDto;
	}

}
