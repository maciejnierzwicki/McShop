package pl.maciejnierzwicki.mcshop.web.controller.setup;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.setupdata.SetupProgress;
import pl.maciejnierzwicki.mcshop.setupdata.SetupStage;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.MainPropertiesForm;

@Controller
@RequestMapping(path = "/setup/appsettings")
public class SetupAppSettingsController {
	
	private SetupProgress setupProgress;
	private MainProperties properties;
	
	@Autowired
	public SetupAppSettingsController(SetupProgress setupProgress, MainProperties properties) {
		this.setupProgress = setupProgress;
		this.properties = properties;
	}

	@ModelAttribute(name = "propertiesForm")
	public MainPropertiesForm propertiesForm() {
		return properties.toForm();
	}
	
	@GetMapping
	public String getAppSettingsSetupView(Model model) {
		if(setupProgress.getStage() != SetupStage.APP_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("VIEW_FILE", "setup/appsettings");
		model.addAttribute("VIEW_NAME", "setup-appsettings");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processAppSettingsSetup(Model model, @ModelAttribute(name = "propertiesForm") @Valid MainPropertiesForm form, BindingResult result, Errors errors) {
		if(errors.hasErrors()) {
			model.addAttribute("VIEW_FILE", "setup/appsettings");
			model.addAttribute("VIEW_NAME", "setup-appsettings");
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		properties.apply(form);
		properties.save();
		setupProgress.setStage(SetupStage.ADMIN_USER_CONFIG);
		return "redirect:/setup/adminuser";
	}
		
	

}
