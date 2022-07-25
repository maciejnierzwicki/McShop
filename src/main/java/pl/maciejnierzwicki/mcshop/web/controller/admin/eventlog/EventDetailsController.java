package pl.maciejnierzwicki.mcshop.web.controller.admin.eventlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.EventService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/eventlog/details")
public class EventDetailsController {
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/{id}")
	public String showDetailsPage(@AuthenticationPrincipal User user, Model model, @PathVariable("id") Long order_id) {
		AppEvent event = eventService.getById(order_id);
		if(event != null) {
			model.addAttribute("event", event);
		}
		model.addAttribute("VIEW_FILE", "admin/eventlog/details");
		model.addAttribute("VIEW_NAME", "eventDetails");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
