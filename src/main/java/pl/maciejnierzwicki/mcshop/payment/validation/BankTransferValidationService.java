package pl.maciejnierzwicki.mcshop.payment.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;

/***
 * A base class for all bank transfer payment validation classes.
 *
 */
@Service
public interface BankTransferValidationService {
	
	PaymentValidationResult validateBankTransfer(HttpServletRequest http_req, String requestBody, BankTransferConfig bankTransferConfig);

}
