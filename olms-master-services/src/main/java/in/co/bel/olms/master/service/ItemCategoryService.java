package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.ItemCategoryDto;
import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

	private final ItemCategoryRepository itemCategoryRepository;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ItemCategory getItemCategoryById(int id) {
		Optional<ItemCategory> itemCategoryOptional = itemCategoryRepository.findById(id);
		if(itemCategoryOptional.isPresent()) {
			return itemCategoryOptional.get();
		} else {
			throw new DataNotFoundException("Itemcategory not found by Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<ItemCategory> getAllItemcategory() {
		return itemCategoryRepository.findAll();
	}
	
	public List<ItemCategory> getAllActiveItemcategory() {
		return itemCategoryRepository.findAllActiveItemCategory();
	}
	
	public Boolean existsByname(String category) {
		return itemCategoryRepository.existsBycategory(category);
	}
	
	public ItemCategory saveItemCategory(ItemCategoryDto itemCategoryDto) {

		ItemCategory itemCategory = mapper.map(itemCategoryDto, ItemCategory.class);
		return itemCategoryRepository.save(itemCategory);

	}
	
	public ItemCategory updateItemCategory (ItemCategoryDto itemCategoryDto) {
			
			Optional<ItemCategory> itemCategoryOptional = itemCategoryRepository.findById(itemCategoryDto.getId());
			if (itemCategoryOptional.isPresent()) {
				ItemCategory itemCategory =  itemCategoryOptional.get();
				itemCategory.setActive(itemCategoryDto.getActive());
				itemCategory.setCategory(itemCategoryDto.getCategory());
				
				return itemCategoryRepository.save(itemCategory);
			
		} else {
			throw new DataNotFoundException("Item category not found with Id: " + itemCategoryDto.getId());
		}
	
	}
	
	public List<ItemCategory> getAllActiveItemCategory() {
		return itemCategoryRepository.findAllActiveItemCategory();
	}
	
	public Boolean existsByNameAndIdNot(String category, int id) {
		return itemCategoryRepository.existsByCategoryAndIdNot(category,id);
	}

}
