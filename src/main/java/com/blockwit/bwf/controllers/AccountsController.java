package com.blockwit.bwf.controllers;

import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.entity.AppContext;
import com.blockwit.bwf.models.entity.Option;
import com.blockwit.bwf.models.repository.AccountRepository;
import com.blockwit.bwf.models.repository.OptionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
public class AccountsController {

    private final AccountRepository accountRepository;

    public AccountsController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/panel/accounts")
    public ModelAndView appPanelAccounts() {
        return new ModelAndView("redirect:/panel/accounts/page/1");
    }

    @GetMapping("/panel/accounts/page/{pageNumber}")
    public ModelAndView appPanelAccountsPage(@PathVariable("pageNumber") int pageNumber) {
        ModelAndView modelAndView = new ModelAndView("panel/accounts");

        Pageable pageRequest =
                PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
        Page<Account> page = accountRepository.findAll(pageRequest);
        int totalPages = page.getTotalPages();

        if (pageNumber > 1)
            modelAndView.addObject("prevPageUrl", pageNumber - 1);
        if (pageNumber < totalPages)
            modelAndView.addObject("nextPageUrl", pageNumber + 1);

        modelAndView.addObject("pageContent", page.getContent());
        modelAndView.addObject("title", "Main page");

        return modelAndView;
    }

}
