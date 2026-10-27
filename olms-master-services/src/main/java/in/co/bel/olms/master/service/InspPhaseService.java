package in.co.bel.olms.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.InspPhase;
import in.co.bel.olms.master.entity.InspPhaseActivities;
import in.co.bel.olms.master.repository.InspPhaseActivitiesRepository;
import in.co.bel.olms.master.repository.InspPhaseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspPhaseService {

	private final InspPhaseRepository inspPhaseRepository;

	private final InspPhaseActivitiesRepository inspPhaseActivitiesRepository;
	
	public List<InspPhase> getAllInspPhase(){
		return inspPhaseRepository.findAll();
	}
	
	public List<InspPhaseActivities> getAllInspPhaseaActivities(){
		return inspPhaseActivitiesRepository.findAll();
	}
	
	public List<InspPhaseActivities> getPreInspPhaseActivities(){
		return inspPhaseActivitiesRepository.getAllInspPhaseActivitiesByInspPhaseId(ApplicationConstants.PRE_INSP_PHASE);
	}

	public List<InspPhaseActivities> getInInspPhaseActivities(){
		return inspPhaseActivitiesRepository.getAllInspPhaseActivitiesByInspPhaseId(ApplicationConstants.IN_PROCESS_INSP_PHASE);
	}

	public List<InspPhaseActivities> getPostInspPhaseActivities(){
		return inspPhaseActivitiesRepository.getAllInspPhaseActivitiesByInspPhaseId(ApplicationConstants.POST_INSP_PHASE);
	}
}
