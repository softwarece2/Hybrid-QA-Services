package in.co.bel.olms.transaction.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * OlLineItemAttachmentDto class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since   13-02-2024
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OlLineItemAttachmentDto {

	private Integer id;
	private Integer lineItemApplicableTestId;
	private Map<Integer, String> applicableTestsMap;
	private List<ApplicableTestDto> applicableTestDtos;
	private String createdBy;

}
