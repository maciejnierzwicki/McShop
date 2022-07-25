package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.config.web.ServerListToModelAppender;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.servermanagement.ServerConfigForm;

@Controller
@RequestMapping("/admin/servermanagement/newserver")
public class NewServerController {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private ServerListToModelAppender serverList;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@GetMapping
	public String showNewServerPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("serverConfig", new ServerConfigForm());
		model.addAttribute("VIEW_FILE", "admin/servermanagement/newserver");
		model.addAttribute("VIEW_NAME", "newserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processNewServer(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newServer") @Valid ServerConfigForm form, Errors errors) {
		if(!errors.hasErrors()) {
			ServerConfig server = new ServerConfig(form.getName(), form.getAddress(), form.getRconPort(), form.getRconPassword());
			serverService.save(server);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVER_CREATE_EVENT).withActor(user).withServer(server).toEvent());
			serverList.updateServers();
			return "redirect:/admin/servermanagement";
		}
		model.addAttribute("VIEW_FILE", "admin/servermanagement/newserver");
		model.addAttribute("VIEW_NAME", "newserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
