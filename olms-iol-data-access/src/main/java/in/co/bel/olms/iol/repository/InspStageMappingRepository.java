package in.co.bel.olms.iol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.entity.InspStageMapping;


@Repository
public interface InspStageMappingRepository extends JpaRepository<InspStageMapping, Integer> {

}
