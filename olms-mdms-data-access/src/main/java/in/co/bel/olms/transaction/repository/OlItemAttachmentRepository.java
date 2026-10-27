package in.co.bel.olms.transaction.repository;

import in.co.bel.olms.transaction.entity.OlLineItemAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OlItemAttachmentRepository class for OlLineItemAttachment entity
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since   13-02-2024
 */

public interface OlItemAttachmentRepository extends JpaRepository<OlLineItemAttachment, Integer> {


}
