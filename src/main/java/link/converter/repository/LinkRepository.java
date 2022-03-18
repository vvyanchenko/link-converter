package link.converter.repository;

import link.converter.domain.entity.UrlToDeeplink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository required to perform CRUD operation on the database with links of different types
 */
public interface LinkRepository extends JpaRepository<UrlToDeeplink, Long> {

    UrlToDeeplink findByUrl(String url);

    UrlToDeeplink findByDeeplink(String deeplink);

}
