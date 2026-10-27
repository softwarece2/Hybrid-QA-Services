package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.Indenter;

/**
* Repository interface for Indenter entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

public interface IndenterRepository extends JpaRepository<Indenter, Integer>{
	
	@Query(value= "SELECT * from mdms.indenter WHERE active = true", nativeQuery = true)
	public List<Indenter> findAllActiveIndenter();
	
	Boolean existsByname(String name);
	Boolean existsByshortName(String shortName);

}
