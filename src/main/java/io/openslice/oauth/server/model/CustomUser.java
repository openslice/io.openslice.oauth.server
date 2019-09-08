package io.openslice.oauth.server.model;

import org.springframework.security.core.userdetails.User;

/**
 * @author ctranoris
 *
 */
public class CustomUser extends User {
   private static final long serialVersionUID = 1L;
   public CustomUser(UserEntity user) {
      super(user.getUsername(), user.getPassword(), user.getGrantedAuthoritiesList());
   }
} 