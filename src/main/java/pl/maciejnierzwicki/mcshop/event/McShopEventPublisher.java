package pl.maciejnierzwicki.mcshop.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;

@Component
public class McShopEventPublisher {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	public void publishEvent(final AppEvent event) {
		publisher.publishEvent(event);
	}
}
