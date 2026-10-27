package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.entity.Account;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.AccountService;
import lombok.RequiredArgsConstructor;

/**
* AccountController class for account APIs
*
* @author  Awadhesh Verma
* @version 1.0
* @since   08-06-2023 
*/

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<KeyValuePair> getAccountById(@PathVariable int id) throws DataNotFoundException {
		
		Account account = accountService.getAccountById(id);
		KeyValuePair keyValuePair = new KeyValuePair();
		keyValuePair.setId(account.getId());
		keyValuePair.setName(account.getAcctType());
		return ResponseEntity.ok(keyValuePair);
	}
	
	/**
	 * 
	 * @return
	 */
	
	@GetMapping("/getAll")
	public ResponseEntity<List<KeyValuePair>> getAllEqptGroup() {
		
		List<KeyValuePair> accountList = new ArrayList<>();
		List<Account> accounts = accountService.getAllAccounts();
		for(Account account : accounts) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId(account.getId());
			keyValuePair.setName(account.getAcctType());
			accountList.add(keyValuePair);
		}
		return ResponseEntity.ok(accountList);
	}

}
