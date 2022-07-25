package pl.maciejnierzwicki.mcshop.web.controller.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.ServiceValidator;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.repository.ServiceRepository;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@Controller
@RequestMapping("/service")
@SessionAttributes("order")
@Slf4j
public class ServiceController {
	
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private ServiceValidator serviceValidator;

	public Order order() {
		log.debug("new plain order object created");
		return new Order(OrderType.SERVICE_ORDER);
	}

	
	@GetMapping(path = "/{id}")
	public String getService(@PathVariable("id") Long id, Model model, SessionStatus status) {
		Optional<Service> op_service = serviceRepo.findById(id);
		if(op_service.isPresent()) {
			Order order = order();
			Service service = op_service.get();
			order.setService(service);
			model.addAttribute("service", service);
			model.addAttribute("order", order);
			model.addAttribute("hasAnyWorkingPaymentMethod", serviceValidator.hasAnyWorkingPaymentMethod(service));
		}
		model.addAttribute("VIEW_FILE", "service");
		model.addAttribute("VIEW_NAME", "service");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
}
