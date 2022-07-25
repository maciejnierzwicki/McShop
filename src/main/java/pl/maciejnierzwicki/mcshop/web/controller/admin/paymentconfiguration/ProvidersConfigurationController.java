package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.McShopApplication;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferProvider;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;
import pl.maciejnierzwicki.mcshop.properties.PaymentProvidersProperties;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.PaymentProvidersPropertiesForm;

@Controller
@RequestMapping("/admin/paymentconfiguration/providers")
@Slf4j
public class ProvidersConfigurationController {
	
	@Autowired
	protected PaymentProvidersProperties providersProperties;
	@Autowired
	private ThreadPoolTaskScheduler scheduler;
	
	@ModelAttribute(name = "providersForm")
	public PaymentProvidersPropertiesForm providersForm() {
		return providersProperties.toForm();
	}
	
	@ModelAttribute("providersProperties")
	public PaymentProvidersProperties providersProperties() {
		return providersProperties;
	}
	
	@GetMapping
	public String showProvidersSelectPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("providersForm", providersForm());
		model.addAttribute("bankTransferProviders", BankTransferProvider.values());
		model.addAttribute("smsProviders", SMSProvider.values());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/providers");
		model.addAttribute("VIEW_NAME", "providers");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processProvidersSelectPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("providersForm") PaymentProvidersPropertiesForm form, BindingResult result, Errors errors) {
		log.debug("Has errors: " + errors.hasErrors());
		log.debug("Selected bank transfer provider: " + form.getBankTransferProvider());
		log.debug("Selected sms provider: " + form.getSmsProvider());
		if(!errors.hasErrors()) {
			providersProperties.apply(form);
			providersProperties.save();
			if(form.isRestartApp()) {
				scheduler.schedule(() -> { McShopApplication.restart(); }, new Date(System.currentTimeMillis() + 500));
			}
			
		}
		model.addAttribute("providersForm", providersForm());
		model.addAttribute("bankTransferProviders", BankTransferProvider.values());
		model.addAttribute("smsProviders", SMSProvider.values());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/providers");
		model.addAttribute("VIEW_NAME", "providers");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
