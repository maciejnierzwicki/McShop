package pl.maciejnierzwicki.mcshop.web.controller.subpages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.properties.SubpagesProperties;
import pl.maciejnierzwicki.mcshop.subpagesconfig.ContactPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/contact")
public class ContactPageController {
	
	private SubpagesProperties subpagesProperties;
	private ContactPageConfig contactPageComponent;
	
	public ContactPageController(ContactPageConfig contactPageComponent, SubpagesProperties subpagesProperties) {
		this.contactPageComponent = contactPageComponent;
		this.subpagesProperties = subpagesProperties;
	}
	
	@GetMapping
	public String showContactPage(Model model) {
		if(!subpagesProperties.isContactPageEnabled()) {
			return "redirect:/";
		}
		model.addAttribute("content", contactPageComponent.getContent());
		model.addAttribute("VIEW_FILE", "contact");
		model.addAttribute("VIEW_NAME", "contact");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
