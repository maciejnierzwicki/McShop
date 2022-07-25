package pl.maciejnierzwicki.mcshop.web.controller.admin.categorymanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.service.EventMetaService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/categorymanagement/deletecategory")
public class DeleteCategoryController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	@Autowired
	private EventMetaService eventMetaService;
	
	@GetMapping
	public String showDeleteCategoryPage(@AuthenticationPrincipal User user, Model model, @RequestParam("categoryid") Long category_id) {
		Category category = categoryService.getById(category_id);
		
		if(category != null) {
			model.addAttribute("category", category);
		}
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/deletecategory");
		model.addAttribute("VIEW_NAME", "deletecategory");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDeleteCategory(@AuthenticationPrincipal User user, Model model, @RequestParam("categoryid") Long category_id) {
		Category category = categoryService.getById(category_id);
		
		if(category != null) {
			Iterable<Service> items = servicesService.getAllByCategory(category);
			
			for(Service service : items) {
				service.setCategory(null);
				servicesService.save(service);
				//TODO: Consider making it configurable to send this event here, this may create lots of entries in eventlog at quick time
				eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_UPDATE_EVENT).withService(service).toEvent());
			}
			eventMetaService.deleteCategoryData(category.getId());
			
			categoryService.delete(category);	
			eventPublisher.publishEvent(EventBuilder.forType(EventType.CATEGORY_DELETE_EVENT).withActor(user).toEvent());
		}

		return "redirect:/admin/categorymanagement";
	}

}
