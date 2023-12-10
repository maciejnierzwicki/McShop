package pl.maciejnierzwicki.mcshop.controller.account;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MainProperties properties;

	@PostConstruct
	private void disableSetupMode() {
		properties.setSetupMode(false);
	}
	
	@Test
	void testAccountHomePageWhenNotLogged() throws Exception {
		mockMvc.perform(get("/account"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	void testAccountHomePageWhenLogged() throws Exception {
		User user = new User("TestUser", "1234");
		mockMvc.perform(get("/account").with(user(user)))
		.andExpect(status().isOk())
		.andExpect(view().name(ControllerCommons.ROOT_VIEW_FILE_NAME));
	}

}
