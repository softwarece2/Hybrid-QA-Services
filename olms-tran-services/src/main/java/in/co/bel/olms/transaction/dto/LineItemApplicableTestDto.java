package in.co.bel.olms.transaction.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LineItemApplicableTestDto {

	private Integer id;
	private Integer olLineItemId;

	private List<ApplicableTestDto> applicableTestDtos;
	private List<String> lineItemAttachments;
	private List<MultipartFile> multipartFiles;
	private String createdBy;

}
