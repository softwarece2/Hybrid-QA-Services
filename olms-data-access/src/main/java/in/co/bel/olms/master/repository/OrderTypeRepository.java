package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.master.entity.OrderType;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {

}
