package pl.maciejnierzwicki.mcshop.web.controller.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.repository.ServerRepository;

@Controller
@RequestMapping("/serversColumn")
public class ServersListController {
	
	@Autowired
	private ServerRepository serverRepo;
	
	@GetMapping
	public String get(Model model) {
		model.addAttribute("servers", serverRepo.findByOrderByPositionAsc());
		return "fragments/serversColumn";
	}

}
