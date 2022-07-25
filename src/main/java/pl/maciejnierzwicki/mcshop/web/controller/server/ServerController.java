package pl.maciejnierzwicki.mcshop.web.controller.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.repository.ServerRepository;
import pl.maciejnierzwicki.mcshop.repository.ServiceRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/server")
@Slf4j
public class ServerController {

	private ServerRepository serverRepo;
	private ServiceRepository itemRepo;
	
	@Autowired
	public ServerController(ServiceRepository itemRepo, ServerRepository serverRepo) {
		this.itemRepo = itemRepo;
		this.serverRepo = serverRepo;
	}
	
	
	@GetMapping(path = "/{id}")
	public String getCategory(@PathVariable("id") Long id, Model model) {
		log.debug("Selected server id: " + id);
		Optional<ServerConfig> op_server = serverRepo.findById(id);
		if(op_server.isPresent()) {
			ServerConfig server = op_server.get();
			model.addAttribute("server", server);
			
			List<Category> categories = getCategoriesWithAtLeastOneServiceOnServer(server);
			
			if(!categories.isEmpty()) {
				log.debug("Loaded categories.");
				model.addAttribute("categories", categories);
			}
			else {
				log.debug("Categories list empty");
			}
			
		}
		model.addAttribute("VIEW_FILE", "server");
		model.addAttribute("VIEW_NAME", "server");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	private List<Category> getCategoriesWithAtLeastOneServiceOnServer(ServerConfig serverConfig) {
		Iterable<Service> services = itemRepo.findAllByServer(serverConfig);
		List<Category> categories = new ArrayList<>();
		for(Service service : services) {
			Category cat = service.getCategory();
			if(cat == null) { continue; }
			if(!categories.contains(cat)) {
				categories.add(cat);
			}
		}
		categories.sort((cat1, cat2) -> {
			if(cat1.getPosition() > cat2.getPosition()) return 1;
			else if(cat1.getPosition() < cat2.getPosition()) return -1;
			else return 0;
		});
		log.debug(categories.size() + " categories with at least one service found on server " + serverConfig.getName());
		return categories;
	}
}
