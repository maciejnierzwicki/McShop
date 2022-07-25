package pl.maciejnierzwicki.mcshop.controller.setup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import pl.maciejnierzwicki.mcshop.properties.MainProperties;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SetupControllerTest {
		
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MainProperties properties;
	
	@Test
	void testSetupPageWhenNotInSetupMode() throws Exception {
		properties.setSetupMode(false);
		mockMvc.perform(get("/setup"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	void testSetupPageWhenInSetupMode() throws Exception {
		properties.setSetupMode(true);
		mockMvc.perform(get("/setup"))
		.andExpect(status().isOk());
		properties.setSetupMode(false);
	}

}
