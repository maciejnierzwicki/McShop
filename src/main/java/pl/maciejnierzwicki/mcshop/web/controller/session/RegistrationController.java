package pl.maciejnierzwicki.mcshop.web.controller.session;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.repository.RoleRepository;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement.NewUserForm;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepo;
	
	@ModelAttribute("registration")
	public NewUserForm registration() {
		return new NewUserForm();
	}
	
	@GetMapping
	public String showRegistration(@ModelAttribute("registration") NewUserForm form, Model model) {
		model.addAttribute("VIEW_FILE", "register");
		model.addAttribute("VIEW_NAME", "register");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processRegistration(@Valid @ModelAttribute("registration") NewUserForm form, Errors errors, Model model) {
		model.addAttribute("VIEW_FILE", "register");
		model.addAttribute("VIEW_NAME", "register");
		if(errors.hasErrors()) {
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		
		User user = userService.getByUsername(form.getUsername());

		if(user != null) {
			model.addAttribute("userexists", true);
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		
		user = form.toUser(passwordEncoder);
		
		Optional<Role> op_default_role = roleRepo.findById("USER");
		
		if(op_default_role.isPresent()) {
			Role role = op_default_role.get();
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			user.setRoles(roles);
		}
		
		userService.save(user);
		return "redirect:/login";
	}
}
