package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/servicemanagement")
public class ServiceManagementController {
	
	@Autowired
	private ServicesService servicesService;
	
	@GetMapping
	public String showMainPage(@AuthenticationPrincipal User user, Model model) {
		Iterable<Service> services = servicesService.getAll();
		model.addAttribute("services", services);
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}