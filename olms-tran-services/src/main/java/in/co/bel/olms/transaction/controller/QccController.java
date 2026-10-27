package in.co.bel.olms.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.transaction.dto.QccDto;
import in.co.bel.olms.transaction.service.QccService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/qcc")
@RequiredArgsConstructor
public class QccController {
	
	private final QccService qccService;
	
	private final HmisAppSecurity hmisAppSecurity = new HmisAppSecurity();

	@PostMapping("/save")
	public ResponseEntity<String> saveQcc(@RequestBody QccDto qccDto, @RequestHeader(name = "Authorization") String token){
		try {
			Integer empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);
			String response = qccService.saveQcc(qccDto, empId, roleId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasAnyAuthority('DO', 'DOA')")
	@PostMapping("/{qccId}/approve")
	public ResponseEntity<String> approveQcc(@PathVariable(name = "qccId") Integer qccId, @RequestHeader(name = "Authorization") String token){
		try {
			Integer empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);
			String response = qccService.approveQcc(qccId, empId, roleId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@GetMapping("/{mergedOlId}")
	public ResponseEntity<Object> getQccByMergedOlId(@PathVariable Integer mergedOlId, @RequestHeader(name = "Authorization") String token,
			@RequestParam(name = "isOl", defaultValue = "false") Boolean isOl){
		try {
			Integer empId = hmisAppSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = hmisAppSecurity.getRoleIdFromJwtToken(token);
			QccDto qccDto = qccService.getQcc(mergedOlId, empId, roleId, isOl);
			return ResponseEntity.ok(qccDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
}
