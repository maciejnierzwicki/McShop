package pl.maciejnierzwicki.mcshop.web.controller.order.przelewy24;

import lombok.Data;

@Data
public class Przelewy24VerificationBody {
	
	private Integer merchantId;
	private Integer posId;
	private String sessionId;
	private Integer amount;
	private String currency;
	private Long orderId;
	private String sign;

}
