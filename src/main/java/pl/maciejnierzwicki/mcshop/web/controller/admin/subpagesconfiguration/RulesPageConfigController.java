package pl.maciejnierzwicki.mcshop.web.controller.admin.subpagesconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.subpagesconfig.RulesPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageContentForm;

@Controller
@RequestMapping("/admin/subpagesconfiguration/rules")
public class RulesPageConfigController {
	
	@Autowired
	private RulesPageConfig rulesPageComponent;
	
	@ModelAttribute("rulesPageContent")
	public PageContentForm rulesPageContentForm() {
		PageContentForm form = new PageContentForm();
		form.setContent(rulesPageComponent.getContent());
		return form;
	}
	
	@GetMapping
	public String showRulesHome(@AuthenticationPrincipal User user, Model model, @ModelAttribute("rulesPageContent") PageContentForm form) {
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/rules");
		model.addAttribute("VIEW_NAME", "rules");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateRulesPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("rulesPageContent") PageContentForm form) {
		rulesPageComponent.setContent(form.getContent());
		rulesPageComponent.saveContentToFile();
		model.addAttribute("rulesPageContent", form);
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/rules");
		model.addAttribute("VIEW_NAME", "rules");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
