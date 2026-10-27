package in.co.bel.olms.master.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.co.bel.olms.master.entity.Account;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
* AccountService service class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   08-06-2023 
*/

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	
	public Account getAccountById(int id) {
		Optional<Account> accountOptional = accountRepository.findById(id);
		if(accountOptional.isPresent()) {
			return accountOptional.get();
		} else {
			throw new DataNotFoundException("Account not found with Id:" + id);
		}
	}
	
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}
}
