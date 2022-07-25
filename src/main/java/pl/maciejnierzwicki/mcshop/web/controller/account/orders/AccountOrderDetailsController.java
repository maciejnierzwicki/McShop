package pl.maciejnierzwicki.mcshop.web.controller.account.orders;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.repository.OrderRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;


@Controller
@RequestMapping("/account/orders/details")
@SessionAttributes(names = {"order"})
public class AccountOrderDetailsController {
	
	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping("/{id}")
	public String showOrderDetailsPage(@AuthenticationPrincipal User user, Model model, @PathVariable("id") Long order_id) {
		Optional<Order> op_order = orderRepo.findById(order_id);
		if(op_order.isPresent()) {
			Order order = op_order.get();
			model.addAttribute("order", order);
		}
		model.addAttribute("VIEW_FILE", "account/orders/details");
		model.addAttribute("VIEW_NAME", "orderDetailsUser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
