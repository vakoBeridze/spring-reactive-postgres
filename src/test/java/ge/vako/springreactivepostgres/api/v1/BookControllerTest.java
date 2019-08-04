package ge.vako.springreactivepostgres.api.v1;

import ge.vako.springreactivepostgres.domain.Book;
import ge.vako.springreactivepostgres.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest
class BookControllerTest {

	private WebTestClient client;
	private List<Book> expectedList;

	@Autowired
	private BookRepository repository;

	@BeforeEach
	void setUp() {
		this.client =
				WebTestClient
						.bindToController(new BookController(repository))
						.configureClient()
						.baseUrl("/v1/books")
						.build();

		this.expectedList = repository.findAll().collectList().block();
	}

	@Test
	void findAll() {
		client
				.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(Book.class)
				.isEqualTo(expectedList);
	}

	@Test
	void testBookNotFound() {
		client
				.get()
				.uri("/-2")
				.exchange()
				.expectStatus()
				.is5xxServerError();
	}

	@Test
	void testBookFound() {
		Book expectedBook = expectedList.get(0);
		client
				.get()
				.uri("/{id}", expectedBook.getId())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Book.class)
				.isEqualTo(expectedBook);
	}
}