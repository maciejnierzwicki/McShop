package pl.maciejnierzwicki.mcshop;

import java.util.HashSet;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.Role;
import pl.maciejnierzwicki.mcshop.dbentity.Service;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.CategoryService;
import pl.maciejnierzwicki.mcshop.service.OrderService;
import pl.maciejnierzwicki.mcshop.service.RoleService;
import pl.maciejnierzwicki.mcshop.service.ServicesService;
import pl.maciejnierzwicki.mcshop.service.UserService;

@Component
@Slf4j
public class ExampleDataLoader {
	
	@Autowired @Lazy
	private RoleService roleService;
	@Autowired @Lazy
	private UserService userService;
	@Autowired @Lazy
	private CategoryService categoryService;
	@Autowired @Lazy
	private ServicesService servicesService;
	@Autowired @Lazy
	private OrderService orderService;
	@Autowired @Lazy
	private PasswordEncoder passwordEncoder;
	@Autowired @Lazy
	private MainProperties properties;
	
	
	@PostConstruct
	public void createExampleDataIfNone() {
		  	  log.debug("Creating example data if not found any.");
		  	  
		  	  if(categoryService.countAll() == 0 && servicesService.countAll() == 0) {
		  	  	  Category cat1 = new Category();
		  	  	  cat1.setName("Pakiety");
		  	  	  Category cat2 = new Category();
		  	  	  cat2.setName("Jajka spawnujące");
		  	  	  categoryService.save(cat1);
		  	  	  categoryService.save(cat2);
		  	  	  
		  	  	  Service item1 = new Service();
		  	  	  item1.setName("Pakiet SuperVIP");
		  	  	  item1.setCategory(cat1);
		  	  	  item1.setDescription("Najlepszy z pakietów.");
		  	  	  item1.setPriceBankTransfer(28.00);
		  	  	  
		  	  	  Service item2 = new Service();
		  	  	  item2.setName("Jajka świni");
		  	  	  item2.setCategory(cat2);
		  	  	  item2.setDescription("10 jaj spawnujących świnię.");
		  	  	  item2.setPriceBankTransfer(4.00);
		  	  	  
		  	  	  servicesService.save(item1);
		  	  	  servicesService.save(item2);
		  	  	  
		  	  }
	  	  /*
	  	  User admin = userService.getByUsername("Admin");
	  	  
	  	  if(admin == null) {
	  	  	  Role admin_role = roleService.getById("ADMIN");
	  	  	  Role user_role = roleService.getById("USER");
	  	  	  
	  	  	  Set<Role> roles = new HashSet<>();
	  	  	  roles.add(user_role);
	  	  	  roles.add(admin_role);
	  	  	  admin = new User("Admin", passwordEncoder.encode("1234"));
	  	  	  admin.setRoles(roles);
	  	  	  userService.save(admin);  
	  	  }
	  	  */
	  	  
		}

}
