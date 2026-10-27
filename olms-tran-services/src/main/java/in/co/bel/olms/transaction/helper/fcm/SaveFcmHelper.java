package in.co.bel.olms.transaction.helper.fcm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.FcmDto;
import in.co.bel.olms.transaction.dto.FcmVendorDto;
import in.co.bel.olms.transaction.entity.Fcm;
import in.co.bel.olms.transaction.entity.FcmVendor;
import in.co.bel.olms.transaction.repository.FcmVendorRepository;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 *
 */

@Component
@RequiredArgsConstructor
public class SaveFcmHelper {
	
	private final Logger logger =LoggerFactory.getLogger(SaveFcmHelper.class);

	private final Mapper mapper;

	private final FcmVendorRepository fcmVendorRepository;
	
	public Fcm prepareSaveFCMObj(FcmDto fcmDto, Fcm fcm) {
		try {
			fcm.setApprovedDrg(fcmDto.getApprovedDrg());

			fcm.setApprovedQap(fcmDto.getApprovedQap());

			SimpleDateFormat dateformat = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT_INPUT);
			
			fcm.setOemTechSpecs(fcmDto.getOemTechSpecs());

			fcm.setImportDocs(fcmDto.getImportDocs());

			fcm.setSpecInst(fcmDto.getSpecInst());

			fcm.setInspDiscrepancy(fcmDto.getInspDiscrepancy());

			if (StringUtils.isNotBlank(fcmDto.getNewDrgDate())) {
				fcm.setNewDrgDate(dateformat.parse(fcmDto.getNewDrgDate()));
			} else {
				fcm.setNewDrgDate(null);
			}

			if (StringUtils.isNotBlank(fcmDto.getExistDrgDate())) {
				fcm.setExistDrgDate(dateformat.parse(fcmDto.getExistDrgDate()));
			} else {
				fcm.setExistDrgDate(null);
			}

			if (StringUtils.isNotBlank(fcmDto.getFreshQapDate())) {
				fcm.setFreshQapDate(dateformat.parse(fcmDto.getFreshQapDate()));
			} else {
				fcm.setFreshQapDate(null);
			}

			if (StringUtils.isNotBlank(fcmDto.getExistQapDate())) {
				fcm.setExistQapDate(dateformat.parse(fcmDto.getExistQapDate()));
			} else {
				fcm.setExistQapDate(null);
			}

			fcm.setQtTestApplicable(fcmDto.getQtTestApplicable());

			fcm.setDetailBom(fcmDto.getDetailBom());

			fcm.setApprovedPil(fcmDto.getApprovedPil());

			fcm.setBillTransport(fcmDto.getBillTransport());

			fcm.setInvoiceOemOriginCert(fcmDto.getInvoiceOemOriginCert());

			fcm.setBillEntryWarehouse(fcmDto.getBillEntryWarehouse());

			fcm.setCertOfConfirmity(fcmDto.getCertOfConfirmity());

			fcm.setWarranty(fcmDto.getWarranty());

			fcm.setMeltPouring(fcmDto.getMeltPouring());

			fcm.setSampleTestNabl(fcmDto.getSampleTestNabl());

			fcm.setSubOrderDet(fcmDto.getSubOrderDet());

			if (StringUtils.isNotBlank(fcmDto.getSubmittedDate())) {
				fcm.setSubmittedDate(dateformat.parse(fcmDto.getSubmittedDate()));
			} else {
				fcm.setSubmittedDate(null);
			}
			
			fcm.setInproInspPcb(fcmDto.getInproInspPcb());
			fcm.setInproInspUnit(fcmDto.getInproInspUnit());
			
			fcm.setInproInspNpe(fcmDto.getInproInspNpe());
			
			fcm.setFinalQt(fcmDto.getFinalQt());
			
			fcm.setFinalFats(fcmDto.getFinalFats());
			
			fcm.setFinalPacking(fcmDto.getFinalPacking());
			fcm.setFinalClearance(fcmDto.getFinalClearance());
			
			fcm.setFinalVendorRating(fcmDto.getFinalVendorRating());
			
			fcm.setFinalConsignee(fcmDto.getFinalConsignee());
			
			fcm.setOthers(fcmDto.getOthers());		
			
			if(null!=  fcmDto.getFcmVendor())
			{
				List<FcmVendor> vendors = prepVendors(fcmDto.getFcmVendor(),fcm);
				if(!vendors.isEmpty())
				{
					vendors=fcmVendorRepository.saveAll(vendors);
					fcm.setFcmVendors(vendors);
				}
				
			}
			
			
		} catch (ParseException e) {
			logger.error("Error during PARSING: ", e);
		}

		return fcm;
	}

	
	
	public List<FcmVendor> prepVendors(List<FcmVendorDto> fcmVendorDtos, Fcm fcm)
	{
		List<FcmVendor> vendors= new ArrayList<>();
		
		for( FcmVendorDto detDto : fcmVendorDtos)
		{
			FcmVendor detBo;
			if(detDto.getId()==0)
			{
				 detBo= mapper.map(detDto, FcmVendor.class);
			}
			else
			{				
				Optional<FcmVendor> optVendor= fcmVendorRepository.findById(detDto.getId());
				if(optVendor.isPresent())
				{
					detBo = optVendor.get(); 
				}
				else
				{
					throw new DataNotFoundException("Prep vendor : FCM vendor not found by  Id: " + detDto.getId());
				}
				detBo.setItemDesc(detDto.getItemDesc());
				detBo.setAgency(detDto.getAgency());
				detBo.setVendor(detDto.getVendor());				
			}
			detBo.setFcm(fcm);
			if(StringUtils.isNoneEmpty(detDto.getItemDesc())   &&   StringUtils.isNoneEmpty(detDto.getAgency())        &&  StringUtils.isNoneEmpty(detDto.getVendor())     )
			{
				vendors.add(detBo);
			}
		}
		
		return vendors;
	}
	
}
