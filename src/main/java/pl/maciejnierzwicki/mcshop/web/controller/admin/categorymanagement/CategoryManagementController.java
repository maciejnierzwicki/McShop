package pl.maciejnierzwicki.mcshop.web.controller.admin.categorymanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/categorymanagement")
public class CategoryManagementController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public String showMainPage(@AuthenticationPrincipal User user, Model model) {
		Iterable<Category> categories = categoryService.getAll();
		model.addAttribute("categories", categories);
		model.addAttribute("VIEW_FILE", "admin/categorymanagement/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
