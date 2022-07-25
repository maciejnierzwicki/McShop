package pl.maciejnierzwicki.mcshop.properties.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Profile("prod")
@Slf4j
public class PropertiesStoragePROD implements IPropertiesStorage {
	
	/***
	 * 
	 * @param absoluteFilePath
	 * @return {@link Properties} or null if cannot find a file
	 */
	public Properties load(String filePath) {
		File file = new File(filePath);
		Properties props = new Properties();
		try(FileInputStream fis = new FileInputStream(file)) {
			props.load(fis);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return props;
	}
	
	/***
	 * 
	 * @param properties
	 * @param absoluteFilePath
	 * @throws FileNotFoundException 
	 */
	public void save(Properties properties, String absoluteFilePath) throws FileNotFoundException {
		File file = new File(absoluteFilePath);
		if(!file.exists()) { throw new FileNotFoundException("Cannot find target file to store properties"); }
		try(FileOutputStream fos = new FileOutputStream(file)) {
			properties.store(fos, null);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}
	
	public void copyFileFromJarIfNotExists(String filePath) throws Exception {
		File file = new File(filePath);
		if(file.exists()) {
			return;
		}
		try {
			if(!file.createNewFile()) {
				throw new Exception("Cannot create file in location " + filePath);
			}
			if(!filePath.startsWith("/")) {
				filePath = "/" + filePath;
			}
			InputStream is = getClass().getResourceAsStream(filePath);
			byte[] bytes = is.readAllBytes();
			is.close();
			try(FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(bytes);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}

}
