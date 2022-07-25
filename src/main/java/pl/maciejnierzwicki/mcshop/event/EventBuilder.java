package pl.maciejnierzwicki.mcshop.event;

import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;
import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.EventMeta;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;

public class EventBuilder {
	
	private EventType type;
	private User actor;
	private User user;
	private Service service;
	private Category category;
	private Order order;
	private ServerConfig server;
	
	public static EventBuilder forType(EventType type) {
		EventBuilder builder = new EventBuilder();
		builder.type = type;
		return builder;
	}
	
	public EventBuilder withActor(User actor) {
		this.actor = actor;
		return this;
	}
	
	public EventBuilder withUser(User user) {
		this.user = user;
		return this;
	}
	
	public EventBuilder withService(Service service) {
		this.service = service;
		return this;
	}
	
	public EventBuilder withCategory(Category category) {
		this.category = category;
		return this;
	}
	
	public EventBuilder withOrder(Order order) {
		this.order = order;
		return this;
	}
	
	public EventBuilder withServer(ServerConfig server) {
		this.server = server;
		return this;
	}
	
	public AppEvent toEvent() {
		AppEvent event = new AppEvent(type);
		EventMeta meta = new EventMeta();
		meta.setActor(actor);
		meta.setUser(user);
		meta.setCategory(category);
		meta.setService(service);
		meta.setOrder(order);
		meta.setServer(server);
		event.setMeta(meta);
		return event;
	}

}
