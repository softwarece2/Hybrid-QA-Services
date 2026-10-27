package in.co.bel.olms.transaction.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.SerialNoStatusRepository;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.LineItemSerialNoHistory;
import in.co.bel.olms.transaction.repository.LineItemSerialNoHistoryRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineItemSerialNoService {

	private final LineItemSerialNoRepository lineItemSerialNoRepo;

	private final SerialNoStatusRepository serialNoStatusRepo;

	private final EmployeeRepository employeeRepository;

	private final LineItemSerialNoHistoryRepository lisnHistoryRepo;

	private final Mapper mapper;
	
	private static final String SERIAL_NO = "Serial no: ";
	
	@Transactional
	public void update(LineItemSerialNoDto requestDto) {
    if (requestDto.getIds() == null || requestDto.getIds().isEmpty())
      throw new IllegalArgumentException("ERROR: IDs are required to update a record.");

    if (requestDto.getStatusId() == null)
      throw new IllegalArgumentException("ERROR: Given status is either null or not-found.");

    SerialNoStatus status = serialNoStatusRepo.findById(requestDto.getStatusId())
        .orElseThrow(() -> new IllegalArgumentException("Status not found"));

    List<LineItemSerialNo> registerDetailsList = lineItemSerialNoRepo.findAllById(requestDto.getIds());

    registerDetailsList.forEach(serialNo -> serialNo.setStatus(status));

    lineItemSerialNoRepo.saveAllAndFlush(registerDetailsList);

  }

  public LineItemSerialNoHistoryDto buildHistoryDto(List<Integer> serialNoIds, Integer statusId, Integer createdById, Integer fkId, String fkName) {

    LineItemSerialNoHistoryDto historyDto = new LineItemSerialNoHistoryDto();
    if (serialNoIds != null && !serialNoIds.isEmpty()) historyDto.setLineItemSerialNoIds(serialNoIds);
    if (statusId != null) historyDto.setStatusId(statusId);
    if (createdById != null) historyDto.setCreatedById(createdById);
    if (fkId != null) historyDto.setFkId(fkId);
    if (fkName != null && !fkName.trim().isEmpty()) historyDto.setFkName(fkName);

    return historyDto;
  }

	@Transactional
	public void addHistory(LineItemSerialNoHistoryDto reqDto) {
	    List<LineItemSerialNoHistory> entityList = new ArrayList<>();

	    if((reqDto.getLineItemSerialNoIds() == null || reqDto.getLineItemSerialNoIds().isEmpty()) && reqDto.getLineItemSerialNoId() != null) {
	    	reqDto.setLineItemSerialNoIds(List.of(reqDto.getLineItemSerialNoId()));
	    }
	    
	    List<LineItemSerialNo> lisnList = lineItemSerialNoRepo.findAllById(reqDto.getLineItemSerialNoIds());
	    if (!lisnList.isEmpty()) { 

	        Map<Integer, SerialNoStatus> statusMap = new HashMap<>();
	        if (reqDto.getStatusId() != null) {
	            SerialNoStatus status = serialNoStatusRepo.findById(reqDto.getStatusId())
	            		.orElseThrow(() -> new IllegalArgumentException("Serial no. status not found for id: " + reqDto.getStatusId()));
	            statusMap.put(reqDto.getStatusId(), status);
	        }

	        Employee emp = employeeRepository.findById(reqDto.getCreatedById()).orElse(null);

	        lisnList.forEach(lisn -> {
	            LineItemSerialNoHistory entity = new LineItemSerialNoHistory();
	            entity.setLineItemSerialNoId(lisn.getId());

	            SerialNoStatus status = statusMap.getOrDefault(reqDto.getStatusId(), lisn.getStatus());
	            entity.setStatusId(status.getId());
	            entity.setStatus(status.getStatus());

	            if (emp != null) {
	                entity.setCreatedById(emp.getId());
	                entity.setCreatedBy(emp.getEname());
	            }

	            entity.setFkId(reqDto.getFkId());
	            entity.setFkName(reqDto.getFkName());
	            String remark = getRemarks(reqDto.getStatusId() ,lisn, emp, reqDto.getFkName());
	            entity.setRemark(remark);
	            entityList.add(entity);
	        });
	    }

	    lisnHistoryRepo.saveAll(entityList);
	}
	
	@Transactional
	public List<LineItemSerialNoHistoryDto> getHistory(Integer lisnId) {
		List<LineItemSerialNoHistory> histories = lisnHistoryRepo.findByLineItemSerialNoId(lisnId);
		return histories.stream().map(history -> mapper.map(history, LineItemSerialNoHistoryDto.class)).toList();
	}
	
	String getRemarks(Integer statusId, LineItemSerialNo lisn, Employee emp, String fkName) {
		
		String remark = "";
		
		Integer snStatusId = statusId != null ? statusId : lisn.getStatus().getId();
		
		switch (snStatusId) {
		case 1:  //created
			remark = SERIAL_NO + lisn.getSerialNo() + " was " + lisn.getStatus().getStatus() + " by " + emp.getEname();
			break;
		case 2:  //mapped
			remark = SERIAL_NO + lisn.getSerialNo() + " was " + lisn.getStatus().getStatus() + " to warrant: " + fkName + " by " + emp.getEname();
			break;
		case 3:  //sub_slip_created
			remark = "Submission slip created for serial no: " + lisn.getSerialNo() + " by " + emp.getEname() + " with Sub slip no: " + fkName;
			break;
		case 4:  //lot_generated
			remark = "Lot generated for serial no: " + lisn.getSerialNo() + " by " + emp.getEname() + " with Lot no: " + fkName;
			break;
		case 5:  //rfr
			remark = SERIAL_NO + lisn.getSerialNo() + " marked as : RFR by " + emp.getEname();
			break;
		case 6:  //accepted
			remark =  SERIAL_NO + lisn.getSerialNo() + " accepted by " + emp.getEname();
			break;
		case 7:  //scraped
			remark = SERIAL_NO + lisn.getSerialNo() + " marked as : Scrap by " + emp.getEname();
			break;
		case 8:  //i_note_generated
			remark = "I-note generated for serial no: " + lisn.getSerialNo() + " by " + emp.getEname() + " with I-Note no: " + fkName;
			break;
		case 9:  //un-mapped
			remark = SERIAL_NO + lisn.getSerialNo() + " was unmapped from " + fkName + " by " + emp.getEname();
			break;
		default:
			break;
		}
		return remark;
	}
}
