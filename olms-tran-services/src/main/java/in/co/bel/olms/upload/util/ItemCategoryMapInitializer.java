package in.co.bel.olms.upload.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ItemCategoryMapInitializer {

	private final ItemCategoryRepository itemCategoryRepository;

	@Bean
	public Map<String, Integer> itemCategoryMap() {

		Map<String, Integer> itemCategoryMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
		for (ItemCategory itemCategory : itemCategories) {
			itemCategoryMap.put(itemCategory.getCategory(), itemCategory.getId());
		}
		return itemCategoryMap;
	}

}
