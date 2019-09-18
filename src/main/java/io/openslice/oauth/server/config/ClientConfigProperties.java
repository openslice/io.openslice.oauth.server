package io.openslice.oauth.server.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@ConfigurationProperties(prefix = "authclients")
public class ClientConfigProperties {
	
//	private static class Authclients {
//		ClientConfigProperties authclients = new ClientConfigProperties();
//
//		/**
//		 * @return the authclients
//		 */
//		public ClientConfigProperties getAuthclients() {
//			return authclients;
//		}
//
//		/**
//		 * @param authclients the authclients to set
//		 */
//		public void setAuthclients(ClientConfigProperties authclients) {
//			this.authclients = authclients;
//		}
//		
//		
//	}
//	
//	public static void main(String[] args) throws JsonProcessingException  {
//		
//		
//		Authclients ac = new Authclients();
//		ClientConfigProperties cp = ac.getAuthclients();
//		
//		Client e = new Client();
//		e.setClientId("osapiWebClientId");
//		e.setTokenSecret( "secret" );
//		e.setTokenExpirationsec(3600);
//		e.getScopes().add( "openapi" );
//		e.getScopes().add( "admin");
//		e.getScopes().add( "read");
//		e.getScopes().add( "write" );
//
//		e.getGrantTypes().add( "authorization_code" );
//		e.getGrantTypes().add( "password" );
//		e.getGrantTypes().add( "refresh_token" );
//		e.getGrantTypes().add( "client_credentials" ); 
//
//		e.getRedirectUris().add("http://localhost:13000/osapi/testweb/oauthresp.html" );
//		e.getRedirectUris().add( "http://localhost:13000/osapi/webjars/springfox-swagger-ui/oauth2-redirect.html" );
//		cp.getClients().put( e.getClientId() , e);		
//
//		e.setClientId("osapiWebClientId2");
//		cp.getClients().put( e.getClientId() , e); 
//		
//		cp.getAllowOrigins().add("http://origon.org");
//		 ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//		 System.out.println ( mapper.writeValueAsString( ac ) );
//		 
//	}
	
	

	private final Map<String, Client> clients = new HashMap<>();
	private final List<String> allowOrigins = new ArrayList<>();
	
	
	/**
	 * @return the clients
	 */
	public Map<String, Client> getClients() {
		return clients;
	}

	

	/**
	 * @return the allowOrigins
	 */
	public List<String> getAllowOrigins() {
		return allowOrigins;
	}



	public static class Client {
		
		private String clientId;
		private String tokenSecret;
		private String allowOrigin;
		private long tokenExpirationsec;
		private List<String> scopes = new ArrayList<>();
		private List<String> grantTypes = new ArrayList<>();
		private Set<String> redirectUris = new HashSet<>();
		/**
		 * @return the clientId
		 */
		public String getClientId() {
			return clientId;
		}
		/**
		 * @param clientId the clientId to set
		 */
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		/**
		 * @return the tokenSecret
		 */
		public String getTokenSecret() {
			return tokenSecret;
		}
		/**
		 * @param tokenSecret the tokenSecret to set
		 */
		public void setTokenSecret(String tokenSecret) {
			this.tokenSecret = tokenSecret;
		}
		/**
		 * @return the tokenExpirationsec
		 */
		public long getTokenExpirationsec() {
			return tokenExpirationsec;
		}
		/**
		 * @param tokenExpirationsec the tokenExpirationsec to set
		 */
		public void setTokenExpirationsec(long tokenExpirationsec) {
			this.tokenExpirationsec = tokenExpirationsec;
		}
		/**
		 * @return the scopes
		 */
		public List<String> getScopes() {
			return scopes;
		}
		/**
		 * @param scopes the scopes to set
		 */
		public void setScopes(List<String> scopes) {
			this.scopes = scopes;
		}
		/**
		 * @return the grantTypes
		 */
		public List<String> getGrantTypes() {
			return grantTypes;
		}
		/**
		 * @param grantTypes the grantTypes to set
		 */
		public void setGrantTypes(List<String> grantTypes) {
			this.grantTypes = grantTypes;
		}
		/**
		 * @return the redirectUris
		 */
		public Set<String> getRedirectUris() {
			return redirectUris;
		}
		/**
		 * @param redirectUris the redirectUris to set
		 */
		public void setRedirectUris(Set<String> redirectUris) {
			this.redirectUris = redirectUris;
		}
		/**
		 * @return the allowOrigin
		 */
		public String getAllowOrigin() {
			return allowOrigin;
		}
		/**
		 * @param allowOrigin the allowOrigin to set
		 */
		public void setAllowOrigin(String allowOrigin) {
			this.allowOrigin = allowOrigin;
		}
		
		
		
		
	}
	
	

}
