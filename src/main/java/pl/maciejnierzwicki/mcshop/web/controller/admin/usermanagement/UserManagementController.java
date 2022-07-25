package pl.maciejnierzwicki.mcshop.web.controller.admin.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/usermanagement")
public class UserManagementController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String showMainPage(@AuthenticationPrincipal User user, Model model) {
		Iterable<User> users = userService.getAll();
		model.addAttribute("users", users);
		model.addAttribute("VIEW_FILE", "admin/usermanagement/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
