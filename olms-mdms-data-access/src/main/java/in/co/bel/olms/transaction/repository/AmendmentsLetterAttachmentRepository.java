package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.co.bel.olms.transaction.entity.AmendmentsLetterAttachment;

public interface AmendmentsLetterAttachmentRepository extends JpaRepository<AmendmentsLetterAttachment, Integer> {
   
}