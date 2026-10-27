package in.co.bel.olms.transaction.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.core.constants.MasterConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.WarrantCertificateDto;
import in.co.bel.olms.transaction.entity.INoteLineItem;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.WarrantCertificate;
import in.co.bel.olms.transaction.repository.INoteLineItemRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.WarrantCertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarrantCertificateService {

	private final WarrantCertificateRepository warrantCertificateRepository;
	
	private final INoteLineItemRepository iNoteLineItemRepository;
	
	private final MergeSubSlipRepository mergeSubSlipRepository;

	private final Mapper mapper;

	DateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private final EmployeeRepository employeeRepository;

	public List<WarrantCertificateDto> getAllWarrantCertificate() {
		List<WarrantCertificate> warrantCertificates = warrantCertificateRepository.findAll();

        List<WarrantCertificateDto> warrantCertificateDtos = new ArrayList<>();
        for (WarrantCertificate warrantCertificate : warrantCertificates) {
            warrantCertificateDtos.add(mapper.map(warrantCertificate, WarrantCertificateDto.class));
        }
        return warrantCertificateDtos;

	}

    public String saveWarrantCertificate(@Valid WarrantCertificateDto warrantCertificateDto) {
    	
    	try {
    		Employee authority = employeeRepository.findById(warrantCertificateDto.getAuthorityId())
    				.orElseThrow(() -> new IllegalArgumentException(
    						"Employee not found with ID: " + warrantCertificateDto.getAuthorityId()));

    		WarrantCertificate warrantCertificate = mapper.map(warrantCertificateDto, WarrantCertificate.class);

            warrantCertificate.setInoteId(warrantCertificateDto.getInoteId());
    		warrantCertificate.setAuthority(authority);

            int warrantType = warrantCertificateDto.getWarrantType();
            
// modified by: Shridhar 
// warrant Type and Qty mapped in dozer            
//            if (warrantType == 4) {
//                warrantCertificate.setStartDate(warrantCertificateDto.getStartDate());
//                warrantCertificate.setEndDate(warrantCertificateDto.getEndDate());
//            } else {
//                LocalDate today = LocalDate.now();
//                warrantCertificate.setStartDate(today);
    //
//                LocalDate endDate;
//                switch (warrantType) {
//                    case 1: // 1 Year
//                        endDate = today.plusYears(1);
//                        break;
//                    case 2: // 2 Year
//                        endDate = today.plusYears(2);
//                        break;
//                    case 3: // 18 Months
//                        endDate = today.plusMonths(18);
//                        break;
//                    default:
//                        endDate = today.plusYears(1);
//                        break;
//                }
//                warrantCertificate.setEndDate(endDate);
//            }

            warrantCertificateRepository.save(warrantCertificate);
            return "Warrant Certificate saved successfully";  		
    	}catch (IllegalArgumentException e) {
            throw e; 
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new RuntimeException("Database Error: The data provided violates database constraints.");
        } catch (Exception e) {
            throw new RuntimeException("Internal Server Error: " + e.getMessage());
        }

    }
	
	public List<WarrantCertificateDto> getWarrantDetailsByInoteId(Integer inoteId) {
		List<Object[]> response = warrantCertificateRepository.getWarrantDetailsByInoteId(inoteId);

		List<WarrantCertificateDto> warrantCertificateDtos = new ArrayList<>();
		
		INoteLineItem iNoteLineItem = iNoteLineItemRepository.getByINoteId(inoteId);
		
		//Modified By: Chirag , Date: 10-10-26
		// Extract serial numbers
	    String passedSerialNos = iNoteLineItem.getOlLineItemSerialNos().stream()
	            .filter(reg -> !reg.getSerialNo().getStatus().getStatus()
	                .equalsIgnoreCase(in.co.bel.olms.constants.ApplicationConstants.REGISTRATION_CONSTANTS.RFR))
	            .sorted(Comparator.comparing(reg -> reg.getSerialNo().getId()))
	            .map(reg -> reg.getSerialNo().getSerialNo()) 
	            .collect(Collectors.joining(", "));

		for (Object[] item : response) {
			WarrantCertificateDto dto = new WarrantCertificateDto();
			dto.setItemCode(item[0] != null ? item[0].toString() : "");
			dto.setNomenclature(item[1] != null ? item[1].toString() : "");
			dto.setDrawingNo(item[2] != null ? item[2].toString() : "");
			dto.setContractNo(item[3] != null ? item[3].toString() : "");
			dto.setInoteId(Integer.parseInt(item[4].toString()));
			dto.setInoteNo(item[5] != null ? item[5].toString() : "");
			dto.setQty(Integer.parseInt(item[6].toString()));
			dto.setRegNos1(passedSerialNos);
			dto.setInoteDate(item[8] != null ? item[8].toString() : "");
            dto.setStartDate(item[9] != null ? (LocalDate) item[9] : null);
            dto.setEndDate(item[10] != null ? (LocalDate) item[10] : null);
			dto.setApprovalDate(item[11] != null ? item[11].toString() : "");
			dto.setWarrantTypeName(item[12] != null ? item[12].toString() : "");
			dto.setAuthorityName(item[13] != null ? item[13].toString() : "");
            dto.setIsDoaApproved(item[14] != null ? (Boolean) item[14] : false);
            dto.setWarrantType(item[15] != null ? (Integer) item[15] : null);
            dto.setId(item[16] != null ? Integer.parseInt(item[16].toString()) : null);
            dto.setDoaApproverId(item[17] != null ? Integer.parseInt(item[17].toString()) : null);
            dto.setContractDate(item[18] !=null ? inputDateFormat.format((Date)item[18]): null);
            dto.setWarrantyPeriodQty(item[19]!=null ? (Integer)item[19]: null); //removed warrPeriodType, Chirag, 24-10-26
			warrantCertificateDtos.add(dto);
		}

		return warrantCertificateDtos;
	}

	public List<WarrantCertificateDto> getWarrantDetailsByInternalInoteId(Integer internalInoteId) {
		List<Object[]> response = warrantCertificateRepository.getWarrantDetailsByInternalInoteId(internalInoteId);
		List<Object[]> passReg = mergeSubSlipRepository.getPassedRegNosByInternalInoteId(internalInoteId);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		List<WarrantCertificateDto> warrantCertificateDtos = new ArrayList<>();
		for (Object[] item : response) {
			WarrantCertificateDto dto = new WarrantCertificateDto();
			dto.setItemCode(item[0] != null ? item[0].toString() : "");
			dto.setNomenclature(item[1] != null ? item[1].toString() : "");
			dto.setDrawingNo(item[2] != null ? item[2].toString() : "");
			dto.setContractNo(item[3] != null ? item[3].toString() : "");
			dto.setInoteId(Integer.parseInt(item[4].toString()));
			dto.setInoteNo(item[5] != null ? item[5].toString() : "");
			if (item[6] != null) {
				try {
					LocalDate dateTime;

					if (item[6] instanceof java.sql.Date) {
						dateTime = ((java.sql.Date) item[6]).toLocalDate();
					}

					else if (item[6] instanceof java.time.LocalDate) {
						dateTime = (java.time.LocalDate) item[6];
					}

					else {
						dateTime = LocalDate.parse(item[6].toString());
					}

					dto.setInoteDate(dateTime.format(formatter));

				} catch (Exception e) {

					dto.setInoteDate(item[6].toString());
				}
			} else {
				dto.setInoteDate("");
			}

			dto.setQty(Integer.parseInt(item[7].toString()));
			dto.setRegNos1(item[8] != null ? item[8].toString() : "");

            dto.setStartDate(item[9] != null ? ((java.sql.Date) item[9]).toLocalDate() : null);
            dto.setEndDate(item[10] != null ? ((java.sql.Date) item[10]).toLocalDate() : null);

			dto.setApprovalDate(item[11] != null ? item[11].toString() : "");
			dto.setWarrantTypeName(item[12] != null ? item[12].toString() : "");
			dto.setEName(item[13] != null ? item[13].toString() : "");
            dto.setIsDoaApproved(item[14] != null ? (Boolean) item[14] : false);
            dto.setWarrantType(item[20] != null ? (Integer) item[20] : null);
            dto.setId(item[16] != null ? Integer.parseInt(item[16].toString()) : null);
            dto.setDoaApproverId(item[17] != null ? Integer.parseInt(item[17].toString()) : null);
            dto.setDoaApproverName(item[18] != null ? item[18].toString() : null);
            dto.setContractDate(item[19] !=null ? inputDateFormat.format((Date)item[19]): null);
            dto.setWarrantyPeriodQty(item[21]!=null? (Integer)item[21]:null);  //removed warrPeriodType, Chirag, 24-10-26
            dto.setRegistrationNo(item[22]!=null? (Integer)item[22]: null);
            dto.setPassedRegNos(passReg.stream().map(regNo -> (regNo[0]!=null? regNo[0].toString(): null)).toList());
			warrantCertificateDtos.add(dto);
		}

		return warrantCertificateDtos;
	}

    @Transactional
    public String approveWarrantyCertificate(Integer iNoteId, Integer empId, Integer roleId) {

        if (iNoteId == null) throw new IllegalArgumentException("I-Note Id cannot be null");
        if (empId == null) throw new IllegalArgumentException("Employee id cannot be null");

        Employee doa = employeeRepository.findById(empId)
                .orElseThrow(() -> new IllegalArgumentException("DOA Employee not found for id: " + empId));

        WarrantCertificate wc = warrantCertificateRepository.findByINoteId(iNoteId)
                .orElseThrow(() -> new IllegalArgumentException("Warranty Certificate not found for I-Note Id: " + iNoteId));

        if (Boolean.TRUE.equals(wc.getIsDoaApproved()) && wc.getDoaApprover() != null) {
            throw new IllegalArgumentException("Warranty Certificate has already been approved by " + wc.getDoaApprover().getEname());
        }

        if (roleId != MasterConstants.EMP_ROLE.ID_DO && doa.getDeptType().getId() == ApplicationConstants.Dept.ASSEMBLY) {
            throw new IllegalArgumentException("Only DO_A is allowed to approve the Warranty Certificate.");
        }

        wc.setDoaApprover(doa);
        wc.setApprovalDate(new Date());
        wc.setIsDoaApproved(true);

        warrantCertificateRepository.save(wc);

        return "Warranty Certificate successfully approved.";
    }
}
	
