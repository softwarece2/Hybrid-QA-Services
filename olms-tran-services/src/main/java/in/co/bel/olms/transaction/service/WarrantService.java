package in.co.bel.olms.transaction.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.ProjectDataDto;
import in.co.bel.olms.transaction.dto.ProjectExcelDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.Ship;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.ShipRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarrantService {
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private final CategoryRepository categoryRepository;

	private final ShipRepository shipRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final SoLineItemRepository soLiItemRepo;

	private static final Logger logger = LoggerFactory.getLogger(WarrantService.class);

	// Still not in use
	public Map<Boolean, String> saveWarrant(ProjectExcelDto projectExcelDto) {

		Map<Boolean, String> returnMap = new HashMap<>();

		String shipNo = null;

		
		// Get project by Id

		try {
			
				
							
			Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(projectExcelDto.getSupplyOrderId());
			if (supplyOrderOptional.isPresent()) {
				SupplyOrder supplyOrder = supplyOrderOptional.get();
				
				SOLineItem soLiItem = soLiItemRepo.findById(projectExcelDto.getSoLiId()).orElseThrow(() -> new IllegalArgumentException("So line item not found."));
				List<Project> existingProjects = supplyOrder.getProjects();
				Set<Project> projects = new HashSet<>(existingProjects);

				for (ProjectDataDto projectDataDto :projectExcelDto.getDataDtos()) {
					
					shipNo = projectDataDto.getShipNo();
					
					if (StringUtils.isNotBlank(shipNo)) {
						if (!InputNameValidation.checkString(shipNo)) {
							throw new DataNotValidException("Invalid Estimate no");
						}
					}
					
					Ship ship = new Ship();
					ship.setShipName(projectDataDto.getShipNo());
					ship.setCreatedDate(new Date());
					ship.setEstDate(projectDataDto.getEstDate());
					ship.setRevNo(projectDataDto.getRevNo());
					Project project = projects.stream()
							.filter(p -> p.getProjectName().equals(projectDataDto.getProjectName().trim()))
							.toList().get(0);
					ship.setProject(project);
					ship.setSoLineItem(soLiItem);
					shipRepository.save(ship);

					Category category = new Category();
					category.setCategoryName(projectDataDto.getCategoryName());
					category.setWtIssueDate(projectDataDto.getWtIssueDate());
					category.setCreatedDate(new Date());
					category.setShip(ship);		
					categoryRepository.save(category);		
					
					
				//	category to saleorder
				}				
				supplyOrderRepository.save(supplyOrder);
				returnMap.put(true, "Data saved successfully.");
			} else {
				returnMap.put(false, "No supply order found with given id:" + projectExcelDto.getSupplyOrderId());
			}
			return returnMap;
		} catch (DataNotValidException de) {
			logger.error("Validation failed: {}", de.getMessage());
			throw de;
		} catch (Exception e) {
			logger.error("Error in saving projects {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	
	public String getSoNumberByWarrantId(int categoryId) {
		String soNumber = null;
		try {
			soNumber = categoryRepository.getSoNumberByCategoryId(categoryId);
			soNumber = (soNumber == null) ? null : soNumber;
		} catch (Exception e) {
			logger.error("Error in fetching so number by warrant id {}, due to {}", categoryId, e.getMessage());
		}
		return soNumber;
	}
	// Add servicefor mapping the warrant no and reg no in SELECT * FROM tdms.warrant_details table
	
	// 2. Get service for the mapped regno from the warrant id list
	
}
