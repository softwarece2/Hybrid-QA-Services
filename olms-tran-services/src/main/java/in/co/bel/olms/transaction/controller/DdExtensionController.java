package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
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
import in.co.bel.olms.transaction.dto.DdExtensionDto;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.service.DdExtensionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ddExtension")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DdExtensionController {
	
	private final DdExtensionService ddExtensionService;

	private final Mapper mapper;

	@GetMapping("/getAll")
	public ResponseEntity<List<DdExtensionDto>> getDdExtensionOfSo() {
		List<DdExtensionDto> ddExtensionDtos = new ArrayList<>();
		List<DdExtension> ddExtensions = ddExtensionService.getDdExtensionOfSo();

		for (DdExtension ddExtension : ddExtensions) {
			DdExtensionDto ddExtensionDto = mapper.map(ddExtension, DdExtensionDto.class);
			ddExtensionDtos.add(ddExtensionDto);
		}
		return ResponseEntity.ok(ddExtensionDtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DdExtensionDto> getById(@PathVariable int id) throws DataNotFoundException {
		DdExtension ddExtension = ddExtensionService.getById(id);
		DdExtensionDto ddExtensiondto = mapper.map(ddExtension, DdExtensionDto.class);
		return ResponseEntity.ok(ddExtensiondto);
	}
	
	@GetMapping("/type/{type}/id/{id}")
	public ResponseEntity<Object> getItemLotsById(@PathVariable Integer type, @PathVariable Integer id) throws DataNotFoundException {
		List<DdExtensionDto> ddExtensionDtos = null;			
		try {
			ddExtensionDtos = ddExtensionService.getByIdAndType(id, type);
            return ResponseEntity.ok(ddExtensionDtos);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
	}

	@PostMapping("/save")
	public ResponseEntity<DdExtensionDto> saveDdExtensionData(@RequestParam(value = "ddExtensionDtoStr" ,required = true) String ddExtensionDtoStr,
			@RequestParam(value = "ddFile",required = false) MultipartFile[]ddFiles) {
		Gson gson = new Gson();
		DdExtensionDto ddExtensionDto = gson.fromJson(ddExtensionDtoStr, new TypeToken<DdExtensionDto>() {
		}.getType());
		try {
			DdExtensionDto savedDdExtension = ddExtensionService.saveDdExtensionData(ddExtensionDto,ddFiles);
			return new ResponseEntity<>(savedDdExtension, HttpStatus.CREATED);
			
		} catch (DataNotValidException de) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Error-Message", de.getMessage());
			return ResponseEntity.badRequest().headers(headers).body(ddExtensionDto);
		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Error-Message", e.getMessage());
			return ResponseEntity.internalServerError().headers(headers).body(ddExtensionDto);
		}
	}

}
