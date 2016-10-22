package org.csspec.auth.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.csspec.auth.db.schema.Account;

import java.io.IOException;

public class AccountSerializer extends StdSerializer<Account> {

    public AccountSerializer() {
        this(null);
    }

    public AccountSerializer(Class<Account> account) {
        super(account);
    }

    @Override
    public void serialize(Account value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("userid", value.getId());
        jgen.writeStringField("username", value.getUsername());
        jgen.writeStringField("role", value.getRole().getValue());
        jgen.writeStringField("email", value.getEmail());
        jgen.writeEndObject();
    }
}
