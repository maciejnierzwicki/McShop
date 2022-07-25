package pl.maciejnierzwicki.mcshop.web.controller.admin.subpagesconfiguration;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.SubpagesProperties;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.SubpagesPropertiesForm;

@Controller
@RequestMapping("/admin/subpagesconfiguration")
@Slf4j
public class SubpagesController {
	
	@Autowired
	private SubpagesProperties subpagesProperties;
	
	@ModelAttribute("subagesForm")
	public SubpagesPropertiesForm subpagesForm() {
		return subpagesProperties.toForm();
	}
	
	
	@GetMapping
	public String showSubpagesHome(@AuthenticationPrincipal User user, Model model, @ModelAttribute("subpagesForm") SubpagesPropertiesForm subpagesForm) {
		model.addAttribute("subpagesForm", subpagesForm());
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateSubpagesSettings(@AuthenticationPrincipal User user, Model model, @ModelAttribute("subpagesForm") @Valid SubpagesPropertiesForm subpagesForm, BindingResult result, Errors errors) {
		if(!errors.hasErrors()) {
			subpagesProperties.apply(subpagesForm);
			subpagesProperties.save();
		}
		model.addAttribute("subpagesForm", subpagesForm);
		model.addAttribute("VIEW_FILE", "admin/subpagesconfiguration/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
