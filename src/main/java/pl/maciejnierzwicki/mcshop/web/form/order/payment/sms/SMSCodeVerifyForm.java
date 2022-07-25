package pl.maciejnierzwicki.mcshop.web.form.order.payment.sms;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SMSCodeVerifyForm {
	
	@NotBlank(message = "Należy podać kod zwrotny sms-a.")
	private String code;

}
