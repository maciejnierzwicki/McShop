package pl.maciejnierzwicki.mcshop.config.web;

import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.service.ServerService;

/***
 * A HandlerInterceptor implementation which gives access to server list through model attributes.<br>
 * Detailed configuration of included/excluded paths is specified in {@link WebConfig}.
 * @author Maciej Nierzwicki
 *
 */
@Component
public class ServerListToModelAppender implements HandlerInterceptor {
	
	private ServerService serverService;
	
	private Iterable<ServerConfig> servers = new ArrayList<>();
	
	@Autowired
	public ServerListToModelAppender(ServerService serverService) {
		this.serverService = serverService;
		updateServers();
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null) { return; }
		modelAndView.addObject("servers", servers);
	}
	
	public void updateServers() {
		this.servers = serverService.getAllOrderedByPositionHighest();
	}

}
