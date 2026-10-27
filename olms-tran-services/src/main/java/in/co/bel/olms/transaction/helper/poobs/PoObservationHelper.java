package in.co.bel.olms.transaction.helper.poobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.PoObservationDetDto;
import in.co.bel.olms.transaction.entity.PoObservation;
import in.co.bel.olms.transaction.entity.PoObservationDet;
import in.co.bel.olms.transaction.entity.check.ScrutinySoDet;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTenderDet;
import in.co.bel.olms.transaction.repository.PoObservationDetRepository;
import in.co.bel.olms.transaction.repository.check.ScrutinySoDetRepository;
import in.co.bel.olms.transaction.repository.check.army.ScrutinyTenderDetRepository;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 *
 */

@Component
@RequiredArgsConstructor
public class PoObservationHelper {

	private final EntityValidator entityValidator;

	private final Mapper mapper;

	private final PoObservationDetRepository poObservationDetRepo;

	private final ScrutinySoDetRepository scrutinySoDetRepository;

	private final ScrutinyTenderDetRepository scrutinyTenderDetRepository;

	public List<PoObservationDet> prepareCreateDetBos(List<PoObservationDetDto> poObservationDetDtos,
			PoObservation poObservation) {
		List<PoObservationDet> poObservationDets = new ArrayList<>();
		for (PoObservationDetDto detDto : poObservationDetDtos) {
			PoObservationDet detBo = mapper.map(detDto, PoObservationDet.class);
			detBo.setObservation(detDto.getObservation());

			Integer soScrutinyDetId = detDto.getSoScrutinyDetId();
			Integer scrutinyTenderDetId = detDto.getScrutinyTenderDetId();
			if (soScrutinyDetId != null && soScrutinyDetId != 0) {
				Optional<ScrutinySoDet> scrutinySoDetOptional = scrutinySoDetRepository.findById(soScrutinyDetId);
				if (scrutinySoDetOptional.isPresent()) {
					ScrutinySoDet scrutinySoDet = scrutinySoDetOptional.get();
					detBo.setScrutinySoDet(scrutinySoDet);
					detBo.setScrutinyTenderDet(null);
				} else {
					throw new DataNotValidException("ScrutinySoDet not found for id: " + soScrutinyDetId);
				}
			}

			if (scrutinyTenderDetId != null && scrutinyTenderDetId != 0) {
				Optional<ScrutinyTenderDet> scrutinyTenderDetOptional = scrutinyTenderDetRepository
						.findById(scrutinyTenderDetId);
				if (scrutinyTenderDetOptional.isPresent()) {
					ScrutinyTenderDet scrutinyTenderDet = scrutinyTenderDetOptional.get();
					detBo.setScrutinyTenderDet(scrutinyTenderDet);
					detBo.setScrutinySoDet(null);
				} else {
					throw new DataNotValidException("ScrutinyTenderDet not found for id: " + scrutinyTenderDetId);
				}
			}

			detBo.setPoObservation(poObservation);
			boolean isvalid = entityValidator.isValidEntity(detBo);
			if (isvalid) {
				poObservationDets.add(detBo);
			} else {
				throw new DataNotValidException("PoObservationDet create: det " + detBo.toString());
			}
		}
		return poObservationDets;
	}

	public List<PoObservationDet> prepareRemedialDetBos(List<PoObservationDetDto> poObservationDetDtos,
			PoObservation poObservation) {
		List<PoObservationDet> poObservationDets = new ArrayList<>();
		for (PoObservationDetDto detDto : poObservationDetDtos) {
			Optional<PoObservationDet> detBoopt = poObservationDetRepo.findById(detDto.getId());
			if (detBoopt.isPresent()) {
				PoObservationDet detBo = detBoopt.get();
				detBo.setRemedialAction(detDto.getRemedialAction());
				boolean isvalid = entityValidator.isValidEntity(detBo);
				if (isvalid) {
					poObservationDets.add(detBo);
				} else {
					throw new DataNotValidException("PoObservationDet save: det " + detBo.toString());
				}
			} else {
				throw new DataNotFoundException("PoObservationdet not found by Id: " + detDto.getId());
			}
		}
		return poObservationDets;
	}

	public List<PoObservationDet> prepareClosureDetBos(List<PoObservationDetDto> poObservationDetDtos,
			PoObservation poObservation) {
		List<PoObservationDet> poObservationDets = new ArrayList<>();
		for (PoObservationDetDto detDto : poObservationDetDtos) {
			Optional<PoObservationDet> detBoopt = poObservationDetRepo.findById(detDto.getId());
			if (detBoopt.isPresent()) {
				PoObservationDet detBo = detBoopt.get();
				detBo.setIsClosed(detDto.getIsClosed());
				
				detBo.setRemarks(detDto.getRemarks());
				boolean isvalid = entityValidator.isValidEntity(detBo);
				if (isvalid) {
					poObservationDets.add(detBo);
				} else {
					throw new DataNotValidException("PoObservationDet save: det " + detBo.toString());
				}
			} else {
				throw new DataNotFoundException("PoObservationdet not found by Id: " + detDto.getId());
			}
		}
		return poObservationDets;
	}

	
	public List<PoObservationDet> prepareClosureSubmitDetBos(List<PoObservationDetDto> poObservationDetDtos,
			PoObservation poObservation) {
		List<PoObservationDet> poObservationDets = new ArrayList<>();
		for (PoObservationDetDto detDto : poObservationDetDtos) {
			Optional<PoObservationDet> detBoopt = poObservationDetRepo.findById(detDto.getId());
			if (detBoopt.isPresent()) {
				PoObservationDet detBo = detBoopt.get();
				detBo.setIsClosed(detDto.getIsClosed());
				if(Boolean.FALSE.equals(detDto.getIsClosed()))
				{
					if( null== detDto.getRemarks())
					{
						throw new DataNotValidException("PoObservationDet save: det " + detBo.toString());
					}
				}
				
				detBo.setRemarks(detDto.getRemarks());
				boolean isvalid = entityValidator.isValidEntity(detBo);
				if (isvalid) {
					poObservationDets.add(detBo);
				} else {
					throw new DataNotValidException("PoObservationDet save: det " + detBo.toString());
				}
			} else {
				throw new DataNotFoundException("PoObservationdet not found by Id: " + detDto.getId());
			}
		}
		return poObservationDets;
	}
}
