package in.co.bel.olms.transaction.service.so;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.exception.OlmsException;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.entity.OrderType;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.master.entity.RegistrationNoType;
import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.exception.DataInUseException;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ConsigneeRepository;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.ItemCategoryRepository;
import in.co.bel.olms.master.repository.OrderTypeRepository;
import in.co.bel.olms.master.repository.OrganizationRepository;
import in.co.bel.olms.master.repository.PartNumberRepository;
import in.co.bel.olms.master.repository.RegistrationNoTypeRepository;
import in.co.bel.olms.master.repository.SoTypeRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.LineItemDto;
import in.co.bel.olms.transaction.dto.SoLineItemDto;
import in.co.bel.olms.transaction.dto.SoLineItemInputDto;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.SoConsigneeMapping;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.OperationRepository;
import in.co.bel.olms.transaction.repository.SaleOrderRepository;
import in.co.bel.olms.transaction.repository.ShipRepository;
import in.co.bel.olms.transaction.repository.SoConsigneeMappingRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B, Awadhesh Verma
 * @since 24-11-22
 */

@Service
@RequiredArgsConstructor
public class SoLineItemService {
	
	@Value("${file.domain-url}")
	private String domainUrl;
	@Value("${spring.application.name}")
	private String applicationName;
	
	private final Map<String, Integer> itemCategoryMap;
	private final Map<String, Integer> uomMap;

	private final SoLineItemRepository soLineItemRepository;

	private final OrderTypeRepository orderTypeRepository;

	private final SaleOrderRepository saleOrderRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final FileStorageService fileStorageService;

	private final ItemCategoryRepository itemCategoryRepository;

	private final PartNumberRepository partNumberRepository;

	private final CategoryRepository categoryRepository;

	private final ConsigneeRepository consigneeRepository;

	private final SoConsigneeMappingRepository soConsigneeMappingRepository;

	private final OrganizationRepository organizationRepository;

	private final SoTypeRepository soTypeRepository;

	private final StatusRepository statusRepository;

	private final RegistrationNoTypeRepository registrationNoTypeRepository;

	private final OperationRepository operationRepository;

	private final ShipRepository shipRepository;

	private final DepartmentRepository departmentRepository;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(SoLineItemService.class);

