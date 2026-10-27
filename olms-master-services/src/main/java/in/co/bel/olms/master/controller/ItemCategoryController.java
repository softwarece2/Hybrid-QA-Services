package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.ItemCategoryDto;
import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequestMapping("/itemCategory")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ItemCategoryController {
	
	private final ItemCategoryService itemCategoryService;

	private final Mapper mapper;
	
	Gson gson = new Gson();
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ItemCategoryDto> getItemCategoryById(@PathVariable int id) throws DataNotFoundException {
		ItemCategory itemcategory = itemCategoryService.getItemCategoryById(id);
		ItemCategoryDto itemCategoryDto = mapper.map(itemcategory, ItemCategoryDto.class);
		return ResponseEntity.ok(itemCategoryDto);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("getAll")
	public ResponseEntity<List<ItemCategoryDto>> getAllItemCategorys() {
		List<ItemCategoryDto> itemCategoryDtoList = new ArrayList<>();
		List<ItemCategory> itemCategorys = itemCategoryService.getAllItemcategory();
		for(ItemCategory itemCategory : itemCategorys) {
			ItemCategoryDto itemCategoryDto = mapper.map(itemCategory, ItemCategoryDto.class);
			itemCategoryDtoList.add(itemCategoryDto);
		}
		return ResponseEntity.ok(itemCategoryDtoList);
	}
	
	/**
	 * 
	 * @param itemCategoryDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveItemCategory(@RequestBody @Valid ItemCategoryDto itemCategoryDto) {
		Boolean name = itemCategoryService.existsByname(itemCategoryDto.getCategory());
		if (name) {
			return new ResponseEntity<String>("Item category name already exists.", HttpStatus.CONFLICT);
		}
		ItemCategory savedItemCategory = itemCategoryService.saveItemCategory(itemCategoryDto);
		ItemCategoryDto savedItemCategoryDto = mapper.map(savedItemCategory, ItemCategoryDto.class);
		return new ResponseEntity<>(gson.toJson(savedItemCategoryDto), HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param itemCategoryDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<String> updateItemCategory(@RequestBody @Valid ItemCategoryDto itemCategoryDto) {
		ItemCategoryDto savedItemCategoryDto = null;
		try {
			if (itemCategoryDto != null) {
				Boolean name = itemCategoryService.existsByNameAndIdNot(itemCategoryDto.getCategory(), itemCategoryDto.getId());
				if (name) {
					return new ResponseEntity<String>("Item category name already exists.", HttpStatus.CONFLICT);
				}
				else
				{
				ItemCategory savedItemcategory = itemCategoryService.updateItemCategory(itemCategoryDto);
				savedItemCategoryDto = mapper.map(savedItemcategory, ItemCategoryDto.class);
				return new ResponseEntity<String>(gson.toJson(savedItemCategoryDto), HttpStatus.CREATED);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/getAllActive")
	public ResponseEntity<List<ItemCategoryDto>> getAllActive() {
		List<ItemCategoryDto> itemCategoryDtoList = new ArrayList<>();
		List<ItemCategory> itemCategorys = itemCategoryService.getAllActiveItemcategory();
		for(ItemCategory itemCategory : itemCategorys) {
			ItemCategoryDto itemCategoryDto = mapper.map(itemCategory, ItemCategoryDto.class);
			itemCategoryDtoList.add(itemCategoryDto);
		}
		return ResponseEntity.ok(itemCategoryDtoList);
	}

}
