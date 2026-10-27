package in.co.bel.olms.master.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.repository.PartnoProductMappingRepository;
import in.co.bel.olms.master.dto.DeptDropDto;
import in.co.bel.olms.master.dto.EpCodeDto;
import in.co.bel.olms.master.dto.PartNumberDto;
import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.master.entity.Uom;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ItemCategoryRepository;
import in.co.bel.olms.master.repository.PartNumberRepository;
import in.co.bel.olms.master.repository.UomRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional 
@RequiredArgsConstructor
public class PartNumberService {

    private final PartNumberRepository partNumberRepository;
    
    private final Mapper mapper;

    private final UomRepository uomRepo;

    private final ItemCategoryRepository itemCatRepo;

    private final PartnoProductMappingRepository partnoProductMappingRepository;

    public PartNumberDto save(PartNumberDto partNumberDTO) {
    	 partNumberRepository.findByPartNoIgnoreCase(partNumberDTO.getPartNo())
         .ifPresent(existingPart -> {
             throw new RuntimeException("Part Number '" + partNumberDTO.getPartNo() + 
                 "' already exists.");
         });
        PartNumber partNumber = mapper.map(partNumberDTO, PartNumber.class);
        partNumber.setCreatedDate(new Date());
        return mapper.map(partNumberRepository.save(partNumber), PartNumberDto.class);
    }

    public List<PartNumberDto> getAll() {
        return partNumberRepository.findAll().stream()
                .map(partNumber -> mapper.map(partNumber, PartNumberDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<PartNumberDto> getAllActive() {

        return partNumberRepository.findByActiveTrue().stream()
                .map(partNumber -> {
                	PartNumberDto dto = mapper.map(partNumber, PartNumberDto.class);
                	List<Integer> sectionCode = partNumberRepository.findSectionCodeByPartNumberId(partNumber.getId());
                	dto.setSectionCode(sectionCode);
                	dto.setPartNoMatDesc(partNumber.getPartNo() + " - " + partNumber.getMaterialDescription());
                	dto.setIsMapped(partnoProductMappingRepository.existsByPartNumberId(partNumber.getId())? true: false); 
                	return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }
	
    public PartNumberDto getById(Integer id) {
        return partNumberRepository.findById(id)
                .map(partNumber -> mapper.map(partNumber, PartNumberDto.class))
                .orElseThrow(() -> new DataNotFoundException("Part Number not found with Id: " + id)); 
    }

    public String update(PartNumberDto partNumberDTO) {
    	
		
        Optional<PartNumber> partNumberOptional = partNumberRepository.findById(partNumberDTO.getId());
        if (partNumberOptional.isPresent()) {
            PartNumber existingPartNumber = partNumberOptional.get();

            if (partNumberDTO.getPartNo() != null) {
                existingPartNumber.setPartNo(partNumberDTO.getPartNo());
            }
            if (partNumberDTO.getMaterialDescription() != null) {
                existingPartNumber.setMaterialDescription(partNumberDTO.getMaterialDescription());
            }
            if (partNumberDTO.getCatPartNo() != null) {
                existingPartNumber.setCatPartNo(partNumberDTO.getCatPartNo());
            }
            if (partNumberDTO.getNsnPartNo() != null) {
                existingPartNumber.setNsnPartNo(partNumberDTO.getNsnPartNo());
            }
            if (partNumberDTO.getActive() != null) {
                existingPartNumber.setActive(partNumberDTO.getActive());
            }
            if (partNumberDTO.getModifiedBy() != null) {
                existingPartNumber.setModifiedBy(partNumberDTO.getModifiedBy());
            }
            if (partNumberDTO.getDrawingNo() != null) {
            	existingPartNumber.setDrawingNo(partNumberDTO.getDrawingNo());
            }
            if (partNumberDTO.getUomId() != null) {
            	Uom uom = uomRepo.findById(partNumberDTO.getUomId()).orElseThrow(() -> new IllegalArgumentException("UOM not found"));
                existingPartNumber.setUom(uom);
            }
            if (partNumberDTO.getUomId() != null) {
            	ItemCategory itemCat = itemCatRepo.findById(partNumberDTO.getItemCategoryId()).orElseThrow(() -> new IllegalArgumentException("Item Category not found"));
                existingPartNumber.setItemCategory(itemCat);
            }
           
            existingPartNumber.setModifiedDate(new Date());
            partNumberRepository.save(existingPartNumber);
            return "Part number updated succesfully";
        } else {
            throw new DataNotFoundException("Part Number not found with Id: " + partNumberDTO.getId());
        }
    }
    
    

	public List<EpCodeDto> getDetailsBypartNumber(Integer partNumberId) {
		List<Object[]> resultsOptional = partNumberRepository.getDetailsBypartNumber(partNumberId);

		if (resultsOptional.isEmpty()) {
			return new ArrayList<>();
		}

		List<EpCodeDto> dtoList = new ArrayList<>();		
		
		for (Object[] data : resultsOptional) {
			EpCodeDto dto = new EpCodeDto();
			dto.setPartNo((String) data[0]);
			dto.setDescription((String) data[1]);
			dto.setLineItemNo((String) data[2]);
			dto.setOrderDetails((String) data[3]);
			dto.setTotalQty((BigDecimal) data[4]);
			dto.setIndenterName((String) data[5]);
			dto.setInspectionAuthorityName((String) data[6]);
			dto.setConsigneeName((String) data[7]);
			dto.setOrderNo((String) data[8]);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public List<DeptDropDto> getAllDepartmentNames(Integer partNumberId) {

		List<Object[]> dept = partNumberRepository.findDepartmentByPartNumberId(partNumberId);
		if (dept.isEmpty()) {
			return new ArrayList<>();
		}

		List<DeptDropDto> dtoList = new ArrayList<>();
		for (Object[] data : dept) {
			DeptDropDto dto = new DeptDropDto();
			dto.setId((int) data[0]);
			dto.setDepartmentName((String) data[1]);
			dtoList.add(dto);

		}
		return dtoList;
	}	
}
	