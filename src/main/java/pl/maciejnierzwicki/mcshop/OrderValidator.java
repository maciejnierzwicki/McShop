/**
 * 
 */
package pl.maciejnierzwicki.mcshop;

import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.dbentity.Order;

@Component
public class OrderValidator {
	public boolean isValidOrder(Order order) {
		return order != null && order.getOrderType() != null;
	}

}
