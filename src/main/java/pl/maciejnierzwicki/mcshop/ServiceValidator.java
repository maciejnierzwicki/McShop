package pl.maciejnierzwicki.mcshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.SMSValidationService;

@Component
public class ServiceValidator {
	
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	@Autowired(required = false)
	private BankTransferValidationService bankTransferValidationService;
	@Autowired(required = false)
	private SMSValidationService smsValidationService;
	
	public boolean hasAnyWorkingPaymentMethod(Service service) throws IllegalStateException {
		if(service == null) { throw new IllegalStateException("Passed null as service"); }
		return (service.getPrice() > 0) || (service.getPriceBankTransfer() > 0 && bankTransferConfig != null && bankTransferValidationService != null) 
				|| (service.getSmsCode() != null && smsConfig != null && smsValidationService != null);
	}
	
	public boolean hasAnyWorkingPaymentMethod(Service service, User user) throws IllegalStateException {
		if(service == null) { throw new IllegalStateException("Passed null as service"); }
		if(user == null) { return hasAnyWorkingPaymentMethod(service); }
		return (service.getPrice() > 0 && user.getMoney() >= service.getPrice()) || (service.getPriceBankTransfer() > 0 && bankTransferConfig != null && bankTransferValidationService != null) 
				|| (service.getSmsCode() != null && smsConfig != null && smsValidationService != null);
	}

}
