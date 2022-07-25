package pl.maciejnierzwicki.mcshop.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.maciejnierzwicki.mcshop.dbentity.User;

public class AuthUtils {
	
	public static User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if(principal instanceof User) {
				User user = (User) auth.getPrincipal();
				return user;
			}
		}
		return null;
	}

}
