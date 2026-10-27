package in.co.bel.olms.ate.dto;

import lombok.Data;
import java.util.List;

@Data
public class PartnoProductMappingRequest {
    private Integer productId;
    private List<PartnoProductMappingDto> mapper;
    public List<PartnoProductMappingDto> getMapper() {
        return mapper;
    }

    public void setMapper(List<PartnoProductMappingDto> mapper) {
        this.mapper = mapper;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}

