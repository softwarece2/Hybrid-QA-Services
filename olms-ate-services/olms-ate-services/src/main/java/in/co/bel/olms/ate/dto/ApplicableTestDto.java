package in.co.bel.olms.ate.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ApplicableTestDto {
	private int id;
    private Integer olLineItemId;
    private Integer applicableTestId;
	private String testName;
	private String remarks;
    private String createdBy;
    private Boolean isAte;
    private Boolean isRmt;
    private Map<String, String> firmAttachmentURLs;
    private Map<String, String> inspectorAttachmentURLs;
    private List<Integer> attachmentIds;
    private String isAteStr;
    private String isRmtStr;
}



