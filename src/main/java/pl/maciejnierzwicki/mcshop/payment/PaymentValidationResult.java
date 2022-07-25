package pl.maciejnierzwicki.mcshop.payment;

import pl.maciejnierzwicki.mcshop.dbentity.Order;

public class PaymentValidationResult {
	
	private Order order = null;
	private boolean paid = false;
	
	public PaymentValidationResult() {}
	
	public PaymentValidationResult(Order order, boolean paid) {
		this.order = order;
		this.paid = paid;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public boolean isPaid() {
		return paid;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

}
