
package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.core.constants.RegistrationConstants;
import in.co.bel.olms.constants.ApplicationConstants.COMMON_STATUS_CONSTANTS;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.entity.Status;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.MergeSubSlipDto;
import in.co.bel.olms.transaction.dto.MergedOlDetDto;
import in.co.bel.olms.transaction.dto.MergedSubSlipDto;
import in.co.bel.olms.transaction.dto.OlDetailDto;
import in.co.bel.olms.transaction.entity.MergeSubSlip;
import in.co.bel.olms.transaction.entity.MergeSubSlipMapping;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.MergeSubSlipMappingRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;

@Service
public class MergeSubSlipService {

	@Autowired
	MergeSubSlipMappingRepository mergeSubSlipMappingRepository;

	@Autowired
	MergeSubSlipRepository mergeSubSlipRepository;

	@Autowired
	SubmissionSlipRepository submissionSlipRepository;
	
	@Autowired
	SubSlipSerialNoRepository subSlipSerialNoRepository;

	@Autowired
	LineItemSerialNoService lineItemSerialNoService;
	
	@Autowired
	private OfferLetterRepository offerLetterRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;

  @Autowired
  StatusRepository statusRepositoy;

	@Autowired
	Mapper mapper;

  public String saveMergeOl(MergeSubSlipDto reqDto) {
    List<Integer> subSlipIds = reqDto.getOlIds();

    if (subSlipIds == null || subSlipIds.isEmpty()) {
      throw new IllegalArgumentException("No subSlipIds provided. Operation completed.");
		}

    if (reqDto.getMergeOlNo() == null || reqDto.getMergeOlNo().trim().isEmpty()) {
      throw new IllegalArgumentException("No mergeOl name provided. Can not save, Operation completed.");
    }

    MergeSubSlip mergeSubSlip = mapper.map(reqDto, MergeSubSlip.class);
    Status status = statusRepositoy.findById(COMMON_STATUS_CONSTANTS.CREATED)
        .orElseThrow(() -> new IllegalArgumentException("Status not found for id: "));
    mergeSubSlip.setStatus(status);
    List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(reqDto.getOlIds().get(0));
	String secCode = null;
	if(obj!=null) {
		for(Object[] det: obj) {
		secCode = det[2]!=null? det[2].toString(): null;
		}
	}
	
	mergeSubSlip.setDepartment(departmentRepository.findBySectionCode(Integer.parseInt(secCode)));
    MergeSubSlip savedMergeSubSlip = mergeSubSlipRepository.save(mergeSubSlip);

    List<MergeSubSlipMapping> mergeSubSlipMappinglist = new ArrayList<>();
    for (Integer subSlipId : subSlipIds) {

      SubmissionSlip subSlip = submissionSlipRepository.findById(subSlipId)
          .orElseThrow(() -> new IllegalArgumentException("SubmissionSlip not found for the given Id: " + subSlipId));
      subSlip.setIsMerged(true);

      MergeSubSlipMapping mergeOlMapping = new MergeSubSlipMapping();
      mergeOlMapping.setMergeOlNoId(savedMergeSubSlip);
      mergeOlMapping.setSubSlip(subSlip);
      mergeOlMapping.setIsMerged(true);
      mergeSubSlipMappinglist.add(mergeOlMapping);
    }
    mergeSubSlipMappingRepository.saveAll(mergeSubSlipMappinglist);

    // update serial no status logic.
    List<Integer> passedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(subSlipIds, RegistrationConstants.PASS);
    List<Integer> failedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(subSlipIds, RegistrationConstants.FAIL);

    if (!passedSerialNos.isEmpty()) {
      LineItemSerialNoDto dto = new LineItemSerialNoDto();
      dto.setIds(passedSerialNos);
      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.LOT_GENERATED);
      lineItemSerialNoService.update(dto);

      LineItemSerialNoHistoryDto passedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(passedSerialNos, SERIAL_NO_STATUS_CONSTANTS.LOT_GENERATED, reqDto.getCreatedBy(), savedMergeSubSlip.getId(), reqDto.getMergeOlNo());
      lineItemSerialNoService.addHistory(passedRegsHistoryDto);
    }

