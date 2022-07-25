package pl.maciejnierzwicki.mcshop.web.controller.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.setupdata.SetupProgress;
import pl.maciejnierzwicki.mcshop.setupdata.SetupStage;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;


@Controller
@RequestMapping(path = "/setup")
public class SetupController {
    
    private SetupProgress setupProgress;
	
	@Autowired
	public SetupController(SetupProgress setupProgress) {
		this.setupProgress = setupProgress;
	}
	
	@GetMapping
	public String getSetupView(Model model) {
		if(setupProgress.getStage() != SetupStage.WAITING_LOGIN) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("setupprogress", setupProgress);
		model.addAttribute("VIEW_FILE", "setup/setup");
		model.addAttribute("VIEW_NAME", "setup");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processLogin(Model model) {
	    if(setupProgress.getStage() == SetupStage.WAITING_LOGIN) {
	    	setupProgress.setStage(SetupStage.DATABASE_CONFIG);
	    }
		return "redirect:/setup/database";
	}
		
	

}
