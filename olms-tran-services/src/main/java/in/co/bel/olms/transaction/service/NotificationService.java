package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.exception.OlmsException;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.entity.Notification;
import in.co.bel.olms.transaction.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

/**
 * The NotificationService contains the methods to perform operations on
 * notification table.
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 17-01-2023
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Notification getNotificationById(int id) {

		Optional<Notification> notificationOptional = notificationRepository.findById(id);
		if (notificationOptional.isPresent()) {
			return notificationOptional.get();
		} else {
			throw new DataNotFoundException("Notification not found by Id: " + id);
		}
	}

	public List<Notification> getNotifByCategory(String category, int empId) {

		List<Notification> notifications = new ArrayList<>();
		try {

			if (StringUtils.isNotBlank(category) && empId != 0) {
				notifications = notificationRepository.getNotifByCategory(category, empId);
			}
			return notifications;
		} catch (Exception e) {
			throw new OlmsException("No notifications found with given category:" + category);
		}
	}

	public List<Notification> getUnreadNotifications(int empId) {

		List<Notification> notifications = new ArrayList<>();
		try {
			notifications = notificationRepository.getUnreadNotifications(empId);
			return notifications;
		} catch (Exception e) {
			throw new OlmsException("Error in fetching notifications");
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}

	public boolean saveNotification(NotificationDto notificationDto) {

		try {
			Notification notification = mapper.map(notificationDto, Notification.class);
			notification.setCreatedDateTime(new Date());
			notification.setReadStatus(false);
			notification.setIsLatest(true);
			notificationRepository.save(notification);
			return true;
		} catch (Exception e) {
			logger.error("Error in saving notification {}", e.getMessage());
			return false;
		}
	}
	
	public boolean saveNotifications(List<NotificationDto> notificationDtos) {

		try {
			List<Notification> notifications = new ArrayList<>();
			Date todayDate = new Date();
			for (NotificationDto notificationDto : notificationDtos) {
				Notification notification = mapper.map(notificationDto, Notification.class);
				notification.setCreatedDateTime(todayDate);
				notification.setReadStatus(false);
				notification.setIsLatest(true);
				notifications.add(notification);
			}
			notificationRepository.saveAll(notifications);
			return true;
		} catch (Exception e) {
			logger.error("Error in saving notification{}", e.getMessage());
			return false;
		}
	}

	public boolean updateReadStatus(Integer notificationId, Integer readBy) {

		boolean status = false;
		try {
			Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
			if (notificationOptional.isPresent()) {
				Notification notification = notificationOptional.get();
				notification.setReadBy(readBy);
				notification.setReadStatus(true);
				notification.setReadTime(new Date());
				notificationRepository.save(notification);
				status = true;
			}
			return status;
		} catch (Exception e) {
			logger.error("Error in saving notification{}", e.getMessage());
			return false;
		}
	}
	
	public List<Notification> getSentUnreadNotifications(int empId) {

		List<Notification> notifications = new ArrayList<>();
		try {
			notifications = notificationRepository.getSentUnreadNotifications(empId);
			return notifications;
		} catch (Exception e) {
			throw new OlmsException("Error in fetching notifications");
		}
	}
	
	public List<Notification> getSenderNotificationByCategory(String category, int empId) {

		List<Notification> notifications = new ArrayList<>();
		try {

			if (StringUtils.isNotBlank(category) && empId != 0) {
				notifications = notificationRepository.getSenderNotificationByCategory(category, empId);
			}
			return notifications;
		} catch (Exception e) {
			throw new OlmsException("No notifications found with given category:" + category);
		}
	}

	public void updateIsLatest(List<String> categories, Integer empId) {
		try {
			notificationRepository.updateIsLatestToFalse(categories, empId);
		} catch (Exception e) {
			throw new OlmsException(e.getMessage());
		}
	}

	public Map<String, Object> getUnreadCategorySummary(Integer empId) {
        List<String> allCategories = notificationRepository.findAllExistingCategories();

        List<Object[]> unreadCounts = notificationRepository.countUnreadDistinctByCategory(empId);
        Map<String, Long> countMap = new HashMap<>();
        
        unreadCounts.forEach(row -> {
            String category = (String) row[0];
            // Cast to Number first, then convert to long
            Long count = (row[1] != null) ? ((Number) row[1]).longValue() : 0L;
            countMap.put(category, count);
        });

        List<String> categoriesWithLatest = notificationRepository.findCategoriesWithLatestUnread(empId);
        Set<String> latestSet = new HashSet<>(categoriesWithLatest);

        Map<String, Object> finalResponse = new HashMap<>();

        allCategories.forEach(category -> {
            Map<String, Object> details = new HashMap<>();
            long count = countMap.getOrDefault(category, 0L);
            details.put("count", count);
            details.put("hasLatest", latestSet.contains(category));
            finalResponse.put(category, details);
        });

        return finalResponse;
    }
	
}
