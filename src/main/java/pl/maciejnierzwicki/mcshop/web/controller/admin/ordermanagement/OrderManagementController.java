package pl.maciejnierzwicki.mcshop.web.controller.admin.ordermanagement;

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

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageForm;

@Controller
@RequestMapping("/admin/ordermanagement")
@Slf4j
public class OrderManagementController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MainProperties properties;
	
	@ModelAttribute(name = "pageForm")
	public PageForm pageForm() {
		return new PageForm();
	}
	
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST} )
	public String showPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("pageForm") @Valid PageForm pageForm) {
		List<Order> orders = orderService.getAllOrdersFromNewest(PageRequest.of(pageForm.getPage() - 1, properties.getMaxOrdersPerPage()));
		long all_orders = orderService.countAll();
		long all_pages = all_orders / properties.getMaxOrdersPerPage();
		if(all_orders % properties.getMaxOrdersPerPage() > 0) {
			all_pages += 1;
		}
		model.addAttribute("orders", orders);
		model.addAttribute("pageForm", pageForm);
		model.addAttribute("allPages", all_pages);
		model.addAttribute("VIEW_FILE", "admin/ordermanagement/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
