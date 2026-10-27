package in.co.bel.olms.iol.dto;

import java.util.List;

import lombok.Data;

@Data
public class ColdTestUploadDto {
	private int id;
    private int acceptanceTestId;
    private List<ColdTestUploadMappingDto> coldTestUploadMappingDtos;

}
