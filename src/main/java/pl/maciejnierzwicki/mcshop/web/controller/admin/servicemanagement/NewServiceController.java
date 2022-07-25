package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.service.ServerService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.utils.StringUtils;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement.NewServiceForm;

@Controller
@RequestMapping("/admin/servicemanagement/newservice")
public class NewServiceController {
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	
	@ModelAttribute(name = "newService")
	public NewServiceForm newService() {
		return new NewServiceForm();
	}
	
	@GetMapping
	public String showNewServicePage(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("categories", categoryService.getAll());
		model.addAttribute("servers", serverService.getAll());
		Iterable<SMSCode> smsCodes;
		if(smsConfig != null) {
			smsCodes = smsCodesService.findByProviderNameOrderByPriceNetAsc(smsConfig.getProviderName());
		}
		else {
			smsCodes = new ArrayList<>();
		}
		model.addAttribute("smsCodes", smsCodes);
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/newservice");
		model.addAttribute("VIEW_NAME", "newservice");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processNewService(@AuthenticationPrincipal User user, Model model, @ModelAttribute("newService") @Valid NewServiceForm form, Errors errors) {
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/newservice");
		model.addAttribute("VIEW_NAME", "newservice");
		if(!errors.hasErrors()) {
			Service service = new Service();
			String category_id = form.getCategory();
			if(category_id != null && !category_id.equals("-1")) {
				Category cat = categoryService.getById(Long.parseLong(category_id));
				if(cat == null) {
					model.addAttribute("categorynotexist", true);
					return ControllerCommons.ROOT_VIEW_FILE_NAME;
				}
				service.setCategory(cat);
			}
			
			ServerConfig server = null;
			
			String server_id = form.getServer();
			if(server_id != null && !server_id.equals("-1")) {
				server = serverService.getById(Long.parseLong(server_id));
				if(server == null) {
					model.addAttribute("servernotexist", true);
					return ControllerCommons.ROOT_VIEW_FILE_NAME;
				}
				service.setServer(server);
			}
			
			SMSCode smsCode = null;
			
			String sms_id = form.getSmsCode();
			if(sms_id != null && !sms_id.equals("-1")) {
				smsCode = smsCodesService.getById(Long.parseLong(sms_id));
				if(smsCode == null) {
					model.addAttribute("smscodenotexist", true);
					return ControllerCommons.ROOT_VIEW_FILE_NAME;
				}
				service.setSmsCode(smsCode);
			}
			
			service.setName(form.getName());
			service.setDescription(form.getDescription());
			service.setPrice(form.getPrice());
			service.setPriceBankTransfer(form.getPriceBankTransfer());
			service.setCommands(StringUtils.toList(form.getCommands()));
			service.setEnabled(form.getEnabled());
			
			
			servicesService.save(service);
			eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_CREATE_EVENT).withActor(user).withService(service).toEvent());
			return "redirect:/admin/servicemanagement";
			
		}
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
