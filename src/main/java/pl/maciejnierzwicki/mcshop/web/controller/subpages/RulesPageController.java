package pl.maciejnierzwicki.mcshop.web.controller.subpages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.properties.SubpagesProperties;
import pl.maciejnierzwicki.mcshop.subpagesconfig.RulesPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/rules")
public class RulesPageController {
	
	private SubpagesProperties subpagesProperties;
	private RulesPageConfig rulesPageComponent;
	
	public RulesPageController(RulesPageConfig rulesPageComponent, SubpagesProperties subpagesProperties) {
		this.rulesPageComponent = rulesPageComponent;
		this.subpagesProperties = subpagesProperties;
	}
	
	@GetMapping
	public String showRulesPage(Model model) {
		if(!subpagesProperties.isRulesPageEnabled()) {
			return "redirect:/";
		}
		model.addAttribute("content", rulesPageComponent.getContent());
		model.addAttribute("VIEW_FILE", "rules");
		model.addAttribute("VIEW_NAME", "rules");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
