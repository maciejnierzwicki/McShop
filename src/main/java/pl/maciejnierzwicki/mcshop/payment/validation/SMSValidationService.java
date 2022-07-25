package pl.maciejnierzwicki.mcshop.payment.validation;

import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;

/***
 * A base class for all SMS payment validation classes.
 * @author Maciej Nierzwicki
 *
 */
@Service
public interface SMSValidationService {
	
	PaymentValidationResult validateSMSCode(String code, Integer phoneNumber, SMSConfig smsConfig);

}
