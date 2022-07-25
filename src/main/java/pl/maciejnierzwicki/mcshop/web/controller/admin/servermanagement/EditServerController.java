package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("/admin/servermanagement/editserver")
public class EditServerController {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private ServerListToModelAppender serverList;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@GetMapping
	public String showEditServerPage(@AuthenticationPrincipal User user, Model model, @RequestParam("serverid") Long server_id) {
		model.addAttribute("serverid", server_id);
		ServerConfig server = serverService.getById(server_id);
		
		if(server != null) {
			model.addAttribute("editServer", server.toServerConfigForm());
		}
		model.addAttribute("VIEW_FILE", "admin/servermanagement/editserver");
		model.addAttribute("VIEW_NAME", "editserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processServerEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editServer") @Valid ServerConfigForm form, BindingResult result, Errors errors, @RequestParam("serverid") Long server_id) {
		if(!errors.hasErrors()) {
			ServerConfig server = serverService.getById(server_id);
			if(server != null) {
				server.setName(form.getName());
				server.setAddress(form.getAddress());
				server.setRconPort(form.getRconPort());
				server.setRconPassword(form.getRconPassword());
				serverService.save(server);
				eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVER_UPDATE_EVENT).withActor(user).withServer(server).toEvent());
				model.addAttribute("editServer", server.toServerConfigForm());
				serverList.updateServers();
			}
			else {
				model.addAttribute("editServer", form);
			}
		}
		fillModelWithServerData(model, server_id);
		model.addAttribute("VIEW_FILE", "admin/servermanagement/editserver");
		model.addAttribute("VIEW_NAME", "editserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	private void fillModelWithServerData(Model model, long server_id) {
		model.addAttribute("serverid", server_id);
	}

}
