package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration.sms.codes;

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

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSCodeForm;

@Controller
@RequestMapping("/admin/paymentconfiguration/sms/codes/edit")
public class EditSMSCodeController {
	
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private SMSConfig smsConfig;
	
	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeForm smsCodeForm() {
		return new SMSCodeForm();
	}
	
	@GetMapping
	public String showEditSMSCodePage(@AuthenticationPrincipal User user, Model model, @RequestParam("codeid") Long id) {
		if(smsConfig == null || smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		SMSCode sms_code = smsCodesService.getById(id);
		if(sms_code != null) {
			model.addAttribute("smsCodeForm", sms_code.toForm());
		}
		model.addAttribute("id", id);
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/editcode");
		model.addAttribute("VIEW_NAME", "editcode");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processEditSMSCodePage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("smsCodeForm") SMSCodeForm form, BindingResult result, Errors errors, @RequestParam("codeid") Long id) {
		if(smsConfig == null || smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		if(!errors.hasErrors()) {
			SMSCode sms_code = smsCodesService.getById(id);
			if(sms_code != null) {
				sms_code.apply(form, smsConfig.getProviderName());
				sms_code = smsCodesService.save(sms_code);
			}
			return "redirect:/admin/paymentconfiguration/sms/codes";	
		}
		model.addAttribute("smsCodeForm", form);
		model.addAttribute("id", id);
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/editcode");
		model.addAttribute("VIEW_NAME", "editcode");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
