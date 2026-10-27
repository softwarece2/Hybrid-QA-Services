package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

	@Query(value="Select * from ate.product where id > 100 order by id asc", nativeQuery = true)
	List<Products> findAll();

	boolean existsByProdName(String prodName); 
}
