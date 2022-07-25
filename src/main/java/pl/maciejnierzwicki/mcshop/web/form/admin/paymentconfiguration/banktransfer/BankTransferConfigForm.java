package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;

@Data
public abstract class BankTransferConfigForm {
	
	public abstract BankTransferConfig toConfig();

}
