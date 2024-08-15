package org.oleh;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;

@SpringBootApplication
@AutoConfigureWireMock
public class Runner {

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Bean
    public Options wireMockOptions(@Value("${wiremock.server.port}") int port, Environment env) throws IOException {
        final WireMockConfiguration options = WireMockSpring.options();
        if(env.matchesProfiles("prod")) {
            options.disableRequestJournal();
        }
        options.containerThreads(64);
        options.port(port);
        return options;
    }
}