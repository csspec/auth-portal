package org.csspec.auth.db.services;

import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthUserService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    public AuthUserService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Account loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = userAccountRepository.findUserByName(userName);

        // if there was no account named account then throw exception
        if (account == null) {
            throw new UsernameNotFoundException(userName);
        }

        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_SPLITTER");

        account.setGrantedAuthorities(authorities);
        return account;
    }
}
