package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.impl.microsms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.mcshop.payment.config.sms.impl.microsms.MicroSMSConfig;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSConfigForm;

@Data
@EqualsAndHashCode(callSuper = false)
public class MicroSMSConfigForm extends SMSConfigForm {
	
	@NotNull(message = "Należy podać numer id partnera MicroSMS.")
	private int userId = 1;
	@NotBlank(message = "Należy podać numer id usługi SMS.")
	private int serviceId;

	@Override
	public MicroSMSConfig toConfig() {
		MicroSMSConfig config = new MicroSMSConfig();
		config.setUserId(userId);
		config.setServiceId(serviceId);
		return null;
	}

}
