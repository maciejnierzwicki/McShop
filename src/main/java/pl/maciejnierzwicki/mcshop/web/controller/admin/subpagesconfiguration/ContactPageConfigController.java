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
import pl.maciejnierzwicki.mcshop.subpagesconfig.ContactPageConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageContentForm;

@Controller
@RequestMapping("/admin/subpagesconfiguration/contact")
public class ContactPageConfigController {
	
	@Autowired
	private ContactPageConfig contactPageComponent;
	
	@ModelAttribute("contactPageContent")
	public PageContentForm contactPageContentForm() {
		PageContentForm form = new PageContentForm();
		form.setContent(contactPageComponent.getContent());
		return form;
	}

	@GetMapping
	public String showContactHome(@AuthenticationPrincipal User user, Model model, @ModelAttribute("contactPageContent") PageContentForm form) {
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/contact");
		model.addAttribute("VIEW_NAME", "contact");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateContactPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("contactPageContent") PageContentForm form) {
		contactPageComponent.setContent(form.getContent());
		contactPageComponent.saveContentToFile();
		model.addAttribute("contactPageContent", form);
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/contact");
		model.addAttribute("VIEW_NAME", "contact");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
