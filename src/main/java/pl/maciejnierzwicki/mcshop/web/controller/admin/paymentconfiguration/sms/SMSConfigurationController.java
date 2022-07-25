package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration.sms;

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

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSCodeForm;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSConfigForm;

@Controller
@RequestMapping("/admin/paymentconfiguration/sms")
public class SMSConfigurationController {

	@Autowired(required = false)
	private SMSConfig smsConfig;
	
	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeForm smsCodeForm() {
		return new SMSCodeForm();
	}
	
	@ModelAttribute(name = "smsForm")
	public SMSConfigForm smsForm() {
		if(smsConfig == null) { return null; }
		return smsConfig.toForm();
	}
	
	@GetMapping
	public String showSMSConfigPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("smsForm", smsForm());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processSMSConfigPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("smsForm") SMSConfigForm form, BindingResult result, Errors errors) throws Exception {
		if(smsConfig == null) {
			throw new Exception("SMS payments are disabled.");
		}
		if(!errors.hasErrors()) {
			smsConfig.apply(form);
			smsConfig.saveToFile();
		}
		
		model.addAttribute("smsForm", smsForm());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	

}
