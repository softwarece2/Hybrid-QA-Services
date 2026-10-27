package in.co.bel.olms.transaction.service.so;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.FcmDto;
import in.co.bel.olms.transaction.dto.FcmVendorDto;
import in.co.bel.olms.transaction.entity.Fcm;
import in.co.bel.olms.transaction.entity.FcmVendor;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.fcm.FcmHelper;
import in.co.bel.olms.transaction.helper.fcm.SaveFcmHelper;
import in.co.bel.olms.transaction.helper.fcm.SubmitFcmHelper;
import in.co.bel.olms.transaction.helper.so.SupplyOrderHelper;
import in.co.bel.olms.transaction.repository.FcmRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 19-04-2023
 *
 */

@Service
@RequiredArgsConstructor
public class FcmService {

	private final  FcmRepository fcmRepository;

	private final  SupplyOrderService supplyOrderService;

	private final  Mapper mapper;

	private final SaveFcmHelper saveFcmHelper;

	private final SubmitFcmHelper submitFcmHelper;

	private final EntityValidator entityValidator;

	private final SupplyOrderHelper supplyOrderHelper;

	private final  NotifyUtil notifyUtil;

	private final FcmHelper fcmHelper;

	private final  EmployeeRepository employeeRepository;

	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(FcmService.class);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Fcm getFcmById(int id) {
		Optional<Fcm> fcmOptional = fcmRepository.findById(id);
		if (fcmOptional.isPresent()) {
			return fcmOptional.get();
		} else {
			throw new DataNotFoundException("FCM not found by Id: " + id);
		}
	}

	/**
	 * 
	 * 
	 * @param soId
	 * @return
	 */
	public FcmDto getFcmBySoId(int soId) {
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		Fcm fcm = fcmRepository.getFcmBySupplyOrder(supplyOrder);
		FcmDto fcmDto;
		if (fcm == null) {
			fcmDto = new FcmDto();
			fcmDto.setId(0);
			fcmDto= fcmHelper.getSoDetails(fcmDto, supplyOrder);
			fcmDto.setFcmVendor(new ArrayList<>());
		} else {
			fcmDto = convertBoToDto(fcm);
		}
		return fcmDto;
	}

	/**
	 * 
	 * @param fcm
	 * @return
	 */
	public FcmDto convertBoToDto(Fcm fcm) {

		FcmDto fcmDto;

		fcmDto = mapper.map(fcm, FcmDto.class);
		SupplyOrder supplyOrder = fcm.getSupplyOrder();

		if (fcm.getSubmittedDate() != null) {
			fcmDto.setSubmittedDate(outputDateFormat.format(fcm.getSubmittedDate()));	
			fcmDto.setSupplyOrderId(supplyOrder.getId());
			fcmDto.setSoDate(supplyOrder.getOrderDate().toString());
			if (supplyOrder.getReceiptDate() != null) {
				try {
					fcmDto.setDateOfReceipt( outputDateFormat.format(fcm.getDateOfReceipt()));
				} catch (Exception e) {
					fcmDto.setDateOfReceipt("");			
				}
			} else {
				fcmDto.setDateOfReceipt("");
			}			
		} else {
			fcmDto.setSubmittedDate(null);			
			fcmDto= fcmHelper.getSoDetails(fcmDto, supplyOrder);			
		}
		try {
			if (null != fcm.getFcmVendors()) {
				List<FcmVendorDto> vendorDtos = new ArrayList<>();
				for (FcmVendor vendor : fcm.getFcmVendors()) {
					FcmVendorDto vendorDto = mapper.map(vendor, FcmVendorDto.class);
					vendorDtos.add(vendorDto);
				}
				fcmDto.setFcmVendor(vendorDtos);
			}
		} catch (MappingException e) {
			logger.error("Get fcm vendors mapping error {} ", e.getMessage());
		} catch (NullPointerException e) {
			logger.error("Get fcm vendors null pointer exception {} ",  e.getMessage());
		}

		return fcmDto;
	}

	/**
	 * Save the draft FCM by inspector
	 * 
	 * @param fcmDto
	 * @return
	 */
	public Fcm saveFcm(FcmDto fcmDto) {
		Fcm fcm;

		if (fcmDto.getId() != 0) {
			Optional<Fcm> fmopt = fcmRepository.findById(fcmDto.getId());
			if (fmopt.isPresent()) {
				fcm = fmopt.get();
			} else {
				throw new DataNotFoundException("Save : FCM not found by  Id: " + fcmDto.getId());
			}
			
		} else {
			fcm = mapper.map(fcmDto, Fcm.class);
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fcmDto.getSupplyOrderId());
			fcm.setSupplyOrder(supplyOrder);
		}
		fcm = saveFcmHelper.prepareSaveFCMObj(fcmDto, fcm);
		boolean isvalid = entityValidator.isValidEntity(fcm);
		if (isvalid) {
			fcm = fcmRepository.save(fcm);
		} else {
			throw new DataNotValidException("FCM save:  " + fcm.toString());
		}

		return fcm;
	}

	/**
	 * 
	 * @param fcmDto
	 * @return
	 */
	public Fcm submitFcm(FcmDto fcmDto) {
		Fcm fcm;
		if (fcmDto.getId() != 0) {
			Optional<Fcm> fmopt = fcmRepository.findById(fcmDto.getId());
			if (fmopt.isPresent()) {
				fcm = fmopt.get();
			} else {
				throw new DataNotFoundException("Save : FCM not found by  Id: " + fcmDto.getId());
			}
			
		} else {
			fcm = mapper.map(fcmDto, Fcm.class);
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fcmDto.getSupplyOrderId());
			fcm.setSupplyOrder(supplyOrder);
			
			boolean isvalid = entityValidator.isValidEntity(fcm);
			if (isvalid) {
				fcm = fcmRepository.save(fcm);
			} else {
				throw new DataNotValidException("FCM save:  " + fcm.toString());
			}
		}
		
		fcm = submitFcmHelper.prepareSubmitFCMObj(fcmDto, fcm);
		fcm.setSubmittedDate(new Date());
		boolean isvalid = entityValidator.isValidEntity(fcm);
		if (isvalid) {
			fcm = fcmRepository.save(fcm);
		} else {
			throw new DataNotValidException("FCM save:  " + fcm.toString());
		}

		try {
			SupplyOrder supplyOrder = fcm.getSupplyOrder();
			Integer empId = employeeRepository.getEmpIdByolId(supplyOrder.getId());
			notifyUtil.NotifySBUnCqae("FCM is submitted for the SO no " + supplyOrder.getOrderNo(),
					supplyOrder.getId(), supplyOrder.getOrderNo(), 1, NotificationConstants.SO, supplyOrder.getSalesGroup().getId(), empId);
		} catch (Exception e) {
			logger.error("Notification error in FCM submitted for the order no {} ", e.getMessage());
			logger.error("Error occured while FCM submitted for the order no ", e);
		}
		
		
		return fcm;
	}

	

}
