package pl.maciejnierzwicki.mcshop.properties;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.SubpagesPropertiesForm;

@Component
@Data
@Slf4j
public class SubpagesProperties {
	
	private boolean rulesPageEnabled = true;
	private boolean contactPageEnabled = true;
	private boolean aboutPageEnabled = true;
	
	private String subpagesConfigFilePath;
	
	private IPropertiesStorage propertiesStorage;
	
	@Autowired
	public SubpagesProperties(String subpagesConfigFilePath, IPropertiesStorage propertiesStorage) {
		this.subpagesConfigFilePath = subpagesConfigFilePath;
		this.propertiesStorage = propertiesStorage;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(subpagesConfigFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Properties props = propertiesStorage.load(subpagesConfigFilePath);
		if(props != null) {
			apply(props);
		}
	}
	
	public void apply(SubpagesPropertiesForm form) {
		setRulesPageEnabled(form.isRulesPageEnabled());
		setContactPageEnabled(form.isContactPageEnabled());
		setAboutPageEnabled(form.isAboutPageEnabled());
	}
	
	public void apply(Properties properties) {
		this.rulesPageEnabled = Boolean.parseBoolean(properties.getProperty("rulesPageEnabled"));
		this.contactPageEnabled = Boolean.parseBoolean(properties.getProperty("contactPageEnabled"));
		this.aboutPageEnabled = Boolean.parseBoolean(properties.getProperty("aboutPageEnabled"));
	}
	
	public SubpagesPropertiesForm toForm() {
		SubpagesPropertiesForm form = new SubpagesPropertiesForm();
		form.setRulesPageEnabled(rulesPageEnabled);
		form.setContactPageEnabled(contactPageEnabled);
		form.setAboutPageEnabled(aboutPageEnabled);
		return form;
	}
	
	public void save() {
		Properties props = new Properties();
		props.setProperty("rulesPageEnabled", String.valueOf(rulesPageEnabled));
		props.setProperty("contactPageEnabled", String.valueOf(contactPageEnabled));
		props.setProperty("aboutPageEnabled", String.valueOf(aboutPageEnabled));
		try {
			propertiesStorage.save(props, subpagesConfigFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
}
