package pl.maciejnierzwicki.mcshop.web.form.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.customformvalidation.FieldMatch;

@Data
@FieldMatch(first = "newpassword", second = "confirmpassword", message = "Podane hasła muszą być identyczne.")
public class EditPasswordForm {
	
	@NotEmpty(message = "Hasło nie może być puste.")
	protected String oldPassword;
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String newPassword;
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String confirmPassword;

}
