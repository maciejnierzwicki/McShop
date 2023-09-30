package pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay.DotPayConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.service.OrderService;

/***
 * Implementation of {@link BankTransferValidationService} for <a href="https://www.przelewy24.pl/">Przelewy24</a> support.<br>
 *
 */
@Service
@Slf4j
//TODO: Complete
public class Przelewy24PaymentValidationService implements BankTransferValidationService {

	@Autowired
	private OrderService orderService;

	@Override
	public PaymentValidationResult validateBankTransfer(HttpServletRequest http_req, String requestBody,
			BankTransferConfig bankTransferConfig) {
			PaymentValidationResult result = new PaymentValidationResult();
			result.setPaid(false);
			return result;
	}


}
