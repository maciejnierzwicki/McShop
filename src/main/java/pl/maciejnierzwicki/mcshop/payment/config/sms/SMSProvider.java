package pl.maciejnierzwicki.mcshop.payment.config.sms;


/***
 * Set of all available SMS providers names.
 *
 */
public enum SMSProvider {
	MICROSMS("MicroSMS");//, HOTPAY("HotPay");
	
	private String name;

	SMSProvider(String name) {
		this.name = name;
	}
		
	public String getName() {
		return name;
	}
}

