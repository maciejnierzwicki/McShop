package pl.maciejnierzwicki.mcshop.web.controller.admin.eventlog;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.EventService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageForm;

@Controller
@RequestMapping("/admin/eventlog")
@Slf4j
public class EventLogController {
	
	@Autowired
	private EventService eventService;
	@Autowired
	private MainProperties properties;
	
	@ModelAttribute(name = "pageForm")
	public PageForm pageForm() {
		return new PageForm();
	}
	
	@RequestMapping(value = "", method = { RequestMethod.GET, RequestMethod.POST} )
	public String showPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("pageForm") @Valid PageForm pageForm) {
		List<AppEvent> events = eventService.getAllEventsFromNewest(PageRequest.of(pageForm.getPage() - 1, properties.getMaxEventsPerPage()));
		long all_events = eventService.countAll();
		long all_pages = all_events / properties.getMaxEventsPerPage();
		if(all_events % properties.getMaxEventsPerPage() > 0) {
			all_pages += 1;
		}
		model.addAttribute("events", events);
		model.addAttribute("pageForm", pageForm);
		model.addAttribute("allPages", all_pages);
		model.addAttribute("VIEW_FILE", "admin/eventlog/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}