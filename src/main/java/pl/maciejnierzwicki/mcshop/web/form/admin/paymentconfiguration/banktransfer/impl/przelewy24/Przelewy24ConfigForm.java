package pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.impl.przelewy24;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay.DotPayConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.przelewy24.Przelewy24Config;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.BankTransferConfigForm;

@Data
@EqualsAndHashCode(callSuper = false)
public class Przelewy24ConfigForm extends BankTransferConfigForm {
	
	@NotBlank(message = "Należy podać numer id sklepu Przelewy24.")
	private int shopId;
	
	@NotBlank(message = "Należy podać klucz crc.")
	private String crc;
	
	public Przelewy24Config toConfig() {
		Przelewy24Config config = new Przelewy24Config();
		config.setShopId(this.shopId);
		config.setCrc(this.crc);
		return config;
	}

}
