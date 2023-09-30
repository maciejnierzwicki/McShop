package pl.maciejnierzwicki.mcshop.web.form.admin.categorymanagement;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CategoryForm {
	
	@NotBlank(message = "Nazwa kategorii nie może być pusta.")
	private String name;
}
