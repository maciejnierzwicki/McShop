package pl.maciejnierzwicki.mcshop.web.form.admin.usermanagement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.customformvalidation.FieldMatch;
import pl.maciejnierzwicki.mcshop.dbentity.User;

@Data
@FieldMatch(first = "password", second = "confirmpassword", message = "Podane hasła muszą być identyczne.")
public class NewUserForm {
	
	@NotBlank(message = "Nazwa użytkownika nie może być pusta.")
	@Size(min = 4, message = "Nazwa użytkownika musi składać się z co najmniej 4 znaków.")
	protected String username;
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String password;
	
	@NotEmpty(message = "Hasło nie może być puste.")
	@Size(min = 5, message = "Hasło musi składać się z co najmniej 5 znaków.")
	protected String confirmpassword;
	
	public User toUser(PasswordEncoder encoder) {
		User user = new User(username, encoder.encode(password));
		return user;
	}
}
