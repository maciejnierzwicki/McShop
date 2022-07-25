package pl.maciejnierzwicki.mcshop.payment.config.banktransfer;

import org.springframework.stereotype.Component;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.BankTransferConfigForm;

/***
 * A base class for all bank transfer providers config classes.
 *
 */
@Data
@Component
public abstract class BankTransferConfig {
	
	protected BankTransferProvider providerName;
	
	public BankTransferConfigForm toForm() {return null;};
	public void apply(BankTransferConfigForm form) {};
	public void saveToFile() {};
	public void loadFromFile() {};
	

}
