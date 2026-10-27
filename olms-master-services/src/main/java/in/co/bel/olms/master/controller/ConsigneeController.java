package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import in.co.bel.olms.master.dto.ConsigneeDto;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.ConsigneeService;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequestMapping("/consignee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ConsigneeController {

	private final ConsigneeService consigneeService;

	private final Mapper mapper;
	
	Gson gson = new Gson();

	/**
	 * 
	 * @return
	 */	
	@GetMapping("/getAll")
	public ResponseEntity<List<ConsigneeDto>> getAllConsignees() {

		List<ConsigneeDto> consigneeDtoList = new ArrayList<>();
		List<Consignee> consignees = consigneeService.getAllConsignees();
		for (Consignee consignee : consignees) {
			ConsigneeDto consigneeDto = mapper.map(consignee, ConsigneeDto.class);
			consigneeDtoList.add(consigneeDto);
		}
		return ResponseEntity.ok(consigneeDtoList);

	}
	
	@GetMapping("/getAllActiveConsignees")
	public ResponseEntity<List<ConsigneeDto>> getAllActiveConsignees() {

		List<ConsigneeDto> consigneeDtoList = new ArrayList<>();
		List<Consignee> consignees = consigneeService.getAllActiveConsignees();
		for (Consignee consignee : consignees) {
			ConsigneeDto consigneeDto = mapper.map(consignee, ConsigneeDto.class);
			consigneeDtoList.add(consigneeDto);
		}
		return ResponseEntity.ok(consigneeDtoList);

	}


	
	/**
	 * 
	 * @param consigneeDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveConsignee(@RequestBody @Valid ConsigneeDto consigneeDto) {
		Boolean name = consigneeService.existsByname(consigneeDto.getName());
		Boolean phone = consigneeService.existsByphone(consigneeDto.getPhone());
		
		if(name) {
			return new ResponseEntity<String>("Name is already exists",HttpStatus.CONFLICT);
		}
		else if(phone) {
			return new ResponseEntity<>("Phone number is already exists",HttpStatus.CONFLICT);
		}
		else {
			Consignee savedConsignee = consigneeService.saveConsignee(consigneeDto);
			ConsigneeDto savedConsigneeDto = mapper.map(savedConsignee, ConsigneeDto.class);
			return new ResponseEntity<>(gson.toJson(savedConsigneeDto),HttpStatus.CREATED);
		}

	}

	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ConsigneeDto> getConsignee(@PathVariable int id) throws DataNotFoundException {

		Consignee consignee = consigneeService.getConsignee(id);
		ConsigneeDto consigneeDto = mapper.map(consignee, ConsigneeDto.class);
		return ResponseEntity.ok(consigneeDto);

	}
	
	
	/**
	 * 
	 * @param consigneeDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<String> updateConsignee(@RequestBody @Valid ConsigneeDto consigneeDto) {
		
		try {
	        Consignee updatedConsignee = consigneeService.updateConsignee(consigneeDto);
	        ConsigneeDto dto = mapper.map(updatedConsignee, ConsigneeDto.class);
	        return new ResponseEntity<>(gson.toJson(dto), HttpStatus.OK);

	    } catch (DataNotFoundException e) { 
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

	    } catch (RuntimeException e) { 
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

	    } catch (Exception e) { 
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}
	


