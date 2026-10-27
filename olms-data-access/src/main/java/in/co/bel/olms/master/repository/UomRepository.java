package in.co.bel.olms.master.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.Uom;

public interface UomRepository extends JpaRepository<Uom, Integer> {
	@Query(value = "SELECT * from mdms.uom WHERE active = true", nativeQuery = true)
	public List<Uom> findAllActiveUom();
	
	@Query(value = "SELECT * from mdms.uom ORDER BY unit ASC", nativeQuery = true)
	public List<Uom> findAll();
	
	Boolean existsByUnit(String unit);

	public Boolean existsByUnitAndIdNot(String unit, int id);
}
