package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.InspectionLevel;

/**
* Repository interface for InspectionLevel entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-04-12 
*/

public interface InspectionLevelRepository extends JpaRepository<InspectionLevel, Integer>{
	
}
