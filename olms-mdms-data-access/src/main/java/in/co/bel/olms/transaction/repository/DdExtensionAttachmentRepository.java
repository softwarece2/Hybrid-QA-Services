package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.co.bel.olms.transaction.entity.DdExtensionAttachment;

public interface DdExtensionAttachmentRepository extends JpaRepository<DdExtensionAttachment, Integer> {
}