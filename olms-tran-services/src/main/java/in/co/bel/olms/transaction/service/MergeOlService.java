package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import in.co.bel.olms.transaction.dto.MergeOlDto;
import in.co.bel.olms.transaction.dto.MergedOlDetDto;
import in.co.bel.olms.transaction.dto.MergedSubSlipDto;
import in.co.bel.olms.transaction.dto.OLDetailsDto;
import in.co.bel.olms.transaction.entity.MergeOl;
import in.co.bel.olms.transaction.entity.MergeOlMapping;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.MergeOlMappingRepository;
import in.co.bel.olms.transaction.repository.MergeOlRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MergeOlService {

	private final MergeOlMappingRepository mergeOlMappingRepository;

	private final MergeOlRepository mergeOlRepository;

	private final OfferLetterRepository offerLetterRepository;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(MergeOlService.class);

	public ResponseEntity<String> saveMergeOl(MergeOlDto reqDto) {
		List<Integer> olIds = reqDto.getOlIds();

		if (olIds == null || olIds.isEmpty()) {
			ResponseEntity.internalServerError().body("No OlIds provided. Operation completed.");
		}

		MergeOl mergeOls = mapper.map(reqDto, MergeOl.class);
		if (reqDto.getMergeOlNo() == null || reqDto.getMergeOlNo().isEmpty()) {
			throw new IllegalArgumentException("No mergeOl name provided. Can not save, Operation completed.");
		}
		mergeOls.setMergeOlNo(reqDto.getMergeOlNo());
		mergeOls.setCreatedAt(new Date());

		try {
			MergeOl savedMergeOl = mergeOlRepository.save(mergeOls);
			for (Integer olId : olIds) {

				if (olId == null)
					throw new IllegalArgumentException("Inote Id cannot be null");

				OfferLetter olExist = offerLetterRepository.findById(olId).get();

				if (olExist == null)
					throw new IllegalArgumentException("Offer Letter not found for the given Id: " + olId);

				MergeOlMapping mergeOlMapping = new MergeOlMapping();
				mergeOlMapping.setMergeOlNoId(mergeOlRepository.findById(savedMergeOl.getId()).get());
				mergeOlMapping.setOfferLetterId(offerLetterRepository.findById(olId).get());
				mergeOlMapping.setIsMerged(true);
				olExist.setIsMerged(true);

			
	}
			return ResponseEntity.ok("Data submitted successfully");
		} catch (IllegalArgumentException e) {
			logger.error("Error processing INote IDs: {}" , e.getMessage());
			return ResponseEntity.badRequest().body("Error processing INote IDs: " + e.getMessage()); // Bad Request
		} catch (Exception e) {
			logger.error("An unexpected error occurred during the operation: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
		}

	}

	public List<MergedSubSlipDto> getAllMergedOls() {
		List<Object[]> data;
		SimpleDateFormat sdfhr = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		data = mergeOlRepository.getAllMergedOls();

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		List<MergedSubSlipDto> mergedOlDetDtos = new ArrayList<>();
		for (Object[] row : data) {
			try {
				MergedSubSlipDto mergedOlDetDto = new MergedSubSlipDto();
				mergedOlDetDto.setMergedOlNo((String) row[0]);

				mergedOlDetDto.setOlNos((String)row[1]);
				
				mergedOlDetDto.setSubmissionSlipNo((String) row[2]);

				mergedOlDetDto.setMergedDate(sdfhr.format((Date) row[3]));
				mergedOlDetDto.setSoNumber((String) row[4]); 
				mergedOlDetDto.setSoDate((String) row[5]);
				mergedOlDetDto.setGrp((String) row[6]); 
				mergedOlDetDto.setMergedOlId((Integer)row[7]); 
				mergedOlDetDto.setOrganizationTypeId((Integer)  row[9]);
				mergedOlDetDto.setInspectionAuthority((String) row[10]);
				mergedOlDetDtos.add(mergedOlDetDto);


//				if (olNo != null) {
//					MergedOlDetDto dto = resultMap.computeIfAbsent(olNo,
//							key -> new MergedOlDetDto(mergedOlNo, olNo, olId, submissionSlipNo, olDate,
//									 mergedDate, soNumber, soDate, grp, mergedINoteId, new ArrayList<>()));
//
//					if (regNo != null)
//						dto.getRegNos().add(regNo);
//
//				} else
//					System.out.println("iNoteno is null, skipping record.");

			} catch (ClassCastException e) {
				// Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings
				// or dates
				logger.error("Error casting row data in getting all mergedOls: {}", e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting all mergedOls: {}", e.getMessage());
			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting all mergedOls: {}", e.getMessage());
			}
		}

		return mergedOlDetDtos;
	}

	public List<OLDetailsDto> getAllOlDetails(List<Integer> olIds) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		// check if same EpCode and Inspection Agency
		 boolean allSamePartNoAndInspAuth = olIds.stream()
		            .map(subSlipId -> {

		                SubmissionSlip subSlip = submissionSlipRepository.findById(subSlipId)
		                        .orElseThrow(() -> new IllegalArgumentException(
		                                "SubmissionSlip not found: " + subSlipId));

		                Integer partNo = subSlip.getWarrant()
		                        .getSoLineItem()
		                        .getPartNumber()
		                        .getId();

		                Integer inspAuth = subSlip.getWarrant()
		                        .getSoLineItem()
		                        .getInspectionAuthorityId()
		                        .getId();

		                return partNo + "-" + inspAuth;
		            })
		            .distinct()
		            .count() == 1;

		    if (!allSamePartNoAndInspAuth) {
		        throw new IllegalArgumentException(
		                "Submission Slips must have the same Part Number and Inspection Agency.");
		    }

		data = mergeOlRepository.getAllOlDetails(olIds);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, OLDetailsDto> resultMapp = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				String supplyOrderNo = (String) row[0];
				String supplyOrderDate = sdf.format((Date) row[1]);
				
				String olNo = (String) row[2];
				String olDate = sdf.format((Date) row[3]);
				Integer subSlipId = Integer.parseInt(row[5].toString());
				String epCode=(String) row[6];
				String regNos = (String) row[4];
				String nomenclature = (String)row[7];
				List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(olIds.get(0));
				String secCode = null;
				Integer deptId = null;
				if(obj!=null) {
					for(Object[] det: obj) {
						deptId = det[0]!=null? (Integer)det[0]: null ;
					secCode = det[2]!=null? det[2].toString(): null;
					}
				}
				String fY = getFinancialYearStrForLot();
				Integer count = mergeOlRepository.getLotCountRows(deptId);
				count = (count == null) ? 0 : count;

				String lotCount = String.format("%05d", count + 1);
				String lotNo = "L" + secCode + fY + lotCount;
				Integer registrationNo = (Integer)row[8];
				if (olNo != null) {
					OLDetailsDto dto = resultMapp.computeIfAbsent(olNo, key -> new OLDetailsDto(supplyOrderNo,
							supplyOrderDate,olDate, olNo, regNos, lotNo, subSlipId,epCode,nomenclature, registrationNo));
				} else
					logger.info("olNo. is null, skipping record.");
			} catch (ClassCastException e) {
				logger.error("Error casting row data in getting all olDetails: {}", e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting all olDetails: {}", e.getMessage());
			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting all olDetails: {}", e.getMessage());
			}
		}
		return new ArrayList<>(resultMapp.values());
	  }

	public List<MergedOlDetDto> getMergedOlDetById(Integer mergedOlId) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdfhr = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		data = mergeOlRepository.getMergedOlDetById(mergedOlId);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, MergedOlDetDto> resultMap = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				String mergedOlNo = (String) row[0];
				OfferLetter ol = offerLetterRepository.findById((Integer) row[1]).get();
				String olNo = ol.getOlNumber();
				Integer olId = ol.getId();
				String submissionSlipNo = (String) row[2];
				String olDate = sdf.format((Date) row[3]);
				String mergedDate = sdfhr.format((Date) row[4]);
				String soNumber = (String) row[5];
				String soDate = (String) row[6];
				String grp = (String) row[7];
				Integer mergedOLId = (Integer)row[8];
				String regNo = (String) row[9];
				String Epcode = (String) row[10];
				String nomenclature = (String) row[11];
				Integer registrationNo = (Integer) row[12];
				if (olNo != null) {
					MergedOlDetDto dto = resultMap.computeIfAbsent(olNo,
							key -> new MergedOlDetDto(mergedOlNo, olNo, olId, submissionSlipNo,
									olDate, mergedDate, soNumber, soDate, grp, mergedOLId , new ArrayList<>(),Epcode, nomenclature, registrationNo));
					if (regNo != null)
						dto.getRegNos().add(regNo);

				} else
					logger.info("iNoteno is null, skipping record.");

			} catch (ClassCastException e) {
				// Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings
				// or dates
				logger.error("Error casting row data in getting MergedOlDetById: {}", e.getMessage());

			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting MergedOlDetById : {}", e.getMessage());

			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting MergedOlDetById: {}", e.getMessage());
			}
		}

		return new ArrayList<>(resultMap.values());
	}
	private static String getFinancialYearStrForLot() {
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
		 return String.valueOf(year%100);
	}

}
