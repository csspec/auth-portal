package org.csspec.auth.db.repositories;

import org.csspec.auth.db.schema.Code;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CodeRepository extends MongoRepository<Code, String> {
    public Code findCodeByCodeAndClientId(String code, String clientId);
}
