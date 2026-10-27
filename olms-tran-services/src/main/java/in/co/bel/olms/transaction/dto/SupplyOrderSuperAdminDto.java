package in.co.bel.olms.transaction.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* SupplyOrderSuperAdminDto created to support the Super Admin's 
* 'Get All Supply Orders' functionality.
*
* @author  Chirag Swamy
* @since   30-09-2026
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyOrderSuperAdminDto {

    private Integer id;
    private Integer organizationId;
    private String orderNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;

    private Integer salesGroupId;
    private String salesGroupName;
    private String fileNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date receiptDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;

    private String status;
    private Boolean isNotified;
    private String remarks;
    private String organizationName;
    private String soStatusNo;
    private Integer soStatusId;
    private Boolean isActive;
    private Boolean isDel;

    // Format: dd-MM-yyyy hh:mm:ss a (30-09-2026 14:16:30 PM)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime delDatetime;

    private String inspectionAuthName;
}
