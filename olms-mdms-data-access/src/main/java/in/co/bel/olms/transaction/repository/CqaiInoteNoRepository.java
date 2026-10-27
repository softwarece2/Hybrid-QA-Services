package in.co.bel.olms.transaction.repository;

import in.co.bel.olms.transaction.entity.CqaiInoteNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CqaiInoteNoRepository extends JpaRepository<CqaiInoteNo, Integer> {

  @Query("SELECT MAX(c.counter) FROM CqaiInoteNo c WHERE c.iNoteNo LIKE %:fySuffix%")
  Integer findMaxCounterByFinancialYear(@Param("fySuffix") String fySuffix);

}