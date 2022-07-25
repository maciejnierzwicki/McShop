package pl.maciejnierzwicki.mcshop.properties;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.MainPropertiesForm;

@Component
@Data
@Slf4j
public class MainProperties {
	
	private String siteTitle = "McShop App";
	private String siteUrl = "localhost";
	private boolean setupMode = true;
	private String playerPlaceholder = "%nick%";
	private int maxOrdersPerPage = 10;
	private int maxEventsPerPage = 10;
	private String dateFormat = "dd.MM.yyyy HH:mm";
	private String eventDateFormat = "dd.MM.yyyy HH:mm:ss";
	private boolean serversColumn = true;
	
	private String mainPropertiesFilePath;
	private IPropertiesStorage propertiesStorage;
	

	@Autowired
	public MainProperties(String mainPropertiesFilePath, IPropertiesStorage storage) {
		this.mainPropertiesFilePath = mainPropertiesFilePath;
		this.propertiesStorage = storage;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(mainPropertiesFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Properties props = propertiesStorage.load(mainPropertiesFilePath);
		if(props != null) {
			apply(props);
		}
	}
	
	public void apply(MainPropertiesForm form) {
		this.siteTitle = form.getSiteTitle();
		this.siteUrl = form.getSiteUrl();
		this.setupMode = form.isSetupMode();
		this.playerPlaceholder = form.getPlayerPlaceholder();
		this.maxOrdersPerPage = form.getMaxOrdersPerPage();
		this.maxEventsPerPage = form.getMaxEventsPerPage();
		this.dateFormat = form.getDateFormat();
		this.eventDateFormat = form.getEventDateFormat();
		this.serversColumn = form.isServersColumn();
	}
	
	public void apply(Properties properties) {
		this.siteTitle = properties.getProperty("siteTitle");
		this.siteUrl = properties.getProperty("siteURL");
		this.playerPlaceholder = properties.getProperty("playerPlaceholder");
		this.serversColumn = Boolean.parseBoolean(properties.getProperty("serversColumn"));
		this.setupMode = Boolean.parseBoolean(properties.getProperty("setupMode"));
		
		try {
			this.maxOrdersPerPage = Integer.parseInt(properties.getProperty("maxOrdersPerPage"));
		}
		catch(NumberFormatException e) {
			this.maxOrdersPerPage = 10;
		}
		try {
			this.maxEventsPerPage = Integer.parseInt(properties.getProperty("maxEventsPerPage"));
		}
		catch(NumberFormatException e) {
			this.maxEventsPerPage = 10;
		}
		
		this.dateFormat = properties.getProperty("dateFormat");
		if(this.dateFormat == null || this.dateFormat.isEmpty()) {
			this.dateFormat = "dd.MM.yyyy HH:mm";
		}
		this.eventDateFormat = properties.getProperty("eventDateFormat");
		if(this.eventDateFormat == null || this.eventDateFormat.isEmpty()) {
			this.eventDateFormat = "dd.MM.yyyy HH:mm:ss";
		}
	}
	
	public MainPropertiesForm toForm() {
		MainPropertiesForm form = new MainPropertiesForm();
		form.setSiteTitle(getSiteTitle());
		form.setSetupMode(isSetupMode());
		form.setPlayerPlaceholder(getPlayerPlaceholder());
		form.setMaxOrdersPerPage(getMaxOrdersPerPage());
		form.setMaxEventsPerPage(getMaxEventsPerPage());
		form.setDateFormat(getDateFormat());
		form.setEventDateFormat(getEventDateFormat());
		form.setServersColumn(isServersColumn());
		form.setSiteUrl(getSiteUrl());
		return form;
	}
	
	public void save() {
		Properties props = new Properties();
		props.setProperty("siteTitle", this.siteTitle);
		props.setProperty("siteURL", this.siteUrl);
		props.setProperty("setupMode", String.valueOf(this.setupMode));
		props.setProperty("serversColumn", String.valueOf(this.serversColumn));
		props.setProperty("playerPlaceholder", this.playerPlaceholder);
		props.setProperty("maxOrdersPerPage", String.valueOf(this.maxOrdersPerPage));
		props.setProperty("maxEventsPerPage", String.valueOf(this.maxEventsPerPage));
		props.setProperty("dateFormat", this.dateFormat);
		props.setProperty("eventDateFormat", this.eventDateFormat);
		try {
			propertiesStorage.save(props, mainPropertiesFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
}
