package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmpRoleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpRoleService {

	private final EmpRoleRepository empRoleRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public EmpRole getEmpRoleById(int id) {
		
		Optional<EmpRole> empRoleOptional = empRoleRepository.findById(id);
		if(empRoleOptional.isPresent()) {
			return empRoleOptional.get();
		} else {
			throw new DataNotFoundException("empRole not found by Id: " + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<EmpRole> getAllEmpRole() {
		return empRoleRepository.findAll();
	}
	
	
	/**
	 * 
	 * @param orgTypeId
	 * @return
	 */
	public List<EmpRole> getEmpRoleByOrgTypeId(int orgTypeId) {
		return empRoleRepository.findByOrgTypeId(orgTypeId);
	}
}
