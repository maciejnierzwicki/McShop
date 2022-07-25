package pl.maciejnierzwicki.mcshop.web.form;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SubpagesPropertiesForm {
	
	private boolean rulesPageEnabled;
	private boolean contactPageEnabled;
	private boolean aboutPageEnabled;
	


}
