package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SMSCodeForm {

	@Min(value = 0, message = "Należy podać prawidłową cenę.")
	private Double priceNet = 0.0;
	
	@Digits(fraction = 0, integer = 9, message = "Należy podać prawidłowy numer telefonu.")
	private Integer phoneNumber;

	@NotBlank(message = "Treść sms'a nie może być pusta.")
	private String message;
	
	@Min(value = 0, message = "Należy podać prawidłową kwotę.")
	private Double fundsToAdd = 0.0;

}
