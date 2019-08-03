package ge.vako.springreactivepostgres.api;

import ge.vako.springreactivepostgres.domain.Book;
import ge.vako.springreactivepostgres.repository.BookRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("books")
public class BookController {

	private final BookRepository repository;

	public BookController(BookRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public Flux<Book> findByTitle(@RequestParam(required = false) String title) {
		if (title == null)
			return repository.findAll();
		return repository.findAllByTitle(title);
	}

	@GetMapping("{id}")
	public Mono<Book> getBook(@PathVariable Long id) {
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException("book with id " + id + " not found")));
	}

	@PostMapping
	public Mono<Book> addBook(@RequestBody Book book) {
		return repository.save(book);
	}

	@PutMapping("{id}")
	public Mono<Book> editBook(@PathVariable Long id, @RequestBody Book bookToSave) {
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException("book with id " + id + " not found")))
				.flatMap(book -> {
					bookToSave.setId(book.getId());
					return repository.save(bookToSave);
				});
	}

	@DeleteMapping("{id}")
	public Mono<Void> deleteBook(@PathVariable Long id) {
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new RuntimeException("book with id " + id + " not found")))
				.flatMap(repository::delete);
	}

	@DeleteMapping
	public Mono<Void> deleteBooks() {
		return repository.deleteAll();
	}
}
