package io.openslice.oauth.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.openslice.oauth.server.repo.CustomDetailsService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomDetailsService customDetailsService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
//	auth.inMemoryAuthentication()
//	  .withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
//	  .withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and()
//	  .withUser("user1").password(passwordEncoder.encode("pass")).roles("USER").and()
//	  .withUser("admin").password(passwordEncoder.encode("nimda")).roles("ADMIN");
    	
    	auth.userDetailsService(customDetailsService).passwordEncoder( passwordEncoder );
    	
    }// @formatter:on

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/oauth/token/revokeById/**").permitAll()
		.antMatchers("/tokens/**").permitAll()
//		.antMatchers("/oauth/token/**").permitAll()
//		.antMatchers("/oauth/token").permitAll()
		.anyRequest().authenticated()
		//.and().formLogin().permitAll()
		.and().csrf().disable()
		//.cors().and().csrf().disable()
		;
		// @formatter:on
	}
	

//	 @Bean
//	    CorsConfigurationSource corsConfigurationSource() {
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration corsConfiguration = new CorsConfiguration();
//	        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//	        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
//	        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//	        corsConfiguration.setAllowCredentials(true);
//	        corsConfiguration.setMaxAge(1800L);
//	        source.registerCorsConfiguration("/**", corsConfiguration); // restrict path here
//	        return source;
//	 }

}