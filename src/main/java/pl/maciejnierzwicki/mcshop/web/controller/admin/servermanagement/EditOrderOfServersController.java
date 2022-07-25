package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import java.util.HashMap;
import java.util.Map;

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

import pl.maciejnierzwicki.mcshop.config.web.ServerListToModelAppender;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.AdminEditOrderOfElementsForm;

@Controller
@RequestMapping("/admin/servermanagement/editorder")
public class EditOrderOfServersController {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private ServerListToModelAppender serverList;
	
	@ModelAttribute(name = "serversOrder")
	public AdminEditOrderOfElementsForm serversOrder() {
		AdminEditOrderOfElementsForm form = new AdminEditOrderOfElementsForm();
		Map<Long, Integer> serversOrder = new HashMap<>();
		for(ServerConfig server : serverService.getAllOrderedByPositionHighest()) {
			serversOrder.put(server.getId(), server.getPosition());
		}
		form.setOrder(serversOrder);
		return form;
	}
	
	@GetMapping
	public String showEditOrderOfServersPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("serversOrder", serversOrder());
		model.addAttribute("VIEW_FILE", "admin/servermanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processEditOrderOfServersPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("serversOrder") @Valid AdminEditOrderOfElementsForm form, BindingResult result, Errors errors) {
		Map<Long, Integer> serversOrder = form.getOrder();
		Iterable<ServerConfig> servers = serverService.getAllOrderedByPositionHighest();
		for(ServerConfig server : servers) {
			long id = server.getId();
			if(serversOrder.containsKey(id)) {
				server.setPosition(serversOrder.get(id));
			}
		}
		serverService.saveAll(servers);
		serverList.updateServers();
		model.addAttribute("VIEW_FILE", "admin/servermanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
