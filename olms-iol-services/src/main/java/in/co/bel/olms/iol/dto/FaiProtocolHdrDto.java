package in.co.bel.olms.iol.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FaiProtocolHdrDto {

	private Integer noOfDevice;
	private List<String> deviceName = new ArrayList<>();
	private List<FaiProtocolDto> faiDtos = new ArrayList<>();
}
