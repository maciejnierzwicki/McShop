package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration;

import org.springframework.stereotype.Component;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferProvider;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;

@Component
@Data
public class PaymentProvidersPropertiesForm {
	
	private BankTransferProvider bankTransferProvider = null;
	
	private SMSProvider smsProvider = null;
	
	private boolean restartApp = false;
	


}
