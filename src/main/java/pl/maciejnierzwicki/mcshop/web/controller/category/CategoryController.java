package pl.maciejnierzwicki.mcshop.web.controller.category;

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
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/category")
@Slf4j
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private ServicesService servicesService;
	
	@GetMapping(path = "/{id}/server={serverid}")
	public String getCategory(@PathVariable("id") Long id, @PathVariable("serverid") Long serverid, Model model) {
		log.debug("Selected category id: " + id + " server id: " + serverid);
		Category category = categoryService.getById(id);
		if(category != null) {
			model.addAttribute("category", category);
			
			ServerConfig server = serverService.getById(serverid);
			
			if(server != null) {
				model.addAttribute("server", server);
				Iterable<Service> services = servicesService.getAllEnabledServicesWithServerAndCategoryAndOrderedByPositionHighest(server, category);
				if(services != null) {
					model.addAttribute("services", services);
					log.debug("Loaded services.");
				}
				else { log.debug("Services null"); }
			}
			
		}
		model.addAttribute("VIEW_FILE", "category");
		model.addAttribute("VIEW_NAME", "category");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
