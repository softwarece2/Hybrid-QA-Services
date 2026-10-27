package in.co.bel.olms.ate.service;

import in.co.bel.olms.ate.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.ate.entity.FAI;
import in.co.bel.olms.ate.entity.FaiDetails;
import in.co.bel.olms.ate.dto.FaiDetailsDto;
import in.co.bel.olms.ate.dto.FaiDto;
import in.co.bel.olms.ate.repository.FaiRepository;
import in.co.bel.olms.ate.repository.FaiDetailsRepository;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaiService {

    private final FaiRepository faiRepository;
    
    private final FaiDetailsRepository faiDetailsRepository;
    
    private final EmployeeRepository employeeRepository;
    
    private final LineItemLotsRegisterRepository lineItemLotsRegisterRepository;
    
    private final OlLineItemRepository olLineItemRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(FaiService.class);

    @Transactional
    public String saveFai(FaiDto faiDto) {
        FAI fai = new FAI();
        fai.setId(faiDto.getId());
        fai.setName(faiDto.getName());
        fai.setStatus(faiDto.getStatus());
        fai.setSubQty(faiDto.getSubQty());
        fai.setActQty(faiDto.getActQty());
        fai.setRejQty(faiDto.getRejQty());
        fai.setRemarks(faiDto.getRemarks());
        
        Employee createdByEmployee = employeeRepository.findById(faiDto.getCreatedBy()).orElse(null);
//        Employee submittedByEmployee = employeeRepository.findById(faiDto.getSubmittedBy()).orElse(null);

        fai.setCreatedBy(createdByEmployee);
//        fai.setSubmittedBy(submittedByEmployee);
//        fai.setSubmittedDatetime(faiDto.getSubmittedDatetime());
        fai.setCreatedDatetime(faiDto.getCreatedDatetime());
        OlLineItem olLineItem = olLineItemRepository.findById(faiDto.getOlLineItemId()).orElse(null);
        fai.setOlLineItem(olLineItem);
        fai.setIsQa((faiDto.getIsQa()));
        FAI savedFai = faiRepository.save(fai);

        if (faiDto.getFaiDetails() != null) {
            faiDto.getFaiDetails().forEach(faiDetailsDto -> {
                FaiDetails faiDetails = new FaiDetails();
                faiDetails.setFai(savedFai); 
                faiDetails.setRemarks(faiDetailsDto.getRemarks());
                faiDetails.setId(faiDetailsDto.getId());
                Optional<LineItemLotsRegister> RegNoOptional = lineItemLotsRegisterRepository.findById(faiDetailsDto.getRegNo());
                if(RegNoOptional.isPresent()) {
                	faiDetails.setRegNo(RegNoOptional.get());	
                }
                else faiDetails.setRegNo(null); 
                faiDetails.setPageNo(faiDetailsDto.getPageNo());
                faiDetailsRepository.save(faiDetails);
            });
        }

        return "Data submitted successfully";
    }


    public String addRegNoToFai(FaiDto faiDto) throws IllegalArgumentException{
        Optional<FAI> savedFai = faiRepository.findById(faiDto.getId());
        if (savedFai.isPresent() && faiDto.getFaiDetails() != null) {
            List<FaiDetails> faiDetailsList = new ArrayList<>();
            faiDto.getFaiDetails().forEach(faiDetailsDto -> {
                FaiDetails faiDetails = new FaiDetails();
                faiDetails.setFai(savedFai.get());
                faiDetails.setRemarks(faiDetailsDto.getRemarks());
                faiDetails.setId(faiDetailsDto.getId());
                Optional<LineItemLotsRegister> RegNoOptional = lineItemLotsRegisterRepository.findById(faiDetailsDto.getRegNo());
                if(RegNoOptional.isPresent()) {
                    faiDetails.setRegNo(RegNoOptional.get());
                }
                else faiDetails.setRegNo(null);
                faiDetails.setPageNo(faiDetailsDto.getPageNo());
                faiDetailsList.add(faiDetails);
            });
            faiDetailsRepository.saveAll(faiDetailsList);
        } else throw new IllegalArgumentException("FAI not found");
        return "Data submitted successfully";
    }
    
    public List<FaiDto> getFaiByOlLineItemId(Integer olLineItemId) {
        List<FAI> faiList = faiRepository.findByOlLineItemId(olLineItemId);      
        return faiList.stream()
                .map(fai -> {
                    FaiDto faiDto = new FaiDto();
                    faiDto.setId(fai.getId());
                    faiDto.setName(fai.getName());
                    faiDto.setStatus(fai.getStatus());
                    faiDto.setActQty(fai.getActQty());
                    faiDto.setSubQty(fai.getSubQty());
                    faiDto.setRejQty(fai.getRejQty());
                    faiDto.setRemarks(fai.getRemarks());
                    
                    if (fai.getSubmittedBy() != null) {faiDto.setSubmittedBy(fai.getSubmittedBy().getId());}                    
                    faiDto.setSubmittedDatetime(fai.getSubmittedDatetime());
                    if (fai.getCreatedBy() != null) {faiDto.setCreatedBy(fai.getCreatedBy().getId());}
                    faiDto.setCreatedDatetime(fai.getCreatedDatetime());
                    faiDto.setOlLineItemId(fai.getOlLineItem().getId());
                    List<FaiDetails> faiDetailsList = faiDetailsRepository.findByFaiId(fai.getId());
                    faiDto.setFaiDetails(faiDetailsList.stream().map(f -> {
                        FaiDetailsDto faiDetailsDto = new FaiDetailsDto();
                        faiDetailsDto.setId(f.getId());
                        if(f.getRegNo()!=null) {
                        	faiDetailsDto.setRegNo(f.getRegNo().getId());
                        	faiDetailsDto.setRegName(f.getRegNo().getRegisterNo());
                        }
                        faiDetailsDto.setRemarks(f.getRemarks());
                        faiDetailsDto.setPageNo(f.getPageNo());
                        return faiDetailsDto;
                    }).collect(Collectors.toList()));

                    
                    return faiDto;
                })
                .collect(Collectors.toList());
    }

    public List<FaiDto> getFaiByOlLineItemIdFroQa(Integer olLineItemId) {
        List<FAI> faiList = faiRepository.findByOlLineItemIdForQa(olLineItemId);
        return faiList.stream()
                .map(fai -> {
                    FaiDto faiDto = new FaiDto();
                    faiDto.setId(fai.getId());
                    faiDto.setName(fai.getName());
                    faiDto.setStatus(fai.getStatus());
                    faiDto.setActQty(fai.getActQty());
                    faiDto.setSubQty(fai.getSubQty());
                    faiDto.setRejQty(fai.getRejQty());

                    if (fai.getSubmittedBy() != null) {faiDto.setSubmittedBy(fai.getSubmittedBy().getId());}
                    faiDto.setSubmittedDatetime(fai.getSubmittedDatetime());
                    if (fai.getCreatedBy() != null) {faiDto.setCreatedBy(fai.getCreatedBy().getId());}
                    faiDto.setCreatedDatetime(fai.getCreatedDatetime());
                    faiDto.setOlLineItemId(fai.getOlLineItem().getId());
                    List<FaiDetails> faiDetailsList = faiDetailsRepository.findByFaiId(fai.getId());
                    faiDto.setFaiDetails(faiDetailsList.stream().map(f -> {
                        FaiDetailsDto faiDetailsDto = new FaiDetailsDto();
                        faiDetailsDto.setId(f.getId());
                        if(f.getRegNo()!=null) {
                            faiDetailsDto.setRegNo(f.getRegNo().getId());
                            faiDetailsDto.setRegName(f.getRegNo().getRegisterNo());
                        }
                        faiDetailsDto.setRemarks(f.getRemarks());
                        faiDetailsDto.setPageNo(f.getPageNo());
                        return faiDetailsDto;
                    }).collect(Collectors.toList()));


                    return faiDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LineItemLotsRegister> saveSampleSelection(LineItemLotsRegisterDto registerDto) {
        List<LineItemLotsRegister> savedEntities = new ArrayList<>();

        if (registerDto.getIds() == null || registerDto.getIds().isEmpty()) {
            return savedEntities;
        }

        List<LineItemLotsRegister> lotsRegister = lineItemLotsRegisterRepository.findAllById(registerDto.getIds());
        InternalINote iNote = new InternalINote();

        for (LineItemLotsRegister registers : lotsRegister) {
            registers.setIsSample(true);
            if (registerDto.getSampleQty() != null) {
                iNote.setSampleQty(registerDto.getSampleQty());
            }
        }
        savedEntities = lineItemLotsRegisterRepository.saveAll(lotsRegister);

        //save FAI Pages
        try{
            //get OL LI wise map
            Map<Integer,List<LineItemLotsRegister>> map = savedEntities.stream().collect(Collectors.groupingBy((reg -> reg.getOlLineItemId().getId())));
            List<FaiDetails> faiDetailsList = new ArrayList<>();
            for(Integer olLiId : map.keySet()) {
                FAI fai = new FAI();
                fai.setName("Round 1");
                Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(olLiId);
                if(olLineItemOptional.isPresent()) {
                OlLineItem olLineItem = olLineItemOptional.get();
                fai.setOlLineItem(olLineItem);
                }
                fai.setIsQa(true);
                FAI savedFai = faiRepository.save(fai);

                int regNoCtr = 1;
                int pageNo = 1;
                for(LineItemLotsRegister register : map.get(olLiId)){
                    FaiDetails faiDetails = new FaiDetails();
                    faiDetails.setFai(savedFai);
                    faiDetails.setRegNo(register);
                    if(regNoCtr % 10 == 0) pageNo++;
                    faiDetails.setPageNo(pageNo);
                    faiDetailsList.add(faiDetails);
                    regNoCtr++;
                }
            }
            faiDetailsRepository.saveAll(faiDetailsList);
        } catch (Exception e) {
            logger.error(" saveSampleSelection save FAI Pages Exception: {}" , e.getMessage());
            e.printStackTrace();
        }
        return savedEntities;
    }

    @Transactional
    public List<LineItemLotsRegister> saveFaiSampleSelection(LineItemLotsRegisterDto registerDto) {
        List<LineItemLotsRegister> savedEntities = new ArrayList<>();

        if (registerDto.getIds() == null || registerDto.getIds().isEmpty()) {
            return savedEntities;
        }

        List<LineItemLotsRegister> lotsRegister = lineItemLotsRegisterRepository.findAllById(registerDto.getIds());
        InternalINote iNote = new InternalINote();

        for (LineItemLotsRegister registers : lotsRegister) {
            registers.setIsFaiSample(true);
            if (registerDto.getSampleQty() != null) {
                iNote.setSampleQty(registerDto.getSampleQty());
            }
        }
        savedEntities = lineItemLotsRegisterRepository.saveAll(lotsRegister);

        //save FAI Pages
        try{
            //get OL LI wise map
            Map<Integer,List<LineItemLotsRegister>> map = savedEntities.stream().collect(Collectors.groupingBy((reg -> reg.getOlLineItemId().getId())));
            List<FaiDetails> faiDetailsList = new ArrayList<>();
            for(Integer olLiId : map.keySet()) {
                FAI fai = new FAI();
                fai.setName("Round 1");
                Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(olLiId);
                if(olLineItemOptional.isPresent()) {
                OlLineItem olLineItem = olLineItemOptional.get();
                fai.setOlLineItem(olLineItem);
                }
                fai.setIsQa(true);
                FAI savedFai = faiRepository.save(fai);

                int regNoCtr = 1;
                int pageNo = 1;
                for(LineItemLotsRegister register : map.get(olLiId)){
                    FaiDetails faiDetails = new FaiDetails();
                    faiDetails.setFai(savedFai);
                    faiDetails.setRegNo(register);
                    if(regNoCtr % 10 == 0) pageNo++;
                    faiDetails.setPageNo(pageNo);
                    faiDetailsList.add(faiDetails);
                    regNoCtr++;
                }
            }
            faiDetailsRepository.saveAll(faiDetailsList);
        } catch (Exception e) {
        	logger.error(" saveSampleSelection save FAI Pages Exception: {}" , e.getMessage());
            e.printStackTrace();
        }
        return savedEntities;
    }
}