package pl.maciejnierzwicki.mcshop.web.controller.admin.servicemanagement;

import java.util.ArrayList;

import jakarta.validation.Valid;

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

import lombok.extern.slf4j.Slf4j;
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
import pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement.AdminEditServiceForm;

@Controller
@RequestMapping("/admin/servicemanagement/editservice")
@Slf4j
public class EditServiceController {
	
	@Autowired
	private ServicesService servicesService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SMSCodesService smsCodesService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	@Autowired(required = false)
	private SMSConfig smsConfig;
	
	@GetMapping
	public String showEditItemPage(@AuthenticationPrincipal User user, Model model, @RequestParam("serviceid") Long service_id) {
		model.addAttribute("serviceid", service_id);
		Service service = servicesService.getById(service_id);
		
		if(service != null) {
			model.addAttribute("editService", service.toAdminEditServiceForm());
		}
		
		Iterable<Category> categories = categoryService.getAll();
		model.addAttribute("categories", categories);
		
		Iterable<SMSCode> smsCodes;
		if(smsConfig != null) {
			smsCodes = smsCodesService.findByProviderNameOrderByPriceNetAsc(smsConfig.getProviderName());
		}
		else {
			smsCodes = new ArrayList<>();
		}
		model.addAttribute("smsCodes", smsCodes);
		
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/editservice");
		model.addAttribute("VIEW_NAME", "editservice");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	
	
	@PostMapping
	public String processServiceEditForm(@AuthenticationPrincipal User user, Model model, @ModelAttribute("editService") @Valid AdminEditServiceForm form, BindingResult result, Errors errors, @RequestParam("serviceid") Long service_id) {
		if(!errors.hasErrors()) {
			Service service = servicesService.getById(service_id);
			if(service == null) {
				log.error("SERVICE DOES NOT EXIST, REDIRECTING TO SERVICE MANAGEMENT PAGE");
				return "redirect:/admin/servicemanagement";
			}
			
			
			/** CATEGORY CHECKS **/
			Category cat = null;
			String cat_id = form.getCategory();
			if(cat_id != null && !cat_id.equals("-1")) {
				cat = categoryService.getById(Long.parseLong(cat_id));
				if(cat == null) {
					model.addAttribute("categorynotexist", true);
				}
			}
			
			/** SERVER CHECKS **/
			ServerConfig server = null;
			String server_id = form.getServer();
			if(server_id != null && !server_id.equals("-1")) {
				server = serverService.getById(Long.parseLong(server_id));
				if(server == null) {
					model.addAttribute("servernotexist", true);
				}
			}
			
			/** SMS CODE CHECKS **/
			SMSCode smsCode = null;
			String sms_id = form.getSmsCode();
			if(sms_id != null && !sms_id.equals("-1")) {
				smsCode = smsCodesService.getById(Long.parseLong(sms_id));
				if(smsCode == null) {
					model.addAttribute("smscodenotexist", true);
				}
			}
			
			if(service != null && server != null && cat != null) {
				service.setName(form.getName());
				service.setDescription(form.getDescription());
				service.setPrice(form.getPrice());
				service.setPriceBankTransfer(form.getPriceBankTransfer());
				service.setCategory(cat);
				service.setSmsCode(smsCode);
				service.setServer(server);
				service.setCommands(StringUtils.toList(form.getCommands()));
				service.setEnabled(form.getEnabled());
				servicesService.save(service);
				eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_UPDATE_EVENT).withActor(user).withService(service).toEvent());
			}
			model.addAttribute("editService", service.toAdminEditServiceForm());
		}
		
		else {
			model.addAttribute("editService", form);
		}
		
		fillModelWithServiceData(model, service_id);
		model.addAttribute("VIEW_FILE", "admin/servicemanagement/editservice");
		model.addAttribute("VIEW_NAME", "editservice");
		Iterable<Category> categories = categoryService.getAll();
		model.addAttribute("categories", categories);
		Iterable<ServerConfig> servers = serverService.getAll();
		model.addAttribute("servers", servers);
		Iterable<SMSCode> smsCodes;
		if(smsConfig != null) {
			smsCodes = smsCodesService.findByProviderNameOrderByPriceNetAsc(smsConfig.getProviderName());
		}
		else {
			smsCodes = new ArrayList<>();
		}
		model.addAttribute("smsCodes", smsCodes);
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	
	private void fillModelWithServiceData(Model model, long service_id) {
		model.addAttribute("serviceid", service_id);
	}

}
