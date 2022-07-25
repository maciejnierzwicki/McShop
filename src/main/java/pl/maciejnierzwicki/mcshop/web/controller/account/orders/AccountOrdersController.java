package pl.maciejnierzwicki.mcshop.web.controller.account.orders;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.repository.OrderRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageForm;


@Controller
@RequestMapping("/account/orders")
@SessionAttributes(names = {"order"})
public class AccountOrdersController {
	
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private MainProperties properties;
	
	@ModelAttribute(name = "pageForm")
	public PageForm pageForm() {
		return new PageForm();
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST} )
	public String showOrdersPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("pageForm") @Valid PageForm pageForm) {
		List<Order> orders = orderRepo.findByUserOrderByCreationDateDesc(user, PageRequest.of(pageForm.getPage() - 1, properties.getMaxOrdersPerPage()));
		long all_orders = orderRepo.countAllByUser(user);
		long all_pages = all_orders / properties.getMaxOrdersPerPage();
		if(all_orders % properties.getMaxOrdersPerPage() > 0) {
			all_pages += 1;
		}
		model.addAttribute("orders", orders);
		model.addAttribute("pageForm", pageForm);
		model.addAttribute("allPages", all_pages);
		model.addAttribute("VIEW_FILE", "account/orders/home");
		model.addAttribute("VIEW_NAME", "orders");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
