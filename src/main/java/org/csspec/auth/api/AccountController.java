package org.csspec.auth.api;

import com.mongodb.DuplicateKeyException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.exceptions.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private UserAccountRepository repository;

    private RequestApproval requestApproval;

    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AccountController(UserAccountRepository repository, RequestApproval requestApproval) {
        this.repository = repository;
        this.requestApproval = requestApproval;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> getAllAccounts(HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.ADMIN);
        List<Account> list = repository.findAll();
        return list == null ? new ArrayList<>() : list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Account createAccount(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.UNKNOWN);
        String username = map.get("username");
        if (username == null) {
            throw new BadRequestException("username field is required");
        }
        String password = map.get("password");
        if (password == null) {
            throw new BadRequestException("password field is required");
        }
        password = passwordEncoder.encode(password);

        String email = map.get("email");
        if (email == null || !GoogleAccountVerifier.verifiedEmails.contains(email)) {
            throw new BadRequestException("email field is missing or incorrect");
        }
        GoogleAccountVerifier.verifiedEmails.remove(email);
        Account account = new Account(username, email, password);

        // we set the default role of the user to UNKNOWN. Only ADMIN can later change the role from admin console.
        account.setRole(UserRole.UNKNOWN);
        repository.save(account);
        return account;
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String accountId, HttpServletRequest request) throws Exception {
        requestApproval.approveRequest(request, UserRole.ADMIN);
        return repository.findUserById(accountId);
    }

    @RequestMapping(value = "/exists/{username}", method = RequestMethod.GET)
    public Map<String, Boolean> checkExists(@PathVariable String username) {
        Account account = repository.findUserByName(username);
        return Collections.singletonMap("exists", account != null);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(BadRequestException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.toString());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse duplicateKeyExceptionHandler(DuplicateKeyException exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), "email id already registered");
    }
}
