package pl.maciejnierzwicki.mcshop.web.form;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class PageForm {
	
	@Min(value = 1)
	private Integer page = 1;

}
