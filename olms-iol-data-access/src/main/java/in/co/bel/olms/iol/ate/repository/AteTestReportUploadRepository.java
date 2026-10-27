package in.co.bel.olms.iol.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.ate.entity.AteTestReportUpload;

public interface AteTestReportUploadRepository extends JpaRepository<AteTestReportUpload, Integer> {

	@Query(value = "Select ate from AteTestReportUpload ate where dev.id = :devId and prod.id = :prodId and test.id = :testId and spec.id = :specId and roundId = :roundId")
	AteTestReportUpload findByIds(Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId);
	
	@Query(value = "Select ate from AteTestReportUpload ate where ol_line_item_ser_no_id = :devId and prod.id = :prodId and test.id = :testId and spec.id = :specId and roundId = :roundId")
	AteTestReportUpload findByIdsInQa(Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId);

	@Query("SELECT a FROM AteTestReportUpload a WHERE a.spec.id = :specId " +
       "AND a.dev.id = :devId AND a.prod.id = :prodId AND a.test.id = :testId " +
       "AND a.roundId = :roundId ORDER BY a.id DESC")
    List<AteTestReportUpload> findAllBySpecIds(@Param("specId") Integer specId, 
        @Param("devId") Integer devId, @Param("prodId") Integer prodId, 
        @Param("testId") Integer testId, @Param("roundId") Integer roundId);
	
    @Query("SELECT ate FROM AteTestReportUpload ate " +
       "WHERE ate.dev.id = :devId AND ate.prod.id = :prodId " +
       "AND ate.test.id = :testId AND ate.roundId = :roundId " +
       "AND ate.spec.id = :specId ORDER BY ate.id DESC")
    List<AteTestReportUpload> findAllByIds(@Param("devId") Integer devId, 
        @Param("prodId") Integer prodId, @Param("testId") Integer testId, 
        @Param("roundId") Integer roundId, @Param("specId") Integer specId);

    @Query("SELECT ate FROM AteTestReportUpload ate " +
       "WHERE ate.ollisn.id = :devId AND ate.prod.id = :prodId " +
       "AND ate.test.id = :testId AND ate.roundId = :roundId " +
       "AND ate.spec.id = :specId ORDER BY ate.id DESC")
    List<AteTestReportUpload> findAllByIdsInQa(@Param("devId") Integer devId, 
        @Param("prodId") Integer prodId, @Param("testId") Integer testId, 
        @Param("roundId") Integer roundId, @Param("specId") Integer specId);
    
	@Query(value = "Select ate from AteTestReportUpload ate where dev.id = :devId and prod.id = :prodId and test.id = :testId and roundId = :roundId")
	List<AteTestReportUpload> findListOfUploadedReportByIds(Integer devId, Integer prodId, Integer testId, Integer roundId);
	
	// For QA Users
	@Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
	       "FROM AteTestReportUpload a WHERE a.fileHash = :hash " +
	       "AND a.ollisn.id = :devId AND a.prod.id = :prodId AND a.test.id = :testId " +
	       "AND a.roundId = :roundId AND a.spec.id = :specId")
	boolean existsByHashAndIdsInQa(String hash, Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId);

	// For Non-QA Users
	@Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
	       "FROM AteTestReportUpload a WHERE a.fileHash = :hash " +
	       "AND a.dev.id = :devId AND a.prod.id = :prodId AND a.test.id = :testId " +
	       "AND a.roundId = :roundId AND a.spec.id = :specId")
	boolean existsByHashAndIds(String hash, Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId);

}
