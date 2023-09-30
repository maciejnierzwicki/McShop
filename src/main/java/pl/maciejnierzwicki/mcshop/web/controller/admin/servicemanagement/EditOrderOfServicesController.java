package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.AdminEditOrderOfElementsForm;

@Controller
@RequestMapping("/admin/servicemanagement/editorder")
public class EditOrderOfServicesController {
	
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute(name = "servicesOrder")
	public AdminEditOrderOfElementsForm servicesOrder() {
		AdminEditOrderOfElementsForm form = new AdminEditOrderOfElementsForm();
		Map<Long, Integer> servicesOrder = new HashMap<>();
		for(Service service : servicesService.getAll()) {
			servicesOrder.put(service.getId(), service.getPosition());
		}
		form.setOrder(servicesOrder);
		return form;
	}
	
	@GetMapping
	public String showEditOrderOfServicesPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("services", servicesService.getAllOrderedByPositionHighest());
		model.addAttribute("servers", serverService.getAllOrderedByPositionHighest());
		model.addAttribute("categories", categoryService.getAllOrderedByPositionHighest());
		model.addAttribute("servicesOrder", servicesOrder());
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processEditOrderOfServicesPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("servicesOrder") @Valid AdminEditOrderOfElementsForm form, BindingResult result, Errors errors) {
		Map<Long, Integer> servicesOrder = form.getOrder();
		Iterable<Service> services = servicesService.getAllOrderedByPositionHighest();
		for(Service service : services) {
			long id = service.getId();
			if(servicesOrder.containsKey(id)) {
				service.setPosition(servicesOrder.get(id));
			}
		}
		servicesService.saveAll(services);
		model.addAttribute("services", services);
		model.addAttribute("servers", serverService.getAllOrderedByPositionHighest());
		model.addAttribute("categories", categoryService.getAllOrderedByPositionHighest());
		model.addAttribute("servicesOrder", servicesOrder());
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
