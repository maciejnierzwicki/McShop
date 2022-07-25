package pl.maciejnierzwicki.mcshop;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class McShopInitializer {
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
