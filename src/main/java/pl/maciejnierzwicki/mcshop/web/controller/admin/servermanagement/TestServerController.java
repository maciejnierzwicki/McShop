package pl.maciejnierzwicki.mcshop.web.controller.admin.servermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.utils.MCServerUtils;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/servermanagement/testserver")
public class TestServerController {
	
	@Autowired
	private ServerService serverService;
	
	@GetMapping
	public String showMainPageWithTestResult(@AuthenticationPrincipal User user, Model model, @RequestParam("serverid") Long server_id) {
		ServerConfig server = serverService.getById(server_id);
		if(server != null) {
			model.addAttribute("serverConfig", server);
			boolean success = MCServerUtils.testConnect(server);
			model.addAttribute("success", success);
		}
		
		model.addAttribute("VIEW_FILE", "admin/servermanagement/testserver");
		model.addAttribute("VIEW_NAME", "testserver");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
