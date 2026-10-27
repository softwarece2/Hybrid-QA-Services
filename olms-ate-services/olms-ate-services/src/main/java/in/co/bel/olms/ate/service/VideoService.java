package in.co.bel.olms.ate.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.constants.ApplicationConstants;
import in.co.bel.olms.ate.constants.ApplicationConstants.VideoStatusConstants;
import in.co.bel.olms.ate.dto.VideoNotificationDto;
import in.co.bel.olms.ate.dto.VideoRecordsDto;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.ate.entity.VideoNotification;
import in.co.bel.olms.ate.entity.VideoNotifiedUsers;
import in.co.bel.olms.ate.entity.VideoRecords;
import in.co.bel.olms.ate.repository.TestRepository;
import in.co.bel.olms.ate.repository.VideoNotificationRepository;
import in.co.bel.olms.ate.repository.VideoRecordsRepository;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.VideoStatus;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.VideoStatusRepository;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoService {

	private final VideoRecordsRepository videoRecordRepo;

	private final OfferLetterRepository olRepo;

	private final OlLineItemRepository olLiRepo;

	private final TestRepository testRepo;

	private final OLLineItemSerialNoRepository olLiSerialNoRepo;

	private final EmployeeRepository empRepo;

	private final VideoNotificationRepository videoNotificationRepo;

	private final VideoStatusRepository videoStatusRepo;

    private final SubmissionSlipRepository submissionSlipRepository;

    private final SubSlipLineItemRepository sSlipLiRepo;

    private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final Mapper mapper;
	
	private static final String DEVICE_NO_NOT_FOUND = "Device/Serial No not found";
	private static final String STATUS_CONSTANT_ERRROR = "Status constant error";

    public List<VideoRecordsDto> getVideoRecords(Integer olId, Integer olLiId, Integer devId, Integer sSlipId, Integer sSlipLiId, Integer sSlipDevId, Integer testId,
			Integer empId) {

		// Validate all the ids
        validateIds(olId, olLiId, devId, sSlipId, sSlipLiId, sSlipDevId, testId, empId);

		// Dynamic Specification
		Specification<VideoRecords> spec = Specification.where(null);

		if (olId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("ol").get("id"), olId));
		}
		if (olLiId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("olLineItem").get("id"), olLiId));
		}
		if (devId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("dev").get("id"), devId));
		}
        if (sSlipId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sSlip").get("id"), sSlipId));
        }
        if (sSlipLiId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sSlipLineItem").get("id"), sSlipLiId));
        }
        if (sSlipDevId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("sSlipSerialNo").get("id"), sSlipDevId));
        }
		if (testId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("test").get("id"), testId));
		}
		if (empId != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("createdBy").get("id"), empId));
		}

		List<VideoRecords> recordings = videoRecordRepo.findAll(spec);

		return recordings.stream().map(recording -> mapper.map(recording, VideoRecordsDto.class)).collect(Collectors.toList());
	}

	@Transactional
	public String saveVideoRecord(VideoRecordsDto dto) {

	    VideoRecords record = new VideoRecords();

        if (dto.getPath() == null || dto.getPath().isEmpty()) {
            throw new IllegalArgumentException("Path is required");
        }

	    record.setPath(dto.getPath());

        if (dto.getIsOl()) {
            if (dto.getOlId() != null) {
                OfferLetter ol = olRepo.findById(dto.getOlId())
                        .orElseThrow(() -> new IllegalArgumentException("Offer Letter not found"));
                record.setOl(ol);
            } else {
                throw new IllegalArgumentException("Offer ID is required");
            }

            if (dto.getOlLiId() != null) {
                OlLineItem olLi = olLiRepo.findById(dto.getOlLiId())
                        .orElseThrow(() -> new IllegalArgumentException("OL Line Item not found"));

                if (!Objects.equals(olLi.getOfferLetter().getId(), dto.getOlId()))
                    throw new IllegalArgumentException("This OlLineItem " + dto.getOlLiId() + " does not belong to Ol: " + dto.getOlId());

                record.setOlLineItem(olLi);
            } else {
                throw new IllegalArgumentException("OL Line Item ID is required");
            }

            if (dto.getDevId() != null) {
                OLLineItemSerialNo dev = olLiSerialNoRepo.findById(dto.getDevId())
                        .orElseThrow(() -> new IllegalArgumentException(DEVICE_NO_NOT_FOUND));

                if (!Objects.equals(dev.getOlLineItem().getId(), dto.getOlLiId()))
                    throw new IllegalArgumentException("This Serial No " + dev.getSerialNo().getSerialNo() + " does not belong to OlLineItem: " + dto.getOlLiId());

                record.setDev(dev);
            } else {
                throw new IllegalArgumentException("Device ID is required");
            }
        } else {
            if (dto.getSSlipId() != null) {
                SubmissionSlip subSlip = submissionSlipRepository.findById(dto.getSSlipId())
                        .orElseThrow(() -> new IllegalArgumentException("Submission slip not found"));
                record.setSSlip(subSlip);
            } else {
                throw new IllegalArgumentException("Submission slip ID is required");
            }

            if (dto.getSSlipLiId() != null) {
                SubSlipLineItem sSlipLi = sSlipLiRepo.findById(dto.getSSlipLiId())
                        .orElseThrow(() -> new IllegalArgumentException("Submission Slip Line Item not found"));

                if (!Objects.equals(sSlipLi.getSubmissionSlip().getId(), dto.getSSlipId()))
                    throw new IllegalArgumentException("This Submission Slip LineItem " + dto.getSSlipLiId() + " does not belong to Submission slip: " + dto.getSSlipId());

                record.setSSlipLineItem(sSlipLi);
            } else {
                throw new IllegalArgumentException("Submission Slip Line Item ID is required");
            }

            if (dto.getSSlipDevId() != null) {
                SubSlipSerialNo subSlipDev = subSlipSerialNoRepository.findById(dto.getSSlipDevId())
                        .orElseThrow(() -> new IllegalArgumentException(DEVICE_NO_NOT_FOUND));

                if (!Objects.equals(subSlipDev.getSubSlipLineItem().getId(), dto.getSSlipLiId()))
                    throw new IllegalArgumentException("This Serial No " + subSlipDev.getWarrantDetails().getLineItemSerialNo().getSerialNo() + " does not belong to Sub Slip Line Item: " + dto.getSSlipLiId());

                record.setSSlipSerialNo(subSlipDev);
            } else {
                throw new IllegalArgumentException("Device ID is required");
            }
        }

	    if (dto.getTestId() != null) {
	        Test test = testRepo.findById(dto.getTestId())
	            .orElseThrow(() -> new IllegalArgumentException("Test not found"));
	        record.setTest(test);
	    } else {
	        throw new IllegalArgumentException("Test ID is required");
	    }

	    if (dto.getCreatedById() != null) {
	        Employee emp = empRepo.findById(dto.getCreatedById())
	            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
	        record.setCreatedBy(emp);
	    } else {
	        throw new IllegalArgumentException("Employee ID is required");
	    }

	    videoRecordRepo.save(record);

		return ApplicationConstants.SUCCESS;
	}

	public List<VideoNotificationDto> getAllVideoNotifications(Integer olId, Integer olLiId, Integer devId, Integer testId, Integer empId) {
	    
	    // 1. Validate IDs
        validateIds(olId, olLiId, devId, null, null, null, testId, empId);
	            
	    Specification<VideoNotification> spec = (root, query, cb) -> {
	        query.distinct(true);
	        root.fetch("notifiedUsers", JoinType.LEFT); 
	        return null;
	    };

	    if (olId != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("ol").get("id"), olId));
	    }
	    if (olLiId != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("olLineItem").get("id"), olLiId));
	    }
	    if (devId != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("dev").get("id"), devId));
	    }
	    if (testId != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("test").get("id"), testId));
	    }
	    if (empId != null) {
	        spec = spec.and((root, query, cb) -> cb.equal(root.get("requestedBy").get("id"), empId));
	    }

	    List<VideoNotification> notifications = videoNotificationRepo.findAll(spec);

	    return notifications.stream()
	            .sorted(Comparator.comparing(VideoNotification::getId).reversed())
	            .map(entity -> mapper.map(entity, VideoNotificationDto.class))
	            .collect(Collectors.toList());
	}

	public String saveVideoNotification(VideoNotificationDto dto) {

		VideoNotification videoNotification = new VideoNotification();
		
		if (dto.getOlId() != null) {
	        OfferLetter ol = olRepo.findById(dto.getOlId())
	            .orElseThrow(() -> new IllegalArgumentException("Offer Letter not found"));
	        videoNotification.setOl(ol);
	    } else {
	        throw new IllegalArgumentException("Offer ID is required");
	    }

        if (dto.getOlLiId() != null) {
            OlLineItem olLi = olLiRepo.findById(dto.getOlLiId())
                    .orElseThrow(() -> new IllegalArgumentException("OL Line Item not found"));

            if (!Objects.equals(olLi.getOfferLetter().getId(), dto.getOlId()))
                throw new IllegalArgumentException("This OlLineItem " + dto.getOlLiId() + " does not belong to Ol: " + dto.getOlId());

            videoNotification.setOlLineItem(olLi);
        } else {
            throw new IllegalArgumentException("OL Line Item ID is required");
        }

        if (dto.getDevId() != null) {
            OLLineItemSerialNo dev = olLiSerialNoRepo.findById(dto.getDevId())
                    .orElseThrow(() -> new IllegalArgumentException(DEVICE_NO_NOT_FOUND));

            if (!Objects.equals(dev.getOlLineItem().getId(), dto.getOlLiId()))
                throw new IllegalArgumentException("This Serial No " + dev.getSerialNo().getSerialNo() + " does not belong to OlLineItem: " + dto.getOlLiId());

            videoNotification.setDev(dev);
        } else {
            throw new IllegalArgumentException("Device ID is required");
        }

	    if (dto.getTestId() != null) {
	        Test test = testRepo.findById(dto.getTestId())
	            .orElseThrow(() -> new IllegalArgumentException("Test not found"));
	        videoNotification.setTest(test);
	    } else {
	        throw new IllegalArgumentException("Test ID is required");
	    }

	    if (dto.getRequestedById() != null) {
	        Employee emp = empRepo.findById(dto.getRequestedById())
	            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
	        videoNotification.setRequestedBy(emp);
	    } else {
	        throw new IllegalArgumentException("Employee ID is required");
	    }

    VideoStatus status = videoStatusRepo.findById(VideoStatusConstants.CREATED)
                .orElseThrow(() -> new IllegalArgumentException(STATUS_CONSTANT_ERRROR));
	    videoNotification.setStatus(status);
		
		List<VideoNotifiedUsers> notifiedUsers = dto.getNotifiedUserIds().stream().map(user -> {
			Employee emp = empRepo.findById(user)
					.orElseThrow(() -> new IllegalArgumentException("User not found: " + user));
			VideoNotifiedUsers notifiedUser = new VideoNotifiedUsers();
			notifiedUser.setStatus(status);
			notifiedUser.setNotifiedUser(emp);
			notifiedUser.setVideoNotification(videoNotification);
			return notifiedUser;
		}).collect(Collectors.toList());
		videoNotification.setNotifiedUsers(notifiedUsers);
		
		videoNotificationRepo.save(videoNotification);

		return "Video Notification saved successfully.";
	}

	public String updateVideoNotificationStatus(VideoNotificationDto dto) {

		VideoNotification videoNotification = videoNotificationRepo.findById(dto.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Record not found with ID: " + dto.getId()));

	    // Prevent updates if already accepted or closed
	    if ((videoNotification.getStatus().getId() == VideoStatusConstants.ACCEPTED)) 
	        throw new IllegalArgumentException("This video notification has already been finalized/accepted.");

	    if (dto.getApprovedById() != null) {
	        Employee approvedBy = empRepo.findById(dto.getApprovedById())
	                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + dto.getApprovedById()));
	        
	        if(dto.getStatusId() == VideoStatusConstants.ACCEPTED) videoNotification.setApprovedBy(approvedBy);
	        
	    } else {
	        throw new IllegalArgumentException("Approved by cannot be null");
	    }

	    List<VideoNotifiedUsers> allUsers = videoNotification.getNotifiedUsers();

	    VideoNotifiedUsers targetUserRecord = allUsers.stream()
	            .filter(u -> u.getNotifiedUser().getId().equals(dto.getApprovedById()))
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException("User is not part of this notification list"));

	    VideoStatus individualStatus = videoStatusRepo.findById(dto.getStatusId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid VideoStatus ID"));
	    
	    targetUserRecord.setStatus(individualStatus);

	    boolean anyoneAccepted = allUsers.stream()
	            .anyMatch(u -> u.getStatus().getId() == VideoStatusConstants.ACCEPTED);
	            
	    boolean allRejected = allUsers.stream()
	            .allMatch(u -> u.getStatus().getId() == VideoStatusConstants.REJECTED);

	    VideoStatus newOverallStatus;

	    if (anyoneAccepted) {
	        // if any one is accepted -> Overall becomes 2 (ACCEPTED)
	        newOverallStatus = videoStatusRepo.findById(VideoStatusConstants.ACCEPTED)
	                .orElseThrow(() -> new IllegalArgumentException(STATUS_CONSTANT_ERRROR));
	    } else if (allRejected) {
	        // if all have rejected -> Overall becomes 3 (REJECTED)
	        newOverallStatus = videoStatusRepo.findById(VideoStatusConstants.REJECTED)
	                .orElseThrow(() -> new IllegalArgumentException(STATUS_CONSTANT_ERRROR));
	    } else {
	        newOverallStatus = videoNotification.getStatus();
	    }

	    videoNotification.setStatus(newOverallStatus);

	    videoNotificationRepo.save(videoNotification);

	    return "Video Notification status updated successfully.";
	}

    private void validateIds(Integer olId, Integer olLiId, Integer devId, Integer sSlipId, Integer sSlipLiId, Integer sSlipDevId, Integer testId, Integer empId) {
		if (olId != null && !olRepo.existsById(olId))
			throw new IllegalArgumentException("Offer letter does not exists: " + olId);

		if (olLiId != null && !olLiRepo.existsById(olLiId))
			throw new IllegalArgumentException("OL line item does not exists: " + olLiId);

		if (devId != null && !olLiSerialNoRepo.existsById(devId))
			throw new IllegalArgumentException("OL line item serial no does not exists: " + devId);

        if (sSlipId != null && !submissionSlipRepository.existsById(sSlipId))
            throw new IllegalArgumentException("Submission Slip does not exists: " + sSlipId);

        if (sSlipLiId != null && !sSlipLiRepo.existsById(sSlipLiId))
            throw new IllegalArgumentException("SubSlip line item does not exists: " + sSlipLiId);

        if (sSlipDevId != null && !subSlipSerialNoRepository.existsById(sSlipDevId))
            throw new IllegalArgumentException("SubSlip line item serial no does not exists: " + sSlipDevId);

		if (testId != null && !testRepo.existsById(testId))
			throw new IllegalArgumentException("Test does not exists: " + testId);

		if (empId != null && !empRepo.existsById(empId))
			throw new IllegalArgumentException("Employee does not exists: " + empId);
	}
}
