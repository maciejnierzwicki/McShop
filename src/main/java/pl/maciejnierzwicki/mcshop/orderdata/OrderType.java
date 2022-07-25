package pl.maciejnierzwicki.mcshop.orderdata;

public enum OrderType {
	
	SERVICE_ORDER("Usługa"), FUNDS_ORDER("Doładowanie portfela");
	
	private String displayName;
	
	OrderType(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
}
