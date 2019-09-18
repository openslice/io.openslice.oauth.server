package io.openslice.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

//		.cors().and().csrf().disable()
		
		;
		// @formatter:on
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    source.registerCorsConfiguration("/**", config);
	    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

	    bean.setOrder(0);

	    return bean;

	}
	


}