/**
 * 
 */
package pl.maciejnierzwicki.mcshop.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;

@Component
public class PaymentProvidersToModelAppender implements HandlerInterceptor {
	
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null) { return; }
		modelAndView.addObject("bankTransferConfig", bankTransferConfig);
		modelAndView.addObject("smsConfig", smsConfig);
	}

}
