package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class VisualTestDto {
	private Integer id;
	private Integer outwardAppear;
	private Double viewHoriz;
	private Double viewVerti;
	private Double magnification;
	private Double diffMagLr;
	private Double resolvingPwrL;
	private Double resolvingPwrR;
	private Double diopterAdjL;
	private Double diopterAdjR;
	private Double diopterDiff;
	private Integer parallelHorizDegL;
	private Integer parallelHorizMinL;
	private Integer parallelHorizDegR;
	private Integer parallelHorizMinR;
	private Double parallelVertiL;
	private Double parallelVertiR;
	private Integer cleanOptical;
	private Integer movementIrisDiaphragm;
	private String range;
	private Date submittedDate;
	private Integer tvneId;
	private Integer visualInspStageId;
}
