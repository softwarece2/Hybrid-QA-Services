package in.co.bel.olms.transaction.service.inote;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.QCC_STATUS;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.INoteStatusRepository;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.INoteLineItemDto;
import in.co.bel.olms.transaction.dto.ProjectDataDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.InternalINoteLineItem;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.MergeSubSlipMapping;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.Operation;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.Qcc;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.Ship;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.ClearanceLetterRepository;
import in.co.bel.olms.transaction.repository.INoteLineItemRepository;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.InternalINoteLineItemRepository;
import in.co.bel.olms.transaction.repository.InternalINoteRepository;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipMappingRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.OperationRepository;
import in.co.bel.olms.transaction.repository.QccRepository;
import in.co.bel.olms.transaction.repository.SoConsigneeMappingRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.transaction.repository.WarrantRepository;
import in.co.bel.olms.transaction.repository.insp.InspectionFileNoteRepository;
import in.co.bel.olms.transaction.service.MergeSubSlipService;
import in.co.bel.olms.transaction.service.SubSlipService;
import in.co.bel.olms.transaction.service.WarrantDetailsService;
import in.co.bel.olms.transaction.service.so.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalInoteService {

	private final SoLineItemRepository soLineItemRepository;

	private final ProjectService projectService;
	
	private final SubSlipService subSlipService;
	
	private final WarrantDetailsService warrantDetailsService;
	
	private final InoteUtilService inoteUtilService;

	private final INoteRepository iNoteRepository;

	private final INoteLineItemRepository iNoteLineItemRepository;

	private final InspectionFileNoteRepository inspectionFileNoteRepository;

	private final ClearanceLetterRepository clearanceLetterRepository;

	private final LineItemLotsRegisterRepository lineItemLotsRegisterRepository;

	private final SubmissionSlipRepository subSlipRepo;

	private final SubSlipLineItemRepository subSlipLineItemRepo;

	private final InspectionRoundRepository inspectionRoundRepository;

	private final ProductsRepository productsRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepo;

	private final InternalINoteRepository internalINoteRepository;

	private final InternalINoteLineItemRepository internalINoteLineItemRepository;

	private final Mapper mapper;

	private final OlLineItemRepository olLineItemRepository;

	private final MergeSubSlipRepository mergeSubSlipRepository;

	private final OperationRepository operationRepository;

	private final INoteStatusRepository iNoteStatusRepository;

	private final WarrantRepository warrantRepository;

	private final MergeSubSlipMappingRepository mergeSubSlipMappingRepository;

	private final DepartmentRepository departmentRepository;
	
	private final MergeSubSlipService mergeSubSlipService;
	
	private final SoConsigneeMappingRepository soConsigneeMappingRepository;

	private final SpecRepository specRepository;
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	
	/**
	 * @author Shubham Anand
	 * @param iNoteDto
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String saveInternalINote(INoteDto iNoteDto , Integer empId) throws Exception {

		Date todayDate = new Date();

		InternalINote existing = internalINoteRepository.getBySslipIdId(iNoteDto.getMergeSubSlipId()).orElse(null);
		if (existing != null) {
			existing.setSubmittedDatetime(todayDate);
			existing.setInoteStatusId(iNoteStatusRepository.findById(8).get());
//			supplyOrderRepository.updateStatusByOrderNo(existing.getSupplyOrderNo(), ApplicationConstants.SO_STATUS_CONSTANTS.COMPLETED);
//			SupplyOrder supplyOrderId = supplyOrderRepository.findByOrderNo(existing.getSupplyOrderNo());
//			soLineItemRepository.updateStatusBySupplyOrderId(supplyOrderId.getId(), ApplicationConstants.SO_STATUS_CONSTANTS.COMPLETED);
			warrantRepository.updateStatusByWarrantNo(iNoteDto.getWarrantyNo(), ApplicationConstants.COMMON_STATUS_CONSTANTS.I_NOTE_GENERATED);
			InternalINote saved = internalINoteRepository.save(existing);
			List<InternalINoteLineItem> iNoteLineItems = new ArrayList<>();
			for (InternalINoteLineItem item : existing.getINoteLineItems()) {
				
			    if (item.getOlLineItem() == null) {
			        continue; // Skip this item if there is no line item to match against
			    }
				Optional<INoteLineItemDto> dto = iNoteDto.getINoteLineItems().stream()
						.filter(li -> li.getOlLineItemId() == item.getOlLineItem().getId()).findAny();
				if (dto.isPresent()) {
					InternalINoteLineItem itemNew = mapper.map(dto, InternalINoteLineItem.class);
					itemNew.setRemarks(item.getRemarks());
					itemNew.setId(item.getId());
					itemNew.setINote(saved);			
                    itemNew.setEname(item.getEname());
					iNoteLineItems.add(itemNew);
				}
			}
			internalINoteLineItemRepository.saveAll(iNoteLineItems);
		} else {
            iNoteDto.setSubmittedById(empId);
			InternalINote iNote = mapper.map(iNoteDto, InternalINote.class);
			iNote.setInoteStatusId(iNoteStatusRepository.findById(8).get());
//			SupplyOrder supplyOrderId = supplyOrderRepository.findByOrderNo(iNote.getSupplyOrderNo());
//			supplyOrderRepository.updateStatusByOrderNo(iNoteDto.getSupplyOrderNo(), ApplicationConstants.SO_STATUS_CONSTANTS.COMPLETED);
//			soLineItemRepository.updateStatusBySupplyOrderId(supplyOrderId.getId(), ApplicationConstants.SO_STATUS_CONSTANTS.COMPLETED);
			warrantRepository.updateStatusByWarrantNo(iNoteDto.getWarrantyNo(), ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS.I_NOTE_GENERATED);
			iNote.setCreatedDatetime(todayDate);
			iNote.setSubmittedDatetime(todayDate);
			iNote.setInoteDate(todayDate);
			
			List<Integer> subSlips = mergeSubSlipRepository.findsubSlipsByMergedId(iNoteDto.getMergeSubSlipId());
			List<Object[]> sectionData = offerLetterRepository.getDepartSecCodeBySubSlipId(subSlips.get(0));
			Integer deptId = null;
			for(Object[] obj: sectionData) {
				deptId = obj[0]!=null ? (Integer)obj[0]: null;
			}
			Department department = departmentRepository.findById(deptId).orElse(null);
			iNote.setDepartment(department);
			InternalINote saveINote = internalINoteRepository.save(iNote);

			List<InternalINoteLineItem> iNoteLineItems = new ArrayList<>();
			for (INoteLineItemDto dto : iNoteDto.getINoteLineItems()) {
				InternalINoteLineItem item = mapper.map(dto, InternalINoteLineItem.class);
//				Optional<OlLineItem> optional = olLineItemRepository.findById(dto.getOlLineItemId());
				Optional<SubSlipLineItem> sslipLineItem = subSlipLineItemRepo.findById(dto.getOlLineItemId()); // added subslipLineItem to InternalInotelineItem table
				if (sslipLineItem.isPresent()) {
					item.setSslipLineItem(sslipLineItem.get());
					item.setINote(saveINote);
					item.setCreatedDatetime(todayDate);
					item.setEname(dto.getEname());
					item.setRemarks(dto.getRemarks());
					iNoteLineItems.add(item);
				}
			}

			internalINoteLineItemRepository.saveAll(iNoteLineItems);
		}

    //After generating internal iNote, update Warrants status and subSlip Status to completed based on condition
    warrantDetailsService.checkAndUpdateWarrantStatus(iNoteDto.getOlId(), true);
    subSlipService.checkAndUpdateSslipStatus(iNoteDto.getMergeSubSlipId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.COMPLETED, true);
    mergeSubSlipService.updateMergeSubSLipStatus(iNoteDto.getMergeSubSlipId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.INT_INOTE_GENERATED);
    return ApplicationConstants.SUCCESS;

	}

	
	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 */
	public INoteDto getInternalINoteByOlId(int olId) {
		InternalINote existing = internalINoteRepository.getByOlId(olId);
		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			iNoteDto.setInoteId(existing.getId());
			
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			
			//Getting product full name for Nomenclature
			List<OlLineItem>  olLineItem = olLineItemRepository.getLineItemsByOl(olId);				
			for(OlLineItem ol:olLineItem) {
				List<LineItemLotsRegister> lineItemLotsRegister = lineItemLotsRegisterRepository.getRegisterNosByOlLineItemId(ol.getId());
				for(LineItemLotsRegister liItemLotsRegister1:lineItemLotsRegister) {
					List<InspectionRound> inspectionRound = inspectionRoundRepository.findInspectionRoundByDeviceId(liItemLotsRegister1.getId());
					for(InspectionRound inspectionRound2:inspectionRound) {
						@SuppressWarnings("deprecation")
						Products products = productsRepository.getById(inspectionRound2.getProduct().getId());
						iNoteDto.setNomenclature(products.getProdFullName());
					}
				}
			}
			
			iNoteDto.setSupplyOrderDateFormatted(outputDateFormat.format(iNoteDto.getSupplyOrderDate()));
			iNoteDto.setOfferLetterDateFormatted(outputDateFormat.format(iNoteDto.getOfferLetterDate()));
			iNoteDto.setInoteDateFormatted(outputDateFormat.format(iNoteDto.getInoteDate()));
			
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (InternalINoteLineItem item : existing.getINoteLineItems()) {

				INoteLineItemDto dto = new INoteLineItemDto();
				
				for(OlLineItem ol : olLineItem ) {
					 dto = mapper.map(item, INoteLineItemDto.class);
					SOLineItem soLineItem = ol.getSoLineItem();
					dto.setCatPartNo(soLineItem.getPartNumber().getCatPartNo());
					dto.setNsnPartNo(soLineItem.getPartNumber().getNsnPartNo());
				}
			

				// registration nos code to be changed
//				Optional<OfferLetter> optional = offerLetterRepository.findById(olId);
//				if (optional.isPresent()) {
//					OfferLetter offerLetter = optional.get();
//					SupplyOrder supplyOrder = offerLetter.getSaleOrder().getSupplyOrder();
//					
//					if (supplyOrder.getIsRegistrationNo() != null && supplyOrder.getIsRegistrationNo()) {
//						List<LineItemLotsRegister> lineItemLotRegisters = item.getOlLineItem()
//								.getLineItemLotsRegisters();
//						List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream()
//								.filter(reg -> Objects.equals(reg.getStatus(),
//										ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
//								.collect(Collectors.toList());
//						dto.setRegNos(filtered.stream().map(LineItemLotsRegister::getRegisterNo)
//								.collect(Collectors.toList()));
//					}
//				} 
//				else
//					logger.info("Registration numbers not found as Offer Letter with OL ID does not exist");

				iNoteLineItemDtos.add(dto);
			}
			
		//	code to be changed
//			Optional<OfferLetter> OpOl = offerLetterRepository.findById(olId);
//			if (OpOl != null) {
//				OfferLetter ol = OpOl.get();
//
//				SupplyOrder supplyOrder =  ol.getSaleOrder().getSupplyOrder();
//				iNoteDto.setDrawingNo(supplyOrder.getDrawingNo());
//				iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
//				List<ProjectDataDto> projectDataDtos =  projectService.getProjectBySoId(supplyOrder.getId());
//				for(ProjectDataDto projectDataDto:projectDataDtos) {
//					iNoteDto.setRevNo(projectDataDto.getRevNo());
//					iNoteDto.setWtIssueDate(projectDataDto.getWtIssueDate());
//					iNoteDto.setEstDate(projectDataDto.getEstDate());
//				}
//				iNoteDto.setFileNo(ol.getSaleOrder().getSupplyOrder().getFileNo());
//
//			}
			
			
			
			// iNoteDto.setTypeOfDelivery(typeOfDelivery);
			// iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));

			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setExisting(true);
			
			// author: Shridhar R  
			// digital signature for qcc			
			iNoteDto.setSubmittedById(existing.getSubmittedBy().getId());
			iNoteDto.setSubmittedByName(existing.getSubmittedBy().getEname());
			iNoteDto.setSubmittedByDesignation(existing.getSubmittedBy().getEmpRole().getEmpRole());

			return iNoteDto;
		} else {
			return getNewInternalINote(olId);
		}

	}

	public INoteDto getInternalINoteBySubSlipId(Integer mergedSubSlipId) {
		
		InternalINote existing = internalINoteRepository.getBySslipIdId(mergedSubSlipId).orElse(null);
		DateFormat cqaoDateFormat = new SimpleDateFormat("dd-mm-yyyy");

		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			iNoteDto.setInoteId(existing.getId());
	        List<MergeSubSlipMapping> mergeSubSlipMapping = mergeSubSlipMappingRepository
						.findByMergeOlNoIdId(mergedSubSlipId);
				
				List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(mergeSubSlipMapping.get(0).getSubSlip().getId());
				String secName = null;
				String secCode = null;
				if(obj!=null) {
					for(Object[] det: obj) {
						secName = det[1]!=null? det[1].toString(): null;
						secCode = det[2]!=null? det[2].toString(): null;
						iNoteDto.setSection(secName);
						iNoteDto.setSecCode(secCode);
					}
					
				}
			
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			
			List<SubSlipLineItem>  sSlipLineItem = subSlipLineItemRepo.getLineItemsByMergedSslipId(mergedSubSlipId);				
			for(SubSlipLineItem ol:sSlipLineItem) {
				List<SubSlipSerialNo> lineItemLotsRegister = subSlipSerialNoRepo.findBySubSlipLineItemIdOrderByIdAsc(ol.getId());
				for(SubSlipSerialNo liItemLotsRegister1:lineItemLotsRegister) {
					List<InspectionRound> inspectionRound = inspectionRoundRepository.findInspectionRoundByDeviceId(liItemLotsRegister1.getId());
					for(InspectionRound inspectionRound2:inspectionRound) {
						@SuppressWarnings("deprecation")
						Products products = productsRepository.findById(inspectionRound2.getProduct().getId()).orElse(null);
						iNoteDto.setNomenclature(products.getProdFullName());
					}
				}
			}
			
			iNoteDto.setSupplyOrderDateFormatted(outputDateFormat.format(iNoteDto.getSupplyOrderDate()));
			iNoteDto.setOfferLetterDateFormatted(outputDateFormat.format(iNoteDto.getOfferLetterDate()));
			iNoteDto.setInoteDateFormatted(outputDateFormat.format(iNoteDto.getInoteDate()));
			List<Object[]> passReg = mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId);
			iNoteDto.setPassedRegNos(passReg.stream().map(regNo -> (regNo[0]!=null? regNo[0].toString(): null)).toList());
			BigDecimal acceptedqty = BigDecimal.ZERO;
			BigDecimal rejectedqty = BigDecimal.ZERO;
			BigDecimal clearedQty = BigDecimal.ZERO;

			for(Object[] data : passReg) {
				if(data[1]!= null && data[1]==ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
					acceptedqty = acceptedqty.add(new BigDecimal(data[2].toString()));
				}
				else {
					acceptedqty = acceptedqty.add(BigDecimal.ONE) ;
				}
			}
