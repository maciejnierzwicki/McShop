package pl.maciejnierzwicki.mcshop.web.controller.admin.categorymanagement;

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

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.categorymanagement.CategoryForm;

@Controller
@RequestMapping("/admin/categorymanagement/newcategory")
public class NewCategoryController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@ModelAttribute(name = "newCategory")
	public CategoryForm newCategory() {
		return new CategoryForm();
	}
	
	@GetMapping
	public String showNewCategoryPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/newcategory");
		model.addAttribute("VIEW_NAME", "newcategory");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processNewCategory(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newCategory") @Valid CategoryForm form, Errors errors) {
		if(!errors.hasErrors()) {
			Category category = new Category();
			category.setName(form.getName());
			if(categoryService.getByName(category.getName()) == null) {
				categoryService.save(category);
				eventPublisher.publishEvent(EventBuilder.forType(EventType.CATEGORY_CREATE_EVENT).withActor(user).withCategory(category).toEvent());
			}
			
			else { model.addAttribute("categoryexists", true); 
				Iterable<Category> categories = categoryService.getAll();
				model.addAttribute("categories", categories);
				model.addAttribute("VIEW_FILE", "admin/categorymanagement/home");
				model.addAttribute("VIEW_NAME", "home");
				return ControllerCommons.ROOT_VIEW_FILE_NAME;
			}
		}
		
		if(categoryService.getByName(form.getName()) != null) {
			model.addAttribute("categoryexists", true);
		}
		
		Iterable<Category> categories = categoryService.getAll();
		
		model.addAttribute("categories", categories);
		
		if(!errors.hasErrors()) {
			return "redirect:/admin/categorymanagement";
		}
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/newcategory");
		model.addAttribute("VIEW_NAME", "newcategory");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
