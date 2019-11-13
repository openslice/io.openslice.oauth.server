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
