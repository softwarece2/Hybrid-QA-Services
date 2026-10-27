package in.co.bel.olms.dash.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.InpectorDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dash")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InpectorDashboardController {
	
	@GetMapping("/so")
	public InpectorDto getOlByInspId() {	
			InpectorDto dto = new InpectorDto();
			dto.setCompleted(2);
			dto.setInProgress(3);
			dto.setPending(1);
			dto.setTotal(6);
			return dto;
	}
}
