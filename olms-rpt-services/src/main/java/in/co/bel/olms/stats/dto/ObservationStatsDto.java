package in.co.bel.olms.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservationStatsDto {
    String type;
    Integer id;
    Integer passCount;
    Integer failCount;
}
