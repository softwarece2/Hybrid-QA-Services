package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
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

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.IndenterDto;
import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.IndenterService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */
@RestController
@RequestMapping("/indenter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class IndenterController {

	Gson gson = new Gson();

	private final IndenterService indenterService;

	private final Mapper mapper;

	/**
	 * 
	 * @return
	 */

	@GetMapping("/getAll")
	public ResponseEntity<List<IndenterDto>> getAllIndenters() {

		List<IndenterDto> indenterDtos = new ArrayList<>();
		List<Indenter> indenters = indenterService.getAllIndenters();
		for (Indenter indenter : indenters) {
			IndenterDto indenterDto = mapper.map(indenter, IndenterDto.class);
			indenterDtos.add(indenterDto);
		}
		return ResponseEntity.ok(indenterDtos);

	}

	/**
	 * 
	 * @param indenterDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<String> saveIndenter(@RequestBody @Valid IndenterDto indenterDto) {
		Boolean name = indenterService.existsByname(indenterDto.getName());
		Boolean shortname = indenterService.existsByshortName(indenterDto.getShortName());
		if (name) {
			return new ResponseEntity<String>("Indenter name already exists.", HttpStatus.CONFLICT);
		} else if (shortname) {
			return new ResponseEntity<String>("Indenter Shortname already exists.", HttpStatus.CONFLICT);
		} else {
			indenterDto.setActive(true);
			Indenter savedIndenter = indenterService.saveIndenter(indenterDto);
			IndenterDto savedIndenterDto = mapper.map(savedIndenter, IndenterDto.class);
			return new ResponseEntity<>(gson.toJson(savedIndenterDto), HttpStatus.CREATED);
		}
	}

	/**
	 * 
	 * @param indenterDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@PostMapping("/update")
	public ResponseEntity<String> updateIndenter(@RequestBody @Valid IndenterDto indenterDto) {
		try {
			Indenter savedIndenter = indenterService.updateIndenter(indenterDto);
			IndenterDto responseDto = mapper.map(savedIndenter, IndenterDto.class);
			return new ResponseEntity<String>(gson.toJson(responseDto), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<IndenterDto> getIndenter(@PathVariable int id) throws DataNotFoundException {
		Indenter indenter = indenterService.getIndenter(id);
		IndenterDto indenterDto = mapper.map(indenter, IndenterDto.class);
		return ResponseEntity.ok(indenterDto);
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/getAllActiveIndenters")
	public ResponseEntity<List<IndenterDto>> getAllActiveIndentors() {
		List<IndenterDto> indenterDtoList = new ArrayList<>();
		List<Indenter> indenterActiveList = indenterService.getAllActiveIndenters();
		for (Indenter indenterActive : indenterActiveList) {
			IndenterDto indenterActiveDto = mapper.map(indenterActive, IndenterDto.class);
			indenterDtoList.add(indenterActiveDto);
		}
		return ResponseEntity.ok(indenterDtoList);
	}

}
