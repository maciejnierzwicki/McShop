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
import pl.maciejnierzwicki.mcshop.subpagesconfig.AboutPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageContentForm;

@Controller
@RequestMapping("/admin/subpagesconfiguration/about")
public class AboutPageConfigController {
	
	@Autowired
	private AboutPageConfig aboutPageComponent;
	
	@ModelAttribute("aboutPageContent")
	public PageContentForm aboutPageContentForm() {
		PageContentForm form = new PageContentForm();
		form.setContent(aboutPageComponent.getContent());
		return form;
	}
	
	@GetMapping
	public String showAboutHome(@AuthenticationPrincipal User user, Model model, @ModelAttribute("aboutPageContent") PageContentForm form) {
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/about");
		model.addAttribute("VIEW_NAME", "about");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateAboutPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("aboutPageContent") PageContentForm form) {
		aboutPageComponent.setContent(form.getContent());
		aboutPageComponent.saveContentToFile();
		model.addAttribute("aboutPageContent", form);
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/about");
		model.addAttribute("VIEW_NAME", "about");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
