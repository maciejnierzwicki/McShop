package pl.maciejnierzwicki.mcshop.web.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	@GetMapping
	public String showAdminPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("VIEW_FILE", "admin/admin");
		model.addAttribute("VIEW_NAME", "admin");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
