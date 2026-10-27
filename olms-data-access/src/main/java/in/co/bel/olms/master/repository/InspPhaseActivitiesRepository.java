package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.InspPhaseActivities;

public interface InspPhaseActivitiesRepository extends JpaRepository<InspPhaseActivities, Integer> {
	
	@Query(value="SELECT * FROM mdms.insp_phase_activities WHERE insp_phase_id = :inspPhaseId", nativeQuery=true)
	List<InspPhaseActivities> getAllInspPhaseActivitiesByInspPhaseId(int inspPhaseId);
}
