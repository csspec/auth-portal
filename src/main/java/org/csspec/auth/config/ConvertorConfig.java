package org.csspec.auth.config;

import org.csspec.auth.serializers.AccountSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.csspec.auth.serializers")
public class ConvertorConfig {
    @Bean
    AccountSerializer accountSerializer() {
        return new AccountSerializer();
    }
}
