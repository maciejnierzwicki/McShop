package pl.maciejnierzwicki.mcshop.utils;

import java.util.ArrayList;
import java.util.List;

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;

public class ServiceUtils {
	
	public static List<PaymentMethod> getAvailablePaymentMethods(Service service) {
		return getAvailablePaymentMethods(service, null);
	}
	
	public static List<PaymentMethod> getAvailablePaymentMethods(Service service, User user) {
		List<PaymentMethod> methods = new ArrayList<>();
		double priceBankTransfer = service.getPriceBankTransfer();
		if(priceBankTransfer > 0) { 
			if(user != null && user.getMoney() >= priceBankTransfer) {
				methods.add(PaymentMethod.ACCOUNT_FUNDS);
			}
			methods.add(PaymentMethod.BANK_TRANSFER);
		}
		SMSCode smsCode = service.getSmsCode();
		if(smsCode != null) { 
			if(!methods.contains(PaymentMethod.ACCOUNT_FUNDS) && user != null) {
				if(user.getMoney() >= smsCode.getPriceNet()) {
					methods.add(PaymentMethod.ACCOUNT_FUNDS);
				}
			}
			methods.add(PaymentMethod.SMS); 
		}
		return methods;
	}
}