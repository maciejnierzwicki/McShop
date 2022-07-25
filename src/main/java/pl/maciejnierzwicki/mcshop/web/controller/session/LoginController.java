package pl.maciejnierzwicki.mcshop.web.controller.session;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@GetMapping
	public String showLoginPage(Model model) {
		model.addAttribute("VIEW_FILE", "login");
		model.addAttribute("VIEW_NAME", "login");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
