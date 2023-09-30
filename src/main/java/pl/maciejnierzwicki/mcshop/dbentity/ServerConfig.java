package pl.maciejnierzwicki.mcshop.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.web.form.admin.servermanagement.ServerConfigForm;

@Data
@Entity
@Table(name = "Servers")
public class ServerConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String address;
	@Column(name = "rcon_port")
	private Integer rconPort;
	@Column(name = "rcon_password")
	private String rconPassword;
	
	private Integer position = 1;
	
	public ServerConfig() {}
	
	public ServerConfig(String name, String address, Integer rconPort, String rconPassword) {
		this.name = name;
		this.address = address;
		this.rconPort = rconPort;
		this.rconPassword = rconPassword;
	}
	
	public ServerConfigForm toServerConfigForm() {
		ServerConfigForm form = new ServerConfigForm();
		form.setName(name);
		form.setAddress(address);
		form.setRconPassword(rconPassword);
		form.setRconPort(rconPort);
		return form;
	}
}
