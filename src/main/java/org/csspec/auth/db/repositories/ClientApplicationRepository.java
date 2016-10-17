package org.csspec.auth.db.repositories;

import org.csspec.auth.db.schema.ClientApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ClientApplicationRepository extends MongoRepository<ClientApplication, String> {
    public ClientApplication findClientApplicationByClientId(String clientId);
    public ClientApplication findClientApplicationByName(String name);
}
