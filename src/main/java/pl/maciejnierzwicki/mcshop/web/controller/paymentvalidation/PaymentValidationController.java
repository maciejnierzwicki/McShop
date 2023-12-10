package pl.maciejnierzwicki.mcshop.web.controller.paymentvalidation;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.dotpay.DotPayPaymentValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24.Przelewy24PaymentValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24.Przelewy24TransactionResult;
import pl.maciejnierzwicki.mcshop.service.OrderService;

/***
 * This controller handles all requests sent to <b>/paymentvalidation</b> path.<br>
 * Its purpose is to process notifications from bank transfer payment providers.<br>
 * These notifications contain important data related to orders made by application users.
 * @author Maciej Nierzwicki
 *
 */
@ResponseBody
@Controller
@RequestMapping(path = "/paymentvalidation")
@Slf4j
public class PaymentValidationController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private BankTransferConfig bankTransferConfig;
	@Autowired(required = false)
	private BankTransferValidationService bankTransferValidationService;
	
	
	@GetMapping
	public String get() {
		log.debug("payment validation GET redirect");
		return "redirect:/";
	}
	
	/***
	 * Passes received request to payment-provider specific validator to obtain {@link PaymentValidationResult}.
	 * If result contains not-null {@link Order} object reference and "paid" field is true, then order status is being set to PAID and Order gets updated in the database.
	 * Returns ResponseEntity with status OK (if whole request was fine and order has been marked as paid), otherwise BAD_REQUEST.
	 * @param http_req {@link HttpServletRequest}
	 * @param requestBody {@link String}
	 * @return {@link ResponseEntity}
	 */
	@PostMapping("/dotpay")
	public ResponseEntity<String> process(HttpServletRequest http_req, @RequestBody String body) {
		return validate(http_req, body);
	}
	
	@PostMapping("/przelewy24")
	public ResponseEntity<String> process(HttpServletRequest http_req, @RequestBody Przelewy24TransactionResult body) {
		return validate(http_req, body);
	}
	
	public <T> ResponseEntity<String> validate(HttpServletRequest http_req, T body) {
		if(bankTransferConfig == null || bankTransferValidationService == null) {
			log.debug("bankTransferConfig or bankTransferValidationService is null");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD_REQUEST");
		}
		PaymentValidationResult result = bankTransferValidationService.validateBankTransfer(http_req, body, bankTransferConfig);
		boolean paid = result.isPaid();
		Order order = result.getOrder();
		if(order == null || !paid) {
			log.debug("validation result returned null order or its status is marked as unpaid");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD_REQUEST");
		}
		
		if(order.getStatus() != OrderStatus.DELIVERED) {
			order.setStatus(OrderStatus.PAID);
			order = orderService.save(order);
			log.debug("order " + order.getId() + " status has been set to 'paid'");	
		}
		else {
			log.debug("This order was already delivered, only returning OK response.");
		}
		return ResponseEntity.ok("OK");
	}

}