    if (!failedSerialNos.isEmpty()) {
      LineItemSerialNoDto dto = new LineItemSerialNoDto();
      dto.setIds(failedSerialNos);
      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.RFR);
      lineItemSerialNoService.update(dto);

      LineItemSerialNoHistoryDto failedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(failedSerialNos, SERIAL_NO_STATUS_CONSTANTS.RFR, reqDto.getCreatedBy(), savedMergeSubSlip.getId(), reqDto.getMergeOlNo());
      lineItemSerialNoService.addHistory(failedRegsHistoryDto);
    }
    return "Data submitted successfully";

	}

	
	// Still code to be changed
  public List<MergedSubSlipDto> getAllMergedOls(int empId, int roleId, List<Integer> statusIds) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    if (statusIds == null || statusIds.isEmpty()) { //default statuses
      statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.QCC_GENERATED, COMMON_STATUS_CONSTANTS.DOA_APPROVED, COMMON_STATUS_CONSTANTS.DOQC_APPROVED, COMMON_STATUS_CONSTANTS.OL_GENERATED);
    }

		if( roleId == MasterConstants.EMP_ROLE.ID_DO) {
      data = mergeSubSlipRepository.getAllMergedOlsDOA(statusIds);
    } else
      data = mergeSubSlipRepository.getAllMergedOls(empId, statusIds);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<MergedSubSlipDto> mergedOlDetDtos = new ArrayList<>();
		for (Object[] row : data) {
			try {
				MergedSubSlipDto mergedOlDetDto = new MergedSubSlipDto();
				int index=0;
				
				mergedOlDetDto.setMergedOlNo((String) row[index]);
				index++;
				mergedOlDetDto.setMergedDate(sdf.format((Date) row[index]));
				index++;
				mergedOlDetDto.setSoNumber((String) row[index]);
				index++;
				mergedOlDetDto.setSoDate((String) row[index]);
				index++;
				mergedOlDetDto.setGrp((String) row[index]);
				index++;
				mergedOlDetDto.setMergedOlId((Integer)row[index]);
				index++;
				mergedOlDetDto.setOrganizationTypeId((Integer)  row[index]);
				index++;
				mergedOlDetDto.setInspectionAuthority((String) row[index]);
				index++;				
				mergedOlDetDto.setSoLineItemId((Integer)  row[index]);				
				index++;
				mergedOlDetDto.setEpcode((String)row[index]);
        index++;
        mergedOlDetDto.setStatusId((Integer) row[index]);
        index++;
        mergedOlDetDto.setStatus(row[index] != null ? (String) row[index] : "");
				mergedOlDetDtos.add(mergedOlDetDto);
			} catch (ClassCastException e) {
        // Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings or dates
				System.err.println("Error casting row data: " + e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error accessing row elements: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("An unexpected error occurred: " + e.getMessage());
			}
		}

		return mergedOlDetDtos;
	}

	public List<OlDetailDto> getAllOlDetails(List<Integer> olIds) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		data = mergeSubSlipRepository.getAllOlDetails(olIds);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, OlDetailDto> resultMapp = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				String supplyOrderNo = (String) row[0];
				String supplyOrderDate = sdf.format((Date) row[1]);
				String olNo = (String) row[2];
				String olDate = sdf.format((Date) row[3]);
			
				String regNo = (String) row[4];

				if (olNo != null) {
					OlDetailDto dto = resultMapp.computeIfAbsent(olNo,
							key -> new OlDetailDto(supplyOrderNo, supplyOrderDate, olDate, olNo,   new ArrayList<>()));

					if (regNo != null)
						dto.getRegNos().add(regNo);

				} else
					System.out.println("olNo. is null, skipping record.");

			} catch (ClassCastException e) {
				System.err.println("Error casting row data: " + e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error accessing row elements: " + e.getMessage());
			} catch (Exception e) {
				System.err.println("An unexpected error occurred: " + e.getMessage());
			}
		}

		return new ArrayList<>(resultMapp.values());
	}

	public List<MergedOlDetDto> getMergedOlDetById(Integer mergedOlId) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		data = mergeSubSlipRepository.getMergedOlDetById(mergedOlId);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, MergedOlDetDto> resultMap = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				String mergedOlNo = (String) row[0];
				SubmissionSlip ol = submissionSlipRepository.findById((Integer) row[1]).get();
				String olNo = ol.getOlNumber();
				Integer olId = ol.getId();
				String submissionSlipNo = (String) row[2];

				String olDate = sdf.format((Date) row[3]);
				String mergedDate = sdf.format((Date) row[4]);
				String soNumber = (String) row[5];
				String soDate = (String) row[6];
				String grp = (String) row[7];
				Integer mergedOLId = (Integer)row[8];
				String regNo = (String)row[9];
				
				String Epcode = (String) row[10];
				String nomenclature = (String) row[11];
				if (olNo != null) {
					MergedOlDetDto dto = resultMap.computeIfAbsent(olNo,
							key -> new MergedOlDetDto(mergedOlNo, olNo, olId, submissionSlipNo,
									olDate, mergedDate, soNumber, soDate, grp, mergedOLId, new ArrayList<>(),Epcode, nomenclature));

					if (regNo != null)
						dto.getRegNos().add(regNo);

				} else
					System.out.println("iNoteno is null, skipping record.");
           
			} catch (ClassCastException e) {
				// Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings
				// or dates
				System.err.println("Error casting row data: " + e.getMessage());

			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error accessing row elements: " + e.getMessage());

			} catch (Exception e) {
				System.err.println("An unexpected error occurred: " + e.getMessage());
			}
		}

		return new ArrayList<>(resultMap.values());
	}


	public List<Map<String, Object>> getRegsByMergeSslipId(Integer mergeSslipId) {

		if(!mergeSubSlipRepository.existsById(mergeSslipId)) 
			throw new IllegalArgumentException("Merge SubSlip details not found for id: " + mergeSslipId);
		
		List<Object[]> regsData = mergeSubSlipRepository.getRegsByMergeSslipId(mergeSslipId);
		
		List<Map<String, Object>> returnMap = new ArrayList<>();
		
		regsData.forEach(reg -> {
			Map<String, Object> map = new HashMap<>();
			map.put("sslipSerialNoId", Integer.parseInt(reg[0].toString()));
			map.put("serialNo", String.valueOf(reg[1]));
			map.put("sslipLiId", Integer.parseInt(reg[2].toString()));
			map.put("roundId", Integer.parseInt(reg[3].toString()));
			map.put("productId", Integer.parseInt(reg[4].toString()));
			returnMap.add(map);
		});
		
		return returnMap;
		
	}

  public void updateMergeSubSLipStatus(Integer id, Integer statusId) {
    if (id != null && statusId != null) {
      MergeSubSlip mergeSubSlip = mergeSubSlipRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Lot details not found for id: " + id));

      Status status = statusRepositoy.findById(statusId)
          .orElseThrow(() -> new IllegalArgumentException("Status not found for id: " + statusId));
      mergeSubSlip.setStatus(status);

      mergeSubSlipRepository.save(mergeSubSlip);
    }
  }
  
  public void addToRfr(SubmissionSlip sSlip) {
	  List<Integer> failedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(Arrays.asList(sSlip.getId()), RegistrationConstants.FAIL);
	  
	  if (!failedSerialNos.isEmpty()) {
	      LineItemSerialNoDto dto = new LineItemSerialNoDto();
	      dto.setIds(failedSerialNos);
	      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.RFR);
	      lineItemSerialNoService.update(dto);
	
	      LineItemSerialNoHistoryDto failedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(failedSerialNos, SERIAL_NO_STATUS_CONSTANTS.RFR, sSlip.getCreatedBy(), sSlip.getId(), sSlip.getOlNumber());
	      lineItemSerialNoService.addHistory(failedRegsHistoryDto);
	    }
  }
	
}
