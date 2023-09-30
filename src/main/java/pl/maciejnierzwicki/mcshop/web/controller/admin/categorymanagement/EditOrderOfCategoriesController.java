package pl.maciejnierzwicki.mcshop.web.controller.admin.categorymanagement;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

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

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.AdminEditOrderOfElementsForm;

@Controller
@RequestMapping("/admin/categorymanagement/editorder")
public class EditOrderOfCategoriesController {
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute(name = "catOrder")
	public AdminEditOrderOfElementsForm categoriesOrder() {
		AdminEditOrderOfElementsForm form = new AdminEditOrderOfElementsForm();
		Map<Long, Integer> order = new HashMap<>();
		for(Category category : categoryService.getAllOrderedByPositionHighest()) {
			order.put(category.getId(), category.getPosition());
		}
		form.setOrder(order);
		return form;
	}
	
	@GetMapping
	public String showEditOrderOfCategoriesPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("categories", categoryService.getAllOrderedByPositionHighest());
		model.addAttribute("catOrder", categoriesOrder());
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processEditOrderOfCategoriesPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("catOrder") @Valid AdminEditOrderOfElementsForm form, BindingResult result, Errors errors) {
		Map<Long, Integer> categoriesOrder = form.getOrder();
		Iterable<Category> categories = categoryService.getAllOrderedByPositionHighest();
		for(Category category : categories) {
			long id = category.getId();
			if(categoriesOrder.containsKey(id)) {
				category.setPosition(categoriesOrder.get(id));
			}
		}
		categoryService.saveAll(categories);
		model.addAttribute("categories", categoryService.getAllOrderedByPositionHighest());
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/editorder");
		model.addAttribute("VIEW_NAME", "editorder");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
