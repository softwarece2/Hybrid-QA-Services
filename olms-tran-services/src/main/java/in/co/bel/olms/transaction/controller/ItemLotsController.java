package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.ItemLotsDto;
import in.co.bel.olms.transaction.service.ItemLotsService;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soItemLots")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ItemLotsController {

	private final ItemLotsService itemLotsService;

	private static final Logger logger = LoggerFactory.getLogger(ItemLotsController.class);
	
	@GetMapping("/type/{type}/id/{id}")
	public ResponseEntity<Object> getItemLotsById(@PathVariable Integer type, @PathVariable Integer id) throws DataNotFoundException {
		List<ItemLotsDto> iLs = null;	
		try {
			iLs = itemLotsService.getSoItemLotsByIdAndType(id, type);
            return ResponseEntity.ok(iLs);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
	}

	@PostMapping("/saveItemLots")
	public ResponseEntity<String> saveItemLots(@RequestParam(value = "itemLotsDtoStr" ,required = true) String itemLotsDtoStr,
			@RequestParam(value = "itemlots",required = false) MultipartFile[] itemLotFiles) {
		Gson gson = new Gson();
		ItemLotsDto itemLotsDto = gson.fromJson(itemLotsDtoStr, new TypeToken<ItemLotsDto>() {
		}.getType());
		try {		
			ItemLotsDto savedItemLots = itemLotsService.saveItemLots(itemLotsDto,itemLotFiles);

			return new ResponseEntity<>(gson.toJson(savedItemLots), HttpStatus.CREATED);
		} catch (DataNotValidException de) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Error-Message", de.getMessage());
			return ResponseEntity.badRequest().headers(headers).body(de.getMessage());
		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Error-Message", e.getMessage());
			return ResponseEntity.badRequest().headers(headers).body(e.getMessage());
		}
	}
}
