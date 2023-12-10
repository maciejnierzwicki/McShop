package pl.maciejnierzwicki.mcshop.web.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;



@Controller
@Slf4j
public class PageErrorController implements ErrorController {
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    if(status != null) {
		    log.debug("http status: " + status.toString());
	        Integer statusCode = Integer.valueOf(status.toString());
	        if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	log.debug("403 template");
	        	model.addAttribute("VIEW_FILE", "errors/403");
	        	model.addAttribute("VIEW_NAME", "403");
	        	return ControllerCommons.ROOT_VIEW_FILE_NAME;
	        }
	        else if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	log.debug("404 template");
	        	model.addAttribute("VIEW_FILE", "errors/404");
	        	model.addAttribute("VIEW_NAME", "404");
	        	return ControllerCommons.ROOT_VIEW_FILE_NAME;
	        }
	        
	    }
    	log.debug("errors template");
    	model.addAttribute("VIEW_FILE", "errors/error");
    	model.addAttribute("VIEW_NAME", "error");
    	return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}

}
