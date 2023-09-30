package pl.maciejnierzwicki.mcshop.dbentity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.utils.MultilineStringConverter;
import pl.maciejnierzwicki.mcshop.utils.StringUtils;
import pl.maciejnierzwicki.mcshop.web.form.admin.servicemanagement.AdminEditServiceForm;

@Entity
@Data
@Table(name = "Services")
public class Service {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String name;
	private String description;
	
	private Double price;
	
	@Column(name = "price_bank_transfer")
	private Double priceBankTransfer;
	
	@OneToOne
	@JoinColumn(name = "sms_id", referencedColumnName = "id")
	private SMSCode smsCode;
	
	@OneToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	@OneToOne
	@JoinColumn(name = "server_id", referencedColumnName = "id")
	private ServerConfig server;
	
	@Convert(converter = MultilineStringConverter.class)
	private List<String> commands = new ArrayList<>();
	
	private Integer position = 1;
	
	private Boolean enabled = true;
	
	public AdminEditServiceForm toAdminEditServiceForm() {
		AdminEditServiceForm form = new AdminEditServiceForm();
		form.setName(getName());
		form.setDescription(getDescription());
		if(getCategory() != null) {
			form.setCategory(getCategory().getId().toString());
		}
		if(getServer() != null) {
			form.setServer(getServer().getId().toString());
		}
		if(getSmsCode() != null) {
			form.setSmsCode(getSmsCode().getId().toString());
		}
		form.setPrice(getPrice());
		form.setPriceBankTransfer(getPriceBankTransfer());
		form.setCommands(StringUtils.toString(getCommands()));
		form.setEnabled(getEnabled());
		return form;
	}
	
}
