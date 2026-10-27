package in.co.bel.olms.transaction.repository; 

import org.springframework.data.jpa.repository.JpaRepository;
import in.co.bel.olms.transaction.entity.ItemLotsAttachment;

public interface ItemLotsAttachmentRepository extends JpaRepository<ItemLotsAttachment, Integer> {
    
}