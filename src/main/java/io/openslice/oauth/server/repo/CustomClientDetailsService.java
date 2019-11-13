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
package io.openslice.oauth.server.repo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.openslice.oauth.server.config.ClientConfigProperties;
import io.openslice.oauth.server.config.ClientConfigProperties.Client;


@Service
public class CustomClientDetailsService implements ClientDetailsService{

	private static final transient Log logger = LogFactory.getLog( CustomClientDetailsService.class.getName());

	@Autowired
	private ClientConfigProperties ccp;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		
				
//			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//			 try {
//				System.out.println ( mapper.writeValueAsString( ccp ) );
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			

		Client client = ccp.getClients().get( clientId );
		if ( client != null ) {
			logger.info( "client (" + clientId + ") = "  + client.toString());
			BaseClientDetails cd = new  BaseClientDetails();
			cd.setClientId( client.getClientId() );
			cd.setClientSecret( passwordEncoder().encode( client.getTokenSecret() ));
						
			cd.setAutoApproveScopes(client.getScopes() );
			cd.setScope( client.getScopes()  );
			cd.setRegisteredRedirectUri( client.getRedirectUris());
			cd.setAccessTokenValiditySeconds( (int) client.getTokenExpirationsec());
			cd.setAuthorizedGrantTypes( client.getGrantTypes());
			
			return cd;
		}
		
		return null;
	}
	
	 @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
