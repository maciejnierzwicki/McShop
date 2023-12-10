package pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24;

import lombok.Data;
import java.util.Map;

@Data
public class Przelewy24TransactionResponse {
	
	private Map<String, String> data;
	private Integer responseCode;

}