//			iNoteDto.setPassedRegNos(mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId));

			iNoteDto.setAcceptedQty(acceptedqty);
			List<Object[]> failedReg = mergeSubSlipRepository.getMergedOlfailedRegNosById(mergedSubSlipId);
			iNoteDto.setFailedRegNos(failedReg.stream().map(regNo -> (regNo[0]!=null ? regNo[0].toString(): null)).toList());
			for (Object[] data: failedReg) {
				if(data[1]!=null && data[1]==ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
					rejectedqty = rejectedqty.add(new BigDecimal(data[2].toString()));
				}else {
					rejectedqty = rejectedqty.add(BigDecimal.ONE);
				}
			}
			iNoteDto.setRejectedQty(rejectedqty);
//			if(submissionSlip.getWarrant().getSoLineItem().getRegistrationNo().getId()== ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
//				clearedQty = submissionSlip.getWarrant().getSoLineItem().getOrderQty();
//			}else {				
//				clearedQty = BigDecimal.valueOf(internalINoteRepository.getClearedQtySslip(submissionSlip.getWarrant().getSoLineItem().getId())) ;
//			}
//			iNoteDto.setClearedQty(clearedQty);
//
//			iNoteDto.setPassedRegNos(mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId));
//			iNoteDto.setAcceptedQty(mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId).size());
//			iNoteDto.setRejectedQty(mergeSubSlipRepository.getMergedOlfailedRegNosById(mergedSubSlipId).size());
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (InternalINoteLineItem item : existing.getINoteLineItems()) {

				INoteLineItemDto dto = new INoteLineItemDto();
				
				for(SubSlipLineItem ol : sSlipLineItem ) {
					 dto = mapper.map(item, INoteLineItemDto.class);
					SOLineItem soLineItem = ol.getSoLineItem();
					dto.setCatPartNo(soLineItem.getPartNumber().getCatPartNo());
					dto.setRegistrationNo(soLineItem.getRegistrationNo().getId());
					dto.setNsnPartNo(soLineItem.getPartNumber().getNsnPartNo());
//					dto.setOrderTypeNo(soLineItem.getOrderTypeNo());
					dto.setLineItemNo(soLineItem.getLineItemNo());
					if(soLineItem.getRegistrationNo().getId()==ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER)
						dto.setAccepted(soLineItem.getOrderQty());
					else
						dto.setAccepted(BigDecimal.valueOf(mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId).size()));
					
//					List<SubSlipSerialNo> subSlipSerialNoId = subSlipSerialNoRepo.findBySubSlipLineItemIdOrderByIdAsc(ol.getId());
					String failedParam = specRepository.getFailedSpecForInternalInote(ol.getId());
					dto.setFailedParamDesc(failedParam);
					if(dto.getQtyAcceptedToDate() == null) {
						dto.setCumulativePassed(dto.getAccepted());
					} else {
						dto.setCumulativePassed(dto.getQtyAcceptedToDate().add(dto.getAccepted()));
					}
					
//					dto.setQtyAcceptedToDate(clearedQty);
//					dto.setRrQty(mergeSubSlipRepository.getMergedOlfailedRegNosById(mergedSubSlipId).size());
					
//					List<SubSlipSerialNo> subSlipSerialNos = subSlipSerialNoRepo
//							.findBySubSlipLineItemIdOrderByIdAsc(ol.getId());
//					dto.setRegNos(subSlipSerialNos.stream()
//							.map(s -> s.getWarrantDetails().getLineItemSerialNo().getSerialNo())
//							.collect(Collectors.toList()));
				}

				// registration nos code to be changed
