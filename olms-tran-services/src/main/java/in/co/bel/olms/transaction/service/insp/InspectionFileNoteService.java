package in.co.bel.olms.transaction.service.insp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.*;
import in.co.bel.olms.transaction.dto.insp.FileNoteINoteDto;
import in.co.bel.olms.transaction.dto.insp.InspectionFileNoteDto;
import in.co.bel.olms.transaction.entity.*;
import in.co.bel.olms.transaction.repository.OlApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.insp.InspectionFileNoteRepository;
import in.co.bel.olms.transaction.service.*;
import in.co.bel.olms.transaction.service.ol.OlApprovalDetailsService;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InspectionFileNoteService {

	private final SupplyOrderService supplyOrderService;

	private final OfferLetterService offerLetterService;

	private final OlLineItemService olLineItemService;

	private final DdExtensionService ddExtensionService;

	private final OlApprovalDetailsService olApprovalService;

	private final InspectionFileNoteRepository inspectionFileNoteRepository;

	private final QinObsNcFormService qinObsNcFormService;

	private final EmployeeRepository employeeRepository;

	private final OlApprovalDetailsRepository olApprovalDetailsRepository;

	final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(InspectionFileNoteService.class);

	private final Mapper mapper;

	public InspectionFileNoteDto getInspFileNoteBySoId(int soid) {
		InspectionFileNoteDto fileNote = new InspectionFileNoteDto();
		OfferLetterDto olDto = new OfferLetterDto();// = mapper.map(offerLetter, OfferLetterDto.class);
		fileNote.setOlDto(olDto);
		// SupplyOrder supplyOrder= offerLetter.getsupplyorder

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soid);
		SupplyOrderDto soDto = mapper.map(supplyOrder, SupplyOrderDto.class);
		soDto.setOrderDateStr(outputDateFormat.format(supplyOrder.getOrderDate()));

		fileNote.setSoDto(soDto);
		return fileNote;
	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return ResponseEntity<InspectionFileNoteDto>
	 */
	public ResponseEntity<InspectionFileNoteDto> getInspFileNoteByOlId(int olId) {
		InspectionFileNote inspectionFileNote = inspectionFileNoteRepository.findByOfferLetterId(olId);
		InspectionFileNoteDto fileNoteDto = new InspectionFileNoteDto();
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);
		try {

			// getting previous file note by so id if exists
			if (inspectionFileNote != null) {
				fileNoteDto = mapper.map(inspectionFileNote, InspectionFileNoteDto.class);
				if (inspectionFileNote.getCqaoApprovalDate() != null) {
					fileNoteDto.setCqaoApprovalDate(inspectionFileNote.getCqaoApprovalDate());
				}
				fileNoteDto.setIsSubmitted(true);
			}

			// getting values for inspector (first time)
			else {
				OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
				SaleOrder saleOrder = offerLetter.getSaleOrder();
				SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
				Project project = saleOrder.getCategory().getShip().getProject();

				// Supply Order
				fileNoteDto.setIsSubmitted(false);
				fileNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
				fileNoteDto.setFileNumber(supplyOrder.getFileNo());
				fileNoteDto.setLotSizeOriginal(String.valueOf(supplyOrder.getLotNo()));
				fileNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
				fileNoteDto.setOriginalDeliverydate(supplyOrder.getDd());
				if (project != null)
					fileNoteDto.setProjectDescription(project.getProjectName());

				// Offer Letter
				fileNoteDto.setOfferLetterId(offerLetter.getId());
				fileNoteDto.setOlNumber(offerLetter.getOlNumber());
				fileNoteDto.setOlDate(offerLetter.getOfferDate());

				// SO Line items
				List<Object[]> lineItemsPoResponse = offerLetterService
						.getLineItemsPoQtyGroupByUom(supplyOrder.getId());
				String lineItemsPoString = getQtyString(supplyOrder.getSoLineItems().size(), lineItemsPoResponse);
				fileNoteDto.setTotalLineItemsPo(lineItemsPoString);
				fileNoteDto.setTotalValueLineItemsPo(df.format(supplyOrder.getValue()));

				// OL Line Items
				List<Object[]> lineItemsOlResponse = offerLetterService.getLineItemsQtyGroupByUom(olId);
				String noLineItemsString = getQtyString(offerLetter.getOlLineItems().size(), lineItemsOlResponse);
				fileNoteDto.setNoLineItemsOl(noLineItemsString);

				BigDecimal olLineItemValue = BigDecimal.ZERO;
				for (OlLineItem olLineItem : offerLetter.getOlLineItems()) {
					olLineItemValue = olLineItemValue.add(olLineItem.getOfferedQty().multiply(olLineItem.getSoLineItem().getUnitCost()));
				}
				fileNoteDto.setValueOfferedOl(df.format(olLineItemValue));

				// Supplied OL Items: so_line_items supplied till date
				List<Integer> suppliedSoLiList = offerLetterService.getSuppliedLineItemListBySoId(supplyOrder.getId());
				List<Object[]> suppliedOlQty = offerLetterService.getSoLineItemsQtyWithListGroupByUom(suppliedSoLiList);
				String suppliedOlString = getQtyString(suppliedSoLiList.size(), suppliedOlQty);
				fileNoteDto.setNoLineItemsSupplied(suppliedOlString);

				// Cleared OL Line Items
				Set<Integer> olLineItemIdList = offerLetter.getOlLineItems().stream().map(OlLineItem::getId)
						.collect(Collectors.toSet());
				Set<Integer> clearedOlLineItemsIds = offerLetterService.getClearedOlLineItemsByOlId(olLineItemIdList);
				List<Object[]> clearedOlLineItemsQty = offerLetterService
						.getClearedLineItemQtyGroupByUom(clearedOlLineItemsIds);
				fileNoteDto.setNoLineItemsClrdOl(getQtyString(clearedOlLineItemsIds.size(), clearedOlLineItemsQty));

				List<OlLineItem> clearedLineItems = olLineItemService.getByIdList(clearedOlLineItemsIds);
				BigDecimal clearedValue = BigDecimal.ZERO;
				for (OlLineItem cleared : clearedLineItems) {
					clearedValue = clearedValue.add(cleared.getOfferedQty().multiply(cleared.getSoLineItem().getUnitCost()))  ;
				}
				fileNoteDto.setValueClrdOl(df.format(clearedValue));

				// DD Extension
				List<DdExtensionDto> ddExtensions = ddExtensionService.getLast2DdExtnBySoId(supplyOrder.getId());
				List<Date> ddExtnSortedAsc = ddExtensions.stream().map(DdExtensionDto::getNewDdDate)
						.sorted(Comparator.reverseOrder()).toList();
				if (!ddExtnSortedAsc.isEmpty())
					fileNoteDto.setExtendedDd1(ddExtnSortedAsc.get(0));
				if (ddExtnSortedAsc.size() > 1)
					fileNoteDto.setExtendedDd1(ddExtnSortedAsc.get(1));
			}

			return ResponseEntity.ok(fileNoteDto);

		} catch (Exception e) {
			logger.error("Error in getting InspFileNoteByOlId {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	private String getQtyString(int no, List<Object[]> lineItemsOlResponse) {
		StringBuilder bld = new StringBuilder();
		bld.append(no);
		for (Object[] obj : lineItemsOlResponse) {
			if (lineItemsOlResponse.indexOf(obj) == 0)
				bld.append(" (");

			// [0]: uom; [1]:value
			bld.append(obj[1] != null ? obj[1].toString() : null);
			bld.append(" ");
			bld.append(obj[0] != null ? obj[0].toString() : null);

			if (lineItemsOlResponse.indexOf(obj) == lineItemsOlResponse.size() - 1)
				bld.append(")");
			else
				bld.append(" + ");
		}
		return bld.toString();
	}

	/**
	 * @author Shubham Anand
	 * @param inspectionFileNoteDtoStr
	 * @return ResponseEntity<String>
	 */
	public String saveInspectionFileNote(String inspectionFileNoteDtoStr) {
		Gson gson = new Gson();
		try {
			InspectionFileNoteDto inspectionFileNoteDto = gson.fromJson(inspectionFileNoteDtoStr,
					new TypeToken<InspectionFileNoteDto>() {
					}.getType());
			InspectionFileNote inspectionFileNote = mapper.map(inspectionFileNoteDto, InspectionFileNote.class);
			if (inspectionFileNoteDto.getIsSubmitted())
				inspectionFileNote.setCreatedDatetime(new Date());
			inspectionFileNoteRepository.save(inspectionFileNote);

			return ApplicationConstants.SUCCESS;
		} catch (MappingException e) {
			logger.error("Error is saving inspection file note: {}", e.getMessage());
			return ApplicationConstants.RETURN_INVALID_INPUT;
		} catch (Exception e) {
			logger.error("Error is saving inspection file note: {}", e.getMessage());
			return ApplicationConstants.FAILURE;
		}
	}

	public ResponseEntity<InspectionFileNoteDto> getInspFileNoteByOlIds(List<Integer> olIds) {

		InspectionFileNote inspectionFileNote = inspectionFileNoteRepository.findByOfferLetterIds(olIds);
		InspectionFileNoteDto fileNoteDto = new InspectionFileNoteDto();
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);
		try {

			// getting previous file note by so id if exists
			if (inspectionFileNote != null) {
				fileNoteDto = mapper.map(inspectionFileNote, InspectionFileNoteDto.class);
				if (inspectionFileNote.getCqaoApprovalDate() != null) {
					fileNoteDto.setCqaoApprovalDate(inspectionFileNote.getCqaoApprovalDate());
				}
				// fileNoteDto.setIsSubmitted(fileNoteDto.get);
			}

			// getting values for inspector (first time)
			else {
				SupplyOrder supplyOrder = offerLetterService.getSupplyOrderByOlIds(olIds);

				List<Project> projects = supplyOrder.getProjects();
				String allProjectNames = "";
				if (!projects.isEmpty()) {

					allProjectNames = projects.stream().map(Project::getProjectName).collect(Collectors.joining(","));
				}

				// Supply Order
				fileNoteDto.setIsSubmitted(false);
				fileNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
				fileNoteDto.setFileNumber(supplyOrder.getFileNo());
				fileNoteDto.setLotSizeOriginal(String.valueOf(supplyOrder.getLotNo()));
				fileNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
				fileNoteDto.setOriginalDeliverydate(supplyOrder.getDd());
				fileNoteDto.setProjectDescription(allProjectNames);

				// Setting Offer Letter Data
				fileNoteDto.setOfferLetterIds(olIds);
				List<OfferLetter> offerLetters = offerLetterService.getOfferLettersByIds(olIds);
				List<OfferLetterDto> dtos = new ArrayList<>();
				for (OfferLetter ol : offerLetters) {
					OfferLetterDto dto = new OfferLetterDto();
					dto.setId(ol.getId());
					dto.setOlNumber(ol.getOlNumber());
					dtos.add(dto);

				}
				fileNoteDto.setOls(dtos);
				String olNumbers = offerLetters.stream().map(OfferLetter::getOlNumber).collect(Collectors.joining(","));
				fileNoteDto.setOlNumber(olNumbers);

				String offerDateStr = "";
				Set<String> offerDateSet = new HashSet<>();
				int lineItemCountInOLs = 0;
				BigDecimal olLineItemValue = BigDecimal.ZERO;
				Set<Integer> olLineItemIdList = new HashSet<>();
				Date latestOfferDate = null;
				for (OfferLetter offerLetter : offerLetters) {
					if (offerLetter.getOfferDate() != null) {
						Date offerDate = offerLetter.getOfferDate();
						if (latestOfferDate == null || offerDate.after(latestOfferDate)) {
							latestOfferDate = offerDate;
						}
						offerDateSet.add(outputDateFormat.format(offerDate));
					}
					lineItemCountInOLs += offerLetter.getOlLineItems().size();
					for (OlLineItem olLineItem : offerLetter.getOlLineItems()) {
						olLineItemValue = olLineItemValue.add(olLineItem.getOfferedQty().multiply(olLineItem.getSoLineItem().getUnitCost())) ;
						olLineItemIdList.add(olLineItem.getId());
					}
				}
				fileNoteDto.setOlDate(latestOfferDate);
				fileNoteDto.setOlDateStr(String.join(",", offerDateSet));
				// SO Line items
				List<Object[]> lineItemsPoResponse = offerLetterService
						.getLineItemsPoQtyGroupByUom(supplyOrder.getId());
				String lineItemsPoString = getQtyString(supplyOrder.getSoLineItems().size(), lineItemsPoResponse);
				fileNoteDto.setTotalLineItemsPo(lineItemsPoString);
				fileNoteDto.setTotalValueLineItemsPo(df.format(supplyOrder.getValue()));

				// OL Line Items
				List<Object[]> lineItemsOlResponse = offerLetterService.getLineItemQtyNUomByOlIds(olIds);
				String noLineItemsString = getQtyString(lineItemCountInOLs, lineItemsOlResponse);
				fileNoteDto.setNoLineItemsOl(noLineItemsString);

				fileNoteDto.setValueOfferedOl(df.format(olLineItemValue));

				// Supplied OL Items: so_line_items supplied till date
				List<Integer> suppliedSoLiList = offerLetterService.getSuppliedLineItemListBySoId(supplyOrder.getId());
				List<Object[]> suppliedOlQty = offerLetterService.getSoLineItemsQtyWithListGroupByUom(suppliedSoLiList);
				String suppliedOlString = getQtyString(suppliedSoLiList.size(), suppliedOlQty);
				fileNoteDto.setNoLineItemsSupplied(suppliedOlString);

				// Cleared OL Line Items
				Set<Integer> clearedOlLineItemsIds = offerLetterService.getClearedOlLineItemsByOlId(olLineItemIdList);
				List<Object[]> clearedOlLineItemsQty = offerLetterService
						.getClearedLineItemQtyGroupByUom(clearedOlLineItemsIds);
				fileNoteDto.setNoLineItemsClrdOl(getQtyString(clearedOlLineItemsIds.size(), clearedOlLineItemsQty));

				List<OlLineItem> clearedLineItems = olLineItemService.getByIdList(clearedOlLineItemsIds);
				BigDecimal clearedValue = BigDecimal.ZERO;
				for (OlLineItem cleared : clearedLineItems) {
					clearedValue = clearedValue.add(cleared.getOfferedQty().multiply(cleared.getSoLineItem().getUnitCost())) ;
				}
				fileNoteDto.setValueClrdOl(df.format(clearedValue));

				// DD Extension
				List<DdExtensionDto> ddExtensions = ddExtensionService.getLast2DdExtnBySoId(supplyOrder.getId());
				List<Date> ddExtnSortedAsc = ddExtensions.stream().map(DdExtensionDto::getNewDdDate)
						.sorted(Comparator.reverseOrder()).toList();
				if (!ddExtnSortedAsc.isEmpty())
					fileNoteDto.setExtendedDd1(ddExtnSortedAsc.get(0));
				if (ddExtnSortedAsc.size() > 1)
					fileNoteDto.setExtendedDd1(ddExtnSortedAsc.get(1));

				for (int olId : olIds) {
					QinObsNcFormDto formDto = new QinObsNcFormDto();
					formDto = qinObsNcFormService.getQinNcFormDetails(olId);
					// System.out.print("ehfhsegfef" +formDto.getIsQinClosed());
					if (formDto != null) {
						boolean isQinCompleted = formDto.getIsQinClosed();
						if (!isQinCompleted) {
							fileNoteDto.setIsQinCompleted(isQinCompleted);
							// return ResponseEntity.internalServerError().body(null);
						}
					}
				}
			}

			return ResponseEntity.ok(fileNoteDto);

		} catch (Exception e) {
			logger.error("Error in getting InspFileNoteByOlIds {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}

	}

	public OlApprovalDto saveInspectionFileNoteV2(String inspectionFileNoteDtoStr, String olApprovalDtoStr,
			MultipartFile[] uploads, String role) {
		Gson gson = new Gson();
		OlApprovalDto olApprovalDto = null;
		try {
			if (inspectionFileNoteDtoStr != null) {
				InspectionFileNoteDto inspectionFileNoteDto = gson.fromJson(inspectionFileNoteDtoStr,
						new TypeToken<InspectionFileNoteDto>() {
						}.getType());
				String completedLotSize = inspectionFileNoteDto.getCompletedLotSize();
				String totalInotesIssued = inspectionFileNoteDto.getTotalInotesCcIssued();
				String noteNo = inspectionFileNoteDto.getNoteNo();
				String qapRef = inspectionFileNoteDto.getQapRef();
				
				if (StringUtils.isNotBlank(completedLotSize)) {
					if (!InputNameValidation.checkString(completedLotSize)) {
						throw new DataNotValidException("Invalid characters in completed lot size");
					}
				}
				
				if (StringUtils.isNotBlank(totalInotesIssued)) {
					if (!InputNameValidation.checkString(totalInotesIssued)) {
						throw new DataNotValidException("Invalid characters in total inotes issued");
					}
				}
				
				if (StringUtils.isNotBlank(noteNo)) {
					if (!InputNameValidation.checkNumbers(noteNo)) {
						throw new DataNotValidException("Invalid characters in note no");
					}
				}							

				if (StringUtils.isNotBlank(qapRef)) {
					if (!InputNameValidation.checkString(qapRef)) {
						throw new DataNotValidException("Invalid characters in QapRef");
					}
				}

				if (inspectionFileNoteDto.getCqaoApprovalDate() == null
						&& !inspectionFileNoteDto.getIsFileNoteIdPresent()) {
					InspectionFileNote inspectionFileNote = mapper.map(inspectionFileNoteDto, InspectionFileNote.class);

					List<OfferLetter> offerLetters = offerLetterService
							.getOfferLettersByIds(inspectionFileNoteDto.getOfferLetterIds());
					inspectionFileNote.setOfferLetters(offerLetters);
					if (inspectionFileNoteDto.getIsSubmitted()) {
						inspectionFileNote.setCreatedDatetime(new Date());

					}
					inspectionFileNote.setSupplyOrderId(inspectionFileNoteDto.getSupplyOrderId());
					InspectionFileNote savedFileNote = inspectionFileNoteRepository.save(inspectionFileNote);

					logger.info("Updating the savedFileNote");

					olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
					}.getType());
					olApprovalDto.setInspectionFileNoteId(savedFileNote.getId());
					olApprovalDto.setNoteNo(inspectionFileNoteDto.getNoteNo());
					
//					String remarks = olApprovalDto.getRemarks();
					
//					if (remarks == null || remarks.isEmpty()) {
//						throw new DataNotValidException("Remarks are required.");
//					}													
					
						if(!InputNameValidation.isHtmlSafe(olApprovalDto.getRemarks())) {
							throw new DataNotValidException("Invalid remarks");
						}
					
					if (inspectionFileNoteDto.getIsSubmitted()) {
						if (olApprovalDto.getInspectionFileNoteId() == null) {
							olApprovalDto.setInspectionFileNoteId(savedFileNote.getId());
							inspectionFileNote.setIsSubmitted(true);
						}
						// Updating the OL Status on successful inspection file note save by inspector
						logger.info("Updating the OL Status");
						offerLetterService.updateOlStatusV2(olApprovalDto.getInspectionFileNoteId(),
								ApplicationConstants.OL_COMPLETED);
						olApprovalService.updateFileNoteApproval(olApprovalDto, uploads, role);
					}

					return olApprovalDto;
				} else if (inspectionFileNoteDto.getCqaoApprovalDate() == null
						&& inspectionFileNoteDto.getIsFileNoteIdPresent()) {

					Optional<InspectionFileNote> savedFileNote = inspectionFileNoteRepository
							.findById(inspectionFileNoteDto.getId());
					logger.info("Updating OL Status {}", inspectionFileNoteDto.getId());
					if (savedFileNote != null) {
						InspectionFileNote fileNote = savedFileNote.get();
						int soId = fileNote.getSupplyOrderId();

						olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
						}.getType());
						olApprovalDto.setInspectionFileNoteId(fileNote.getId());
						//String remarks = olApprovalDto.getRemarks();
						
//						if (remarks == null || remarks.isEmpty()) {
//							throw new DataNotValidException("Remarks are required.");
//						}
//														
//						if(StringUtils.isNotBlank(olApprovalDto.getRemarks())) {
//							if(!InputNameValidation.isHtmlSafe(olApprovalDto.getRemarks())) {
//								throw new DataNotValidException("Invalid remarks");
//							}
//						}
						
						// Increment note_no
//						Integer latestNoteNo = olApprovalDetailsRepository.getLatestNoteNoByFileNote(fileNote.getId());
//						if(latestNoteNo!=null)
//						latestNoteNo = latestNoteNo + 1;
//						olApprovalDto.setNoteNo(latestNoteNo!=null?String.valueOf(latestNoteNo):null);

						mapper.map(inspectionFileNoteDto, fileNote);
						fileNote.setSupplyOrderId(soId);
						if (inspectionFileNoteDto.getIsSubmitted()) {
							fileNote.setCreatedDatetime(new Date());
							if (fileNote.getIsSubmitted()) {
								Integer latestNoteNo = olApprovalDetailsRepository
										.getLatestNoteNoByFileNote(inspectionFileNoteDto.getId());
								if (olApprovalDto.getNoteNo() == null && latestNoteNo != null) {
									latestNoteNo = latestNoteNo + 1;
								}
								olApprovalDto.setNoteNo(
										latestNoteNo != null ? String.valueOf(latestNoteNo) : fileNote.getNoteNo());
								// olApprovalDto.setNoteNo(fileNote.getNoteNo());
							}
							fileNote.setIsSubmitted(true);

						}
						inspectionFileNoteRepository.save(fileNote);
						if (inspectionFileNoteDto.getIsSubmitted()) {
							olApprovalService.updateFileNoteApproval(olApprovalDto, uploads, role);
							offerLetterService.updateOlStatusV2(olApprovalDto.getInspectionFileNoteId(),
									ApplicationConstants.OL_COMPLETED);
						}
					}

					return olApprovalDto;
				} else {

					olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
					}.getType());
					// Increment note_no
					Integer latestNoteNo = olApprovalDetailsRepository
							.getLatestNoteNoByFileNote(inspectionFileNoteDto.getId());
					olApprovalDto.setNoteNo(latestNoteNo != null ? String.valueOf(latestNoteNo + 1) : null);
					olApprovalService.updateFileNoteApproval(olApprovalDto, uploads, role);
					// updating the CQAO approval date in case of approval by CQAO.
					// inspectionFileNoteDtoStr shall be null in this case.
					
					String remarks = olApprovalDto.getRemarks();
					
					if (remarks == null || remarks.isEmpty()) {
						throw new DataNotValidException("Remarks are required.");
					}
													
					if(StringUtils.isNotBlank(olApprovalDto.getRemarks())) {
						if(!InputNameValidation.isHtmlSafe(olApprovalDto.getRemarks())) {
							throw new DataNotValidException("Invalid remarks");
						}
					}
					
					if (inspectionFileNoteDto.getCqaoApprovalDate() != null) {
						Optional<InspectionFileNote> savedFileNote = inspectionFileNoteRepository
								.findById(olApprovalDto.getInspectionFileNoteId());
						if (savedFileNote.isPresent()) {
							InspectionFileNote fileNote = savedFileNote.get();
							fileNote.setCqaoApprovalDate(olApprovalDto.getCqaoApprovalDate());
							fileNote.setIsSubmitted(true);
							olApprovalDto.setInspectionFileNoteId(fileNote.getId());
							inspectionFileNoteRepository.save(fileNote);
						}
					}
					return olApprovalDto;
				}

			} else {

				olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
				}.getType());
				// Increment note_no
				if (olApprovalDto.getNoteNo() == null) {
					Integer latestNoteNo = olApprovalDetailsRepository
							.getLatestNoteNoByFileNote(olApprovalDto.getInspectionFileNoteId());
					olApprovalDto.setNoteNo(latestNoteNo != null ? String.valueOf(latestNoteNo + 1) : null);
				}
				
				String remarks = olApprovalDto.getRemarks();
				
				if (remarks == null || remarks.isEmpty()) {
					throw new DataNotValidException("Remarks are required.");
				}
												
				if(StringUtils.isNotBlank(olApprovalDto.getRemarks())) {
					if(!InputNameValidation.isHtmlSafe(olApprovalDto.getRemarks())) {
						throw new DataNotValidException("Invalid remarks");
					}
				}
				
				olApprovalService.updateFileNoteApproval(olApprovalDto, uploads, role);
				// updating the CQAO approval date in case of approval by CQAO.
				// inspectionFileNoteDtoStr shall be null in this case.

				Optional<InspectionFileNote> savedFileNote = inspectionFileNoteRepository
						.findById(olApprovalDto.getInspectionFileNoteId());
				if (savedFileNote.isPresent()) {
					InspectionFileNote fileNote = savedFileNote.get();
					fileNote.setCqaoApprovalDate(olApprovalDto.getCqaoApprovalDate());
					olApprovalDto.setInspectionFileNoteId(fileNote.getId());
					fileNote.setIsSubmitted(true);
					inspectionFileNoteRepository.save(fileNote);

				}
				return olApprovalDto;
			}

		} catch (ResponseStatusException re) {
			logger.error("Error: {}", re.getMessage());
			throw re;
		} catch (MappingException e) {
			logger.error("Error in saving inspection file note: {}", e.getMessage());
			return olApprovalDto;
		} catch (DataNotValidException de) {
			logger.error("Validation failed: {}", de.getMessage());
			throw de;
		} catch (Exception e) {
			logger.error("Error in saving inspection file note: {}", e.getMessage());
			return olApprovalDto;
		}
	}

	public List<InspectionFileNoteDto> getAllFileNotes(int orgId) {

		List<Object[]> fileNotes = inspectionFileNoteRepository.findFileNotes();
		// List<InspectionFileNoteDto> fileNoteDto = new ;
		List<InspectionFileNoteDto> fileNoteDtos = new LinkedList<>();
//		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
//		df.setMaximumFractionDigits(2);
		{

			for (Object[] objects : fileNotes) {
				InspectionFileNoteDto inspectionFileNoteDto = new InspectionFileNoteDto();
				inspectionFileNoteDto.setId((int) objects[0]);
				inspectionFileNoteDto.setOlNumber(objects[1].toString());
				inspectionFileNoteDto.setSupplyOrderNo(objects[2] != null ? objects[2].toString() : null);
				inspectionFileNoteDto.setSalesGrpName(objects[3] != null ? objects[3].toString() : null);
				inspectionFileNoteDto.setProjectDescription(objects[4] != null ? objects[4].toString() : null);
				if (objects[5] != null) {
					inspectionFileNoteDto.setOlDateStr(objects[5].toString());
				}
				// inspectionFileNoteDto.setOlDate(olDate);

				inspectionFileNoteDto.setFileNoteStatus(objects[6] != null ? objects[6].toString() : "null");
				inspectionFileNoteDto.setSupplyOrderId(objects[7] != null ? (int) objects[7] : null);
				inspectionFileNoteDto.setOlDescription(objects[8] != null ? objects[8].toString() : null);
				// Integer
				// clearanceId=clearanceLetterRepository.getByOlId(offerLetterDto.getId());

				fileNoteDtos.add(inspectionFileNoteDto);

			}
//			Collections.sort(fileNoteDtos, new FileNoteStatusComparator());;
			return fileNoteDtos;

		}
	}

	// getFileNoteById

	public InspectionFileNoteDto getFileNoteById(int olIds) {
		Optional<InspectionFileNote> inspectionFileNote = inspectionFileNoteRepository.findById(olIds);
		InspectionFileNoteDto fileNoteDto = null;
		try {
			if (inspectionFileNote.isPresent()) {
				InspectionFileNote fileNote = inspectionFileNote.get();
				fileNoteDto = mapper.map(fileNote, InspectionFileNoteDto.class);
				List<Integer> offerLetterIds = fileNote.getOfferLetters().stream().map(OfferLetter::getId)
						.toList();
				fileNoteDto.setOfferLetterIds(offerLetterIds);
				List<OfferLetterDto> dtos = new ArrayList<>();
				List<OfferLetter> offerLetters = fileNote.getOfferLetters();
				OfferLetter offerLetter = offerLetters.get(0);
				if (offerLetter.getSaleOrder() != null && offerLetter.getSaleOrder().getSupplyOrder() != null
						&& offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup() != null) {
					String salesGroupName = offerLetters.get(0).getSaleOrder().getSupplyOrder().getSalesGroup()
							.getName();
					fileNoteDto.setSalesGrpName(salesGroupName);
				}
				for (OfferLetter ol : offerLetters) {
					OfferLetterDto dto = new OfferLetterDto();
					dto.setId(ol.getId());
					dto.setOlNumber(ol.getOlNumber());
					dtos.add(dto);

				}
				fileNoteDto.setOls(dtos);
				fileNoteDto.setSupplyOrderId(fileNote.getSupplyOrderId());
//			fileNoteDto.setIsSubmitted(true);
			}
		} catch (Exception e) {
			logger.error("not found: {}", e.getMessage());
		}
		return fileNoteDto;
	}

	public OlApprovalDto updateInspectionFileNoteV2(String inspectionFileNoteDtoStr, String olApprovalDtoStr) {
		Gson gson = new Gson();
		OlApprovalDto olApprovalDto = null;
		try {
			if (inspectionFileNoteDtoStr != null) {
				InspectionFileNoteDto inspectionFileNoteDto = gson.fromJson(inspectionFileNoteDtoStr,
						new TypeToken<InspectionFileNoteDto>() {
						}.getType());

				Optional<InspectionFileNote> opFileNote = inspectionFileNoteRepository
						.findById(inspectionFileNoteDto.getId());
				if (opFileNote != null) {

					InspectionFileNote fileNote = opFileNote.get();
					{
						mapper.map(inspectionFileNoteDto, fileNote);
						if (inspectionFileNoteDto.getIsSubmitted()) {
							fileNote.setCreatedDatetime(new Date());

						}
						fileNote.setSupplyOrderId(inspectionFileNoteDto.getSupplyOrderId());
						inspectionFileNoteRepository.save(fileNote);

						logger.info("Updating the savedFileNote");
						olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
						}.getType());

						Optional<OlApprovalDetails> optOLA = olApprovalDetailsRepository
								.findById(olApprovalDto.getId());
						if (!optOLA.isEmpty()) {
							OlApprovalDetails olapr = optOLA.get();

							olapr.setRemarks(olApprovalDto.getRemarks());
							Optional<Employee> emp = employeeRepository.findById(olApprovalDto.getApprovedBy());
							if (emp.isPresent()) {
								olapr.setApprovedBy(emp.get());

							}
							Optional<Employee> appr = employeeRepository.findById(olApprovalDto.getNextApproverId());
							if (appr.isPresent()) {
								olapr.setNextApprover(appr.get());

							}
							olapr.setRemarks(olApprovalDto.getRemarks());

							olApprovalDetailsRepository.save(olapr);
						}

						olApprovalDto.setInspectionFileNoteId(inspectionFileNoteDto.getId());
					}
				}

				else {

					logger.error("Inspection note Not found");

				}
			}

			return olApprovalDto;

		} catch (MappingException e) {
			logger.error("Error is saving inspection file note: {}", e.getMessage());
			return olApprovalDto;
		} catch (Exception e) {
			logger.error("Error is saving inspection file note: {}", e.getMessage());
			return olApprovalDto;
		}
	}

	public String updateFileNoteOlDesc(InspectionFileNoteDto inspFileNoteDto) {
		Optional<InspectionFileNote> optionalInspectionFileNoteOptional = inspectionFileNoteRepository
				.findById(inspFileNoteDto.getId());
		if (optionalInspectionFileNoteOptional.isPresent()) {
			InspectionFileNote inspectionFileNote = optionalInspectionFileNoteOptional.get();
			inspectionFileNote.setOlDescription(inspFileNoteDto.getOlDescription());
			inspectionFileNoteRepository.save(inspectionFileNote);
		} else {
			throw new IllegalArgumentException("File note with id " + inspFileNoteDto.getId() + " does not exist");
		}
		return ApplicationConstants.SUCCESS;
	}

	public List<FileNoteINoteDto> getINoteNoByFileNoteId(int fileNoteId) {

		List<FileNoteINoteDto> fileNoteINoteDtos = new ArrayList<>();
		try {
			List<Object[]> inoteDetails = inspectionFileNoteRepository.getINoteNoByFileNoteId(fileNoteId);
			for (Object[] inoteDetail : inoteDetails) {
				FileNoteINoteDto fileNoteINoteDto = new FileNoteINoteDto();
				fileNoteINoteDto.setFileNoteId(inoteDetail[0] != null ? (int) inoteDetail[0] : 0);
				fileNoteINoteDto.setOlIds(inoteDetail[1] != null ? inoteDetail[1].toString() : "");
				fileNoteINoteDto.setOlNumbers(inoteDetail[2] != null ? inoteDetail[2].toString() : "");
				fileNoteINoteDto.setConsigneeId(inoteDetail[3] != null ? (int) inoteDetail[3] : 0);
				fileNoteINoteDto.setConsigneeName(inoteDetail[4] != null ? inoteDetail[4].toString() : "");
				fileNoteINoteDto.setINoteNo(inoteDetail[5] != null ? inoteDetail[5].toString() : "NA");
				fileNoteINoteDtos.add(fileNoteINoteDto);
			}
		} catch (Exception e) {
			logger.error("Could not fetch INote Nos. due to {}", e.getMessage());
		}
		return fileNoteINoteDtos;
	}

	public String updateINoteNo(List<FileNoteINoteDto> fileNoteINoteDtos) {
		String status = null;
		try {
			for (FileNoteINoteDto fileNoteINoteDto : fileNoteINoteDtos) {
				List<Integer> offerLetterIds = fileNoteINoteDto.getOfferLetterIds();
				inspectionFileNoteRepository.changeINoteNo(fileNoteINoteDto.getNewINoteNo(),
						fileNoteINoteDto.getFileNoteId(), offerLetterIds);
				status = ApplicationConstants.SUCCESS;
			}
		} catch (Exception ex) {
			logger.error("Could not update INote no due to {}", ex.getMessage());
			status = ApplicationConstants.FAILURE;
		}
		return status;
	}
}