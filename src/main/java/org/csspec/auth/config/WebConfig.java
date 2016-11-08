package org.csspec.auth.config;

import org.csspec.auth.util.RequestListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {
    @Bean
    public Filter requestListener() {
        return new RequestListener();
    }
}
