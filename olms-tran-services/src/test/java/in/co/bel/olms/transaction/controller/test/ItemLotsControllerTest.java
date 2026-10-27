package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.transaction.controller.ItemLotsController;
import in.co.bel.olms.transaction.dto.ItemLotsDto;
import in.co.bel.olms.transaction.entity.ItemLots;
import in.co.bel.olms.transaction.repository.ItemLotsRepository;
import in.co.bel.olms.transaction.service.ItemLotsService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class ItemLotsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ItemLotsControllerTest.class);
	
	@InjectMocks
	ItemLotsController itemLotsController;
	
	@Mock
	ItemLotsService itemLotsService;
	
	@Mock
	ItemLotsRepository itemLotsRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getItemLotsByIdValidTest() {
		logger.info("ItemLotsControllerTest.getItemLotsByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 223;
			List<ItemLots> lItemLot = new ArrayList<>();
			ItemLots itemLot = new ItemLots();
			itemLot.setId(soId);
			lItemLot.add(itemLot);
			lItemLot.add(itemLot);
			when(itemLotsRepository.getSoItemLotsBySoId(anyInt())).thenReturn(lItemLot);
			when(mapper.map(any(ItemLots.class), (ItemLotsDto.class))).thenReturn(new ItemLotsDto());
			ResponseEntity<Object> result = itemLotsController.getItemLotsById(soId,anyInt());
			assertNotNull(result);
		}, "Test failed due to an exception: getItemLotsByIdValidTest ");
		logger.info("ItemLotsControllerTest.getItemLotsByIdValidTest : Finished");
	}
	
	@Test 
	void saveItemLotsValidTest() {
		logger.info("ItemLotsControllerTest.saveItemLotsValidTest : Initiated");
		assertDoesNotThrow(() -> {
			ItemLotsDto itemLotsDto = new ItemLotsDto();
			when(itemLotsService.saveItemLots(any(ItemLotsDto.class), null)).thenReturn(new ItemLotsDto());
			when(mapper.map(any(ItemLots.class), (ItemLotsDto.class))).thenReturn(itemLotsDto);
			ResponseEntity<String> result = itemLotsController.saveItemLots(itemLotsDto.toString(),null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveItemLotsValidTest ");
		logger.info("ItemLotsControllerTest.saveItemLotsValidTest : Finished");
	}
}
