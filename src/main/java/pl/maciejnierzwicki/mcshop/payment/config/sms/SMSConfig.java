package pl.maciejnierzwicki.mcshop.payment.config.sms;


import org.springframework.stereotype.Component;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSConfigForm;

/***
 * A base class for all SMS providers config classes.
 * @author Maciej Nierzwicki
 *
 */
@Data
@Component
public abstract class SMSConfig {
	
	protected SMSProvider providerName;
	
	public abstract SMSConfigForm toForm();
	public abstract void apply(SMSConfigForm form);
	public abstract void saveToFile();
	public abstract void loadFromFile();
	
}
