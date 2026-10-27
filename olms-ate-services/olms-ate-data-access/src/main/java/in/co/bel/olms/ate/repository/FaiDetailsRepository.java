package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.FaiDetails;
@Repository
public interface FaiDetailsRepository extends JpaRepository<FaiDetails, Integer> {

	List<FaiDetails> findByFaiId(Integer id);


}
