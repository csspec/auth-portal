package org.csspec.auth.db.services;

import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.exceptions.BadClientException;
import org.csspec.auth.exceptions.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientApplicationDetailService {
    private ClientApplicationRepository repository;

    @Autowired
    public ClientApplicationDetailService(ClientApplicationRepository repository) {
        this.repository = repository;
    }

    public ClientApplication loadClientByClientId(String id) throws ClientNotFoundException {
        System.out.println("Looking for " + id);
        ClientApplication client = repository.findClientApplicationByClientId(id);
        if (client == null) {
            System.out.println();
            throw new ClientNotFoundException("'" + id + "' not found");
        }
        return client;
    }

    public ClientApplication loadClientByIdAndSecret(String id, String secret)
            throws BadClientException, ClientNotFoundException {
        ClientApplication client = loadClientByClientId(id);
        if (client.getClientSecret().equals(secret))
            return client;
        throw new BadClientException("'" + id + "' bad credentials");
    }
}
