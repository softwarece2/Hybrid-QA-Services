package in.co.bel.olms.iol.ate.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.iol.ate.dto.AteTestReportUploadDto;
import in.co.bel.olms.iol.ate.dto.FileDetailDto;
import in.co.bel.olms.iol.ate.dto.ProdAteDto;
import in.co.bel.olms.iol.ate.entity.AteTestReportUpload;
import in.co.bel.olms.iol.ate.repository.AteTestReportUploadRepository;
import in.co.bel.olms.iol.feign.FileUploadFeignService;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AteReportUploadService {

	private final AteTestReportUploadRepository ateTestReportUploadRepo;

	private final FileUploadFeignService fileUploadFeignService;

	private final OLLineItemSerialNoRepository olLineItemSerialNoRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final Mapper mapper;

    @Value("${file.domain-url}")
    private String domainUrl;
    
	@Value("${file.upload-dir}")
	private String fileUploadPath;
	 
	
	private static final Logger log = LoggerFactory.getLogger(AteReportUploadService.class);

	@Transactional
	public List<AteTestReportUploadDto> uploadAteReportData(AteTestReportUploadDto dto, MultipartFile[] files, Boolean isQa) {
	    List<AteTestReportUploadDto> uploadResults = new ArrayList<>();

	    for (MultipartFile file : files) {
	        if (file.isEmpty()) continue;

	        String fileHash;
	        try (InputStream is = file.getInputStream()) {
	            fileHash = DigestUtils.md5DigestAsHex(is);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to process file hash", e);
	        }

	        boolean fileExists = isQa ?
	            ateTestReportUploadRepo.existsByHashAndIdsInQa(fileHash, dto.getDevId(), dto.getProdId(), dto.getTestId(), dto.getRoundId(), dto.getSpecId()) :
	            ateTestReportUploadRepo.existsByHashAndIds(fileHash, dto.getDevId(), dto.getProdId(), dto.getTestId(), dto.getRoundId(), dto.getSpecId());

	        if (fileExists) {
	            throw new IllegalArgumentException("This file already exists. Upload a different file");
	        }

	        String fileName = fileUploadFeignService.storeFile(file);
	        
	        AteTestReportUpload newUpload = mapper.map(dto, AteTestReportUpload.class);
	        newUpload.setFileName(fileName);
	        newUpload.setFileHash(fileHash); 
	        newUpload.setOrigFileName(file.getOriginalFilename());

	        if (isQa) {
	            newUpload.setOllisn(olLineItemSerialNoRepository.findById(dto.getDevId()).orElse(null));
	        } else {
	            newUpload.setDev(subSlipSerialNoRepository.findById(dto.getDevId()).orElse(null));
	        }

	        newUpload = ateTestReportUploadRepo.save(newUpload);

	        AteTestReportUploadDto returnDto = mapper.map(newUpload, AteTestReportUploadDto.class);
	        returnDto.setFileDownloadUrl(getDownloadUrl(newUpload.getFileName()));
	        
	        if (isQa) {
	            returnDto.setOllisnId(newUpload.getOllisn() != null ? newUpload.getOllisn().getId() : null);
	        } else {
	            returnDto.setDevId(newUpload.getDev() != null ? newUpload.getDev().getId() : null);
	        }

	        uploadResults.add(returnDto);
	    }

	    return uploadResults;
	}

	public List<AteTestReportUploadDto> getUploadedReportDataList(Integer devId, Integer prodId, Integer testId, Integer roundId) {
		List<AteTestReportUpload> uploadDataList = ateTestReportUploadRepo.findListOfUploadedReportByIds(devId, prodId, testId, roundId);		
		return uploadDataList.stream().map(data -> mapper.map(data, AteTestReportUploadDto.class)).collect(Collectors.toList());
	}
	
	public List<AteTestReportUpload> getAllUploadedReports(Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId, Boolean isQa, Integer olLiId) {
	    if (isQa == null || !isQa) {
	        return ateTestReportUploadRepo.findAllByIds(devId, prodId, testId, roundId, specId);
	    } else {
	        Integer olsnSerialNoId = olLineItemSerialNoRepository.findBySerialNoOlLiId(devId, olLiId);
	        return ateTestReportUploadRepo.findAllByIdsInQa(olsnSerialNoId, prodId, testId, roundId, specId);
	    }
	}

	public String getFileName(Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId) {
		AteTestReportUpload uploadData = ateTestReportUploadRepo.findByIds(devId, prodId, testId, roundId, specId);
		return uploadData != null ? uploadData.getFileName() : null;
	}
	
	private String getDownloadUrl(String fileName) {
		return domainUrl + "olms-tran-services/files/" + fileName;
	}
	
	
	public void addFileToDto(AteTestReportUpload uploadData, ProdAteDto prodDto) {
	    FileDetailDto fileDetail = new FileDetailDto();
	    
	    String downloadUrl = domainUrl + "olms-tran-services/files/" + uploadData.getFileName();
	    
	    fileDetail.setFileUploadId(uploadData.getId());
	    fileDetail.setFileName(uploadData.getFileName());
	    fileDetail.setOrigFileName(uploadData.getOrigFileName());
	    fileDetail.setFileDownloadUrl(downloadUrl);
	    
	    prodDto.getFileDetails().add(fileDetail);
	}
	
	public String deleteFile(Integer fileUploadId) {
		AteTestReportUpload uploadData = ateTestReportUploadRepo.findById(fileUploadId)
				.orElseThrow(() -> new IllegalArgumentException("Uploaded file not found."));
		try {
			ateTestReportUploadRepo.delete(uploadData); // Deletes the entry from DB.
			fileUploadFeignService.deleteFile(uploadData.getFileName()); // Deletes the file form FileSystem
			return "File deleted succesfully";
		} catch (Exception e) {
			return e.getMessage();
		}

	}
	
	public List<AteTestReportUploadDto> getAteReportsBySpec(Integer devId, Integer prodId, Integer testId, Integer roundId, Integer specId, Boolean isQa) {
	    List<AteTestReportUpload> reports;
	    
	    if (isQa != null && isQa) {
	        reports = ateTestReportUploadRepo.findAllByIdsInQa(devId, prodId, testId, roundId, specId);
	    } else {
	        reports = ateTestReportUploadRepo.findAllByIds(devId, prodId, testId, roundId, specId);
	    }

	    return reports.stream().map(report -> {
	        AteTestReportUploadDto dto = mapper.map(report, AteTestReportUploadDto.class);
	        dto.setFileDownloadUrl(getDownloadUrl(report.getFileName()));
	        
	        if (isQa) {
	            if (report.getOllisn() != null) dto.setOllisnId(report.getOllisn().getId());
	        } else {
	            if (report.getDev() != null) dto.setDevId(report.getDev().getId());
	        }
	        return dto;
	    }).collect(Collectors.toList());
	}
	
}

