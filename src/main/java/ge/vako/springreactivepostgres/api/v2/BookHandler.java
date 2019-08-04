package ge.vako.springreactivepostgres.api.v2;

import ge.vako.springreactivepostgres.domain.Book;
import ge.vako.springreactivepostgres.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class BookHandler {

	private final BookRepository repository;


	public BookHandler(BookRepository repository) {
		this.repository = repository;
	}


	public Mono<ServerResponse> getAllBooks(ServerRequest request) {
		Flux<Book> books = repository.findAll();

		return ServerResponse.ok()
				.contentType(APPLICATION_JSON)
				.body(books, Book.class);
	}

	public Mono<ServerResponse> getBook(ServerRequest request) {
		Long id = Long.parseLong(request.pathVariable("id"));
		Mono<Book> bookMono = this.repository.findById(id);

		return bookMono
				.flatMap(book ->
						ServerResponse.ok()
								.contentType(APPLICATION_JSON)
								.body(fromObject(book))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> saveBook(ServerRequest request) {
		Mono<Book> bookMono = request.bodyToMono(Book.class);

		return bookMono.flatMap(book ->
				ServerResponse.status(HttpStatus.CREATED)
						.contentType(APPLICATION_JSON)
						.body(repository.save(book), Book.class)
		);
	}

	public Mono<ServerResponse> updateBook(ServerRequest request) {
		Long id = Long.parseLong(request.pathVariable("id"));

		Mono<Book> existingBookMono = this.repository.findById(id);
		Mono<Book> bookToSaveMono = request.bodyToMono(Book.class);

		return bookToSaveMono.zipWith(
				existingBookMono,
				(bookToSave, existingBook) -> {
					bookToSave.setId(existingBook.getId());
					return bookToSave;
				})
				.flatMap(book ->
						ServerResponse.ok()
								.contentType(APPLICATION_JSON)
								.body(repository.save(book), Book.class)
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteBook(ServerRequest request) {
		Long id = Long.parseLong(request.pathVariable("id"));

		Mono<Book> bookMono = this.repository.findById(id);

		return bookMono
				.flatMap(existingBook ->
						ServerResponse.ok()
								.build(repository.delete(existingBook))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteAllBooks(ServerRequest request) {
		return ServerResponse.ok()
				.build(repository.deleteAll());
	}

}
