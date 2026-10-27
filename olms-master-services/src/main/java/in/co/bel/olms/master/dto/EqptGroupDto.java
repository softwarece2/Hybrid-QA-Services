package in.co.bel.olms.master.dto;

import java.util.List;

import lombok.Data;

@Data
public class EqptGroupDto {
	
	private int id;
	private String name;
    private List<EmpRoleDto> qaUsers;
	 private List<EmpRoleDto> qcUsers;
	 
	 private List<EmpRoleDto> users;
	 
	 

}
