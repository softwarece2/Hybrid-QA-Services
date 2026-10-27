package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ColdTestDto {

	private Integer id;
	private Integer testType;
	private Integer hotCold;
	private String deviceNo;
	private String testDate;
	private Double volt;
	private Double phCurrR;
	private Double phCurrS;
	private Double phCurrT;
	private String ocularHeating;
	private String commander;
	private String autoMan;
	private Double rcTime;
	private String rcTimeBid;
	private Integer rcTimeDs;
	private Double workOffDTime;
	private String workOffDBid;
	private Integer workOffDDs;
	private String failureSingle;
	private String failureSingleBid;
	private String failureSingleDs;
	private Double mutliArresting;
	private Double singnalRotaryTrans;
	private String fireContact;
	private String fireContactBid;
	private String fireContactDs;
	private Double compensation;
	private String compMgFire;
	private Double voltSoftClockP1;
	private Double voltSoftAntiClockP1;
	private Double voltSoftClockP2;
	private Double voltSoftAntiClockP2;
	private Double voltHardClockP1;
	private Double voltHardAntiClockP1;
	private Double voltHardClockP2;
	private Double voltHardAntiClockP2;
	private Double voltZeroClockP1;
	private Double voltZeroAntiClockP1;
	private String safeContPt5v;
	private String safeContPt9v;
	private String blocking;
	private Double maxUpwCurrSpeed;
	private Double maxUpwCurrP1;
	private Double maxDownCurrSpeed;
	private Double maxDownCurrP2;
	private Double minAimSpeedDown;
	private Double minAimSpeedUp;
	private Integer evenessSightUp;
	private Integer evenessSightDown;
	private String zeroSetting;
	private Double mechSp9vForw;
	private Double mechSp9vBackw;
	private Double mechSp14vForw;
	private Double mechSp14vBackw;
	private Double mechSpPhCurrR;
	private Double mechSpPhCurrS;
	private Double mechSpPhCurrT;
	private Double driftSpeedPl0Deg;
	private Double driftSpeedMi5Deg;
	private Double driftSpeedPl15Deg;
	private String illumination;
	private Double projSpApdsHef;
	private Double projSpApdsHeat;
	private Double projSpHefHeat;
	private Double projSpHeatHef;
	private Double projSpHeatApds;
	private Double projSpHefApds;
	private Integer parallelism;
	private String torqHandwheel;
	private Integer applicableTestId;
    private Integer olLineItemId;
    private Integer sslipLineItemId;
    private String remarks;  
    private Integer status;
    private Date createdDatetime;
    private String submittedDate;
  //  private Integer productTypeId;
    
    
	private Integer approvedById;

	private Integer productId;
	
    private Integer devId;
	private Integer inspRoundId;
	
	private Integer testId;

}
