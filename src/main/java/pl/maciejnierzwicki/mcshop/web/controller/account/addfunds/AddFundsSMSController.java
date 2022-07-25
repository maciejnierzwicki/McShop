package pl.maciejnierzwicki.mcshop.web.controller.account.addfunds;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.SMSValidationService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.account.AddFundsFormSMS;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.sms.SMSCodeVerifyForm;

@Controller
@RequestMapping("/account/addfunds/sms")
@SessionAttributes(names = {"smsCode", "order"})
public class AddFundsSMSController {
	
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private OrderService orderService;
	@Autowired(required = false)
	private SMSValidationService smsValidationService;
	@Autowired
	private UserService userService;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@ModelAttribute(name = "fundsFormSMS")
	public AddFundsFormSMS fundsFormSMS() {
		return new AddFundsFormSMS();
	}
	
	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeVerifyForm smsCodeForm() {
		return new SMSCodeVerifyForm();
	}
	
	
	@PostMapping
	public String processAddFunds(@AuthenticationPrincipal User user, Model model, @ModelAttribute("fundsFormSMS") AddFundsFormSMS fundsForm) throws Exception {
		if(smsValidationService == null || smsConfig == null) {
			throw new Exception("SMS payments disabled.");
		}
		Order order = new Order(OrderType.FUNDS_ORDER);
		order.setPaymentMethod(PaymentMethod.SMS);
		order.setUser(user);
		model.addAttribute("smsCodeForm", smsCodeForm());
		SMSCode sms_code = smsCodesService.getById(Long.parseLong(fundsForm.getSmsCodeId()));
		if(sms_code != null) {
			order.setAmount(sms_code.getFundsToAdd());
			order.setFinalPrice(sms_code.getPriceNet() * 1.23);
			model.addAttribute("smsCode", sms_code);
		}
		order.setCreationDate(new Date());
		order = orderService.save(order);
		model.addAttribute("order", order);
		eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_CREATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
		model.addAttribute("VIEW_FILE", "account/addfunds/sms");
		model.addAttribute("VIEW_NAME", "sms");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping("/retry")
	public String processAddFundsWithExistsingOrder(@AuthenticationPrincipal User user, Model model, @ModelAttribute("order") Order order) throws Exception {
		if(order.getStatus() != OrderStatus.WAITING_PAYMENT) {
			return "redirect:/";
		}
		if(smsValidationService == null || smsConfig == null) {
			throw new Exception("SMS payments disabled.");
		}
		Iterable<SMSCode> codes = smsCodesService.findAllByProviderNameAndFundsToAdd(smsConfig.getProviderName(), order.getAmount());
		SMSCode sms_code = null;
		for(SMSCode code : codes) {
			if(code.getFundsToAdd() == order.getAmount()) {
				sms_code = code;
				break;
			}
		}
		model.addAttribute("smsCodeForm", smsCodeForm());
		if(sms_code != null) {
			model.addAttribute("smsCode", sms_code);
		}
		model.addAttribute("order", order);
		model.addAttribute("VIEW_FILE", "account/addfunds/sms");
		model.addAttribute("VIEW_NAME", "sms");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping(path = "/payment")
	public String processAddFunds(@AuthenticationPrincipal User user, Model model, @ModelAttribute(name = "smsCodeForm") SMSCodeVerifyForm form, @SessionAttribute(name = "smsCode") SMSCode smsCode, @ModelAttribute("order") Order order, SessionStatus status) throws Exception {
		if(smsValidationService == null || smsConfig == null) {
			throw new Exception("SMS payments disabled.");
		}
		PaymentValidationResult result = smsValidationService.validateSMSCode(form.getCode(), smsCode.getPhoneNumber(), smsConfig);
		boolean valid = result.isPaid();
		if(valid) { 
			order.setStatus(OrderStatus.PAID);
			order = orderService.save(order);
			userService.save(user);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_UPDATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
		}
		
		model.addAttribute("smsCodeForm", form);
		model.addAttribute("valid", valid);
		model.addAttribute("VIEW_FILE", "account/addfunds/sms");
		model.addAttribute("VIEW_NAME", "sms");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
