package pl.maciejnierzwicki.mcshop.web.controller.admin.usermanagement;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/admin/usermanagement/newuser")
public class NewUserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute(name = "newUser")
	public NewUserForm newUser() {
		return new NewUserForm();
	}
	
	@GetMapping
	public String showNewUserPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("VIEW_FILE", "admin/usermanagement/newuser");
		model.addAttribute("VIEW_NAME", "newuser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processNewUser(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newUser") @Valid NewUserForm form, Errors errors) {
		model.addAttribute("VIEW_FILE", "admin/usermanagement/newuser");
		model.addAttribute("VIEW_NAME", "newuser");
		if(!errors.hasErrors()) {
			User new_user = form.toUser(passwordEncoder);
			if(userService.getByUsername(new_user.getUsername()) == null) {
				Optional<Role> op_default_role = roleRepo.findById("USER");
				
				if(op_default_role.isPresent()) {
					Role role = op_default_role.get();
					Set<Role> roles = new HashSet<>();
					roles.add(role);
					new_user.setRoles(roles);
				}
				userService.save(new_user);
			}
			
			else { 
				model.addAttribute("userexists", true); 
				Iterable<User> users = userService.getAll();
				model.addAttribute("users", users); 
				return ControllerCommons.ROOT_VIEW_FILE_NAME;
			}
		}
		
		if(userService.getByUsername(form.getUsername()) != null) {
			model.addAttribute("userexists", true);
		}
		
		Iterable<User> users = userService.getAll();
		
		model.addAttribute("users", users);
		
		if(!errors.hasErrors()) {
			return "redirect:/admin/usermanagement";
		}
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
