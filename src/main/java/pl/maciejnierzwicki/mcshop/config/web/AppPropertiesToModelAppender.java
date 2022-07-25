package pl.maciejnierzwicki.mcshop.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.properties.SubpagesProperties;

/***
 * A HandlerInterceptor implementation which gives access to commonly used app properties through model attributes.<br>
 * Detailed configuration of included/excluded paths is specified in {@link WebConfig}.
 * @author Maciej Nierzwicki
 *
 */
@Component
public class AppPropertiesToModelAppender implements HandlerInterceptor {
	
	@Autowired
	protected MainProperties properties;
	@Autowired
	protected SubpagesProperties subpagesProperties;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null) { return; }
		modelAndView.addObject("properties", properties);
		modelAndView.addObject("subpagesProperties", subpagesProperties);
	}

}
