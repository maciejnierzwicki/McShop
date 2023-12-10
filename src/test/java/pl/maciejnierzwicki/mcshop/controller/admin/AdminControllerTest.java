package pl.maciejnierzwicki.mcshop.controller.admin;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import pl.maciejnierzwicki.mcshop.properties.MainProperties;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MainProperties properties;
	
	@PostConstruct
	private void disableSetupMode() {
		properties.setSetupMode(false);
	}

	@Test
	void testAdminHomePageWhenNotLogged() throws Exception {
		mockMvc.perform(get("/admin"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	void testAdminHomePageWhenNotAuthorized() throws Exception {
		mockMvc.perform(get("/admin").with(user("TestUser").authorities(new SimpleGrantedAuthority("USER"))))
		.andExpect(status().isForbidden());
	}
	
	@Test
	void testAdminHomePageWhenAdminUser() throws Exception {
		mockMvc.perform(get("/admin").with(user("TestAdmin").authorities(new SimpleGrantedAuthority("ADMIN"))))
		.andExpect(status().isOk());
	}

}
