package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.OpaDto;
import in.co.bel.olms.master.entity.Opa;
import in.co.bel.olms.master.service.OpaService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/opa")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OpaController {
	
	Gson gson = new Gson();

	private final OpaService opaService;

	private final Mapper mapper;

	@PostMapping("/save")
	public ResponseEntity<String> saveOpa(@RequestBody @Valid OpaDto opaDto) {
		
		Boolean name = opaService.existsByopaName(opaDto.getOpaName());
		Boolean shortname = opaService.existsByopashortName(opaDto.getOpashortName());
		
		if(name) {
			return new ResponseEntity<String>("OPA Name is already exists",HttpStatus.CONFLICT);
		}
		else if(shortname) {
			return new ResponseEntity<>("OPA Shortname is already exists",HttpStatus.CONFLICT);
		}
		else {
			Opa savedOpa = opaService.saveOpa(opaDto);
			OpaDto savedOpaDto = mapper.map(savedOpa, OpaDto.class);
			return new ResponseEntity<>(gson.toJson(savedOpaDto),HttpStatus.CREATED);
		}
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<OpaDto>> getAllOpa() {
		List<OpaDto> opaDtoList = new ArrayList<>();
		List<Opa> opas = opaService.getAllOpa();
		for (Opa opa : opas) {
			OpaDto opaDto = mapper.map(opa, OpaDto.class);
			opaDtoList.add(opaDto);
		}	
		return ResponseEntity.ok(opaDtoList);

	}
	
}
