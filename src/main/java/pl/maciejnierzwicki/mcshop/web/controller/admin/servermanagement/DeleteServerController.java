package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.config.web.ServerListToModelAppender;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.EventMetaService;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/servermanagement/deleteserver")
public class DeleteServerController {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private EventMetaService eventMetaService;
	@Autowired
	private ServerListToModelAppender serverList;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@GetMapping
	public String showDeleteServerPage(@AuthenticationPrincipal User user, Model model, @RequestParam("serverid") Long server_id) {
		ServerConfig server = serverService.getById(server_id);
		
		if(server != null) {
			model.addAttribute("server", server);
		}
		model.addAttribute("VIEW_FILE", "admin/servermanagement/deleteserver");
		model.addAttribute("VIEW_NAME", "deleteserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDeleteServer(@AuthenticationPrincipal User user, Model model, @RequestParam("serverid") Long server_id) {
		ServerConfig server = serverService.getById(server_id);
		
		if(server != null) {
			Iterable<Service> services = servicesService.getAllByServer(server);
			
			for(Service service : services) {
				service.setServer(null);
				servicesService.save(service);
				//TODO: Consider making it configurable to send this event here, this may create lots of entries in eventlog at quick time
				eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_UPDATE_EVENT).toEvent());
			}
			eventMetaService.deleteServerData(server.getId());
			
			serverService.delete(server);	
			eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVER_DELETE_EVENT).withActor(user).toEvent());
			serverList.updateServers();
		}

		return "redirect:/admin/servermanagement";
	}

}
