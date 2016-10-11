package org.csspec.auth.api;

import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private UserAccountRepository repository;

    @Autowired
    AccountController(UserAccountRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Account createAccount(@RequestBody Account account) {
        repository.save(account);
        return account;
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String accountId) {
        return repository.findUserById(accountId);
    }
}
