package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration.sms.codes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/paymentconfiguration/sms/codes")
public class SMSCodeHomeController {
	
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private SMSConfig smsConfig;
	
	@GetMapping
	public String showSMSCodesPage(@AuthenticationPrincipal User user, Model model) {
		if(smsConfig == null || smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		model.addAttribute("codes", smsCodesService.findByProviderNameOrderByPriceNetAsc(smsConfig.getProviderName()));
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
