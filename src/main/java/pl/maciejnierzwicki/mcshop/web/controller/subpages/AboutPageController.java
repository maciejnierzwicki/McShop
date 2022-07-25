package pl.maciejnierzwicki.mcshop.web.controller.subpages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.properties.SubpagesProperties;
import pl.maciejnierzwicki.mcshop.subpagesconfig.AboutPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/about")
public class AboutPageController {
	
	private SubpagesProperties subpagesProperties;
	private AboutPageConfig aboutPageComponent;
	
	public AboutPageController(AboutPageConfig aboutPageComponent, SubpagesProperties subpagesProperties) {
		this.aboutPageComponent = aboutPageComponent;
		this.subpagesProperties = subpagesProperties;
	}
	
	@GetMapping
	public String showContactPage(Model model) {
		if(!subpagesProperties.isAboutPageEnabled()) {
			return "redirect:/";
		}
		model.addAttribute("content", aboutPageComponent.getContent());
		model.addAttribute("VIEW_FILE", "about");
		model.addAttribute("VIEW_NAME", "about");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
