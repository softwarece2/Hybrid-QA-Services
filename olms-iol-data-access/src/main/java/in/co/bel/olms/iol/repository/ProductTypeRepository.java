package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.iol.entity.ProductType;


@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

	@Query(value = "SELECT id as pr_id, product as pr FROM iol.product_type p WHERE p.id = :id", nativeQuery = true)
	ProductType findProductTypeById(@Param("id") Integer id);

	@Query(value = "SELECT * FROM iol.product_type ORDER BY id ASC", nativeQuery = true)
	List<Products> findAllProductTypes();

}
