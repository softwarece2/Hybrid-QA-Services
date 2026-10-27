package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class InpectorDto {

	int completed;
	int pending;
	int inProgress;
	int total;
}
