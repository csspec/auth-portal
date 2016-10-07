package org.csspec.auth.db.repositories;

import org.csspec.auth.db.schema.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<Account, String> {
    public Account findUserByName(String userName);
    public Account findUserById(String id);
}
