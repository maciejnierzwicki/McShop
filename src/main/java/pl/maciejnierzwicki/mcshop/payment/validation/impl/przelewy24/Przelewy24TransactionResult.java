package pl.maciejnierzwicki.mcshop.payment.validation.impl.przelewy24;

import lombok.Data;

@Data
public class Przelewy24TransactionResult {
	
	private Integer merchantId;
	private Integer posId;
	private String sessionId;
	private Integer amount;
	private Integer originAmount;
	private String currency;
	private Long orderId;
	private Integer methodId;
	private String statement;
	private String sign;

}
