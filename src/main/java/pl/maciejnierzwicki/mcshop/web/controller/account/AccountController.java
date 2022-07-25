package pl.maciejnierzwicki.mcshop.web.controller.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/account")
public class AccountController {
		
	@GetMapping
	public String showAccountPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("VIEW_FILE", "account/account");
		model.addAttribute("VIEW_NAME", "account");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
