package in.co.bel.olms.master.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.ConsigneeDto;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ConsigneeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsigneeService {

	private final ConsigneeRepository consigneeRepository;

	private final Mapper mapper;

	
	/**
	 * 
	 * @param consigneeDto
	 * @return
	 */
	public Consignee saveConsignee(ConsigneeDto consigneeDto) {

		Consignee consignee = mapper.map(consigneeDto, Consignee.class);
		consignee.setCreatedDate(new Date());
		return consigneeRepository.save(consignee);

	}

	public Consignee updateConsignee(ConsigneeDto consigneeDto) {
		Optional<Consignee> consigneeOptional = consigneeRepository.findById(consigneeDto.getId());
		if (consigneeOptional.isPresent()) {
			Consignee consignee=  consigneeOptional.get();
			
			Boolean nameExists = consigneeRepository.existsByNameAndIdNot(consigneeDto.getName(), consigneeDto.getId());
		    Boolean phoneExists = consigneeRepository.existsByPhoneAndIdNot(consigneeDto.getPhone(), consigneeDto.getId());

	        if (nameExists) {
	            throw new RuntimeException("Consignee Name already exists"); 
	        }
	        if (phoneExists) {
	            throw new RuntimeException("Phone number already exists");
	        }
			
			consignee.setActive(consigneeDto.getActive());
			consignee.setAddress(consigneeDto.getAddress());
			consignee.setFax(consigneeDto.getFax());
			consignee.setPhone(consigneeDto.getPhone());
			consignee.setModifiedDate(new Date());
			consignee.setName(consigneeDto.getName());
			
			return consigneeRepository.save(consignee);
			
		} else {
			throw new DataNotFoundException("Consignee not found with Id: " + consigneeDto.getId());
		}
	
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Consignee getConsignee(int id) {

		Optional<Consignee> consigneeOptional = consigneeRepository.findById(id);
		if (consigneeOptional.isPresent()) {
			return consigneeOptional.get();
		} else {
			throw new DataNotFoundException("Consignee not found with Id: " + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Consignee> getAllConsignees() {

		List <Consignee>result= consigneeRepository.findAll();
		  result.sort(Comparator.comparing(Consignee::getName));

		return result;

	} 
	
	public List<Consignee> getAllActiveConsignees() {

		return consigneeRepository.findAllActiveConsignees();

	}
	
	public Boolean existsByname(String name) {
		return consigneeRepository.existsByname(name);
	}

	public Boolean existsByphone(String phone) {
		return consigneeRepository.existsByphone(phone);
	}
}
