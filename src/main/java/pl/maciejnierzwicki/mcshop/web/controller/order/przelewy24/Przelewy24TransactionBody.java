package pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24;

import lombok.Data;

@Data
public class Przelewy24TransactionBody {
	
	private Integer merchantId;
	private Integer posId;
	private String sessionId;
	private Integer amount;
	private String currency;
	private String description;
	private String email;
	private String country;
	private String language;
	private String urlReturn;
	private String urlStatus;
	private String sign;

}
