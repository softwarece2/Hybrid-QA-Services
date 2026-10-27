package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.INoteType;


/**
* Repository interface for INoteType entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/
public interface INoteTypeRepository extends JpaRepository<INoteType, Integer>{
	
	List<INoteType> findByStatus(Boolean status);

}
