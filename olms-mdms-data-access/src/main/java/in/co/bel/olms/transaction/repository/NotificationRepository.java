package in.co.bel.olms.transaction.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	
	@Query(value = "SELECT * FROM TDMS.NOTIFICATION WHERE NOTIFICATION_CATEGORY LIKE '%' ||:notifCategory || '%' AND RECEIVER = :empId ORDER BY CREATED_DATETIME DESC", nativeQuery = true)
	List<Notification> getNotifByCategory(@Param("notifCategory") String notifCategory, @Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.NOTIFICATION WHERE READ_STATUS IS FALSE AND RECEIVER = :empId ORDER BY CREATED_DATETIME DESC", nativeQuery = true)
	List<Notification> getUnreadNotifications(@Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.NOTIFICATION WHERE NOTIFICATION_CATEGORY LIKE '%' || :notifCategory || '%' AND SENDER = :empId ORDER BY CREATED_DATETIME DESC", nativeQuery = true)
	List<Notification> getSenderNotificationByCategory(@Param("notifCategory") String notifCategory, @Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.NOTIFICATION WHERE READ_STATUS IS FALSE AND SENDER = :empId ORDER BY CREATED_DATETIME DESC", nativeQuery = true)
	List<Notification> getSentUnreadNotifications(@Param("empId") int empId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE TDMS.NOTIFICATION " +
       " SET is_latest = FALSE " +
       " WHERE notification_category IN (:notifCategories) " +
       " AND receiver = :empId AND is_latest = TRUE " , nativeQuery = true)
    void updateIsLatestToFalse(List<String> notifCategories, Integer empId);

	 @Query(value = "SELECT n.notification_category, COUNT(DISTINCT n.notification_name)"
			   + "                   FROM tdms.notification n WHERE n.receiver = :empId AND n.read_status = false"
			   + "                   GROUP BY n.notification_category" , nativeQuery = true)
     List<Object[]> countUnreadDistinctByCategory(@Param("empId") Integer empId);

     @Query(value = "SELECT DISTINCT n.notification_category FROM tdms.notification n "
     		+ "            WHERE n.receiver = :empId  AND n.read_status = false AND n.is_latest= true", nativeQuery = true)
     List<String> findCategoriesWithLatestUnread(@Param("empId") Integer empId);

     @Query(value = "SELECT DISTINCT n.notification_category FROM tdms.notification n",nativeQuery = true)
     List<String> findAllExistingCategories();
     
}
