package in.co.bel.olms.master.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Data;

/**
 * 
 * @author Manasa
 *
 */

@Data
public class EmployeeDto {
	
	private int id;
	private String ename;
	private String enameHi;
	private int roleId;
	private String roleName;
	private String roleNameHi;
	private String designation;
	private String designationHi;
    private Character gender;
    private String sealNo;
    private Integer organization;
    private Integer empRole;
    private String empDesignation;
    private Integer salesGroup;
    private Date  createdDateTime;
    private Integer createdBy;
    private Integer reportingOfficer;
    private Boolean status;
    private Integer departmentId;
    private String departmentName;
    private String orgName;
    private String staffNo;
    
    private Integer deptTypeId;
    private String deptTypeName;
    private List<Integer> deptIds;
    private Integer deptId;
    private String deptName;
    
    private String enameAndStaffNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDto that = (EmployeeDto) o;
        return id == that.id && Objects.equals(ename, that.ename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ename);
    }
}
