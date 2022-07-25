package pl.maciejnierzwicki.mcshop.web.controller.order;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.ServiceValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.utils.AuthUtils;
import pl.maciejnierzwicki.mcshop.utils.ServiceUtils;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.OrderPaymentMethodForm;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.sms.SMSCodeVerifyForm;

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
@Slf4j
public class OrderController {
	
	@Autowired
	private ServiceValidator serviceValidator;

	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeVerifyForm smsCodeForm() {
		return new SMSCodeVerifyForm();
	}
	
	@ModelAttribute(name = "paymentMethodForm")
	public OrderPaymentMethodForm paymentMethodForm() {
		return new OrderPaymentMethodForm();
	}
	
	@PostMapping
	public String processOrder(Model model, @ModelAttribute(name = "order") @Valid Order order, Errors errors) {
		if(errors.hasErrors() || !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) {
			log.debug("Invalid order or service, redirecting");
			return "redirect:/";
		}
		log.debug("playerName = " + order.getPlayerName());
		User user = AuthUtils.getAuthenticatedUser();
		if(user != null) {
			log.debug("applying user info");
			order.setUser(user);	
		}
		else {
			log.debug("is not logged user");
		}
		model.addAttribute("paymentMethods", ServiceUtils.getAvailablePaymentMethods(order.getService(), user));
		model.addAttribute("paymentMethodForm", paymentMethodForm());
		model.addAttribute("VIEW_FILE", "order");
		model.addAttribute("VIEW_NAME", "order");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	
	@GetMapping(path = "/finish") 
	public String getOrderFinish() {
		return "redirect:/";
	}
	
	@PostMapping(path = "/finish")
	public String postOrderFinish(Model model, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		model.addAttribute("VIEW_FILE", "orderfinish");
		model.addAttribute("VIEW_NAME", "orderfinish");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
