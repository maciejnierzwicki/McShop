package pl.maciejnierzwicki.mcshop.web.controller.account.addfunds;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.repository.OrderRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.account.AddFundsForm;

@Controller
@RequestMapping("/account/addfunds/banktransfer")
@SessionAttributes("order")
public class AddFundsBankTransferController {
	
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@PostMapping
	public String processAddFunds(@AuthenticationPrincipal User user, Model model, @ModelAttribute("fundsForm") AddFundsForm fundsForm, SessionStatus status) throws Exception {
		if(bankTransferConfig == null) {
			throw new Exception("Bank transfer payments disabled.");
		}
		Order order = new Order(OrderType.FUNDS_ORDER);
		order.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
		order.setUser(user);
		order.setAmount(fundsForm.getAmount());
		order.setFinalPrice(order.getAmount());
		order.setCreationDate(new Date());
		order = orderRepo.save(order);
		eventPublisher.publishEvent(EventBuilder.forType(EventType.ORDER_CREATE_EVENT).withOrder(order).withActor(order.getUser()).toEvent());
		model.addAttribute("order", order);
		model.addAttribute("VIEW_FILE", "account/addfunds/banktransfer");
		model.addAttribute("VIEW_NAME", "addfunds_banktransfer");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping("/retry")
	public String processAddFundsWithOrder(@AuthenticationPrincipal User user, Model model, @ModelAttribute("order") Order order) throws Exception {
		if(order.getStatus() != OrderStatus.WAITING_PAYMENT) {
			return "redirect:/";
		}
		if(bankTransferConfig == null) {
			throw new Exception("Bank transfer payments disabled.");
		}
		model.addAttribute("order", order);
		model.addAttribute("VIEW_FILE", "account/addfunds/banktransfer");
		model.addAttribute("VIEW_NAME", "addfunds_banktransfer");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
