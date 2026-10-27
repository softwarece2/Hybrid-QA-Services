package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.DepartmentToProductMapping;

@Repository
public interface DepartmentProductMappingRepository extends JpaRepository<DepartmentToProductMapping, Integer> {
	 boolean existsByDepartmentIdAndProductId(Integer deparmentId,Integer productId);
	 boolean existsByDepartmentId(Integer departmentId);
	 boolean existsByProductId(Integer productId);
	  
	 @Query(value = "SELECT dpm.id,dpm.department_id,dpm.product_id,dept.department_name, p.prod_name "
//	 		+ ", pn.part_no, pn.material_description, pn.cat_part_no, pn.nsn_part_no \r\n"
	 		+ " FROM ate.department_product_mapping dpm \r\n"
//	 		+ " LEFT JOIN ate.partno_product_mapping ppm on ppm.product_id = dpm.product_id \r\n"
	 		+ " LEFT JOIN mdms.department dept on dpm.department_id = dept.id \r\n"
	 		+ " LEFT JOIN ate.product p on dpm.product_id = p.id \r\n"
//	 		+ " LEFT JOIN mdms.part_number pn  on  ppm.part_number_id = pn.id "
	 		, nativeQuery= true)
	 List<Object[]> getDeptProdAndEpCode();
}
