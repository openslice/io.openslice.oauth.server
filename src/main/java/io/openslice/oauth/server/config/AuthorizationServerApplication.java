package io.openslice.oauth.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * based on https://www.baeldung.com/rest-api-spring-oauth2-angular 
 * @author ctranoris
 *
 */
@SpringBootApplication(scanBasePackages = "io.openslice")
public class AuthorizationServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}