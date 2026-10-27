package in.co.bel.olms.master.repository.checklist.army;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQacl;

public interface ChecklistScrutinyQaclRepository extends JpaRepository<ChecklistScrutinyQacl, Integer>{

	@Query(value="select * from mdms.checklist_scrutiny_qacl where part=:partId",nativeQuery = true)
	List<ChecklistScrutinyQacl> findBypartId(int partId);

}
