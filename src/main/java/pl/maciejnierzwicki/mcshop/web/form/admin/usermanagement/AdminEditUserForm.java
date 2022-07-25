package pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AdminEditUserForm {
	
	@NotBlank(message = "Nazwa użytkownika nie może być pusta.")
	@Size(min = 4, message = "Nazwa użytkownika musi składać się z co najmniej 4 znaków.")
	private String username;
	
	@Size(min = 1, message = "Użytkownik musi należeć do co najmniej jednej grupy.")
	private List<String> activeRoles;
	
	@Min(value = 0, message = "Wartość nie może być mniejsza od 0.")
	private Double money = 0.0;

}
