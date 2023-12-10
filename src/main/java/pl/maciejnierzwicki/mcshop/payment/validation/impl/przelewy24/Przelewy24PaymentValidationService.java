package pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24;

import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24.Przelewy24TransactionResult;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay.DotPayConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.przelewy24.Przelewy24Config;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24.Przelewy24TransactionBody;
import pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24.Przelewy24TransactionResponse;
import pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24.Przelewy24VerificationBody;

import org.springframework.http.HttpEntity;

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
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public <T> PaymentValidationResult validateBankTransfer(HttpServletRequest http_req, T requestBody, BankTransferConfig bankTransferConfig) {
		if(!(bankTransferConfig instanceof Przelewy24Config)) {
			throw new IllegalStateException("Invalid bank transfer configuration passed as parameter");
		}	
				
		Przelewy24Config przelewy24Config = (Przelewy24Config) bankTransferConfig;
		PaymentValidationResult result = new PaymentValidationResult();
		String ip_address = http_req.getRemoteAddr();
		if(!przelewy24Config.getAllowedIPs().contains(ip_address)) { log.debug("Request sender IP " + ip_address + " is not allowed in Dotpay configuration"); return result; }
		
		Przelewy24TransactionResult txResult = (Przelewy24TransactionResult) requestBody;
		
		Order order = orderService.getById(Long.parseLong(txResult.getSessionId()));
		if(order == null) { return result; }
		result.setOrder(order);
		
		Przelewy24VerificationBody body = new Przelewy24VerificationBody();
		body.setMerchantId(przelewy24Config.getMerchantId());
		body.setPosId(przelewy24Config.getShopId());
		body.setSessionId(String.valueOf(txResult.getSessionId()));
		body.setAmount(txResult.getAmount());
		body.setCurrency(txResult.getCurrency());
		body.setOrderId(txResult.getOrderId());
		String sign = "{\"sessionId\":\"" + body.getSessionId() + "\",\"orderId\":" + body.getOrderId() + ",\"amount\":" + body.getAmount() + ",\"currency\":\"" + body.getCurrency() + "\",\"crc\":\"" + przelewy24Config.getCrc() + "\"}";
		sign = StringEscapeUtils.unescapeJava(sign);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-384");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return result;
		}
		byte[] messageDigest = md.digest(sign.getBytes());
		BigInteger no = new BigInteger(1, messageDigest);
		String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        log.debug("Generated sign(2): " + hashtext);
		body.setSign(hashtext);
		
		
        String auth = body.getPosId() + ":" + przelewy24Config.getApiKey();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
		//RequestEntity<Przelewy24VerificationBody> entity = RequestEntity.post(URI.create(przelewy24Config.getTransactionVerifyUrl())).headers(headers).body(body);
		HttpEntity<Przelewy24VerificationBody> entity = new HttpEntity<>(body, headers);
		ResponseEntity<Przelewy24TransactionResponse> response = restTemplate.exchange(URI.create(przelewy24Config.getTransactionVerifyUrl()), HttpMethod.PUT, entity, Przelewy24TransactionResponse.class);
		int status = response.getStatusCode().value();
		log.debug("Status code: " + status);
		if(status == 200) {
			result.setPaid(true);
			return result;
		}
		else {
			log.debug("response returned with code " + status);
		}
		
		
		result.setPaid(false);
		return result;
	}


}
