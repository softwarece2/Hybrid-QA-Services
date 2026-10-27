package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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

import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.dto.SalesGroupDto;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SalesGroupService;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequestMapping("/salesGroup")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SalesGroupController {

	private final SalesGroupService salesGroupService;

	private final Mapper mapper;

	Gson gson = new Gson();
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SalesGroupDto>> getAllSalesGrps() {

		List<SalesGroupDto> salesGrpDtos = new ArrayList<>();
		List<SalesGroup> salesGroups = salesGroupService.getAllSalesGroups();
		for (SalesGroup salesGroup : salesGroups) {
			SalesGroupDto salesGroupDto = mapper.map(salesGroup, SalesGroupDto.class);
			if (salesGroup.getOrganization() != null) {
				salesGroupDto.setOrganizationName(salesGroup.getOrganization().getOrganizationName());
			}
			salesGrpDtos.add(salesGroupDto);
		}
		return ResponseEntity.ok(salesGrpDtos);

	}

	
	/**
	 * 
	 * @param salesGroupDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveSalesGroup(@RequestBody @Valid SalesGroupDto salesGroupDto) {
		SalesGroupDto savedSalegrpDto = null;
		try {
			if(salesGroupDto != null ) {
				Boolean name = salesGroupService.existsByName(salesGroupDto.getName());
				if(name) {
					return new ResponseEntity<String>("Sales Group Name already exists!",HttpStatus.CONFLICT);
				}
				else {
				SalesGroup salesGroup = mapper.map(salesGroupDto,SalesGroup.class);
				salesGroup.setcreatedDate(new Date());
				salesGroup.setActive(true);
				SalesGroup savedsalesgroup = salesGroupService.saveSalesGroup(salesGroup);
				savedSalegrpDto = mapper.map(savedsalesgroup, SalesGroupDto.class);
				return new ResponseEntity<String>(gson.toJson(savedSalegrpDto),HttpStatus.CREATED);
				}
			}
			}
			catch(Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SalesGroupDto> getSalesGroup(@PathVariable int id) throws DataNotFoundException {

		SalesGroup salesGroup = salesGroupService.getSalesGroup(id);
		SalesGroupDto salesGroupDto = mapper.map(salesGroup, SalesGroupDto.class);
		return ResponseEntity.ok(salesGroupDto);

	}
	
	@GetMapping("/getAllForChart")
	public ResponseEntity<List<KeyValuePair>> getAllSalesGrpsForChart() {

		List<KeyValuePair> salesGrpList = new LinkedList<>();
		List<SalesGroup> salesGroups = salesGroupService.getAllSalesGroups();
		KeyValuePair defaultSalesGrp = new KeyValuePair(-1, "All");
		salesGrpList.add(defaultSalesGrp);
		for (SalesGroup salesGroup : salesGroups) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId(salesGroup.getId());
			keyValuePair.setName(salesGroup.getName());
			salesGrpList.add(keyValuePair);
		}
		return ResponseEntity.ok(salesGrpList);

	}
	
	/**
	 * 
	 * @param salesGroupDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<?> updateSalesGroup(@Valid @RequestBody SalesGroupDto salesGroupDto) {
	    try {
	        
	        SalesGroup savedSalesGroup = salesGroupService.updateSalesGroup(salesGroupDto);
	        SalesGroupDto responseDto = mapper.map(savedSalesGroup, SalesGroupDto.class);
	        
	       
	        return new ResponseEntity<>(responseDto, HttpStatus.OK);
	    } catch (DataNotFoundException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (RuntimeException e) {
	     
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
