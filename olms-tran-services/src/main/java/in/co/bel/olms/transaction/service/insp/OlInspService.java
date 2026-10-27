package in.co.bel.olms.transaction.service.insp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.InspectionAuthorityRepository;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.insp.AllotOlDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.insp.OlInspRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */

@Service
@RequiredArgsConstructor
public class OlInspService {

	private final OlInspRepository olInspRepository;

	private final OfferLetterService offerLetterService;

	private final InspectionAuthorityRepository inspectionAuthorityRepository;

	private final OfferLetterRepository offerLetterRepository;

	private final EmployeeDao employeeDao;

	private final Mapper mapper;

	Gson gson = new Gson();

	private final NotifyUtil notifyUtil;
	
	private static final Logger logger =LoggerFactory.getLogger(OlInspService.class);
	
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private AllotOlDto convertToAllotDto(OfferLetter offerLetter)
	{
		AllotOlDto olDto = mapper.map(offerLetter, AllotOlDto.class);
		if(offerLetter.getOfferDate()!=null)
		{
			olDto.setOfferDate( outputDateFormat.format (offerLetter.getOfferDate()) );
		}
		else
		{
			olDto.setOfferDate("");
		}
		return olDto;
	}
	
	
	/**
	 * Get the offering letters by inspector
	 * @author Manasa B 
	 * @param inspId
	 * @return
	 */
	public String getOlByInspId(Integer inspId) {

		List<OfferLetterDto> dtoList = new ArrayList<>();

		try {
		List<OfferLetter> offerLetters = olInspRepository.getOfferLetterByInspectorId(inspId);
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2); 

		if (!offerLetters.isEmpty()) {
			for (OfferLetter ol : offerLetters) {
				OfferLetterDto dto = mapper.map(ol, OfferLetterDto.class);
				SaleOrder saleOrder = ol.getSaleOrder();
				SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
				dto.setTotalItems(supplyOrder.getTotalQty());
				if(supplyOrder.getValue() != null) {
					dto.setTotalValue(supplyOrder.getValue());					
				}
				dto.setFileNo(supplyOrder.getFileNo());
				dto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
				dto.setName(ol.getOlNumber());
				dto.setSoNumber(supplyOrder.getOrderNo());
				if (ol.getSaleOrder().getCategory().getShip().getProject() != null)
				{
					dto.setProjectName(ol.getSaleOrder().getCategory().getShip().getProject().getProjectName());
				}
				dtoList.add(dto);
			}
		} else {
			throw new DataNotFoundException("OL not found by insp Id: " + inspId);
		}
		}
		catch (Exception e) {
			logger.error("Error in getting OlByInspId {}", e.getMessage());
		}
		return gson.toJson(dtoList);

	}

	
	/**
	 * 
	 * @param roleId
	 * @param empId
	 * @return
	 */
	public String getOlByRolenEmpId(Integer roleId, Integer empId) {

		List<AllotOlDto> dtoLsit = new ArrayList<>();
		List<OfferLetter> soList ;
		switch (roleId) {
		case ApplicationConstants.Role.INSPECTOR:
			soList = olInspRepository.getOfferLetterByInspectorId(empId);
		break;
		case ApplicationConstants.Role.ACQAO:
			 soList = olInspRepository.getOfferLetterByAcqaoId(empId);
			break;
		case ApplicationConstants.Role.DCQAO:
			soList = olInspRepository.getOfferLetterByDcqaoId(empId);
			break;
 
		case ApplicationConstants.Role.JCQAO:
			soList = olInspRepository.getOfferLetterByJcqaoId(empId);
			break;
		default:
			soList= new ArrayList<>();
			break;
		}

		
		for (OfferLetter ol : soList) {
			AllotOlDto dto =convertToAllotDto(ol);
			dtoLsit.add(dto);
		}
		Gson gsondate = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
		return gsondate.toJson(dtoLsit);

	}
	
	/**
	 * @author Manasa B
	 * @since 10-03-2023
	 * @param olDto
	 * @return
	 */
	public OfferLetterDto allotInsp(OfferLetterDto olDto) {

		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olDto.getId());

		Employee emp = employeeDao.getEmployee(olDto.getEmployeeId());
		offerLetter.setEmployee(emp);

		offerLetter = offerLetterRepository.save(offerLetter);

		olDto = mapper.map(offerLetter, OfferLetterDto.class);
		try {
			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(olDto.getEmployeeId());
			notifyUtil.notifyUser("QACL alloted to Inspector for the order no " + offerLetter.getOlNumber(), offerLetter.getSaleOrder().getSupplyOrder().getOrganization(), 1, offerLetter.getOlNumber(), 1, NotificationConstants.SO, receivers,offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
		} catch (Exception e) {
			logger.error("Notification error in Allot inspector for OL {} ", e.getMessage());
			logger.error("Error in Allot inspector for OL ", e);
		}
		return olDto;
	}

	/**
	 * @author Manasa B
	 * @since 10-03-2023
	 * @param olDto
	 * @return
	 */
	public AllotOlDto allotSecOfficer(OfferLetterDto olDto) {
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olDto.getId());
		Employee emp = employeeDao.getEmployee(olDto.getSecOfficerId());
		offerLetter.setSectionOfficer(emp);

		offerLetter = offerLetterRepository.save(offerLetter);

		AllotOlDto alDto = mapper.map(offerLetter, AllotOlDto.class);
		try {			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(olDto.getSecOfficerId());
			notifyUtil.notifyUser("QACL alloted to ACQAO for the order no " + offerLetter.getOlNumber(), offerLetter.getSaleOrder().getSupplyOrder().getOrganization(), 1, offerLetter.getOlNumber(), 1, NotificationConstants.SO, receivers,offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
		} catch (Exception e) {
			logger.error("Notification error in Allot ACQAO for OL {} ", e.getMessage());
			logger.error("Error inAllot ACQAO for OL ", e);
		}
		return alDto;
	}
	
	/**
	 * @author Manasa B
	 * @since 10-03-2023
	 * @param olDto
	 * @return
	 */
	public AllotOlDto allotDcqao(OfferLetterDto olDto) {
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olDto.getId());
		Employee emp = employeeDao.getEmployee(olDto.getDcqaoId());
		offerLetter.setDcqao(emp);;

		offerLetter = offerLetterRepository.save(offerLetter);

		AllotOlDto alDto = mapper.map(offerLetter, AllotOlDto.class);
		try {			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(olDto.getDcqaoId());
			notifyUtil.notifyUser("QACL alloted to DCQAO for the order no " + offerLetter.getOlNumber(), offerLetter.getSaleOrder().getSupplyOrder().getOrganization(), 1, offerLetter.getOlNumber(), 1, NotificationConstants.SO, receivers,offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
		} catch (Exception e) {
			logger.error("Notification error in Allot DCQAO  for QACL  {} ", e.getMessage());
			logger.error("Error in Allot DCQAO  for QACL ", e);
		}
		return alDto;
	}
	
	
	/**
	 * @author Manasa B
	 * @since 10-03-2023
	 * @param olDto
	 * @return
	 */
	public AllotOlDto allotJcqao(OfferLetterDto olDto) {
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olDto.getId());
		Employee emp = employeeDao.getEmployee(olDto.getJcqaoId());
		offerLetter.setJcqao(emp);;

		offerLetter = offerLetterRepository.save(offerLetter);

		AllotOlDto alDto = mapper.map(offerLetter, AllotOlDto.class);
		try {			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(olDto.getJcqaoId());
			notifyUtil.notifyUser("OL received for allotment at JCQAO for the order no " + offerLetter.getOlNumber(), offerLetter.getSaleOrder().getSupplyOrder().getOrganization(), 1, offerLetter.getOlNumber(), 1, NotificationConstants.SO, receivers,offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
		} catch (Exception e) {
			logger.error("Notification error in Allot JCQAO for OL {} ", e.getMessage());
			logger.error("Error in Allot JCQAO for OL  ", e);
		}
		return alDto;
	}
	
	
	public AllotOlDto allotSubordinate(AllotOlDto allotDto)
	{
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(allotDto.getId());
		Employee emp = employeeDao.getEmployee(allotDto.getSubordinateId());
		switch (emp.getEmpRole().getId()) {
		case ApplicationConstants.Role.INSPECTOR:
			offerLetter.setEmployee(emp);
		break;
		case ApplicationConstants.Role.ACQAO:
			offerLetter.setSectionOfficer(emp);
			break;
		case ApplicationConstants.Role.DCQAO:
			offerLetter.setDcqao(emp);
			break; 
		case ApplicationConstants.Role.JCQAO:
			offerLetter.setJcqao(emp);
			break;
		default:			
			break;
		}
		offerLetter = offerLetterRepository.save(offerLetter);
		try {			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(allotDto.getSubordinateId());
			notifyUtil.notifyUser("OL received for allotment for the order no " + offerLetter.getOlNumber(), offerLetter.getSaleOrder().getSupplyOrder().getOrganization(), 1, offerLetter.getOlNumber(), 1, NotificationConstants.OL, receivers,offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
		} catch (Exception e) {
			logger.error("Notification error in Allot subordiante for OL {} ",  e.getMessage());
			logger.error("Error in Allot subordiante for OL ", e);
		}
		return new AllotOlDto();
	}
	
}