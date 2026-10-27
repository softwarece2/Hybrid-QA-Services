//package in.co.bel.olms.transaction.service;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import javax.transaction.Transactional;
//
//import org.dozer.Mapper;
//import org.springframework.dao.DataAccessException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import in.co.bel.olms.transaction.dto.INoteDetailDto;
//import in.co.bel.olms.transaction.dto.MergeINoteDetDto;
//import in.co.bel.olms.transaction.dto.MergeINoteDto;
//import in.co.bel.olms.transaction.dto.MergeINoteOlMappingDto;
//import in.co.bel.olms.transaction.dto.qar.sqae.QarCheckReportDto;
//import in.co.bel.olms.transaction.entity.InternalINote;
//import in.co.bel.olms.transaction.entity.MergeINote;
//import in.co.bel.olms.transaction.entity.MergeINoteOlMapping;
//import in.co.bel.olms.transaction.entity.OfferLetter;
//import in.co.bel.olms.transaction.repository.InternalINoteRepository;
//import in.co.bel.olms.transaction.repository.MergeINoteOlMappingRepository;
//import in.co.bel.olms.transaction.repository.MergeINoteRepository;
//import in.co.bel.olms.transaction.repository.OfferLetterRepository;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class MergeINoteService {
//
//	private final MergeINoteOlMappingRepository mergeINoteMappingRepository;
//
//	private final MergeINoteRepository mergeINoteRepository;
//
//	private final InternalINoteRepository internalINoteRepository;
//
//	private final Mapper mapper;
//
//	public ResponseEntity<String> saveMergeINote(MergeINoteDto reqDto) {
//		List<Integer> inoteIds = reqDto.getInoteIds();
//
//		if (inoteIds == null || inoteIds.isEmpty()) {
//			ResponseEntity.internalServerError().body("No inoteIds provided. Operation completed.");
//		}
//
//		MergeINote mergeInotes = mapper.map(reqDto, MergeINote.class);
//		if (reqDto.getMergeInoteNo() == null || reqDto.getMergeInoteNo().isEmpty()) {
//			throw new IllegalArgumentException("No mergeInote name provided. Can not save, Operation completed.");
//		}
//		mergeInotes.setMergeINoteNo(reqDto.getMergeInoteNo());
//		mergeInotes.setCreatedAt(new Date());
//
//		try {
//			MergeINote savedMergeInote = mergeINoteRepository.save(mergeInotes);
//			for (Integer inoteId : inoteIds) {
//
//				if (inoteId == null)
//					throw new IllegalArgumentException("Inote Id cannot be null");
//
//				InternalINote iNoteExist = internalINoteRepository.findById(inoteId).get();
//
//				if (iNoteExist == null)
//					throw new IllegalArgumentException("INote not found for the given Id: " + inoteId);
//
//				MergeINoteOlMapping mergeInoteMapping = new MergeINoteOlMapping();
//				mergeInoteMapping.setMergeINoteId(mergeINoteRepository.findById(savedMergeInote.getId()).get());
//				mergeInoteMapping.setINoteId(internalINoteRepository.findById(inoteId).get());
//				mergeInoteMapping.setIsMerged(true);
//				iNoteExist.setIsMerged(true);
//
//				MergeINoteOlMapping savedOlMapping = mergeINoteMappingRepository.save(mergeInoteMapping);
//
//			}
//			return ResponseEntity.ok("Data submitted successfully");
//		} catch (IllegalArgumentException e) {
//			System.err.println("Error processing INote IDs: " + e.getMessage());
//			return ResponseEntity.badRequest().body("Error processing INote IDs: " + e.getMessage()); // Bad Request
//		} catch (Exception e) {
//			System.err.println("An unexpected error occurred during the operation: " + e.getMessage());
//			return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
//		}
//
//	}
//
//	public List<MergeINoteDetDto> getAllMergedINotes() {
//		List<Object[]> data;
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		SimpleDateFormat sdfhr = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
//		data = mergeINoteRepository.getAllMergedINotes();
//
//		if (data == null || data.isEmpty()) {
//			return Collections.emptyList();
//		}
//		Map<String, MergeINoteDetDto> resultMap = new LinkedHashMap<>();
//
//		for (Object[] row : data) {
//			try {
//				String mergedInoteNo = (String) row[0];
//				InternalINote intINote = internalINoteRepository.findById((Integer) row[1]).get();
//				String iNoteno = intINote.getINoteNo();
//				Integer iNoteId = intINote.getId();
//				String submissionSlipNo = (String) row[2];
//				String subSlipDate = sdf.format((Date) row[3]);
//				String iNoteDate = sdf.format((Date) row[4]);
//				String mergedDate = sdfhr.format((Date) row[5]);
//				String soNumber = (String) row[6];
//				String soDate = (String) row[7];
//				String grp = (String) row[8];
//				Integer mergedINoteId = (Integer)row[9];
//				String regNo = (String) row[10];
//
//				if (iNoteno != null) {
//					MergeINoteDetDto dto = resultMap.computeIfAbsent(iNoteno,
//							key -> new MergeINoteDetDto(mergedInoteNo, iNoteno, iNoteId, submissionSlipNo, subSlipDate,
//									iNoteDate, mergedDate, soNumber, soDate, grp, mergedINoteId, new ArrayList<>()));
//
//					if (regNo != null)
//						dto.getRegNos().add(regNo);
//
//				} else
//					System.out.println("iNoteno is null, skipping record.");
//
//			} catch (ClassCastException e) {
//				// Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings
//				// or dates
//				System.err.println("Error casting row data: " + e.getMessage());
//
//			} catch (ArrayIndexOutOfBoundsException e) {
//				System.err.println("Error accessing row elements: " + e.getMessage());
//
//			} catch (Exception e) {
//				System.err.println("An unexpected error occurred: " + e.getMessage());
//			}
//		}
//
//		return new ArrayList<>(resultMap.values());
//	}
//
//	public List<INoteDetailDto> getAllINoteDetails(List<Integer> inoteIds) {
//		List<Object[]> data;
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		data = mergeINoteRepository.getAllINoteDetails(inoteIds);
//
//		if (data == null || data.isEmpty()) {
//			return Collections.emptyList();
//		}
//		Map<String, INoteDetailDto> resultMapp = new LinkedHashMap<>();
//
//		for (Object[] row : data) {
//			try {
//				String supplyOrderNo = (String) row[0];
//				String supplyOrderDate = sdf.format((Date) row[1]);
//				String submissionSlipNo = (String) row[2];
//				String submissionSlipDate = sdf.format((Date) row[3]);
//				String iNoteDate = sdf.format((Date) row[4]);
//				String iNoteno = (String) row[5];
//				String regNo = (String) row[6];
//
//				if (iNoteno != null) {
//					INoteDetailDto dto = resultMapp.computeIfAbsent(iNoteno,
//							key -> new INoteDetailDto(supplyOrderNo, supplyOrderDate, submissionSlipNo,
//									submissionSlipDate, iNoteDate, iNoteno, new ArrayList<>()));
//
//					if (regNo != null)
//						dto.getRegNos().add(regNo);
//
//				} else
//					System.out.println("iNoteno is null, skipping record.");
//
//			} catch (ClassCastException e) {
//				System.err.println("Error casting row data: " + e.getMessage());
//			} catch (ArrayIndexOutOfBoundsException e) {
//				System.err.println("Error accessing row elements: " + e.getMessage());
//			} catch (Exception e) {
//				System.err.println("An unexpected error occurred: " + e.getMessage());
//			}
//		}
//
//		return new ArrayList<>(resultMapp.values());
//	}
//
//	public List<MergeINoteDetDto> getMergedINoteDetById(Integer mergedInoteId) {
//		List<Object[]> data;
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		SimpleDateFormat sdfhr = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
//		data = mergeINoteRepository.getMergedINoteDetById(mergedInoteId);
//
//		if (data == null || data.isEmpty()) {
//			return Collections.emptyList();
//		}
//		Map<String, MergeINoteDetDto> resultMap = new LinkedHashMap<>();
//
//		for (Object[] row : data) {
//			try {
//				String mergedInoteNo = (String) row[0];
//				InternalINote intINote = internalINoteRepository.findById((Integer) row[1]).get();
//				String iNoteno = intINote.getINoteNo();
//				Integer iNoteId = intINote.getId();
//				String submissionSlipNo = (String) row[2];
//				String subSlipDate = sdf.format((Date) row[3]);
//				String iNoteDate = sdf.format((Date) row[4]);
//				String mergedDate = sdfhr.format((Date) row[5]);
//				String soNumber = (String) row[6];
//				String soDate = (String) row[7];
//				String grp = (String) row[8];
//				Integer mergedINoteId = (Integer)row[9];
//				String regNo = (String) row[10];
//
//				if (iNoteno != null) {
//					MergeINoteDetDto dto = resultMap.computeIfAbsent(iNoteno,
//							key -> new MergeINoteDetDto(mergedInoteNo, iNoteno, iNoteId, submissionSlipNo, subSlipDate,
//									iNoteDate, mergedDate, soNumber, soDate, grp, mergedINoteId, new ArrayList<>()));
//
//					if (regNo != null)
//						dto.getRegNos().add(regNo);
//
//				} else
//					System.out.println("iNoteno is null, skipping record.");
//
//			} catch (ClassCastException e) {
//				// Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings
//				// or dates
//				System.err.println("Error casting row data: " + e.getMessage());
//
//			} catch (ArrayIndexOutOfBoundsException e) {
//				System.err.println("Error accessing row elements: " + e.getMessage());
//
//			} catch (Exception e) {
//				System.err.println("An unexpected error occurred: " + e.getMessage());
//			}
//		}
//
//		return new ArrayList<>(resultMap.values());
//	}
//	
//}
