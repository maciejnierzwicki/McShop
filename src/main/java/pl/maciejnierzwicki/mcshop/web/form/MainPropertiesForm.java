package pl.maciejnierzwicki.mcshop.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.customformvalidation.ValidDateTimeFormat;

@Component
@Data
public class MainPropertiesForm {
	
	@NotBlank(message = "Tytuł strony nie może składać się wyłącznie z białych spacji.")
	@Size(max = 40, message = "Tytuł strony może składać się z maksymalnie 40 znaków.")
	private String siteTitle = "MCShop App";
	
	@NotBlank(message = "Adres strony nie może składać się wyłącznie z białych spacji.")
	private String siteUrl = "localhost";
	
	@NotBlank(message = "Wartość domyślnej grupy nie może być pusta.")
	private String defaultUserRole = "USER";
	
	private boolean registrationsEnabled = true;
	private boolean setupMode = true;
	private boolean serversColumn = true;
	
	@NotBlank(message = "Wartość znacznika gracza nie może być pusta.")
	private String playerPlaceholder;
	
	@Min(value = 1, message = "Wartość nie może być mniejsza niż 1.")
	private Integer maxOrdersPerPage = 10;
	@Min(value = 1, message = "Wartość nie może być mniejsza niż 1.")
	private Integer maxEventsPerPage = 10;
	
	private boolean playerPlaceholderUpdateServices = true;
	
	@ValidDateTimeFormat
	private String dateFormat = "dd.MM.yyyy HH:mm";
	@ValidDateTimeFormat
	private String eventDateFormat = "dd.MM.yyyy HH:mm:ss";
	


}
