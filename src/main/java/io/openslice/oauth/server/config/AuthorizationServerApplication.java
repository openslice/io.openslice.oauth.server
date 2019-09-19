package io.openslice.oauth.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * based on
 * https://www.baeldung.com/rest-api-spring-oauth2-angular 
 * https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm
 * https://www.baeldung.com/spring-security-5-oauth2-login
 * https://developer.okta.com/blog/2019/05/15/spring-boot-login-options
 * @author ctranoris
 *
 */
@EntityScan("io.openslice.model")
@SpringBootApplication(scanBasePackages = "io.openslice")
@EnableAuthorizationServer
@EnableJpaRepositories("io.openslice.oauth.server.repo") 
@EnableConfigurationProperties( ClientConfigProperties.class )
public class AuthorizationServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
    
  
}