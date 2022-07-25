package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration.banktransfer;

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

import pl.maciejnierzwicki.mcshop.McShopInitializer;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.BankTransferConfigForm;

@Controller
@RequestMapping("/admin/paymentconfiguration/banktransfer")
public class BankTransferConfigurationController {
	
	@Autowired
	private McShopInitializer initializer;
	@Autowired(required = false)
	private BankTransferConfig bankTransferConfig;
	
	@ModelAttribute(name = "bankTransferForm")
	public BankTransferConfigForm bankTransferForm() {
		if(bankTransferConfig == null) { return null; }
		return bankTransferConfig.toForm();
	}
	
	@GetMapping
	public String showBankTransferConfigPage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("bankTransferForm", bankTransferForm());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/banktransfer/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processBankTransferConfigPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("bankTransferForm") BankTransferConfigForm form, BindingResult result, Errors errors) throws Exception {
		if(bankTransferConfig == null) {
			throw new Exception("Bank tranfer payments are disabled.");
		}
		if(!errors.hasErrors()) {
			BankTransferConfig config = bankTransferConfig;
			config.apply(form);
			config.saveToFile();
		}
		
		model.addAttribute("bankTransferForm", bankTransferForm());
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/banktransfer/home");
		model.addAttribute("VIEW_NAME", "home");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
