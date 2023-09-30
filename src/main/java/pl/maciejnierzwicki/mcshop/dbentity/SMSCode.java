package pl.maciejnierzwicki.mcshop.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSCodeForm;

@Entity
@Data
@NoArgsConstructor
@Table(name = "SmsCodes")
public class SMSCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "price_net")
	private double priceNet;
	
	@Column(name = "phone_number")
	private int phoneNumber;
	
	@Column(name = "text")
	private String message;
	
	@Column(name = "funds_to_add")
	private double fundsToAdd;
	
	@Column(name = "provider")
	private SMSProvider providerName;
	
	public SMSCode(double priceNet, int phoneNumber, String message, double fundsToAdd) {
		this.priceNet = priceNet;
		this.phoneNumber = phoneNumber;
		this.message = message;
		this.fundsToAdd = fundsToAdd;
	}
	
	public SMSCodeForm toForm() {
		SMSCodeForm form = new SMSCodeForm();
		form.setPriceNet(priceNet);
		form.setPhoneNumber(phoneNumber);
		form.setMessage(message);
		form.setFundsToAdd(fundsToAdd);
		return form;
	}
	
	public void apply(SMSCodeForm form, SMSProvider smsProviderName) {
		this.priceNet = form.getPriceNet();
		this.phoneNumber = form.getPhoneNumber();
		this.message = form.getMessage();
		this.fundsToAdd = form.getFundsToAdd();
		this.providerName = smsProviderName;
	}

}
