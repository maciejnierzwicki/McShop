package pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AdminEditServiceForm {
	
	@NotBlank(message = "Nazwa usługi nie może być pusta.")
	private String name;
	
	private String category;
	
	private String smsCode;
	
	private String server;
	
	private String description;
	
	@DecimalMin(value = "0.00", message = "Nieprawidłowa wartość ceny.")
	private Double price;
	
	@DecimalMin(value = "0.00", message = "Nieprawidłowa wartość ceny.")
	private Double priceBankTransfer;
	
	private String commands;
	
	private Boolean enabled;
	
	

}