	@Transactional
	public Map<Boolean, String> saveSoLineItems(SoLineItemInputDto soLineItemInputDto, Integer empId) {
		
		List<ItemCategory> itemCat = itemCategoryRepository.findAll();
		for(ItemCategory category : itemCat) {
			itemCategoryMap.put(category.getCategory(), category.getId());
		}

		Map<Boolean, String> returnMap = new HashMap<>();
		List<SOLineItem> soLineItems = new ArrayList<>();
		try {
			int supplyOrderId = soLineItemInputDto.getSupplyOrderId();
			List<SoLineItemDto> soLineItemDtos = soLineItemInputDto.getLineItemDtos();
			
			// create sale order
			
			// order master no --- sale order no
			// create so line item - one to one mapp
			// so line tiem.saleor( new created obj set)
			// save solin item
			
//			List<String> saleOrderNoList = soLineItemDtos.stream().map(SoLineItemDto::getSaleOrderNo)
//					.collect(Collectors.toList());
//			List<String> existingSaleOrderNoList = saleOrderRepository.getSaleOrderNosBySoId(supplyOrderId);

//			List<SaleOrder> saleOrderList = saleOrderRepository.findSaleOrderIds(saleOrderNoList);
			
//			Category category = categoryRepository.findById(null)
//			SaleOrder saleOrder  = saleOrderRepository.findById(soLineItemInputDto.getSaleOrderId()).get();
			
			SaleOrder newSaleOrder = new SaleOrder();
			newSaleOrder.setSaleOrderNo(soLineItemInputDto.getSaleOrderNo());
			newSaleOrder.setCreatedDate(new Date());
			newSaleOrder.setActive(true);
			newSaleOrder.setCreatedBy(empId);
			
//			saleOrder.setSaleOrderNo(soLineItemInputDto.getOrderMasterNo());
			
			
//			Map<String, SaleOrder> saleOrderMap = new HashMap<>();
//			for (SaleOrder so : saleOrderList) {
//				saleOrderMap.put(so.getSaleOrderNo(), so);
//			}
			Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(supplyOrderId);
			if (supplyOrderOptional.isPresent()) {
				SupplyOrder supplyOrder = supplyOrderOptional.get();
				newSaleOrder.setSupplyOrder(supplyOrder);
				for (SoLineItemDto soLineItemDto : soLineItemDtos) {
//					if (!existingSaleOrderNoList.contains(soLineItemDto.getSaleOrderNo().trim())) {
//						String respString = "Sale order '" + soLineItemDto.getSaleOrderNo()
//								+ "' doesn't belong to selected Supply Order.";
//						returnMap.put(false, respString);
//						return returnMap;
//					}
					
					//Check if this lineItemNo already exists for the given supplyOrderId.
					if (soLineItemRepository.existsBySupplyOrderIdAndLineItemNo(supplyOrderId, soLineItemDto.getLineItemNo())) {
				        returnMap.put(false, "Line Item No: " + soLineItemDto.getLineItemNo() + " already exists for this Supply Order.");
				        return returnMap;
				    }
					
					soLineItemDto.setOrderMasterNo(soLineItemDto.getOrderMasterNo());
					
					// Validate Unit Price
					String unitCostInput = soLineItemDto.getUnitCostStr();
					if (unitCostInput != null) {
						try {
							BigDecimal unitPrice = (unitCostInput != null && !unitCostInput.isEmpty())? new BigDecimal(unitCostInput): BigDecimal.ZERO;
							if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
								returnMap.put(false, "Unit Price must be a non-negative value for line item: "
										+ soLineItemDto.getLineItemNo());
								return returnMap;
							}
							soLineItemDto.setUnitCostStr(unitCostInput);
							soLineItemDto.setUnitCost(unitPrice);

						} catch (NumberFormatException e) {
							returnMap.put(false, "Invalid input for Unit Price for line item: "
									+ soLineItemDto.getLineItemNo() + ". Please enter a valid numeric value.");
							return returnMap;
						}
					}

					// Validate Total Quantity
					String qtyInput = soLineItemDto.getOrderQtyStr();
					if (qtyInput != null) {
						try {

							BigDecimal	totalQuantity = (qtyInput != null && !qtyInput.isEmpty()) 
					                ? new BigDecimal(unitCostInput) 
					                : BigDecimal.ZERO;
							
							if (totalQuantity.compareTo(BigDecimal.ZERO)< 0) {
								returnMap.put(false, "Total Quantity must be a non-negative value for line item: "
										+ soLineItemDto.getLineItemNo());
								return returnMap;
							}
							soLineItemDto.setOrderQtyStr(qtyInput);
							soLineItemDto.setOrderQty(totalQuantity);
						}

						catch (NumberFormatException e) {
							returnMap.put(false, "Invalid input for Total Quantity for line item: "
									+ soLineItemDto.getLineItemNo() + ". Please enter a valid integer value.");
							return returnMap;
						}
					}

					String durationTypeString = soLineItemDto.getDurationTypeStr();
					if (StringUtils.containsIgnoreCase(durationTypeString, "year")) {
						soLineItemDto.setDurationTypeId(ApplicationConstants.YEARS);
					} else if (StringUtils.containsIgnoreCase(durationTypeString, "month")) {
						soLineItemDto.setDurationTypeId(ApplicationConstants.MONTHS);
					} else if (StringUtils.containsIgnoreCase(durationTypeString, "day")) {
						soLineItemDto.setDurationTypeId(ApplicationConstants.DAY);
					}
//					String itemCategory = soLineItemDto.getItemCategoryName();
//					String uom = soLineItemDto.getUomName();
//					
//					if (itemCategory == null || !itemCategoryMap.containsKey(itemCategory)) {
//	                    returnMap.put(false, "Invalid item category for line item: " + soLineItemDto.getLineItemNo());
//	                    return returnMap;
//	                }
//					if (uom == null || !uomMap.containsKey(uom)) {
//	                    returnMap.put(false, "Invalid UoM for line item: " + soLineItemDto.getLineItemNo());
//	                    return returnMap;
//	                }					
					
					//Setting the Item Category Id from On Startup initialized Map
//					soLineItemDto.setItemCategoryId(itemCategoryMap.get(itemCategory));
//					soLineItemDto.setUomId(uomMap.get(uom));
//					//Setting the UoM Id from On Startup initialized Map
					SOLineItem soLineItem = mapper.map(soLineItemDto, SOLineItem.class);										
					soLineItem.setIsImported(soLineItemDto.getIsImported());
					
//					if (soLineItemDto.getDeptId() != null) {
//					    Optional<Department> deptOptional = departmentRepository.findById(soLineItemDto.getDeptId());
//					    if (deptOptional.isPresent()) {
//					        soLineItem.setDeptId(deptOptional.get());
//					    } else {
//					        
//					        throw new OlmsException("Invalid Department ID: " + soLineItemDto.getDeptId());
//					    }
//					}
					
//					
////					if (soLineItemDto.getIsImported()) {						
////					    if (soLineItemDto.getPartNo() != null) {
//					        Optional<PartNumber> partNumberOptional = partNumberRepository.findByPartNoId(soLineItemDto.getPartNumberId());
//					        if (partNumberOptional.isPresent()) {
//					            soLineItem.setPartNumber(partNumberOptional.get());
//					            soLineItem.setPartNo(partNumberOptional.get().getPartNo());
//					        } else {
//					        	throw new OlmsException("Invalid Material No./EP Code No.: "+ soLineItemDto.getPartNo());
//					        }
////					    } else {
////					    	throw new OlmsException("Material No./EP Code No. cannot be empty");
////					    }
////					}
					        
					    	
							if (!soLineItemDto.getIsImported()) {						
							    if (soLineItemDto.getPartNo() != null) {
							        Optional<PartNumber> partNumberOptional = partNumberRepository.findByPartNoId(soLineItemDto.getPartNumberId());
							        if (partNumberOptional.isPresent()) {
							            soLineItem.setPartNumber(partNumberOptional.get());
							            soLineItem.setPartNo(partNumberOptional.get().getPartNo());
							        } else {
							        	throw new OlmsException("Invalid Material No./EP Code No.: "+ soLineItemDto.getPartNo());
							        }
							    } 
//							        else {
//							    	throw new OlmsException("Material No./EP Code No. cannot be empty");
//							    }
							} else {
								 if (soLineItemDto.getPartNo() != null) {
									 Optional<PartNumber> partNumberOptional = partNumberRepository.findByPartNo(soLineItemDto.getPartNo());
								        if (partNumberOptional.isPresent()) {
								            soLineItem.setPartNumber(partNumberOptional.get());
								            soLineItem.setPartNo(partNumberOptional.get().getPartNo());
								        } else {
								        	throw new OlmsException("Invalid Material No./EP Code No.: "+ soLineItemDto.getPartNo());
								        }
								 }
							}
					soLineItem.setCreatedDateTime(new Date());
					soLineItem.setIsUnderWarranty(soLineItemDto.isUnderWarranty());
					soLineItem.setSupplyOrder(supplyOrder);
					
					try {
						BigDecimal itemValue = soLineItem.getUnitCost().multiply(soLineItem.getOrderQty());
						logger.debug("value is {}" , itemValue);
						
						logger.info("RAW MULTIPLICATION: {}", itemValue.toPlainString());
						
						BigDecimal roundedValue = itemValue.setScale(2, RoundingMode.HALF_UP);
						
						logger.info("ROUNDED VALUE: {}", roundedValue.toPlainString());
						soLineItem.setItemValue(roundedValue);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("error {}", e.getMessage());
					}
					
					
					
//					soLineItem.setSaleOrder(saleOrderMap.get(soLineItemDto.getSaleOrderNo()));
					soLineItem.setStatus(ApplicationConstants.ACTIVE);
					SaleOrder savedSaleOrder = saleOrderRepository.save(newSaleOrder);
					soLineItem.setSaleOrder(savedSaleOrder);
					soLineItem.setCreatedBy(empId);
					soLineItem.setStatusId(statusRepository.findById(14).get());
					
					soLineItems.add(soLineItem);
				    
					List<Integer> consigneeIds = soLineItemDto.getConsigneeIds();
					
					if( consigneeIds != null && !consigneeIds.isEmpty()) {
						List<SoConsigneeMapping> soConsigneeMappings = new ArrayList<SoConsigneeMapping>();
						consigneeIds.forEach(consigneeId -> {
							SoConsigneeMapping soConsigneeMapping = new SoConsigneeMapping();
							Consignee con = consigneeRepository.findById(consigneeId).orElseThrow(() -> new IllegalArgumentException("Consignee not found!"));
							soConsigneeMapping.setConsignee(con);
							soConsigneeMapping.setSupplyOrder(supplyOrder);
							soConsigneeMapping.setSoLineitem(soLineItem);
							soConsigneeMappings.add(soConsigneeMapping);
						});
						soConsigneeMappingRepository.saveAll(soConsigneeMappings);
					} else {
						throw new IllegalArgumentException("Consignee ids cannot be null/empty.");
					}
				}

				List<SOLineItem> savedSoLineItems = soLineItemRepository.saveAll(soLineItems);

				BigDecimal totalVal = savedSoLineItems.stream()
						.map(soLineItem1 -> soLineItem1.getUnitCost().multiply(soLineItem1.getOrderQty()))
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal roundedTotalValue = totalVal.setScale(2, RoundingMode.HALF_UP);
				supplyOrder.setValue(roundedTotalValue);
				
				//31-07-26
//				double totalVal = savedSoLineItems.stream().mapToDouble(soLineItem1 -> soLineItem1.getUnitCost() * soLineItem1.getOrderQty()).sum();
//				supplyOrder.setValue(totalVal);
				BigDecimal totalQty = savedSoLineItems.stream().map(SOLineItem::getOrderQty).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				int totalItems = savedSoLineItems.size();
				supplyOrder.setTotalQty(totalQty);
				supplyOrder.setValue(totalVal);
				supplyOrder.setNoOfItems(totalItems);
				supplyOrder.setCreatedBy(empId);
//				supplyOrder.setSoStatus(soStatusRepository.findById(6).get());
				supplyOrderRepository.save(supplyOrder);
//				supplyOrder.setTotalQty(totalQty);
//				supplyOrder.setValue(totalVal);
//				
//				supplyOrderRepository.save(supplyOrder);

				// creating a notification for the CQAE user
				/*
				 * NotificationDto notificationDto = new NotificationDto();
				 * notificationDto.setNotificationName("New Supply Order created with Id " +
				 * supplyOrder.getOrderNo()); notificationDto.setNotificationCategory("SO");
				 * notificationDto.setCategoryId(supplyOrder.getId());
				 * notificationService.saveNotification(notificationDto); if
				 * (supplyOrder.getOrganization() != null) { List<Integer> receivers =
				 * employeeRepository
				 * .getJCnAdminIdsByOrgId(supplyOrder.getOrganization().getId()); MessageBean
				 * message = new MessageBean();
				 * message.setSender(soLineItemInputDto.getCreatedBy());
				 * message.setMessageType(ApplicationConstants.SUPPLY_ORDER);
				 * message.setMessage("New Supply Order created with Id: " +
				 * supplyOrder.getOrderNo()); message.setItemNo(supplyOrder.getOrderNo());
				 * message.setReceivers(receivers); wsService.notifyCqae(message); }
				 */
				String respString = "Data saved successfully";
				returnMap.put(true, respString);
				return returnMap;
			} else {
				throw new OlmsException("No Sale Order found with given id.");
			}
		}catch (OlmsException ex) {		
			logger.error("error:{}",ex.getMessage());
            returnMap.put(false, ex.getMessage());
            return returnMap;
        } 
		catch (Exception e) {
			logger.error("Error in saving so line item details {}", e.getMessage());
			logger.error("error:",e);
			String respString = "Some error occurred. Please try again.";
			returnMap.put(false, respString);
			return returnMap;
		}

	}

	public SOLineItem saveLineItem(LineItemDto lineItemDto) {

		SOLineItem lineItem = mapper.map(lineItemDto, SOLineItem.class);
		lineItem.setCreatedDateTime(new Date());
		return soLineItemRepository.save(lineItem);

	}

	public SOLineItem getSoLineItemById(int id) {
		Optional<SOLineItem> soLineItemOptional = soLineItemRepository.findById(id);
		if (soLineItemOptional.isPresent()) {
			return soLineItemOptional.get();
		} else {
			throw new DataNotFoundException("SoLineItem not found by Id: " + id);
		}
	}

	public List<SOLineItem> getSoLineItemsBySoId(int soId) {

		return soLineItemRepository.getSoLineItemsBySoId(soId);

	}
	
	public String getConsigneesBySoId(int soId)
	{
		return soLineItemRepository.getConsigneesBySoId(soId);
	}
	public String getConsigneesBySliId(int sliId)
	{
		return soLineItemRepository.getConsigneesBySliId(sliId);
	}
	
	public List<Integer> getConsigneesIdBySliId(int sliId)
	{
		return soLineItemRepository.getConsigneesIdBySliId(sliId);
	}
	public List<SoLineItemDto> getSoLineItemDetails(int soLineItemId) throws ParseException {

		List<Object[]> soLi=soLineItemRepository.getSoLineItemDetails(soLineItemId);
		List<SoLineItemDto> soLineItemDtos = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		for (Object[] obj : soLi) {
			SoLineItemDto soLineItemDto = new SoLineItemDto();
			soLineItemDto.setEpCode(obj[0] != null ? obj[0].toString() : null);
			soLineItemDto.setSaleOrderNo(obj[1] != null ? obj[1].toString() : null);
			soLineItemDto.setOrderMasterNo(obj[2] != null ? obj[2].toString() : null);
			soLineItemDto.setLineItemNo(obj[3] != null ? obj[3].toString() : null);
			soLineItemDto.setInspectionAuthorityId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : null);
			soLineItemDto.setSupplyType(obj[5] != null ? Integer.parseInt(obj[5].toString()) : null);
			soLineItemDto.setItemCategoryId(obj[6] != null ? Integer.parseInt(obj[6].toString()) : null);
			soLineItemDto.setDrawingNo(obj[7] != null ? obj[7].toString() : null);
			soLineItemDto.setCatPartNo(obj[8] != null ? obj[8].toString() : null);
			soLineItemDto.setNsnPartNo(obj[9] != null ? obj[9].toString() : null);
			soLineItemDto.setUomId(obj[10] != null ? Integer.parseInt(obj[10].toString()) : null);
			soLineItemDto.setMaterialDescription(obj[11] != null ? obj[11].toString() : null);
			soLineItemDto.setUnitCost(obj[12] != null ? new BigDecimal(obj[12].toString()) : null);
			soLineItemDto.setDeliveryDate(obj[13] != null ? sdf.parse(obj[13].toString()) : null);
			soLineItemDto.setOrderQty(obj[14] != null ? new BigDecimal(obj[14].toString()) : null);
			soLineItemDto.setConsignee(obj[15] != null ? (obj[15].toString()) : null);
			soLineItemDto.setRefNo(obj[16] != null ? obj[16].toString() : null);
			soLineItemDto.setOrderTypeId(obj[17] != null ? Integer.parseInt(obj[17].toString()) : null);
			soLineItemDto.setOrderTypeNo(obj[18] != null ? obj[18].toString() : null);
			soLineItemDto.setOrderTypeDate(obj[19] != null ? sdf.parse(obj[19].toString()) : null);
			soLineItemDto.setGem(obj[20] != null ? obj[20].toString() : null);
			soLineItemDto.setSelfCertification(obj[21] != null ? obj[21].toString() : null);
			soLineItemDto.setRemarks(obj[22] != null ? obj[22].toString() : null);
			soLineItemDto.setIsImported(obj[23] != null ? (Boolean) obj[23] : null);
			soLineItemDto.setGcs(obj[24] != null ? (Boolean) obj[24] : null);
			soLineItemDto.setStatus(obj[25] != null ? obj[25].toString() : null);
			soLineItemDto.setRegistrationNo(obj[27] != null ? Integer.parseInt(obj[27].toString()) : null);
			String consigneeIdsString = (String) obj[26];
		    List<Integer> consigneeIds = null;

		    if (consigneeIdsString != null && !consigneeIdsString.isEmpty()) {
	            consigneeIds = Arrays.stream(consigneeIdsString.split(","))
	                                 .map(idStr -> idStr.trim()) 
	                                 .filter(idStr -> !idStr.isEmpty())
	                                 .map(Integer::parseInt)
	                                 .toList();
		    }

		    soLineItemDto.setConsigneeIds(consigneeIds);
		    
			soLineItemDtos.add(soLineItemDto);
		}
		return soLineItemDtos;

	}
	
