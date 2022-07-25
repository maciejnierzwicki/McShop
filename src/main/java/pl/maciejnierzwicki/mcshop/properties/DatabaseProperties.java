package pl.maciejnierzwicki.mcshop.properties;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.setup.SetupDatabaseForm;

@Component
@Data
@Slf4j
public class DatabaseProperties {
	
	public enum DatabaseType {	MYSQL,	H2	}
	
	private DatabaseType databaseType = DatabaseType.H2;
	private String databaseAddress = "localhost";
	private String databaseName = "mcshop";
	private int databasePort = 3306;
	private String databaseUsername = "";
	private String databasePassword = "";
	private String databaseFilePath;
	
	private IPropertiesStorage propertiesStorage;
	
	@Autowired
	public DatabaseProperties(String databaseFilePath, IPropertiesStorage storage) {
		this.databaseFilePath = databaseFilePath;
		this.propertiesStorage = storage;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(databaseFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Properties props = propertiesStorage.load(databaseFilePath);
		if(props != null) {
			apply(props);
		}
	}
	
	public void apply(SetupDatabaseForm form) {
		this.databaseType = form.getDbtype();
		this.databaseAddress = form.getDbaddress();
		this.databaseName = form.getDbname();
		this.databasePort = form.getDbport();
		this.databaseUsername = form.getDbusername();
		this.databasePassword = form.getDbpassword();
	}
	
	public void apply(Properties properties) {
		this.databaseAddress = properties.getProperty("databaseAddress");
		this.databaseName = properties.getProperty("databaseName");
		if(this.databaseName == null || this.databaseName.isEmpty()) {
			this.databaseName = "forumapp";
		}
		this.databasePort = Integer.parseInt(properties.getProperty("databasePort"));
		this.databaseUsername = properties.getProperty("databaseUsername");
		this.databasePassword = properties.getProperty("databasePassword");
		if(properties.getProperty("databaseType") != null && !properties.getProperty("databaseType").isEmpty()) {
			DatabaseType type = EnumUtils.getEnum(DatabaseType.class, properties.getProperty("databaseType").toUpperCase());
			if(type != null) {
				this.databaseType = type;
			}
		}
	}
	
	public void save() {
		Properties props = new Properties();
		props.setProperty("databaseType", this.databaseType.toString());
		props.setProperty("databaseAddress", this.databaseAddress);
		props.setProperty("databaseName", this.databaseName);
		props.setProperty("databasePort", String.valueOf(this.databasePort));
		props.setProperty("databaseUsername", this.databaseUsername);
		props.setProperty("databasePassword", this.databasePassword);
		try {
			propertiesStorage.save(props, databaseFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
}
