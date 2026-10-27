package in.co.bel.olms.master.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.SupplierDto;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {

	private final SupplierRepository supplierRepository;

	private final Mapper mapper;

	/**
	 * 
	 * @param supplierDto
	 * @return
	 */
	public Supplier saveSupplier(SupplierDto supplierDto) {

		Supplier supplier = mapper.map(supplierDto, Supplier.class);
		supplier.setCreatedDate(new Date());
		return supplierRepository.save(supplier);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Supplier getSupplier(int id) {

		Optional<Supplier> supplierOptional = supplierRepository.findById(id);
		if (supplierOptional.isPresent()) {
			return supplierOptional.get();
		} else {
			throw new DataNotFoundException("Supplier not found with Id: " + id);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<Supplier> getAllSuppliers() {
		List<Supplier> result=supplierRepository.findAll();
		result.sort(Comparator.comparing(Supplier::getName));
		return result;
	}
	
	public Supplier updateSupplier (SupplierDto supplierDto ) {
		
		Optional<Supplier> indenterOptional = supplierRepository.findById(supplierDto.getId());
		if (indenterOptional.isPresent()) {
			Supplier supplier =  indenterOptional.get();
			supplier.setActive(supplierDto.getActive());
			supplier.setModifiedDate(new Date());
			supplier.setName(supplierDto.getName());
			
			return supplierRepository.save(supplier);
			
		} else {
			throw new DataNotFoundException("Supplier not found with Id: " + supplierDto.getId());
		}
	
	}

	/**
	 * 
	 * @return
	 */
	public Boolean existsByname(String name) {
		return supplierRepository.existsByname(name);
	}

	/**
	 * 
	 * @return
	 */
	public List<Supplier> getAllActiveSuppliers() {
		return supplierRepository.findAllActiveSuppliers();
	}


	public Boolean existsByNameAndIdNot(String name, int id) {
		return supplierRepository.existsByNameAndIdNot(name, id);
	}

}
