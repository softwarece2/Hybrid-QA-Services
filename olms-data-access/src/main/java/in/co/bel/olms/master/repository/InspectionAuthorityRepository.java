package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.InspectionAuthority;

/**
* Repository interface for InspectionAuthority entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

public interface InspectionAuthorityRepository extends JpaRepository<InspectionAuthority, Integer>{

}
