package pl.maciejnierzwicki.mcshop.web.controller.setup;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.repository.RoleRepository;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.setupdata.SetupProgress;
import pl.maciejnierzwicki.mcshop.setupdata.SetupStage;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement.NewUserForm;

@Controller
@RequestMapping(path = "/setup/adminuser")
public class SetupAdminAccountController {
	
	private PasswordEncoder passwordEncoder;
	private SetupProgress setupProgress;
	private UserService userService;
	private RoleRepository roleRepo;
	
	@Autowired
	public SetupAdminAccountController(UserService userService, RoleRepository roleRepo, PasswordEncoder passwordEncoder, SetupProgress setupProgress) {
		this.passwordEncoder = passwordEncoder;
		this.setupProgress = setupProgress;
		this.userService = userService;
		this.roleRepo = roleRepo;
	}
	
	@ModelAttribute("registration")
	public NewUserForm registration() {
		return new NewUserForm();
	}
	
	@GetMapping
	public String getAdminAccountSetupView(@ModelAttribute("registration") NewUserForm form, Model model) {
		if(setupProgress.getStage() != SetupStage.ADMIN_USER_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("VIEW_FILE", "setup/adminuser");
		model.addAttribute("VIEW_NAME", "setup-adminuser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processAdminAccountSetup(Model model, @Valid @ModelAttribute("registration") NewUserForm form, BindingResult result, Errors errors) {
		if(errors.hasErrors()) {
			model.addAttribute("VIEW_FILE", "setup/adminuser");
			model.addAttribute("VIEW_NAME", "setup-adminuser");
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		
		User admin_user = userService.getByUsername(form.getUsername());
		if(admin_user != null) {
			model.addAttribute("userexists", true);
			model.addAttribute("VIEW_FILE", "setup/adminuser");
			model.addAttribute("VIEW_NAME", "setup-adminuser");
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		admin_user = form.toUser(passwordEncoder);
		
		Optional<Role> op_admin_role = roleRepo.findById("ADMIN");
		if(op_admin_role.isPresent()) {
			Role role = op_admin_role.get();
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			admin_user.setRoles(roles);
		}
		userService.save(admin_user);
		setupProgress.setStage(SetupStage.FINISH);
		return "redirect:/setup/finish";
	}
		
	

}
