package pl.maciejnierzwicki.mcshop.orderdata;

public enum PaymentMethod {
	
	ACCOUNT_FUNDS("Mój portfel"), BANK_TRANSFER("Przelew"), SMS("SMS");
	
	private String displayName;
	
	PaymentMethod(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
}
