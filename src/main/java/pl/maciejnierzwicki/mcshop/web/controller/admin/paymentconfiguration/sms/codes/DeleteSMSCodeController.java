package pl.maciejnierzwicki.mcshop.web.controller.admin.paymentconfiguration.sms.codes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.event.EventBuilder;
import pl.maciejnierzwicki.mcshop.event.EventType;
import pl.maciejnierzwicki.mcshop.event.McShopEventPublisher;
import pl.maciejnierzwicki.mcshop.service.SMSCodesService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/admin/paymentconfiguration/sms/codes/delete")
public class DeleteSMSCodeController {
	
	@Autowired(required = false)
	private SMSCodesService smsCodesService;
	@Autowired(required = false)
	private ServicesService servicesService;
	@Autowired
	private McShopEventPublisher eventPublisher;
	
	@GetMapping
	public String showDeleteSMSCodePage(@AuthenticationPrincipal User user, Model model, @RequestParam("codeid") Long id) {
		if(smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		SMSCode sms_code = smsCodesService.getById(id);
		if(sms_code != null) {
			model.addAttribute("smsCode", sms_code);
		}
		model.addAttribute("id", id);
		model.addAttribute("VIEW_FILE", "admin/paymentconfiguration/sms/codes/deletecode");
		model.addAttribute("VIEW_NAME", "deletecode");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String processDeleteSMSCodePage(@AuthenticationPrincipal User user, Model model, @RequestParam("codeid") Long id) {
		if(smsCodesService == null) {
			return "redirect:/admin/paymentconfiguration/sms";
		}
		SMSCode sms_code = smsCodesService.getById(id);
		if(sms_code != null) {
			
			Iterable<Service> services = servicesService.getAllBySmsCode(sms_code);
			for(Service service : services) {
				service.setSmsCode(null);
				servicesService.save(service);
				//TODO: Consider making it configurable to send this event here, this may create lots of entries in eventlog at quick time
				eventPublisher.publishEvent(EventBuilder.forType(EventType.SERVICE_UPDATE_EVENT).toEvent());
			}
			
			smsCodesService.delete(sms_code);
		}
		return "redirect:/admin/paymentconfiguration/sms/codes";
	}

}
