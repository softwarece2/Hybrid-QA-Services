package in.co.bel.olms.transaction.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.WorkLoadDto;
import in.co.bel.olms.transaction.dto.WorkloadInspActDetDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.WorkLoad;
import in.co.bel.olms.transaction.entity.WorkloadInspActDet;
import in.co.bel.olms.transaction.repository.WorkLoadOlRepository;
import in.co.bel.olms.transaction.repository.WorkLoadRepository;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 24-04-2023
 */

@Service
@RequiredArgsConstructor
public class WorkLoadService {

	private final Mapper mapper;

	private final SupplyOrderService supplyOrderService;

	private final WorkLoadRepository workLoadRepository;

	private final WorkLoadOlRepository workLoadOlRepository;
	
	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private WorkLoadDto updateSodetails(SupplyOrder so, WorkLoadDto workLoadDto) {
		workLoadDto.setSupplyOrderId(so.getId());
		workLoadDto.setSoOrderNo(so.getOrderNo());
		workLoadDto.setValue(so.getValue());
		workLoadDto.setNoOfItems(so.getNoOfItems());
		if (so.getIndenter() != null) {
			workLoadDto.setOrderIssuedBy(so.getIndenter().getName());
		}
		if (so.getSoType() != null) {
			workLoadDto.setMainEqptItem(so.getSoType().getSoType());
		}
		workLoadDto.setTotalQty(so.getTotalQty());
		workLoadDto.setSoDate(outputDateFormat.format(so.getOrderDate()));
		if (null != so.getDd()) {
			workLoadDto.setOriginalDd(outputDateFormat.format(so.getDd()));
		} else {
			workLoadDto.setOriginalDd("");
		}

		if (null != so.getSupplier()) {
			workLoadDto.setSupplierName(so.getSupplier().getName());
		} else {
			workLoadDto.setSupplierName("");
		}
		
		
		if(StringUtils.isNoneBlank(so.getFileNo()))
		{
			workLoadDto.setFileNo(so.getFileNo());
		}
		return workLoadDto;
	}
	
	public WorkLoadDto getWorkLoadBySoId(int soId) {
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
		WorkLoad workLoad = workLoadRepository.getWorkLoadBySupplyOrder(supplyOrder);
		if (workLoad == null) {
			throw new DataNotFoundException("WorkLoad not found Exception by So Id" + soId);
		}
		WorkLoadDto workLoadDto = mapper.map(workLoad, WorkLoadDto.class);
		SupplyOrder so = workLoad.getSupplyOrder();
		workLoadDto.setSupplyOrderId(so.getId());
		workLoadDto.setSoDate(so.getOrderDate().toString());
//		if(StringUtils.isNoneBlank(supplyOrder.getFileNo()))
//		{
//			workLoadDto.setFileNo(supplyOrder.getFileNo());
//		}

		workLoadDto = updateSodetails(so, workLoadDto);
		
		//Todo:  Add loop for the detail rows
		String regex = "[0-9]+"; // regex to check if string contains only digits
		Pattern pattern = Pattern.compile(regex); // compiles the regex
		List<WorkloadInspActDetDto> wrkLdPreDetDtos = new ArrayList<>();
		if (workLoad.getWorkLoadDets() != null) {
			for (WorkloadInspActDet detBo : workLoad.getWorkLoadDets()) {
				WorkloadInspActDetDto detDto = mapper.map(detBo, WorkloadInspActDetDto.class);				
				detDto.setInspPhaseActId(detBo.getInspPhaseActivities().getId());
				detDto.setInspPhaseActivity(detBo.getInspPhaseActivities().getActivity());
				detDto.setSlNo(detBo.getInspPhaseActivities().getSlNo());
				Matcher matcherText = pattern.matcher(detBo.getInspPhaseActivities().getSlNo());
				detDto.setBold(matcherText.matches());
				wrkLdPreDetDtos.add(detDto);
			}
		}
		workLoadDto.setWrkLdPreDetDtos(wrkLdPreDetDtos.stream().sorted(Comparator.comparing(WorkloadInspActDetDto::getSlNo)).collect(Collectors.toList())); 
		
		//Todo: add for the offer letters
		List<Object[]>  olData= workLoadOlRepository.getwrkLoadDataBySupplyOrder(supplyOrder.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<WorkloadInspActDetDto> wkldInDetDtos = new ArrayList<>();
		List<WorkloadInspActDetDto> wkldPostDetDtos = new ArrayList<>();
		for (Object[] item : olData) {
			WorkloadInspActDetDto detDto = new WorkloadInspActDetDto();

			// Shubham Anand: setting data
			if (item[0] != null)
				detDto.setInspPhaseActId(Integer.parseInt(item[0].toString()));
			if (item[1] != null) {
				detDto.setSlNo(item[1].toString());
				Matcher matcherText = pattern.matcher(item[1].toString());
				detDto.setBold(matcherText.matches());
			}
			if (item[2] != null)
				detDto.setInspPhaseActivity(item[2].toString());
			if (item[3] != null)
				detDto.setEstManDays(Double.parseDouble(item[3].toString()));
			if (item[4] != null)
				detDto.setPlanCompletion(sdf.format(item[4]));
			if (item[5] != null)
				detDto.setActualManDays(Double.parseDouble(item[5].toString()));
			if (item[6] != null)
				detDto.setActualCompletion(sdf.format(item[6]));
			if (item[7] != null)
				detDto.setRemarks(item[7].toString());

			if (item[8] != null && Integer.parseInt(item[8].toString()) == 2)
				wkldInDetDtos.add(detDto);
			else if (item[8] != null && Integer.parseInt(item[8].toString()) == 3)
				wkldPostDetDtos.add(detDto);
		}
		workLoadDto.setWrkLdInprogDetDtos(wkldInDetDtos.stream().sorted(Comparator.comparing(WorkloadInspActDetDto::getSlNo)).collect(Collectors.toList()));
		workLoadDto.setWrkLdPostDetDtos(wkldPostDetDtos.stream().sorted(Comparator.comparing(WorkloadInspActDetDto::getSlNo)).collect(Collectors.toList()));
		
		return workLoadDto;

	}
	
}
