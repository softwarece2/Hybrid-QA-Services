package in.co.bel.olms.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.constants.ApplicationConstants.OrgType;
import in.co.bel.core.constants.ApplicationConstants.Role;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.PartNumberRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.entity.Notification;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.SubSlipService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.WarrantService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

/**
 * @author Awadhesh Verma
 * @since 17-01-23
 */

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	private final OfferLetterService olService;

	private final SupplyOrderService supplyOrderService;

	private final WarrantService warrantService;

	private final SubSlipService subSlipService;

	private final Mapper mapper;

	private final EmployeeRepository employeeRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final PartNumberRepository partNumberRepository;

	private final OfferLetterRepository offerLetterRepository;
	
	private static final String STATUS_READ = "Read";
	private static final String STATUS_UNREAD = "Unread";
	@Value("${bel.jwt.secret}")
	private String jwtSecret;

	private final HmisAppSecurity appSecurity = new HmisAppSecurity();

	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

	@PutMapping("/update")
	public ResponseEntity<Boolean> updateReadStatus(@RequestBody @Valid NotificationDto notificationDto) {
		Boolean savedStatus = notificationService.updateReadStatus(notificationDto.getId(),
				notificationDto.getReadBy());
		if (savedStatus.booleanValue()) {
			return new ResponseEntity<>(savedStatus, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(savedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/unreadNotifications/{empId}")
	public ResponseEntity<List<NotificationDto>> getUnreadNotifications(@PathVariable int empId) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		List<Notification> notifications = notificationService.getUnreadNotifications(empId);
		for (Notification notification : notifications) {
			NotificationDto notificationDto = mapper.map(notification, NotificationDto.class);
			notificationDto.setCreatedDateTime(dateFormat.format(notification.getCreatedDateTime()));
			notificationDto.setReadStatusString(notification.isReadStatus() ? "Read" : STATUS_UNREAD);
			notificationDto.setSbuName(notification.getSbuName());
			notificationDtos.add(notificationDto);
		}
		return ResponseEntity.ok(notificationDtos);
	}

	@GetMapping("/notifByCategory/{category}/{empId}")
	public ResponseEntity<List<NotificationDto>> getNotifByCategory(@PathVariable String category,
			@PathVariable int empId) {
		Employee emp = employeeRepository.findById(empId)
				.orElseThrow(() -> new IllegalArgumentException("Employee not found for id: " + empId));
		Boolean isQa = emp.getOrganization().getOrganizationType().getId() == OrgType.ID_CQAE;
		List<NotificationDto> notificationDtos = new ArrayList<>();
		List<Notification> notifications = notificationService.getNotifByCategory(category, empId);
		for (Notification notification : notifications) {
			NotificationDto notificationDto = mapper.map(notification, NotificationDto.class);
			if (notification.getNotificationCategory().equals(NotificationConstants.OL)
					|| notification.getNotificationCategory().equals(NotificationConstants.OL_ALLOTMENT)) {
				OfferLetter ol = offerLetterRepository.findById(notification.getCategoryId()).orElse(null);
				if (ol != null && ol.getLot() != null) {
					notificationDto.setMergeSubSlipId(ol.getLot().getId());
				}
			}
			String partN = null;
			if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_OL)) {
				String soNumber = isQa ? olService.getSoNumberByOlId(notification.getCategoryId())
						: subSlipService.getSoNumberBySubSlipId(notification.getCategoryId());
				if (soNumber != null) {
					notificationDto.setSoNumber(soNumber);
//					partN = supplyOrderService.getPartNumberBySoNumber(soNumber);
					List<Object[]> data = isQa ? supplyOrderService.getPartNumberByOlId(notification.getCategoryId()) :supplyOrderService.getPartNumberBySubSlipId(notification.getCategoryId());					
//					String inspAuth = supplyOrderRepository.getInspAuthBySoNumber(soNumber);
					String inspAuth = null;
					for(Object[] obj: data) {
						partN = obj[0]!=null? obj[0].toString(): null;
						inspAuth = obj[1]!=null ? String.valueOf(obj[1]) : null;
					}
//					String inspAuth = supplyOrderRepository.getInspAuthBySoNumber(soNumber);
					notificationDto.setInspAgency(inspAuth);
					notificationDto.setEpcode(partN);
				}
			} else if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_SO)) {
//				String soNumber = supplyOrderService.getSoNumber(notification.getCategoryId());
				SupplyOrder so = supplyOrderRepository.findById(notification.getCategoryId()).orElse(null);
				if (so != null) {
					notificationDto.setSoNumber(so.getOrderNo());
					List<String> inpAuth = supplyOrderRepository.getInspAuthBySoNumber(so.getOrderNo());
					notificationDto.setInspAgency(inpAuth != null ? String.join(" & ", inpAuth) : "");
					partN = supplyOrderService.getPartNumberBySoNumber(so.getOrderNo());
					notificationDto.setEpcode(partN);
				}
			} else if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_WT)) {
				String soNumber = warrantService.getSoNumberByWarrantId(notification.getCategoryId());
				notificationDto.setSoNumber(soNumber);
				if (soNumber != null) {
//					partN = supplyOrderService.getPartNumberBySoNumber(soNumber);
					partN = supplyOrderService.getPartNumberByWarrantId(notification.getCategoryId());
					List<String> inpAuth = supplyOrderRepository.getInspAuthBySoNumber(soNumber);
					notificationDto.setInspAgency(inpAuth != null ? String.join(" & ", inpAuth) : "");
					notificationDto.setEpcode(partN);
				}
			} else {
				notificationDto.setSoNumber(null);
			}
			// notificationDto.setInspAgency(notification.get)
			notificationDto.setSbuName(notification.getSbuName());
			notificationDto.setCreatedDateTime(dateFormat.format(notification.getCreatedDateTime()));
			notificationDto.setReadStatusString(notification.isReadStatus() ? "Read" : STATUS_UNREAD);
