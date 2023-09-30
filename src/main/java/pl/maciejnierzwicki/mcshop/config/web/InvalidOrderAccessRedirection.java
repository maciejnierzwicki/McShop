/**
 * 
 */
package pl.maciejnierzwicki.mcshop.config.web;

import java.util.Iterator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Order;
import pl.maciejnierzwicki.mcshop.dbentity.Service;

@Component
@Slf4j
public class InvalidOrderAccessRedirection implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		
		Iterator<String> it = request.getParameterNames().asIterator();
		while(it.hasNext()) {
			String attribute = it.next();
			log.debug("Existing parameter: " + attribute);
			if(attribute.equalsIgnoreCase("order")) {
				log.debug("Order parameter found");
				log.debug("Value: " + request.getParameter("order"));
				//log.debug("Order player name: " + order.getPlayerName());
				//Service service = order.getService();
				//log.debug("Order service null: " + (service == null));
			}
		}
		HttpSession session = request.getSession();
		if(session.getAttribute("order") == null) {
			log.debug("No order found in session attributes, redirecting to /");
			//response.sendRedirect("/");
			//return false;
		}
		return true;
	}
	
	

}
