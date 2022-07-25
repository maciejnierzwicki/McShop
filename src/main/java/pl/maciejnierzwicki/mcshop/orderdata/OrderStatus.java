package pl.maciejnierzwicki.mcshop.orderdata;

public enum OrderStatus {
	
	WAITING_PAYMENT("Oczekiwanie na płatność"), PAID("Opłacono"), DELIVERED("Dostarczono"), CANCELLED("Anulowano");
	
	private String displayName;
	
	OrderStatus(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
}
