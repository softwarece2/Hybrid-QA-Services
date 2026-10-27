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

import in.co.bel.olms.master.dto.SupplierDto;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SupplierService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/supplier")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SupplierController {

	Gson gson = new Gson();

	private final SupplierService supplierService;

	private final Mapper mapper;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SupplierDto>> getAllUsers() {

		List<SupplierDto> supplierDtos = new ArrayList<>();
		List<Supplier> suppliers = supplierService.getAllSuppliers();
		for (Supplier supplier : suppliers) {
			SupplierDto supplierDto = mapper.map(supplier, SupplierDto.class);
			supplierDtos.add(supplierDto);
		}
		return ResponseEntity.ok(supplierDtos);

	}

	/**
	 * 
	 * @param supplierDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveSupplier(@RequestBody @Valid SupplierDto supplierDto) {
		Boolean name = supplierService.existsByname(supplierDto.getName());
		if(name) {
			 return new ResponseEntity<String>("Supplier name already exists.",HttpStatus.CONFLICT);
		}		
		else {
			supplierDto.setActive(true);
		Supplier savedSupplier = supplierService.saveSupplier(supplierDto);
		SupplierDto savedSupplierDto = mapper.map(savedSupplier, SupplierDto.class);
		return new ResponseEntity<>(gson.toJson(savedSupplierDto), HttpStatus.CREATED);
		}
	}

	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<String> updateSupplier(@RequestBody @Valid SupplierDto supplierDto) {
		SupplierDto savedSupplierDto = null;
		try {
			if(supplierDto != null ) {
//				Boolean name = supplierService.existsByname(supplierDto.getName());
				Boolean name = supplierService.existsByNameAndIdNot(supplierDto.getName(), supplierDto.getId());
				if(name) {
					 return new ResponseEntity<String>("Supplier name already exists.",HttpStatus.CONFLICT);
				}		
				else {
				Supplier savedSupplier = supplierService.updateSupplier(supplierDto);
				savedSupplierDto = mapper.map(savedSupplier, SupplierDto.class);
				return new ResponseEntity<String>(gson.toJson(savedSupplierDto),HttpStatus.CREATED);
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
	public ResponseEntity<SupplierDto> getSupplier(@PathVariable int id) throws DataNotFoundException {

		Supplier supplier = supplierService.getSupplier(id);
		SupplierDto supplierDto = mapper.map(supplier, SupplierDto.class);
		return ResponseEntity.ok(supplierDto);

	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAllActiveSuppliers")
	public ResponseEntity<List<SupplierDto>> getAllActiveSuppliers() {
		List<SupplierDto> supplierDtoList = new ArrayList<>();
		List<Supplier> supplierList = supplierService.getAllActiveSuppliers();
		for(Supplier supplier : supplierList) {
			SupplierDto supplierDto = mapper.map(supplier, SupplierDto.class);
			supplierDtoList.add(supplierDto);
		}
		return ResponseEntity.ok(supplierDtoList);
	}
}
