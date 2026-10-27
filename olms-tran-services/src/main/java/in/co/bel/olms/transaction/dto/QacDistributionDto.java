package in.co.bel.olms.transaction.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor  // IMPORTANT: Jackson needs this to create the object
@AllArgsConstructor // Good practice
public class QacDistributionDto {
    private Integer id;
    private List<String> distributionName; // Use <String> to match your array payload
    private Integer inoteId;
    private String createdAt;
}