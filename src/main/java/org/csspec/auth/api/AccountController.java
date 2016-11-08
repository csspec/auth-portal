package org.csspec.auth.api;

import com.mongodb.DuplicateKeyException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.exceptions.ErrorResponse;
import org.csspec.auth.exceptions.UpdateNotAllowedException;
import org.csspec.auth.exceptions.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    private MongoOperations mongo;

    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AccountController(UserAccountRepository repository, RequestApproval requestApproval, MongoOperations mongo) {
        this.repository = repository;
        this.requestApproval = requestApproval;
        this.mongo = mongo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> getAllAccounts(HttpServletRequest request) throws Exception {
//        requestApproval.approveRequest(request, UserRole.ADMIN);
        List<Account> list = repository.findAll();
        return list == null ? new ArrayList<>() : list;
    }

    private void setAccountRole(String role, Account account) {
        switch (role) {
            case "STUDENT":
                account.setRole(UserRole.STUDENT);
                break;

            case "TEACHER":
                account.setRole(UserRole.TEACHER);
                break;

            case "ADMIN":
                account.setRole(UserRole.ADMIN);
                break;

            case "DAA":
                account.setRole(UserRole.DAA);
                break;

            case "CR":
                account.setRole(UserRole.CR);
                break;

            case "SR":
                account.setRole(UserRole.SR);
                break;

            case "HS":
                account.setRole(UserRole.HS);

            default:
                account.setRole(UserRole.UNKNOWN);
                break;
        }

    }

    private Account createPersistentAccount(Map<String, String> map) throws Exception {
        String username = map.get("username");

        String password = map.get("password");

        if (password != null)
            password = passwordEncoder.encode(password);
        String email = map.get("email");
        String role = map.get("role");

        return new Account(username, email, password);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Account createAccount(@RequestBody Map<String, String> map) throws Exception {
        Account account = createPersistentAccount(map);
        setAccountRole(map.get("role") == null ? "UNKNOWN" : map.get("role"), account);

        if (account.getUsername() == null) {
            throw new BadRequestException("username field is required");
        }

        if (account.getPassword() == null) {
            throw new BadRequestException("password field is required");
        }

        repository.save(account);
        return account;
    }

    private Update getUpdate(Account old, Account change, Account updater) throws UpdateNotAllowedException {
        Update update = new Update();

        if (change.getRole() != null && !old.getRole().equals(change.getRole())) {
            if (!updater.getRole().equals(UserRole.ADMIN))
                throw new UpdateNotAllowedException(updater.getUsername(), updater.getRole(), UserRole.ADMIN);
            update.set("role", change.getRole());
        }

        if (change.getEmail() != null && (old.getEmail() == null || !old.getEmail().equals(change.getEmail()))) {
            if (updater.getId().equals(old.getId())) {
                update.set("email", change.getEmail());
            } else if (updater.getRole().equals(UserRole.ADMIN)) {
                update.set("email", change.getEmail());
            } else {
                throw new UpdateNotAllowedException(updater.getUsername(), updater.getRole(), UserRole.ADMIN);
            }
        }

        if (change.getPassword() != null && !old.getPassword().equals(change.getPassword())) {
            System.out.println("Updating password to: " + change.getPassword());
            String password = change.getPassword();
            if (updater.getId().equals(old.getId())) {
                update.set("password", password);
            } else if (updater.getRole().equals(UserRole.ADMIN)) {
                update.set("password", password);
            } else {
                throw new UpdateNotAllowedException(updater.getUsername(), updater.getRole(), UserRole.ADMIN);
            }
        }
        return update;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Map putChanges(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {
        Account changing = requestApproval.approveRequest(request);
        Account account = createPersistentAccount(map);

        String userId = map.get("userid");
        if (userId == null) {
            throw new BadRequestException("userid was not specified");
        }
        account.setId(userId);
        setAccountRole(map.get("role") != null ? map.get("role") : changing.getRole().getValue(), account);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(account.getId()));

        Account oldAccount = mongo.findOne(query, Account.class);
        if (oldAccount == null)
            throw new UsernameNotFoundException(account.getId());

        Update update = getUpdate(oldAccount, account, changing);

        if (mongo.updateFirst(query, update, Account.class) == null)
            throw new InternalError("Unable to update the document");
        return Collections.singletonMap("status", "updated");
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

    @ExceptionHandler(UpdateNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse updateNotAllowedExceptionHandler(UpdateNotAllowedException exception) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), exception.toString());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse usernameNotFoundExceptionHandler(UsernameNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.toString());
    }
}
