package in.co.bel.olms.transaction.service.so;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.exception.OlmsException;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.SerialNoStatusRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.dto.ProjectDataDto;
import in.co.bel.olms.transaction.dto.ProjectExcelDto;
import in.co.bel.olms.transaction.dto.ProjectSummaryDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.Operation;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.Ship;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.WarrantDetails;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OperationRepository;
import in.co.bel.olms.transaction.repository.ProjectRepository;
import in.co.bel.olms.transaction.repository.SaleOrderRepository;
import in.co.bel.olms.transaction.repository.ShipRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.repository.WarrantDetailsRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private final ProjectRepository projectRepository;

	private final CategoryRepository categoryRepository;

	private final SaleOrderRepository saleOrderRepository;

	private final ShipRepository shipRepository;

	private final OperationRepository opRepo;

	private final SupplyOrderRepository supplyOrderRepository;

	private final SoLineItemRepository soLineItemRepo;

	private final StatusRepository statusRepository;

	private final SerialNoStatusRepository serialNoStatusRepository;

	private final LineItemSerialNoRepository lineItemSerialNoRepository;

	private final EmployeeRepository employeeRepository;

	private final WarrantDetailsRepository warrantDetailsRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Project getProjectById(int id) {

		Optional<Project> projectOptional = projectRepository.findById(id);
		if (projectOptional.isPresent()) {
			return projectOptional.get();
		} else {
			throw new DataNotFoundException("Project not found by Id: " + id);
		}

	}

	public List<ProjectDataDto> getProjectBySoId(int id) {

		List<ProjectDataDto> projectDataDtos = new ArrayList<>();
		List<Object[]> projectDetails = null;
		try {

			if (id != 0) {
				projectDetails = projectRepository.findProjectDetailsBySoId(id);
			} else {
				projectDetails = projectRepository.findAllProjectDetails();
			}
			if (!projectDetails.isEmpty()) {
				for (Object[] projectDetail : projectDetails) {
					ProjectDataDto projectDataDto = new ProjectDataDto();
          projectDataDto.setProjectName(projectDetail[0] != null ? projectDetail[0].toString() : null);
          projectDataDto.setShipNo(projectDetail[1] != null ? projectDetail[1].toString() : null);
          projectDataDto.setCategoryName(projectDetail[2] != null ? projectDetail[2].toString() : null);
//					projectDataDto.setSaleOrderNo(projectDetail[3].toString());
//					if (projectDetail[4] != null) {
//						String status = projectDetail[4].equals("true") ? "Active" : "Inactive";
//						projectDataDto.setStatus(status);
//					}
//					if (projectDetail[5] != null) {
//						projectDataDto.setSaleOrderId(Integer.valueOf(projectDetail[5].toString()));
//					}
					projectDataDto.setEstDate(projectDetail[3]!= null ? (Date)projectDetail[3]: null);
					projectDataDto.setRevNo(projectDetail[4]!= null ? (Integer)projectDetail[4]: null);
					projectDataDto.setWtIssueDate(projectDetail[5]!= null ? (Date)projectDetail[5]: null);
					projectDataDtos.add(projectDataDto);
				}
			}
			return projectDataDtos;
		} catch (Exception e) {
			logger.error("Error occured in getting Project by SoId ", e);
      logger.error(e.getMessage());
			throw new OlmsException("project not found by supply order Number:" + id);

		}
	}

	/**
	 * 
	 * @return
	 */
	public List<Project> getAllProject() {
		return projectRepository.findAll();
	}

	public List<Object[]> findAllProjects() {
		return projectRepository.getAllProjects();
	}


	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public List<ProjectDataDto> getProjectForSalesGrp(int empId) {

		List<ProjectDataDto> projectDataDtos = new ArrayList<>();
		List<String[]> projectDetails = null;
		try {
			if (empId != 0) {
				projectDetails = projectRepository.getProjectForSalesGrp(empId);
				for (String[] projectDetail : projectDetails) {
					ProjectDataDto projectDataDto = new ProjectDataDto();
					projectDataDto.setProjectName(projectDetail[0]);
					projectDataDto.setShipNo(projectDetail[1]);
					projectDataDto.setCategoryName(projectDetail[2]);
					projectDataDto.setSaleOrderNo(projectDetail[3]);
					projectDataDtos.add(projectDataDto);
				}
			}
			return projectDataDtos;
		} catch (Exception e) {
			throw new OlmsException("Projects not found for the given sales order");
		}
	}

	public List<Object[]> getAllByOrgId(int orgId, int salesGrpId) {
		return projectRepository.getAllByOrgId(orgId, salesGrpId);
	}

	public Map<Boolean, String> updateProject(@Valid ProjectDataDto projectDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		try {
//			List<String> saleOrderNoList = saleOrderRepository.findSaleOrderNos();
//			if (!saleOrderNoList.contains(projectDto.getSaleOrderNo())) {

			Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(projectDto.getSaleOrderId());
			if (saleOrderOptional.isPresent()) {

				Date date = new Date();
				SaleOrder saleOrder = saleOrderOptional.get();
				saleOrder.setSaleOrderNo(projectDto.getSaleOrderNo());
				saleOrder.setModifiedDate(date);

				Category category = saleOrder.getCategory();
				category.setCategoryName(projectDto.getCategoryName());
				category.setModifiedDate(date);

				Ship ship = category.getShip();
				ship.setShipName(projectDto.getShipNo());
				ship.setModifiedDate(date);

				Project project = ship.getProject();
				project.setProjectName(projectDto.getProjectName());
				if (projectDto.getStatus() != null) {
					boolean status = projectDto.getStatus().equalsIgnoreCase("Active");
					saleOrder.setActive(status);
					saleOrder.setModifiedDate(date);
//						saleOrderRepository.save(saleOrder);
//						returnMap.put(true, "Sale Order updated successfully.");
				}

//					saleOrderRepository.save(saleOrder);
//					categoryRepository.save(category);
//					shipRepository.save(ship);
//				} else {
//					returnMap.put(false, "No details found with given sale order id");
//				}
				projectRepository.save(project);
				shipRepository.save(ship);
				categoryRepository.save(category);
				saleOrderRepository.save(saleOrder);

				returnMap.put(true, "Sale Order updated successfully.");
			} else {
				String respString = "Sale Order doesn't exist with name:" + projectDto.getSaleOrderNo();
				logger.error(respString);
				returnMap.put(false, respString);
			}
		} catch (Exception e) {
			logger.error("Error in updating sale order details {}", e.getMessage());
			return returnMap;
		}
		return returnMap;
	}

	@Transactional
	public Map<Boolean, String> saveProjectsV3(ProjectExcelDto projectExcelDto, int empId) {
		
		Map<Boolean, String> returnMap = new HashMap<>();
		String projName = null;
		String shipNo = null;
		List<ProjectDataDto> lProjectDataDtos = projectExcelDto.getDataDtos();

		try {
			for (ProjectDataDto projDto : lProjectDataDtos) {
				projName = projDto.getProjectName();
				shipNo = projDto.getShipNo();
				if (StringUtils.isNotBlank(projName)) {
					if (!InputNameValidation.checkString(projName)) {
						throw new DataNotValidException("Invalid project name");
					}
				}

				if (StringUtils.isNotBlank(shipNo)) {
					if (!InputNameValidation.checkString(shipNo)) {
						throw new DataNotValidException("Invalid ship no");
					}
				}
			}
			List<ProjectDataDto> projectDataDtos = projectExcelDto.getDataDtos();
//			List<String> saleOrderNoList = saleOrderRepository.findSaleOrderNos();
//			List<ProjectDataDto> distinctProjects = projectDataDtos.stream()
//					.filter(distinctByKey(p -> p.getProjectName())).collect(Collectors.toList());

			SOLineItem soLineItem = soLineItemRepo.findById(projectExcelDto.getSoLiId()).orElseThrow(() -> new IllegalArgumentException("SO line item not found."));
			soLineItemRepo.updateStatus(soLineItem.getId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.IN_PROGRESS);
			SupplyOrder supplyOrder = soLineItem.getSupplyOrder();
			if (supplyOrder != null) {
//				List<Project> existingProjects = supplyOrder.getProjects();
//				Set<Project> projects = new HashSet<>(existingProjects); // epcode.desc
			//	Set<Ship> ships = new HashSet<>(); // Estimation no and date
				


				Project project = new Project();
//				for (ProjectDataDto projectDataDto : distinctProjects) {
					String projectName = soLineItem.getPartNumber().getMaterialDescription();
//					if (!existingProjectNames.contains(projectName)) {
						project.setProjectName(projectName);
						project.setCreatedDate(new Date());
						project.setActive(true);
						project.setSupplyOrder(supplyOrder);
//					}
//				}
				Project savedProject = projectRepository.save(project);
				//Set<SaleOrder> saleOrders = new HashSet<>();
				for (ProjectDataDto projectDataDto : projectDataDtos) {
					Ship ship = new Ship();
					ship.setShipName(projectDataDto.getShipNo());
					ship.setCreatedDate(new Date());
                    ship.setEstDate(projectDataDto.getEstDate());
                    ship.setRevNo(projectDataDto.getRevNo());
                    ship.setSoLineItem(soLineItem);
//					Project project = projects.stream()
//							.filter(p -> p.getProjectName().equals(projectDataDto.getProjectName()))
//							.collect(Collectors.toList()).get(0);
					ship.setProject(savedProject);
					Ship savedShip = shipRepository.save(ship);
//					ships.add(ship);
                    if(projectDataDto.getCategoryName()!=null && categoryRepository.existsByCategoryName(projectDataDto.getCategoryName()))
                   {
	throw new IllegalArgumentException("Warrant No: '"+ projectDataDto.getCategoryName( )+ "' already exists!");
                  }
					Category category = new Category();
					

					category.setSoLineItem(soLineItem);
					category.setCategoryName(projectDataDto.getCategoryName());
				    category.setWtIssueDate(projectDataDto.getWtIssueDate());
					category.setCreatedDate(new Date());
					category.setShip(savedShip);
					LineItemSerialNo savedlineItemSerialNo = new LineItemSerialNo();
					// Serial no generation if line item is WITHOUT_REGISTRATION_NO TYPE
					if(soLineItem.getRegistrationNo().getId() == ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
						category.setQty(soLineItem.getOrderQty());
						 LineItemSerialNo lineItemSerialNo = new LineItemSerialNo();
						 String regCount = String.format("%05d", lineItemSerialNoRepository.count() + 1);
							String registerNo = "WRN"  + regCount;
				            lineItemSerialNo.setSerialNo(registerNo);
				            
				            Integer serialNoStatusId = SERIAL_NO_STATUS_CONSTANTS.CREATED;
				            SerialNoStatus status1 = serialNoStatusRepository.findById(serialNoStatusId)
				            		.orElseThrow(() -> new IllegalArgumentException("Serial Status with ID " + serialNoStatusId + " not found"));
				    		lineItemSerialNo.setStatus(status1);
				            lineItemSerialNo.setCreatedAt(new Date());	
				            lineItemSerialNo.setSoLineItem(soLineItem);
				
				            savedlineItemSerialNo = lineItemSerialNoRepository.save(lineItemSerialNo);
					}
					else {
						category.setQty(projectDataDto.getQty());
					}
					
					category.setSerialNoStatus(statusRepository.findById(ApplicationConstants.COMMON_STATUS_CONSTANTS.CREATED).get());
					Category savedCategory = categoryRepository.save(category);
					
					// warrant to reg mapping if line item is WITHOUT_REGISTRATION_NO TYPE
					if(soLineItem.getRegistrationNo().getId() == ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
						WarrantDetails warrRegMap = new WarrantDetails();
						warrRegMap.setCreatedDate(new Date());
						warrRegMap.setCategory(savedCategory);
						Employee employee = employeeRepository.findById(empId).orElse(null);
						warrRegMap.setCreatedBy(employee);
						warrRegMap.setLineItemSerialNo(savedlineItemSerialNo);
						 SerialNoStatus status = serialNoStatusRepository.findById(SERIAL_NO_STATUS_CONSTANTS.MAPPED)
							        .orElseThrow(() -> new IllegalArgumentException("Status not found"));
						
						warrRegMap.setSerialNoStatus(status);
						WarrantDetails savedWarrantDetails = warrantDetailsRepository.save(warrRegMap);
						savedlineItemSerialNo.setStatus(status);
                        lineItemSerialNoRepository.save(savedlineItemSerialNo);
					}
					
					if(projectDataDto.getOpNo() != null){
						Operation op = new Operation();
						op.setOpNo(projectDataDto.getOpNo());
						op.setWarrant(savedCategory);
						opRepo.save(op);
					}

//					String saleOrderNo = projectDataDto.getSaleOrderNo();
//					if (!saleOrderNoList.contains(saleOrderNo)) {
//						SaleOrder saleOrder = new SaleOrder();
//						saleOrder.setSaleOrderNo(saleOrderNo);
//						saleOrder.setCreatedDate(new Date());
//						saleOrder.setActive(true);
//						saleOrder.setCategory(savedCategory);
//						saleOrder.setSupplyOrder(supplyOrder);
//						saleOrders.add(saleOrder);
//					} else {
//						String respString = "Work order no. " + saleOrderNo + " already exists";
//						logger.error(respString);
//						returnMap.put(false, respString);
//						return returnMap;
//					}
				}

//				shipRepository.saveAll(ships);
			//	saleOrderRepository.saveAll(saleOrders);
				supplyOrderRepository.save(supplyOrder);
				returnMap.put(true, "Data saved successfully.");
			} else {
				returnMap.put(false, "No supply order found with given id:" + soLineItem.getSupplyOrder().getId());
			}
			return returnMap;
		} catch (DataNotValidException de) {
			logger.error("data not valid: ",de);
			logger.error("Validation failed: {}", de.getMessage());
			throw de;
		} catch (Exception e) {
			logger.error("Error occured while saving projects ", e);
			logger.error("Error in saving projects {}", e.getMessage());
			returnMap.put(false,e.getMessage());
			return returnMap;
		}
	}

	public Map<Boolean, String> updateProjectsV3(CategoryDto dataDto) {
		Map<Boolean, String> returnMap = new HashMap<>();

		try {

			if (dataDto.getId() == null)
				throw new IllegalArgumentException("Category id must not be null");

			Category existingCategory = categoryRepository.findById(dataDto.getId())
					.orElseThrow(() -> new IllegalArgumentException("Category not found for id: " + dataDto.getId()));
//			 added name validation to warrant details update. (modified by:Gopala krishna)
			 boolean nameExists = categoryRepository.existsByCategoryNameAndIdNot(dataDto.getCategoryName(), dataDto.getId());
		        
		        if (nameExists) {
		            returnMap.put(false, " Warrant No. '" + dataDto.getCategoryName() + "' already exists.");
		            return returnMap;
		        }
	        
			// mapper.map(dataDto, existingCategory);
			existingCategory.setCategoryName(dataDto.getCategoryName());
			String dateString = dataDto.getWtIssueDate();
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date issueDate = inputFormat.parse(dateString);
			existingCategory.setWtIssueDate(issueDate);
			existingCategory.setQty(dataDto.getQty());
			categoryRepository.save(existingCategory);

			Ship ship = existingCategory.getShip();
			if (ship != null) {
				ship.setShipName(dataDto.getShipName());
				ship.setEstDate(dataDto.getEstDate());
				ship.setRevNo(Integer.parseInt(dataDto.getRevNo()));
				ship = shipRepository.save(ship);
			}

			Operation op = opRepo.findByWarrant(existingCategory);
			if (op != null) {
				op.setOpNo(dataDto.getOpNo());
				opRepo.save(op);
			}
			returnMap.put(true, "Data updated successfully.");
			return returnMap;

		} catch (Exception e) {
			logger.error("Error occured while updating projects ", e);
			logger.error("Error in updating projects: {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public Map<Boolean, String> deleteSaleOrder(int saleOrderId) {

		Map<Boolean, String> returnMap = new HashMap<>();
		try {

			Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(saleOrderId);
			if (saleOrderOptional.isPresent()) {
				saleOrderRepository.delete(saleOrderOptional.get());
				String respString = "Sale Order deleted:";
				returnMap.put(true, respString);

			} else {
				String respString = "Not able to delete the Sale Order Which is present in the Offer Letter";
				logger.error(respString);
				returnMap.put(false, respString);
			}
		} catch (Exception e) {
			String respString = "Not able to delete the Sale Order Which is present in the Offer Letter";
			logger.error("Error in updating sale order details {}", e.getMessage());
			returnMap.put(false, respString);
			return returnMap;
		}
		return returnMap;
	}
	
	public List<Object[]> getAllByFirmId(int orgId) {
		return projectRepository.getAllByFirmId(orgId);
	}
	
	public List<ProjectSummaryDto> getProjectSummaryByPoId(int id) {
        List<ProjectSummaryDto> projectSummaryDtos = new ArrayList<>();
        List<Object[]> projectSummaryDetails = null;

        try {
            if (id != 0) {
            	projectSummaryDetails = projectRepository.getProjectSummaryByProjectId(id);
            } else {
            	throw new Exception("Summary does not exist for id: " + id);
            }

            if (projectSummaryDetails != null && !projectSummaryDetails.isEmpty()) {
                for (Object[] projSummaryDet : projectSummaryDetails) {
                    ProjectSummaryDto projectSummaryDto = new ProjectSummaryDto();
                    projectSummaryDto.setOpa((String) projSummaryDet[2]);
                    projectSummaryDto.setPrVal((Double) projSummaryDet[3]);
                    projectSummaryDto.setValCLot((Integer) projSummaryDet[4]);
                    projectSummaryDto.setAlert(true);
                    projectSummaryDto.setTestThrInd(1);
                    projectSummaryDto.setValClQty((Integer) projSummaryDet[5]);
                    projectSummaryDto.setValTtlLItems((Integer) projSummaryDet[6]);
                    projectSummaryDto.setValTtlTe((BigInteger) projSummaryDet[9]);
                    projectSummaryDto.setQaclNo((String) projSummaryDet[10]);
                    projectSummaryDto.setNoRfr((BigInteger) projSummaryDet[11]);
                    projectSummaryDtos.add(projectSummaryDto);
                }
            }
        } catch (Exception e) {
        	logger.error("Error occured in getting ProjectSummary By PoId {}", e.getMessage());
        }
		return projectSummaryDtos;
    }
	
   

}
