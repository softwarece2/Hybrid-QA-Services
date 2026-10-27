package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.entity.AteTestProtocol;


public interface AteTestProtocolRepository extends JpaRepository<AteTestProtocol, Integer> {

	@Query(value = "SELECT * FROM iol.ate_test_protocol where ate_test_id =:ateTest", nativeQuery = true)
	List<AteTestProtocol> findbyAteTestId(@Param("ateTest") int ateTest);

}
