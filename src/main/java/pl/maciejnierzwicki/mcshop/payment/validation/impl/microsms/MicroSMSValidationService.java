package pl.maciejnierzwicki.mcshop.payment.validation.impl.microsms;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.impl.microsms.MicroSMSConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.SMSValidationService;

/***
 * Implementation of {@link SMSValidationService} for <a href="https://microsms.pl/">MicroSMS</a> support.
 *
 */
@Service
@Slf4j
public class MicroSMSValidationService implements SMSValidationService {
	
	@Autowired
	private RestTemplate template;
	
	private final String link = "https://microsms.pl/api/v2/multi.php?userid=<userid>&number=<number>&code=<code>&serviceid=<serviceid>";

	@Override
	public PaymentValidationResult validateSMSCode(String code, Integer phoneNumber, SMSConfig smsConfig) {
		PaymentValidationResult result = new PaymentValidationResult();
		MicroSMSConfig microSMSConfig = (MicroSMSConfig) smsConfig;
		String verification_link = getSMSVerificationLink(code, phoneNumber, microSMSConfig.getUserId(), microSMSConfig.getServiceId());
		String response = template.getForObject(URI.create(verification_link), String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			MicroSMSResponse sms_response = mapper.readValue(response, MicroSMSResponse.class);
			MicroSMSResponseData data = sms_response.getData();
			boolean valid = data.getStatus() == 1 && data.getUsed() == null && data.getNumber() == phoneNumber;
			if(valid) { result.setPaid(true); }
		} catch (JsonMappingException e) {
			log.warn(e.getMessage());
		} catch (JsonProcessingException e) {
			log.warn(e.getMessage());
		}
		return result;
	}
	
	private String getSMSVerificationLink(String code, Integer phoneNumber, int userId, int serviceId) {
		return link.replaceAll("<userid>", String.valueOf(userId)).replaceAll("<number>", String.valueOf(phoneNumber)).replaceAll("<code>", code).replaceAll("<serviceid>", String.valueOf(serviceId));
	}

}
