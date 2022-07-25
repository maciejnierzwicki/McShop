package pl.maciejnierzwicki.mcshop.web.form.order.payment;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class OrderPaymentMethodForm {
	
	@NotBlank(message = "Wartość nie może być pusta.")
	private String paymentMethod;

}
