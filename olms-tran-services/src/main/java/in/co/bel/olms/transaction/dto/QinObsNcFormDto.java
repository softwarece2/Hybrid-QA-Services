package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * QinObsNcFormDto DTO class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-03-27
 */

@Data
public class QinObsNcFormDto {

	private Integer id;
	private Integer offerLetterId;
	private Date createdDate;
	private Date modifiedDate;
	private Integer createdBy;
	private Integer modifiedBy;
	private Boolean isReworkSubmitted;
	private Boolean isQinClosed;
	private List<QinObsNcFormDetDto> qinObsNcFormDets;
}