//			List<SOLineItem> soLineList=soLineItemService.getSoLineItemsBySoId(notification.getCategoryId());
//			if(!soLineList.isEmpty())
//			{
//	          partN=soLineList.get(0).getPartNumber().getPartNo();
//			}
//			notificationDto.setEpcode(partN);
			notificationDtos.add(notificationDto);
		}
		return ResponseEntity.ok(notificationDtos);
	}

	@PostMapping("/notificationRead/{notificationId}/{empId}")
	public ResponseEntity<Boolean> setNotificationRead(@PathVariable Integer notificationId,
			@PathVariable Integer empId) {
		Boolean notifications = notificationService.updateReadStatus(notificationId, empId);
		return ResponseEntity.ok(notifications);
	}

	@GetMapping("/senderNotificationsByCategory/{category}/{empId}")
	public ResponseEntity<List<NotificationDto>> getSenderNotificationByCategory(
			@RequestHeader("Authorization") String token, @PathVariable String category, @PathVariable int empId) {
		try {

			Employee emp = employeeRepository.findById(empId)
					.orElseThrow(() -> new IllegalArgumentException("Employee not found for id: " + empId));

			Boolean isQa = emp.getOrganization().getOrganizationType().getId() == OrgType.ID_CQAE;
			Integer empRoleId = emp.getEmpRole().getId();

			Integer loginEmpId = appSecurity.getEmpIdFromJwtToken(token);

			List<NotificationDto> notificationDtos = new ArrayList<>();
			List<Notification> notifications = notificationService.getSenderNotificationByCategory(category, empId);
			for (Notification notification : notifications) {
				NotificationDto notificationDto = mapper.map(notification, NotificationDto.class);

				if (notification.getNotificationCategory().equals(NotificationConstants.OL)
						|| notification.getNotificationCategory().equals(NotificationConstants.OL_ALLOTMENT)) {
					OfferLetter ol = offerLetterRepository.findById(notification.getCategoryId()).orElse(null);
					if (ol != null && ol.getLot() != null) {
						notificationDto.setMergeSubSlipId(ol.getLot().getId());
					}
				}
				String partN = null;
				if (empId == loginEmpId) {
					notificationDto.setNotificationCategory(notificationDto.getNotificationCategory() + "Sender");
				}
				if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_OL)) {
//				String soNumber = olService.getSoNumberByOlId(notification.getCategoryId());
//				String soNumber = subSlipService.getSoNumberBySubSlipId(notification.getCategoryId());

					String soNumber = isQa || empRoleId == Role.DO
							? olService.getSoNumberByOlId(notification.getCategoryId())
							: subSlipService.getSoNumberBySubSlipId(notification.getCategoryId());

					if (soNumber != null) {

						notificationDto.setSoNumber(soNumber);
//						partN = supplyOrderService.getPartNumberBySoNumber(soNumber);
						List<Object[]> data = isQa ? supplyOrderService.getPartNumberByOlId(notification.getCategoryId()) :supplyOrderService.getPartNumberBySubSlipId(notification.getCategoryId());					
//						String inspAuth = supplyOrderRepository.getInspAuthBySoNumber(soNumber);
						String inspAuth = null;
						for(Object[] obj: data) {
							partN = obj[0]!=null? obj[0].toString(): null;
							inspAuth = obj[1]!=null ? String.valueOf(obj[1]): null;
						}
						notificationDto.setInspAgency(inspAuth);
						notificationDto.setEpcode(partN);

					}
				} else if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_SO)) {
//				String soNumber = supplyOrderService.getSoNumber(notification.getCategoryId());
					SupplyOrder so = supplyOrderRepository.findById(notification.getCategoryId()).orElse(null);
					if (so != null) {
						notificationDto.setSoNumber(so.getOrderNo());
						partN = supplyOrderService.getPartNumberBySoNumber(so.getOrderNo());
						List<String> inpAuth = supplyOrderRepository.getInspAuthBySoNumber(so.getOrderNo());
						notificationDto.setInspAgency(inpAuth != null ? String.join(" & ", inpAuth) : "");
						notificationDto.setEpcode(partN);
						String matDescr = partNumberRepository.findMaterialDescriptionByPartNumberId(partN);
						notificationDto.setMaterialDescription(matDescr);
					}

				} else if (category.equalsIgnoreCase(ApplicationConstants.LOCATION_WT)) {
					String soNumber = warrantService.getSoNumberByWarrantId(notification.getCategoryId());
					notificationDto.setSoNumber(soNumber);
					if (soNumber != null) {
//						partN = supplyOrderService.getPartNumberBySoNumber(soNumber);
						partN = supplyOrderService.getPartNumberByWarrantId(notification.getCategoryId());
						notificationDto.setEpcode(partN);
						List<String> inpAuth = supplyOrderRepository.getInspAuthBySoNumber(soNumber);
						notificationDto.setInspAgency(inpAuth != null ? String.join(" & ", inpAuth) : "");
					}
				}
				notificationDto.setSbuName(notification.getSbuName());
				notificationDto.setCreatedDateTime(dateFormat.format(notification.getCreatedDateTime()));
				notificationDto.setReadStatusString(notification.isReadStatus() ? "Read" : STATUS_UNREAD);
				notificationDtos.add(notificationDto);
			}
			return ResponseEntity.ok(notificationDtos);
		} catch (Exception e) {
			logger.error("error in retrieving notifications : ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/sentUnreadNotifications/{empId}")
	public ResponseEntity<List<NotificationDto>> getSentUnreadNotifications(
			@RequestHeader("Authorization") String token, @PathVariable int empId) {

		String jwtToken = token.substring(7);
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
		int loginEmpId = claims.get("empId", Integer.class);

		List<NotificationDto> notificationDtos = new ArrayList<>();
		List<Notification> notifications = notificationService.getSentUnreadNotifications(empId);
		for (Notification notification : notifications) {
			NotificationDto notificationDto = mapper.map(notification, NotificationDto.class);
			if (empId == loginEmpId) {
				notificationDto.setNotificationCategory(notificationDto.getNotificationCategory() + "Sender");
			}
			notificationDto.setCreatedDateTime(dateFormat.format(notification.getCreatedDateTime()));
			notificationDto.setReadStatusString(notification.isReadStatus() ? "Read" : STATUS_UNREAD);
			notificationDto.setSbuName(notification.getSbuName());
			notificationDtos.add(notificationDto);
		}
		return ResponseEntity.ok(notificationDtos);
	}

	@PostMapping("/updateLatest")
	public ResponseEntity<String> updateLatest(@RequestBody NotificationDto reqDto,
			@RequestHeader("Authorization") String token) {
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			List<String> categories = reqDto.getCategories();
			if (categories == null || categories.isEmpty())
				return ResponseEntity.badRequest().body("Notification categories cannot be empty.");

			notificationService.updateIsLatest(categories, empId);
			return ResponseEntity.ok(ApplicationConstants.SUCCESS);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/categoryWiseCount")
	public ResponseEntity<Map<String, Object>> getCategoryWiseCount(@RequestHeader("Authorization") String token) {
		try {
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Map<String, Object> response = notificationService.getUnreadCategorySummary(empId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("error in retrieving notification count: ", e);
			return ResponseEntity.internalServerError().body(Map.of());
		}
	}

}
