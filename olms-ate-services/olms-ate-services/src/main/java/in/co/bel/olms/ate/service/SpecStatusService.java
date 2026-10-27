package in.co.bel.olms.ate.service;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.ate.dto.SpecStatusDto;
import in.co.bel.olms.ate.dto.SpecStatusUpdateDto;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.entity.SpecStatus;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.ate.repository.SpecStatusRepository;
import in.co.bel.olms.ate.repository.TestRepository;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecStatusService {

	private final SpecRepository specRepository;

	private final SpecStatusRepository specStatusRepository;

	private final TestRepository testRepository;

	private final Mapper mapper;

	private final EmployeeDao empDao;

	public String updateSpecStatus(SpecStatusDto specStatusDto) {

		if (specStatusDto.getSpecId() == null || specStatusDto.getCreatedById() == null
				|| specStatusDto.getActive() == null) {
			throw new IllegalArgumentException("Invalid payload");
		}

		Boolean existBySpecId = specRepository.existsById(specStatusDto.getSpecId());

		if (!existBySpecId)
			throw new IllegalArgumentException("Spec not found for id : " + specStatusDto.getSpecId());

		SpecStatus specStatus = new SpecStatus();

		mapper.map(specStatusDto, specStatus);

		Employee empDets = empDao.getEmployee(specStatusDto.getCreatedById());
		if (empDets.getEname() != null || empDets.getEmpRole().getEmpRole() != null) {
			specStatus.setCreatedByNameAndRole(empDets.getEname() + " (" + empDets.getEmpRole().getEmpRole() + ")");
		}

		specStatusRepository.save(specStatus);

		return "Spec status updated successfully";
	}

	public List<SpecStatusDto> getAllSpecStatus(Integer specId) {

		if (specId == null)
			throw new IllegalArgumentException("Invalid specId: " + specId);

		Boolean existBySpecId = specRepository.existsById(specId);

		if (!existBySpecId)
			throw new IllegalArgumentException("Spec not found for id : " + specId);

		List<SpecStatusDto> specStatusList = new ArrayList<>();

		List<SpecStatus> specStatuses = specStatusRepository.findBySpecIdOrderByCreatedAtDesc(specId);

		specStatuses.forEach(specStatus -> specStatusList.add(mapper.map(specStatus, SpecStatusDto.class)));

		return specStatusList;

	}

	public Boolean getLatestStatus(Integer specId) {
		SpecStatus spec = specStatusRepository.findTopBySpecIdOrderByCreatedAtDesc(specId).orElse(null);
		return spec != null ? spec.getActive() : null;
	}

	public List<SpecStatusDto> getAllSpecUpdateHistory(Integer testId) {
		
		List<SpecStatus> specStatusHistory  = new ArrayList<>();
		
		if(testId != null) {
			if(!testRepository.existsById(testId)) throw new IllegalArgumentException("Test not found for Id: " + testId);
		
			specStatusHistory = specStatusRepository.findByTestId(testId);
		}

		List<SpecStatusDto> specStatusHistoryDto = new ArrayList<>();

		specStatusHistory.forEach(specStatus -> specStatusHistoryDto.add(mapper.map(specStatus, SpecStatusDto.class)));

		return specStatusHistoryDto;
	}

	@Transactional
	public String updateSpecStatusMany(SpecStatusDto specStatusDto) {

		List<SpecStatusUpdateDto> updateDtos = specStatusDto.getUpdateDtos();

		if (specStatusDto.getUpdateDtos() == null || specStatusDto.getCreatedById() == null
				|| specStatusDto.getRemark() == null) {
			throw new IllegalArgumentException("Invalid payload");
		}
		
		List<SpecStatus> specStatuses = new ArrayList<>();
		Employee empDets = empDao.getEmployee(specStatusDto.getCreatedById());
		
		updateDtos.forEach(updateDto -> {
			Spec existingSpec = specRepository.findById(updateDto.getSpecId()).orElse(null);
			
			if (existingSpec == null)
				throw new IllegalArgumentException("Spec not found for id : " + specStatusDto.getSpecId());
			
			SpecStatus newSpecStatus = new SpecStatus();

			mapper.map(specStatusDto, newSpecStatus);
			
			newSpecStatus.setActive(updateDto.getActive());
			newSpecStatus.setSpec(existingSpec);
			
			if (empDets.getEname() != null || empDets.getEmpRole().getEmpRole() != null) {
				newSpecStatus.setCreatedByNameAndRole(empDets.getEname() + " (" + empDets.getEmpRole().getEmpRole() + ")");
			}

			specStatuses.add(newSpecStatus);
			
			// Updating status in the Spec table too!
			existingSpec.setActive(updateDto.getActive());
			specRepository.save(existingSpec);
			
		});
		
		specStatusRepository.saveAll(specStatuses);

		return "Spec status updated successfully";
	}

}
