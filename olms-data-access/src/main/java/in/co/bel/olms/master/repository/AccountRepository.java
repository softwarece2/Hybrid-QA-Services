package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.Account;

/**
* AccountRepository JPA repository class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   08-06-2023 
*/

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
