package pl.maciejnierzwicki.mcshop.payment.validation.impl.microsms;

import lombok.Data;

@Data
public class MicroSMSResponse {
	
	private boolean connect;
	private MicroSMSResponseData data;

}
