package pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.OrderValidator;
import pl.maciejnierzwicki.mcshop.ServiceValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.controller.order.OrderPaymentController;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.OrderPaymentMethodForm;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.przelewy24.Przelewy24Config;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;

@Controller
@RequestMapping("/order/payment/przelewy24")
@Slf4j
public class Przelewy24PaymentProcesserController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private Order order;
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	@Autowired
	private ServiceValidator serviceValidator;
	@Autowired
	private OrderValidator orderValidator;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	protected MainProperties properties;
	
	@SuppressWarnings("deprecation")
	@PostMapping
	public String prepareTransactionAndRedirectToPayment(Model model, @ModelAttribute(name = "id") String order_id, @ModelAttribute(name = "email") String email, Errors errors) {
		if(order == null || !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) { log.debug("order null or service invalid, redirecting to main page"); return "redirect:/"; }
		log.debug("Looking for order with id " + order_id);
		Order validated_order = orderService.getById(Long.valueOf(order_id));
		log.debug("validated order null? " + (validated_order == null));
		if(validated_order != null) {
			log.debug("validated order service null? " + (validated_order.getService() == null));
		}
		validated_order.setOrderType(order.getOrderType());
		validated_order.setService(order.getService());
		validated_order.setUser(order.getUser());
		if(orderValidator.isValidOrder(validated_order)) {
			order = validated_order;
			Przelewy24Config przelewy24Config = (Przelewy24Config) bankTransferConfig;
			Przelewy24TransactionBody body = new Przelewy24TransactionBody();
			body.setMerchantId(przelewy24Config.getMerchantId());
			body.setPosId(przelewy24Config.getShopId());
			body.setUrlReturn(properties.getSiteUrl() + "/order/finish");
			body.setSessionId(String.valueOf(validated_order.getId()));
			body.setAmount((int) (validated_order.getFinalPrice() * 100));
			body.setCurrency("PLN");
			body.setDescription(order.getService().getName());
			body.setEmail(email);
			body.setUrlStatus(properties.getSiteUrl() + "/paymentvalidation/przelewy24");
			body.setCountry("PL");
			body.setLanguage("pl");
			
			String sign = "{\"sessionId\":\"" + body.getSessionId() + "\",\"merchantId\":" + body.getMerchantId() + ",\"amount\":" + body.getAmount() + ",\"currency\":\"" + body.getCurrency() + "\",\"crc\":\"" + przelewy24Config.getCrc() + "\"}";
			sign = StringEscapeUtils.unescapeJava(sign);
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA-384");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return "redirect:/";
			}
			byte[] messageDigest = md.digest(sign.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
			body.setSign(hashtext);
			log.debug("Generated sign: " + hashtext);
	        String auth = body.getPosId() + ":" + przelewy24Config.getApiKey();
	        byte[] encodedAuth = Base64.encodeBase64(
	                auth.getBytes(StandardCharsets.ISO_8859_1));
	        String authHeader = "Basic " + new String(encodedAuth);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
			RequestEntity<Przelewy24TransactionBody> entity = RequestEntity.post(URI.create(przelewy24Config.getTransactionRegisterUrl())).headers(headers).body(body);
			ResponseEntity<Przelewy24TransactionResponse> response = restTemplate.postForEntity(URI.create(przelewy24Config.getTransactionRegisterUrl()), entity, Przelewy24TransactionResponse.class);
			int status = response.getStatusCode().value();
			log.debug("Status code: " + status);
			if(status == 200) {
				String token = response.getBody().getData().get("token");
				String paymentUrl = "https://secure.przelewy24.pl/trnRequest/" + token;
				log.debug("redirecting to payment url: " + paymentUrl);
				return "redirect:" + paymentUrl;
			}
			else {
				log.debug("response returned with code " + status + ", redirecting to /error");
				return "redirect:/error";
			}
		}
		
		else {
			log.debug("(payment-przelewy24) not valid order");
			return "redirect:/";
		}
	}

}
