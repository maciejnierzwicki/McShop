package pl.maciejnierzwicki.mcshop.web.controller.admin.categorymanagement;

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

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.categorymanagement.CategoryForm;

@Controller
@RequestMapping("/admin/categorymanagement/editcategory")
public class EditCategoryController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@ModelAttribute(name = "editCategory")
	public CategoryForm editCategory() {
		return new CategoryForm();
	}
	
	@GetMapping
	public String showEditUserPage(@AuthenticationPrincipal User user, Model model, @RequestParam("categoryid") Long category_id) {
		model.addAttribute("categoryid", category_id);
		Category category = categoryService.getById(category_id);
		
		if(category != null) {
			model.addAttribute("editCategory", category.toCategoryForm());
		}
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/editcategory");
		model.addAttribute("VIEW_NAME", "editcategory");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processCategoryEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editCategory") @Valid CategoryForm form, BindingResult result, Errors errors, @RequestParam("categoryid") Long category_id) {
		if(!errors.hasErrors()) {
			Category category = categoryService.getById(category_id);
			if(category != null) {
				category.setName(form.getName());
				categoryService.save(category);
				eventPublisher.publishEvent(EventBuilder.forType(EventType.CATEGORY_UPDATE_EVENT).withActor(user).withCategory(category).toEvent());
				model.addAttribute("editCategory", category.toCategoryForm());
			}
			
			else {
				model.addAttribute("editCategory", form);
			}
		}
		fillModelWithCategoryData(model, category_id);
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/editcategory");
		model.addAttribute("VIEW_NAME", "editcategory");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	private void fillModelWithCategoryData(Model model, long category_id) {
		model.addAttribute("categoryid", category_id);
	}

}