//	public int  getRemainingQtyBySoLiId(int soId) {
//
//		   Integer remainingQty  = soLineItemRepository.getRemainingQtyBySoLiId(soId);
//		   
//       return (remainingQty == null) ? 0 : remainingQty; 
//
//	}
	

	public List<SOLineItem> getAllSoLineItems() {
		return soLineItemRepository.findAll();
	}

	public Boolean x(@Valid SoLineItemDto soLineItemDto) {
		
		return null;
	}

	public List<SOLineItem> getSoLineItemsBySaleOrderId(int saleOrderId) {
		return soLineItemRepository.getSoLineItemsBySaleOrderId(saleOrderId);
	}
	
	@Transactional
	public SoLineItemDto editSoLineItem(SoLineItemDto soLineItemDto, MultipartFile tmrFile) {
		try {
			Optional<SOLineItem> lineItemOptional = soLineItemRepository.findById(soLineItemDto.getId());

			if (lineItemOptional.isPresent()) {
				SOLineItem soLineItem = lineItemOptional.get();
				SupplyOrder supplyOrder = soLineItem.getSupplyOrder();
//				SaleOrder saleOrder=soLineItem.getSaleOrder();
//				saleOrder.setSaleOrderNo(soLineItemDto.getSaleOrderNo());
//				soLineItem.setSaleOrder(saleOrder);
				Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(soLineItemDto.getSaleOrderId());
				if(saleOrderOptional.isPresent())
					soLineItem.setSaleOrder(saleOrderOptional.get());
				
				soLineItem.setLineItemNo(soLineItemDto.getLineItemNo());				
				soLineItem.setPartNo(soLineItemDto.getPartNo());
				soLineItem.setOrderMasterNo(soLineItemDto.getOrderMasterNo());
				Organization insp = new Organization();
				Optional<Organization> orgOpt = organizationRepository.findById(soLineItemDto.getInspectionAuthorityId());
				if(orgOpt.isPresent()) {
					insp = orgOpt.get();
					soLineItem.setInspectionAuthorityId(insp);
				}
				SoType soType=new SoType();
				Optional<SoType> soTypeOpt = soTypeRepository.findById(soLineItemDto.getSupplyType());
				if(soTypeOpt.isPresent()) {
					soType = soTypeOpt.get();
					soLineItem.setSupplyType(soType);
				}
				RegistrationNoType regNoType = new RegistrationNoType();
				Optional<RegistrationNoType> regNoTypeOpt = registrationNoTypeRepository.findById(soLineItemDto.getRegistrationNo());
				if(regNoTypeOpt.isPresent()) {
					regNoType = regNoTypeOpt .get();
					soLineItem.setRegistrationNo(regNoType);
				}
				soLineItem.setRefNo(soLineItemDto.getRefNo());
        if (soLineItemDto.getOrderTypeDate() != null) {
          soLineItem.setOrderTypeDate(soLineItemDto.getOrderTypeDate());
        }
        if (soLineItemDto.getOrderTypeId() != null) {
          OrderType orderType = orderTypeRepository.findById(soLineItemDto.getOrderTypeId())
              .orElseThrow(() -> new IllegalArgumentException("ordertype not found"));
          soLineItem.setOrderTypeId(orderType);
        }
        if (soLineItemDto.getOrderTypeNo() != null) {
          soLineItem.setOrderTypeNo(soLineItemDto.getOrderTypeNo());
        }
				if(soLineItemDto.getPartNumberId() != null) {
          PartNumber partNumber = partNumberRepository.findById(soLineItemDto.getPartNumberId())
              .orElseThrow(() -> new IllegalArgumentException("Part Number not found"));
          soLineItem.setPartNumber(partNumber);
        }
        if (soLineItemDto.getDeliveryDate() != null) {
          soLineItem.setDeliveryDate(soLineItem.getDeliveryDate());
				}
				soLineItem.setDescription(soLineItemDto.getDescription());
				soLineItem.setUnitCost(soLineItemDto.getUnitCost());
				soLineItem.setDeliveryDate(soLineItemDto.getDeliveryDate());

//		Optional<Uom> uomOptional = uomRepository.findById(soLineItemDto.getUomId());
//				if(uomOptional.isPresent())
//					soLineItem.setUom(uomOptional.get());
//				
//			Optional<ItemCategory> itemCategoryOptional = itemCategoryRepository.findById(soLineItemDto.getItemCategoryId());
//				if(itemCategoryOptional.isPresent())
//					soLineItem.setItemCategory(itemCategoryOptional.get());
//				

				soLineItem.setOrderQty(soLineItemDto.getOrderQty());
				
				soLineItem.setIsGcs(soLineItemDto.getGcs());
				soLineItem.setGem(soLineItemDto.getGem());
				soLineItem.setSelfCertification(soLineItemDto.getSelfCertification());
				soLineItem.setStatus(soLineItemDto.getStatus());
				soLineItem.setImported(soLineItemDto.getIsImported());
				soLineItem.setRemarks(soLineItemDto.getRemarks());
//				soLineItem.setNsnPartNo(soLineItemDto.getNsnPartNo());
//				soLineItem.setCatPartNo(soLineItem.getCatPartNo());
				mapper.map(soLineItem, soLineItemDto);
				if(soLineItem.getTmrFileName() != null) {
					fileStorageService.deleteFile(soLineItem.getTmrFileName());
				}
				if (tmrFile != null) {
					String fileName = fileStorageService.storeFile(tmrFile, soLineItem.getId(), 1);
					soLineItem.setTmrFileName(fileName);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/" + fileName;
					soLineItemDto.setTmrFileUrl(fileDownloadUri);
				}
//				List<Consignee> consignees = consigneeRepository.getConsigneesByIds(consigneeIds);
				
				updateConsigneeMappings(soLineItem, supplyOrder, soLineItemDto.getConsigneeIds());
//				List<SoConsigneeMapping> consigneeMapping = soConsigneeMappingRepository.findBySoLineItem(soLineItemDto.getId());
				
				
				soLineItemRepository.save(soLineItem);
				BigDecimal totalVal = supplyOrder.getSoLineItems().stream()
						.map(soLineItem1 -> soLineItem1.getUnitCost().multiply(soLineItem1.getOrderQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
				
				BigDecimal roundedValue = totalVal.setScale(2, RoundingMode.HALF_UP);				
				supplyOrder.setValue(roundedValue);
				BigDecimal totalQty = supplyOrder.getSoLineItems().stream().map(SOLineItem::getOrderQty).reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal roundedQty = totalQty.setScale(2, RoundingMode.HALF_UP);				
				supplyOrder.setTotalQty(roundedQty);
				supplyOrderRepository.save(supplyOrder);

			}
		} catch (Exception e) {
			logger.error("error: ",e);
			logger.error("Error in  updating supply order attachments{}", e.getMessage());
		}
		return soLineItemDto;
	}
	
	private void updateConsigneeMappings(SOLineItem soLineItem, SupplyOrder supplyOrder, List<Integer> newConsigneeIds) {
	    // DELETE existing mappings for this Line Item

	    soConsigneeMappingRepository.deleteBySoLineitemId(soLineItem.getId());

	    //  ADD new mappings from the payload
	    if (newConsigneeIds != null && !newConsigneeIds.isEmpty()) {
	        List<SoConsigneeMapping> newMappings = new ArrayList<>();
	        
	        for (Integer cId : newConsigneeIds) {
	            Consignee con = consigneeRepository.findById(cId)
	                    .orElseThrow(() -> new IllegalArgumentException("Consignee not found with ID: " + cId));
	            
	            SoConsigneeMapping mapping = new SoConsigneeMapping();
	            mapping.setConsignee(con);
	            mapping.setSupplyOrder(supplyOrder);
	            mapping.setSoLineitem(soLineItem);
	            newMappings.add(mapping);
	        }
	        soConsigneeMappingRepository.saveAll(newMappings);
	    }
	}
	
	public void deleteSoLi(int soLiId) {
		soLineItemRepository.deleteById(soLiId);
	}
	
	
	@Transactional
	public void deleteLineItem(int lineItemId) {
		// Check if the line item is being used
		String olNumber = soLineItemRepository.findOlNumberBySoLineItemId(lineItemId);
		if (olNumber != null) {
			throw new DataInUseException("Can't delete this line item as it is being used in OL No: " + olNumber);
		}

		soConsigneeMappingRepository.deleteBySoLineitemId(lineItemId);

		Integer warrantId = categoryRepository.getCategoryIdBySoLineitemId(lineItemId);
		 if (warrantId != null) {
		        operationRepository.deleteByWarrantId(warrantId);
		    }

		categoryRepository.deleteBySoLineitemId(lineItemId);
    
		shipRepository.deleteBySoLineitemId(lineItemId);
		
		soLineItemRepository.deleteById(lineItemId);
		
	}


//	// AV
//	// Sending a message to web socket to notify the CAQE user on SO creation
//	public boolean sendMsgToCqae(int staffId, String itemNo) {
//		boolean response = true;
//		try {
//			MessageBean message = new MessageBean("newSOCreated", staffId, itemNo);
//			wsService.notifyCqae(message);
//		} catch (Exception e) {
//			response = false;
//			logger.info("Failed to send message to topic on SO creation {}", e.getMessage());
//		}
//		return response;
//	}

}