//				List<SubmissionSlip> submissionSlip = subSlipRepo.findByMergedSslipId(mergedSubSlipId)
//						.orElseThrow(() -> new IllegalArgumentException("Registration numbers not found as Offer Letter with OL ID does not exist"));
//				
//				for()
//				SupplyOrder supplyOrder = submissionSlip.getWarrant().getSoLineItem().getSupplyOrder();
//				
//				if (supplyOrder.getIsRegistrationNo() != null && supplyOrder.getIsRegistrationNo()) {
//					List<LineItemLotsRegister> lineItemLotRegisters = item.getOlLineItem()
//							.getLineItemLotsRegisters();
//					List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream()
//							.filter(reg -> Objects.equals(reg.getStatus(),
//									ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
//							.collect(Collectors.toList());
//					dto.setRegNos(filtered.stream().map(LineItemLotsRegister::getRegisterNo)
//							.collect(Collectors.toList()));
//				}
				iNoteLineItemDtos.add(dto);
			}
			
		//	code to be changed
			List<SubmissionSlip> submissionSlipList = subSlipRepo.findByMergedSslipId(mergedSubSlipId);
//					.orElseThrow(() -> new IllegalArgumentException("Registration numbers not found as Offer Letter with OL ID does not exist"));
			if (submissionSlipList != null) {

				SubmissionSlip submissionSlip = submissionSlipList.get(0);

				SupplyOrder supplyOrder = submissionSlip.getWarrant().getSoLineItem().getSupplyOrder();
				SOLineItem soLineItem = submissionSlip.getWarrant().getSoLineItem();
				Operation op = operationRepository.findByWarrant(submissionSlip.getWarrant());
				iNoteDto.setRegistrationNo(soLineItem.getRegistrationNo().getId());
//			clearedQty = BigDecimal.valueOf(internalINoteRepository.getClearedQtyByLineItem(submissionSlip.getWarrant().getSoLineItem().getId()));

//			iNoteDto.setClearedQty(clearedQty);
//			iNoteDto.setCumulativePassed(clearedQty.add(acceptedqty));
				iNoteDto.setOperationNo(op != null ? op.getOpNo() : null);
				iNoteDto.setDrawingNo(soLineItem.getPartNumber().getDrawingNo());
				iNoteDto.setOrderTypeNo(soLineItem.getOrderTypeNo());
				iNoteDto.setEpCode(soLineItem.getPartNumber().getPartNo());
				String indDate = outputDateFormat.format(soLineItem.getOrderTypeDate());
				iNoteDto.setOrderTypeDate(indDate);
				iNoteDto.setLineItemSNo(soLineItem.getLineItemNo());
				iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
				iNoteDto.setIndentDateStr(dateFormat.format(soLineItem.getDeliveryDate()));

				List<ProjectDataDto> projectDataDtos = projectService.getProjectBySoId(supplyOrder.getId());
				for (ProjectDataDto projectDataDto : projectDataDtos) {
					iNoteDto.setRevNo(projectDataDto.getRevNo());
					if (projectDataDto.getWtIssueDate() != null)
						iNoteDto.setWtIssueDate(dateFormat.format(projectDataDto.getWtIssueDate()));
					if (projectDataDto.getEstDate() != null)
						iNoteDto.setEstDate(dateFormat.format(projectDataDto.getEstDate()));
				}
				iNoteDto.setFileNo(submissionSlip.getWarrant().getSoLineItem().getSupplyOrder().getFileNo());

				// iNoteDto.setTypeOfDelivery(typeOfDelivery);
				// iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setExisting(true);
			
			// author: Shridhar R  
			// digital signature for qcc			
			iNoteDto.setSubmittedById(existing.getSubmittedBy().getId());
			iNoteDto.setSubmittedByName(existing.getSubmittedBy().getEname());
			iNoteDto.setSubmittedByDesignation(existing.getSubmittedBy().getEmpRole().getEmpRole());
            iNoteDto.setNomenclature(existing.getProjectDetails());
            
			return iNoteDto;
		} else {
//			return getNewInternalINote(mergedSubSlipId);
			return getNewInternalINoteByMergeSslipId(mergedSubSlipId);
		}

	}


	
	// dependency to be removed
	private final OfferLetterRepository offerLetterRepository;

	private final QccRepository qccRepository;
	
	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 */
	public INoteDto getNewInternalINote(int olId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		
		Optional<Qcc>  qccOp=qccRepository.findByMergedSubSlipId(olId);
		
		
		
	
		if (qccOp.isPresent()) {
			Qcc qcc = qccOp.get();
			INoteDto iNoteDto = new INoteDto();
			
//			{----------
////			SupplyOrder supplyOrder = offerLetter.getSaleOrder().getSupplyOrder();
//			SupplyOrder supplyOrder = offerLetter.getWarrant().getSoLineItem().getSupplyOrder();
//			SaleOrder saleOrder = offerLetter.getWarrant().getSoLineItem().getSaleOrder();
//			Project project = supplyOrder.getProjects() != null ? supplyOrder.getProjects().get(0) : null;
//			Ship ship = project != null && project.getShips() != null ? project.getShips().get(0) : null;
//			Category category = ship != null && ship.getCategories() != null ? ship.getCategories().get(0) : null;
//			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
//			iNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
//			iNoteDto.setWorkOrderNo(saleOrder.getSaleOrderNo());
//			iNoteDto.setRfEstNo(ship != null ? ship.getShipName() : null);
//			iNoteDto.setWarranty(category != null ? category.getCategoryName() : null);
//			iNoteDto.setEpCode(offerLetter.getOlLineItems().get(0).getSoLineItem().getPartNo());
//			iNoteDto.setDeliveryDate(supplyOrder.getDd() != null ? dateFormat.format(supplyOrder.getDd()):null); 
//			iNoteDto.setOrderedQty(supplyOrder.getTotalQty());
//			
			iNoteDto.setDrawingNo(qcc.getDrawingNo());
//			iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
//			
//			Integer clearedQty = internalINoteRepository.getClearedQty(offerLetter.getOlLineItems().get(0).getSoLineItem().getId());
//			iNoteDto.setClearedQty(clearedQty);
//
//			iNoteDto.setProjectDetails(project.getProjectName());
//			List<ProjectDataDto> projectDataDtos =  projectService.getProjectBySoId(supplyOrder.getId());
//			for(ProjectDataDto projectDataDto:projectDataDtos) {
//				iNoteDto.setRevNo(projectDataDto.getRevNo());
//				iNoteDto.setWtIssueDate(projectDataDto.getWtIssueDate());
//				iNoteDto.setEstDate(projectDataDto.getEstDate());
//			}
//			Organization organization = supplyOrder.getOrganization();
//			iNoteDto.setInspectionAgencyId(organization.getId());
//			iNoteDto.setInspectionAgency(organization.getOrganizationName());
//			// Generation of I-Note no based on STD code, serial no for the FY and CQAE
//			String stdCode = organization.getStdCode();
//			String fY = TdmsUtil.getFinancialYearStr();
//			int inoteCountInCurrentFY = internalINoteRepository.getInoteCountInCurrentFY();
//			String inoteNo = stdCode + fY + (String.format("%05d", inoteCountInCurrentFY + 1));
//			iNoteDto.setINoteNo(inoteNo);
//			iNoteDto.setFileNo(supplyOrder.getFileNo());
//			List<DdExtension> ddExtensions = supplyOrder.getDdExtensions();
//			if (!ddExtensions.isEmpty()) {
//
//				Collections.sort(ddExtensions, (o1, o2) -> o2.getNewDdDate().compareTo(o1.getNewDdDate()));
//				DdExtension latestDdExt = ddExtensions.get(0);
//				iNoteDto.setNewDd(latestDdExt.getNewDdDate());
//				iNoteDto.setDdExtLetterDate(iNoteDateFormat.format(latestDdExt.getLetterDate()));
//				iNoteDto.setDdExtLetterNo(latestDdExt.getLetterNo());
//			}
//
//			SalesGroup salesGroup = supplyOrder.getSalesGroup();
//			if (salesGroup != null) {
//				Organization salesGrpOrganization = salesGroup.getOrganization();
//				iNoteDto.setContractorName(salesGrpOrganization.getOrganizationName());
//				iNoteDto.setContractorAddress(salesGrpOrganization.getAddress());
//			}
//
//			if (supplyOrder.getIndenter() != null)
//				iNoteDto.setIndentor(supplyOrder.getIndenter().getAddress());
//
//			List<SOLineItem> soLineItem1 = soLineItemRepository.getSoLineItemsBySaleOrderId(saleOrder.getId());
//			for (SOLineItem lineItem : soLineItem1) {
//		        iNoteDto.setIndentNo(lineItem.getLineItemNo()); 
//		    }
//			
//			iNoteDto.setIndentDate(supplyOrder.getIndentDate());
//			iNoteDto.setOrgName(supplyOrder.getOrganization().getAddress());
//
//			iNoteDto.setConsignee(supplyOrder.getSoConsigneeMappings().stream().map(Consignee::getName)
//					.collect(Collectors.joining(",")));
//
//			iNoteDto.setOfferLetterNo(offerLetter.getOlNumber());
//			iNoteDto.setOfferLetterDate(offerLetter.getOfferDate());
//
//			iNoteDto.setDescription(offerLetter.getStoresDescription());
//
//			// Getting last I-Note details By Supply Order Id
//			List<Object[]> lastINoteDetails = internalINoteRepository.getLastInoteDetailsBySoId(supplyOrder.getId());
//			if (!lastINoteDetails.isEmpty()) {
//				iNoteDto.setPrevINoteNo(lastINoteDetails.get(0)[0].toString());
//				iNoteDto.setPrevINoteDate(
//						lastINoteDetails.get(0)[1] != null ? lastINoteDetails.get(0)[1].toString() : null);
//			}
//			iNoteDto.setRemarks(offerLetter.getRemarks());
//
//			if (supplyOrder.getInoteType() != null) {
//				iNoteDto.setInoteTypeId(supplyOrder.getInoteType().getId());
//			}
//			iNoteDto.setOlId(offerLetter.getId());
//			
//			if(supplyOrder.getInoteType()!= null)
//			iNoteDto.setInoteTypeName(supplyOrder.getInoteType().getName());
//			else
//			iNoteDto.setInoteTypeName(null);	
//
//			// inspection Start Date and End Date, description
//
//			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
//			for (OlLineItem olLineItem : offerLetter.getOlLineItems()) {
//				int tendered = olLineItem.getOfferedQty();
////				int accepted = olLineItem.getClearedQty() != null ? olLineItem.getClearedQty() : 0;
//				int accepted = tendered;
////				int rejected = tendered - accepted;
//				int rejected = 0;
//
//				INoteLineItemDto dto = new INoteLineItemDto();
//				SOLineItem soLineItem = olLineItem.getSoLineItem();
////				dto.setPartNo(soLineItem.getPartNo());
//				dto.setPartNo(soLineItem.getPartNumber().getPartNo());
//				dto.setDescription(soLineItem.getDescription());
//				dto.setOrderedQty(soLineItem.getOrderQty());
//				dto.setOlLineItemId(olLineItem.getId());
////				dto.setUom(soLineItem.getUom().getUnit());
//				dto.setTendered(tendered);
//				dto.setAccepted(accepted);
//				dto.setRejected(rejected);
//				dto.setLedgerFolio(tendered);
//				dto.setIndentNo(soLineItem.getLineItemNo());
////			Code to be changed
//				//dto.setQtyAcceptedToDate(
////						offerLetterRepository.getCountOfCompletedLineItemsBySoLiId(soLineItem.getId()));
//				// registration nos
//				if (supplyOrder.getIsRegistrationNo() != null && supplyOrder.getIsRegistrationNo()) {
//					List<LineItemLotsRegister> lineItemLotRegisters = olLineItem.getLineItemLotsRegisters();
//					List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream().filter(reg -> Objects
//							.equals(reg.getStatus(), ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
//							.collect(Collectors.toList());
//					dto.setRegNos(
//							filtered.stream().map(LineItemLotsRegister::getRegisterNo).collect(Collectors.toList()));
//				}
//
//				iNoteLineItemDtos.add(dto);
//			}
//			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
//			iNoteDto.setExisting(false);
//			----}
			return iNoteDto;
		} else
			return null;
	}

	
	public INoteDto getNewInternalINoteByMergeSslipId(Integer mergedSubSlipId) {
		INoteDto iNoteDto = new INoteDto();
		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		Optional<Qcc>  qccOp = qccRepository.findByMergedSubSlipId(mergedSubSlipId);
		
        List<MergeSubSlipMapping> mergeSubSlipMapping = mergeSubSlipMappingRepository
					.findByMergeOlNoIdId(mergedSubSlipId);
			
			List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(mergeSubSlipMapping.get(0).getSubSlip().getId());
			String secName = null;
			String secCode = null;
			Integer deptId = null;
			if(obj!=null) {
				for(Object[] det: obj) {
					deptId = det[0]!=null ? (Integer)det[0]: null;
					secName = det[1]!=null? det[1].toString(): null;
					secCode = det[2]!=null? det[2].toString(): null;
					iNoteDto.setSection(secName);
					iNoteDto.setSecCode(secCode);
				}
				
			}
		
		List<SubmissionSlip> submissionSlipList = subSlipRepo.findByMergedSslipId(mergedSubSlipId);
				//.orElseThrow(() -> new IllegalArgumentException("Registration numbers not found as Offer Letter with OL ID does not exist"));
		
		SubmissionSlip submissionSlip= submissionSlipList.get(0);
		
		if (qccOp.isPresent()) {
			Qcc qcc = qccOp.get();
			
			if(qcc.getQccStatus().getId() != QCC_STATUS.DO_QC_APPROVED) {
				throw new IllegalArgumentException("QCC has not been aproved by DO_QC");
			}
			
//			INoteDto iNoteDto = new INoteDto();
	        Operation op = operationRepository.findByWarrant(submissionSlip.getWarrant());
	        iNoteDto.setOperationNo(op!=null? op.getOpNo(): null);
			SupplyOrder supplyOrder = submissionSlip.getWarrant().getSoLineItem().getSupplyOrder();
			SaleOrder saleOrder = submissionSlip.getWarrant().getSoLineItem().getSaleOrder();
      Project project = supplyOrder.getProjects() != null && !supplyOrder.getProjects().isEmpty() ? supplyOrder.getProjects().get(0) : null;
      Ship ship = project != null && project.getShips() != null && !project.getShips().isEmpty() ? project.getShips().get(0) : null;
//      Category category = ship != null && ship.getCategories() != null && !ship.getCategories().isEmpty() ? ship.getCategories().get(0) : null;
      Category category = submissionSlip.getWarrant();
			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
			iNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
			iNoteDto.setWorkOrderNo(saleOrder.getSaleOrderNo());
			iNoteDto.setRfEstNo(ship != null ? ship.getShipName() : null);
			iNoteDto.setWarranty(category != null ? category.getCategoryName() : null);
			iNoteDto.setEpCode(submissionSlip.getWarrant().getSoLineItem().getPartNumber().getPartNo());
			iNoteDto.setDeliveryDate(supplyOrder.getDd() != null ? dateFormat.format(supplyOrder.getDd()):null); 
			iNoteDto.setOrderedQty(supplyOrder.getTotalQty());;
//			iNoteDto.setNomenclature(qcc.getNomenclature());
			iNoteDto.setDrawingNo(qcc.getDrawingNo());
			iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
			
			List<Object[]> passReg = mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId);
			iNoteDto.setPassedRegNos(passReg.stream().map(regNo -> (regNo[0]!=null? regNo[0].toString(): null)).toList());
			BigDecimal acceptedqty = BigDecimal.ZERO;
			BigDecimal rejectedqty = BigDecimal.ZERO;
			BigDecimal clearedQty = BigDecimal.ZERO;

			for(Object[] data : passReg) {
				if(data[1]!= null && data[1]==ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
					acceptedqty = acceptedqty.add(new BigDecimal(data[2].toString()));
				}
				else {
					acceptedqty = acceptedqty.add(BigDecimal.ONE) ;
				}
			}
//			iNoteDto.setPassedRegNos(mergeSubSlipRepository.getMergedOlPassedRegNosById(mergedSubSlipId));
		
			iNoteDto.setAcceptedQty(acceptedqty);
			List<Object[]> failedReg = mergeSubSlipRepository.getMergedOlfailedRegNosById(mergedSubSlipId);
			iNoteDto.setFailedRegNos(failedReg.stream().map(regNo -> (regNo[0]!=null ? regNo[0].toString(): null)).toList());
			for (Object[] data: failedReg) {
				if(data[1]!=null && data[1]==ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
					rejectedqty = rejectedqty.add(new BigDecimal(data[2].toString()));
				}else {
					rejectedqty = rejectedqty.add(BigDecimal.ONE);
				}
			}
			iNoteDto.setRejectedQty(rejectedqty);
			if(submissionSlip.getWarrant().getSoLineItem().getRegistrationNo().getId()== ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
				clearedQty = submissionSlip.getWarrant().getSoLineItem().getOrderQty();
			}else {				
//				clearedQty = BigDecimal.valueOf(internalINoteRepository.getClearedQtySslip(submissionSlip.getWarrant().getSoLineItem().getId())) ;
				//TODO to check the return type integer or bigdecimal
				Integer clearQty= internalINoteRepository.getClearedQtyByLineItem(submissionSlip.getWarrant().getSoLineItem().getId());
				if(clearQty==null)
					clearQty=0;
				clearedQty = BigDecimal.valueOf(clearQty);
			}
			iNoteDto.setClearedQty(clearedQty);

			iNoteDto.setProjectDetails(project.getProjectName());
			List<ProjectDataDto> projectDataDtos =  projectService.getProjectBySoId(supplyOrder.getId());
			for(ProjectDataDto projectDataDto:projectDataDtos) {
				iNoteDto.setRevNo(projectDataDto.getRevNo());
        if (projectDataDto.getWtIssueDate() != null)
          iNoteDto.setWtIssueDate(dateFormat.format(projectDataDto.getWtIssueDate()));
        if (projectDataDto.getEstDate() != null) iNoteDto.setEstDate(dateFormat.format(projectDataDto.getEstDate()));
			}
			Organization inspectionAuth = submissionSlip.getWarrant().getSoLineItem().getInspectionAuthorityId();
			iNoteDto.setInspectionAgencyId(inspectionAuth.getId());
			iNoteDto.setInspectionAgency(inspectionAuth.getOrganizationName());
			// Generation of I-Note no based on STD code, serial no for the FY and CQAE
//			String stdCode = inspectionAuth.getStdCode();
			
//			String fY = TdmsUtil.getFinancialYearStr();
//			int inoteCountInCurrentFY = internalINoteRepository.getInoteCountInCurrentFY(deptId);
//			
//			
//			String inoteNo = "I" + secCode + fY + (String.format("%05d", inoteCountInCurrentFY + 1));
			
			String inoteNo = inoteUtilService.getNextInotenumber(deptId, secCode);
			
			iNoteDto.setINoteNo(inoteNo);
			iNoteDto.setFileNo(supplyOrder.getFileNo());
			List<DdExtension> ddExtensions = supplyOrder.getDdExtensions();
			if (!ddExtensions.isEmpty()) {

				Collections.sort(ddExtensions, (o1, o2) -> o2.getNewDdDate().compareTo(o1.getNewDdDate()));
				DdExtension latestDdExt = ddExtensions.get(0);
				iNoteDto.setNewDd(latestDdExt.getNewDdDate());
				iNoteDto.setDdExtLetterDate(iNoteDateFormat.format(latestDdExt.getLetterDate()));
				iNoteDto.setDdExtLetterNo(latestDdExt.getLetterNo());
			}

			SalesGroup salesGroup = supplyOrder.getSalesGroup();
			if (salesGroup != null) {
				Organization salesGrpOrganization = salesGroup.getOrganization();
				iNoteDto.setContractorName(salesGrpOrganization.getOrganizationName());
				iNoteDto.setContractorAddress(salesGrpOrganization.getAddress());
			}

			if (supplyOrder.getIndenter() != null)
				iNoteDto.setIndentor(supplyOrder.getIndenter().getAddress());

			List<SOLineItem> soLineItem1 = soLineItemRepository.getSoLineItemsBySaleOrderId(saleOrder.getId());
			for (SOLineItem lineItem : soLineItem1) {
		        iNoteDto.setIndentNo(lineItem.getLineItemNo()); 
		        iNoteDto.setLineItemSNo(lineItem.getLineItemNo());
		        iNoteDto.setOrderTypeNo(lineItem.getOrderTypeNo());
		        String indDate = outputDateFormat.format(lineItem.getOrderTypeDate());
				iNoteDto.setOrderTypeDate(indDate);
		        iNoteDto.setIndentDateStr(dateFormat.format(lineItem.getDeliveryDate()));
		        iNoteDto.setRegistrationNo(lineItem.getRegistrationNo().getId());
		    }
			
			iNoteDto.setIndentDate(supplyOrder.getIndentDate()) ;
//			iNoteDto.setOrgName(supplyOrder.getOrganization().getAddress());

			iNoteDto.setConsignee(
				    soConsigneeMappingRepository
				        .findBySoLineItem(soLineItem1.get(0).getId())
				        .stream()
				        .map(mapping -> mapping.getConsignee().getName())
				        .filter(Objects::nonNull)
				        .collect(Collectors.joining(", "))
				);			
//			iNoteDto.setConsignee(supplyOrder.getSoConsigneeMappings().stream().map(Consignee::getName)
//					.collect(Collectors.joining(",")));

			iNoteDto.setOfferLetterNo(submissionSlip.getOlNumber());
			iNoteDto.setOfferLetterDate(submissionSlip.getOfferDate());

			iNoteDto.setDescription(submissionSlip.getStoresDescription());

			// Getting last I-Note details By Supply Order Id
			List<Object[]> lastINoteDetails = internalINoteRepository.getLastInoteDetailsBySoId(supplyOrder.getId());
			if (!lastINoteDetails.isEmpty()) {
				iNoteDto.setPrevINoteNo(lastINoteDetails.get(0)[0].toString());
				iNoteDto.setPrevINoteDate(
						lastINoteDetails.get(0)[1] != null ? lastINoteDetails.get(0)[1].toString() : null);
			}
			iNoteDto.setRemarks(submissionSlip.getRemarks());

			if (supplyOrder.getInoteType() != null) {
				iNoteDto.setInoteTypeId(supplyOrder.getInoteType().getId());
			}
			iNoteDto.setOlId(submissionSlip.getId());
			
			if(supplyOrder.getInoteType()!= null)
			iNoteDto.setInoteTypeName(supplyOrder.getInoteType().getName());
			else
			iNoteDto.setInoteTypeName(null);	

			// inspection Start Date and End Date, description

			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (SubSlipLineItem sSlipLineItem : submissionSlip.getSubSlipLineItems()) {
//				BigDecimal tendered = sSlipLineItem.getOfferedQty();
				
//				int accepted = olLineItem.getClearedQty() != null ? olLineItem.getClearedQty() : 0;
				BigDecimal accepted = acceptedqty;
//				int rejected = tendered - accepted;
				BigDecimal rejected = rejectedqty;
				BigDecimal tendered = accepted;
				INoteLineItemDto dto = new INoteLineItemDto();
				SOLineItem soLineItem = sSlipLineItem.getSoLineItem();
//				dto.setPartNo(soLineItem.getPartNo());
				dto.setPartNo(soLineItem.getPartNumber().getPartNo());
//				dto.setDescription(soLineItem.getDescription());
				dto.setOrderedQty(soLineItem.getOrderQty());
				dto.setOlLineItemId(sSlipLineItem.getId());
//				dto.setUom(soLineItem.getUom().getUnit());
				dto.setTendered(tendered);
				dto.setAccepted(accepted);
				dto.setRejected(rejected);
				dto.setQtyAcceptedToDate(clearedQty);
//				dto.setLedgerFolio(tendered);
				dto.setIndentNo(soLineItem.getLineItemNo());
				dto.setCatPartNo(soLineItem.getPartNumber().getCatPartNo());
				dto.setNsnPartNo(soLineItem.getPartNumber().getNsnPartNo());
				iNoteDto.setNomenclature(soLineItem.getPartNumber().getMaterialDescription());  
//			Code to be changed
				//dto.setQtyAcceptedToDate(
//						offerLetterRepository.getCountOfCompletedLineItemsBySoLiId(soLineItem.getId()));
				// registration nos
				if (supplyOrder.getIsRegistrationNo() != null && supplyOrder.getIsRegistrationNo()) {
//					List<LineItemLotsRegister> lineItemLotRegisters = sSlipLineItem.getLineItemLotsRegisters();
//					List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream().filter(reg -> Objects
//							.equals(reg.getStatus(), ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
//							.collect(Collectors.toList());
//					dto.setRegNos(
//							filtered.stream().map(LineItemLotsRegister::getRegisterNo).collect(Collectors.toList()));
					List<SubSlipSerialNo> subSlipSerialNos = subSlipSerialNoRepo.findBySubSlipLineItemIdOrderByIdAsc(sSlipLineItem.getId());
//					List<SubSlipSerialNo> lineItemLotRegisters = sSlipLineItem.getLineItemLotsRegisters();
//					List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream().filter(reg -> Objects
//							.equals(reg.getStatus(), ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
//							.collect(Collectors.toList());
					dto.setRegNos(
							subSlipSerialNos.stream().map(s -> s.getWarrantDetails().getLineItemSerialNo().getSerialNo()).toList());
				}
				
				String failedParam = specRepository.getFailedSpecForInternalInote(sSlipLineItem.getId());
				dto.setFailedParamDesc(failedParam);

				iNoteLineItemDtos.add(dto);
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setExisting(false);
			
			if(qcc.getProdApprovedBy() != null) {
				iNoteDto.setProdUserId(qcc.getProdApprovedBy().getId());
				iNoteDto.setProdUserName(qcc.getProdApprovedBy().getEname());
			}
			return iNoteDto;
		} else
			return null;
	}

}
