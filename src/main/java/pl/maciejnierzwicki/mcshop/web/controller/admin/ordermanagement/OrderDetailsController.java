package pl.maciejnierzwicki.mcshop.web.controller.admin.ordermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/ordermanagement/details")
public class OrderDetailsController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/{id}")
	public String showDetailsPage(@AuthenticationPrincipal User user, Model model, @PathVariable("id") Long order_id) {
		Order order = orderService.getById(order_id);
		if(order != null) {
			model.addAttribute("order", order);
		}
		model.addAttribute("VIEW_FILE", "admin/ordermanagement/details");
		model.addAttribute("VIEW_NAME", "orderDetailsAdmin");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
