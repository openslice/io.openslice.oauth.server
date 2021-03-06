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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import io.openslice.oauth.server.repo.CustomClientDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfigJwt extends AuthorizationServerConfigurerAdapter {


	@Value("${oauthsign.key}")
    private String SIGNING_KEY = null;


	@Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    

	@Autowired
	private CustomClientDetailsService customDetailsService;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
    	clients.withClientDetails( customDetailsService );
    	
//        clients.inMemory()
//                
//                .withClient("osapiWebClientId") // Authorization=Basic b3NhcGlXZWJDbGllbnRJZDpzZWNyZXQ=
//                .secret(passwordEncoder().encode("secret")) //Content-Type=application/x-www-form-urlencoded
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")//grant_type=password
//                .scopes("openapi","admin", "read", "write")
//                .autoApprove( true )
//                .accessTokenValiditySeconds(3600)       // 1 hour
//                .refreshTokenValiditySeconds(2592000)  // 30 days
//                .redirectUris("http://www.example.com",
//                		"http://localhost:13000/osapi/testweb/oauthresp.html",
//                		"http://localhost:8080/login/oauth2/code/custom", 
//                		"http://localhost:13000/osapi/webjars/springfox-swagger-ui/oauth2-redirect.html")
//                ;
    } // @formatter:on

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();        
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey( SIGNING_KEY);
        // final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"), "mypass".toCharArray());
        // converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

}
