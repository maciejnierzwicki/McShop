package pl.maciejnierzwicki.mcshop.web.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;


@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping
	public String getHome(Model model) {
		model.addAttribute("VIEW_FILE", "home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
