package in.co.bel.olms.ate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import in.co.bel.olms.ate.entity.VideoNotification;

public interface VideoNotificationRepository extends JpaRepository<VideoNotification, Integer>, JpaSpecificationExecutor<VideoNotification>{

}