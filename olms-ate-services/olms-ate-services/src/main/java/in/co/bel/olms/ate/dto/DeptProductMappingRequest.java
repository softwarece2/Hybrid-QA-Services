package in.co.bel.olms.ate.dto;
import lombok.Data;
import java.util.List;

@Data

public class DeptProductMappingRequest {

    private List<DepartmentToProductMappingDto> mapper;
    private Integer productId;

    // Getters and setters
    public List<DepartmentToProductMappingDto> getMapper() {
        return mapper;
    }

    public void setMapper(List<DepartmentToProductMappingDto> mapper) {
        this.mapper = mapper;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}