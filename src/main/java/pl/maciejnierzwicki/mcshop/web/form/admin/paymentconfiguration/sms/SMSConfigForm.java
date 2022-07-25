package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;

@Data
public abstract class SMSConfigForm {
	
	public abstract SMSConfig toConfig();

}
