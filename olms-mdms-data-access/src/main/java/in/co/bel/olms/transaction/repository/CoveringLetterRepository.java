package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.CoveringLetter;

public interface CoveringLetterRepository  extends JpaRepository<CoveringLetter, Integer> {
	@Query(value = "SELECT * FROM tdms.covering_letter WHERE file_note_id= :fnId", nativeQuery = true)
	CoveringLetter findByFileNoteId(@Param("fnId") int fnId);
}
