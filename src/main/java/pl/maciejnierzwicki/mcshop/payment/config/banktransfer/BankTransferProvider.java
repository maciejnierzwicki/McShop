package pl.maciejnierzwicki.mcshop.payment.config.banktransfer;

/***
 * Set of all available bank transfer providers names.
 *
 */
public enum BankTransferProvider {
	DOTPAY("DotPay");//, HOTPAY("HotPay");
	
	private String name;

	BankTransferProvider(String name) {
		this.name = name;
	}
		
	public String getName() {
		return name;
	}
}

