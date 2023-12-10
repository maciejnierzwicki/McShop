package pl.maciejnierzwicki.mcshop.payment.validation.impl.dotpay;

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
 * Implementation of {@link BankTransferValidationService} for <a href="https://www.dotpay.pl/">DotPay</a> support.<br>
 *
 */
@Service
@Slf4j
public class DotPayPaymentValidationService implements BankTransferValidationService {

	@Autowired
	private OrderService orderService;
	
	/***
	 * Converts given String to Map, assuming all key-value pairs are separated by "<b>&</b>" char and values are prepended by "<b>=</b>" char.<br>
	 * Example String: "name=John&surname=Doe" produces map with two key-value pairs (name: John, surname: Doe)<br>
	 * @param requestBody
	 * @return resulting Map object
	 */
	private Map<String, String> toMap(String requestBody) throws IllegalArgumentException {
		log.debug("Request body: " + requestBody);
		if(!requestBody.contains("=")) {
			throw new IllegalArgumentException("Invalid String, not containg '=' characters.");
		}
		String[] parts = requestBody.split("&");
		Map<String, String> map = new HashMap<>();
		for(String part : parts) {
			String[] kv = part.split("=");
			map.put(kv[0], kv[1]);
			log.debug(kv[0] + " = " + kv[1]);
		}
		return map;
	}
	
	/***
	 * Checks whether map contains valid payment details: currency and amount and compares them with data in provided {@link Order} object.
	 * @param map
	 * @param order
	 * @return
	 */
	private boolean hasValidPaymentData(Map<String, String> map, Order order) {
		// Dotpay uses incorrect spelling of word "original" in its notifications...
		if(!map.containsKey("amount") || !map.containsKey("orginal_amount")) {
			log.debug("Map doesn't contain 'amount' or 'orginal_amount' key.");
			return false;
		}
		String amount = map.get("amount");
		String original_amount = map.get("orginal_amount");
		if(amount == null || original_amount == null) {
			log.debug("'amount' or 'orginal_amount' is null");
			return false;
		}
		
		String[] original_amount_data = original_amount.split("\\+");
		if(!original_amount_data[0].equals(amount)) { log.debug("'orginal_amount' value isn't equal to 'amount' value (" + original_amount_data[0] + " != " + amount + ")"); return false; }
		if(!original_amount_data[1].equals("PLN")) { log.debug("'orginal_amount' doesn't contain valid currency data ('PLN' expected, got '" + original_amount_data[1] + "')."); return false; }

		double paymentAmount;
		try {
			paymentAmount = Double.parseDouble(amount);
		}
		catch(NumberFormatException e) {paymentAmount = -1;}
		double finalPrice = order.getFinalPrice().doubleValue();
		if(finalPrice != paymentAmount) {
			log.debug("final price " + finalPrice + " of order " + order.getId() + " is different from amount value passed in 'amount' key (" + paymentAmount + ")");
			return false;
		}
		return true;
	}
	
	/***
	 * Tries to obtain status code of payment assuming it is a value of "t_status" key in map.
	 * Returns true if status code is equal to 2, otherwise false.
	 * @param map
	 * @return true if map contains key "t_status" and its value is equal to 2, otherwise false.
	 */
	private boolean isPaid(Map<String, String> map) {
		if(!map.containsKey("t_status")) { log.debug("Map doesn't contain 't_status' key"); return false; }
		String status = map.get("t_status");
		// status code 2 is considered as paid
		if(!status.equals("2")) {
			log.debug("t_status is not equal to 2");
			return false;
		}
		log.debug("Map contains 't_status' key with value 2 (paid order)");
		return true;
	}
	
	/***
	 * Tries to obtain order id assuming it is a value of "control" key in map.
	 * Returns resulting long if any, otherwise -1.
	 * @param map {@link Map}
	 * @return {@link Long} value of "control" key in map or -1 if no such key exist or value is not a valid long.
	 */
	public static Long getOrderID(Map<String, String> map) {
		if(!map.containsKey("control")) { log.debug("Map doesn't contain 'control' key"); return -1L; }
		String control = map.get("control");
		Long id;
		try {
			id = Long.parseLong(control);
		}
		catch(NumberFormatException e) {log.debug("Invalid number passed as 'control' value (" + control + ")"); return -1L;}
		return id;
	}
	
	@Override
	public <T> PaymentValidationResult validateBankTransfer(HttpServletRequest http_req, T requestBody, BankTransferConfig bankTransferConfig) {
		if(!(bankTransferConfig instanceof DotPayConfig)) {
			throw new IllegalStateException("Invalid bank transfer configuration passed as parameter");
		}
		DotPayConfig dotPayConfig = (DotPayConfig) bankTransferConfig;
		PaymentValidationResult result = new PaymentValidationResult();
		String ip_address = http_req.getRemoteAddr();
		if(!dotPayConfig.getAllowedIPs().contains(ip_address)) { log.debug("Request sender IP " + ip_address + " is not allowed in Dotpay configuration"); return result; }
		String body = (String) requestBody;
		Map<String, String> map = toMap(body);
		if(!isPaid(map)) { return result; }
		long order_id = getOrderID(map);
		log.debug("Returned order id: " + order_id);
		Order order = orderService.getById(order_id);
		if(order == null) { log.debug("Order with id " + order_id + " not found "); return result; }
		if(!hasValidPaymentData(map, order)) { log.debug("No valid payment data"); return result; }
		result.setPaid(true);
		result.setOrder(order);
		return result;
	}
	

}
