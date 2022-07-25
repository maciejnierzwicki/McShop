package pl.maciejnierzwicki.mcshop.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.maciejnierzwicki.mcshop.properties.DatabaseProperties;

@Configuration
public class DataSourceConfigurer {
	
	@Bean
	public DataSource dataSource(@Autowired DatabaseProperties properties) {
		DataSourceBuilder<?> builder = DataSourceBuilder.create();
		
		switch(properties.getDatabaseType()) {
			case MYSQL: {
				builder.username(properties.getDatabaseUsername());
				builder.password(properties.getDatabasePassword());
				builder.driverClassName("com.mysql.jdbc.Driver");
				builder.url("jdbc:mysql://" + properties.getDatabaseAddress() + ":" + properties.getDatabasePort() + "/" + properties.getDatabaseName());
				break;
			}
			case H2:
			default: {
				builder.username("Admin");
				builder.password("1234");
				builder.driverClassName("org.h2.Driver");
				builder.url("jdbc:h2:file:./" + properties.getDatabaseName() + ";DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
				break;
			}
		}
		return builder.build();
	}
}
