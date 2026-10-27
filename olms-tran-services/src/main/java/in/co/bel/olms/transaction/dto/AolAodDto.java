package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;
@Data
public class AolAodDto {
	private Integer id;
	private Integer aolEmpId;
	private Integer asnEmpId;
	private Integer createdBy;
	private Date fromDate;
	private Date toDate;
	private Date createdDatetime;
	private String asnDesg;
	private String type;
	private String asnEmpName;
	private String asnEmpRole;
	private Boolean active;

}
