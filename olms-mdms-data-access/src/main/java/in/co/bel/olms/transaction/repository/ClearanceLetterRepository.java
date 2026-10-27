package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.ClearanceLetter;


public interface ClearanceLetterRepository extends JpaRepository<ClearanceLetter, Integer>{
	
	@Query(value = "SELECT * FROM tdms.clearance_letter WHERE offer_letter_id= :olId", nativeQuery = true)
	ClearanceLetter findByOlId(@Param("olId") int olId);
	
	@Query(value = "SELECT id FROM tdms.clearance_letter WHERE offer_letter_id= :olId", nativeQuery = true)
	Integer getByOlId(@Param("olId") int olId);
	
	@Query(value = " SELECT SG.NAME AS NAME,"
			+ "\tO.ORGANIZATION_NAME AS SUPPLIER,"
			+ "\tO.ADDRESS,"
			+ "\tPO.FILE_NO,"
			+ "\tORG.ORGANIZATION_NAME AS CQAE_NAME "
			+ " FROM TDMS.OFFER_LETTER OL "
			+ " LEFT JOIN TDMS.SALE_ORDER SA ON OL.SALE_ORDER_ID = SA.ID "
			+ " LEFT JOIN TDMS.SUPPLY_ORDER PO ON SA.SUPPLY_ORDER_ID = PO.ID "
			+ " LEFT  JOIN MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID "
			+ " LEFT JOIN MDMS.ORGANIZATION O ON SG.ORGANIZATION_ID = O.ID "
			+ " LEFT JOIN MDMS.ORGANIZATION ORG ON PO.ORGANIZATION_ID = ORG.ID "
			+ " WHERE OL.ID = :olId ", nativeQuery = true)
	List<Object[] > getAddressByOlId(@Param("olId") int olId);

}