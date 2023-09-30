package pl.maciejnierzwicki.mcshop.web.form.admin.servermanagement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ServerConfigForm {
	
	@NotBlank(message = "Nazwa serwera nie może być pusta.")
	private String name;
	@NotBlank(message = "Adres nie może być pusty.")
	private String address;
	@Min(value = 0, message = "Nieprawidłowa wartość portu.")
	private Integer rconPort;
	@NotBlank(message = "Hasło nie może być puste.")
	private String rconPassword;

}
