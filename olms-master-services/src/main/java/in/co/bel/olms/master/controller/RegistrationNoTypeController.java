package in.co.bel.olms.master.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.RegistrationNoTypeDto;
import in.co.bel.olms.master.entity.RegistrationNoType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.RegistrationNoTypeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/registrationNoType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class RegistrationNoTypeController {

	private final RegistrationNoTypeService registrationNoTypeService;

	private final Mapper mapper;

	@GetMapping("/getAll")
	public ResponseEntity<List<RegistrationNoTypeDto>> getAllRegistrationNoType() {
		List<RegistrationNoType> registrationNoTypes = registrationNoTypeService.getAllRegistrationNoType();
		List<RegistrationNoTypeDto> registrationNoTypeDtoList = registrationNoTypes.stream()
				.map(RegistrationNoType -> mapper.map(RegistrationNoType, RegistrationNoTypeDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(registrationNoTypeDtoList);

	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<RegistrationNoTypeDto> getOrderTypeById(@PathVariable int id) throws DataNotFoundException {
		RegistrationNoType registrationNoType = registrationNoTypeService.getRegistrationNoTypeById(id);
		RegistrationNoTypeDto registrationNoTypeDto = mapper.map(registrationNoType, RegistrationNoTypeDto.class);
		return ResponseEntity.ok(registrationNoTypeDto);
	}
}
