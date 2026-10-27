package in.co.bel.olms.transaction.service.so;


import in.co.bel.olms.master.dto.OrganizationDto;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.FagEndNoticeDto;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.FagEndNotice;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.DdExtensionRepository;
import in.co.bel.olms.transaction.repository.FagEndNoticeRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

import org.dozer.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FagEndNoticeService {

    private final FagEndNoticeRepository fagEndNoticeRepository;

    private final DdExtensionRepository ddExtensionRepository;

    private final EmployeeRepository employeeRepository;

    private final SupplyOrderService supplyOrderService;

    private final OfferLetterService offerLetterService;

    private final Mapper mapper;

    private final EntityValidator entityValidator;

    Logger logger = LoggerFactory.getLogger(FagEndNoticeService.class);


    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Transactional
    public FagEndNotice saveFagEndNotice(FagEndNoticeDto fagEndNoticeDto) {
        FagEndNotice fagEndNotice;
        if (fagEndNoticeDto.getId() != 0) {
            Optional<FagEndNotice> fagOptional = fagEndNoticeRepository.findById(fagEndNoticeDto.getId());
            if (fagOptional.isPresent()) {
                fagEndNotice = fagOptional.get();
                fagEndNotice.setIssuerDesignation(fagEndNoticeDto.getIssuerDesignation());
                fagEndNotice.setIssuerName(fagEndNoticeDto.getIssuerName());
                fagEndNotice.setIssuerSignature(fagEndNoticeDto.getIssuerSignature());
                fagEndNotice.setModifiedDateTime(new Date());
                fagEndNotice.setEmplId(fagEndNoticeDto.getEmpId());
            } else {
                throw new DataNotFoundException(
                        "FagEndNotice save : FagEndNotice not found By Id: " + fagEndNoticeDto.getId());
            }
        } else {
            fagEndNotice = mapper.map(fagEndNoticeDto, FagEndNotice.class);
            List<DdExtension> lDdExt = ddExtensionRepository.getBySoId(fagEndNoticeDto.getSupplyOrderId());
            Date latestDate = null;
            SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(fagEndNoticeDto.getSupplyOrderId());
            OfferLetter offerLetter = offerLetterService.getOfferLetterById(fagEndNoticeDto.getOfferLetterId());
            fagEndNotice.setSupplyOrder(supplyOrder);
            fagEndNotice.setOfferLetter(offerLetter);
            fagEndNotice.setOrderNo(supplyOrder.getOrderNo());
            fagEndNotice.setOrderDate(supplyOrder.getOrderDate());
            fagEndNotice.setDescription(supplyOrder.getDescription());
            fagEndNotice.setOlNumber(offerLetter.getOlNumber());
            fagEndNotice.setOfferDate(offerLetter.getOfferDate());
            fagEndNotice.setRecievedDate(supplyOrder.getReceiptDate());
            fagEndNotice.setCreatedDatetime(new Date());
            fagEndNotice.setEmplId(fagEndNoticeDto.getEmpId());
            Integer empId = fagEndNoticeDto.getEmpId();
            Employee employee = getEmployeeById(empId);
            EmpRole empRole = employee.getEmpRole();

            if (employee.getEmpDesignation() != null) {
                fagEndNotice.setIssuerDesignation(employee.getEmpDesignation().getDesignation());
            } else {
                fagEndNotice.setIssuerDesignation(empRole.getEmpRole());
                logger.debug("fagEndNoticeDto = {}", fagEndNoticeDto);
            }
            fagEndNotice.setIssuerName(employee.getEname());
            fagEndNotice.setIssuerSignature(employee.getEname());
            for (DdExtension ddExtension : lDdExt) {
                Date currentDate = ddExtension.getNewDdDate();
                if (latestDate == null || currentDate.after(latestDate)) {
                    latestDate = currentDate;
                }
            }
            if (latestDate != null) {
                fagEndNotice.setDd(latestDate);
            } else {
                fagEndNotice.setDd(supplyOrder.getDd());
            }

        }
        boolean isvalid = entityValidator.isValidEntity(fagEndNotice);
        if (isvalid) {
            fagEndNotice = fagEndNoticeRepository.save(fagEndNotice);
        } else {
            throw new DataNotValidException("FagEndNotice save: det " + fagEndNotice.toString());
        }

        return fagEndNotice;

    }

    public FagEndNotice submitFagEndNotice(@Valid FagEndNoticeDto fagEndNoticeDto) {
        FagEndNotice fagEndNotice = saveFagEndNotice(fagEndNoticeDto);
        fagEndNotice.setSubmittedDate(new Date());
        boolean isValid = entityValidator.isValidEntity(fagEndNotice);
        if (isValid) {
            fagEndNotice = fagEndNoticeRepository.save(fagEndNotice);
            fagEndNotice.setEmplId(fagEndNoticeDto.getEmpId());
        } else {
            throw new DataNotFoundException("FagEndNotice Submit:" + fagEndNotice.toString());
        }
        return fagEndNotice;
    }

    public FagEndNotice getFagEndNotice(int id) {

        FagEndNotice fagEndNotice = fagEndNoticeRepository.fetchFagEndNoticeByOlId(id);
        if (fagEndNotice != null) {
            return fagEndNotice;
        } else {
            throw new DataNotFoundException("FagEndNotice not found with Id: " + id);
        }
    }

    public Employee getEmployeeById(int id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new DataNotFoundException("Employee not found Id: " + id);
        }

    }

    public FagEndNotice getFagEndNoticeByOlId(int olId) {

        Optional<FagEndNotice> fagEndNoticeOptional = fagEndNoticeRepository.findByOlid(olId);
        if (fagEndNoticeOptional.isPresent()) {
            return fagEndNoticeOptional.get();
        } else {
            throw new DataNotFoundException("FagEndNotice not found with Id: " + olId);
        }
    }


    public FagEndNoticeDto getScrutinyFagEndNoticeBySoId(int olid) {
        FagEndNoticeDto fagEndNoticeDto = new FagEndNoticeDto();
        try {
            OfferLetter offerLetter = offerLetterService.getOfferLetterById(olid);
            int supplyOrderId = offerLetter.getSaleOrder().getSupplyOrder().getId();
            SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(supplyOrderId);
            fagEndNoticeDto.setSupplyOrderId(supplyOrderId);
            List<DdExtension> lDdExt = ddExtensionRepository.getBySoId(supplyOrderId);
            Optional<FagEndNotice> fagEndNoticeOpt = fagEndNoticeRepository.findByOlid(olid);

            //Supply order Org
            OrganizationDto mappedSoOrgDto = mapper.map(supplyOrder.getOrganization(), OrganizationDto.class);
            mappedSoOrgDto.setOrganizationType(null);
            fagEndNoticeDto.setSoOrganizationDto(mappedSoOrgDto);


            //Sell Group Org
            OrganizationDto mappedSgOrgDto = mapper.map(supplyOrder.getSalesGroup().getOrganization(), OrganizationDto.class);
            mappedSgOrgDto.setOrganizationType(null);
            fagEndNoticeDto.setSgOrganizationDto(mappedSgOrgDto);

            fagEndNoticeDto.setCategoryName(offerLetter.getSaleOrder().getCategory().getCategoryName());
            fagEndNoticeDto.setProjName(offerLetter.getSaleOrder().getCategory().getShip().getProject().getProjectName());


            if (fagEndNoticeOpt.isEmpty()) {
                logger.debug("FagEndNotice not found for offerLetter ID: {}", olid);
                populateFagEndNoticeDtoFromOfferLetter(fagEndNoticeDto, offerLetter, supplyOrder, lDdExt);
            } else {
                FagEndNotice fagEndNotice = fagEndNoticeOpt.get();
                logger.debug("FagEndNotice is found for offerLetter ID: {}", olid);
                populateFagEndNoticeDtoFromFagEndNotice(fagEndNoticeDto, fagEndNotice, lDdExt);
            }
        } catch (Exception e) {

            logger.error("Error occured in fetching the FagEndNotice: {}", e.getMessage());
            return null;
        }
        logger.debug("FagEndNoticeDto: {}", fagEndNoticeDto);
        return fagEndNoticeDto;
    }

    private void populateFagEndNoticeDtoFromOfferLetter(FagEndNoticeDto dto, OfferLetter offerLetter, SupplyOrder supplyOrder, List<DdExtension> ddExtensions) {
        dto.setSupplyOrderId(offerLetter.getSaleOrder().getSupplyOrder().getId());
        dto.setEmpId(dto.getEmpId());
        dto.setOfferLetterId(offerLetter.getId());
        dto.setOrderNo(offerLetter.getSaleOrder().getSupplyOrder().getOrderNo());
        dto.setOrderDate(offerLetter.getSaleOrder().getSupplyOrder().getOrderDate().toString());
        dto.setDescription(offerLetter.getSaleOrder().getSupplyOrder().getDescription());
        dto.setOlNumber(offerLetter.getOlNumber());
        if (offerLetter.getOfferDate() != null || offerLetter.getOfferDate().toString().isEmpty()) {
            dto.setOfferDate(offerLetter.getOfferDate().toString());
        } else {
            dto.setOfferDate("");
        }
        dto.setDd(getLatestDdDate(ddExtensions));
        dto.setRecievedDate(offerLetter.getSaleOrder().getSupplyOrder().getReceiptDate() != null
                ? offerLetter.getSaleOrder().getSupplyOrder().getReceiptDate().toString()
                : "Date not available");
    }

    private void populateFagEndNoticeDtoFromFagEndNotice(FagEndNoticeDto dto, FagEndNotice fagEndNotice, List<DdExtension> ddExtensions) {
        Employee employee = getEmployeeById(fagEndNotice.getEmplId());
        dto.setOrderNo(fagEndNotice.getSupplyOrder().getOrderNo());
        dto.setOfferLetterId(fagEndNotice.getOfferLetter().getId());
        dto.setSupplyOrderId(fagEndNotice.getSupplyOrder().getId());
        if (fagEndNotice.getSupplyOrder() != null && fagEndNotice.getSupplyOrder().getOrderDate() != null) {
            dto.setOrderDate(fagEndNotice.getSupplyOrder().getOrderDate().toString());
        } else {
            dto.setOrderDate("Date not available");
        }
        dto.setDescription(fagEndNotice.getSupplyOrder().getDescription());
        if (fagEndNotice.getOfferLetter() != null && fagEndNotice.getOfferLetter().getOlNumber() != null) {
            dto.setOlNumber(fagEndNotice.getOfferLetter().getOlNumber());
        } else {
            dto.setOlNumber("Offer Letter Number not available");
        }
        if (fagEndNotice.getOfferLetter() != null && fagEndNotice.getOfferLetter().getOfferDate() != null) {
            dto.setOfferDate(fagEndNotice.getOfferLetter().getOfferDate().toString());
        } else {
            dto.setOfferDate("Date not available");
        }
        Date latestDate = getLatestDdDate(ddExtensions);
        if (latestDate == null) {
            dto.setDd(fagEndNotice.getDd());
        } else {
            dto.setDd(latestDate);
        }
//	    if (fagEndNotice.getEmployee() != null && fagEndNotice.getEmployee().getEmpDesignation() != null && fagEndNotice.getEmployee().getEmpDesignation().getDesignation() != null) {
//	        dto.setIssuerDesignation(employee.getEmpDesignation().getDesignation());
//	    } else {
        dto.setIssuerDesignation(fagEndNotice.getIssuerDesignation());
//	    }
        if (employee.getEname() != null) {
            dto.setIssuerName(employee.getEname());
        } else {
            dto.setIssuerName("Name is not available");
        }
        if (employee.getEname() != null) {
            dto.setIssuerSignature(employee.getEname());
        } else {
            dto.setIssuerName("Signature is not available");
        }
        dto.setEmpId(fagEndNotice.getEmplId());
        dto.setRecievedDate(fagEndNotice.getSupplyOrder().getReceiptDate() != null
                ? fagEndNotice.getSupplyOrder().getReceiptDate().toString()
                : "Date not available");
        dto.setCreatedDatetime(new Date());
        dto.setLastUpdatedBy(fagEndNotice.getLastUpdatedBy());
        dto.setSubmittedDate(fagEndNotice.getSubmittedDate());
        dto.setEmplId(fagEndNotice.getEmplId());
    }

    private Date getLatestDdDate(List<DdExtension> ddExtensions) {
        Date latestDate = null;
        for (DdExtension ddExtension : ddExtensions) {
            Date currentDate = ddExtension.getNewDdDate();
            if (latestDate == null || currentDate.after(latestDate)) {
                latestDate = currentDate;
            }
        }
        return latestDate;
    }


}
