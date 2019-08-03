package ge.vako.springreactivepostgres.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

	@Value("${spring.data.postgres.host}")
	private String host;

	@Value("${spring.data.postgres.port}")
	private Integer port;

	@Value("${spring.data.postgres.database}")
	private String database;

	@Value("${spring.data.postgres.username}")
	private String username;

	@Value("${spring.data.postgres.password}")
	private String password;

	@Override
	public ConnectionFactory connectionFactory() {

		return new PostgresqlConnectionFactory(
				PostgresqlConnectionConfiguration.builder()
						.host(host)
						.port(port)
						.username(username)
						.password(password)
						.database(database)
						.build()
		);
	}
}
