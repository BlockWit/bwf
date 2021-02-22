package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.rest.AccountRestDTO;
import com.blockwit.bwf.model.rest.ListResponse;
import com.blockwit.bwf.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class Accounts {

	AccountRepository accountRepository;

	public Accounts(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ListResponse<AccountRestDTO> accounts(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
																							 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
		Page<Account> page = accountRepository.findAll(pageRequest);
		List<AccountRestDTO> list = page.getContent().stream()
			.map(t -> new AccountRestDTO(t.getId(), t.getLogin(), t.getEmail(), t.getConfirmationStatus().name()))
			.collect(Collectors.toList());
		return new ListResponse<>(list);
	}

}
