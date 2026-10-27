package in.co.bel.olms.master.dto;

import lombok.Data;

@Data
public class EmpRoleDto {
	
	private int id;
	private int seniorityId;
	private String name;
	private boolean isAdmin;
	
	private String nameAndStaffNo;

}
