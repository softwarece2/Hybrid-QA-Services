package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.Supplier;

/**
* Repository interface for Supplier entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
	@Query(value = "SELECT * from mdms.supplier WHERE active = true", nativeQuery = true)
	public List<Supplier> findAllActiveSuppliers();
	
	Boolean existsByname(String name);

	public Boolean existsByNameAndIdNot(String name, int id);
}
