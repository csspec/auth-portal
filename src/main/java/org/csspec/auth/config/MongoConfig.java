package org.csspec.auth.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.csspec.auth")
public class MongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "AuthPortalDB";
    }

    @Override
    public Mongo mongo() {
        return new MongoClient("localhost", 27017);
    }
}
