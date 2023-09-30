package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.impl.dotpay;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay.DotPayConfig;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.BankTransferConfigForm;

@Data
@EqualsAndHashCode(callSuper = false)
public class DotPayConfigForm extends BankTransferConfigForm {
	
	@NotBlank(message = "Należy podać numer id sklepu DotPay.")
	private int shopId;
	
	public DotPayConfig toConfig() {
		DotPayConfig config = new DotPayConfig();
		config.setShopId(this.shopId);
		return config;
	}

}
