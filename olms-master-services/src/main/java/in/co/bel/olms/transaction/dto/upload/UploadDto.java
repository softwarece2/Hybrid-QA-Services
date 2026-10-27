package in.co.bel.olms.transaction.dto.upload;

import lombok.Data;

/**
 * @author Manasa B
 * @since 20-10-22
 * Parameter required for the upload of the excel data
 */

@Data
public class UploadDto {

	private String fileName;
	private Integer lastUpdatedBy;
	private Integer supplyOrderId;
}
