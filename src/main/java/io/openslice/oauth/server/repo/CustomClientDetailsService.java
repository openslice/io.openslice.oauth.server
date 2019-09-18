package io.openslice.oauth.server.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import io.openslice.oauth.server.config.ClientConfigProperties;
import io.openslice.oauth.server.config.ClientConfigProperties.Client;


@Service
public class CustomClientDetailsService implements ClientDetailsService{


	@Autowired
	private ClientConfigProperties ccp;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		
		
		Client client = ccp.getClients().get( clientId );
		if ( client != null ) {
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
