package in.co.bel.olms.transaction.controller;

import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.ReturnOfferLetterDto;
import in.co.bel.olms.transaction.entity.ReturnOfferLetter;
import in.co.bel.olms.transaction.service.ol.ReturnOfferLetterService;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 * @since 11-05-2023
 */

@RestController
@RequestMapping("/returnOfferLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ReturnOfferLetterController {

	private final Mapper mapper;
	
	private static final Logger logger =LoggerFactory.getLogger(ReturnOfferLetterController.class);

	private final ReturnOfferLetterService returnOfferLetterService;
	
	/**
	 * 
	 * @param returnOfferLetterDto
	 * @return
	 */
	@PostMapping("/saveReturnOfferLetter")
	public ResponseEntity<ReturnOfferLetterDto> saveReturnOfferLetter(@RequestBody @Valid ReturnOfferLetterDto returnOfferLetterDto) {
		ReturnOfferLetter returnOfferLetter = returnOfferLetterService.saveReturnOfferLetter(returnOfferLetterDto);
		ReturnOfferLetterDto savedReturnOfferLetterDto = mapper.map(returnOfferLetter, ReturnOfferLetterDto.class);
		return new ResponseEntity<>(savedReturnOfferLetterDto, HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getById/{id}")
	public ResponseEntity<ReturnOfferLetterDto> getById(@PathVariable int id)
	{
		ReturnOfferLetterDto retDto= returnOfferLetterService.getById(id);
		return ResponseEntity.ok(retDto);

	}
	
	@GetMapping("/getByOlId/{offerLetterId}")
	public ResponseEntity<ReturnOfferLetterDto> getByOlId(@PathVariable int offerLetterId)
	{
		ReturnOfferLetterDto retDto= returnOfferLetterService.getByOlId(offerLetterId);
		return ResponseEntity.ok(retDto);

	}
	/**
	 * 
	 * @param salesGrpId
	 * @return
	 */
	
	@PreAuthorize("hasAnyAuthority('SBU_ADMIN')")
	@GetMapping("/getBySalesGrpId/{salesGrpId}")
	public ResponseEntity<List<ReturnOfferLetterDto>> getBySalesGrpId(@PathVariable int salesGrpId) {
		List<ReturnOfferLetterDto> olDtos = returnOfferLetterService.getBySalesGrpId(salesGrpId);
		return ResponseEntity.ok(olDtos);

	}
	
	
	/**
	 * 
	 * @param roleId
	 * @param empId
	 * @return
	 */
	
	@GetMapping("/getByEmp/{empId}")
	public ResponseEntity<List<ReturnOfferLetterDto>> getByEmp( @PathVariable int empId) {
		List<ReturnOfferLetterDto> olDtos = returnOfferLetterService.getByEmp(empId);
		return ResponseEntity.ok(olDtos);
	}
	
	
	/**
	 * 
	 * @param roleId
	 * @param empId
	 * @return
	 */
	
	@GetMapping("/getByEmpnSub/{empId}")
	public ResponseEntity<List<ReturnOfferLetterDto>> getByEmpnSub( @PathVariable int empId) {
		List<ReturnOfferLetterDto> olDtos = returnOfferLetterService.getByEmpnSub(empId);
		return ResponseEntity.ok(olDtos);
	}
	
}
