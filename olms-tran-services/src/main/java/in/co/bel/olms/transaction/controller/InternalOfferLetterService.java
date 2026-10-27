package in.co.bel.olms.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import in.co.bel.olms.master.entity.DepartmentType;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalOfferLetterService {

    private final EmployeeRepository empRepository;

    private final OlLineItemRepository olLineItemRepository;

    private final OfferLetterRepository offerLetterRepository;

    private final Mapper mapper;
    
    private static final Logger logger = LoggerFactory.getLogger(InternalOfferLetterService.class);

    private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public OfferLetterDto getInternalOlByOlId(int id) {
        OfferLetterDto internalOfferLetterDto = new OfferLetterDto();
        List<OlLineItemDto> olLineItemDtos = new ArrayList<>();
        Optional<OfferLetter> internalOfferLetterOptional = offerLetterRepository.findById(id);
        if (internalOfferLetterOptional.isPresent()) {
            OfferLetter internalOfferLetter = internalOfferLetterOptional.get();
            internalOfferLetterDto.setId(internalOfferLetter.getId());
            internalOfferLetterDto.setOlNumber(internalOfferLetter.getOlNumber());
            internalOfferLetterDto.setSaleOrderNo(internalOfferLetter.getSaleOrder().getId());
            internalOfferLetterDto.setSaleOrderNoStr(internalOfferLetter.getSaleOrder().getSaleOrderNo());
            internalOfferLetterDto.setEmployeeId(internalOfferLetter.getCreatedBy());
            internalOfferLetterDto.setOfferDate(dateFormat.format(internalOfferLetter.getOfferDate()));
            List<OlLineItem> internalOlLineItemList = olLineItemRepository.getLineItemsByOl(id);
            for (OlLineItem internalOlLineItem : internalOlLineItemList) {
                OlLineItemDto olLineItemDto = mapper.map(internalOlLineItem, OlLineItemDto.class);
                olLineItemDtos.add(olLineItemDto);
            }
            internalOfferLetterDto.setOlLineItemDtos(olLineItemDtos);
        }
        return internalOfferLetterDto;
    }

    public List<OfferLetterDto> getInternalOlByEmpId(Integer empId) {
        List<OfferLetterDto> internalOfferLetterDtoList = new ArrayList<>();

        Optional<Employee> optionalEmployee = empRepository.findById(empId);
        if(optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            DepartmentType department = employee.getDeptType();
            //Get all OLs for Assy Chargeman, else by allotment or notification to employee
            List<OfferLetter> internalOfferLetterList;
            if (employee.getEmpRole().getId() == in.co.bel.core.constants.ApplicationConstants.Role.GRP_INCHARGE &&
                    department.getId() == in.co.bel.core.constants.ApplicationConstants.Dept.ASSEMBLY)
                internalOfferLetterList = offerLetterRepository.findAll();
            else
                internalOfferLetterList = offerLetterRepository.findInternalOlByEmpId(empId);
            List<Integer> olIdlist = internalOfferLetterList.stream().map(OfferLetter::getId)
                    .toList();
            List<OlLineItem> olLineItemList = olLineItemRepository.findByInternalOlIdList(olIdlist);
            for (OfferLetter internalOfferLetter : internalOfferLetterList) {
                OfferLetterDto internalOfferLetterDto = new OfferLetterDto();
                List<OlLineItemDto> olLineItemDtos = new ArrayList<>();
                internalOfferLetterDto.setId(internalOfferLetter.getId());
                internalOfferLetterDto.setOlNumber(internalOfferLetter.getOlNumber());
                internalOfferLetterDto.setSaleOrderNo(internalOfferLetter.getSaleOrder().getId());
                internalOfferLetterDto.setSaleOrderNoStr(internalOfferLetter.getSaleOrder().getSaleOrderNo());
                internalOfferLetterDto.setEmployeeId(internalOfferLetter.getCreatedBy());
                internalOfferLetterDto.setOfferDate(dateFormat.format(internalOfferLetter.getOfferDate()));
                List<OlLineItem> internalOlLineItemList = olLineItemList.stream()
                        .filter(li -> Objects.equals(li.getOfferLetter().getId(), internalOfferLetter.getId()))
                        .toList();
                for (OlLineItem internalOlLineItem : internalOlLineItemList) {
                    OlLineItemDto olLineItemDto = mapper.map(internalOlLineItem, OlLineItemDto.class);
                    olLineItemDtos.add(olLineItemDto);
                }
                internalOfferLetterDto.setOlLineItemDtos(olLineItemDtos);
                internalOfferLetterDtoList.add(internalOfferLetterDto);
            }
        }
        return internalOfferLetterDtoList;
    }
}
