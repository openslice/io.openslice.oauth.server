/*-
 * ========================LICENSE_START=================================
 * io.openslice.oauth.server
 * %%
 * Copyright (C) 2019 openslice.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package io.openslice.oauth.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.openslice.oauth.server.repo.CustomDetailsService;

@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)

@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomDetailsService customDetailsService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Autowired
	private ClientConfigProperties ccp;

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

    public void configure( WebSecurity web ) throws Exception {
        web
        .ignoring()
        .antMatchers( HttpMethod.OPTIONS, "/**" )
        .antMatchers( HttpMethod.DELETE, "/**" );
    }

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/oauth/token/revokeById/**").permitAll()
		.antMatchers("/tokens/**").permitAll()
		.antMatchers("/oauth/**").permitAll()
		.antMatchers("/oauth/token/**").permitAll()
		.antMatchers("/oauth/token").permitAll()
		.antMatchers("/actuator/**").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().permitAll()
		.and().csrf().disable()
		.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()

//		.cors().and().csrf().disable()
		
		;
		// @formatter:on
	}
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FilterRegistrationBean corsFilter() {

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.applyPermitDefaultValues();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("content-length"));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
	    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

	    bean.setOrder(0);

	    return bean;

	}
	


}
