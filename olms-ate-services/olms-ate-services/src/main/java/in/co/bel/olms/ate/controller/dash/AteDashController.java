package in.co.bel.olms.ate.controller.dash;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.Dash.GraphSpecDto;
import in.co.bel.olms.ate.service.AteDashService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dash")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AteDashController {

	private final AteDashService ateDashService;
			
	@GetMapping("/getGraphSpecs/{productId}/{testId}")
	public GraphSpecDto getGraphSpecs(@PathVariable Integer productId,@PathVariable Integer testId) throws DataNotFoundException {
		GraphSpecDto dto = ateDashService.getGraphSpecs(productId, testId, 0);
		return dto;
	}
		

}
