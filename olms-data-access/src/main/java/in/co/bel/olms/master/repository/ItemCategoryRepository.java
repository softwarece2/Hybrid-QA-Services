package in.co.bel.olms.master.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.master.entity.ItemCategory;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Integer> {
	@Query(value = "SELECT * from mdms.item_category ORDER BY category ASC", nativeQuery = true)
	public List<ItemCategory> findAll();
	
	//List<ItemCategory> findAll(Sort.by(Sort.Direction.ASC, "category"));
	
	@Query(value = "SELECT * from mdms.item_category WHERE active = true", nativeQuery = true)
	public List<ItemCategory> findAllActiveItemCategory();
	
	
	Boolean existsBycategory(String category);

	public Boolean existsByCategoryAndIdNot(String category, int id);
}
