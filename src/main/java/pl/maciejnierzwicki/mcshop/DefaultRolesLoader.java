package pl.maciejnierzwicki.mcshop;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.service.RoleService;

@Component(value = "DefaultRolesLoader")
public class DefaultRolesLoader {
	
	@Autowired
	private RoleService roleService;
	
	@PostConstruct
	private void loadDefaualtRoles() {
		createRoleIfNotExists("USER", "Użytkownik");
		createRoleIfNotExists("ADMIN", "Administrator");
	}
	
	private void createRoleIfNotExists(String id, String displayName) {
		Role role = roleService.getById(id);
		if(role == null) {
			role = new Role(id);
			role.setDisplayname(displayName);
			roleService.save(role);
		}
	}

}
