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
        /*
        generates a five digit code and encodes it into base64
         */
        return UUIDGenerator.toBase64(UUIDGenerator.getRandomUUID()).substring(0, 5);
    }

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

    /**
     * remove the code
     * @param codeStr issued code string
     */
    public void removeIfValid(String codeStr, ClientApplication application) {
        repository.delete(repository.findCodeByCodeAndClientId(codeStr, application.getClientId()));
    }
}
