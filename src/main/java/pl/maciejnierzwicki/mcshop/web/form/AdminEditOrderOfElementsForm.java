package pl.maciejnierzwicki.mcshop.web.form;

import java.util.Map;

import lombok.Data;


@Data
public class AdminEditOrderOfElementsForm {
	
	private Map<Long, Integer> order;

}
