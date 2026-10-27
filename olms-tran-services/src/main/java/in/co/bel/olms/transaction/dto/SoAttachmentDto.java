package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class SoAttachmentDto {

	
	private List<SoAttachmentDetailDto> qapFiles;
	private List<SoAttachmentDetailDto> drawings;
	private List<SoAttachmentDetailDto> atpFiles;
	private List<SoAttachmentDetailDto> attachments;
	private List<SoAttachmentDetailDto> amendments;
	private List<SoAttachmentDetailDto> poFiles;
	private List<SoAttachmentDetailDto> ddFiles;
	private List<SoAttachmentDetailDto> itemLotFiles;
}
