package pl.maciejnierzwicki.mcshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import pl.maciejnierzwicki.mcshop.dbentity.Order;

@Configuration
public class SessionOrderBeanConfig {
	
	@Bean
	@Scope(
	  value = WebApplicationContext.SCOPE_SESSION, 
	  proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Order order() {
		return new Order();
	}

}
