package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query(value = "select category_name from tdms.category", nativeQuery = true)
	List<String> findCategoryNames();
	
	Category findByCategoryName(String categoryName);

	List<Category> findBySaleOrderId(Integer soId);
	
	@Query(value="Select po.order_no from tdms.supply_order po \r\n"
			+ "left join tdms.so_line_item sli on sli.so_id=po.id\r\n"
			+ "left join tdms.category c on c.so_line_item_id=sli.id\r\n"
			+ "where c.id= :categoryId ", nativeQuery=true)
	String getSoNumberByCategoryId(int categoryId);
	
	boolean existsByCategoryName(String categoryName);
	
	@Modifying
	@Query(value = "DELETE FROM tdms.category  WHERE so_line_item_id =:lineItemId", nativeQuery = true)
	void deleteBySoLineitemId(int lineItemId);

	@Query(value = "select id from tdms.category  WHERE so_line_item_id =:lineItemId", nativeQuery = true)
	Integer getCategoryIdBySoLineitemId(int lineItemId);
	
	boolean existsByCategoryNameAndIdNot(String categoryName, Integer id);

}
