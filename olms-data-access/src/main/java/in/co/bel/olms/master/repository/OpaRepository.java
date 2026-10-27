package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.Opa;

public interface OpaRepository extends JpaRepository<Opa, Integer> {
	Boolean existsByopaName(String opaName);
	Boolean existsByopashortName(String opashortName);
}
