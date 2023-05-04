package pl.maciejnierzwicki.mcshop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay.DotPayConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.przelewy24.Przelewy24Config;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.impl.microsms.MicroSMSConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.SMSValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.dotpay.DotPayPaymentValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.microsms.MicroSMSValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24.Przelewy24PaymentValidationService;
import pl.maciejnierzwicki.mcshop.properties.PaymentProvidersProperties;

@Configuration
@Slf4j
public class PaymentProvidersLoader implements ApplicationContextAware {
	
	@Autowired
	private PaymentProvidersProperties providersProperties;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
		
	}
	
	@Bean
	@Primary
	public BankTransferValidationService bankTransferPaymentValidationService() {
		if(providersProperties.getBankTransferProvider() == null) {
			return null;
		}
		switch(providersProperties.getBankTransferProvider()) {
			case DOTPAY: {
				return context.getBean(DotPayPaymentValidationService.class);
			}
			case PRZELEWY24: {
				return context.getBean(Przelewy24PaymentValidationService.class);
			}
		}
		return null;
	}
	
	@Bean
	@Primary
	public SMSValidationService smsValidationService() {
		if(providersProperties.getSmsProvider() == null) {
			return null;
		}
		switch(providersProperties.getSmsProvider()) {
			case MICROSMS: {
				return context.getBean(MicroSMSValidationService.class);
			}
		}
		return null;
	}
	
	@Bean
	@Primary
	public BankTransferConfig bankTransferConfig() {
		BankTransferConfig config = null;
		if(providersProperties.getBankTransferProvider() == null) {
			return config;
		}
		switch(providersProperties.getBankTransferProvider()) {
			case DOTPAY: {
				config = context.getBean(DotPayConfig.class);
				break;
			}
			case PRZELEWY24: {
				config = context.getBean(Przelewy24Config.class);
				break;
			}
		}
		if(config != null) {
			config.loadFromFile();
		}
		return config;
	}
	
	@Bean
	@Primary
	public SMSConfig smsConfig() {
		SMSConfig config = null;
		if(providersProperties.getSmsProvider() == null) {
			return config;
		}
		switch(providersProperties.getSmsProvider()) {
			case MICROSMS: {
				config = context.getBean(MicroSMSConfig.class);
				break;
			}
		}
		if(config != null) {
			config.loadFromFile();
		}
		return config;
	}

}
