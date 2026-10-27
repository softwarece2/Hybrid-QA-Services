package in.co.bel.olms.master.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmployeeRepository;

@Component
public class EmployeeDao {

	@Autowired
	EmployeeRepository employeeRepository;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Employee getEmployee(int id) {

		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (employeeOptional.isPresent()) {
			return employeeOptional.get();
		} else {
			throw new DataNotFoundException("Employee not found with Id: " + id);
		}
	}
	
}
