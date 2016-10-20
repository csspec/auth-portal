package org.csspec.auth;

import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.schema.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthPortal implements CommandLineRunner {
    @Autowired
    private ClientApplicationRepository repository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public static void main(String []args) {
        SpringApplication.run(AuthPortal.class, args);
    }

    public void run(String ...args) {
        userAccountRepository.deleteAll();
        repository.deleteAll();

        Configuration configuration = new Configuration();

        String adminAccountName = configuration.getAdminAccountId();
        String adminPassword = configuration.getAdminPassword();
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
        application.setClientSecret("simple");
        repository.save(application);
    }
}
