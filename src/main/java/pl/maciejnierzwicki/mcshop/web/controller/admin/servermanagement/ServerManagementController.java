package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/servermanagement")
public class ServerManagementController {

	@GetMapping
	public String showMainPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("VIEW_FILE", "admin/servermanagement/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
