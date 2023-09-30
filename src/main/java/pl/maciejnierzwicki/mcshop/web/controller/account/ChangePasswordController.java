package pl.maciejnierzwicki.mcshop.web.controller.account;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.account.EditPasswordForm;

@Controller
@RequestMapping("/account/changepassword")
public class ChangePasswordController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	
	@ModelAttribute(name = "editPassword")
	public EditPasswordForm editPassword() {
		return new EditPasswordForm();
	}
	
	@GetMapping
	public String showChangePasswordPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("editPassword", editPassword());
		model.addAttribute("VIEW_FILE", "account/changepassword");
		model.addAttribute("VIEW_NAME", "changepassword");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	
	@PostMapping
	public String processPasswordChange(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editPassword") @Valid EditPasswordForm form, BindingResult result, Errors errors) {
		if(!errors.hasErrors()) {
			user.setPassword(passwordEncoder.encode(form.getNewPassword()));
			userService.save(user);
			model.addAttribute("success", true);
		}
		model.addAttribute("VIEW_FILE", "account/changepassword");
		model.addAttribute("VIEW_NAME", "changepassword");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
