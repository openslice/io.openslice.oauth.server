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
