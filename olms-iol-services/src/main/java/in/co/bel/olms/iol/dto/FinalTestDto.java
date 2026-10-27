package in.co.bel.olms.iol.dto;

import lombok.Data;

@Data
public class FinalTestDto {
	private Double switching27V; // d
	private Integer lampBrOn;
	private String safeContactTest;
	private String gryoPhaseSeq;
	private String testFrequency;
	private String gyroDriveTest;
	private Double phasecurrR; // d
	private Double phasecurrS; // d
	private Double phasecurrT; // d
	private String phaseVoltIndicateLamp;
	private String indicationCommander; // d
	private Double projApdsHef; // d
	private Double projSpHefHeat; // d
	private Double projSpHeatApds; // d
	private String lampsBrHefHeaLighting;
	private Double projSpApdsHeat; // d
	private Double projSpHeatHef; // d
	private Double projSpHefApds; // d
	private String projectileHandDriveApds;
	private String lampDisSetting;
	private Double perTimeRangeFinder;
	private Double rmuAfterSwitch;
	private Double mechSpPhCurrR; // d
	private Double mechSpPhCurrS; // d
	private Double mechSpPhCurrT; // d
	

	private String testAutoOrManual;
	private Double workOffDistanceTime; // d
	private String workOffDBid; // d
	private Integer workOffDDs; // d
	private String testOfLampReadyD;
	private Double voltageOnPS;
	private Double voltageCWCenterP1;
	private Double voltageCWCenterP2;
	private Double voltageCWSoftP1; // d
	private Double voltageCWSoftP1StopP2;
	private Double voltageCWHardP1; // d
	private Double voltageCWSoftStopP2;
	private Double voltageACWCenterP1;
	private Double voltageACWCenterP2;
	private Double voltageACWSoftP1; // d
	private Double voltageACWSoftP1StopP2;
	private Double voltageACWHardP1; // d
	private Double voltageACWSoftStopP2;
	private String lightingOfH7H8H9H10;
	private Integer doubleTrigger20;
	private String rmuIndication;
	private Double rechargeTime; // d
	private Double pumpingVoltage;
	private String ocularHeating; // d
	private Double maxAimingSpeedUpward; // d
	private Double maxAimingSpeedDownward; // d
	private Double correctionCurrentUpward;
	private Double correctionCurrentDownward;
	private Double minAimingSpeedUpward; // d
	private Double minAimingSpeedDownward; // d
	private String jerkyChangeOfLineOfSight;
	private Integer evennessOfAimingNoOfCorrection1;
	private Integer evennessOfAimingNoOfCorrection2;
	private String lampUnarestON;
	private String lampStabilizerON;
	private String blocking; // d
	private String turnOfObjectImage;
	

	private Double currentID;
	private String lightingLampMechD;
	private Integer forwardMotion12VMin;
	private Integer forwardMotion12VMax;
	private Integer forwardMotion14VMin;
	private Integer forwardMotion14VMax;
	private Integer reverseMotion12VMin;
	private Integer reverseMotion12VMax;
	private Integer reverseMotion14VMin;
	private Integer reverseMotion14VMax;
	private Double forwardReverseMotionVoltage9V;
	private Double forwardReverseMotionVoltage3V;
	private String jerkyMotionOfProjectilesAPDS;
	private String jerkyMotionOfProjectilesHEF;
	private String jerkyMotionOfProjectilesHEAT;
	private String switchingZones40ON;
	private String switchingZones40OFF;
	private String switchingZones0ON;
	private String switchingZones0OFF;
	private Double adjRangeOfLineOfSightVerticalUP;
	private Double adjRangeOfLineOfSightVerticalDOWN;
	private Double adjRangeOfLineOfSightHorizontalLEFT;
	private Double adjRangeOfLineOfSightHorizontalRIGHT;
	private Double reticleTurning30;
	private Double rangeOfFOVPositionOfMAS;
	private Double horzRangeOAOB;
	private Double verticalRangeOCOD;
	private Double diffOfHorzVerticalFOV;
	private Double positionOfMAS;
	private Double quotient;
	private Double positionOfRingMark;
	private Double easinessOfMotionOfAdjustment;
	private Double horzDeviation;
	private Double signalChangeOfRotTrans;
	private Double slopeOfRotTransSignal;
	private Double humVoltageOfRotTrans;
	private Double phaseShiftOfRotTrans;
	private Double signalOfRotTrans;

	

	private String multipleArresting;
	private Double diffBetweenEndAndIntVal;
	private Double fireBlockingUpperVal;
	private Double fireBlockingLowerVal;
	private Double fireBlockingClosingAngle;
	private Double possOfMiddleAsToArrestPos;
	private Double compensationContactUpperVal;
	private Double compensationContactLowerVal;
	private Double compensationContactClosingAngle;
	private Double deviationArrestPosition;
	private Double deviationMiddleOfCompCont;
	private String testOfIndicationElementMG;
	private Integer noOfCorrectionSystem0_0;
	private Integer noOfCorrectionSystem0_12;
	private Integer noOfCorrectionSystem_5_12;
	private String jerkyVerticalChange;
	private Double driftSpeed0Lt1;
	private Double driftSpeed0Gt1;
	private Double driftSpeed5Lt1;
	private Double driftSpeed5Gt1;
	private Double driftSpeed15Lt1;
	private Double driftSpeed15Gt1;
	private Double resolvingPowerLt20;
	private Double sideDisplacement;
	private String reticleIllumination;
	private Integer fourHourProgram240TriggerIn4Hr;
	private String indOfBID;
	private String availabilityOfFireSpot;
	private Integer analysisOf4HrNoOfInd0;
	private Integer analysisOf4HrNoOfIndDiffFrom0And0000;
	private Double currentConsumption;
	private String switchingOffDevice;
}
