package in.co.bel.olms.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.Status;


public interface StatusRepository  extends JpaRepository<Status, Integer> {

	Optional<Status> findByStatus(Integer created);

}
