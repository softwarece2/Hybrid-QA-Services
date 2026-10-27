package in.co.bel.olms.transaction.controller.check;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.check.ScrutinySoDetDto;
import in.co.bel.olms.transaction.dto.check.ScrutinySoDto;
import in.co.bel.olms.transaction.entity.check.ScrutinySo;
import in.co.bel.olms.transaction.service.check.ScrutinySoService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 06-03-2023
 *
 */

@RestController
@RequestMapping("/scrutinySo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinySoController {

	private final ScrutinySoService scrutinySoService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinySoController.class);

	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinySoDto> saveScrutinySo(@RequestBody @Valid ScrutinySoDto scrutinySoDto) {
		try {
			logger.info("scrutiny save {}",scrutinySoDto);
			ScrutinySo scrutinySo = scrutinySoService.saveScrutinySo(scrutinySoDto);
			scrutinySoDto = mapper.map(scrutinySo, ScrutinySoDto.class);
			List<ScrutinySoDetDto> soDetDtos = scrutinySoService.prepareSoDets(scrutinySo.getScrutinySoDets());
			scrutinySoDto.setScrutinySoDetDtos(soDetDtos);
			return new ResponseEntity<>(scrutinySoDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {

			logger.error("save ScrutinySo Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinySoDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinySo: ", e);
			logger.error("save ScrutinySo Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinySoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param scrutinySoDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinySoDto> submitScrutinySo(@RequestBody @Valid ScrutinySoDto scrutinySoDto) {
		try {		
			ScrutinySo scrutinySo = scrutinySoService.submitScrutinySo(scrutinySoDto);
			scrutinySoDto = mapper.map(scrutinySo, ScrutinySoDto.class);
			scrutinySoDto.setSubmittedDatetime(dateFormat.format(scrutinySo.getSubmittedDatetime()));
			List<ScrutinySoDetDto> soDetDtos = scrutinySoService.prepareSoDets(scrutinySo.getScrutinySoDets());
			scrutinySoDto.setScrutinySoDetDtos(soDetDtos);
			return new ResponseEntity<>(scrutinySoDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinySo Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinySoDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinySo Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinySoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 
	 * @author Manasa B
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<ScrutinySoDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		ScrutinySoDto fcm = scrutinySoService.getScrutinySoBySoId(soid);
		return ResponseEntity.ok(fcm);
	}

}
