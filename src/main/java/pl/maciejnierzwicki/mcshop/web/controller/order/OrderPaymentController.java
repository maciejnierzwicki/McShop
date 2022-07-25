package pl.maciejnierzwicki.mcshop.web.controller.order;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.OrderValidator;
import pl.maciejnierzwicki.mcshop.ServiceValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;
import pl.maciejnierzwicki.mcshop.payment.PaymentValidationResult;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.validation.BankTransferValidationService;
import pl.maciejnierzwicki.mcshop.payment.validation.SMSValidationService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.OrderPaymentMethodForm;
import pl.maciejnierzwicki.mcshop.web.form.order.payment.sms.SMSCodeVerifyForm;

@Controller
@RequestMapping("/order/payment")
@SessionAttributes("order")
@Slf4j
public class OrderPaymentController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	@Autowired(required = false)
	private SMSValidationService smsValidationService;
	@Autowired(required = false)
	private BankTransferValidationService bankTransferValidationService;
	
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	@Autowired
	private ServiceValidator serviceValidator;
	@Autowired
	private OrderValidator orderValidator;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeVerifyForm smsCodeForm() {
		return new SMSCodeVerifyForm();
	}
	
	@ModelAttribute(name = "paymentMethodForm")
	public OrderPaymentMethodForm paymentMethodForm() {
		return new OrderPaymentMethodForm();
	}
	
	@GetMapping 
	public String getOrderPayment() {
		return "redirect:/";
	}
	
	@PostMapping
	public String showOrderPayment(Model model, @SessionAttribute(name = "order") Order order,  @ModelAttribute(name = "paymentMethodForm") @Valid OrderPaymentMethodForm paymentMethodForm, Errors errors) {
		if(order == null|| !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) { log.debug("order null or service invalid, redirecting to main page"); return "redirect:/"; }
		if(orderValidator.isValidOrder(order)) {
			String selected_method = paymentMethodForm.getPaymentMethod() != null ? paymentMethodForm.getPaymentMethod() : order.getPaymentMethod().toString();
			log.debug("Selected payment method: " + (selected_method != null ? selected_method : order.getPaymentMethod().toString()));
			order.setPaymentMethod(PaymentMethod.valueOf(selected_method));
			model.addAttribute("order", order);	
			switch(order.getPaymentMethod()) {
				case BANK_TRANSFER:{
					if(bankTransferConfig == null || bankTransferValidationService == null || order.getService().getPriceBankTransfer() <= 0) { log.debug("bank transfer payment method was selected but bank transfer provider is not loaded or service doesn't allow this payment method; redirecting to home page"); return "redirect:/"; }
					model.addAttribute("bankTransferConfig", bankTransferConfig);
					order.setFinalPrice(order.getService().getPriceBankTransfer());
					break;
				}
				case SMS: {
					if(smsConfig == null || smsValidationService == null || order.getService().getSmsCode() == null) { log.debug("sms payment method was selected but sms provider is not loaded or service doesn't allow this payment method; redirecting to home page"); return "redirect:/"; }
					model.addAttribute("smsConfig", smsConfig);
					model.addAttribute("smsCodeForm", smsCodeForm());
					order.setFinalPrice(order.getService().getSmsCode().getPriceNet() * 1.23);
					break;
				}
				case ACCOUNT_FUNDS: {
					if(order.getService().getPrice() <= 0) { log.debug("'user funds' payment method was selected but service doesn't allow this payment method; redirecting to home page"); return "redirect:/"; } 
					order.setFinalPrice(order.getService().getPrice());
					break;
				}
			}
			if(order.getCreationDate() == null) {
				order.setCreationDate(new Date());
				order = orderService.save(order);
				eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_CREATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
			}
			else {
				order = orderService.save(order);
			}
		}
		
		model.addAttribute("VIEW_FILE", "orderpayment");
		model.addAttribute("VIEW_NAME", "orderpayment");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping(path = "/sms") 
	public String processOrderPaymentSMSForm(Model model, @SessionAttribute(name = "order") Order order, @ModelAttribute(name = "smsCodeForm") SMSCodeVerifyForm form, SessionStatus sessionStatus) {
		if(order == null || smsConfig == null || smsValidationService == null || !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) { log.debug("order null or service invalid, redirecting to main page"); return "redirect:/"; }
		log.debug("Got sms code to verify: " + form.getCode());
		PaymentValidationResult result = smsValidationService.validateSMSCode(form.getCode(), order.getService().getSmsCode().getPhoneNumber(), smsConfig);
		boolean valid = result.isPaid();
		if(valid) { 
			order.setStatus(OrderStatus.PAID);
			orderService.save(order);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_UPDATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
			sessionStatus.setComplete();
		}
		
		
		model.addAttribute("order", order);
		model.addAttribute("smsConfig", smsConfig);
		model.addAttribute("smsCodeForm", form);
		model.addAttribute("valid", valid);
		
		
		model.addAttribute("VIEW_FILE", "orderpayment");
		model.addAttribute("VIEW_NAME", "orderpayment");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping(path = "/funds") 
	public String processOrderPaymentFundsForm(@AuthenticationPrincipal User user, Model model, @SessionAttribute(name = "order") Order order, SessionStatus sessionStatus) {
		if(order == null || !serviceValidator.hasAnyWorkingPaymentMethod(order.getService())) { log.debug("order null or service invalid, redirecting to main page"); return "redirect:/"; }
		model.addAttribute("order", order);
		double finalPrice = order.getFinalPrice();
		double userMoney = user.getMoney();
		if(userMoney >= finalPrice) {
			user.setMoney(userMoney - finalPrice);
			userService.save(user);
			order.setStatus(OrderStatus.PAID);
			orderService.save(order);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_UPDATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
			model.addAttribute("success", true);
			sessionStatus.setComplete();
		}
		else {
			model.addAttribute("notEnoughFunds", true);
		}
		
		
		model.addAttribute("VIEW_FILE", "order/payment/funds-result");
		model.addAttribute("VIEW_NAME", "funds-result");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
