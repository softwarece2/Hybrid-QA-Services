package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.WorkloadInspActDet;
import in.co.bel.olms.transaction.repository.WorkloadInspActDetRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class WorkloadInspActDetService {

		private final WorkloadInspActDetRepository workloadInspActDetRepository;

		public final WorkloadInspActDet getWorkloadInspActDetById(int id) {

		Optional<WorkloadInspActDet> workloadInspActDetOptional = workloadInspActDetRepository.findById(id);
			if ( workloadInspActDetOptional .isPresent()) {
				return  workloadInspActDetOptional.get();
			} else {
				throw new DataNotFoundException("WorkLoad not found with Id: " + id);
			}
		}
		
		public List<WorkloadInspActDet> getAllWorkloadInspActDet() {
			return workloadInspActDetRepository.findAll();
		}
}
