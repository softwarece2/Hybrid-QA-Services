package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.QapReference;

/**
* Repository interface for QapReference entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-17 
*/

public interface QapReferenceRepository extends JpaRepository<QapReference, Integer>{
	
}
