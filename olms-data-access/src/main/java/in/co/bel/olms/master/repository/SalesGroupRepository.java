package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.SalesGroup;

/**
* Repository interface for SalesGroup entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

public interface SalesGroupRepository extends JpaRepository<SalesGroup, Integer>{
	
	Boolean existsByname(String name);
	
	List<SalesGroup> findByActive(Boolean active);
	
	
	@Query(value="select sg.id, sg.name from mdms.sales_group sg where  sg.active = true ", nativeQuery=true)
	List<Object[]> findByStatus();
	
	 @Query(value="select sg.name from mdms.sales_group sg where  sg.id = :sbuId", nativeQuery=true)
     String getSbuName( @Param("sbuId") Integer sbuId);
	    boolean existsByNameAndIdNot(String name, Integer id);	    
	    boolean existsByCodeAndIdNot(String code, Integer id);
}
