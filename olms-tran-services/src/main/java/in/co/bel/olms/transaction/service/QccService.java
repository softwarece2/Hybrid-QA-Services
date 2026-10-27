package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.core.constants.MasterConstants.EMP_ROLE;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.QCC_STATUS;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.QccStatus;
import in.co.bel.olms.master.repository.ConsigneeRepository;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.QccDto;
import in.co.bel.olms.transaction.entity.INote;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.MergeSubSlip;
import in.co.bel.olms.transaction.entity.MergeSubSlipMapping;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.Qcc;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.InternalINoteRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipMappingRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.QccRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QccService {

	private final QccRepository qccRepo;

	private final EmployeeRepository empRepo;

	private final InternalINoteRepository internalINoteRepo;

	private final ConsigneeRepository consigneeRepo;

	private final OfferLetterRepository offerLetterRepository;

	private final MergeSubSlipMappingRepository mergeSubSlipMappingRepository;

	private final Mapper mapper;

	private final SupplyOrderRepository supplyOrderRepository;
	
	private final INoteRepository iNoteRepository;
	
	private final MergeSubSlipService mergeSubSlipService;
  
	private final MergeSubSlipRepository mergeSubSlipRepository;
  	
	private final DepartmentRepository departmentRepository;
  	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	private final NotificationService notificationService;
		
	private final WSService wsService;
		
	private static final Logger logger = LoggerFactory.getLogger(QccService.class);
		
	@Transactional
	public String saveQcc(QccDto qccDto, Integer empId, Integer roleId) {

		Employee emp = empRepo.findById(empId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));		
		
		MergeSubSlip mergeSubSlip = mergeSubSlipRepository.findById(qccDto.getMergedSubSlipId())
				.orElseThrow(() -> new IllegalArgumentException("Merge Sub Slip Not Found"));
		Qcc qcc;
		if (qccDto.getId() != null) {
			qcc = qccRepo.findById(qccDto.getId())
					.orElseThrow(() -> new IllegalArgumentException("QCC not found for Id: " + qccDto.getId()));
		} else {
			qcc = new Qcc();
			qcc.setCreatedBy(emp);
			QccStatus status = new QccStatus();
			status.setId(QCC_STATUS.GENERATED);
			qcc.setQccStatus(status);
			mergeSubSlipService.updateMergeSubSLipStatus(qccDto.getMergedSubSlipId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.QCC_GENERATED);
		}
		try {
			mapper.map(qccDto, qcc);		
			qcc= qccRepo.save(qcc);	
			return "QCC data saved succesfully";
		} catch(Exception e) {
			logger.error("Notification error in QCC Generation ", e);
			throw new RuntimeException("Failed to save QCC", e);
		}

//		try {	
//			List<Employee> empdoqclist = empRepo.getAllEmpsByRoleIdAndDeptId(MasterConstants.EMP_ROLE.ID_DO, in.co.bel.core.constants.ApplicationConstants.Dept.ASSEMBLY);
//
//     		for(Employee doa: empdoqclist) { 
//     			
//			//Add Notification for QCC Generated. 
//			NotificationDto notificationDto = new NotificationDto();
//			notificationDto.setSenderId(empId);
//			notificationDto.setReceiverId(doa.getId());
//			notificationDto.setNotificationCategory(NotificationConstants.QCC);
//			notificationDto.setCategoryId(qcc.getId());
//			notificationDto.setSbuName(null);
//			List<NotificationDto> notificationDtoList = new ArrayList<>();
//			
//			notificationDtoList.add(notificationDto);
//			
//			String msg = "QCC for Lot " + mergeSubSlip.getMergeOlNo()+ " is Generated by " + emp.getEname() + " Pending for Approval";
//						
//			for(NotificationDto dto : notificationDtoList) {
//				dto.setNotificationName(msg);
//			}
//			List<Integer> receivers = new ArrayList<>();
//			
//			receivers.add(doa.getId());					
//			notificationService.saveNotifications(notificationDtoList);
//			MessageBean message = new MessageBean();
//			message.setSender(1);
//			message.setMessageType(NotificationConstants.QCC);
//			message.setMessage(msg);
//			message.setItemNo(qcc.getMergedSubSlip().getMergeOlNo());
//			message.setReceivers(receivers);
//			wsService.notifyCqae(message);
//			
//     		}
//		} catch (Exception e) {
//			logger.error("Notification error in QCC Generation {} ", e.getMessage());
//			e.printStackTrace();
//		}
		
		
	}

	@Transactional
	public String approveQcc(Integer qccId, Integer empId, Integer roleId) {

		Employee emp = empRepo.findById(empId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
	
		Date now = new Date();

		if (qccId == null)
			throw new IllegalArgumentException("QCC id cannot be null");

		Qcc qcc = qccRepo.findById(qccId)
				.orElseThrow(() -> new IllegalArgumentException("QCC not found for Id: " + qccId));
		
		
		Optional<MergeSubSlip> mergeSubSlip = mergeSubSlipRepository.findById(qcc.getMergedSubSlip().getId());
		
		Optional<Department> prodApprovedDept = departmentRepository.findById(mergeSubSlip.get().getDepartment().getId());

		QccStatus status = new QccStatus();

		if (roleId == EMP_ROLE.ID_DO && emp.getDeptType().getId() == in.co.bel.core.constants.ApplicationConstants.Dept.ASSEMBLY) { // DO A (for Prod)
			qcc.setProdApprovedBy(emp);
			qcc.setProdApprovedAt(now);
			qcc.setProdApprovedByDept(prodApprovedDept.get().getDepartmentName());
			status.setId(QCC_STATUS.DOA_APPROVED);
			qcc.setQccStatus(status);

             mergeSubSlipService.updateMergeSubSLipStatus(qcc.getMergedSubSlip().getId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.DOA_APPROVED);
             
             try {	
            	 
            	//get code list of employees in qc dept and role = do
         		List<Employee> empdoqclist = empRepo.getAllEmpsByRoleIdAndDeptId(MasterConstants.EMP_ROLE.ID_DO, in.co.bel.core.constants.ApplicationConstants.Dept.QC);

         		for(Employee doQc: empdoqclist) {         			
         			//Notification to DO-QC for Approval. 
         			NotificationDto notificationDto = new NotificationDto();
         			notificationDto.setSenderId(empId);
         			notificationDto.setReceiverId(doQc.getId());
         			notificationDto.setNotificationCategory(NotificationConstants.QCC);
         			notificationDto.setCategoryId(qcc.getId());
         			notificationDto.setSbuName(null);
         			List<NotificationDto> notificationDtoList = new ArrayList<>();

         			notificationDtoList.add(notificationDto);
         			
         			String msg = "QCC for Lot " + qcc.getMergedSubSlip().getMergeOlNo()+ " is Approved by " + emp.getEname() + " Pending for QC Approval";
         						
         			for(NotificationDto dto : notificationDtoList) {
         				dto.setNotificationName(msg);
         			}
         			List<Integer> receivers = new ArrayList<>();         			
         			receivers.add(doQc.getId());         					
         			notificationService.saveNotifications(notificationDtoList);
         			MessageBean message = new MessageBean();
         			message.setSender(1);
         			message.setMessageType(NotificationConstants.QCC);
         			message.setMessage(msg);
         			message.setItemNo(qcc.getMergedSubSlip().getMergeOlNo());
         			message.setReceivers(receivers);
         			wsService.notifyCqae(message);
         		}         		   	         		
         		
     		} catch (Exception e) {
     			logger.error("Notification error in QCC Generation {} ", e.getMessage());
     			logger.error("Error during QCC notification ", e);
     		}
             
		} else if (roleId == EMP_ROLE.ID_DO && emp.getDeptType().getId() == in.co.bel.core.constants.ApplicationConstants.Dept.QC ) { // DO QC (for Insp)
			if (qcc.getProdApprovedBy() == null || !qcc.getQccStatus().getId().equals(QCC_STATUS.DOA_APPROVED))
				throw new IllegalArgumentException("DOA approval is pending for this QCC");

			qcc.setQcApprovedBy(emp);
			qcc.setQcApprovedAt(now);
//			qcc.setQcApprovedByDept(emp.getDepartment().getDepartmentName());
			//need to update

			status.setId(QCC_STATUS.DO_QC_APPROVED);
			qcc.setQccStatus(status);
            mergeSubSlipService.updateMergeSubSLipStatus(qcc.getMergedSubSlip().getId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.DOQC_APPROVED);
		} else {
			throw new IllegalArgumentException("Only DO_A and DO_QC users are allowed to approve the QCC");
		}

		qccRepo.save(qcc);	
		
		return "QCC data approved succesfully";
	}

	public QccDto getQcc(Integer mergedSubSlipId, Integer empId, Integer roleId, Boolean isOl) {
		QccDto qccDto = new QccDto();
		try {
			Qcc qcc = qccRepo.findByMergedSubSlipId(mergedSubSlipId).orElse(null);

			OfferLetter existingOl = offerLetterRepository.findByLotId(mergedSubSlipId);

			InternalINote intINote = internalINoteRepo.findByMergeSubSlipId(mergedSubSlipId).orElse(null);

			if (qcc != null) {
				qccDto = mapper.map(qcc, QccDto.class);
				if (qcc.getInoteId() == null) {
					Qcc updateQcc = mapper.map(qccDto, Qcc.class);
					updateQcc.setId(qcc.getId());
					qccRepo.save(updateQcc);
				}

				if (intINote != null) {
					qccDto.setInoteDate(sdf.format(intINote.getSubmittedDatetime()));
					qccDto.setInoteNo(intINote.getINoteNo());
					qccDto.setInoteId(intINote.getId());
				}

				qccDto.setIfdNo(qccRepo.findIfdByMergeSSlipId(mergedSubSlipId));
				String fY = getFinancialYearStrForQcc();
				List<MergeSubSlipMapping> mergeSubSlipMapping = mergeSubSlipMappingRepository
						.findByMergeOlNoIdId(qcc.getMergedSubSlip().getId());
				
				List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(mergeSubSlipMapping.get(0).getSubSlip().getId());
				String secName = null;
				String secCode = null;
				Integer deptId = null;
				if(obj!=null) {
					for(Object[] det: obj) {
						deptId = det[0]!=null? (Integer)det[0]: null;
						secName = det[1]!=null? det[1].toString(): null;
						secCode = det[2]!=null? det[2].toString(): null;
					}
					
				}
				
				qccDto.setSection(secName);
				
		Integer count = offerLetterRepository.getOlCountRows(deptId);		
		 count = (count == null)? 0 : count;       
        String olCount = String.format("%05d", count + 1);
      
        String olNo = "OL" + secCode + fY + olCount;
        qccDto.setOlNo(olNo);

				SimpleDateFormat istDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				if (existingOl != null) {
					qccDto.setOlNo(existingOl.getOlNumber());
					qccDto.setOfferDate(istDateFormat.format(existingOl.getOfferDate()));
					qccDto.setOlRemarks(existingOl.getRemarks());
					INote existingInote = iNoteRepository.findByOlId(existingOl.getId());
					if(existingInote != null) {
						qccDto.setExternalInoteId(existingInote.getId());
						qccDto.setExternalInoteNo(existingInote.getINoteNo());
						qccDto.setExternalInoteDate(sdf.format(existingInote.getInoteDate()) );
					}
//					qccDto.setExternalInoteId(inote.);
				} else {
					Date currentDate = new Date();
					String formattedDate = istDateFormat.format(currentDate);
					qccDto.setOfferDate(formattedDate);
				}

				List<Object[]> data = qccRepo.findSoLineItemDetailsFromQccMergeSslipId(mergedSubSlipId);
				for (Object[] det : data) {
					qccDto.setCatPartNo(det[0] != null ? det[0].toString() : null);
					qccDto.setNsnPartNo(det[1] != null ? det[1].toString() : null);
					qccDto.setItemCategory(det[2] != null ? det[2].toString() : null);
					qccDto.setRegistrationNo(det[3] !=null ? (Integer)det[3]: null);
					qccDto.setIfdDate(det[4] != null ? det[4].toString() : null);
					qccDto.setEpCode(det[5]!=null? det[5].toString(): null);
				}

				SupplyOrder so = supplyOrderRepository.findByOrderNo(qccDto.getSoNo());
				qccDto.setSoDate(sdf.format(so.getOrderDate()));
//				qccDto.setSection(offerLetterRepository.getDepartSecCode(olId).getSectionCode().toString(););

			} else {
				List<Object[]> qccMergeSslipData = qccRepo.findQccByMergeSSlipId(mergedSubSlipId);

				if (qccMergeSslipData != null && !qccMergeSslipData.isEmpty()) {
					Object[] result = qccMergeSslipData.get(0);
					qccDto.setWarrantNo(result[0] != null ? result[0].toString() : "");
					qccDto.setNomenclature(result[1] != null ? result[1].toString() : "");
					qccDto.setDrawingNo(result[2] != null ? result[2].toString() : "");
					qccDto.setSwoNo(result[3] != null ? result[3].toString() : "");
					if(result[12]!=null && result[12]== ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
						qccDto.setQtyTendered(result[13]!=null? new BigDecimal(result[13].toString()): BigDecimal.ZERO);
					}
					else {
						qccDto.setQtyTendered(result[4] != null ? new BigDecimal(result[4].toString()) : BigDecimal.ZERO);	
					}
			
					qccDto.setSoNo(result[5] != null ? result[5].toString() : "");
					qccDto.setRegNo(result[6] != null ? result[6].toString() : "");
					qccDto.setIfdNo(result[7] != null ? result[7].toString() : "");
					qccDto.setSoDate(result[8] != null ? result[8].toString() : "");
					qccDto.setCatPartNo(result[9] != null ? (String) result[9] : null);
					qccDto.setNsnPartNo(result[10] != null ? (String) result[10] : null);
					qccDto.setItemCategory(result[11] != null ? (String) result[11] : null);
					// qccDto.setRegistrationNo(result[12]!=null ? (Integer)result[12]: null); 
					// need to be verified
                    qccDto.setIfdDate(result[12] != null ? (String) result[12] : null); 
                    qccDto.setEpCode(result[13]!= null ? (String)result[13]: null);
                    qccDto.setRegistrationNo(result[14]!=null? Integer.parseInt(result[14].toString()): null);
				}
				List<MergeSubSlipMapping> mergeSubSlipMapping = mergeSubSlipMappingRepository
						.findByMergeOlNoIdId(mergedSubSlipId);
				
				List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(mergeSubSlipMapping.get(0).getSubSlip().getId());
//				Object[] obj = offerLetterRepository.getDepartSecCodeBySubSlipId(id);
//				System.out.println("Length = " + obj.length);
//				for (int i = 0; i < obj.length; i++) {
//				    System.out.println(i + " -> " + obj[i]);
//				}
				
				String secName = null;
				if(obj!=null) {
					for(Object[] det: obj)
					secName = det[1]!=null? det[1].toString(): null;
				}
				
				qccDto.setSection(secName);
			}

			if (isOl != null && isOl) {
//			List<LineItemSerialNo> regs = lineItemSerialNoRepo.findPassedRegsByMergedSSlipId(mergedSubSlipId);
//			String regNames = regs.stream().map(reg -> reg.getSerialNo()).collect(Collectors.joining(", "));
//			qccDto.setRegNo(regNames);
			}

			List<Consignee> consignees = new ArrayList<>();
			if (existingOl != null) {
//			List<OLLineItemSerialNo> serialNos = olLineItemSerialNoRepo.findRegNosByOlId(existingOl.getId());
//			String regNames = serialNos.stream().map(reg -> reg.getSerialNo().getSerialNo()).collect(Collectors.joining(", "));
//			qccDto.setRegNo(regNames);
				qccDto.setConsignee(existingOl.getConsignee().getName());
				qccDto.setIsOlGenerated(true);
			} else {
				List<Object[]> consigneeData = consigneeRepo.getConsigneeIdsAndNames(mergedSubSlipId);

				if (consigneeData != null && !consigneeData.isEmpty()) {
					List<Map<String, Object>> consigneeMaps = consigneeData.stream().map(row -> {
						Map<String, Object> map = new HashMap<>();
						map.put("id", row[0]);
						map.put("name", row[1]);
						return map;
					}).toList();

					qccDto.setConsignees(consigneeMaps);
					qccDto.setIsOlGenerated(false);
				}
			}

			return qccDto;
		} catch (Exception e) {
			logger.error("Error occured while fetching QCC {}", e.getMessage());
			throw new RuntimeException("Error fetching QCC", e);
		}
	}

	private static String getFinancialYearStrForQcc() {
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		Month month = currentDate.getMonth();

		// If the current month is from January to March, the financial year belongs to
		// the previous calendar year.
		if (month.getValue() >= 1 && month.getValue() <= 3) {
			year = year - 1;
		}

		// The financial year is represented by the last two digits of the starting
		// year.
		return String.valueOf(year % 100);
	}
}
