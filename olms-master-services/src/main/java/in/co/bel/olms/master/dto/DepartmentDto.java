package in.co.bel.olms.master.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
@EqualsAndHashCode
@Data
public class DepartmentDto {
    private Integer id;
    private String departmentName;
    private Boolean status;
    private Integer salesGrpId;
    private String salesGrpName;
    private Integer departmentTypeId;
    private String departmentType;
    private Integer sectionCode;
}
