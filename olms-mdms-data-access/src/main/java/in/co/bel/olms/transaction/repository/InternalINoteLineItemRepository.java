package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.transaction.entity.InternalINoteLineItem;

public interface InternalINoteLineItemRepository extends JpaRepository<InternalINoteLineItem, Integer> {

}
