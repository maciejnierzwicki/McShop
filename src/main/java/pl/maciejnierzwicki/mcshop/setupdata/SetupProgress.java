package pl.maciejnierzwicki.mcshop.setupdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Component
@Slf4j
public class SetupProgress {
	
	private SetupStage stage = SetupStage.WAITING_LOGIN;
	
	@Autowired
	private String setupFilePath;
	
	public String getCurrentStageUrlPath() {
		switch((stage != null) ? stage : SetupStage.WAITING_LOGIN) {
			case DATABASE_CONFIG: {
				return "/setup/database";
			}
			case APP_CONFIG: {
				return "/setup/appsettings";
			}
			case ADMIN_USER_CONFIG: {
				return "/setup/adminuser";
			}
			case FINISH: {
				return "/setup/finish";
			}
			case WAITING_LOGIN:
			default: {
				return "/setup";
			}
		}
	}
	
	public void saveToFile() throws Exception {
		File file;
		try {
			file = ResourceUtils.getFile(setupFilePath);
			try {
				if(!file.exists()) { 
					if(!file.createNewFile()) {
						throw new Exception("Cannot create setup file in location " + setupFilePath);
					}
				}
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
					writer.write(getStage().toString());
				}
			}
			catch(IOException e) { return; }
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	@PostConstruct
	public void loadFromFile() {
		File file;
		try {
			file = ResourceUtils.getFile(setupFilePath);
			try {
				if(!file.exists()) { 
					return;
				}
				String value = null;
				try(BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
					value = reader.readLine();
				}
				SetupStage stage = EnumUtils.getEnum(SetupStage.class, value);
				if(stage != null) { setStage(stage); }
			}
			catch(IOException e) { return; }
		} catch (FileNotFoundException e) {
			log.error(e.getMessage()); // -- cannot find file (it might not been created when setup mode is disabled)
		}
	}
	
	public void deleteFile() {
		File file;
		try {
			file = ResourceUtils.getFile(setupFilePath);
			if(!file.exists()) { 
				return;
			}
			if(!file.delete()) {
				throw new Exception("Cannot delete setup file");
			}
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

	}

}
