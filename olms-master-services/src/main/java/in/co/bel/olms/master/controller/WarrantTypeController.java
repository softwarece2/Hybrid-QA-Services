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

import in.co.bel.olms.master.dto.WarrantTypeDto;
import in.co.bel.olms.master.entity.WarrantType;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.WarrantTypeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/warrantType")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WarrantTypeController {

		private final WarrantTypeService warrantTypeService;

		private final Mapper mapper;

		@GetMapping("/getAll")
		public ResponseEntity<List<WarrantTypeDto>> getAllWarrantType() {
			List<WarrantType> warrantTypes = warrantTypeService.getAllWarrantType();
			List<WarrantTypeDto> warrantTypeDtoList = warrantTypes.stream()
					.map(warrantType-> mapper.map(warrantType, WarrantTypeDto.class)).collect(Collectors.toList());
			return ResponseEntity.ok(warrantTypeDtoList);

		}
		@GetMapping("/getById/{id}")
		public ResponseEntity<WarrantTypeDto> getOrderTypeById(@PathVariable int id) throws DataNotFoundException {
			WarrantType warrantType = warrantTypeService.getWarrantTypeById(id);
			WarrantTypeDto orderTypeDto = mapper.map(warrantType, WarrantTypeDto.class);
			return ResponseEntity.ok(orderTypeDto);
		}
}
