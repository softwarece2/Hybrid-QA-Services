package in.co.bel.olms.transaction.repository;

import in.co.bel.olms.transaction.entity.FagEndNotice;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FagEndNoticeRepository extends JpaRepository<FagEndNotice, Integer> {

	@Query(value = "FROM FagEndNotice WHERE supplyOrder = :supplyOrder AND offerLetter = :offerLetter")
	FagEndNotice getFagEndNoticeBySupplyOrderAndOfferLetter(@Param("supplyOrder") SupplyOrder supplyOrder, @Param("offerLetter") OfferLetter offerLetter);

	@Query(value = "SELECT * FROM tdms.fag_end_notice WHERE offer_letter_id = :olid ",  nativeQuery = true)
	FagEndNotice findFagEndNoticeByOlid( @Param("olid") int olid);
	
	@Query(value = "SELECT * FROM tdms.offer_letter ol WHERE ol.id = :olid ",  nativeQuery = true)
	FagEndNotice getFagEndNoticeByOlid( @Param("olid") int olid);
	
	@Query(value="SELECT * FROM tdms.fag_end_notice fn where fn.offer_letter_id =:olid ",nativeQuery = true)
	public FagEndNotice fetchFagEndNoticeByOlId(@Param("olid")int olid);

	@Query(value = "SELECT * FROM tdms.fag_end_notice WHERE offer_letter_id = :olid", nativeQuery = true)
	Optional<FagEndNotice> findByOlid(@Param("olid") int olid);


	
}
