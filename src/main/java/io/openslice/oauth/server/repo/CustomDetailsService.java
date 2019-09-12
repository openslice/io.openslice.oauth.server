package io.openslice.oauth.server.repo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.openslice.model.PortalUser;
import io.openslice.model.UserRoleType;
import io.openslice.oauth.server.model.CustomUser;
import io.openslice.oauth.server.model.UserEntity;

@Service
public class CustomDetailsService implements UserDetailsService {

	@Autowired
	UsersRepository usersRepo;

	@Override
	public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
		UserEntity userEntity = new UserEntity();
		try {
			PortalUser pu = this.usersRepo.findDistinctFirstByUsername(username);
			if ( pu != null){
				userEntity.setUsername(pu.getUsername());
				userEntity.setPassword(pu.getPassword());
				Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
				for (UserRoleType rt : pu.getRoles()) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority( rt.getValue() );
					grantedAuthoritiesList.add(grantedAuthority);
				}
				userEntity.setGrantedAuthoritiesList(grantedAuthoritiesList);				
			} else {
				throw new UsernameNotFoundException("User " + username + " was not found in the database");				
			}

			CustomUser customUser = new CustomUser(userEntity);
			return customUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
	}
}