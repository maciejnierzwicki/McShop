package pl.maciejnierzwicki.mcshop.web.controller.account.addfunds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.account.AddFundsForm;
import pl.maciejnierzwicki.mcshop.web.form.account.AddFundsFormSMS;

@Controller
@RequestMapping("/account/addfunds")
public class AddFundsController {

	@Autowired(required = false)
	private SMSCodesService smsCodesService;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	
	@ModelAttribute(name = "fundsFormSMS")
	public AddFundsFormSMS fundsFormSMS() {
		return new AddFundsFormSMS();
	}
	
	@ModelAttribute(name = "fundsForm")
	public AddFundsForm fundsForm() {
		return new AddFundsForm();
	}
	
	@GetMapping
	public String showAddFundsPage(@AuthenticationPrincipal User user, Model model) {
		if(smsConfig != null && smsCodesService != null) {
			Iterable<SMSCode> smsCodes = smsCodesService.findAllByProviderNameAndFundsToAddIsGreaterThanOrderByFundsToAddAsc(smsConfig.getProviderName(), 0.0);
			model.addAttribute("smsCodes", smsCodes);
		}
		model.addAttribute("fundsForm", fundsForm());
		model.addAttribute("fundsFormSMS", fundsFormSMS());
		model.addAttribute("VIEW_FILE", "account/addfunds/home");
		model.addAttribute("VIEW_NAME", "addfunds");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}


}
