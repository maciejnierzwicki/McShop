package pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class NewServiceForm {
	
	@NotBlank(message = "Nazwa usługi nie może być pusta.")
	protected String name;
	
	protected String description;
	
	@DecimalMin(value = "0.00", message = "Nieprawidłowa wartość ceny.")
	protected Double price;
	
	@DecimalMin(value = "0.00", message = "Nieprawidłowa wartość ceny.")
	protected Double priceBankTransfer;
	
	protected String category;
	
	protected String smsCode;
	
	protected String server;
	
	protected String commands;
	
	protected Boolean enabled = true;
}
