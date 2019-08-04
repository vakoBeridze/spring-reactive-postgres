package ge.vako.springreactivepostgres.api.v2;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Component
public class BookFunctionalEndpoints {

	@Bean
	RouterFunction<ServerResponse> routes(BookHandler handler) {

		return nest(path("/v2/books"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
						route(
								GET("/"), handler::getAllBooks)
								.andRoute(method(HttpMethod.POST), handler::saveBook)
								.andRoute(method(HttpMethod.DELETE), handler::deleteAllBooks)
								.andNest(
										path("/{id}"),
										route(method(HttpMethod.GET), handler::getBook)
												.andRoute(method(HttpMethod.PUT), handler::updateBook)
												.andRoute(method(HttpMethod.DELETE), handler::deleteBook)
								)
				)
		);
	}
}
