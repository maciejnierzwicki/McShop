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

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSCodeForm;

@Controller
@RequestMapping("/admin/paymentconfiguration/sms/codes/add")
public class AddSMSCodeController {
	
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private SMSConfig smsConfig;
	
	@ModelAttribute(name = "smsCodeForm")
	public SMSCodeForm smsCodeForm() {
		return new SMSCodeForm();
	}
	
	@GetMapping
	public String showAddSMSCodePage(@AuthenticationPrincipal User user, Model model) {
		if(smsConfig == null || smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		model.addAttribute("smsCodeForm", smsCodeForm());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/addcode");
		model.addAttribute("VIEW_NAME", "addcode");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processddSMSCodePage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("smsCodeForm") SMSCodeForm form, BindingResult result, Errors errors) {
		if(smsConfig == null || smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		if(!errors.hasErrors()) {
			SMSCode code = new SMSCode(form.getPriceNet(), form.getPhoneNumber(), form.getMessage(), form.getFundsToAdd());
			code.setProviderName(smsConfig.getProviderName());
			code = smsCodesService.save(code);
			return "redirect:/admin/paymentconfiguration/sms/codes";	
		}
		model.addAttribute("smsCodeForm", form);
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/addcode");
		model.addAttribute("VIEW_NAME", "addcode");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
