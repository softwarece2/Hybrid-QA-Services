package in.co.bel.olms.transaction.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.transaction.dto.INoteRegisterDto;
import in.co.bel.olms.transaction.dto.INoteRegisterItemDto;
import in.co.bel.olms.transaction.dto.OLRegisterDto;
import in.co.bel.olms.transaction.dto.SORegisterDto;
import in.co.bel.olms.transaction.dto.SORegisterListDto;
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.service.RegistersService;
import lombok.RequiredArgsConstructor;

/**
 * Controller class with endpoints for various registers
 *
 * @author Awadhesh Verma
 * @since 26-08-2023
 */
@RestController
@RequestMapping("/registers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class RegistersController {

    private final  RegistersService registersService;
 
    private final SalesGroupRepository salesGroupRepository;

    private final  Mapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(RegistersController.class);

    @GetMapping("/so")
    public ResponseEntity<SORegisterListDto> getSORegister(@RequestParam(required = true) int orgId, @RequestParam(required = true) String fromDate,
                                                             @RequestParam(required = true) String toDate) {

        try {
            List<SupplyOrder> supplyOrders = registersService.getSORegister(orgId, fromDate, toDate);
            List<SORegisterDto> soRegisterDtoList = new ArrayList<>();
            SORegisterListDto  soRegisterListDto=new SORegisterListDto();
            double totalValue=0;
            for (SupplyOrder supplyOrder : supplyOrders) {
                SORegisterDto soRegisterDto = mapper.map(supplyOrder, SORegisterDto.class);
                Indenter indenter = supplyOrder.getIndenter();
                if (indenter != null) {
                    soRegisterDto.setIndentorName(indenter.getName());
                }
                Supplier supplier = supplyOrder.getSupplier();
                if (supplier != null) {
                    soRegisterDto.setSupplierName(supplier.getName());
                }
                soRegisterDto.setDescription(supplyOrder.getDescription());
                if(soRegisterDto.getValue()!=null) {
                totalValue=Double.valueOf(soRegisterDto.getValue())+totalValue;
                soRegisterDtoList.add(soRegisterDto);}
            }
            soRegisterListDto.setSoregisters(soRegisterDtoList);
            soRegisterListDto.setTotalSO(supplyOrders.size());
            soRegisterListDto.setTotalValue( (totalValue));
            return ResponseEntity.ok(soRegisterListDto);
        } catch (Exception e) {
            logger.error("Error in getting SO register {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @GetMapping("/ol")
    public ResponseEntity<List<OLRegisterDto>> getOLRegister(@RequestParam(required = true) int orgId, @RequestParam(required = true) String fromDate,
                                                             @RequestParam(required = true) String toDate) {

        try {
            List<Object[]> offerLetters = registersService.getOLRegister(orgId, fromDate, toDate);
            List<OLRegisterDto> olRegisterDtoList = new ArrayList<>();
            for (Object[] olObjArr : offerLetters) {
                OLRegisterDto olRegisterDto = new OLRegisterDto();
                olRegisterDto.setOlNumber(olObjArr[0] != null ? olObjArr[0].toString() : "");
                olRegisterDto.setOfferDate(olObjArr[1] != null ? (Date) olObjArr[1] : null);
                olRegisterDto.setIndentNo(olObjArr[2] != null ? olObjArr[2].toString() : "");
                olRegisterDto.setIndentDate(olObjArr[3] != null ? (Date) olObjArr[3] : null);
                olRegisterDto.setOrderDate(olObjArr[4] != null ? (Date) olObjArr[4] : null);
                olRegisterDto.setProjectDescription(olObjArr[5] != null ? olObjArr[5].toString() : "");
                olRegisterDto.setNoOfItems(olObjArr[6] != null ? new BigDecimal(olObjArr[6].toString()).intValue() : 0);
                olRegisterDto.setTotalQty(olObjArr[7] != null ? new BigDecimal(olObjArr[7].toString()).intValue() : 0);
                olRegisterDto.setValue(olObjArr[8] != null ? (double) olObjArr[8] : 0);
                olRegisterDto.setDd(olObjArr[9] != null ? (Date) olObjArr[9] : null);

                olRegisterDtoList.add(olRegisterDto);
            }
            return ResponseEntity.ok(olRegisterDtoList);
        } catch (Exception e) {
            logger.error("Error in getting OL register {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/surpriseCheck")
    public ResponseEntity<List<SurpriseCheckDto>> getSurpriseCheckRegister(@RequestParam(required = true) int orgId, @RequestParam(required = true) String fromDate,
                                                                           @RequestParam(required = true) String toDate) {

        try {
            List<Object[]> offerLetters = registersService.getSurpriseCheckRegister(orgId, fromDate, toDate);
            List<SurpriseCheckDto> surpriseCheckDtoList = new ArrayList<>();
            for (Object[] olObjArr : offerLetters) {
                SurpriseCheckDto surpriseCheckDto = new SurpriseCheckDto();
                surpriseCheckDto .setInspectionDate(olObjArr[0] != null ? olObjArr[0].toString() : "");
                surpriseCheckDto .setFirm(olObjArr[1] != null ? olObjArr[1].toString() : "");
                surpriseCheckDto .setItemNo(olObjArr[2] != null ? olObjArr[2].toString() : "");
                surpriseCheckDto .setItemDescription(olObjArr[3] != null ? olObjArr[3].toString() : "");
                surpriseCheckDto .setSupplyOrderNo(olObjArr[4] != null ? olObjArr[4].toString() : "");
                surpriseCheckDto .setIndividualResponsible(olObjArr[5] != null ? olObjArr[5].toString() : "");
                surpriseCheckDto .setTestCarried(olObjArr[6] != null ? olObjArr[6].toString() : "");
                surpriseCheckDto .setCrossVerifyBy(olObjArr[7] != null ? olObjArr[7].toString() : "");
                surpriseCheckDto .setCrossVerifyDate(olObjArr[8] != null ? olObjArr[8].toString() : "");
                surpriseCheckDto .setRemarks(olObjArr[9] != null ? olObjArr[9].toString() : "");

                surpriseCheckDtoList.add(surpriseCheckDto);
            }
            return ResponseEntity.ok(surpriseCheckDtoList);
        } catch (Exception e) {
            logger.error("Error in getting Surprise Check register {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/inoteRegister")
    public ResponseEntity<INoteRegisterDto> getInoteRegister(@RequestParam(required = true) int orgId, @RequestParam(required = true) String fromDate,
                                                                           @RequestParam(required = true) String toDate) {

        try {
           List<Object[]> inotes = registersService.getINoteRegister(orgId, fromDate, toDate);
           List<Object[]> salesGrpList=salesGroupRepository.findByStatus();
           Map<String, String> returnMap = new HashMap<>();
            INoteRegisterDto   iNoteRegisterDto=new INoteRegisterDto();
           List<INoteRegisterItemDto> inoteRegisterListDtoList = new ArrayList<>();
           DecimalFormat df = new DecimalFormat("0.00000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            int totalItems=0;
        	int  totalQty=0;
        	double totalAmt=0;
        	int salesGrpId;
            for (Object[] inoteObjArr : inotes) {
            	salesGrpId=new BigDecimal(inoteObjArr[2].toString()).intValue();
            	totalItems=totalItems+(new BigDecimal(inoteObjArr[12].toString()).intValue());
            	totalQty=totalQty+(new BigDecimal(inoteObjArr[13].toString()).intValue());	  
            	totalAmt= totalAmt+( (double) inoteObjArr[15]);
            	INoteRegisterItemDto inoteRegisterListDto = new INoteRegisterItemDto();
            	inoteRegisterListDto .setINoteNo(inoteObjArr[0] != null ? inoteObjArr[0].toString() : "");
            	String division = inoteObjArr[1] != null ? inoteObjArr[1].toString() : "";
            	inoteRegisterListDto .setDivision(division);
            	inoteRegisterListDto .setSectionId(((int)inoteObjArr[2]));
            	inoteRegisterListDto .setInoteDate(inoteObjArr[3] != null ? inoteObjArr[3].toString() : "");
            	inoteRegisterListDto .setOrderNo(inoteObjArr[4] != null ? inoteObjArr[4].toString() : "");
            	inoteRegisterListDto .setOrderDate(inoteObjArr[5] != null ? inoteObjArr[5].toString() : "");
            	inoteRegisterListDto .setIndenter(inoteObjArr[6] != null ? inoteObjArr[6].toString() : "");
            	inoteRegisterListDto .setOlNumber(inoteObjArr[7] != null ? inoteObjArr[7].toString() : "");
            	inoteRegisterListDto .setOlDate(inoteObjArr[8] != null ? inoteObjArr[8].toString() : "");
            	inoteRegisterListDto .setFileNo(inoteObjArr[9] != null ? inoteObjArr[9].toString() : "");
            	inoteRegisterListDto .setDd(inoteObjArr[10] != null ? inoteObjArr[10].toString() : "");
            	inoteRegisterListDto.setNoOfItems((new BigDecimal(inoteObjArr[12].toString()).intValue()));
            	inoteRegisterListDto.setQty((new BigDecimal(inoteObjArr[13].toString()).intValue()));
            	inoteRegisterListDto .setDeliveryType(inoteObjArr[11] != null ? inoteObjArr[11].toString() : "");	
            	inoteRegisterListDto .setValue(inoteObjArr[14] != null ? new BigDecimal(inoteObjArr[14].toString()).doubleValue() : 0);
            	inoteRegisterListDto .setDescription(inoteObjArr[16] != null ? inoteObjArr[16].toString() : "");	
            	inoteRegisterListDtoList.add(inoteRegisterListDto);
            	if(returnMap.get(division) != null) {
            		double totalValue =  Double.valueOf(returnMap.get(division)) + inoteRegisterListDto.getValue();
            		returnMap.put(division, df.format(totalValue));
            	}
            	else {
            		returnMap.put(inoteRegisterListDto.getDivision(), df.format(inoteRegisterListDto.getValue()));            		
            	}
            	
            }
			for (Object[] salesGrp : salesGrpList) {
				String sName = salesGrp[1] != null ? salesGrp[1].toString() : "";
				Set<String> sbuNamesWithAmt = returnMap.keySet();
				if (!sbuNamesWithAmt.contains(sName)) {
					returnMap.put(sName, "0");
				}
			}

            iNoteRegisterDto.setNoOfItems(totalItems);
            iNoteRegisterDto.setTotalQty(totalQty);
            iNoteRegisterDto.setTotalINotes(inotes.size());
            iNoteRegisterDto.setTotalValue(df.format(totalAmt));
            iNoteRegisterDto.setINoteRegisterItemDtos(inoteRegisterListDtoList);
            returnMap.forEach((k,v) -> {
            	double	val= Double.valueOf(v)/10000000;
             returnMap.put(k, df.format(val));
            });
            iNoteRegisterDto.setINoteValueMap(returnMap);
            return ResponseEntity.ok(iNoteRegisterDto);
        } catch (Exception e) {
        	logger.error("Error in getting INoteRegister ", e);
            logger.error("Error in getting Surprise Check register {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    
}
