package pl.maciejnierzwicki.mcshop.config.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import pl.maciejnierzwicki.mcshop.properties.MainProperties;

/***
 * A HandlerInterceptor implementation which redirects any unwanted request to setup page when setup mode is enabled.<br>
 * Detailed configuration of included/excluded paths is specified in {@link WebConfig}.
 * @author Maciej Nierzwicki
 *
 */
@Component
public class AppSetupModeInterceptor implements HandlerInterceptor{
	
	private MainProperties properties;
	
	@Autowired
	public AppSetupModeInterceptor(@Lazy MainProperties properties) {
		this.properties = properties;
	}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
    	if(properties.isSetupMode()) {
	    	try {
				response.sendRedirect("/setup");
			} catch (IOException e) {}
    	}
        return true;
    }
}