package org.csspec.auth;

import org.csspec.auth.api.AccountController;
import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.schema.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthPortal implements CommandLineRunner {
    private ClientApplicationRepository repository;

    private UserAccountRepository userAccountRepository;

    @Autowired
    public AuthPortal(ClientApplicationRepository repository, UserAccountRepository userAccountRepository) {
        this.repository = repository;
        this.userAccountRepository = userAccountRepository;
    }

    public static void main(String []args) {
        SpringApplication.run(AuthPortal.class, args);
    }

    public void run(String ...args) {
        Configuration configuration = new Configuration();

        String adminAccountName = configuration.getAdminAccountId();
        String adminPassword = configuration.getAdminPassword();
        adminPassword = AccountController.passwordEncoder.encode(adminPassword);
        String adminEmail = configuration.getAccountEmail();

        Account admin = new Account(adminAccountName, adminEmail, adminPassword);
        admin.setRole(UserRole.ADMIN);
        userAccountRepository.save(admin);

        ClientApplication application = new ClientApplication();
        application.setClientId(configuration.getThisClientId());
        application.setClientSecret(configuration.getThisClientSecret());
        repository.save(application);

        // create a feedback application client
        application = new ClientApplication();
        application.setClientId("feedback");
        application.setClientSecret("secret");
        repository.save(application);
    }
}
