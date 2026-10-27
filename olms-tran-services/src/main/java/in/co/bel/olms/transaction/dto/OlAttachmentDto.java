package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class OlAttachmentDto {
	private int id;
	private List<String> attachments;
	private List<String> preInspectionReports;
	private List<String> qap;
	private List<String> drawings;
	private List<String> ateAtp;
	private String createdDate;

}
