package in.co.bel.olms.iol.reports.dto;

import java.io.ByteArrayOutputStream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReportFileDto {

	ByteArrayOutputStream outputStream;
	String outputFileName;
}
