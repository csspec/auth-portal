package org.csspec.auth.api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private UserAccountRepository repository;

    private RequestApproval requestApproval;

    @Autowired
    AccountController(UserAccountRepository repository, RequestApproval requestApproval) {
        this.repository = repository;
        this.requestApproval = requestApproval;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> getAllAccounts(HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.TEACHER);
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Account createAccount(@RequestBody Account account, HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.ADMIN);
        repository.save(account);
        return account;
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String accountId, HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.ADMIN);
        return repository.findUserById(accountId);
    }

}
