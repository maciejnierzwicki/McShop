package pl.maciejnierzwicki.mcshop.web.controller.admin.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.EventMetaService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.UserService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/usermanagement/deleteuser")
public class DeleteUserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EventMetaService eventMetaService;
	@Autowired
	private OrderService orderService;

	@GetMapping
	public String showDeleteUserPage(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		User selected_user = userService.getById(user_id);
		
		if(selected_user != null) {
			model.addAttribute("user", selected_user);
		}
		model.addAttribute("VIEW_FILE", "admin/usermanagement/deleteuser");
		model.addAttribute("VIEW_NAME", "deleteuser");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDeleteUser(@AuthenticationPrincipal User user, Model model, @RequestParam("userid") Long user_id) {
		User selected_user = userService.getById(user_id);
		
		if(selected_user != null) {
			if(!selected_user.getId().equals(user.getId())) {
				eventMetaService.deleteUserData(user_id);
				eventMetaService.deleteActorData(user_id);
				orderService.deleteUserData(user_id);
				userService.delete(selected_user);	
			}
		}
		return "redirect:/admin/usermanagement";
	}
}
