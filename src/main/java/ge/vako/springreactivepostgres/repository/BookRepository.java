package ge.vako.springreactivepostgres.repository;

import ge.vako.springreactivepostgres.domain.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Configuration
@Repository
public interface BookRepository extends R2dbcRepository<Book, Long> {

	@Query("SELECT * FROM books WHERE title = $1")
	Flux<Book> findAllByTitle(String title);
}
