package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.EventMetaService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/servicemanagement/deleteservice")
public class DeleteServiceController  {
	
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private EventMetaService eventMetaService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@GetMapping
	public String showDeleteServicePage(@AuthenticationPrincipal User user, Model model, @RequestParam("serviceid") Long service_id) {
		Service service = servicesService.getById(service_id);
		
		if(service != null) {
			model.addAttribute("service", service);
		}
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/deleteservice");
		model.addAttribute("VIEW_NAME", "deleteservice");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDeleteService(@AuthenticationPrincipal User user, Model model, @RequestParam("serviceid") Long service_id) {
		Service service = servicesService.getById(service_id);
		
		if(service != null) {
			eventMetaService.deleteServiceData(service.getId());
			orderService.deleteServiceData(service_id);
			servicesService.delete(service);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_DELETE_EVENT).withActor(user).toEvent());
		}

		return "redirect:/admin/servicemanagement";
	}

}
