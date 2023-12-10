package pl.maciejnierzwicki.mcshop.payment.validation;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;

/***
 * A base class for all bank transfer payment validation classes.
 *
 */
@Service
public interface BankTransferValidationService {
	
	<T> PaymentValidationResult validateBankTransfer(HttpServletRequest http_req, T requestBody, BankTransferConfig bankTransferConfig);

}
