package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

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
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.utils.MCServerUtils;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement.TestServiceForm;

@Controller
@RequestMapping("/admin/servicemanagement/testservice")
public class TestServiceController {
	
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private MainProperties properties;
	
	@ModelAttribute(name = "testService")
	public TestServiceForm testService() {
		return new TestServiceForm();
	}
	
	@GetMapping
	public String showTestServicePage(@AuthenticationPrincipal User user, Model model, @RequestParam("serviceid") Long service_id) {
		Service service = servicesService.getById(service_id);
		
		if(service != null) {
			model.addAttribute("service", service);
		}
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/testservice");
		model.addAttribute("VIEW_NAME", "testservice");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processTestService(@AuthenticationPrincipal User user, Model model, @ModelAttribute("testService") @Valid TestServiceForm form, BindingResult result, Errors errors, @RequestParam("serviceid") Long service_id) {
		String playername = form.getPlayername();
		
		Service service = servicesService.getById(service_id);
		if(service != null) {
			model.addAttribute("service", service);
			ServerConfig config = service.getServer();
			MCServerUtils.sendCommand(config, "say MCShop SERVICE TEST: " + service.getName());
			MCServerUtils.sendCommands(config, service.getCommands(), playername, properties.getPlayerPlaceholder());
		}
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/testservice");
		model.addAttribute("VIEW_NAME", "testservice");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
