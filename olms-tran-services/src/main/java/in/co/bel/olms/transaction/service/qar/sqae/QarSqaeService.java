package in.co.bel.olms.transaction.service.qar.sqae;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.QARConstants;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.CrossCheckTier2Dto;
import in.co.bel.olms.transaction.dto.CrossCheckTier3Dto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarCheckReportDto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaeDto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaePartADto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaePartBDto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaePartCDto;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaePartDdto;
import in.co.bel.olms.transaction.entity.CrossCheckTier2;
import in.co.bel.olms.transaction.entity.CrossCheckTier3;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.qar.sqae.QarCheckReport;
import in.co.bel.olms.transaction.entity.qar.sqae.QarSqae;
import in.co.bel.olms.transaction.entity.qar.sqae.QarSqaePartA;
import in.co.bel.olms.transaction.entity.qar.sqae.QarSqaePartB;
import in.co.bel.olms.transaction.entity.qar.sqae.QarSqaePartC;
import in.co.bel.olms.transaction.entity.qar.sqae.QarSqaePartD;
import in.co.bel.olms.transaction.repository.CrossCheckTier2Repository;
import in.co.bel.olms.transaction.repository.CrossCheckTier3Repository;
import in.co.bel.olms.transaction.repository.QarAllotmentRepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarCheckReportRepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarSqaePartArepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarSqaePartBrepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarSqaePartCrepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarSqaePartDrepository;
import in.co.bel.olms.transaction.repository.qar.sqae.QarSqaeRepository;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QarSqaeService {

	private final QarSqaePartArepository partArepository;

	private final QarSqaePartBrepository partBrepository;

	private final QarSqaePartCrepository partCrepository;

	private final QarSqaePartDrepository partDrepository;

	private final CrossCheckTier2Repository tier2Repository;

	private final CrossCheckTier3Repository tier3Repository;

	private final QarAllotmentRepository qarAllotmentRepository;

	private final OfferLetterService offerLetterService;

	private final Mapper mapper;

	private final QarSqaeRepository qarSqaeRepository;

	private QarCheckReportRepository checkReportRepository;

	private final EntityValidator entityValidator;

	SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(QarSqaeService.class);

	@Transactional
	public QarSqaeDto saveQar(QarSqaeDto qarSqaeDto) {

		QarSqae qarSqae = new QarSqae();

		if (qarSqaeDto.getId() != 0) {
			Optional<QarSqae> optional = qarSqaeRepository.findById(qarSqaeDto.getId());
			if (optional.isPresent()) {
				qarSqae = optional.get();
				qarSqae.setId(optional.get().getId());
			}
		} else {
			qarSqae = mapper.map(qarSqaeDto, QarSqae.class);
			qarSqae.setCreatedDatetime(new Date());
		}

		if (qarSqaeDto.getQarSqaePartADto() != null) {
			QarSqaePartA partA = savePartA(qarSqaeDto.getQarSqaePartADto(), false);
			qarSqae.setQarPartA(partA);
		}
		if (qarSqaeDto.getQarSqaePartBDto() != null) {
			QarSqaePartB partB = savePartB(qarSqaeDto.getQarSqaePartBDto(), false);
			qarSqae.setQarPartB(partB);
		}
		if (qarSqaeDto.getQarSqaePartCDto() != null) {
			QarSqaePartC partC = savePartC(qarSqaeDto.getQarSqaePartCDto(), false);
			qarSqae.setQarPartC(partC);
		}
		if (qarSqaeDto.getQarSqaePartDdto() != null) {
			QarSqaePartD partD = savePartD(qarSqaeDto.getQarSqaePartDdto(), false);
			qarSqae.setQarPartD(partD);
		}
		if (qarSqaeDto.getCrossCheckTier3Dto() != null) {
			CrossCheckTier3 checkTier3 = saveCheckTier3(qarSqaeDto.getCrossCheckTier3Dto(), false);
			qarSqae.setCheckTier3(checkTier3);
		}
		if (qarSqaeDto.getCrossCheckTier2Dto() != null) {
			CrossCheckTier2 checkTier2 = saveCheckTier2(qarSqaeDto.getCrossCheckTier2Dto(), false);
			qarSqae.setCheckTier2(checkTier2);
		}
		if (qarSqaeDto.getCheckReportDto() != null) {
			QarCheckReport checkReport = saveQarCheckReport(qarSqaeDto.getCheckReportDto());
			qarSqae.setQarCheckReport(checkReport);
		}

		qarSqae = qarSqaeRepository.save(qarSqae);
		QarSqaeDto dto = mapper.map(qarSqae, QarSqaeDto.class);
		if (qarSqae.getQarPartA() != null) {
			dto.setQarSqaePartADto(mapper.map(qarSqae.getQarPartA(), QarSqaePartADto.class));
		}
		if (qarSqae.getQarPartB() != null) {
			dto.setQarSqaePartBDto(mapper.map(qarSqae.getQarPartB(), QarSqaePartBDto.class));
		}
		if (qarSqae.getQarPartC() != null) {
			dto.setQarSqaePartCDto(mapper.map(qarSqae.getQarPartC(), QarSqaePartCDto.class));
		}
		if (qarSqae.getQarPartD() != null) {
			dto.setQarSqaePartDdto(mapper.map(qarSqae.getQarPartD(), QarSqaePartDdto.class));
		}
		if (qarSqae.getCheckTier2() != null) {
			dto.setCrossCheckTier2Dto(mapper.map(qarSqae.getCheckTier2(), CrossCheckTier2Dto.class));
		}
		if (qarSqae.getCheckTier3() != null) {
			dto.setCrossCheckTier3Dto(mapper.map(qarSqae.getCheckTier3(), CrossCheckTier3Dto.class));
		}
		if (qarSqae.getQarCheckReport() != null) {
			dto.setCheckReportDto(mapper.map(qarSqae.getQarCheckReport(), QarCheckReportDto.class));
		}
		return dto;
	}

	public QarSqaePartA savePartA(QarSqaePartADto dto, boolean isSubmit) {

		QarSqaePartA partA = new QarSqaePartA();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<QarSqaePartA> partAOpt = partArepository.findById(dto.getId());
			if (partAOpt.isPresent()) {
				QarSqaePartA qarSqaePartA = partAOpt.get();
				partA = mapper.map(dto, QarSqaePartA.class);
				partA.setId(qarSqaePartA.getId());

			}
		} else {
			partA = mapper.map(dto, QarSqaePartA.class);
		}
		boolean isvalid = entityValidator.isValidEntity(partA);
		if (isvalid) {
			if(isSubmit) {
				partA.setSubmittedDate(new Date());
			}
			partA = partArepository.save(partA);
		} else {
			throw new DataNotValidException("QAR PART-A save:  " + partA.toString());
		}

		return (partA);

	}

	public QarSqaePartB savePartB(QarSqaePartBDto dto, boolean isSubmit) {

		QarSqaePartB partB = new QarSqaePartB();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<QarSqaePartB> partBOpt = partBrepository.findById(dto.getId());
			if (partBOpt.isPresent()) {
				partB = mapper.map(dto, QarSqaePartB.class);
				partB.setId(partBOpt.get().getId());
			}
		} else {
			partB = mapper.map(dto, QarSqaePartB.class);
		}
		boolean isvalid = entityValidator.isValidEntity(partB);
		if (isvalid) {
			if(isSubmit) {
				partB.setSubmittedDate(new Date());
			}
			partB = partBrepository.save(partB);
		} else {
			throw new DataNotValidException("QAR PART-B save:  " + partB.toString());
		}

		return (partB);

	}

	public QarSqaePartC savePartC(QarSqaePartCDto dto, boolean isSubmit) {

		QarSqaePartC partC = new QarSqaePartC();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<QarSqaePartC> partCOpt = partCrepository.findById(dto.getId());
			if (partCOpt.isPresent()) {
				partC = mapper.map(dto, QarSqaePartC.class);
				partC.setId(partCOpt.get().getId());
			}
		} else {
			partC = mapper.map(dto, QarSqaePartC.class);
		}
		boolean isvalid = entityValidator.isValidEntity(partC);
		if (isvalid) {
			if(isSubmit) {
				partC.setSubmittedDate(new Date());
			}
			partC = partCrepository.save(partC);
		} else {
			throw new DataNotValidException("QAR PART-B save:  " + partC.toString());
		}

		return (partC);

	}

	public QarSqaePartD savePartD(QarSqaePartDdto dto, boolean isSubmit) {

		QarSqaePartD partD = new QarSqaePartD();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<QarSqaePartD> partDOpt = partDrepository.findById(dto.getId());
			if (partDOpt.isPresent()) {
				partD = mapper.map(dto, QarSqaePartD.class);
				partD.setId(partDOpt.get().getId());
			}
		} else {
			partD = mapper.map(dto, QarSqaePartD.class);
		}
		boolean isvalid = entityValidator.isValidEntity(partD);
		if (isvalid) {
			if(isSubmit) {
				partD.setSubmittedDate(new Date());
			}
			partD = partDrepository.save(partD);
		} else {
			throw new DataNotValidException("QAR PART-D save:  " + partD.toString());
		}

		return (partD);
	}

	public CrossCheckTier2 saveCheckTier2(CrossCheckTier2Dto dto, boolean isSubmit) {

		CrossCheckTier2 crossCheckTier2 = new CrossCheckTier2();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<CrossCheckTier2> optional = tier2Repository.findById(dto.getId());
			if (optional.isPresent()) {
				crossCheckTier2 = mapper.map(dto, CrossCheckTier2.class);
				crossCheckTier2.setId(optional.get().getId());
			}
		} else {
			crossCheckTier2 = mapper.map(dto, CrossCheckTier2.class);
		}
		boolean isvalid = entityValidator.isValidEntity(crossCheckTier2);
		if (isvalid) {
			if(isSubmit) {
				crossCheckTier2.setSubmittedDate(new Date());
			}
			crossCheckTier2 = tier2Repository.save(crossCheckTier2);
		} else {
			throw new DataNotValidException("CrossCheckTier2 save:  " + crossCheckTier2.toString());
		}

		return (crossCheckTier2);

	}

	public CrossCheckTier3 saveCheckTier3(CrossCheckTier3Dto dto, boolean isSubmit) {

		CrossCheckTier3 checkTier3 = new CrossCheckTier3();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<CrossCheckTier3> optional = tier3Repository.findById(dto.getId());
			if (optional.isPresent()) {
				checkTier3 = mapper.map(dto, CrossCheckTier3.class);
				checkTier3.setId(optional.get().getId());
			}
		} else {
			checkTier3 = mapper.map(dto, CrossCheckTier3.class);
		}
		boolean isvalid = entityValidator.isValidEntity(checkTier3);
		if (isvalid) {
			if(isSubmit) {
				checkTier3.setSubmittedDate(new Date());
			}
			checkTier3 = tier3Repository.save(checkTier3);
		} else {
			throw new DataNotValidException("CrossCheckTier3 save:  " + checkTier3.toString());
		}

		return checkTier3;

	}

	public QarCheckReport saveQarCheckReport(QarCheckReportDto dto) {

		QarCheckReport checkReport = new QarCheckReport();
		if (dto.getId() != null && dto.getId() != 0) {
			Optional<QarCheckReport> optional = checkReportRepository.findById(dto.getId());
			if (optional.isPresent()) {
				checkReport = mapper.map(dto, QarCheckReport.class);
				checkReport.setId(optional.get().getId());
			}
		} else {
			checkReport = mapper.map(dto, QarCheckReport.class);
		}
		boolean isvalid = entityValidator.isValidEntity(checkReport);
		if (isvalid) {
			checkReport = checkReportRepository.save(checkReport);
		} else {
			throw new DataNotValidException("QarCheckReport save:  " + checkReport.toString());
		}

		return checkReport;

	}

	@Transactional
	public QarSqaeDto submitQar(QarSqaeDto qarSqaeDto) {

		QarSqae qarSqae = new QarSqae();

		if (qarSqaeDto.getId() != null && qarSqaeDto.getId() != 0) {
			qarSqae = qarSqaeRepository.getQarById(qarSqaeDto.getId());
		} else {
			qarSqae = mapper.map(qarSqaeDto, QarSqae.class);
			qarSqae.setCreatedDatetime(new Date());
		}
		if (qarSqaeDto.getQarSqaePartADto() != null) {
			QarSqaePartA partA = savePartA(qarSqaeDto.getQarSqaePartADto(), true);
			qarSqae.setQarPartA(partA);
		}
		if (qarSqaeDto.getQarSqaePartBDto() != null) {
			QarSqaePartB partB = savePartB(qarSqaeDto.getQarSqaePartBDto(), true);
			qarSqae.setQarPartB(partB);
		}
		if (qarSqaeDto.getQarSqaePartCDto() != null) {
			QarSqaePartC partC = savePartC(qarSqaeDto.getQarSqaePartCDto(), true);
			qarSqae.setQarPartC(partC);
		}
		if (qarSqaeDto.getQarSqaePartDdto() != null) {
			QarSqaePartD partD = savePartD(qarSqaeDto.getQarSqaePartDdto(), true);
			qarSqae.setQarPartD(partD);
		}
		if (qarSqaeDto.getCrossCheckTier3Dto() != null) {
			CrossCheckTier3 checkTier3 = saveCheckTier3(qarSqaeDto.getCrossCheckTier3Dto(), true);
			qarSqae.setCheckTier3(checkTier3);
		}
		if (qarSqaeDto.getCrossCheckTier2Dto() != null) {
			CrossCheckTier2 checkTier2 = saveCheckTier2(qarSqaeDto.getCrossCheckTier2Dto(), true);
			qarSqae.setCheckTier2(checkTier2);
		}
		if (qarSqaeDto.getCheckReportDto() != null) {
			QarCheckReport checkReport = saveQarCheckReport(qarSqaeDto.getCheckReportDto());
			qarSqae.setQarCheckReport(checkReport);
		}
		Date todayDate = new Date();
		if (qarSqae.getCreatedDatetime() == null) {
			qarSqae.setCreatedDatetime(todayDate);
		}
		qarSqae.setSubmittedDatetime(todayDate);
		qarSqae = qarSqaeRepository.save(qarSqae);
		QarSqaeDto dto = mapper.map(qarSqae, QarSqaeDto.class);

		if (qarSqae.getQarPartA() != null) {
			dto.setQarSqaePartADto(mapper.map(qarSqae.getQarPartA(), QarSqaePartADto.class));
		}
		if (qarSqae.getQarPartB() != null) {
			dto.setQarSqaePartBDto(mapper.map(qarSqae.getQarPartB(), QarSqaePartBDto.class));
		}
		if (qarSqae.getQarPartC() != null) {
			dto.setQarSqaePartCDto(mapper.map(qarSqae.getQarPartC(), QarSqaePartCDto.class));
		}
		if (qarSqae.getQarPartD() != null) {
			dto.setQarSqaePartDdto(mapper.map(qarSqae.getQarPartD(), QarSqaePartDdto.class));
		}
		if (qarSqae.getCheckTier2() != null) {
			dto.setCrossCheckTier2Dto(mapper.map(qarSqae.getCheckTier2(), CrossCheckTier2Dto.class));
		}
		if (qarSqae.getCheckTier3() != null) {
			dto.setCrossCheckTier3Dto(mapper.map(qarSqae.getCheckTier3(), CrossCheckTier3Dto.class));
		}
		if (qarSqae.getQarCheckReport() != null) {
			dto.setCheckReportDto(mapper.map(qarSqae.getQarCheckReport(), QarCheckReportDto.class));
		}

		return dto;
	}

	public QarSqaeDto getQarByolId(int olId, int empId) {

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
		QarSqae qarSqae = qarSqaeRepository.getQarByOfferLetter(offerLetter.getId());
		List<String> allottedParts = qarAllotmentRepository.getAllottedQARSections(empId, offerLetter.getId());
		QarSqaeDto qarSqaeDto = new QarSqaeDto();
		if (qarSqae == null) {
			qarSqaeDto.setId(0);
			qarSqaeDto.setSubmittedDatetime(null);
		} else {
			qarSqaeDto = mapper.map(qarSqae, QarSqaeDto.class);
			for (String allottedPart : allottedParts) {
				switch (allottedPart) {
				case QARConstants.PART_A:
					qarSqaeDto.setPartaAllottedStatus(Boolean.TRUE);
					break;
				case QARConstants.PART_B:
					qarSqaeDto.setPartBAllottedStatus(Boolean.TRUE);
					break;
				case QARConstants.PART_C:
					qarSqaeDto.setPartCAllottedStatus(Boolean.TRUE);
					break;
				case QARConstants.PART_D:
					qarSqaeDto.setPartDAllottedStatus(Boolean.TRUE);
					break;
				case QARConstants.TIER_II:
					qarSqaeDto.setTierIIAllottedStatus(Boolean.TRUE);
					break;
				case QARConstants.TIER_III:
					qarSqaeDto.setTierIIIAllottedStatus(Boolean.TRUE);
					break;
				default:
					break;
				}
			}
			if (offerLetter != null && offerLetter.getSaleOrder() != null
					&& offerLetter.getSaleOrder().getSupplyOrder() != null) {
				SupplyOrder supplyOrder = offerLetter.getSaleOrder().getSupplyOrder();
				qarSqaeDto.setIndentNo(supplyOrder.getOrderNo());
				qarSqaeDto.setSupplyOrderDate(dateFormat.format(supplyOrder.getOrderDate()));
				if (supplyOrder.getSalesGroup() != null) {
					qarSqaeDto.setFirmName(supplyOrder.getSalesGroup().getName());
				}
				if (supplyOrder.getIndenter() != null) {
					qarSqaeDto.setIndentor(supplyOrder.getIndenter().getAddress());
				}
			}
			if (qarSqae.getQarPartA() != null) {
				Optional<QarSqaePartA> partA = partArepository.findById(qarSqae.getQarPartA().getId());
				if (partA.isPresent()) {
					qarSqaeDto.setQarSqaePartADto(mapper.map(partA.get(), QarSqaePartADto.class));
				}
			}
			if (qarSqae.getQarPartB() != null) {
				Optional<QarSqaePartB> partB = partBrepository.findById(qarSqae.getQarPartB().getId());
				if (partB.isPresent()) {
					qarSqaeDto.setQarSqaePartBDto(mapper.map(partB.get(), QarSqaePartBDto.class));
				}
			}
			if (qarSqae.getQarPartC() != null) {
				Optional<QarSqaePartC> partC = partCrepository.findById(qarSqae.getQarPartC().getId());
				if (partC.isPresent()) {
					qarSqaeDto.setQarSqaePartCDto(mapper.map(partC.get(), QarSqaePartCDto.class));
				}
			}
			if (qarSqae.getQarPartD() != null) {
				Optional<QarSqaePartD> partD = partDrepository.findById(qarSqae.getQarPartD().getId());
				if (partD.isPresent()) {
					qarSqaeDto.setQarSqaePartDdto(mapper.map(partD.get(), QarSqaePartDdto.class));
				}
			}
			if (qarSqae.getCheckTier2() != null) {
				Optional<CrossCheckTier2> checkTier2 = tier2Repository.findById(qarSqae.getCheckTier2().getId());
				if (checkTier2.isPresent()) {
					qarSqaeDto.setCrossCheckTier2Dto(mapper.map(checkTier2.get(), CrossCheckTier2Dto.class));
				}
			}
			if (qarSqae.getCheckTier3() != null) {
				Optional<CrossCheckTier3> checkTier3 = tier3Repository.findById(qarSqae.getCheckTier3().getId());
				if (checkTier3.isPresent()) {
					qarSqaeDto.setCrossCheckTier3Dto(mapper.map(checkTier3.get(), CrossCheckTier3Dto.class));
				}
			}
			if (qarSqae.getQarCheckReport() != null) {
				Optional<QarCheckReport> checkReport = checkReportRepository
						.findById(qarSqae.getQarCheckReport().getId());
				if (checkReport.isPresent()) {
					qarSqaeDto.setCheckReportDto(mapper.map(checkReport.get(), QarCheckReportDto.class));
				}
			}

		}
		return qarSqaeDto;

	}

}
