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

import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import io.openslice.model.PortalUser;

/**
 * 
 * @author ctranoris
 * @see
 * https://www.amitph.com/spring-data-jpa-query-methods/, 
 * https://www.amitph.com/pagination-sorting-spring-data-jpa/, 
 * https://www.baeldung.com/spring-data-jpa-query,
 * for usefull methods on spring repository
 * 
 * 
 */
@Repository
public interface UsersRepository  extends PagingAndSortingRepository<PortalUser, Long> {
	

	@Query( value = "SELECT m FROM PortalUser m JOIN FETCH m.roles r WHERE m.username=?1" ) //
	PortalUser findDistinctFirstByUsername( String username );
	

}
