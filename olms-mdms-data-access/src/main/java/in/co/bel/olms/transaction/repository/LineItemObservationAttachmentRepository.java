package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.transaction.entity.LineItemObservationAttachment;

public interface LineItemObservationAttachmentRepository extends JpaRepository<LineItemObservationAttachment, Integer> {

}
