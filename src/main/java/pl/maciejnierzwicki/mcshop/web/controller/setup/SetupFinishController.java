package pl.maciejnierzwicki.mcshop.web.controller.setup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.ExampleDataLoader;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.setupdata.SetupProgress;
import pl.maciejnierzwicki.mcshop.setupdata.SetupStage;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping(path = "/setup/finish")
public class SetupFinishController {
	
	private SetupProgress setupProgress;
	private MainProperties properties;
	private ExampleDataLoader exampleDataLoader;
	
	@Autowired
	public SetupFinishController(SetupProgress setupProgress, MainProperties properties, ExampleDataLoader exampleDataLoader) {
		this.setupProgress = setupProgress;
		this.properties = properties;
		this.exampleDataLoader = exampleDataLoader;
	}
	
	@GetMapping
	public String getSetupFinishView(Model model) {
		if(setupProgress.getStage() != SetupStage.FINISH) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("VIEW_FILE", "setup/finish");
		model.addAttribute("VIEW_NAME", "setup-finish");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processSetupFinish() {
		properties.setSetupMode(false);
		properties.save();
		exampleDataLoader.createExampleDataIfNone();
		setupProgress.deleteFile();
		return "redirect:/login";
	}
}
