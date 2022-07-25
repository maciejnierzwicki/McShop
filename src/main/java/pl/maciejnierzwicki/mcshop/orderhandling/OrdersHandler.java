package pl.maciejnierzwicki.mcshop.orderhandling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.OrderValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.utils.MCServerUtils;

@Component
@Slf4j
public class OrdersHandler {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private MainProperties properties;
	@Autowired
	private OrderValidator orderValidator;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@Scheduled(fixedDelay = 1000)
	public void processOrders() {
		Iterable<Order> orders = orderService.getAllWithStatus(OrderStatus.PAID);
		for(Order order : orders) {
			if(!orderValidator.isValidOrder(order)) {
				log.debug("Invalid order with id: " + order.getId() + ", skipping");
				continue;
			}
			
			switch(order.getOrderType()) {
				case SERVICE_ORDER: {
					processService(order);
					break;
				}
				case FUNDS_ORDER: {
					processFunds(order);
					break;
				}
			}
			order.setStatus(OrderStatus.DELIVERED);
			orderService.save(order);
			log.info("Order with id " + order.getId() + " has been delivered.");
		}
	}
	
	private void processService(Order order) {
		Service service = order.getService();
		ServerConfig server = service.getServer();
		List<String> commands = service.getCommands();
		//connect to server and send commands
		MCServerUtils.sendCommands(server, commands, order.getPlayerName(), properties.getPlayerPlaceholder());
		eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_COMPLETE_EVENT).withOrder(order).toEvent());
	}
	
	private void processFunds(Order order) {
		double amount = order.getAmount();
		log.debug("Funds order id " + order.getId() + "; amount of funds to add: " + amount);
		User user = order.getUser();
		user.setMoney(user.getMoney() + amount);
		user = userService.save(user);
		for(Object principal : sessionRegistry.getAllPrincipals()) {
			User loggedUser = (User) principal;
			if(loggedUser.getId() == user.getId()) {
				loggedUser.setMoney(user.getMoney());
			}
		}
		log.info("Updated money for user " + user.getUsername());
		eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_COMPLETE_EVENT).withOrder(order).toEvent());
	}

}
