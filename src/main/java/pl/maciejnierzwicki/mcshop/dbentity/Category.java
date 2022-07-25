package pl.maciejnierzwicki.mcshop.dbentity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.web.form.admin.categorymanagement.CategoryForm;

@Entity
@Data
@Table(name = "Categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
