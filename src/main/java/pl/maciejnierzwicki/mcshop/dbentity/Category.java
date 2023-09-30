package pl.maciejnierzwicki.mcshop.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.web.form.admin.categorymanagement.CategoryForm;

@Entity
@Data
@Table(name = "Categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String name;
	private Integer position = 1;
	
	public CategoryForm toCategoryForm() {
		CategoryForm form = new CategoryForm();
		form.setName(getName());
		return form;
	}

}
