package pl.maciejnierzwicki.mcshop.web.form.setup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.properties.DatabaseProperties.DatabaseType;

@Data
public class SetupDatabaseForm {
	
	@NotNull(message = "Wymagany jest typ bazy danych.")
	private DatabaseType dbtype = DatabaseType.H2;
	private String dbaddress;
	private String dbname;
	@Min(value=0, message = "Nieprawidłowy port.")
	@Max(value=Integer.MAX_VALUE, message= "Nieprawidłowy port.")
	private int dbport;
	private String dbusername;
	private String dbpassword;

}
