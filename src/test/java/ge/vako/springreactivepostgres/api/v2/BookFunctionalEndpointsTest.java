package ge.vako.springreactivepostgres.api.v2;

import ge.vako.springreactivepostgres.domain.Book;
import ge.vako.springreactivepostgres.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;

@SpringBootTest
class BookFunctionalEndpointsTest {

	private WebTestClient client;
	private List<Book> expectedList;

	@Autowired
	private BookRepository repository;

	@Autowired
	private RouterFunction routerFunction;

	@BeforeEach
	void setUp() {
		this.client =
				WebTestClient
						.bindToRouterFunction(routerFunction)
						.configureClient()
						.baseUrl("/v2/books")
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
				.isNotFound();
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