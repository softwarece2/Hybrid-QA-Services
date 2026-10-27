package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.transaction.entity.VendorRating;

public interface VendorRatingRepository extends JpaRepository<VendorRating, Integer> {

}
