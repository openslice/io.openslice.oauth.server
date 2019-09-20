package io.openslice.oauth.server.controller;


import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class RevokeTokenEndpoint {


	private static final transient Log logger = LogFactory.getLog( RevokeTokenEndpoint.class.getName());
	
    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public void revokeToken( HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            if (tokenServices.revokeToken(tokenId) ) {
            	logger.info("Succesfully revoked tokenId=" + tokenId);
            	
            	new SecurityContextLogoutHandler().logout(request, null, null);
                try {
                    //sending back to client app
                    response.sendRedirect(request.getHeader("referer"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
            	logger.info("Fail to revoke tokenId=" + tokenId);
            	
            }
        }
    }

}