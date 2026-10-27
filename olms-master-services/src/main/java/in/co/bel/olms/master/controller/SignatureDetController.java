package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.SignatureDetDto;
import in.co.bel.olms.master.entity.SignatureDet;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.SignatureDetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signatureDet")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SignatureDetController {

	private final SignatureDetService signatureDetService;

	private final Mapper mapper;
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SignatureDetDto> getSignatureDetById(@PathVariable int id) throws DataNotFoundException {
		SignatureDet signatureDet = signatureDetService.getSignatureDetById(id);
		SignatureDetDto signatureDetDto = mapper.map(signatureDet, SignatureDetDto.class);
		return ResponseEntity.ok(signatureDetDto);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAll")
	public ResponseEntity<List<SignatureDetDto>> getAllSignatureDets() {
		List<SignatureDetDto> signatureDetDtoList = new ArrayList<>();
		List<SignatureDet> signatureDets = signatureDetService.getAllSignatureDet();
		for(SignatureDet signatureDet : signatureDets) {
			SignatureDetDto signatureDetDto = mapper.map(signatureDet, SignatureDetDto.class);
			signatureDetDtoList.add(signatureDetDto);
		}
		return ResponseEntity.ok(signatureDetDtoList);
	}

}
