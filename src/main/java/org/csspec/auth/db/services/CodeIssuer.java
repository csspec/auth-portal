package org.csspec.auth.db.services;

import org.csspec.auth.config.UUIDGenerator;
import org.csspec.auth.db.repositories.CodeRepository;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.schema.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CodeIssuer {
    private CodeRepository repository;

    @Autowired
    CodeIssuer(CodeRepository repository) {
        this.repository = repository;
    }

    private String generateCode(ClientApplication application, List<String> services) {
        return UUIDGenerator.toBase64(UUIDGenerator.getRandomUUID()).substring(0, 5);
    }

    /* TODO: Instead of storing account id we should use better way like storing the services which the client was requesting
     */
    public String issue(ClientApplication application, List<String> services) {
        String code = generateCode(application, services);
        repository.save(new Code(application.getClientId(), services, code));
        return code;
    }

    public boolean isValid(String codeStr, ClientApplication application) {
        long currentTime = System.currentTimeMillis();
        Code code = repository.findCodeByCodeAndClientId(codeStr, application.getClientId());
        return code != null;
    }

}
