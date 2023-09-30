package pl.maciejnierzwicki.mcshop.web.controller.admin;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

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

import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.repository.ServiceRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.MainPropertiesForm;

@Controller
@RequestMapping("/admin/general")
public class GeneralController {
	
	private ServiceRepository serviceRepo;
	private MainProperties properties;
	
	@Autowired
	public GeneralController(ServiceRepository serviceRepo, MainProperties properties) {
		this.serviceRepo = serviceRepo;
		this.properties = properties;
	}
	
	@ModelAttribute(name = "propertiesForm")
	public MainPropertiesForm propertiesForm() {
		return properties.toForm();
	}
	
	
	@GetMapping
	public String showAdminPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("propertiesForm") MainPropertiesForm propertiesForm) {
		model.addAttribute("VIEW_FILE", "admin/general");
		model.addAttribute("VIEW_NAME", "general");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateForumSettings(@AuthenticationPrincipal User user, Model model, @ModelAttribute("propertiesForm") @Valid MainPropertiesForm propertiesForm, BindingResult result, Errors errors, HttpServletRequest request, HttpServletResponse response) {
		if(!errors.hasErrors()) {
			if(propertiesForm.isPlayerPlaceholderUpdateServices()) {
				String old_player_placeholder = properties.getPlayerPlaceholder();
				String new_player_placeholder = propertiesForm.getPlayerPlaceholder();
				if(!new_player_placeholder.equals(old_player_placeholder)) {
					Iterable<Service> services = serviceRepo.findAll();
					for(Service service : services) {
						List<String> commands = service.getCommands();
						for(int i = 0; i < commands.size(); i++) {
							String cmd = commands.get(i);
							cmd = cmd.replaceAll(old_player_placeholder, new_player_placeholder);
							commands.set(i, cmd);
						}
						service.setCommands(commands);
					}
					serviceRepo.saveAll(services);
				}
			}
			properties.apply(propertiesForm);
			properties.save();
		}
		
		model.addAttribute("VIEW_FILE", "admin/general");
		model.addAttribute("VIEW_NAME", "general");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
