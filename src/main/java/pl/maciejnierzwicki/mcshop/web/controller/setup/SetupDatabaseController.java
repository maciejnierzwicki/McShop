package pl.maciejnierzwicki.mcshop.web.controller.setup;


import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.McShopApplication;
import pl.maciejnierzwicki.mcshop.properties.DatabaseProperties;
import pl.maciejnierzwicki.mcshop.properties.DatabaseProperties.DatabaseType;
import pl.maciejnierzwicki.mcshop.setupdata.SetupProgress;
import pl.maciejnierzwicki.mcshop.setupdata.SetupStage;
import pl.maciejnierzwicki.mcshop.utils.DatabaseUtil;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.setup.SetupDatabaseForm;

@Controller
@RequestMapping(path = "/setup/database")
@Slf4j
public class SetupDatabaseController {
	
	private DatabaseProperties dataSourceConfig;
	private ThreadPoolTaskScheduler scheduler;
	private SetupProgress setupProgress;
	
	@Autowired
	public SetupDatabaseController(DatabaseProperties dataSourceConfig, ThreadPoolTaskScheduler scheduler, SetupProgress setupProgress) {
		this.dataSourceConfig = dataSourceConfig;
		this.scheduler = scheduler;
		this.setupProgress = setupProgress;
	}
	
	@ModelAttribute("setupDatabase")
	public SetupDatabaseForm setupDatabase() {
		return new SetupDatabaseForm();
	}
	
	@GetMapping
	public String getSetupProcessView(Model model) {
		if(setupProgress.getStage() != SetupStage.DATABASE_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		model.addAttribute("VIEW_FILE", "setup/database");
		model.addAttribute("VIEW_NAME", "setup-database");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDatabaseSetup(Model model, @ModelAttribute(name = "setupDatabase") @Valid SetupDatabaseForm form, Errors errors) {
		if(setupProgress.getStage() != SetupStage.DATABASE_CONFIG) {
			return "redirect:" + setupProgress.getCurrentStageUrlPath();
		}
		if(errors.hasErrors()) {
			model.addAttribute("VIEW_FILE", "setup/database");
			model.addAttribute("VIEW_NAME", "setup-database");
			return ControllerCommons.ROOT_VIEW_FILE_NAME;
		}
		if(form.getDbtype() == DatabaseType.MYSQL) {
			boolean valid_connection = DatabaseUtil.testMySQLConnection(form.getDbaddress(), form.getDbname(), form.getDbport(), form.getDbusername(), form.getDbpassword());
			if(!valid_connection) {
				model.addAttribute("connection_error", true);
				model.addAttribute("VIEW_FILE", "setup/database");
				model.addAttribute("VIEW_NAME", "setup-database");
				return ControllerCommons.ROOT_VIEW_FILE_NAME;
			}
		}
		dataSourceConfig.apply(form);
		dataSourceConfig.save();
		setupProgress.setStage(SetupStage.APP_CONFIG);
		try {
			setupProgress.saveToFile();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		scheduler.schedule(() -> { McShopApplication.restart(); }, new Date(System.currentTimeMillis() + 500));
		model.addAttribute("VIEW_FILE", "setup/database-restart");
		model.addAttribute("VIEW_NAME", "setup-database-restart");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
		
	

}
