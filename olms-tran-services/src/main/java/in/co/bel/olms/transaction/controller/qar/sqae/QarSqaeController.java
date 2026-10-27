package in.co.bel.olms.transaction.controller.qar.sqae;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.qar.sqae.QarSqaeDto;
import in.co.bel.olms.transaction.service.qar.sqae.QarSqaeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/qarSqae")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class QarSqaeController {

	private final QarSqaeService qarSqaeService;
	
	@Value("${bel.jwt.secret}")
	private String jwtSecret;

	private static final Logger logger =LoggerFactory.getLogger(QarSqaeController.class);
	
	/**
	 * To save as Draft QAR by the inspector
	 * @since 11-05-2024
	 * @param qarSqaeDto
	 * @return
	 */
	
	@PostMapping("/saveQarSqae")
	public ResponseEntity<QarSqaeDto> saveQar(@RequestBody @Valid QarSqaeDto qarSqaeDto) {
		try {
			QarSqaeDto dto =  qarSqaeService.saveQar(qarSqaeDto);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		}catch (DataNotValidException ex) {
			logger.error("Save QAR error:  {}", ex.getMessage());
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error("Error occurred while saving Qar: ", e);
			logger.error("Save QAR error:  {}",e.getMessage());
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}

	
	/**
	 * To submit QAR by the inspector
	 * @since 11-05-2024
	 * @param qarSqaeDto
	 * @return
	 */
	
	@PostMapping("/submitQarSqae")
	public ResponseEntity<QarSqaeDto> submitQar(@RequestBody @Valid QarSqaeDto qarSqaeDto) {
		try {	
			QarSqaeDto dto =  qarSqaeService.submitQar(qarSqaeDto);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (DataNotValidException e) {
			logger.error("Submit QAR error:  {}", e.getMessage());
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.BAD_REQUEST);
		}	
		catch (Exception e) {
			logger.error("Submit QAR error:  {}", e.getMessage());
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	/**
	 * 
	 * @since 11-05-2024
	 * @param olId
	 * @return
	 */
	@GetMapping("/ol/{olId}")
	public ResponseEntity<QarSqaeDto> getQarByolId(@PathVariable int olId, @RequestHeader("Authorization") String token) {
		try {
			String jwtToken = token.substring(7);
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
			
			int empId = claims.get("empId", Integer.class);
			QarSqaeDto dto = qarSqaeService.getQarByolId(olId, empId);	
			return ResponseEntity.ok(dto);
		}		
		catch (DataNotValidException e) {
			logger.error("Get QAR by Offer letter error:  {}", e.getMessage());
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.BAD_REQUEST);
		}			
		catch (Exception e) {			
			logger.error("Get QAR by Offer letter error:  {}", e.getMessage());			
			return new ResponseEntity<>(new QarSqaeDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
