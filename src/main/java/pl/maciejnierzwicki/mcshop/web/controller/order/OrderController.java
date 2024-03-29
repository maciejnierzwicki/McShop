package pl.maciejnierzwicki.mcshop.web.controller.order;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.ServiceValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.utils.AuthUtils;
import pl.maciejnierzwicki.mcshop.utils.ServiceUtils;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.OrderPaymentMethodForm;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.sms.SMSCodeVerifyForm;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
	
	@Autowired
	private ServiceValidator serviceValidator;
	
	@Autowired
	private Order order;

	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeVerifyForm smsCodeForm() {
		return new SMSCodeVerifyForm();
	}
	
	@ModelAttribute(name = "paymentMethodForm")
	public OrderPaymentMethodForm paymentMethodForm() {
		return new OrderPaymentMethodForm();
	}
	

	@PostMapping
	public String processOrder(Model model, @ModelAttribute(name = "order") @Valid Order validated_order, Errors errors) {
		if(errors.hasErrors() || !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) {
			log.debug("Invalid order or service, redirecting");
			return "redirect:/";
		}
		order.setPlayerName(validated_order.getPlayerName());
		log.debug("playerName = " + order.getPlayerName());
		User user = AuthUtils.getAuthenticatedUser();
		if(user != null) {
			log.debug("applying user info");
			order.setUser(user);	
		}
		else {
			log.debug("is not logged user");
		}
		order.setOrderType(OrderType.SERVICE_ORDER);
		model.addAttribute("order", order);
		model.addAttribute("paymentMethods", ServiceUtils.getAvailablePaymentMethods(order.getService(), user));
		model.addAttribute("paymentMethodForm", paymentMethodForm());
		model.addAttribute("VIEW_FILE", "order");
		model.addAttribute("VIEW_NAME", "order");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	
	@RequestMapping(path = "/finish", method = { RequestMethod.GET, RequestMethod.POST })
	public String showOrderFinish(Model model, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		model.addAttribute("VIEW_FILE", "orderfinish");
		model.addAttribute("VIEW_NAME", "orderfinish");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
