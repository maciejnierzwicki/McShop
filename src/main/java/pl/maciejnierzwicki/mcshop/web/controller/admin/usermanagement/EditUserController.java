package pl.maciejnierzwicki.mcshop.web.controller.admin.usermanagement;

import java.util.HashSet;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.RoleService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement.AdminEditUserForm;
import pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement.AdminEditUserPasswordForm;

@Controller
@RequestMapping("/admin/usermanagement/edituser")
public class EditUserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute(name = "editUser")
	public AdminEditUserForm editUser() {
		return new AdminEditUserForm();
	}
	
	@ModelAttribute(name = "editUserPassword")
	public AdminEditUserPasswordForm editUserPassword() {
		return new AdminEditUserPasswordForm();
	}
	
	@GetMapping
	public String showEditUserPage(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		model.addAttribute("userid", user_id);
		User selected_user = userService.getById(user_id);
		
		if(selected_user != null) {
			model.addAttribute("editUser", selected_user.toAdminEditUserForm());
			model.addAttribute("editUserPassword", editUserPassword());
		}
		
		Iterable<Role> all_roles = roleService.getAll();
		model.addAttribute("all_roles", all_roles);
		model.addAttribute("VIEW_FILE", "admin/usermanagement/edituser");
		model.addAttribute("VIEW_NAME", "edituser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processForumUsersEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editUser") @Valid AdminEditUserForm form, BindingResult result, Errors errors, @RequestParam("userid") Long user_id) {
		if(!errors.hasErrors()) {
			User selected_user = userService.getById(user_id);
			if(selected_user != null) {
				User claimed_name_user = userService.getByUsername(form.getUsername());
				if(claimed_name_user != null && !claimed_name_user.getId().equals(selected_user.getId())) {
					model.addAttribute("nameclaimed", true);
					form.setUsername(selected_user.getUsername());
				}
				else {
					selected_user.setUsername(form.getUsername());
					Set<Role> roles = new HashSet<>();
					for(String id : form.getActiveRoles()) {
						Role role = roleService.getById(id);
						if(role != null) {
							roles.add(role);
						}
					}
					selected_user.setRoles(roles);
					selected_user.setMoney(form.getMoney());
					if(selected_user.getId() == user.getId()) {
						user.setMoney(form.getMoney());
						user.setRoles(roles);
						userService.save(user);
					}
					userService.save(selected_user);
					model.addAttribute("editUser", selected_user.toAdminEditUserForm());
				}
			}
			
			else {
				model.addAttribute("editUser", form);
			}
		}
		fillModelWithUserAndAllRolesData(model, user_id);
		model.addAttribute("VIEW_FILE", "admin/usermanagement/edituser");
		model.addAttribute("VIEW_NAME", "edituser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping("/password")
	public String processForumUsersEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editUserPassword") @Valid AdminEditUserPasswordForm form, BindingResult result, Errors errors, @RequestParam("userid") Long user_id) {
		User selected_user = userService.getById(user_id);
		if(!errors.hasErrors() && selected_user != null) {
			selected_user.setPassword(passwordEncoder.encode(form.getPassword()));
			userService.save(selected_user);
			model.addAttribute("editUser", selected_user.toAdminEditUserForm());
		}
		fillModelWithUserAndAllRolesData(model, user_id);
		model.addAttribute("VIEW_FILE", "admin/usermanagement/edituser");
		model.addAttribute("VIEW_NAME", "edituser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	private void fillModelWithUserAndAllRolesData(Model model, long user_id) {
		model.addAttribute("userid", user_id);
		Iterable<Role> all_roles = roleService.getAll();
		model.addAttribute("all_roles", all_roles);
	}

}
